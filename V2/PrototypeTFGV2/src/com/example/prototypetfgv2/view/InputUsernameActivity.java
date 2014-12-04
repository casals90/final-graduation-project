package com.example.prototypetfgv2.view;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;

public class InputUsernameActivity extends Activity {

	private Controller controller;
	
	private EditText mEditTextUsername;
	private ImageButton mImageButtonAccept, mImageButtonRemove;
	private TextView mTextViewTitleActionBar,mTextViewInocrrectUsername;
	private String username;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_username);
		
		initActionBar();
		
		controller = (Controller) getApplicationContext();
		
		mEditTextUsername = (EditText) findViewById(R.id.username);
		mTextViewInocrrectUsername = (TextView) findViewById(R.id.incorrect_username);
		
		mEditTextUsername.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,KeyEvent event) {
				int result = actionId & EditorInfo.IME_MASK_ACTION;
		        switch(result) {
			        case EditorInfo.IME_ACTION_DONE:
			            // done stuff
			        	new UpdateUseNameTask().execute();
			            break;
			    }
		        return false;
			}
		});
		
		//Request focus and show soft keyboard automatically
        mEditTextUsername.requestFocus();
        getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.input_username, menu);
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
	
	public void initActionBar() {
		ActionBar actionBar = getActionBar();

		actionBar.setCustomView(R.layout.custom_action_bar_log_in);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		
		mTextViewTitleActionBar = (TextView) getActionBar().getCustomView().findViewById(R.id.label);
		mTextViewTitleActionBar.setText(getString(R.string.upper_input_username));
		
		mImageButtonRemove = (ImageButton) getActionBar().getCustomView().findViewById(R.id.button_back);
		mImageButtonRemove.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Go to login activity
				controller.logout();
				finish();
			}
		});
		mImageButtonAccept = (ImageButton) getActionBar().getCustomView().findViewById(R.id.button_accept);
		mImageButtonAccept.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new UpdateUseNameTask().execute();
			}
		});
	}
	
	public class UpdateUseNameTask extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog progressDialog;
		
		@Override
	    protected void onPreExecute() {
			username = mEditTextUsername.getText().toString();
			mTextViewInocrrectUsername.setVisibility(View.INVISIBLE);
	        progressDialog= ProgressDialog.show(InputUsernameActivity.this, "Set username","waiting", true);       
	    };
		
		@Override
		protected Boolean doInBackground(Void... params) {
			if(controller.setUsername(username)) {
				controller.downloadCurrentUser();
				controller.getAllLikes();
				//import profile picture from social network
				//check if user login with Twitter or facebook
				if(controller.isLinkedWithTwitter())
					controller.setProfilePictureFromTwitter();
				//else
					//facebook
				return true;
			}
			return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			progressDialog.dismiss();
			Log.v("prototypev1","onPostExecute "+success);
			if (success) {
				Log.v("prototypev1","correcte onPostExecute update user name");
				goToMainActivity();
			} 
			else {
				Log.v("prototypev1","incorrecte onPostExecute update username");
				mTextViewInocrrectUsername.setVisibility(View.VISIBLE);
			}
		}

		@Override
		protected void onCancelled() {
			progressDialog.dismiss();
			mTextViewInocrrectUsername.setVisibility(View.VISIBLE);
			Log.v("prototypev1","log in cancelat 2");
		}
	}
	
	public void goToMainActivity() {
		Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
	}
}
