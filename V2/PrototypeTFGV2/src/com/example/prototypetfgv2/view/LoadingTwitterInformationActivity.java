package com.example.prototypetfgv2.view;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.prototypetfgv2.R;

public class LoadingTwitterInformationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading_twitter_information);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.loading_twitter_information, menu);
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
	
public class ImportProfilePictureFromTwitterTask extends AsyncTask<Void, Void, Boolean> {
		
		@Override
	    protected void onPreExecute() {
			
	    };
		
		@Override
		protected Boolean doInBackground(Void... params) {
			//todo
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			
			if (success) {
				Log.v("prototypev1","correcte set profile picture twitter");
				
			} else {
				Toast.makeText(getApplicationContext(),"Error set profile picture twitter",  Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onCancelled() {
			//mProgressBar.setVisibility(View.VISIBLE);
			//profilePicture.setVisibility(View.INVISIBLE);
			Toast.makeText(getApplicationContext(),"Error download twitter data",  Toast.LENGTH_LONG).show();
		}
	}
}
