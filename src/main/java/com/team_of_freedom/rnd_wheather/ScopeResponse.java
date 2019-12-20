package com.team_of_freedom.rnd_wheather;

public class ScopeResponse extends BaseResponse {
    private float[] average;
    private String[] weather_type;

    public ScopeResponse(String status, Integer code) {
        super(status, code);
    }

    public ScopeResponse(String status, Integer code, float[] average, String[] weather_type) {
        super(status, code);
        this.average = average;
        this.weather_type = weather_type;
    }

    public float[] getAverage() {
        return average;
    }

    public void setAverage(float[] average) {
        this.average = average;
    }

    public String[] getWeather_type() {
        return weather_type;
    }

    public void setWeather_type(String[] weather_type) {
        this.weather_type = weather_type;
    }
}
