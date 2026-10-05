package com.lee.android_device_control.farm.dto;


public record SensorDataReq(
        int deviceId,
        int rssi,
        Sensors sensors

){}
