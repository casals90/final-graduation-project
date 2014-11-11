package com.example.prototypetfgv2.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.R.id;
import com.example.prototypetfgv2.R.layout;
import com.example.prototypetfgv2.R.menu;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Photo;
import com.example.prototypetfgv2.model.User;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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
import android.widget.TextView;
import android.widget.Toast;

public class ShowPhotoActivity extends Activity {
	
	String DEBUG_TAG ="Touch test";
	private ArrayList<Photo> photos;
	private int currentPosition;
	private FullScreenImageAdapter fullScreenAdapter;
	
	private ViewPager mViewPager;
	
	private ViewHolderActionBar viewHolderActionBar;

	
	private Controller controller;
	
	private GestureDetectorCompat mDetector; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen_view);
		initActionBar();
		
		controller = (Controller) this.getApplicationContext();
		
		Intent data = getIntent();
		if(data != null) {
			photos = data.getParcelableArrayListExtra("photos");
			currentPosition = data.getIntExtra("currentPosition",0);
		}
		
		fullScreenAdapter = new FullScreenImageAdapter(this, photos,currentPosition);
		
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(fullScreenAdapter);
		mViewPager.setCurrentItem(currentPosition);
		mViewPager.setPageMargin(50);
		//mViewPager.setOffscreenPageLimit(4);
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				updateActionBar(position);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void updateActionBar(int position) {
		Photo photo = photos.get(position);
		ImageLoader.getInstance().displayImage(photo.getOwnerUser().getProfilePicture(),viewHolderActionBar.mImageView);
		viewHolderActionBar.mTextViewPhotoTitle.setText(photo.getTitle());
		viewHolderActionBar.mTextViewUsername.setText(photo.getOwnerUser().getUsername());
	}
	
	public void initActionBar() {
		ActionBar actionBar = getActionBar();
		
		actionBar.setCustomView(R.layout.custom_action_bar_full_screen);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
		
		viewHolderActionBar = new ViewHolderActionBar();
		viewHolderActionBar.mImageView = (ImageView) actionBar.getCustomView().findViewById(R.id.profile_picture);
		viewHolderActionBar.mTextViewPhotoTitle = (TextView) actionBar.getCustomView().findViewById(R.id.photo_title);
		viewHolderActionBar.mTextViewUsername = (TextView) actionBar.getCustomView().findViewById(R.id.username);
	}
	
	private class ViewHolderActionBar {
		ImageView mImageView;
		TextView mTextViewUsername,mTextViewPhotoTitle;
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
