package com.example.prototypetfgv2.view;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.model.Photo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentShowPhoto extends Fragment {

	private ImageView photo;
	private Button like,unlike,comment;
	private TextView numberOfLikes,numberOfComments,username;
	
	private ImageLoader imageLoader;
	private Photo p;
	//Error likes number
	public FragmentShowPhoto() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//getActivity().setTitle(R.string.news);
		Bundle data = this.getArguments();
		p = data.getParcelable("Photo");
		Log.v("prototypev1", "show photo "+p.getId()+" l "+p.getLikesNumber());
		imageLoader = new ImageLoader(getActivity().getApplicationContext());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_show_photo,container,false);
		
		photo = (ImageView) view.findViewById(R.id.photo);
		imageLoader.DisplayImage(p.getPhoto(),photo);
		username = (TextView) view.findViewById(R.id.username);
		like = (Button) view.findViewById(R.id.like);
		like.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		unlike = (Button) view.findViewById(R.id.unlike);
		unlike.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		comment = (Button) view.findViewById(R.id.comment);
		comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//go to activity comments
			}
		});
		numberOfLikes = (TextView) view.findViewById(R.id.number_of_likes);
		//numberOfLikes.setText(p.getLikesNumber());
		numberOfComments = (TextView) view.findViewById(R.id.number_of_comments);
		//numberOfComments.setText(p.getCommentsNumber());
		
		return view;
	}
	
	public void goToCommentsActivity() {
		Intent commentsActivity = new Intent(getActivity(), CommentsActivity.class);
		Bundle bundle = new Bundle();
		commentsActivity.putExtra("photo",p);
		startActivity(commentsActivity);
	}

}
