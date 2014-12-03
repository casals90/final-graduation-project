package com.example.prototypetfgv2.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.parse.ParseFacebookUtils;

public class LoginActivity extends Activity implements OnClickListener {
	
	private static final String MyPREFERENCES = "PrototypeTFGV1";
	
	private Controller controller;
	
	//private EditText mUsernameView,mPasswordView;
	//private TextView mIncorrectLoginView;
	private Button mLogin,mSignup,mLoginTwitter,mLoginFacebook;
	
	private String username,password;
	
	SharedPreferences sharedpreferences;
	Controller app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		app = (Controller) getApplicationContext();
		controller = (Controller) getApplication();
		
		/*mUsernameView = (EditText)findViewById(R.id.username);
		mPasswordView = (EditText)findViewById(R.id.password);
		mIncorrectLoginView = (TextView)findViewById(R.id.incorrect_login);*/
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
			logIn(); 
			Log.d("prototypev1", "login ");
			break;
			
		case R.id.sign_up:
			//goToSignUp();
			Log.d("prototypev1", "sign up");
			break;
			
		case R.id.log_in_twitter:
			controller.logInTwitter(this);
			//Log.d("prototypev1", "login twitter");
			break;
		
		case R.id.log_in_facebook:
			//controller.getParseFunctions().logInFacebook(this);
			//logInFacebook();
			Log.d("prototypev1", "login twitter");
			break;
		
		default:
			break;
		}
	}
	
	public void logInFacebook() {
		//LoginActivity.this.progressDialog = ProgressDialog.show(
	    // LoginActivity.this, "", "Logging in...", true);
		controller.getParseFunctions().logInFacebook(this);
	    
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  Log.v("prototypev1","onActivityResult facebook");
	  ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}
	
	/*public void fetchInputData() {
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
	}*/
	
	public void logIn() {
		Intent inputUsernameAndPassword = new Intent(this,InputUsernameAndPassword.class);
		startActivity(inputUsernameAndPassword);
	}
	
	public void goToSignUp() {
		Intent signUp = new Intent(this,SignUpActivity.class);
		startActivity(signUp);
	}
	
	public void goToInputUsername() {
		Intent inputUsername = new Intent(this,InputUsernameActivity.class);
		startActivity(inputUsername);
	}
	
	/*public void rememberLogin() {
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
	}*/
}
