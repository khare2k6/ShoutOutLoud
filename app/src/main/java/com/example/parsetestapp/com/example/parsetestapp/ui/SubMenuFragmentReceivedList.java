package com.example.parsetestapp.com.example.parsetestapp.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.parsetestapp.R;
import com.example.parsetestapp.com.example.parsetestapp.schema.HelpRequest;
import com.example.parsetestapp.com.example.parsetestapp.schema.HelpRequestManager;

/**
 * Created by AKhare on 28-Dec-14.
 */
public class SubMenuFragmentReceivedList extends Fragment {
    private static final String TAG = SubMenuFragmentReceivedList.class.getSimpleName();
    ListView mLvReceivedHelpRequestList;
    private ReceivedListCustomAdapter mAdapter;
    private LinearLayout mContainerLayout;
    private TextView mTvStatus;

    @Override
    public void onResume() {
        super.onResume();
        if(HelpRequestManager.getmInstance().getReceivedRequestsCount() > 0){
            mContainerLayout.setVisibility(View.VISIBLE);
            mTvStatus.setVisibility(View.GONE);
        }else{
            mContainerLayout.setVisibility(View.GONE);
            mTvStatus.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_fragment_received_list, null);
        mLvReceivedHelpRequestList = (ListView) v.findViewById(R.id.lv_received_request_list);
        mContainerLayout = (LinearLayout) v.findViewById(R.id.container_received_requests);
        mTvStatus = (TextView) v.findViewById(R.id.tv_received_request_list_status);
        mAdapter = new ReceivedListCustomAdapter(getActivity(), HelpRequestManager.getmInstance().getAllReceivedHelpRequests());
        mLvReceivedHelpRequestList.setAdapter(mAdapter);
        return v;
    }
}
