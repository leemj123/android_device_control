package com.lee.android_device_control.farm.service;

import com.lee.android_device_control.farm.dto.SensorDataReq;
import com.lee.android_device_control.farm.dto.Sensors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FarmDashboardService {


    public void esp32LuxSensor(SensorDataReq sensorDataReq) {
        Sensors sensors = sensorDataReq.sensors();
        log.info(String.valueOf(sensors.lux()));
        log.info(String.valueOf(sensors.temperate()));
        log.info(String.valueOf(sensors.humidity()));
        log.info("--------");

    }
}
