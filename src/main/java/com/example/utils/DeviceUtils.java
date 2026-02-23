package com.example.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

@Component
public class DeviceUtils {
    public String generateDeviceId(String userAgent) {
        return DigestUtils.md5DigestAsHex(userAgent.getBytes());
    }
}