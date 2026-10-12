package com.lee.android_device_control.farm.dto.dashboard;

import lombok.Builder;

@Builder
public record Sensor(
   float value,
   float previous
) {}
