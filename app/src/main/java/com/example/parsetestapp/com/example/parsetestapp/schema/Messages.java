package com.example.parsetestapp.com.example.parsetestapp.schema;

import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.util.HashMap;

/**
 * Created by AKhare on 24-Dec-14.
 */
public class Messages {
    public static final String MESSAGE_TYPE_VALUE_HELP_CARD = "HelpCard";
    public static final String MESSAGE_TYPE_VALUE_CHAT_MSG = "ChatMsg";
    private static final String COLON = ":";
    public static final String MESSAGE_TYPE_KEY = "Message";
    public static final String CHANNEL_NAME = "Channel";
    private static final String TAG = Messages.class.getSimpleName();

    public String getmMessageType() {
        return mMessageType;
    }

    public HelpRequest getHelpRequest() {
        return mRequest;
    }

    public ChatMessage getChatMessage() {
        return mMessage;
    }
    @Expose
    private String mMessageType;
    @Expose
    private HelpRequest mRequest;
    @Expose
    private ChatMessage mMessage;//incase of chat message

    public Messages(String msgType,HelpRequest request,ChatMessage chatMsg){
        mMessageType = msgType;
        mRequest = request;
        mMessage = chatMsg;
    }

    public static void sendHelpRequest(double latitude,double longitude,HelpRequest request){
        Log.d(TAG,"sendHelpRequest..");
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        Messages msgToBeSent = new Messages(MESSAGE_TYPE_VALUE_HELP_CARD,request,null);
        String jsonToBeSent = gson.toJson(msgToBeSent);
        Log.d(TAG, "Json request to be sent:"+jsonToBeSent);

        HashMap<String, Object> values = new HashMap<String, Object>();
        values.put("Latitude", latitude);
        values.put("Longitude", longitude);
        values.put(MESSAGE_TYPE_KEY,jsonToBeSent);
        Server.sendHelpRequestNow(values);
    }

    public static void sendChatMsg(String msg,String channelName){
        Log.d(TAG,"sendChatMsg..");
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        Messages msgToBeSent = new Messages(MESSAGE_TYPE_VALUE_CHAT_MSG,null,new ChatMessage(msg,channelName));
        String jsonToBeSent = gson.toJson(msgToBeSent);
        Log.d(TAG, "Json request to be sent:"+jsonToBeSent);

        HashMap<String, Object> values = new HashMap<String, Object>();
        values.put(MESSAGE_TYPE_KEY,jsonToBeSent);
        values.put(CHANNEL_NAME,channelName);
        Server.sendChatMessage(values);
    }

    public static Messages getReceivedHelpRequest(String json){
        Gson gson = new Gson();
        Messages msg = gson.fromJson(json,Messages.class);
        return msg;
    }
}
