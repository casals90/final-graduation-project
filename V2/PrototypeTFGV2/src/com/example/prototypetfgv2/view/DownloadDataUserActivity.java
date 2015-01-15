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

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
/**
 * class 
 * @author jordi
 *
 */
public class DownloadDataUserActivity extends Activity {

	private static final String MyPREFERENCES = "PhotoCloudData";
	private SharedPreferences sharedPreferences;
	private Controller controller;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download_data_user);
		
		controller = (Controller) getApplication();
		sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		
		new DownloadUserDataTask().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.download_data_user, menu);
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
	
	public void saveDataForAutomaticLogin() {
		Log.v("prototypev1","eXECUTE save for facebook twiter users");
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("username",controller.getCurrentUser().getUsername());
		if(controller.isLinkedWithFacebook())
			editor.putBoolean("facebook",true);
		else if(controller.isLinkedWithTwitter())
			editor.putBoolean("twitter",true);
		editor.commit();
	}
	
	public void goToMainActivity() {
		Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
	}
	
	//class to do log in in Parse in background
	public class DownloadUserDataTask extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog progressDialog;
		
		@Override
	    protected void onPreExecute() {
			
	    }
		
		@Override
		protected Boolean doInBackground(Void... params) {
			//download data
			controller.downloadCurrentUser();
			controller.getAllLikes();
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if(success) {
				saveDataForAutomaticLogin();
				goToMainActivity();
			} 
			else {
				Log.v("prototypev1","download cancelat");
			}
		}

		@Override
		protected void onCancelled() {
			progressDialog.dismiss();
			Log.v("prototypev1","download cancelat");
		}
	}
}
