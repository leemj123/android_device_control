package com.lee.android_device_control.farm.dto;

public record DeviceErrorRequest(
        String target,
        String code,
        String message
) {}
