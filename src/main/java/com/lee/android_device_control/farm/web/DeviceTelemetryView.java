package com.lee.android_device_control.farm.web;

import java.util.List;

public record DeviceTelemetryView(
        Integer jsonVersion,
        String deviceKey,
        Long sequence,
        Long uptimeMs,
        List<SensorReadingView> readings,
        List<ActuatorView> actuators,
        NetworkView network,
        List<ErrorView> errors
) {}
