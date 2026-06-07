package com.lee.android_device_control.andorid;

import com.lee.android_device_control.andorid.dto.AndroidStatusReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class AndroidService {
    private final DeviceRepo deviceRepo;
    public void statusLoader(AndroidStatusReq androidStatusReq) {
        log.info("received: {}", androidStatusReq);
    }
}
