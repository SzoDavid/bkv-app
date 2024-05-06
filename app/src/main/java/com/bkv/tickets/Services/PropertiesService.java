package com.bkv.tickets.Services;

import com.bkv.tickets.BuildConfig;

public class PropertiesService {
    public static String getSecretKey() {
        return BuildConfig.secretKey;
    }
}
