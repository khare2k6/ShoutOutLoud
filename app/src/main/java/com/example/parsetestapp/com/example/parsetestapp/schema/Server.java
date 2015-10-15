package com.example.parsetestapp.com.example.parsetestapp.schema;

import android.location.Location;
import android.util.Log;

import com.parse.FunctionCallback;
import com.parse.LogInCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.HashMap;

/**
 * Created by AKhare on 29-Dec-14.
 */
public class Server {

    public final static String SERVER_API_SEND_HELP_REQUEST = "notifyOthersLocationBased";
    public final static String SERVER_API_SEND_CHAT_MSG = "notifyChatMessage";
    public final static String KEY_LOCATION = "location";
    private static final String TAG = Server.class.getSimpleName();

    public static void login(String uName,String pwd, LogInCallback callback){
        ParseUser.logInInBackground(uName, pwd,callback);
    }

    public static void signup(String uName,String pwd,SignUpCallback callback){
        ParseUser user = new ParseUser();
        user.setUsername(uName);
        user.setPassword(pwd);
        user.signUpInBackground(callback);
    }

    public static ParseUser getCurrentUser(){
        return ParseUser.getCurrentUser();
    }

    public static void uploadLocationToServer(Location location){
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        ParseGeoPoint geopoint = new ParseGeoPoint(location.getLatitude(),location.getLongitude());
        installation.put(KEY_LOCATION, geopoint);
        installation.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

            }
        });
    }

    public static void sendHelpRequestNow(HashMap<String,Object> values){
        ParseCloud.callFunctionInBackground(SERVER_API_SEND_HELP_REQUEST, values, new FunctionCallback<String>() {
            public void done(String result, ParseException e) {
                if (e == null) {
//                    showToast("Cloud notifyOthersLocationBased called");
//                    setStatus("Help notified to others!");
                }
            }
        });
    }

    public static void joinChatForThisRequest(final String channelName){
        ParsePush.subscribeInBackground(channelName,new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.d(TAG,"subscribed..e = "+e);
//                /Messages.sendChatMsg("Logged in..",channelName);
            }
        });
    }

    public static void leaveChatForThisRequest(String channelName){
        ParsePush.unsubscribeInBackground(channelName);
    }

    public static void sendChatMessage(HashMap<String,Object> values){
        ParseCloud.callFunctionInBackground(SERVER_API_SEND_CHAT_MSG, values);
    }
}
