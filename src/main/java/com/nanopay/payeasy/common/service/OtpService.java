package com.nanopay.payeasy.common.service;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class OtpService {

    private final Cache<String, String> otpCache;

    public OtpService(Cache<String, String> otpCache) {
        this.otpCache = otpCache;
    }

    public String generateOtp(String key) {
        String otp = String.valueOf(100000 + new SecureRandom().nextInt(900000));
        otpCache.put(key, otp);
        return otp;
    }

    public boolean validateOtp(String key, String inputOtp) {
        String cachedOtp = otpCache.getIfPresent(key);

        if (cachedOtp == null) {
            return false;
        }

        if (cachedOtp.equals(inputOtp)) {
            otpCache.invalidate(key); // remove after success
            return true;
        }

        return false;
    }
}

