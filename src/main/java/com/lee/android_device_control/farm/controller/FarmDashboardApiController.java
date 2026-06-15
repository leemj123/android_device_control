package com.lee.android_device_control.farm.controller;

import com.lee.android_device_control.farm.service.FarmDashboardMockService;
import com.lee.android_device_control.farm.web.FarmDashboardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/farm")
@RequiredArgsConstructor
public class FarmDashboardApiController {

    private final FarmDashboardMockService farmDashboardMockService;

    @GetMapping("/dashboard")
    public FarmDashboardResponse dashboard() {
        return farmDashboardMockService.getDashboard();
    }
}
