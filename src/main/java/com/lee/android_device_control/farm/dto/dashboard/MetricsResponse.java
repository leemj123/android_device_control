package com.lee.android_device_control.farm.dto.dashboard;

import lombok.Builder;
import lombok.Setter;

import java.util.List;

@Builder
public record MetricsResponse (
        String metricId,
        String rangeId,
        String kind,
        String unit,
        int precision,
        String axis,
        int bucketMinutes,
        long start,
        long end,
        List<MetricsPoint> points
)
{
    public record MetricsPoint(
            long t,
            Float v
    ){}
}
