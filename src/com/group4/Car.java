package com.group4;

import org.json.JSONObject;

enum FuelType {
    Gas, Diesel, Electric, Hybrid, Other
}

enum TransmissionType {
    Automatic, NA, Manual
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

    public int getPrice() {
        return price;
    }

    public void printDetails() {
        CustomPrint.println(this);
    }

    public JSONObject getJsonObject() {

            JSONObject carObject = new JSONObject();

            carObject.put("name", name);
            carObject.put("price", price);
            carObject.put("kms", kmsDriven);
            carObject.put("image", imageUrl == null || imageUrl.isEmpty() ? " ": imageUrl);
            carObject.put("fuelType", fuelType);
            carObject.put("transmission", transmissionType);

            return carObject;
    }

    @Override
    public String toString() {
        StringBuilder data = new StringBuilder();
        data.append("\nCar Details");
        data.append("\n").append("-".repeat(10));
        data.append(STR."\nName: \{name}");
        data.append(STR."\nPrice: $ \{price}");
        data.append(STR."\nFuel: \{fuelType}");
        data.append(STR."\nTransmission: \{transmissionType}");
        if (kmsDriven >= 0)
            data.append(STR."\nKms Driven: \{kmsDriven} kms");
        if (imageUrl != null && !imageUrl.isBlank()) {
            data.append(STR."\nImage Url: \{imageUrl}");
        }
        return data.toString();
    }
}
