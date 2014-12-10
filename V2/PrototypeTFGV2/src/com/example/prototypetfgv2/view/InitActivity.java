package com.example.prototypetfgv2.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;

public class InitActivity extends Activity {
	
	private static final String MyPREFERENCES = "PhotoCloudData";
	
	private Controller controller;
	
	private SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init);
		
		controller = (Controller) getApplication();
		
		sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		//Delete all
		//getApplicationContext().getSharedPreferences(MyPREFERENCES, 0).edit().clear().commit();
		
		LoggedUser user = initLoggedUserData();
		Log.v("prototypev1", "Logged user "+user.username+" pass "+user.password +" facebook "+user.facebook+" twitter "+user.twitter);
		login(user);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.init, menu);
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
	
	public class LoggedUser {
		String username,password;
		boolean facebook,twitter;
	}
	
	public LoggedUser initLoggedUserData() {
		LoggedUser user = new LoggedUser();
		user.username = sharedPreferences.getString("username",null);
		user.password = sharedPreferences.getString("pass",null);
		user.facebook = sharedPreferences.getBoolean("facebook",false);
		user.twitter = sharedPreferences.getBoolean("twitter",false);
		return user;
	}
	
	public void login(LoggedUser user) {
		if(user.username == null && user.password == null)
			goToLoginActivity();
		else if(user.username != null && user.password != null) 
			//Parse login and go to main
			logIn(user.username, user.password);
		else if(user.username != null && user.facebook == true) {
			//Facebook auto login
			controller.logInFacebook(this);
			goToDownloadUserData();
		}	
		else if(user.username != null && user.twitter == true) {
			controller.logInTwitter(this);
			goToDownloadUserData();
		}	
	}
	
	public void logIn(String username,String password) {
		if(controller.logIn(username,password))
			goToDownloadUserData();
	}
	
	public void goToLoginActivity() {
		Intent login = new Intent(this, LoginActivity.class);
		login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(login);
	}
	
	public void goToDownloadUserData() {
		Intent download = new Intent(this, DownloadDataUserActivity.class);
		download.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(download);
	}
}
