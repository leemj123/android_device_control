package com.lee.android_device_control.farm.infra;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorMetricsRepo extends JpaRepository<SensorMetrics,Long> {
    List<SensorMetrics> findBy(Pageable pageable);
}
