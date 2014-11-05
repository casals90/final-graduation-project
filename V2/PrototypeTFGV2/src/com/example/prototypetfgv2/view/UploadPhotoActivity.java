package com.example.prototypetfgv2.view;

import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;

import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Album;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UploadPhotoActivity extends Activity {

	private ImageView mImageView;
	private ImageButton send;
	private EditText mEditText;
	
	private String mCurrentPhotoPath;
	
	private Bitmap photo;
	
	//private ImageLoader imageLoader;
	private Controller controller;
	private Activity activity;
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_photo);
		
		//imageLoader = ImageLoader.getInstance();
		controller = (Controller) getApplicationContext();
		
		activity = this;
		
		mEditText = (EditText) findViewById(R.id.photo_title);
		mImageView = (ImageView) findViewById(R.id.img);
		send = (ImageButton) findViewById(R.id.send_title);
		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String title = mEditText.getText().toString();
				if(title.length() > 0) {
					mEditText.setText("");
					controller.updatePhoto(photo, title,activity);
				}
				else
					Toast.makeText(getApplicationContext(), "Input comment",Toast.LENGTH_LONG).show();
				finish();
			}
		});
		Bundle data = getIntent().getExtras();
		if(data != null)
			mCurrentPhotoPath = data.getString("pathNewPhoto");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.upload_photo, menu);
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
	
	
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		//setPic();
		//imageLoader.displayImage("file:///mnt"+mCurrentPhotoPath,mImageView);
		//Bitmap bmp = imageLoader.loadImageSync("file://"+mCurrentPhotoPath);
		//imageLoader.displayImage("file://"+mCurrentPhotoPath, mImageView);
		photo = showImage(mCurrentPhotoPath, mImageView);
		mImageView.setImageBitmap(photo);
	}

	private Bitmap showImage(String mCurrentPhotoPath,ImageView mImageView) {
	    // Get the dimensions of the View
	    int targetW = mImageView.getWidth();
	    int targetH = mImageView.getHeight();
	    
	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(mCurrentPhotoPath,bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;
	    
	    // Determine how much to scale down the image
	    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;

	    Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
	    
	    //fet per mi
	    ExifInterface ei;
		try {
			ei = new ExifInterface(mCurrentPhotoPath);
			int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			
			switch(orientation) {
			    case ExifInterface.ORIENTATION_ROTATE_90:
			    	Log.d("test", "rotate 90 ");
			    	bitmap = rotateBitmap(bitmap);
			        break;
			    case ExifInterface.ORIENTATION_ROTATE_180:
			    	Log.d("test", "rotate 180 ");
			        break;
		}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return bitmap;
	    
	    //-------------
		
	    //imageLoader.DisplayImage(mCurrentPhotoPath,mImageView);
		//mImageView.setImageBitmap(bitmap);
	}
	
	public Bitmap rotateBitmap(Bitmap mBitmap) {
		Matrix matrix = new Matrix();
		matrix.postRotate(90);
		Bitmap rotatedBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
		return rotatedBitmap;
	}
	
	/*public void goToAlbums() {	
		FragmentAlbums albums = new FragmentAlbums();
		
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		
		transaction.replace(R.id.container_fragment_main,albums);
		transaction.addToBackStack(null);
		transaction.commit();	
		
	}*/
}
