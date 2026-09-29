package com.smartaqi.models;

import java.util.ArrayList;
import java.util.List;

public class AQIData {
    private String cityName;
    private int aqiValue;
    private String category;
    private double pm25;
    private double pm10;
    private String healthTip;
    private String lastUpdated;
    private String trendStart;
    private String trendEnd;
    private final List<Integer> historyValues = new ArrayList<>();
    private final List<String> historyDates = new ArrayList<>();
    private final List<Double> historyPm25 = new ArrayList<>();
    private final List<Double> historyPm10 = new ArrayList<>();

    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }
    public int getAqiValue() { return aqiValue; }
    public void setAqiValue(int aqiValue) { this.aqiValue = aqiValue; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public double getPm25() { return pm25; }
    public void setPm25(double pm25) { this.pm25 = pm25; }
    public double getPm10() { return pm10; }
    public void setPm10(double pm10) { this.pm10 = pm10; }
    public String getHealthTip() { return healthTip; }
    public void setHealthTip(String healthTip) { this.healthTip = healthTip; }
    public String getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }
    public String getTrendStart() { return trendStart; }
    public void setTrendStart(String trendStart) { this.trendStart = trendStart; }
    public String getTrendEnd() { return trendEnd; }
    public void setTrendEnd(String trendEnd) { this.trendEnd = trendEnd; }
    public List<Integer> getHistoryValues() { return historyValues; }
    public List<String> getHistoryDates() { return historyDates; }
    public List<Double> getHistoryPm25() { return historyPm25; }
    public List<Double> getHistoryPm10() { return historyPm10; }
}
