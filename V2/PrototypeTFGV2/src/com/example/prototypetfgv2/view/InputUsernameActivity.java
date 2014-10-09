package com.example.prototypetfgv2.view;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.parse.ParseUser;

public class InputUsernameActivity extends Activity {

	private Controller controller;
	
	private EditText username;
	private Button bAccept;
	private TextView incorrectUsername;
	
	private String input;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_username);
		
		controller = (Controller) getApplicationContext();
		
		username = (EditText) findViewById(R.id.username);
		incorrectUsername = (TextView) findViewById(R.id.incorrect_username);
		bAccept = (Button) findViewById(R.id.b_accept);
		bAccept.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				input = username.getText().toString();
				//Check username availability
				new UpdateUserTask().execute();
				//Check login with twitter or facebook and import profile picture
			}
		});
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
	
	
	public class UpdateUserTask extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog progressDialog;
		
		@Override
	    protected void onPreExecute()
	    {
	        progressDialog= ProgressDialog.show(InputUsernameActivity.this, "Log in","waiting", true);       
	    };
		
		@Override
		protected Boolean doInBackground(Void... params) {
			if(controller.setUsername(input)) {
				//import profile picture
				controller.setProfilePictureFromTwitter();			
				return true;
			}
			return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			progressDialog.dismiss();
			if (success) {
				Log.v("prototypev1","correcte onPostExecute update user gotomainac");
				incorrectUsername.setVisibility(View.INVISIBLE);
				goToMainActivity();
			} 
			else {
				Log.v("prototypev1","incorrecte onPostExecute update user gotomainac");
				incorrectUsername.setVisibility(View.VISIBLE);
			}
		}

		@Override
		protected void onCancelled() {
			progressDialog.dismiss();
			incorrectUsername.setVisibility(View.VISIBLE);
			Log.v("prototypev1","log in cancelat 2");
		}
	}
	
	public void goToMainActivity() {
		Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
	}
}
