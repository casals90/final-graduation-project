package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.R.id;
import com.example.prototypetfgv2.R.layout;
import com.example.prototypetfgv2.R.menu;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Album;
import com.example.prototypetfgv2.model.Photo;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ListViewPhotosActivity extends Activity {

	private ListView mListViewPhotos;
	private AdapterListViewShowPhotos adapter;
	private Controller controller;
	private ArrayList<Photo> photos;
	private ProgressBar mProgressBar;
	//private Album album;
	private String id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_list_view_photos);
		
		controller = (Controller) getApplicationContext();
		
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
		mListViewPhotos = (ListView) findViewById(R.id.photos);
		
		Intent data = getIntent();
		id = data.getStringExtra("Album");
		new DownloadPhotosTask().execute(id);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_view_photos, menu);
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
	
	private class DownloadPhotosTask extends AsyncTask<String, Void, Boolean> {
		
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        	mListViewPhotos.setVisibility(View.INVISIBLE);
        	mProgressBar.setVisibility(View.VISIBLE);
        }
 
        @Override
        protected Boolean doInBackground(String... params) {
        	String id = params[0];
        	photos = controller.downloadPhotosFromAlbum(id);
        	if(photos != null)
        		return true;
        	return false;
        }

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result) {
				mListViewPhotos.setVisibility(View.VISIBLE);
	        	mProgressBar.setVisibility(View.INVISIBLE);
				adapter = new AdapterListViewShowPhotos(getApplicationContext(), photos);
				mListViewPhotos.setAdapter(adapter);
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			//Toast.makeText(getActivity(),"Error download photos",  Toast.LENGTH_LONG).show();
		}	
    }
}
