package com.lee.android_device_control.farm.web;

public record ErrorView(
        String target,
        String code,
        String message
) {}
