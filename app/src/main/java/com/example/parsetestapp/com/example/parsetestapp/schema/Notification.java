package com.example.parsetestapp.com.example.parsetestapp.schema;

import android.text.TextUtils;
import android.util.Log;
import  com.example.parsetestapp.com.example.parsetestapp.schema.Trip.LastActivity;

/**
 * Created by akhare on 22-Dec-14.
 */
public class Notification {
    private final MsgType mMsgType;
    public static final String BOOL_VALUE_TRUE = "true";
    public static final String SELF_DRIVEN = "Self";
    private static final String TAG = Notification.class.getSimpleName();

    private final LastActivity mVictimLastKnownActivity;  //eg CAB_BOARDED,WALKING etc
    private final boolean mIsSelfDriven;//if true,below driver's detail will be null
    private final String mVictimName;     //Incase its a help card message ,following info will be there
    private final String mVictimNumber;
    private final String mVictimImId;
    private final String mVictimImageUrl;
    private final String mDriverName;
    private final String mDriverNum;
    private final String mCarNumber;
    private final String mVictimCompany;
    private final String mCarMake;//Like Meru Dezire,Ola Indica OR Swift etc
    private final String mChannelName;// Parse channel on which helpers can chat
    private final String mGLink;//glympse link

            //Incase chat message ??

    public Notification(Builder builder){
        this.mVictimName = builder.VictimName;
        this.mVictimNumber = builder.VictimNumber;
        this.mVictimImId = builder.VictimImId;
        this.mVictimImageUrl = builder.VictimImageUrl;
        this.mVictimLastKnownActivity = getLastActivity(builder.VictimLastKnownActivity);
        this.mCarMake = builder.CarMake;
        this.mCarNumber = builder.CarNumber;
        this.mChannelName = builder.ChannelName;
        this.mGLink = builder.GLink;
        this.mIsSelfDriven = getBoolValFromString(builder.IsSelfDriven);
        this.mMsgType = getMessageType(builder.MessageType);
        this.mVictimCompany = builder.VictimCompany;
        if (this.mIsSelfDriven) {
            this.mDriverName = SELF_DRIVEN;
            this.mDriverNum = SELF_DRIVEN;
        }else{
            this.mDriverName = builder.DriverName;
            this.mDriverNum = builder.DriverNum;
        }
    }

    private MsgType getMessageType(String type){
        MsgType category = MsgType.OTHER;
        if(MsgType.HELP_CARD.equals(type)){
            category = MsgType.HELP_CARD;
        }else if(MsgType.CHAT_MSG.equals(type)){
            category = MsgType.CHAT_MSG;
        }
        return category;
    }

    private boolean getBoolValFromString(String value){
        boolean ret = false;
        if(!TextUtils.isEmpty(value) && value.equals(BOOL_VALUE_TRUE)){
            ret = true;
        }
        return ret;
    }
    /**
     * Get User's last known activity,convert from
     * string to enum
     * @param lastActivity
     * @return
     */
    public static LastActivity getLastActivity(String lastActivity){
        LastActivity act = null;
        if(TextUtils.isEmpty(lastActivity)){
            Log.d(TAG, "Last Activity received is Empty or null!");
            return act;
        }
        if(LastActivity.BOARDED_OFFICE_BUS.getName().equals(lastActivity)){
            act = Trip.LastActivity.BOARDED_OFFICE_BUS;
        }else if(LastActivity.BOARDED_OFFICE_CAB.getName().equals(lastActivity)){
            act = LastActivity.BOARDED_OFFICE_CAB;
        }
        else if(LastActivity.BOARDED_PUBLIC_BUS.getName().equals(lastActivity)){
            act = LastActivity.BOARDED_PUBLIC_BUS;
        }
        else if(LastActivity.BOARDED_PRIVATE_CAB.getName().equals(lastActivity)){
            act = LastActivity.BOARDED_PRIVATE_CAB;
        }
        else if(LastActivity.DRIVING_OWN_CAR.getName().equals(lastActivity)){
            act = LastActivity.DRIVING_OWN_CAR;
        }else if(LastActivity.WALKING.getName().equals(lastActivity)){
            act = LastActivity.WALKING;
        }
        Log.d(TAG,"Last Activity"+act);
        return act;
    }

    public enum MsgType{
        HELP_CARD,
        CHAT_MSG,
        OTHER;
    }

    public static class Builder{
        private String VictimName;
        private String VictimNumber;
        private String VictimImId;
        private String VictimCompany;
        private String VictimImageUrl;
        private String VictimLastKnownActivity;//eg CAB_BOARDED,WALKING etc
        private String IsSelfDriven;//if true,below driver's detail will be null
        private String DriverName;
        private String DriverNum;
        private String CarNumber;
        private String CarMake;//Like Meru Dezire,Ola Indica OR Swift etc
        private String ChannelName;// Parse channel on which helpers can chat
        private String GLink;//glympse link
        private String MessageType;

        public Notification build(){
            return new Notification(this);
        }

        public Builder setVictimCompany(String victimCompany) {
            VictimCompany = victimCompany;
            return this;
        }
        public void setMessageType(String messageType) {
            MessageType = messageType;
        }


        public Builder setVictimName(String victimName) {
            VictimName = victimName;
            return this;
        }

        public Builder setVictimNumber(String victimNumber) {
            VictimNumber = victimNumber;
            return this;
        }

        public Builder setVictimImId(String victimImId) {
            VictimImId = victimImId;
            return this;
        }

        public Builder setVictimImageUrl(String victimImageUrl) {
            VictimImageUrl = victimImageUrl;
            return this;
        }

        public Builder setVictimLastKnownActivity(String victimLastKnownActivity) {
            VictimLastKnownActivity = victimLastKnownActivity;
            return this;
        }

        public Builder setIsSelfDriven(String isSelfDriven) {
            IsSelfDriven = isSelfDriven;
            return this;
        }

        public Builder setDriverName(String driverName) {
            DriverName = driverName;
            return this;
        }

        public Builder setDriverNum(String driverNum) {
            DriverNum = driverNum;
            return this;
        }

        public Builder setCarNumber(String carNumber) {
            CarNumber = carNumber;
            return this;
        }

        public Builder setCarMake(String carMake) {
            CarMake = carMake;
            return this;
        }

        public Builder setChannelName(String channelName) {
            ChannelName = channelName;
            return this;
        }

        public Builder setGLink(String GLink) {
            this.GLink = GLink;
            return this;
        }


    }
}
