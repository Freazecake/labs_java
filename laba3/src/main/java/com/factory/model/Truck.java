package com.factory.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Truck extends AbstractEntity {
    private String type;
    private String model;
    private double enginePower;
    private int maxSpeed;
    private Date productionDate;
    private double price;

    public Truck() {
        super();
    }

    public Truck(int id, String type, String model, double enginePower,
            int maxSpeed, Date productionDate, double price) {
        super(id);
        this.type = type;
        this.model = model;
        this.enginePower = enginePower;
        this.maxSpeed = maxSpeed;
        this.productionDate = productionDate;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getEnginePower() {
        return enginePower;
    }

    public void setEnginePower(double enginePower) {
        this.enginePower = enginePower;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String date = productionDate != null ? sdf.format(productionDate) : "null";
        return String.format(
                "Truck{id=%d, type='%s', model='%s', enginePower=%.1f, maxSpeed=%d, productionDate=%s, price=%.2f}",
                id, type, model, enginePower, maxSpeed, date, price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Truck))
            return false;
        return id == ((Truck) o).id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}