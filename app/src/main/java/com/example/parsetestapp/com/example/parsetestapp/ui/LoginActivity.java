package com.example.parsetestapp.com.example.parsetestapp.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parsetestapp.R;
import com.example.parsetestapp.com.example.parsetestapp.schema.Server;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends Activity {

	
	private EditText mEtUserName;
	private EditText mEtPassword;
	private Button mBtnLogin;
	private Button mBtnSignup;
	private ProgressDialog mProgressDialog;
	
	private final String MSG_LOGGING_IN = "Logging in , please wait..";
	private final String MSG_LOGGING_IN_TITLE = "Log In";
	private final String MSG_SIGN_UP = "Signing up , please wait..";
	private final String MSG_SIGN_UP_TITLE = "Sign Up";
	
	private static final String TAG = LoginActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_login);
		mEtUserName = (EditText)findViewById(R.id.et_username);
		mEtPassword = (EditText)findViewById(R.id.et_password);
		mBtnLogin = (Button)findViewById(R.id.btn_login);
		mBtnSignup = (Button)findViewById(R.id.btn_signup);

	}
	
	private void showToast(String msg){
		Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
	}
	
	private void startMainActivity(){
		Intent intent = new Intent(LoginActivity.this, MainActivity.class);
		startActivity(intent);
        finish();
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		mBtnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
	            Server.login(mEtUserName.getText().toString().trim(),mEtPassword.getText().toString().trim(), new LogInCallback() {
                ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, MSG_LOGGING_IN_TITLE, MSG_LOGGING_IN);
					
					@Override
					public void done(ParseUser user, ParseException e) {
						progressDialog.dismiss();
						if(user == null){
							showToast("New user, sign up required!");
							//updateScreen(ViewOptions.SHOW_SIGNUP);
						}else{
							showToast("Login successful!");
							Log.d(TAG,"user = "+ParseUser.getCurrentUser());
							startMainActivity();
						}
					}
				});
			}
		});
		
		mBtnSignup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, MSG_SIGN_UP_TITLE, MSG_SIGN_UP);
					Server.signup(mEtUserName.getText().toString().trim(),mEtPassword.getText().toString(),new SignUpCallback(){;
					@Override
					public void done(ParseException e) {
						progressDialog.dismiss();
						if(e == null){
							showToast("Sign up successful");
						}else{
                            showToast("Error:"+e.getMessage());
                        }
					}
				});
			}
		});
        if(Server.getCurrentUser() != null){
            startMainActivity();
        }
//        else {
//            updateScreen(ViewOptions.SHOW_LOGIN);
//        }

    }

}
