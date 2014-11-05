package com.example.prototypetfgv2.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Album;
import com.example.prototypetfgv2.model.Photo;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FragmentShowPhoto extends Fragment {

	private ImageView photo;
	private Button like,unlike,comment;
	private TextView numberOfLikes,numberOfComments,username,date;
	
	private ImageLoader imageLoader;
	private Controller controller;
	
	private Photo currentPhoto;
	private int countLikes = 0;
	private int countComments = 0;
	private boolean currentUserLikesThisPhoto;
	
	public FragmentShowPhoto() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//getActivity().setTitle(R.string.news);
		Bundle data = this.getArguments();
		if(data != null)
			currentPhoto = data.getParcelable("Photo");
		
		imageLoader = ImageLoader.getInstance();
		controller = (Controller) getActivity().getApplicationContext();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_show_photo,container,false);
		
		photo = (ImageView) view.findViewById(R.id.photo);
		imageLoader.displayImage(currentPhoto.getPhoto(),photo);
		username = (TextView) view.findViewById(R.id.username);
		username.setText(currentPhoto.getOwnerUser().getUsername());
		like = (Button) view.findViewById(R.id.like);
		unlike = (Button) view.findViewById(R.id.unlike);
		like.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				controller.likePhoto(currentPhoto.getId());
				//hidden like button
				like.setVisibility(View.INVISIBLE);
				unlike.setVisibility(View.VISIBLE);
				//increment numbers of likes in list view
				countLikes++;
				numberOfLikes.setText(String.valueOf(countLikes));
			}
		});
		unlike.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				controller.unlikePhoto(currentPhoto.getId());
				//hidden unlike button
				like.setVisibility(View.VISIBLE);
				unlike.setVisibility(View.INVISIBLE);
				//decrement numbers of likes in list view
				countLikes--;
				numberOfLikes.setText(String.valueOf(countLikes));
			}
		});
		//Check if currentUser likes photo
		Log.v("prototypev1", "Users likes this photo "+controller.currentUserLikesCurrentPhoto(currentPhoto.getId()));
		if(controller.currentUserLikesCurrentPhoto(currentPhoto.getId())) {
			like.setVisibility(View.INVISIBLE);
			unlike.setVisibility(View.VISIBLE);
		}
		else {
			like.setVisibility(View.VISIBLE);
			unlike.setVisibility(View.INVISIBLE);
		}
			
		comment = (Button) view.findViewById(R.id.comment);
		comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//go to activity comments
				goToCommentsActivity();
			}
		});
		numberOfLikes = (TextView) view.findViewById(R.id.number_of_likes);
		numberOfComments = (TextView) view.findViewById(R.id.number_of_comments);
		
		date = (TextView) view.findViewById(R.id.date);
		date.setText(currentPhoto.getCreatedAt());
		
		new DownloadNumberLikesAndCommentsTask().execute();
		
		return view;
	}
	
	public void goToCommentsActivity() {
		Intent commentsActivity = new Intent(getActivity(), CommentsActivity.class);
		commentsActivity.putExtra("photo",currentPhoto);
		startActivity(commentsActivity);
	}
	
	private class DownloadNumberLikesAndCommentsTask extends AsyncTask<Void, Void, Boolean> {
    	
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        	//mProgressBar.setVisibility(View.VISIBLE);
        }
 
        @Override
        protected Boolean doInBackground(Void... params) {
        	countLikes = controller.countPhotoLikes(currentPhoto.getId());
        	countComments = controller.countPhotoComments(currentPhoto.getId());
        	currentUserLikesThisPhoto = controller.getParseFunctions().currentUserLikesCurrentPhoto(currentPhoto.getId());
        	//Check correct values
        	if(countComments > -1 && countLikes > -1)
        		return true;
        	return false;		
        }
 
        @Override
        protected void onPostExecute(final Boolean success) {
        	if(success) {
        		numberOfLikes.setText(String.valueOf(countLikes));
        		numberOfComments.setText(String.valueOf(countComments));
        		if(currentUserLikesThisPhoto) {
        			like.setVisibility(View.INVISIBLE);
        			unlike.setVisibility(View.VISIBLE);
        		}
        		else {
        			like.setVisibility(View.VISIBLE);
        			unlike.setVisibility(View.INVISIBLE);
        		}
        	}
        	else {
        		
        	}	
        }

		@Override
		protected void onCancelled() {
			super.onCancelled();
			Toast.makeText(getActivity(),"Error download likes and comments",  Toast.LENGTH_LONG).show();
		}
    }
   
   public void goToShowAlbum(Album album) {
		Bundle data = new Bundle();
		data.putParcelable("Album",album);
		FragmentShowAlbum showAlbum = new FragmentShowAlbum();
		showAlbum.setArguments(data);
		
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.container_fragment_main,showAlbum);
		transaction.addToBackStack(null);
		transaction.commit();	
	}

}
