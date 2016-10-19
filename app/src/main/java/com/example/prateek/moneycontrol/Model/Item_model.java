package com.example.prateek.moneycontrol.Model;

public class Item_model {

    String name, value, date, type;

    public Item_model() {
    }

    public Item_model(String name, String value, String date, String type) {
        this.name = name;
        this.value = value;
        this.date = date;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
