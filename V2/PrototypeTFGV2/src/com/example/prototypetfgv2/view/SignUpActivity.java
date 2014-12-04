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

public class SignUpActivity extends Activity {
	
	private Controller controller;

	private ImageButton mImageButtonRemove,mImageButtonAccept;
	private EditText mEditTextUsername,mEditTextPassword,mEditTextRepeatPassword;
	private TextView mTextViewTitleActionBar,mIncorrectSignUp;;
	
	private String username,password,repeatPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		
		controller = (Controller) getApplicationContext();
		
		initActionBar();
		
		mEditTextUsername = (EditText) findViewById(R.id.username);
		mEditTextPassword = (EditText) findViewById(R.id.password);
		mEditTextRepeatPassword = (EditText) findViewById(R.id.repeat_password);
		mIncorrectSignUp = (TextView) findViewById(R.id.incorrect_sign_up);
		mEditTextRepeatPassword.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,KeyEvent event) {
				int result = actionId & EditorInfo.IME_MASK_ACTION;
		        switch(result) {
			        case EditorInfo.IME_ACTION_DONE:
			            // done stuff
			        	Log.v("prototypev1","done");
			        	signUp();
			            break;
			    }
		        return false;
			}
		});
		
		// Request focus and show soft keyboard automatically
        mEditTextUsername.requestFocus();
        getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
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
		mTextViewTitleActionBar.setText(getString(R.string.upper_sign_up));
		
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
				signUp();
			}
		});
	}
	
	
	public boolean areTwoPasswordsEquals() {
		if(password.equals(repeatPassword))
			return true;
		return false;
	}
	
	public void signUp() {
		
		username = mEditTextUsername.getText().toString();
    	password = mEditTextPassword.getText().toString();
    	repeatPassword = mEditTextRepeatPassword.getText().toString();
		
		if(areTwoPasswordsEquals()) {
			Log.v("prototypev1","The two passwords are equals");
			new SignUpTask().execute();	
		}	
		else {
			Log.v("prototypev1","The two passwords are not equals");
			showSignUpErrorMessage(true);
		}		
	}
	
	public void showSignUpErrorMessage(boolean show) {
		if(show)
			mIncorrectSignUp.setVisibility(View.VISIBLE);
		else
			mIncorrectSignUp.setVisibility(View.INVISIBLE);
	}
	
	public void goToMainActivity() {
		Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
	}
	
	// class to do sign up in Parse in background
	public class SignUpTask extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog progressDialog;
		
		@Override
	    protected void onPreExecute() {
	        progressDialog= ProgressDialog.show(SignUpActivity.this, "Sign up","waiting", true);       
	    };
		
		@Override
		protected Boolean doInBackground(Void... params) {
			return controller.signUp(username, password);
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			progressDialog.dismiss();
			if (success) {
				controller.downloadCurrentUser();
				goToMainActivity();
			} else {
				Log.v("prototypev1","sign up cancelat ");
				showSignUpErrorMessage(true);
			}
		}

		@Override
		protected void onCancelled() {
			progressDialog.dismiss();
			Log.v("prototypev1","sign up cancelat ");
			showSignUpErrorMessage(true);
		}
	}
	
}
