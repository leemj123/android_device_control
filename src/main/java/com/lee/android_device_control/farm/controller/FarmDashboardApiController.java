package com.lee.android_device_control.farm.controller;

import com.lee.android_device_control.farm.dto.TempSignRequest;
import com.lee.android_device_control.farm.service.FarmDashboardMockService;
import com.lee.android_device_control.farm.web.FarmDashboardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("/api/farm")
@RequiredArgsConstructor
public class FarmDashboardApiController {

    private final FarmDashboardMockService farmDashboardMockService;

    @GetMapping("/ping")
    public ResponseEntity<?> ping () {
        return ResponseEntity.ok().body("pong");
    }
    @PostMapping("/esp")
    public void esp32Status(@RequestBody TempSignRequest tempSignRequest) {
        farmDashboardMockService.getEsp32StatusLog(tempSignRequest);
    }
    @GetMapping("/dashboard")
    public FarmDashboardResponse dashboard() {
        return farmDashboardMockService.getDashboard();
    }
}
