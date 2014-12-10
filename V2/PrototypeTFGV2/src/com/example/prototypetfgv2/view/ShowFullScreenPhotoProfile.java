package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.model.Photo;

public class ShowFullScreenPhotoProfile extends Activity {

	private ArrayList<Photo> photos;
	private int currentPosition;
	private ViewPager mViewPager;
	private FullScreenPhotosProfileAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		setContentView(R.layout.activity_show_full_screen_photo_profile);
		
		Intent data = getIntent();
		if(data != null) {
			photos = data.getParcelableArrayListExtra("photos");
			currentPosition = data.getIntExtra("currentPosition",0);
			Log.v("prototypev1","photos size "+photos.size()+"currenmt "+currentPosition);
		}
		
		adapter = new FullScreenPhotosProfileAdapter(this,photos);
		
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(adapter);
		mViewPager.setCurrentItem(currentPosition);
		mViewPager.setPageMargin(50);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.v("prototypev1","OnResum shoFullProfile");
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(R.string.my_photos);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_full_screen_photo_profile, menu);
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
				Log.v("prototypev1","home button");
				finish();
		        break;
	
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
}
