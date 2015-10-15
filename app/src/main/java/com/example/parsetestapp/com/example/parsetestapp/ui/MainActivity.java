package com.example.parsetestapp.com.example.parsetestapp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parsetestapp.R;
import com.example.parsetestapp.com.example.parsetestapp.schema.MainMenuItem;
import com.example.parsetestapp.com.example.parsetestapp.schema.Server;
import com.example.parsetestapp.com.example.parsetestapp.schema.LocationManagerImpl;

public class MainActivity extends Activity implements OnClickListener {

    protected static final String TAG = MainActivity.class.getSimpleName();
    public static final int SHOW_RECEIVED_REQUESTS = 0;
    public static final int SHOW_MY_TRIPS= 1;
    public static final int LOCATE_ME= 2;
    public static final int SETTINGS = 3;
    public static String SHOW_FRAGMENT = "showFragment";

    LocationManager mLocationManager;
    LocationListener mLocationListener;
    Location mCurrentLocation;
    private Button mBtnLocateMe;
    private TextView mTvStatus;

    private final String MSG_LOCATION_FETCH_TITLE = "Fetch Location";
    private final String MSG_LOCATION_FETCH_MSG = "Fetching Your Current Location..";

    private ListView mLvMainMenu;
    private MainMenuCustomAdapter mMenuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
        setContentView(R.layout.layout_main);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mBtnLocateMe = (Button) findViewById(R.id.btn_getMyLocation);
        mTvStatus = (TextView) findViewById(R.id.tv_status);
        mLvMainMenu = (ListView) findViewById(R.id.lv_menu_list);
        mMenuAdapter = new MainMenuCustomAdapter(this, MainMenuItem.getMenuItems(this));
        mLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                Log.d(TAG, "onLocationChanged called");
                setStatus("Got location from GPS");
                mCurrentLocation = location;
                LocationManagerImpl.getInstance().setCurrentLocation(mCurrentLocation);
                saveLocationToServer(location);
            }

            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }

        };
    }

    private void setStatus(String msg){
        mTvStatus.setText(msg);
    }

    private void showToast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void saveLocationToServer(Location location) {
        if (location == null) {
            setStatus("Location is still null! Try again!");
            return;
        }
        setStatus(location.getLatitude()+","+location.getLongitude());
        Server.uploadLocationToServer(location);
        LocationManagerImpl.getInstance().setCurrentLocation(mCurrentLocation);
    }

    private Location getLastLocation(){
        Location lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(lastKnownLocation == null){
            Log.d(TAG,"trying last location of network provider");
            lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        return lastKnownLocation;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.menu_preference:
                intent = new Intent(this, PrefActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_locate_me:
                mCurrentLocation = getLastLocation();
                saveLocationToServer(mCurrentLocation);
                break;
//            case R.id.seeActiveChat:
//                intent = new Intent(this, ChatWindowActivity.class);
//                startActivity(intent);
  //              break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, mLocationListener);
        setStatus(Server.getCurrentUser().getUsername() + " logged in..");
        mBtnLocateMe.setOnClickListener(this);
        mLvMainMenu.setAdapter(mMenuAdapter);

        mLvMainMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG,"onItemClick on MainMenu on MainActivity");
                switch(position){
                    case SHOW_RECEIVED_REQUESTS:
                        Intent intent = new Intent(MainActivity.this,SubMenuActivity.class);
                        intent.putExtra(SHOW_FRAGMENT,position);
                        startActivity(intent);
                        break;

                    case SHOW_MY_TRIPS:
                        if(mCurrentLocation == null) {
                             mCurrentLocation = getLastLocation();
                            saveLocationToServer(mCurrentLocation);
                        }
                        Intent intent2 = new Intent(MainActivity.this,SubMenuActivity.class);
                        intent2.putExtra(SHOW_FRAGMENT,position);
                        startActivity(intent2);
                        break;

                    case LOCATE_ME:
                        mCurrentLocation = getLastLocation();
                        saveLocationToServer(mCurrentLocation);
                        break;

                    case SETTINGS:
                        intent = new Intent(MainActivity.this, PrefActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_getMyLocation:
                mCurrentLocation = getLastLocation();
                saveLocationToServer(mCurrentLocation);
                break;

        }
    }
}
