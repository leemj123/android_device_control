package com.lee.android_device_control.farm.dto.dashboard;


public record Device (
    String id,
    String name,
    String channel,
    boolean on,
    String mood,
    long updatedAt
) {}
