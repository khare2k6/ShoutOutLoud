package com.example.parsetestapp.com.example.parsetestapp.schema;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * Created by AKhare on 29-Dec-14.
 */
public class ChatMessage implements Parcelable{
    @Expose
    private String mUserName,mMessage,mChanngelName;


    public ChatMessage(String msg,String channelName){
        mUserName = Server.getCurrentUser().getUsername();
        mMessage = msg;
        mChanngelName = channelName;
    }

    private ChatMessage(Parcel in){
        this.mUserName = in.readString();
        this.mMessage = in.readString();
        this.mChanngelName = in.readString();
    }

    public ChatMessage(String uName,String msg,String channelName){
        mUserName = uName;
        mMessage = msg;
        mChanngelName = channelName;
    }

    public String getmUserName() {
        return mUserName;
    }

    public String getmMessage() {
        return mMessage;
    }
    public String getmChanngelName() {
        return mChanngelName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUserName);
        dest.writeString(mMessage);
        dest.writeString(mChanngelName);
    }

    public static Creator<ChatMessage> CREATOR = new Creator<ChatMessage>(){
        @Override
        public ChatMessage createFromParcel(Parcel source) {
            return new ChatMessage(source);
        }

        @Override
        public ChatMessage[] newArray(int size) {
            return new ChatMessage[size];
        }
    };
}
