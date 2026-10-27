package com.lee.android_device_control.farm.domain;

import com.lee.android_device_control.farm.dto.SensorDataReq;
import com.lee.android_device_control.farm.dto.Sensors;
import com.lee.android_device_control.farm.infra.SensorMetrics;
import com.lee.android_device_control.farm.infra.SensorMetricsRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FarmEspService {

    private final SensorMetricsRepo sensorMetricsRepo;

    public void luxSensor(SensorDataReq sensorDataReq) {
        Sensors sensors = sensorDataReq.sensors();

        SensorMetrics sensorMetrics = SensorMetrics.builder()
                .sectionNum(1)
                .onAirTemp(true)
                .airTempValue(sensors.temperate())
                .onHumidity(true)
                .humidityValue(sensors.humidity())
                .onLux(true)
                .luxValue(sensors.lux())
                .onWaterTemp(true)
                .waterTempValue(sensors.waterTemperate())
                .onConductivity(false)
                .conductivityValue(-1)
                .onPh(false)
                .potentialHydrogenValue(-1)
                .onLed(true)
                .onWaterPump(false)
                .build();

        sensorMetricsRepo.save(sensorMetrics);

    }
}
