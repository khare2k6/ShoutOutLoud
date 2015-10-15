package com.example.parsetestapp.com.example.parsetestapp.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.parsetestapp.R;
import com.example.parsetestapp.com.example.parsetestapp.schema.Glympse;
import com.example.parsetestapp.com.example.parsetestapp.schema.HelpRequest;
import com.example.parsetestapp.com.example.parsetestapp.schema.HelpRequestManager;
import com.example.parsetestapp.com.example.parsetestapp.schema.LocationManagerImpl;
import com.example.parsetestapp.com.example.parsetestapp.schema.Messages;
import com.example.parsetestapp.com.example.parsetestapp.schema.Trip;
import com.example.parsetestapp.com.example.parsetestapp.schema.TripManager;
import com.glympse.android.lite.GTicketLite;

/**
 * Created by AKhare on 28-Dec-14.
 */
public class SubMenuFragmentMyTrips extends Fragment implements View.OnClickListener,HelpRequestManager.INewHelpReqCreationListner{
    private TextView mTvRequestSentSource,mTvRequestSentDestination,mTvRequestSentStatus,mTvRequestSentTime;
    private Button mBtnViewSentRequest,mBtnExpireSentRequest,mBtnSendAgainSentRequest,mBtnSendRequest,mBtnDeleteReq;
    private LinearLayout mContainerTripDetails;
    private Context mContext;
    private String MSG_CREATE_MAP_LINK_TITTLE = "Generating Map Link";
    private String MSG_CREATE_MAP_LINK_TEXT = "Generating Map Link, please wait..";
    private ProgressDialog mProgressDialog;
    private String TAG = SubMenuFragmentMyTrips.class.getSimpleName();
    private LinearLayout mAddNewTripContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_fragment_my_trips, null);
        //Sent request container views
        Log.d(TAG,"onCreateView");
        mContext = getActivity();
        mBtnViewSentRequest = (Button) v.findViewById(R.id.btn_fragment_my_trips_view_trip);
        mBtnExpireSentRequest = (Button) v.findViewById(R.id.btn_sent_request_expire);
        mBtnSendAgainSentRequest = (Button) v.findViewById(R.id.btn_sent_request_send_again);
        mBtnSendRequest = (Button) v.findViewById(R.id.btn_fragment_my_trips_send_help_request);
        mBtnDeleteReq = (Button) v.findViewById(R.id.btn_fragment_my_trips_delete_trip);


        mTvRequestSentSource = (TextView) v.findViewById(R.id.tv_sent_request_source);
        mTvRequestSentDestination = (TextView) v.findViewById(R.id.tv_sent_request_destination);
        mTvRequestSentStatus = (TextView) v.findViewById(R.id.tv_sent_request_status);
        mTvRequestSentTime = (TextView) v.findViewById(R.id.tv_sent_request_time_left);
        mContainerTripDetails = (LinearLayout) v.findViewById(R.id.layout_sent_requests_container);
        mAddNewTripContainer = (LinearLayout) v.findViewById(R.id.container_empty_button);
        mProgressDialog = new ProgressDialog(mContext);

        return v;
    }

    private ShowView checkState(){
        HelpRequest sentRequst = HelpRequestManager.getmInstance().getRecentlySentHelpRequest();
        Trip trip = TripManager.getInstance().getTrip();
        if(sentRequst != null){
            return ShowView.REQUEST_SENT;
        }else if(trip != null){
            return ShowView.TRIP_CREATED;
        }
        return ShowView.EMPTY;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG,"onAttach");
        HelpRequestManager.getmInstance().registerRequestCreatedListner(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG,"onDetach");
        HelpRequestManager.getmInstance().removeListener(this);
    }

    public void updateViews(){
      switch(checkState()){
          case REQUEST_SENT:
            Log.d(TAG,"updateViews REQUEST_SENT");
              Trip tripSent = HelpRequestManager.getmInstance().getRecentlySentHelpRequest().getmTrip();
              GTicketLite ticket = tripSent.getGlympseTicket();
              long startTime = ticket.getStartTime();
              long endTime = ticket.getExpireTime();
              long currentTime= Glympse.getInstance(mContext).getGlympseTime();

              mContainerTripDetails.setVisibility(View.VISIBLE);
              mTvRequestSentSource.setText(tripSent.getmSource());
              mTvRequestSentDestination.setText(tripSent.getmDestination());
              mTvRequestSentStatus.setText(mContext.getResources().getString(R.string.title_active));
              mTvRequestSentTime.setText(Glympse.convertTime(endTime - currentTime));

              mBtnSendRequest.setVisibility(View.GONE);
              mBtnDeleteReq.setVisibility(View.GONE);
              mBtnExpireSentRequest.setVisibility(View.VISIBLE);
              mBtnSendAgainSentRequest.setVisibility(View.VISIBLE);
              mAddNewTripContainer.setVisibility(View.GONE);
              break;

          case TRIP_CREATED:
              Log.d(TAG,"updateViews TRIP_CREATED");
              Trip tripCreated = TripManager.getInstance().getTrip();

              mContainerTripDetails.setVisibility(View.VISIBLE);
              mTvRequestSentSource.setText(tripCreated.getmSource());
              mTvRequestSentDestination.setText(tripCreated.getmDestination());
              mTvRequestSentStatus.setText(mContext.getResources().getString(R.string.title_double_dash));
              mTvRequestSentTime.setText(mContext.getResources().getString(R.string.title_double_dash));

              mBtnSendRequest.setVisibility(View.VISIBLE);
              mBtnDeleteReq.setVisibility(View.VISIBLE);
              mBtnExpireSentRequest.setVisibility(View.GONE);
              mBtnSendAgainSentRequest.setVisibility(View.GONE);
              mAddNewTripContainer.setVisibility(View.GONE);
              break;

          case EMPTY:
              Log.d(TAG,"updateViews EMPTY");
              mAddNewTripContainer.setVisibility(View.VISIBLE);
              mContainerTripDetails.setVisibility(View.GONE);
              break;
      }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");

        mBtnSendRequest.setOnClickListener(this);
        mBtnDeleteReq.setOnClickListener(this);
        mBtnExpireSentRequest.setOnClickListener(this);
        mBtnSendAgainSentRequest.setOnClickListener(this);
        mBtnViewSentRequest.setOnClickListener(this);
        mAddNewTripContainer.setOnClickListener(this);
        updateViews();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
        //HelpRequestManager.getmInstance().removeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_fragment_my_trips_view_trip:
                break;

            case R.id.btn_fragment_my_trips_send_help_request:
                HelpRequestManager.getmInstance().createNewHelpRequest(mContext);
                mProgressDialog.setMessage(MSG_CREATE_MAP_LINK_TEXT);
                mProgressDialog.setTitle(MSG_CREATE_MAP_LINK_TITTLE);
                mProgressDialog.setCancelable(true);
                mProgressDialog.show();
                Log.d(TAG,"progree dislog is showing:"+mProgressDialog.isShowing());
                break;

            case R.id.btn_sent_request_send_again:
                Location location = LocationManagerImpl.getInstance().getCurrentLocation();
                HelpRequest sentRequest = HelpRequestManager.getmInstance().getRecentlySentHelpRequest();
                Messages.sendHelpRequest(location.getLatitude(),location.getLongitude(),sentRequest);
                break;

            case R.id.btn_fragment_my_trips_delete_trip:
                TripManager.getInstance().removeTrip();
                break;

            case R.id.btn_sent_request_expire:
                Log.d(TAG,"Expire Clicked");
                HelpRequest request = HelpRequestManager.getmInstance().getRecentlySentHelpRequest();
                GTicketLite ticket = request.getmTrip().getGlympseTicket();
                Glympse.getInstance(mContext).stopAndExpireGlympse(ticket);
                HelpRequestManager.getmInstance().removeHelpRequestSent();
                break;

            case R.id.container_empty_button:
                Intent intent = new Intent(mContext,TripInformationActivity.class);
                startActivity(intent);
                break;
        }
        updateViews();
    }

    @Override
    public void notifyNewHelpRequestCreated(HelpRequest request) {
        Log.d(TAG, "Request obj received:" + request+" progress dialog showing:"+mProgressDialog.isShowing());
        Log.d(TAG,"Channel name:"+request.getmNotificationChannel());
        Location location = LocationManagerImpl.getInstance().getCurrentLocation();
        Log.d(TAG,"getCurrentLocation returned..lat="+location.getLatitude()+" long:"+location.getLongitude());
        Messages.sendHelpRequest(location.getLatitude(),location.getLongitude(), request);
        if(mProgressDialog !=null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
           // mProgressDialog = null;
        }
        updateViews();
    }

    public enum ShowView{
        REQUEST_SENT,
        TRIP_CREATED,
        EMPTY;
    }
}
