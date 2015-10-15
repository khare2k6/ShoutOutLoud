package com.example.parsetestapp;

import android.content.Context;
import android.content.Intent;

import com.example.parsetestapp.com.example.parsetestapp.schema.HelpRequest;
import com.example.parsetestapp.com.example.parsetestapp.schema.Person;
import com.example.parsetestapp.com.example.parsetestapp.ui.HelpCardActivity;

/**
 * Created by akhare on 22-Dec-14.
 */
public class Controller {
    private Person mMyInfo;
    private static Controller mInstance = new Controller();
    private Controller(){}
    public static Controller getInstance(){
        return mInstance;
    }

    public void onNewHelpCard(Context context,HelpRequest card){
        Intent intent = new Intent(context, HelpCardActivity.class);
        intent.putExtra(HelpRequest.class.getSimpleName(),card);
        context.startActivity(intent);
    }

    public interface INotificationListener{
        public void onHelpCardReceived(HelpRequest card);
        public void onChatMsgReceived();
    }
}
