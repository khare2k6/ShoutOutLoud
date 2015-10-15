package com.example.parsetestapp.com.example.parsetestapp.schema;

import android.content.Context;
import android.util.Log;

import com.glympse.android.lite.GListenerLite;
import com.glympse.android.lite.LC;
import com.glympse.android.lite.GGlympseLite;
import com.glympse.android.lite.GInviteLite;
import com.glympse.android.lite.GTicketLite;
import com.glympse.android.lite.LiteFactory;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by AKhare on 25-Dec-14.
 */
public class Glympse {
    public static final String GLYMPSE_API_KEY ="tcMVcJ0eYUiDQcEv";
    private static Glympse mInstance = new Glympse();
    private static final int DEFAULT_TIME = 300000;
    private GTicketLite mGlympseTicket;
    private static GGlympseLite mGlympse;
    private GlympseEventListner mGEventListner;
    private String TAG = "GlympseParse";

    private Glympse(){

    };
    public static Glympse getInstance(Context context){
        mGlympse = LiteFactory.createGlympse(
                context, "sandbox.glympse.com", GLYMPSE_API_KEY);
        return mInstance;
    }


    public void kickStartGlympse(Context context,IGlympseLinkListener listener){
        mGlympseTicket= LiteFactory.createTicket(300000, "Help Required!", null);
        mGlympseTicket.addInvite(LC.INVITE_TYPE_LINK,null,null);
// Set flags that adjust the presentation of the Glympse Send Wizard.
// These can enable/disable fields in the wizard screen.
        final int WIZARD_FLAGS
                = LC.INVITE_TYPE_LINK
                | LC.SEND_WIZARD_MESSAGE_EDITABLE
                | LC.SEND_WIZARD_DESTINATION_EDITABLE
                | LC.SEND_WIZARD_TIME_EDITABLE;
// Launches the wizard which will send the Glympse
        mGEventListner = new GlympseEventListner(listener);
        mGlympse.start();
        mGlympse.addListener(mGEventListner);
        mGlympse.sendTicket(mGlympseTicket, WIZARD_FLAGS);

    }

    public void stopAndExpireGlympse(GTicketLite ticket){
        Log.d(TAG,"ticketExpire called..");
        ticket.expire();
        mGlympse.removeListener(mGEventListner);
        mGlympse.stop();
    }

    public long getGlympseTime(){
        return mGlympse.getTime();
    }
    private class GlympseEventListner implements GListenerLite
    {
        private GGlympseLite _glympse;
        private final String  TAG = "LiteListener";
        private IGlympseLinkListener mListener;

        public GlympseEventListner(IGlympseLinkListener listener){
            mListener = listener;
        }

        public void eventsOccurred(GGlympseLite glympse, int code, Object param1, Object param2)
        {
            Log.d(TAG, "EvenOccured-->:"+code+"url = ");
            if(code == com.glympse.android.lite.LC.EVENT_INVITE_URL_CREATED ) {
               Log.d(TAG,"ankit event code:"+code);
                GInviteLite invite = (GInviteLite)param2;
                Log.d(TAG," url link ..notifying= "+invite.getUrl());
                mListener.notifyLinkCreateion(invite.getUrl(),mGlympseTicket);
            }

        }
        public void subscribe()
        {
            _glympse.addListener(this);
        }
        public void unsubscribe()
        {
            _glympse.removeListener(this);
        }
    }

    public interface IGlympseLinkListener{
        public void notifyLinkCreateion(String url,GTicketLite ticket);
    }

    public static String convertTime(long millis){
        return String.format(Locale.US,"%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }
}
