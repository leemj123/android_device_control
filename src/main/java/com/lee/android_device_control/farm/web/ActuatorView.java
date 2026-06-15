package com.lee.android_device_control.farm.web;

public record ActuatorView(
        String actuatorKey,
        String type,
        String state
) {}
