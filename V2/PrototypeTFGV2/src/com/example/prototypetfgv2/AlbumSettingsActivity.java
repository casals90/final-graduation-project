package com.example.prototypetfgv2;

import com.example.prototypetfgv2.model.Album;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class AlbumSettingsActivity extends Activity {

	private Album album;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album_settings);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Bundle data = getIntent().getExtras();
		if(data != null) {
			album = data.getParcelable("Album");
			Log.v("prototypev1","album settings "+album.getAlbumTitle());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.album_settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
			case android.R.id.home:
				finish();
		        break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
}
