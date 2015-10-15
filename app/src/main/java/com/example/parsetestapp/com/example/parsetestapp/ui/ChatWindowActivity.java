package com.example.parsetestapp.com.example.parsetestapp.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.parsetestapp.R;
import com.example.parsetestapp.com.example.parsetestapp.schema.ChatMessage;
import com.example.parsetestapp.com.example.parsetestapp.schema.HelpRequest;
import com.example.parsetestapp.com.example.parsetestapp.schema.HelpRequestManager;
import com.example.parsetestapp.com.example.parsetestapp.schema.Messages;
import com.example.parsetestapp.com.example.parsetestapp.schema.Server;

import java.util.ArrayList;
import java.util.List;

public class ChatWindowActivity extends Activity {

    public static String NEW_CHAT_MSG = "new_chat_msg";
    public static final String ACTION_NEW_CHAT_MSG_RECEIVED = "com.parse.test.CHAT_MSG_RECEIVED";

    private ListView mLvChatMsgList;
    private Button mBtnSendChatMsg;
    private TextView mTvChatWindowStatus;
    private EditText mEtChatMsg;
    private ChatAdapter mAdapter;
    private List<ChatMessage> mMessageList;
    private ChatReceiver mChatReceiver;
    private LinearLayout mContainerChatLayout,mContainerEmptyView;
    private LinearLayout mChatRow;

    private String TAG = ChatWindowActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        mLvChatMsgList = (ListView) findViewById(R.id.lv_chat_msgs);
        mBtnSendChatMsg = (Button) findViewById(R.id.btn_send_chat);
        mEtChatMsg = (EditText) findViewById(R.id.et_chat_text);
        mTvChatWindowStatus = (TextView) findViewById(R.id.tv_status_chat_window);
        mChatReceiver = new ChatReceiver();
        mChatRow = (LinearLayout) findViewById(R.id.row_container_char_msg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mChatReceiver, new IntentFilter(ACTION_NEW_CHAT_MSG_RECEIVED));
        final Intent intent = getIntent();
        String channelName = intent.getStringExtra(Messages.CHANNEL_NAME);
        HelpRequest request = HelpRequestManager.getmInstance().getHelpRequestReceived(channelName);

        mTvChatWindowStatus.setText("Request From: "+request.getmTrip().getmTraveller().getmName());
        mMessageList =  request.getChatMessagesList();
        Log.d(TAG,"channelName:"+channelName);
        Log.d(TAG,"..msg coutn="+mMessageList.size());
        mAdapter = new ChatAdapter(this,mMessageList);
        mLvChatMsgList.setAdapter(mAdapter);
        mBtnSendChatMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send chat message
                Messages.sendChatMsg(mEtChatMsg.getText().toString(),intent.getStringExtra(Messages.CHANNEL_NAME));
                mEtChatMsg.setText("");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mChatReceiver);
    }

    private class ChatReceiver extends BroadcastReceiver{
        private final String TAG = ChatReceiver.class.getSimpleName();

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive");
            String channel = intent.getStringExtra(Messages.CHANNEL_NAME);
            mMessageList =  HelpRequestManager.getmInstance().getHelpRequestReceived(channel).getChatMessagesList();
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Custom adapter for chat window
     */
    private class ChatAdapter extends ArrayAdapter<ChatMessage>{

        private final Context mContext;
        private final LayoutInflater mInflater;
        private List<ChatMessage>mMessageList;

        public ChatAdapter(Context context,List<ChatMessage> objects) {
            super(context,android.R.layout.simple_list_item_1 , R.layout.layout_row_chat_msg,objects);
            mMessageList = objects;
            mContext = context;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Log.d(TAG, "getView called for position" + position);
            View rowView = convertView;
            if(rowView == null ){
                rowView = mInflater.inflate(R.layout.layout_row_chat_msg, parent, false);
                ViewHolder vholder = new ViewHolder();
                vholder.tv_user_name = (TextView)rowView.findViewById(R.id.tv_row_chat_user_name);
                vholder.tv_user_msg = (TextView)rowView.findViewById(R.id.tv_row_chat_msg);
                rowView.setTag(vholder);
            }
            ViewHolder holder = (ViewHolder) rowView.getTag();
            String userName = mMessageList.get(position).getmUserName();
            Log.d(TAG,"userName:"+userName+" and Server.getC:"+Server.getCurrentUser().getUsername()
            +" equal?:"+userName.equalsIgnoreCase(Server.getCurrentUser().getUsername()));
            if(userName.equalsIgnoreCase(Server.getCurrentUser().getUsername())){
                //holder.tv_user_name.setTextColor(Color.RED);
                rowView.setBackground(getResources().getDrawable(R.drawable.red_bubble_2));
            }else{
               // holder.tv_user_name.setTextColor(Color.BLUE);
                rowView.setBackground(getResources().getDrawable(R.drawable.yellow_bubble_2));
            }
            holder.tv_user_name.setText(mMessageList.get(position).getmUserName());
            holder.tv_user_msg.setText(mMessageList.get(position).getmMessage());

            return rowView;
        }

        /*
         * For Using View holder pattern
         */
        private class ViewHolder{

            TextView tv_user_name;
            TextView tv_user_msg;
        }
    }
}
