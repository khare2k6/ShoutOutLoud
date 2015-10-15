package com.example.parsetestapp;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ParseApplication extends Application{
	
	public void onCreate() {
		  Parse.initialize(this, "wB9QZ9bhPC4j46sYrUw5St3SKPQRjqiaWfWVWWT9", "d8aGH8bVQe68xOGeRAdMyw17sdEpSwW7xEWdPn2m");
		  ParseObject testObject = new ParseObject("LocationObject");
//		  testObject.put("Latitude", "12.992214");
//		  testObject.put("Longitude", "77.715900");
//		  testObject.put("Area", "");
//		  testObject.saveInBackground();
		  
		  ParseInstallation installation = ParseInstallation.getCurrentInstallation();
		  installation.saveInBackground();
		  
		  ParsePush.subscribeInBackground("", new SaveCallback() {
			  @Override
			  public void done(ParseException e) {
			    if (e == null) {
			      Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
			    } else {
			      Log.e("com.parse.push", "failed to subscribe for push", e);
			    }
			  }
			});
		}
}
