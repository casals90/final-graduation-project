package com.example.prototypetfgv2.view;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Photo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ShowFullScreenPhotoOfNews extends Activity {

	private ImageView mImageViewPhoto;
	private ProgressBar mProgressBar;
	private Button mButtonLike, mButtonComments;

	private ActionBar actionBar;

	private Controller controller;
	
	private String idAlbum;
	private Photo photo;
	private DisplayImageOptions options;
	
	private boolean like;
	private boolean fullScreen;
	private Activity activiy;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		setContentView(R.layout.activity_show_full_screen_photo_of_news);
		activiy = this;
		fullScreen = false;
		
		controller = (Controller) getApplication();
		
		//Get photo to show
		Intent data = getIntent();
		photo = data.getParcelableExtra("photo");
		
		changeActionBar();
		
		mImageViewPhoto = (ImageView) findViewById(R.id.imgDisplay);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
		
		
		initDisplayOptions();
		ImageLoader.getInstance().displayImage(photo.getPhoto(),mImageViewPhoto,options,new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
            	mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            	mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            	mProgressBar.setVisibility(View.INVISIBLE);            	
            }
        });
		
		mButtonLike = (Button) findViewById(R.id.like);
		mButtonLike.setText(String.valueOf(photo.getLikesNumber()));
		mButtonComments = (Button) findViewById(R.id.comment);
		mButtonComments.setText(String.valueOf(photo.getCommentsNumber()));
		
		like = controller.currentUserLikedCurrentPhoto(photo.getId());
		if(!like) {
        	//Change color
        	changeShapeColorBlack(mButtonLike);
        	mButtonLike.setOnClickListener(new OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				idAlbum = photo.getOwnerAlbum();
    				new LikePhotoTask().execute(photo.getId());
    				photo.incrementNumberLikes();
    				incrementLikesNumberInButton(mButtonLike,photo.getId());
    				mButtonLike.setOnClickListener(null);
    			}
    		});
        }
        else 
        	changeShapeColorCyan(mButtonLike);
		
		changeShapeColorBlack(mButtonComments);
		mButtonComments.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goToCommentsActivity(photo);
			}
		});
		
		//Check if full screen or not
        showOrHiddenButtons(fullScreen, mButtonLike,mButtonComments);
        //Listener to detect clik
        this.findViewById(android.R.id.content).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.v("prototypev1","click view  ");
				fullScreen(mButtonLike,mButtonComments);
			}
		});
	}
		
	@Override
	protected void onResume() {
		super.onResume();
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	public void goToCommentsActivity(Photo currentPhoto) {
		Intent commentsActivity = new Intent(this,CommentsActivity.class);
		commentsActivity.putExtra("photo",currentPhoto);
		startActivity(commentsActivity);
	}
	
	public void changeShapeColorCyan(Button button) {
		GradientDrawable bgShape = (GradientDrawable)button.getBackground();
		//cyan
		bgShape.setColor(Color.rgb(51,181,229));
	}
	
	public void changeShapeColorBlack(Button button) {
		GradientDrawable bgShape = (GradientDrawable)button.getBackground();
		bgShape.setColor(Color.argb(204,44,44,44));
	}
	
	public void incrementLikesNumberInButton(Button button,String idPhoto) {
		int n = Integer.valueOf(button.getText().toString());
		n++;
		button.setText(String.valueOf(n));
		controller.addLikePhotoCurrentUser(idPhoto);
		changeShapeColorCyan(button);
	}
	
	private class ViewHolderActionBar {
		ImageView mImageView;
		TextView mTextViewUsername,mTextViewPhotoTitle;
	}
	
	public void fullScreen(Button like,Button comment) {
		fullScreen = !fullScreen;
		if(fullScreen) {
			actionBar.hide();
			showOrHiddenButtons(fullScreen, like, comment);
		} 
		else {
			actionBar.show();
			showOrHiddenButtons(fullScreen, like, comment);
		}
	}
	
	public void showOrHiddenButtons(boolean show,Button like,Button comment) {
		if(!show) {
			like.setVisibility(View.VISIBLE);
			comment.setVisibility(View.VISIBLE);
		}
		else {
			like.setVisibility(View.INVISIBLE);
			comment.setVisibility(View.INVISIBLE);
		}
	}
	
	public void changeActionBar() {
		actionBar = getActionBar();
		actionBar.setCustomView(R.layout.custom_action_bar_full_screen);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
		
		ViewHolderActionBar viewHolderActionBar = new ViewHolderActionBar();
		viewHolderActionBar.mImageView = (ImageView) actionBar.getCustomView().findViewById(R.id.profile_picture);
		viewHolderActionBar.mTextViewPhotoTitle = (TextView) actionBar.getCustomView().findViewById(R.id.photo_title);
		viewHolderActionBar.mTextViewUsername = (TextView) actionBar.getCustomView().findViewById(R.id.username);
		
		ImageLoader.getInstance().displayImage(photo.getOwnerUser().getProfilePicture(),viewHolderActionBar.mImageView);
		viewHolderActionBar.mTextViewPhotoTitle.setText(photo.getTitle());
		viewHolderActionBar.mTextViewUsername.setText(photo.getOwnerUser().getUsername());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_full_screen_photo_of_news, menu);
		return true;
	}
	
	public void initDisplayOptions() {
		options = new DisplayImageOptions.Builder()
        .showImageForEmptyUri(R.drawable.ic_launcher) // resource or drawable
        .showImageOnFail(R.drawable.ic_launcher) // resource or drawable
        .resetViewBeforeLoading(true) 
        .considerExifParams(true)
        .imageScaleType(ImageScaleType.EXACTLY)
        .bitmapConfig(Bitmap.Config.RGB_565)
        .build();
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
			case R.id.delete_photo:
				deletePhoto(photo);
				break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class LikePhotoTask extends AsyncTask<String, Void, Boolean> {
		
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        }
 
        @Override
        protected Boolean doInBackground(String... params) {
        	String idPhoto = params[0];
        	return controller.likePhoto(idPhoto,idAlbum);
        }

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result) {
				Toast.makeText(getApplication(),"Liked!",  Toast.LENGTH_SHORT).show();
				like = !like;
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			//Toast.makeText(getActivity(),"Error download photos",  Toast.LENGTH_LONG).show();
		}	
    }
	
	public void deletePhoto(Photo photo) {
		if(photo.getOwnerUser().getId().compareTo(controller.getCurrentUser().getId()) == 0) 
			new ConfirmationDeleteDialogFragment().show(getFragmentManager(),"confirm_delete");
		else 
			new InformationDialogFragment().show(getFragmentManager(),"no_delete");
	}
	
	public class InformationDialogFragment extends DialogFragment {
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setMessage(R.string.not_delete_photo)
	               .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // FIRE ZE MISSILES!
	                	   dismiss();
	                   }
	               });
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
	}
	
	public class ConfirmationDeleteDialogFragment extends DialogFragment {
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setMessage(R.string.confirm_delete_photo)
	               .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   dismiss();
	                	   new DeletePhotosTask().execute();
	                   }
	               })
	               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                	   dismiss();
                   }
               });
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
	}

	private class DeletePhotosTask extends AsyncTask<Void, Void, Boolean> {
		
		private ProgressDialog mProgressDialog;
		
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        	mProgressDialog = ProgressDialog.show(activiy,"Delete Photo","waiting", true);
        }
 
        @Override
        protected Boolean doInBackground(Void... params) {
        	return controller.deletePhoto(photo,idAlbum);
        }

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result) {
					mProgressDialog.dismiss();
					finish();
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			//Toast.makeText(getActivity(),"Error download photos",  Toast.LENGTH_LONG).show();
		}	
    }
}
