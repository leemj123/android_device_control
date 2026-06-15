package com.lee.android_device_control.farm.web;

import java.time.LocalDateTime;

public record FarmView(
        Long id,
        String name,
        LocalDateTime createTime,
        LocalDateTime updateTime
) {}
