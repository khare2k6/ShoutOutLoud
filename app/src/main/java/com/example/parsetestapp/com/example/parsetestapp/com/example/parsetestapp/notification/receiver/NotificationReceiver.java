package com.example.parsetestapp.com.example.parsetestapp.com.example.parsetestapp.notification.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.parsetestapp.R;
import com.example.parsetestapp.com.example.parsetestapp.schema.ChatMessage;
import com.example.parsetestapp.com.example.parsetestapp.schema.HelpRequest;
import com.example.parsetestapp.com.example.parsetestapp.schema.HelpRequestManager;
import com.example.parsetestapp.com.example.parsetestapp.schema.Messages;
import com.example.parsetestapp.com.example.parsetestapp.ui.ChatWindowActivity;
import com.example.parsetestapp.com.example.parsetestapp.ui.HelpCardActivity;
import com.parse.ParsePushBroadcastReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akhare on 22-Dec-14.
 */
public class NotificationReceiver extends ParsePushBroadcastReceiver {
    private String TAG = NotificationReceiver.class.getSimpleName();
    private final String EXTRA_CHANNEL = "com.parse.Channel";
    private final String EXTRA_DATA = "com.parse.Data";
    private Notification mforegroundNotification;
    NotificationManager mNotificationMgr;


    @Override
    public void onReceive (Context context, Intent intent){
        Log.d(TAG, "onReceived called");
        String action = intent.getAction();
        String channel = intent.getExtras().getString(EXTRA_CHANNEL);
        String dataRx = intent.getExtras().getString(EXTRA_DATA);
        Log.d(TAG, "OnReceiveddataRx:"+dataRx);
        Messages msg = Messages.getReceivedHelpRequest(dataRx);
        Log.d(TAG,"msg type:"+msg.getHelpRequest());
        if(Messages.MESSAGE_TYPE_VALUE_HELP_CARD.equals(msg.getmMessageType())){
            Log.d(TAG,"New help card request->channelName:"+msg.getHelpRequest().getmNotificationChannel());
            HelpRequestManager.getmInstance().addHelpRequestReceived(msg.getHelpRequest().getmNotificationChannel(),msg.getHelpRequest());
            Intent acitivityIntent = new Intent(context, HelpCardActivity.class);
            acitivityIntent.putExtra(Messages.MESSAGE_TYPE_VALUE_HELP_CARD,msg.getHelpRequest());
            showNotification(context,acitivityIntent,Messages.MESSAGE_TYPE_VALUE_HELP_CARD);
        }else{
            Log.d(TAG,"Not help request,must be chat msg");
            Log.d(TAG,msg.getChatMessage().getmUserName()+" says "+msg.getChatMessage().getmMessage()+" channel="+msg.getChatMessage().getmChanngelName());
            HelpRequest helpRequest = HelpRequestManager.getmInstance().getHelpRequestReceived(msg.getChatMessage().getmChanngelName());
            List<ChatMessage> chatList = helpRequest.getChatMessagesList();
            chatList.add(msg.getChatMessage());

            //send chat msg intent
            Intent intentChatMsg = new Intent(ChatWindowActivity.ACTION_NEW_CHAT_MSG_RECEIVED);
            intentChatMsg.putExtra(Messages.CHANNEL_NAME,msg.getChatMessage().getmChanngelName());
            context.sendBroadcast(intentChatMsg);
        }
    }

    public void showNotification(Context context,Intent intent,String notificationType) {
        mforegroundNotification = getForegroundNotification(context,intent,notificationType);
        mforegroundNotification.defaults |= Notification.DEFAULT_SOUND;
        mforegroundNotification.defaults |= Notification.DEFAULT_LIGHTS;
        mforegroundNotification.defaults |= Notification.DEFAULT_VIBRATE;
        mforegroundNotification.defaults |= Notification.FLAG_INSISTENT;
        Log.d(TAG, "sending notification..");
        mNotificationMgr.notify(1, mforegroundNotification);
    }

    private Notification getForegroundNotification(Context context,Intent intent,String notificationType){

        mNotificationMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        String notificationTitle,notificationText;
        if(Messages.MESSAGE_TYPE_VALUE_HELP_CARD.equals(notificationType)){
            notificationTitle = "Help Needed!";
            notificationText = " You are nearby victim..";
        }else{
            notificationTitle = "Chat message";
            notificationText = "new ping received";
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.push_icon)
                .setContentIntent(pIntent);
        return notificationBuilder.build();
    }
}
