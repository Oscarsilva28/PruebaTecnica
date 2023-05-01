package com.example.pruebatecnica.utils.ListElements;

public class ListElement {
    public long id;
    public String neoReferenceId;
    public String photo;
    public String name;
    public Long magnitude;
    public Boolean isPotentiallHazardous;

    public ListElement(long id,String neoReferenceId, String photo, String name, Long magnitude, Boolean isPotentiallHazardous) {
        this.id = id;
        this.neoReferenceId = neoReferenceId;
        this.photo = photo;
        this.name = name;
        this.magnitude = magnitude;
        this.isPotentiallHazardous = isPotentiallHazardous;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNeoReferenceId() {
        return neoReferenceId;
    }

    public void setNeoReferenceId(String neoReferenceId) {
        this.neoReferenceId = neoReferenceId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(Long magnitude) {
        this.magnitude = magnitude;
    }

    public Boolean getIsPotentiallHazardous() {
        return isPotentiallHazardous;
    }

    public void setIsPotentiallHazardous(Boolean isPotentiallHazardous) {
        this.isPotentiallHazardous = isPotentiallHazardous;
    }
}
