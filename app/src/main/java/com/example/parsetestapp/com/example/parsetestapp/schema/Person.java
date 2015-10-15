package com.example.parsetestapp.com.example.parsetestapp.schema;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by akhare on 22-Dec-14.
 */
public class Person implements Parcelable{
    @Expose
    private String mName;
    @Expose
    private URL mImage;
    @Expose
    private String mPhoneNumber;
    @Expose
    private String mCompanyName;
    @Expose
    private String mImId;

    public void setmImId(String mImId) {
        this.mImId = mImId;
    }

    public void setmCompanyName(String mCompanyName) {
        this.mCompanyName = mCompanyName;
    }

    public void setmImage(URL mImage) {
        this.mImage = mImage;
    }

    public String getmName() {

        return mName;
    }

    public URL getmImage() {
        return mImage;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public String getmCompanyName() {
        return mCompanyName;
    }

    public String getmImId() {
        return mImId;
    }

    public Person(String name,String num){
        this.mName = name;
        this.mPhoneNumber = num;

    }

    private Person(Parcel in){
        this.mName = in.readString();
        String imgLink = in.readString();
        if(TextUtils.isEmpty(imgLink)){
            this.mImage = null;
        }else{
            try {
                this.mImage = new URL(imgLink);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        this.mPhoneNumber = in.readString();
        this.mCompanyName = in.readString();
        this.mImId = in.readString();
        //this.mVehicle = in.readParcelable(Person.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        if(mImage != null){
            dest.writeString(mImage.toString());
        }else{
            dest.writeString("");
        }
        dest.writeString(mPhoneNumber);
        dest.writeString(mCompanyName);
        dest.writeString(mImId);
      //  dest.writeParcelable(mVehicle,flags);
    }

    public static Creator<Person> CREATOR = new Parcelable.Creator<Person>(){

        @Override
        public Person createFromParcel(Parcel source) {
            return new Person(source);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
}
