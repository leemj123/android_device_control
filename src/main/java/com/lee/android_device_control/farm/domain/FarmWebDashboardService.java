package com.lee.android_device_control.farm.domain;

import com.lee.android_device_control.farm.dto.dashboard.Metrics;
import com.lee.android_device_control.farm.dto.dashboard.MetricsResponse;
import com.lee.android_device_control.farm.dto.dashboard.Sensor;
import com.lee.android_device_control.farm.dto.dashboard.SnapshotResponse;
import com.lee.android_device_control.farm.infra.QSensorMetrics;
import com.lee.android_device_control.farm.infra.SensorMetrics;
import com.lee.android_device_control.farm.infra.SensorMetricsRepo;
import com.mysema.commons.lang.Pair;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FarmWebDashboardService {

    private final SensorMetricsRepo sensorMetricsRepo;
    private final JPAQueryFactory jpaQueryFactory;
    private final QSensorMetrics qSensorMetrics = QSensorMetrics.sensorMetrics;

    public ResponseEntity<?> sensorSnapShot() {

        // 0번째 페이지에서 2개를 가져오되, createTime 기준 내림차순(최신순) 정렬
        List<SensorMetrics> latestMetrics = jpaQueryFactory
                .selectFrom(qSensorMetrics)
                .limit(2)
                .orderBy(qSensorMetrics.id.desc())
                .fetch();

        if (latestMetrics.isEmpty()) {
            return ResponseEntity.ok().build();
        }
        SensorMetrics sm = latestMetrics.get(0);
        SensorMetrics pm = latestMetrics.get(1);

        SnapshotResponse snapshotResponse = SnapshotResponse.builder()
                .updatedAt(sm.getCreateTime().toInstant(ZoneOffset.UTC).toEpochMilli())
                .metrics(
                        Metrics.builder()
                                .temperature(Sensor.builder()
                                        .value(sm.getAirTempValue())
                                        .previous(pm.getAirTempValue())
                                        .build())
                                .humidity(Sensor.builder()
                                        .value(sm.getHumidityValue())
                                        .previous(pm.getHumidityValue())
                                        .build())
                                .lux(Sensor.builder()
                                        .value(sm.getLuxValue())
                                        .previous(pm.getLuxValue())
                                        .build())
                                .water_temp(Sensor.builder()
                                        .value(sm.getWaterTempValue())
                                        .previous(pm.getWaterTempValue())
                                        .build())
                                .led_state(true)
                                .pump_state(false)
                                .build()
                )
                .devices(
                        new ArrayList<>()
                )
                .build();
        return ResponseEntity.ok(snapshotResponse);
    }

    public ResponseEntity<?> sensorMetrics(
            String target,
            String range,
            String unit,
            String kind,
            NumberPath<? extends Number> sourceColumn // .avg()를 빼고 원본 경로(Path) 자체를 받습니다.
    ) {
        DateTimeTemplate<LocalDateTime> rangeTemplate = this.groupOfTime(range);

        // SELECT 절에서만 집계 함수(.avg())를 적용합니다.
        NumberExpression<Double> avgExpression = sourceColumn.avg();

        List<Tuple> tuples = jpaQueryFactory
                .select(
                        rangeTemplate,
                        avgExpression // SELECT 절에는 평균값 수식을 넣음
                )
                .from(qSensorMetrics)
                .groupBy(rangeTemplate) // GROUP BY 절에는 오직 시간 템플릿만 명확하게 지정
                .orderBy(rangeTemplate.asc())
                .fetch();

        List<MetricsResponse.MetricsPoint> points = tuples.stream()
                .map(tuple -> {
                    LocalDateTime time = tuple.get(rangeTemplate);
                    long timeMile = time != null ? time.toInstant(ZoneOffset.UTC).toEpochMilli() : 0L;

                    // 결과 추출 시 avgExpression을 사용
                    Double rawValue = tuple.get(avgExpression);
                    Float value = rawValue != null ? rawValue.floatValue() : null;

                    return new MetricsResponse.MetricsPoint(timeMile, value);
                })
                .toList();

        long startTime = 0L;
        long endTime = 0L;
        if (!points.isEmpty()) {
            startTime = points.get(0).t();
            endTime = points.get(points.size() - 1).t();
        }

        int minutesValue = this.getBucketMinutes(range);

        MetricsResponse metricsResponse = MetricsResponse.builder()
                .metricId(target)
                .rangeId(range)
                .kind(kind)
                .unit(unit)
                .precision(1)
                .axis( minutesValue > 60 ? "date" : "time")
                .bucketMinutes(minutesValue)
                .start(startTime)
                .end(endTime)
                .points(points)
                .build();

        return ResponseEntity.ok(metricsResponse);
    }
    // 분기 로직을 별도 메서드로 추출하여 가독성 및 유지보수성 향상
    private int getBucketMinutes(String range) {
        return switch (range) {
            case "10m" -> 10;
            case "1d", "1day" -> 1440; // 1day 케이스 누락 방지
            default -> 60;
        };
    }

//    public ResponseEntity<?> sensorMetrics(String target, String range, String unit, String kind, Expression<?> targetColumn) {
//
//        DateTimeTemplate<LocalDateTime> rangeTemplate = this.groupOfTime(range);
//
//        List<Tuple> tuples = jpaQueryFactory
//                .select(
//                        rangeTemplate,
//                        targetColumn // 대다수 그룹바이 쿼리 특성상 평균/합산 등 집계함수 권장
//                )
//                .from(qSensorMetrics)
//                .groupBy(rangeTemplate)
//                .orderBy(rangeTemplate.desc())
//                .fetch();
//
//        List<MetricsResponse.MetricsPoint> points = tuples.stream()
//                .map(tuple -> {
//                    long timeMile = tuple.get(rangeTemplate).toInstant(ZoneOffset.UTC).toEpochMilli();
//                    Float value = tuple.get(targetColumn) != null ? ((Number) tuple.get(targetColumn)).floatValue() : null;
//                    return new MetricsResponse.MetricsPoint(timeMile, value);
//                })
//                .toList();
//        long startTime = 0L;
//        long endTime = 0L;
//
//        if (!points.isEmpty()) {
//            // 현재 쿼리가 DESC(내림차순) 정렬이므로:
//            // 리스트의 마지막 원소(size - 1)가 가장 과거의 시간 = start
//            // 리스트의 첫 번째 원소(0)가 가장 최근의 시간 = end
//            startTime = points.get(points.size() - 1).t(); // DTO의 getter명(예: getTime())에 맞춰 호출하세요
//            endTime = points.get(0).t();
//        }
//        MetricsResponse metricsResponse = MetricsResponse.builder()
//                .metricId(target)
//                .rangeId(range)
//                .kind(kind)
//                .unit(unit)
//                .precision(1)
//                .axis("time")
//                .bucketMinutes(
//                        switch (range){
//                            case "10m"->{yield 10;}
//                            case "1h" ->{yield 60;}
//                            case "1d" ->{yield 1440;}
//                            default -> {yield  60;}
//                        }
//                )
//                .start(startTime)
//                .end(endTime)
//                .points(points)
//                .build();
//        return ResponseEntity.ok(metricsResponse);
//
//    }

//    private DateTimeTemplate<LocalDateTime> groupOfTime(String range) {
//        return switch (range) {
//            // 10분 단위: AS CHAR를 AS String으로 변경합니다.
//            case "10m" -> Expressions.dateTimeTemplate(LocalDateTime.class,
//                    "STR_TO_DATE(CONCAT(DATE_FORMAT({0}, '%Y-%m-%d %H:'), LPAD(CAST(FLOOR(MINUTE({0}) / 10) * 10 AS String), 2, '0'), ':00'), '%Y-%m-%d %H:%i:%s')",
//                    qSensorMetrics.createTime
//            );
//
//            // 1시간 단위
//            case "1h" -> Expressions.dateTimeTemplate(LocalDateTime.class,
//                    "STR_TO_DATE(DATE_FORMAT({0}, '%Y-%m-%d %H:00:00'), '%Y-%m-%d %H:%i:%s')",
//                    qSensorMetrics.createTime
//            );
//
//            // 1일 단위
//            case "1day", "1d" -> Expressions.dateTimeTemplate(LocalDateTime.class,
//                    "STR_TO_DATE(DATE_FORMAT({0}, '%Y-%m-%d 00:00:00'), '%Y-%m-%d %H:%i:%s')",
//                    qSensorMetrics.createTime
//            );
//
//            default -> throw new IllegalArgumentException("지원하지 않는 시간 범위(range)입니다: " + range);
//        };
//    }
private DateTimeTemplate<LocalDateTime> groupOfTime(String range) {
    return switch (range) {
        // 10분 단위: 10분 단위로 절삭하기 위해 date_trunc('hour')에 분을 계산해서 더해줍니다.
        case "10m" -> Expressions.dateTimeTemplate(LocalDateTime.class,
                "date_trunc('hour', {0}) + (floor(extract(minute from {0}) / 10) * 10 * interval '1 minute')",
                qSensorMetrics.createTime
        );

        // 1시간 단위: 분/초를 정직하게 00:00으로 날립니다.
        case "1h" -> Expressions.dateTimeTemplate(LocalDateTime.class,
                "date_trunc('hour', {0})",
                qSensorMetrics.createTime
        );

        // 1일 단위: 시간/분/초를 전부 00:00:00으로 날립니다.
        case "1day", "1d" -> Expressions.dateTimeTemplate(LocalDateTime.class,
                "date_trunc('day', {0})",
                qSensorMetrics.createTime
        );

        default -> throw new IllegalArgumentException("지원하지 않는 시간 범위(range)입니다: " + range);
    };
}
}
