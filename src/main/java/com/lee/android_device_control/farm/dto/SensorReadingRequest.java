package com.lee.android_device_control.farm.dto;

public record SensorReadingRequest(
        String sensorKey,
        String type,
        Double value,
        String unit,
        String status,
        String errorCode,
        String errorMessage
) {}
