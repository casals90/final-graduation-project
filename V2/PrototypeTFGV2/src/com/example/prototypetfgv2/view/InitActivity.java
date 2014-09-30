package com.example.prototypetfgv2.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;

public class InitActivity extends Activity {
	
	private static final String MyPREFERENCES = "PrototypeTFGV1";
	
	private Controller controller;
	
	private SharedPreferences sharedPreferences;
	
	private boolean rememberLogin;
	private String username, password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init);
		
		controller = new Controller(getApplicationContext());
		
		sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		
		if(sharedPreferences.contains("rememberLogin"))
			rememberLogin = sharedPreferences.getBoolean("rememberLogin",false);
		
		if(rememberLogin) {
			username = sharedPreferences.getString("username","");
			password = sharedPreferences.getString("password","");
			
			if(controller.logIn(username, password)) 
				goToMainActivity();
			else
				goToLoginActivity();
		}
		else
			goToLoginActivity();
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
	
	public void goToLoginActivity() {
		Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
	}
	
	public void goToMainActivity() {
		Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
	}
}
