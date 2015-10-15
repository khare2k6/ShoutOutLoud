package com.example.parsetestapp.com.example.parsetestapp.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.parsetestapp.R;
import com.example.parsetestapp.com.example.parsetestapp.schema.MainMenuItem;

import java.util.List;

/**
 * Created by AKhare on 28-Dec-14.
 */
public class MainMenuCustomAdapter extends ArrayAdapter<MainMenuItem> {

    private static final String TAG = MainMenuCustomAdapter.class.getSimpleName();
    private LayoutInflater mInflater;
    private List<MainMenuItem> mMenuItems;

    public MainMenuCustomAdapter(Context context,List<MainMenuItem> objects) {
        super(context,android.R.layout.simple_list_item_1 , R.layout.layout_row_main_activity_menu, objects);
        mMenuItems = objects;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if(rowView == null ){
            rowView = mInflater.inflate(R.layout.layout_row_main_activity_menu, parent, false);
            ViewHolder vholder = new ViewHolder();
            vholder.iv_item_icon = (ImageView)rowView.findViewById(R.id.iv_main_menu_item_icon);
            vholder.tv_item_name = (TextView)rowView.findViewById(R.id.tv_main_menu_item_title);
            vholder.tv_item_subtitle = (TextView)rowView.findViewById(R.id.tv_main_menu_item_subtitle_text);
             rowView.setTag(vholder);
        }
        ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.iv_item_icon.setBackground(mMenuItems.get(position).getmIcon());
        holder.tv_item_name.setText(mMenuItems.get(position).getmItemName());
        holder.tv_item_subtitle.setText(mMenuItems.get(position).getmItemSubtitle());
        rowView.setBackgroundColor(Color.parseColor("#CCFFFFFF"));

        return rowView;
    }

    /*
     * For Using View holder pattern
     */
    private class ViewHolder{
        ImageView iv_item_icon;
        TextView tv_item_name;
        TextView tv_item_subtitle;
    }
}
