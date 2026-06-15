package com.lee.android_device_control.farm.dto;

import java.util.List;

public record TelemetryRequest (
        Integer jsonVersion,
        String deviceKey,
        Long sequence,
        Long uptimeMs,
        List<SensorReadingRequest> readings,
        List<ActuatorStateRequest> actuators,
        NetworkRequest network,
        List<DeviceErrorRequest> errors
){}
