package com.example.parsetestapp.com.example.parsetestapp.schema;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.glympse.android.lite.GTicketLite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by AKhare on 26-Dec-14.
 */
public class HelpRequestManager {
    private HelpRequest mSentRequest;
    private HashMap<String,HelpRequest> mReceivedHelpRequesttMap;
    public static final String HIFEN = "-";
    public static final String LETTER_A = "A";
    public static final int MAX_FIVE_DIGITS = 5;
    private static HelpRequestManager mInstance = new HelpRequestManager();
    private List<INewHelpReqCreationListner> mRegistrationList;
    private HelpRequestManager(){
        mSentRequest = null;
        mReceivedHelpRequesttMap = new HashMap<String,HelpRequest>();
        mRegistrationList = new ArrayList<INewHelpReqCreationListner>();
  }

    public static HelpRequestManager getmInstance(){

        return mInstance;
    }

    public void registerRequestCreatedListner(INewHelpReqCreationListner listener){
        if(!mRegistrationList.contains(listener)) {
            mRegistrationList.add(listener);
        }
    }

    public void removeListener(INewHelpReqCreationListner listener){
        mRegistrationList.remove(listener);
    }
    /**
     * Generates new channel name on which
     * other devices can further subscribe for
     * chatting purpose
     * @param trip
     * @return
     */
    public  String generateAngGetChannelName(Context context,Trip trip){
        String channel ;
        String uName = PersonalInfoManager.getMyName(context);
        String number = PersonalInfoManager.getMyNumber(context);
        if(TextUtils.isEmpty(uName)){
           return channel = LETTER_A + new Random().nextInt(MAX_FIVE_DIGITS);
        }
        channel = uName+HIFEN+number;
        return channel;
    }

    public String generateUniqueId(Trip trip){
        String id = trip.getmSource()+new Random().nextInt(MAX_FIVE_DIGITS);
        return id;
    }
    /**
     * Generates new Help request out of existing Trip
     * to be sent to the server & other near by devices
     * @return
     */
    public void createNewHelpRequest(final Context context){
        String glympseLink = "";
        Glympse.getInstance(context).kickStartGlympse(context, new Glympse.IGlympseLinkListener() {
            @Override
            public void notifyLinkCreateion(String url, GTicketLite gTicket) {
                Trip trip = TripManager.getInstance().getTrip();
                trip.setGLink(url);
                trip.setGlympseTicket(gTicket);
                trip.setmTimeTillExpiry(gTicket.getExpireTime() - gTicket.getStartTime());
                String channel = generateAngGetChannelName(context,trip);
                String uniqueId = generateUniqueId(trip);
                HelpRequest request = new HelpRequest(trip, channel, uniqueId);
                //addHelpRequestSent(uniqueId, request);
                addHelpRequestSent(channel, request);//for chat purpose,let the unique id be its channelName
                for (INewHelpReqCreationListner listener : mRegistrationList) {
                    listener.notifyNewHelpRequestCreated(request);
                }
            }
        });


    }

    /**
     * Add this new Help request to be sent to the server
     * in the send list
     * @param id
     * @param req
     */
    public void addHelpRequestSent(String id,HelpRequest req){
        mSentRequest = req;
    }

    public void removeHelpRequestSent(){
        mSentRequest = null;
        TripManager.getInstance().removeTrip();
    }

    public void addHelpRequestReceived(String id,HelpRequest req){
        mReceivedHelpRequesttMap.put(id, req);
    }

    public HelpRequest getRecentlySentHelpRequest(){
       return mSentRequest;
    }

    public HelpRequest getHelpRequestReceived(String id){
       return mReceivedHelpRequesttMap.get(id);
    }

    public HelpRequest removeHelpRequestReceived(String id){
        return mReceivedHelpRequesttMap.remove(id);
    }

    public interface INewHelpReqCreationListner{
        public void notifyNewHelpRequestCreated(HelpRequest request);
    }

    public List<HelpRequest>getAllReceivedHelpRequests(){
        return new ArrayList<HelpRequest>(mReceivedHelpRequesttMap.values());
    }

    public int getReceivedRequestsCount(){
        return mReceivedHelpRequesttMap.size();
    }
}
