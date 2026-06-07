package com.lee.android_device_control.andorid.dto;

public record AndroidStatusReq (
        String androidId,
        String deviceName,
        String manufacturer,
        String model,
        String brand,
        String device,
        String androidVersion,
        String sdkInt,
        Integer batteryLevel,
        Boolean charging
){}
