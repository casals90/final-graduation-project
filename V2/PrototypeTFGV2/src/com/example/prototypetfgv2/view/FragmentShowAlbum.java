package com.example.prototypetfgv2.view;

import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Album;
import com.example.prototypetfgv2.model.Photo;

public class FragmentShowAlbum extends Fragment {
	
	private GridView gridView;
	private ProgressBar mProgressBar;
	private TextView noPhotos;
	
	private Controller controller;
	private Album album;
	private List<Photo> photos;
	//Photo that user selected
	private Photo photo;
	
	private GridViewAdapterForShowPhotos adapter;
	
	public FragmentShowAlbum() {
		super();
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setTitle(R.string.news);
		controller = (Controller) this.getActivity().getApplicationContext();
		//Fetch data album
		Bundle data = this.getArguments();
		album = data.getParcelable("Album");
		
		//Put album title in action bar
		getActivity().setTitle((album.getAlbumTitle()));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_show_album,container,false);
		
		gridView = (GridView) view.findViewById(R.id.gridView_photos);
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar_download_albums);
		noPhotos = (TextView) view.findViewById(R.id.no_photos);
		//New task
		new DownloadPhotosTask().execute();
		return view;
	}
	
//Class to download photos
private class DownloadPhotosTask extends AsyncTask<Void, Void, Boolean> {
		
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
            gridView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        }
 
        @Override
        protected Boolean doInBackground(Void... params) {
        	//return controller.deleteFriend(user.getId());
        	photos = controller.downloadPhotosFromAlbum(album.getId());
        	if(photos != null && photos.size() > 0)
        		return true;
        	return false;
        }

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result) {
				noPhotos.setVisibility(View.INVISIBLE);
				mProgressBar.setVisibility(View.INVISIBLE);
				gridView.setVisibility(View.VISIBLE);
				//adapter
				// Pass the results into ListViewAdapter.java
	            adapter = new GridViewAdapterForShowPhotos(getActivity(),photos);
	            // Binds the Adapter to the ListView
	            gridView.setAdapter(adapter);
	            gridView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
						Log.v("prototypev1","photos position "+position);
						photo = photos.get(position);
						goToShowPhoto(photo);
					}
				});
				
	            //Create listener
	           
			}
			else {
				//Show message no photos
				noPhotos.setVisibility(View.VISIBLE);
				mProgressBar.setVisibility(View.INVISIBLE);
				gridView.setVisibility(View.INVISIBLE);
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			mProgressBar.setVisibility(View.INVISIBLE);
			Toast.makeText(getActivity(),"Error download photos",  Toast.LENGTH_LONG).show();
		}	
    }

	public void goToShowPhoto(Photo photo) {
		Bundle data = new Bundle();
		data.putParcelable("Photo",photo);
		FragmentShowPhoto fshowPhoto = new FragmentShowPhoto();
		fshowPhoto.setArguments(data);
		
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.container_fragment_main,fshowPhoto);
		transaction.addToBackStack(null);
		transaction.commit();	
	}
}
