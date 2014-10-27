package com.example.prototypetfgv2.view;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.model.Photo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentShowPhoto extends Fragment {

	private ImageView photo;
	private Button like,unlike,comment;
	private TextView numberOfLikes,numberOfComments,username;
	
	
	public FragmentShowPhoto() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//getActivity().setTitle(R.string.news);
		Bundle data = this.getArguments();
		Photo p= data.getParcelable("Photo");
		Log.v("prototypev1", "show photo "+p.getId());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_show_photo,container,false);
		
		photo = (ImageView) view.findViewById(R.id.photo);
		username = (TextView) view.findViewById(R.id.username);
		like = (Button) view.findViewById(R.id.like);
		unlike = (Button) view.findViewById(R.id.unlike);
		comment = (Button) view.findViewById(R.id.comment);
		numberOfLikes = (TextView) view.findViewById(R.id.number_of_likes);
		numberOfComments = (TextView) view.findViewById(R.id.number_of_comments);
		
		return view;
	}

}
