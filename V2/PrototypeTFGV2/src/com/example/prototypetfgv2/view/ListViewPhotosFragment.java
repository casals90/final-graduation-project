package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Album;
import com.example.prototypetfgv2.model.Photo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ListViewPhotosFragment extends Fragment {

	private ListView mListViewPhotos;
	private AdapterListViewShowPhotos adapter;
	private Controller controller;
	private ArrayList<Photo> photos;
	private ProgressBar mProgressBar;
	private Album album;
	private ImageLoader imageLoader;
	
	public ListViewPhotosFragment() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setTitle(R.string.albums);
		
		controller = (Controller) this.getActivity().getApplicationContext();
		
		//Fetch album data
		Bundle data = this.getArguments();
		album = data.getParcelable("Album");
		//Put album title in action bar
		getActivity().setTitle((album.getAlbumTitle()));
		
		imageLoader = ImageLoader.getInstance();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_list_view_photos,container,false);
		
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		mListViewPhotos = (ListView) view.findViewById(R.id.photos);
		//New task
		new DownloadPhotosTask().execute(album.getId());
		return view;
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
	        	
	        	boolean pauseOnScroll = false; // or true
				boolean pauseOnFling = true; // or false
				PauseOnScrollListener listener = new PauseOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling);
				mListViewPhotos.setOnScrollListener(listener);
	        	
				adapter = new AdapterListViewShowPhotos(getActivity().getApplicationContext(), photos,imageLoader);
				mListViewPhotos.setAdapter(adapter);
				mListViewPhotos.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
						Log.v("prototypev1","photo position "+position);
						//goToShowPhotoFullScreen(photos.get(position),position);
					}
				});
				
				/*mListViewPhotos.setOnScrollListener(new OnScrollListener() {
					
					@Override
					public void onScroll(AbsListView view,int firstVisibleItem, int visibleItemCount,int totalItemCount) {
						// TODO Auto-generated method stub
						Log.v("prototypev1","es mou l scroll");
					}

					@Override
					public void onScrollStateChanged(AbsListView view,int scrollState) {
						Log.v("prototypev1","es mou l scroll");
						if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
							Log.v("prototypev1","es mou l scroll");
							ImageLoader.getInstance().stop();
							
						}
						else {
							Log.v("prototypev1","es mou l scroll");
							ImageLoader.getInstance().resume();
						}
					}
					
					
				});*/
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			//Toast.makeText(getActivity(),"Error download photos",  Toast.LENGTH_LONG).show();
		}	
    }
}
