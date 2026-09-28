package com.lee.android_device_control.farm.dto;

public record TempSignRequest(
        String deviceId,
        Long cycleCount,
        String status
) {
}
