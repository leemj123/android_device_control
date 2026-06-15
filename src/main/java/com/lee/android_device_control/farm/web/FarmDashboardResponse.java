package com.lee.android_device_control.farm.web;

import java.time.LocalDateTime;
import java.util.List;

public record FarmDashboardResponse(
        FarmView farm,
        List<DeviceTelemetryView> devices,
        List<SensorSlotView> sensorSlots,
        LocalDateTime displayedAt
) {}
