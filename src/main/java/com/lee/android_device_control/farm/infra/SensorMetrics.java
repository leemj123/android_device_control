package com.lee.android_device_control.farm.infra;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder(toBuilder = true) // 빌더 사용 시 안정성을 위해 권장
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SensorMetrics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int sectionNum;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    private boolean onAirTemp;
    private float airTempValue;

    private boolean onHumidity;
    private float humidityValue;
    private boolean onLux;
    private float luxValue;
    private boolean onWaterTemp;
    private float waterTempValue;
    private boolean onConductivity;
    private float conductivityValue;
    private boolean onPh;
    private float potentialHydrogenValue;

    private boolean onLed;
    private boolean onWaterPump;

}
