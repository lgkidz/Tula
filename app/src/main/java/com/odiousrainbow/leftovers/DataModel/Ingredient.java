package com.odiousrainbow.leftovers.DataModel;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Ingredient implements Serializable, Parcelable {
    private String name;
    private String quantity;
    private String unit;
    private boolean spice;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isSpice() {
        return spice;
    }

    public void setSpice(boolean spice) {
        this.spice = spice;
    }

    public Ingredient(){}

    public Ingredient(String name, String quantity,String unit, boolean spice){
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.spice = spice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String toString(){
        return this.getQuantity() + " " + this.getUnit() + " " + this.getName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
