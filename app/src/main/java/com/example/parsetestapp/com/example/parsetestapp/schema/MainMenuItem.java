package com.example.parsetestapp.com.example.parsetestapp.schema;

import android.content.Context;
import android.graphics.drawable.Drawable;


import com.example.parsetestapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AKhare on 28-Dec-14.
 */
public class MainMenuItem {
    private Drawable mIconId;
    private String mItemName;
    private String mItemSubtitle;

    public  MainMenuItem(Drawable icon,String itemName,String subtitle){
        this.mIconId = icon;
        this.mItemName = itemName;
        this.mItemSubtitle = subtitle;
    }

    public Drawable getmIcon() {
        return mIconId;
    }

    public String getmItemSubtitle() {
        return mItemSubtitle;
    }

    public String getmItemName() {
        return mItemName;
    }
    public static List<MainMenuItem> getMenuItems(Context context){
        List<MainMenuItem> list = new ArrayList<MainMenuItem>();
        // Received List menu option
        list.add(new MainMenuItem(context.getResources().getDrawable(R.drawable.ico_received_requests),
                context.getResources().getString(R.string.title_main_menu_received_list),
                context.getResources().getString(R.string.title_main_menu_received_list_subtitle)));
        //Sent list menu option
        list.add(new MainMenuItem(context.getResources().getDrawable(R.drawable.ico_my_trips),
                context.getResources().getString(R.string.title_main_menu_sent_list),
                context.getResources().getString(R.string.title_main_menu_sent_list_subtitle)));
        //Locate me, gets current location of the user
       /* list.add(new MainMenuItem(context.getResources().getDrawable(R.drawable.icon_locate_me),
                context.getResources().getString(R.string.title_main_menu_locate_me),
                context.getResources().getString(R.string.title_main_menu_locate_me_subtitle)));

        list.add(new MainMenuItem(context.getResources().getDrawable(R.drawable.ico_settings),
                context.getResources().getString(R.string.title_main_menu_settings),
                context.getResources().getString(R.string.title_main_menu_settings_subtitle)));*/

        return list;
    }
}
