package com.lee.android_device_control.farm.dto;

public record ActuatorStateRequest (
        String actuatorKey,
        String type,
        String state
) {}
