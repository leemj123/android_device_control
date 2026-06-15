package com.lee.android_device_control.farm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FarmDashboardController {

    @GetMapping("/farm")
    public String farmDashboard() {
        return "forward:/farm/dashboard.html";
    }
}
