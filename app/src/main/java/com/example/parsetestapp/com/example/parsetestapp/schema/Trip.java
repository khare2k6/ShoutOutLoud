package com.example.parsetestapp.com.example.parsetestapp.schema;

import android.os.Parcel;
import android.os.Parcelable;

import com.glympse.android.lite.GTicketLite;
import com.google.gson.annotations.Expose;

/**
 * Created by AKhare on 24-Dec-14.
 */
public class Trip implements Parcelable{

    @Expose
    private final String mSource,mDestination;
    @Expose
    private final Person mTraveller,mDriver;
    @Expose
    private final boolean mIsSelfDriven;
    @Expose
    private final LastActivity mLastActivity;
    @Expose
    private final Vehicle mVehicle;
    //No need to create Glink from staring ,thats why not final
    @Expose
    private String mGLink;

    @Expose
    private long mTimeTillExpiry;

    private GTicketLite mGlympseTicket;

    public GTicketLite getGlympseTicket() {
        return mGlympseTicket;
    }

    public void setGlympseTicket(GTicketLite mGlympseTicket) {
        this.mGlympseTicket = mGlympseTicket;
    }
    public String getmSource() {
        return mSource;
    }

    public String getmDestination() {
        return mDestination;
    }

    public Person getmTraveller() {
        return mTraveller;
    }

    public Person getmDriver() {
        return mDriver;
    }

    public boolean ismIsSelfDriven() {
        return mIsSelfDriven;
    }

    public LastActivity getmLastActivity() {
        return mLastActivity;
    }

    public Vehicle getmVehicle() {
        return mVehicle;
    }

    public String getmGLink() {
        return mGLink;
    }
    public long getmTimeTillExpiry() {
        return mTimeTillExpiry;
    }

    public void setmTimeTillExpiry(long mTimeTillExpiry) {
        this.mTimeTillExpiry = mTimeTillExpiry;
    }




    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Set Glink for this trip
     * @param url
     */
    public void setGLink(String url){
        this.mGLink = url;
    }

    public Trip(Parcel in){
        this.mSource = in.readString();
        this.mDestination= in.readString();
        this.mIsSelfDriven = (in.readByte() != 0);
        this.mLastActivity  = Notification.getLastActivity(in.readString());
        this.mTraveller = in.readParcelable(Trip.class.getClassLoader());
        this.mDriver = in.readParcelable(Trip.class.getClassLoader());
        this.mVehicle = in.readParcelable(Trip.class.getClassLoader());
        this.mGLink = in.readString();

    }

    public Trip(Builder builder){
        this.mSource = builder.Source;
        this.mDestination= builder.Destination;
        this.mIsSelfDriven = builder.IsSelfDriven;
        this.mLastActivity  = builder.LastActivity;
        this.mTraveller = builder.Traveller;
        this.mDriver = builder.Driver;
        this.mVehicle = builder.Vehicle;
        //this.mGLink = builder.GLink;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mSource);
        dest.writeString(mDestination);
        dest.writeByte((byte) (mIsSelfDriven ? 1 : 0));
        dest.writeString(mLastActivity.getName());
        dest.writeParcelable(mTraveller,flags);
        dest.writeParcelable(mDriver,flags);
        dest.writeParcelable(mVehicle,flags);
        dest.writeString(mGLink);
    }

    public static Parcelable.Creator<Trip>CREATOR = new Parcelable.Creator<Trip>(){
        @Override
        public Trip createFromParcel(Parcel source) {
            return new Trip(source);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };
    public enum LastActivity {
        BOARDED_PRIVATE_CAB("Private Cab"),
        BOARDED_OFFICE_CAB("Office Cab"),
        BOARDED_PUBLIC_BUS("Public Bus"),
        BOARDED_OFFICE_BUS("Office Bus"),
        DRIVING_OWN_CAR("Own Car"),
        WALKING("Walking");
        @Expose
        private String mActivityName;
        private LastActivity(String name){
            mActivityName = name;
        }
        public String getName(){
            return mActivityName;
        }
    }

    @Override
    public String toString() {
        return "Trip{" +
                "mSource='" + mSource + '\'' +
                ", mDestination='" + mDestination + '\'' +
                ", mTraveller=" + mTraveller +
                ", mDriver=" + mDriver +
                ", mIsSelfDriven=" + mIsSelfDriven +
                ", mLastActivity=" + mLastActivity +
                ", mVehicle=" + mVehicle +
                '}';
    }

    public static class Builder{
        public Builder setSource(String source) {
            Source = source;
            return this;
        }

        public Builder setDestination(String destination) {
            Destination = destination;
            return this;
        }

        public Builder setTraveller(Person traveller) {
            Traveller = traveller;
            return this;
        }

        public Builder setDriver(Person driver) {
            Driver = driver;
            return this;
        }

        public Builder setSelfDriven(boolean isSelfDriven) {
            IsSelfDriven = isSelfDriven;
            return this;
        }

        public Builder setLastActivity(Trip.LastActivity lastActivity) {
            LastActivity = lastActivity;
            return this;
        }

        public Builder setVehicle(Vehicle vehicle) {
            Vehicle = vehicle;
            return this;
        }

//        public Builder setGLink(String GLink) {
//            this.GLink = GLink;
//            return this;
//        }

        public Trip build(){
            return new Trip(this);
        }

        private String Source,Destination;
        private Person Traveller,Driver;
        private boolean IsSelfDriven;
        private LastActivity LastActivity;
        private Vehicle Vehicle;
       // private String GLink;
    }
}
