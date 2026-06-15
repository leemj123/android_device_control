package com.lee.android_device_control.farm.web;

public record SensorReadingView(
        String sensorKey,
        String type,
        Double value,
        String unit,
        String status,
        String errorCode,
        String errorMessage
) {}
