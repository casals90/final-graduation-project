package com.example.prototypetfgv2.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;

public class LoginActivity extends Activity implements OnClickListener {
	
	private static final String MyPREFERENCES = "PrototypeTFGV1";
	
	private Controller controller;
	
	private EditText mUsernameView,mPasswordView;
	private TextView mIncorrectLoginView;
	private Button mLogin,mSignup,mLoginTwitter,mLoginFacebook;
	
	private String username,password;
	
	private LogInTask mAuthTask;
	
	SharedPreferences sharedpreferences;
	Controller app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		app = (Controller) getApplicationContext();
		controller = (Controller) getApplicationContext();
		
		mUsernameView = (EditText)findViewById(R.id.username);
		mPasswordView = (EditText)findViewById(R.id.password);
		mIncorrectLoginView = (TextView)findViewById(R.id.incorrect_login);
		mLogin = (Button)findViewById(R.id.log_in);
		mLogin.setOnClickListener(this);
		mSignup=(Button)findViewById(R.id.sign_up);
		mSignup.setOnClickListener(this);
		mLoginTwitter = (Button)findViewById(R.id.log_in_twitter);
		mLoginTwitter.setOnClickListener(this);
		mLoginFacebook = (Button)findViewById(R.id.log_in_facebook);
		mLoginFacebook.setOnClickListener(this);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
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
	// Listener of buttons
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.log_in:
			showIncorrectLoginMessage(false);			
			logIn(); 
			
			break;
			
		case R.id.sign_up:
			goToSignUp();
			break;
			
		case R.id.log_in_twitter:
			controller.logInTwitter(this);
			//go to new activity
			break;
		
		case R.id.log_in_facebook:
			
			break;
		
		default:
			break;
		}
	}
	
	public void fetchInputData() {
		username = mUsernameView.getText().toString();
		password = mPasswordView.getText().toString();
	}
	
	public void showIncorrectLoginMessage(boolean show) {
		if(show)
			mIncorrectLoginView.setVisibility(View.VISIBLE);
		else
			mIncorrectLoginView.setVisibility(View.INVISIBLE);
	}
	
	public void changeErrorMessage(String error) {
		Log.v("prototypev1","error signup "+error);
		mIncorrectLoginView.setText(error);
	}
	
	public void logIn() {
		fetchInputData();
		mAuthTask = new LogInTask();
		mAuthTask.execute((Void) null);		
	}
	
	public void goToMainActivity() {
		Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
	}
	
	public void goToSignUp() {
		Intent signUp = new Intent(this,SignUpActivity.class);
		startActivity(signUp);
	}
	
	public void goToInputUsername() {
		Intent inputUsername = new Intent(this,InputUsernameActivity.class);
		startActivity(inputUsername);
	}
	
	public void rememberLogin() {
		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedpreferences.edit();
		
		editor.putBoolean("rememberLogin",true);
		editor.putString("username",username);
		editor.putString("password",password);
		editor.commit();
	}
	
	public void deleteRememberLogin() {
		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedpreferences.edit();
		
		editor.putBoolean("rememberLogin",false);
		editor.remove("username");
		editor.remove("password");
		editor.commit();
	}
	
	//class to do log in in Parse in background
	public class LogInTask extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog progressDialog;
		
		@Override
	    protected void onPreExecute()
	    {
	        progressDialog= ProgressDialog.show(LoginActivity.this, "Log in","waiting", true);       
	    };
		
		@Override
		protected Boolean doInBackground(Void... params) {
			return controller.logIn(username, password);
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			progressDialog.dismiss();
			if (success) {
				Log.v("prototypev1","correcte onPostExecute");
				rememberLogin();
				goToMainActivity();
			} 
			else {
				Log.v("prototypev1","log in cancelat");
				showIncorrectLoginMessage(true);
			}
		}

		@Override
		protected void onCancelled() {
			progressDialog.dismiss();
			mAuthTask = null;
			Log.v("prototypev1","log in cancelat 2");
		}
	}
}
