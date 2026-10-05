package com.lee.android_device_control.farm.controller;

import com.lee.android_device_control.farm.dto.SensorDataReq;
import com.lee.android_device_control.farm.service.FarmDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sensor")
@RequiredArgsConstructor
public class FarmDashboardApiController {

    private final FarmDashboardService farmDashboardService;

    @PostMapping("/section/001/condition")
    public ResponseEntity<?> esp32LuxSensor(@RequestBody SensorDataReq sensorDataReq) {
        farmDashboardService.esp32LuxSensor(sensorDataReq);
        return ResponseEntity.ok().build();
    }

}
