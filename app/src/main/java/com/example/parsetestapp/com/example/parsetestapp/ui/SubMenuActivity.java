package com.example.parsetestapp.com.example.parsetestapp.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.example.parsetestapp.R;

public class SubMenuActivity extends Activity {

    private static final String TAG = SubMenuActivity.class.getSimpleName() ;
    FragmentManager mFragmentManager;
    RelativeLayout mFragmentContainer;
    private Fragment mMyTripsFragment,mReceivedRequestFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_menu);
        mFragmentManager = getFragmentManager();
        mFragmentContainer = (RelativeLayout) findViewById(R.id.fragment_container);

        Intent intent = getIntent();
        int whichFragment = intent.getIntExtra(MainActivity.SHOW_FRAGMENT,MainActivity.SHOW_RECEIVED_REQUESTS);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        Log.d(TAG, "onResume: which Fragment:" + whichFragment);
        switch(whichFragment){
            case MainActivity.SHOW_MY_TRIPS:
                mMyTripsFragment = new SubMenuFragmentMyTrips();
                transaction.setCustomAnimations(
                    R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                    R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                        .replace(R.id.fragment_container,mMyTripsFragment,SubMenuFragmentMyTrips.class.getSimpleName());
                transaction.commit();
                break;

            case MainActivity.SHOW_RECEIVED_REQUESTS:
                mReceivedRequestFragment = new SubMenuFragmentReceivedList();
                transaction.setCustomAnimations(
                        R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                        .replace(R.id.fragment_container,mReceivedRequestFragment,SubMenuFragmentReceivedList.class.getSimpleName());
                transaction.commit();
                break;
        }
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

    }

    /*
    private void removeFragments(){
        SubMenuFragmentMyTrips fragment = (SubMenuFragmentMyTrips) mFragmentManager.findFragmentByTag(SubMenuFragmentReceivedList.class.getSimpleName());
        if(fragment !=null && fragment.isVisible()){
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.remove(fragment);
            transaction.commit();
        }
        SubMenuFragmentReceivedList fragment2 = (SubMenuFragmentReceivedList) mFragmentManager.findFragmentByTag(SubMenuFragmentReceivedList.class.getSimpleName());
        if(fragment2 !=null && fragment2.isVisible()){
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.remove(fragment2);
            transaction.commit();
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sub_menu, menu);
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
}
