package com.example.pruebatecnica.utils.models;

import java.util.Map;

public class CloseApproachData {
    private String close_approach_date;
    private String close_approach_date_full;
    private String orbiting_body;
    private Long epoch_date_close_approach;
    private Map<String,String> relative_velocity;
    private Map<String,String> miss_distance;

    public CloseApproachData(String close_approach_date, String close_approach_date_full, String orbiting_body, Long epoch_date_close_approach, Map<String, String> relative_velocity, Map<String, String> miss_distance) {
        this.close_approach_date = close_approach_date;
        this.close_approach_date_full = close_approach_date_full;
        this.orbiting_body = orbiting_body;
        this.epoch_date_close_approach = epoch_date_close_approach;
        this.relative_velocity = relative_velocity;
        this.miss_distance = miss_distance;
    }

    public CloseApproachData() {
    }

    public String getClose_approach_date() {
        return close_approach_date;
    }

    public void setClose_approach_date(String close_approach_date) {
        this.close_approach_date = close_approach_date;
    }

    public String getClose_approach_date_full() {
        return close_approach_date_full;
    }

    public void setClose_approach_date_full(String close_approach_date_full) {
        this.close_approach_date_full = close_approach_date_full;
    }

    public String getOrbiting_body() {
        return orbiting_body;
    }

    public void setOrbiting_body(String orbiting_body) {
        this.orbiting_body = orbiting_body;
    }

    public Long getEpoch_date_close_approach() {
        return epoch_date_close_approach;
    }

    public void setEpoch_date_close_approach(Long epoch_date_close_approach) {
        this.epoch_date_close_approach = epoch_date_close_approach;
    }

    public Map<String, String> getRelative_velocity() {
        return relative_velocity;
    }

    public void setRelative_velocity(Map<String, String> relative_velocity) {
        this.relative_velocity = relative_velocity;
    }

    public Map<String, String> getMiss_distance() {
        return miss_distance;
    }

    public void setMiss_distance(Map<String, String> miss_distance) {
        this.miss_distance = miss_distance;
    }
}