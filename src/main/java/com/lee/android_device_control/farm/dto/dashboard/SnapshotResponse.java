package com.lee.android_device_control.farm.dto.dashboard;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class SnapshotResponse {
    long updatedAt;
    Metrics metrics;
    List<Device> devices;
}
