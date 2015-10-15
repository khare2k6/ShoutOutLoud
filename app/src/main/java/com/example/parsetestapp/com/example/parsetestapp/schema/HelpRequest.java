package com.example.parsetestapp.com.example.parsetestapp.schema;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akhare on 22-Dec-14.
 */
public class HelpRequest implements Parcelable{

    @Expose
    private Trip mTrip;
    @Expose
    private String mNotificationChannel;//so that people can chat using this channel via parse
    @Expose
    private String mUniqueId;


    private List<ChatMessage> mChatMessagesList;

    public HelpRequest(Trip trip,String channel,String id){
        mTrip = trip;
        mNotificationChannel = channel;
        mUniqueId = id;
    }

    public String getmUniqueId() {
        return mUniqueId;
    }

    public String getmNotificationChannel() {
        return mNotificationChannel;
    }

    public Trip getmTrip() {
        return mTrip;
    }

    private HelpRequest(Parcel in){
        this.mTrip = in.readParcelable(Trip.class.getClassLoader());
        this.mNotificationChannel = in.readString();
        this.mUniqueId = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public List<ChatMessage> getChatMessagesList() {
        if(mChatMessagesList == null){
            Log.d("HelpRequest", "creating new chatList for HelpReq with channelName:" + this.mNotificationChannel);
            mChatMessagesList = new ArrayList<ChatMessage>();
        }
        return mChatMessagesList;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mTrip,flags);
        dest.writeString(mNotificationChannel);
        dest.writeString(mUniqueId);
    }

    public static Creator<HelpRequest> CREATOR = new Creator<HelpRequest>(){

        @Override
        public HelpRequest createFromParcel(Parcel source) {
            return new HelpRequest(source);
        }

        @Override
        public HelpRequest[] newArray(int size) {
            return new HelpRequest[size];
        }
    };


}
