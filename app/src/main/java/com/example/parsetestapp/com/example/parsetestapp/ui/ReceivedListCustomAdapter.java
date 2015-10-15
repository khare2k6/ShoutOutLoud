package com.example.parsetestapp.com.example.parsetestapp.ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.parsetestapp.R;
import com.example.parsetestapp.com.example.parsetestapp.schema.Glympse;
import com.example.parsetestapp.com.example.parsetestapp.schema.HelpRequest;
import com.example.parsetestapp.com.example.parsetestapp.schema.HelpRequestManager;
import com.example.parsetestapp.com.example.parsetestapp.schema.Messages;
import com.example.parsetestapp.com.example.parsetestapp.schema.Server;

import java.util.List;

/**
 * Created by AKhare on 28-Dec-14.
 */
public class ReceivedListCustomAdapter extends ArrayAdapter<HelpRequest> {

    private static final String TAG = ReceivedListCustomAdapter.class.getSimpleName();
    private LayoutInflater mInflater;
    private List<HelpRequest> mHelpRequestList;
    private Context mContext;

    public ReceivedListCustomAdapter(Context context,List<HelpRequest>objects) {
        super(context,android.R.layout.simple_list_item_1 , R.layout.layout_row_received_request
                , objects);
        mHelpRequestList = objects;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "getView called for position" + position+"List ="+mHelpRequestList.size());
        View rowView = convertView;
        if(rowView == null ){
            rowView = mInflater.inflate(R.layout.layout_row_received_request, parent, false);
            ViewHolder vholder = new ViewHolder();
            vholder.tv_item_sender_name = (TextView)rowView.findViewById(R.id.tv_received_request_sender);
            vholder.tv_item_location = (TextView)rowView.findViewById(R.id.tv_received_request_senders_location);
            vholder.tv_item_expiry_time = (TextView)rowView.findViewById(R.id.tv_received_request_expiry_time);
            vholder.btn_item_view_help_request = (Button)rowView.findViewById(R.id.btn_received_request_view_request);
            vholder.btn_item_delete_help_request = (Button)rowView.findViewById(R.id.btn_received_request_delete_request);
             rowView.setTag(vholder);
        }
        ViewHolder holder = (ViewHolder) rowView.getTag();
        Log.d(TAG,"Name:"+mHelpRequestList.get(position).getmTrip().getmTraveller().getmName());
        holder.tv_item_sender_name.setText(mHelpRequestList.get(position).getmTrip().getmTraveller().getmName());
        holder.tv_item_location.setText("Reverse geo code");
        holder.tv_item_expiry_time.setText(Glympse.convertTime(mHelpRequestList.get(position).getmTrip().getmTimeTillExpiry()));

        holder.btn_item_view_help_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,HelpCardActivity.class);
                intent.putExtra(Messages.MESSAGE_TYPE_VALUE_HELP_CARD,mHelpRequestList.get(position));
                mContext.startActivity(intent);
            }
        });
        holder.btn_item_delete_help_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpRequestManager.getmInstance().removeHelpRequestReceived(mHelpRequestList.get(position).getmNotificationChannel());
                Server.leaveChatForThisRequest(mHelpRequestList.get(position).getmNotificationChannel());
                mHelpRequestList.remove(position);
                notifyDataSetChanged();
            }
        });
        return rowView;
    }

    /*
     * For Using View holder pattern
     */
    private class ViewHolder{
        TextView tv_item_sender_name;
        TextView tv_item_location;
        TextView tv_item_expiry_time;
        Button btn_item_view_help_request;
        Button btn_item_delete_help_request;


    }
}
