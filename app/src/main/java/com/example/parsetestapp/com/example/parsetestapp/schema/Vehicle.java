package com.example.parsetestapp.com.example.parsetestapp.schema;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * Created by akhare on 22-Dec-14.
 */
public class Vehicle implements Parcelable{
    @Expose
    private String mNumber;
    @Expose
    private String mModelName;

    @Override
    public int describeContents() {
        return 0;
    }

    public String getmModelName() {
        return mModelName;
    }

    public String getmNumber() {
        return mNumber;
    }

    public Vehicle(String num,String model){
        this.mNumber = num;
        this.mModelName = model;

    }

    private Vehicle(Parcel in){
     this.mModelName = in.readString();
     this.mNumber = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mModelName);
        dest.writeString(this.mNumber);
    }

    public static final Creator<Vehicle> CREATOR = new Creator<Vehicle>(){

        @Override
        public Vehicle createFromParcel(Parcel source) {
            return new Vehicle(source);
        }

        @Override
        public Vehicle[] newArray(int size) {
            return new Vehicle[size];
        }
    };
}
