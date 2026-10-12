package com.lee.android_device_control.farm.controller;

import com.lee.android_device_control.farm.domain.FarmWebDashboardService;
import com.lee.android_device_control.farm.infra.QSensorMetrics;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FarmWebDashboardController {

    private final QSensorMetrics qSensorMetrics = QSensorMetrics.sensorMetrics;
    private final FarmWebDashboardService farmWebDashboardService;

    @GetMapping("/snapshot")
    public ResponseEntity<?> sensorSnapShot() {
        return farmWebDashboardService.sensorSnapShot();
    }

    @GetMapping("/metrics/{target}/series")
    public ResponseEntity<?> sensorMetrics(
            @PathVariable String target,
            @RequestParam(defaultValue = "1h") String range
    ) {
        switch (target) {
            case "temperature" -> {
                return farmWebDashboardService.sensorMetrics(target, range,"°C","analog",qSensorMetrics.airTempValue.avg());
            }
            case "humidity" -> {
                return farmWebDashboardService.sensorMetrics(target, range,"%","analog",qSensorMetrics.humidityValue.avg());
            }
            case "lux" -> {
                return farmWebDashboardService.sensorMetrics(target, range,"lux","analog",qSensorMetrics.luxValue.avg());
            }
            case "water_temp" -> {
                return farmWebDashboardService.sensorMetrics(target, range,"°C","analog",qSensorMetrics.waterTempValue.avg());
            }
            case "led_state" -> {
                return farmWebDashboardService.sensorMetrics(target, range,null,"binary",qSensorMetrics.onLed);
            }
            case "pump_state" -> {
                return farmWebDashboardService.sensorMetrics(target, range,null,"binary",qSensorMetrics.onWaterPump);
            }
//            case "led_runtime" -> {
//                targetColumn = qSensorMetrics.led;
//                return farmWebDashboardService.sensorMetrics(range,"분",targetColumn);
//            }
//            case "pump_runtime" -> {
//                targetColumn = qSensorMetrics.airTempValue;
//                return farmWebDashboardService.sensorMetrics(range,"분",targetColumn);
//            }
            default -> throw new IllegalArgumentException("지원하지 않는 target입니다: " + target);
        }

    }
}
