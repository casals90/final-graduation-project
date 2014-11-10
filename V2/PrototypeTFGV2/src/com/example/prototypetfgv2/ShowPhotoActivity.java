package com.example.prototypetfgv2;

import java.util.ArrayList;

import com.example.prototypetfgv2.model.Photo;
import com.example.prototypetfgv2.model.User;
import com.example.prototypetfgv2.view.FullScreenImageAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class ShowPhotoActivity extends Activity {
	private GestureDetectorCompat mDetector; 
	
	String DEBUG_TAG ="Touch test";
	private ArrayList<Photo> photos;
	private int currentPosition;
	private FullScreenImageAdapter fullScreenAdapter;
	
	private ViewPager mViewPager;
	
	ImageView mImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen_view);
		initActionBar();
		
		Intent data = getIntent();
		if(data != null) {
			photos = data.getParcelableArrayListExtra("photos");
			currentPosition = data.getIntExtra("currentPosition",0);
		}
		
		fullScreenAdapter = new FullScreenImageAdapter(this, photos);
		
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(fullScreenAdapter);
		mViewPager.setCurrentItem(currentPosition);
		mViewPager.setPageMargin(50);
		
	}
	
	public void initActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setCustomView(R.layout.custom_action_bar_full_screen);
		
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_photo, menu);
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
}
