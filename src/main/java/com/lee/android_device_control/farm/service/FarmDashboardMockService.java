package com.lee.android_device_control.farm.service;

import com.lee.android_device_control.farm.web.ActuatorView;
import com.lee.android_device_control.farm.web.DeviceTelemetryView;
import com.lee.android_device_control.farm.web.ErrorView;
import com.lee.android_device_control.farm.web.FarmDashboardResponse;
import com.lee.android_device_control.farm.web.FarmView;
import com.lee.android_device_control.farm.web.NetworkView;
import com.lee.android_device_control.farm.web.SensorReadingView;
import com.lee.android_device_control.farm.web.SensorSlotView;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FarmDashboardMockService {

    public FarmDashboardResponse getDashboard() {
        LocalDateTime now = LocalDateTime.now();
        return new FarmDashboardResponse(
                mockFarmView(now),
                mockDeviceViews(),
                mockSensorSlots(),
                now
        );
    }

    private FarmView mockFarmView(LocalDateTime now) {
        return new FarmView(1L, "테스트 스마트팜", now.minusDays(30), now);
    }

    private List<DeviceTelemetryView> mockDeviceViews() {
        return List.of(
                new DeviceTelemetryView(
                        1,
                        "farm-unit-01",
                        42L,
                        3_600_000L,
                        List.of(
                                new SensorReadingView("temp-1", "temperature", 24.5, "°C", "ok", null, null),
                                new SensorReadingView("humid-1", "humidity", 62.0, "%", "ok", null, null),
                                new SensorReadingView("co2-1", "co2", 820.0, "ppm", "warning", null, null)
                        ),
                        List.of(
                                new ActuatorView("fan-1", "fan", "ON"),
                                new ActuatorView("pump-1", "pump", "OFF")
                        ),
                        new NetworkView(-65, "192.168.0.10"),
                        List.of()
                ),
                new DeviceTelemetryView(
                        1,
                        "farm-unit-02",
                        18L,
                        1_800_000L,
                        List.of(
                                new SensorReadingView("temp-1", "temperature", 28.3, "°C", "error", "E_TEMP_HIGH", "온도 상한 초과")
                        ),
                        List.of(
                                new ActuatorView("fan-1", "fan", "ON")
                        ),
                        new NetworkView(-78, "192.168.0.11"),
                        List.of(
                                new ErrorView("temp-1", "E_TEMP_HIGH", "온도 센서 상한값 초과")
                        )
                )
        );
    }

    private List<SensorSlotView> mockSensorSlots() {
        return List.of(
                new SensorSlotView(1L),
                new SensorSlotView(2L),
                new SensorSlotView(3L)
        );
    }
}
