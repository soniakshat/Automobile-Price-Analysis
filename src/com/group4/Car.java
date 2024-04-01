package com.group4;

import org.json.JSONObject;

enum FuelType {
    Gas, Diesel, Electric, Hybrid, Other
}

enum TransmissionType {
    Automatic, NA, Manual
}

/**
 * Custom car class to create car objects
 * @author Akshat Soni
 * */
public class Car {
    private String name;
    private int price;
    private FuelType fuelType;
    private TransmissionType transmissionType;
    private int kmsDriven;
    private String imageUrl;


    /**
     * Car object constructor
     * @param name Car Name
     * @param price Car price
     * @param fuelType fuel type
     * @param imageUrl url of image
     * @param transmissionType transmission type
     * @param kmsDriven total kms driven
     * @author Akshat Soni
     * */
    public Car(String name, int price, FuelType fuelType, TransmissionType transmissionType, int kmsDriven, String imageUrl) {
        this.name = name;
        this.price = price;
        this.fuelType = fuelType;
        this.transmissionType = transmissionType;
        this.kmsDriven = kmsDriven;
        this.imageUrl = imageUrl;
    }

    /**
     * @return car name
     * @author Akshat Soni
     * */
    public String getName() {
        return name;
    }

    /**
     * @return fuel type
     * @author Akshat Soni
     * */
    public FuelType getFuelType() {
        return fuelType;
    }

    /**
     * @return transmission type
     * @author Akshat Soni
     * */
    public TransmissionType getTransmissionType() {
        return transmissionType;
    }

    /**
     * @return kms driven
     * @author Akshat Soni
     * */
    public int getKmsDriven() {
        return kmsDriven;
    }

    /**
     * @return image url
     * @author Akshat Soni
     * */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * @return car price
     * @author Akshat Soni
     * */
    public int getPrice() {
        return price;
    }

    /**
     * Creates a json object of the car object
     * @return json object
     * @author Akshat Soni
     * */
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


    /**
     * @return Stringify the car object
     * @author Akshat Soni
     * */
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
