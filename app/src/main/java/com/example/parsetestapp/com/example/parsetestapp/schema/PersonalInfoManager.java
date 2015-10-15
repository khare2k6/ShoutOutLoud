package com.example.parsetestapp.com.example.parsetestapp.schema;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.parsetestapp.R;

/**
 * Created by AKhare on 24-Dec-14.
 */
public class PersonalInfoManager {
    public static String getMyName(Context context){
        return getValue(context, context.getResources().getString(R.string.pref_my_name_key));
    }

    public static String getMyNumber(Context context){
        return getValue(context, context.getResources().getString(R.string.pref_my_num_key));
    }

    public static String getMyCarNum(Context context){
        return getValue(context, context.getResources().getString(R.string.pref_my_car_num_key));
    }

    public static Person getMyInfoObject(Context context){
        String myName = getMyName(context);
        String myNumber = getMyNumber(context);
        String myEmail = getValue(context, context.getResources().getString(R.string.pref_my_email_key));
        Person MyInfo = new Person(myName,myNumber);
        MyInfo.setmImId(myEmail);
        return MyInfo;
    }

    public static Vehicle getMyCarInfoObject(Context context){
        String myCarNum = getValue(context, context.getResources().getString(R.string.pref_my_car_num_key));
        String myCarName = getValue(context, context.getResources().getString(R.string.pref_my_car_make_key));
        return new Vehicle(myCarNum,myCarName);
   }

    private static String getValue(Context context,String key){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(key,"null");
    }
}
