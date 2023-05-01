package com.example.pruebatecnica.utils.models;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class EstimatedDiameter {
    private Map<String,Long> kilometers;
    private Map<String,Long> meters;
    private Map<String,Long> miles;
    private Map<String,Long> feet;

    public EstimatedDiameter(Map<String, Long> kilometers, Map<String, Long> meters, Map<String, Long> miles, Map<String, Long> feet) {
        this.kilometers = kilometers;
        this.meters = meters;
        this.miles = miles;
        this.feet = feet;
    }

    @Override
    public String toString() {
        return "EstimatedDiameter{" +
                "kilometers=" + kilometers +
                ", meters=" + meters +
                ", miles=" + miles +
                ", feet=" + feet +
                '}';
    }

    public EstimatedDiameter() {
    }

    public Map<String, Long> getKilometers() {
        return kilometers;
    }

    public void setKilometers(Map<String, Long> kilometers) {
        this.kilometers = kilometers;
    }

    public Map<String, Long> getMeters() {
        return meters;
    }

    public void setMeters(Map<String, Long> meters) {
        this.meters = meters;
    }

    public Map<String, Long> getMiles() {
        return miles;
    }

    public void setMiles(Map<String, Long> miles) {
        this.miles = miles;
    }

    public Map<String, Long> getFeet() {
        return feet;
    }

    public void setFeet(Map<String, Long> feet) {
        this.feet = feet;
    }
}