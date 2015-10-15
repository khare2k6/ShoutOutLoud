package com.example.parsetestapp.com.example.parsetestapp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parsetestapp.R;
import com.example.parsetestapp.com.example.parsetestapp.schema.Notification;
import com.example.parsetestapp.com.example.parsetestapp.schema.Person;
import com.example.parsetestapp.com.example.parsetestapp.schema.PersonalInfoManager;
import com.example.parsetestapp.com.example.parsetestapp.schema.Trip;
import com.example.parsetestapp.com.example.parsetestapp.schema.TripManager;
import com.example.parsetestapp.com.example.parsetestapp.schema.Vehicle;

public class TripInformationActivity extends Activity implements View.OnClickListener {

    private TableRow mDriverNameRow,mDriverNumRow,mCabNameRow,mCabNumRow;
    private EditText mEtDriverName,mEtDriverNum,mEtCabName,mEtCabNum,mEtSource,mEtDestination;
    private CheckBox mCbSelfDriven;
    private Spinner mSpnActivityType;
    private TripManager mTripMgr;
    private TableLayout mDriverInfoLayout;
    private ArrayAdapter<String> mSpnAcitivityTypeAdapter;
    private Trip.LastActivity mLastActivity;
    private final String TAG = TripInformationActivity.class.getSimpleName();
    private Button mBtnSubmitTripInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_information);
        mTripMgr = TripManager.getInstance();
        mCbSelfDriven = (CheckBox) findViewById(R.id.cb_self_driven);
        mBtnSubmitTripInfo = (Button) findViewById(R.id.btn_submit_trip_info);

        mEtDriverName = (EditText) findViewById(R.id.et_driver_name);
        mEtDriverNum = (EditText) findViewById(R.id.et_driver_num);
        mEtCabName = (EditText) findViewById(R.id.et_cab_make);
        mEtCabNum = (EditText) findViewById(R.id.et_cab_num);
        mEtSource = (EditText) findViewById(R.id.et_source);
        mEtDestination = (EditText) findViewById(R.id.et_Destination);

        mDriverNameRow = (TableRow) findViewById(R.id.tripTableRowDriverName);
        mDriverNumRow = (TableRow) findViewById(R.id.tripTableRowDriverNumber);
        mCabNameRow = (TableRow) findViewById(R.id.tripTableRowCabMakeModel);
        mCabNumRow = (TableRow) findViewById(R.id.tripTableRowCabNumber);
        mSpnActivityType = (Spinner) findViewById(R.id.spn_activity);
        mSpnActivityType.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, getAcitivityTypeArray()));

    }

    private String[] getAcitivityTypeArray(){
        Trip.LastActivity [] actTypes = Trip.LastActivity.values();
        String [] arr = new String[actTypes.length];
        for(int i =0;i< actTypes.length;i++){
            arr[i] = actTypes[i].getName();
        }
        return arr;
    }
    @Override
    protected void onResume() {
        super.onResume();

        mCbSelfDriven.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "onChecked:" + isChecked);
                updateViews();
            }
        });
        mBtnSubmitTripInfo.setOnClickListener(this);
        mSpnActivityType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = ((TextView)view).getText().toString();
                Log.d(TAG,"Selected Val:"+selectedValue);
                mLastActivity = Notification.getLastActivity(selectedValue);
                updateViews();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        updateViews();
    }

    /**
     * Updates the views of this activity according to
     * the selection of activity type of user and isDriven
     * value
     */
    private void updateViews(){
        if(mLastActivity != null && Trip.LastActivity.WALKING.equals(mLastActivity)){
            //user is walking, hide every other view
            mCbSelfDriven.setVisibility(View.INVISIBLE);
            mDriverNameRow.setVisibility(View.INVISIBLE);
            mDriverNumRow.setVisibility(View.INVISIBLE);
            mCabNameRow.setVisibility(View.INVISIBLE);
            mCabNumRow.setVisibility(View.INVISIBLE);
        }else if(mCbSelfDriven.isChecked()){
            //user is driving himself,hide driver details
            mCbSelfDriven.setVisibility(View.VISIBLE);
            mDriverNameRow.setVisibility(View.INVISIBLE);
            mDriverNumRow.setVisibility(View.INVISIBLE);
            mCabNameRow.setVisibility(View.INVISIBLE);
            mCabNumRow.setVisibility(View.INVISIBLE);
        }else{
            //user is taking cab,show driver related views
            mCbSelfDriven.setVisibility(View.VISIBLE);
            mDriverNameRow.setVisibility(View.VISIBLE);
            mDriverNumRow.setVisibility(View.VISIBLE);
            mCabNameRow.setVisibility(View.VISIBLE);
            mCabNumRow.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trip_information, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      switch(id){
          case R.id.menu_add_trip:
              if(!isInputValid()){
                  showToast("Source & Destination not proper");
                  return true;
              }

              Trip trip = getNewTrip();
              Log.d(TAG,"Trip obj:"+trip.toString());
              TripManager.getInstance().addTrip(trip);
              showToast("Trip added successfully");
              finish();
              break;

      }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Form Person Object of Driver from views
     * @return Driver's Person object
     */
    private Person getDriverInfo(){
        String driverName = mEtDriverName.getText().toString();
        String driverNum = mEtDriverNum.getText().toString();
        return new Person(driverName,driverNum);
    }

    /**
     * Form Cab's Vehicle object from the views
     * @return Cab's Vehicle Object
     */
    private Vehicle getCabInfo(){
        String cabName = mEtCabName.getText().toString();
        String cabNum = mEtCabNum.getText().toString();
        return new Vehicle(cabNum,cabName);
    }

    /**
     * gets new Trip object to be added in TripManager
     * @return
     */
    private Trip getNewTrip(){
        Person traveller = PersonalInfoManager.getMyInfoObject(TripInformationActivity.this);
        Trip.Builder builder = new Trip.Builder();
        switch(mLastActivity){
            case WALKING:
                    builder.setTraveller(traveller)
                    .setLastActivity(mLastActivity);
                    return builder.build();
            default:
                Person driver ;
                Vehicle vehicle;
                if(mCbSelfDriven.isChecked()){
                    vehicle = PersonalInfoManager.getMyCarInfoObject(TripInformationActivity.this);
                    driver = traveller;
                }else{
                    vehicle = getCabInfo();
                    driver = getDriverInfo();
                }
                return builder.setSource(mEtSource.getText().toString())
                        .setDestination(mEtDestination.getText().toString())
                        .setTraveller(traveller)
                        .setDriver(driver)
                        .setVehicle(vehicle)
                        .setLastActivity(mLastActivity)
                        .build();
        }

    }
    private void showToast(String msg){
        Toast.makeText(TripInformationActivity.this,msg,Toast.LENGTH_SHORT).show();
    }

    private boolean isInputValid(){

       boolean isSourceValid = !TextUtils.isEmpty(mEtSource.getText().toString().trim());
       boolean isDestValid = !TextUtils.isEmpty(mEtDestination.getText().toString().trim());
        return (isSourceValid && isDestValid)?true:false;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_clear_trip_info:
                boolean deleteSuccess = TripManager.getInstance().removeTrip();
                showToast("Delete Success:"+deleteSuccess);
                break;
            case R.id.btn_submit_trip_info:
                if(!isInputValid()){
                    showToast("Source & Destination not proper");
                    return;
                }

                Trip trip = getNewTrip();
                Log.d(TAG,"Trip obj:"+trip.toString());
                TripManager.getInstance().addTrip(trip);
                showToast("Trip added successfully");
                finish();
                break;
        }
    }
}
