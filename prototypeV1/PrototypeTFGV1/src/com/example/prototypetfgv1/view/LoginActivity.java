package com.example.prototypetfgv1.view;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.prototypetfgv1.R;
import com.example.prototypetfgv1.controller.Controller;
import com.parse.LogInCallback;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class LoginActivity extends Activity implements OnClickListener {
	
	private static final String MyPREFERENCES = "PrototypeTFGV1";
	
	private Controller controller;
	
	private EditText mUsernameView,mPasswordView;
	private TextView mIncorrectLoginView;
	private Button mLogin,mSignup;
	private CheckBox mRememberLogin;
	
	private String username,password;
	private boolean rememberLogin;
	
	private LogInTask mAuthTask;
	
	SharedPreferences sharedpreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		controller = new Controller(getApplicationContext());
		
		mUsernameView = (EditText)findViewById(R.id.username);
		mPasswordView = (EditText)findViewById(R.id.password);
		mIncorrectLoginView = (TextView)findViewById(R.id.incorrect_login);
		mLogin = (Button)findViewById(R.id.log_in);
		mLogin.setOnClickListener(this);
		mSignup=(Button)findViewById(R.id.sign_up);
		mSignup.setOnClickListener(this);
		mRememberLogin = (CheckBox) findViewById(R.id.remember_login);
		
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
	
	/*@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  Log.v("prototypev1","onactivity result facebook ");
	  ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}*/
	
	// Listener of buttons
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.log_in:
			showIncorrectLoginMessage(false);			
			logIn();
			/*List<String> permissions = Arrays.asList("basic_info", "user_about_me",
                    "user_relationships", "user_birthday", "user_location","email");
			
			ParseFacebookUtils.logIn(permissions,this, new LogInCallback() {
				
				@Override
				public void done(ParseUser user, com.parse.ParseException e) {
					// TODO Auto-generated method stub
					Log.v("prototypev1","inside done facebook login ");
					 if (user == null) {
					      Log.d("prototypev1", "Uh oh. The user cancelled the Facebook login.");
					    } else if (user.isNew()) {
					      Log.d("prototypev1", "User signed up and logged in through Facebook!");
					    } else {
					      Log.d("prototypev1", "User logged in through Facebook!");
					    }
				}
			});*/
			
			break;
			
		case R.id.sign_up:
			Intent signUp = new Intent(this,SignUpActivity.class);
			startActivity(signUp);
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
				
				rememberLogin = mRememberLogin.isChecked();
				Log.v("prototypev1","remember login "+rememberLogin);
				if(rememberLogin)
					rememberLogin();
				else 
					deleteRememberLogin();
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
