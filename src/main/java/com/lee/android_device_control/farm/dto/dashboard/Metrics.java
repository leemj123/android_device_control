package com.lee.android_device_control.farm.dto.dashboard;

import lombok.Builder;

import java.util.List;

@Builder
public record Metrics(
        Sensor temperature,
        Sensor humidity,
        Sensor lux,
        Sensor water_temp,
        boolean led_state,
        boolean pump_state
)
{}
