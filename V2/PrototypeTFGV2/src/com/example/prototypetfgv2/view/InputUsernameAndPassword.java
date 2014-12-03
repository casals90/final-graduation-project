package com.example.prototypetfgv2.view;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;

public class InputUsernameAndPassword extends Activity {

	private EditText mEditTextUsername,mEditTextPassword;
	private ImageButton mImageButtonAccept, mImageButtonRemove;
	
	private String username,password;
	
	private Controller controller;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_username_and_password);
		
		initActionBar();
		
		mEditTextUsername = (EditText) findViewById(R.id.username);
		mEditTextPassword = (EditText) findViewById(R.id.password);
		
		 // Request focus and show soft keyboard automatically
        mEditTextUsername.requestFocus();
        getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        
        controller = (Controller) getApplication();
	}
	
	public void initActionBar() {
		ActionBar actionBar = getActionBar();
		
		actionBar.setCustomView(R.layout.custom_action_bar_log_in);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		
		mImageButtonRemove = (ImageButton) getActionBar().getCustomView().findViewById(R.id.button_back);
		mImageButtonRemove.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mImageButtonAccept = (ImageButton) getActionBar().getCustomView().findViewById(R.id.button_accept);
		mImageButtonAccept.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				username = mEditTextUsername.getText().toString();
				password = mEditTextPassword.getText().toString();
				// TODO parse login
				new LogInTask().execute();
			}
		});
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.input_username_and_password, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void goToMainActivity() {
		Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
	}
	
	//class to do log in in Parse in background
	public class LogInTask extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog progressDialog;
		
		@Override
	    protected void onPreExecute() {
	        progressDialog= ProgressDialog.show(InputUsernameAndPassword.this, "Log in","waiting", true);       
	    };
		
		@Override
		protected Boolean doInBackground(Void... params) {
			return controller.logIn(username, password);
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			progressDialog.dismiss();
			if (success) {
				controller.downloadCurrentUser();
				controller.getAllLikes();
				goToMainActivity();
			} 
			else {
				Log.v("prototypev1","log in cancelat");
				//showIncorrectLoginMessage(true);
			}
		}

		@Override
		protected void onCancelled() {
			progressDialog.dismiss();
			Log.v("prototypev1","log in cancelat 2");
		}
	}
}
