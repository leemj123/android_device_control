package com.lee.android_device_control.andorid;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false, length = 100)
    private String androidId;
    @Column(nullable = false, length = 100)
    private String deviceName;
    @Column(nullable = false, length = 100)
    private String model;


}
