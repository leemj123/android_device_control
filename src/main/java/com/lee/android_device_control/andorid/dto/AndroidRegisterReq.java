package com.lee.android_device_control.andorid.dto;

public record AndroidRegisterReq(
        String androidId,
        String deviceName,
        String model
) {
}
