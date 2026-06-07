package com.lee.android_device_control.andorid;

import com.lee.android_device_control.andorid.dto.AndroidLoginReq;
import com.lee.android_device_control.andorid.dto.AndroidRegisterReq;
import com.lee.android_device_control.andorid.dto.AndroidStatusReq;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;

@RestController
@RequiredArgsConstructor
public class AndroidController {

    private final AndroidService androidService;

//    @PostMapping("/api/android/login")
//    public HttpResponse<?> deviceLogin(@RequestBody AndroidLoginReq androidLoginReq) {
//
//    }
//    @PostMapping("/api/android/register")
//    public HttpResponse<?> deviceLogin(@RequestBody AndroidRegisterReq androidRegisterReq) {
//
//    }

    @PostMapping("/api/android/status")
    public void statusLoad(@RequestBody AndroidStatusReq androidStatusReq) {
        androidService.statusLoader(androidStatusReq);
    }
}
