package com.group4;

enum FuelType {
    Gas,
    Diesel,
    Electric,
    Hybrid,
    Other
}

enum TransmissionType {
    Automatic,
    NA, Manual
}

public class Car {
    private String name;
    private int price;
    private FuelType fuelType;
    private TransmissionType transmissionType;
    private int kmsDriven;
    private String imageUrl;


    public Car(String name, int price, FuelType fuelType, TransmissionType transmissionType, int kmsDriven, String imageUrl) {
        this.name = name;
        this.price = price;
        this.fuelType = fuelType;
        this.transmissionType = transmissionType;
        this.kmsDriven = kmsDriven;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public TransmissionType getTransmissionType() {
        return transmissionType;
    }

    public int getKmsDriven() {
        return kmsDriven;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
