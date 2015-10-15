package com.example.parsetestapp.com.example.parsetestapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.parsetestapp.R;
import com.example.parsetestapp.com.example.parsetestapp.schema.ChatMessage;
import com.example.parsetestapp.com.example.parsetestapp.schema.Glympse;
import com.example.parsetestapp.com.example.parsetestapp.schema.HelpRequest;
import com.example.parsetestapp.com.example.parsetestapp.schema.HelpRequestManager;
import com.example.parsetestapp.com.example.parsetestapp.schema.Messages;
import com.example.parsetestapp.com.example.parsetestapp.schema.Person;
import com.example.parsetestapp.com.example.parsetestapp.schema.Server;
import com.example.parsetestapp.com.example.parsetestapp.schema.Trip;
import com.example.parsetestapp.com.example.parsetestapp.schema.TripManager;
import com.example.parsetestapp.com.example.parsetestapp.schema.Vehicle;

public class HelpCardActivity extends Activity implements View.OnClickListener{
    HelpRequest mRequest;
    private TextView mTvVictimName;
    private TextView mTvVictimNum;
    private TextView mTvVictimIm;
    private TextView mTvDrvierName;
    private TextView mTvDriverNum;
    private TextView mTvCabNum;
    private TextView mTvCabName;
    private TextView mTvLastActivity;
    private Button mBtnNavigateMe,mBtnJoinChat;
    private Trip mTripForThisRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_card);
        mTvVictimName = (TextView)findViewById(R.id.tv_contact_name);
        mTvVictimNum = (TextView)findViewById(R.id.tv_contact_number);
        mTvVictimIm = (TextView)findViewById(R.id.tv_contact_im);
        mTvDrvierName = (TextView)findViewById(R.id.tv_driver_name);
        mTvDriverNum = (TextView)findViewById(R.id.tv_driver_num);
        mTvCabNum = (TextView)findViewById(R.id.tv_car_num);
        mTvCabName = (TextView)findViewById(R.id.tv_car_make);
        mTvLastActivity = (TextView)findViewById(R.id.tv_last_activity);
        mBtnNavigateMe = (Button) findViewById(R.id.btn_navigate_to_victim);
        mBtnJoinChat = (Button) findViewById(R.id.btn_join_chat);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        mRequest = intent.getParcelableExtra(Messages.MESSAGE_TYPE_VALUE_HELP_CARD);
        mTripForThisRequest = mRequest.getmTrip();

        Person victim = mTripForThisRequest.getmTraveller();
        mTvVictimName.setText(victim.getmName());
        mTvVictimNum.setText(victim.getmPhoneNumber());
        mTvVictimIm.setText((victim.getmImId()));

        Person driver = mTripForThisRequest.getmDriver();
        mTvDrvierName.setText(driver.getmName());
        mTvDriverNum.setText(driver.getmPhoneNumber());

        Vehicle vehicleDetails = mTripForThisRequest.getmVehicle();
        mTvCabName.setText(vehicleDetails.getmModelName());
        mTvCabNum.setText(vehicleDetails.getmNumber());
        mBtnNavigateMe.setOnClickListener(this);
        mBtnJoinChat.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_help_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_navigate_to_victim:

                String url = mTripForThisRequest.getmGLink();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                break;

            case R.id.btn_join_chat:
                Server.joinChatForThisRequest(mRequest.getmNotificationChannel());

                Intent intent = new Intent(this,ChatWindowActivity.class);
                intent.putExtra(Messages.CHANNEL_NAME,mRequest.getmNotificationChannel());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }
}
