package com.smartaqi.util;

public final class AQIUtil {
    private AQIUtil() {}

    public static String getCategory(int aqi) {
        if (aqi <= 50) return "Good";
        if (aqi <= 100) return "Moderate";
        if (aqi <= 200) return "Poor";
        return "Hazardous";
    }
}
