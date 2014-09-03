package com.example.prototypetfgv1.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.prototypetfgv1.R;
import com.parse.ParseException;
import com.parse.ParseUser;

public class SignUpActivity extends Activity {
	
	private EditText mUsernameView, mPasswordView, mRepeatPasswordView;
	private TextView mIncorrectSignUpView;
	private Button mSignup;
	private String mUsername,mPassword,mRepeatPassword;
	
	private SignUpTask mAuthTask = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		
		mUsernameView = (EditText)findViewById(R.id.username);
		mPasswordView = (EditText)findViewById(R.id.password);
		mRepeatPasswordView = (EditText)findViewById(R.id.repeat_password);
		mIncorrectSignUpView = (TextView)findViewById(R.id.incorrect_sign_up);
		mSignup = (Button)findViewById(R.id.sign_up);
		mSignup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//restart error message
				showSignUpErrorMessage(false);
				signUp();
			}
		});
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
	
	public void fetchInputData() {
		mUsername = mUsernameView.getText().toString();
		mPassword = mPasswordView.getText().toString();
		mRepeatPassword = mRepeatPasswordView.getText().toString();
	}
	
	public boolean areTwoPasswordsEquals() {
		if(mPassword.equals(mRepeatPassword))
			return true;
		return false;
	}
	
	public void signUp() {
		
		if (mAuthTask != null) {
			return;
		}
		
		fetchInputData();
		
		if(areTwoPasswordsEquals()) {
			Log.v("prototypev1","The two passwords are equals");
			mAuthTask = new SignUpTask();
			mAuthTask.execute((Void) null);	
		}	
		else {
			Log.v("prototypev1","The two passwords are not equals");
		}		
	}
	
	public boolean signUpInParse() {
		boolean correct = true;
		ParseUser user = new ParseUser();
		user.setUsername(mUsername);
		user.setPassword(mPassword);
		
		try {
			user.signUp();
		} catch (ParseException e) {
			e.printStackTrace();
			Log.v("prototypev1","error signup "+e);
			correct = false;
		}
		return correct;
	}
	
	public void showSignUpErrorMessage(boolean show) {
		if(show)
			mIncorrectSignUpView.setVisibility(View.VISIBLE);
		else
			mIncorrectSignUpView.setVisibility(View.INVISIBLE);
	}
	
	public void changeErrorMessage(String error) {
		Log.v("prototypev1","error signup "+error);
		mIncorrectSignUpView.setText(error);
	}
	
	/* class to do sign up in Parse in background*/
	public class SignUpTask extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog progressDialog;
		
		@Override
	    protected void onPreExecute()
	    {
	        progressDialog= ProgressDialog.show(SignUpActivity.this, "Sign up","waiting", true);       
	    };
		
		@Override
		protected Boolean doInBackground(Void... params) {
			return signUpInParse();	
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			progressDialog.dismiss();
			if (success) {
				//finish();
				//Go to main
				Log.v("prototypev1","correcte onPostExecute");
				
			} else {
				Log.v("prototypev1","sign up cancelat ");
				showSignUpErrorMessage(true);
			}
		}

		@Override
		protected void onCancelled() {
			progressDialog.dismiss();
			mAuthTask = null;
			Log.v("prototypev1","sign up cancelat ");
		}
	}	
	
}
