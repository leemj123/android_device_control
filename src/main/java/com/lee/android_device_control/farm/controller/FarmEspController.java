package com.lee.android_device_control.farm.controller;

import com.lee.android_device_control.farm.dto.SensorDataReq;
import com.lee.android_device_control.farm.domain.FarmEspService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sensor")
@RequiredArgsConstructor
public class FarmEspController {

    private final FarmEspService farmEspService;

    @PostMapping("/section/001/condition")
    public ResponseEntity<?> sensorInput(@RequestBody SensorDataReq sensorDataReq) {
        farmEspService.luxSensor(sensorDataReq);
        return ResponseEntity.ok().build();
    }

}
