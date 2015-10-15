package com.example.parsetestapp.com.example.parsetestapp.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.widget.EditText;


import com.example.parsetestapp.R;

import java.util.List;
import java.util.Map;

/**
 * Created by AKhare on 24-Dec-14.
 */
public class PrefActivity extends PreferenceActivity {

    private static final String TAG = PrefActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return Prefs1Fragment.class.getName().equals(fragmentName);
    }



    /**
     * Populate the activity with the top-level headers.
     */
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }
    /**
     * This fragment shows the preferences for the first header.
     */
    public static class Prefs1Fragment extends PreferenceFragment {
        SharedPreferences mPref;
        SharedPreferences.OnSharedPreferenceChangeListener mSPChangeListener = new
                SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                        Log.d(TAG, "SharedPref changed:" + key);
                        changePrefTitle(key);
                  }
                    // your stuff here
                };

        private void changePrefTitle(String key){
            Preference pref = findPreference(key);
            if (pref instanceof EditTextPreference) {
                EditTextPreference etp = (EditTextPreference) pref;
                pref.setTitle(etp.getText());
                etp.getEditText().setTypeface(null, Typeface.BOLD);
            }
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Log.d(TAG, "onCreate shared pref" );
            addPreferencesFromResource(R.xml.preference);
            mPref = getPreferenceManager().getDefaultSharedPreferences(getActivity());
            Map<String,?> preferences = mPref.getAll();
            for(Map.Entry<String,?> entry:preferences.entrySet()){
                changePrefTitle(entry.getKey());
            }
   }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            getPreferenceManager().getDefaultSharedPreferences(activity).registerOnSharedPreferenceChangeListener(mSPChangeListener);
        }

    }
}