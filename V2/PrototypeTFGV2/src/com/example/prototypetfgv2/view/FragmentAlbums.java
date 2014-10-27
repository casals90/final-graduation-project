package com.example.prototypetfgv2.view;

import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Album;

public class FragmentAlbums extends Fragment {
	
	private Controller controller;
	
	private ListView listviewAlbums;
	private ProgressBar mProgressBar;
	private Button newAlbum;
	private TextView noAlbums;
	
	private ListViewAdapterForAlbums adapter;
	private List<Album> albums;
    
	public FragmentAlbums() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		controller = (Controller) this.getActivity().getApplicationContext();
		getActivity().setTitle(R.string.albums);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_albums,container,false);
		
		noAlbums = (TextView) view.findViewById(R.id.label_no_albums);
		listviewAlbums = (ListView) view.findViewById(R.id.list_albums);
		/*listviewAlbums.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
				// TODO Auto-generated method stub
				Album album = albums.get(position);
				//Go to activity to show all photos
			}
		});*/
		
		newAlbum = (Button) view.findViewById(R.id.add_album);
		newAlbum.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//change Fragment
				goToNewAlbum();
			}
		});
		
		//listview = (ListView) view.findViewById(R.id.listview);
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar_albums);
		//Execute new Thread for download photos
		//new RemoteDataTask().execute();
		new DownloadAlbumsTask().execute();
		return view;
	}
	
	public void goToNewAlbum() {
		FragmentManager manager = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.container_fragment_main,new FragmentNewAlbum());
		transaction.addToBackStack(null);
		transaction.commit();
	}
	
   private class DownloadAlbumsTask extends AsyncTask<Void, Void, Boolean> {
    	
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        	mProgressBar.setVisibility(View.VISIBLE);
        }
 
        @Override
        protected Boolean doInBackground(Void... params) {
        	//albums = controller.getParseFunctions().getAlbums();
        	albums = controller.getAlbums();
            if(albums != null && albums.size() > 0)
            	return true;
            return false;
            		
        }
 
        @Override
        protected void onPostExecute(final Boolean success) {
        	if(success) {
        		//hidden label no albums 
        		noAlbums.setVisibility(View.INVISIBLE);
				// Pass the results into ListViewAdapter.java
	            adapter = new ListViewAdapterForAlbums(getActivity(),albums);
	            // Binds the Adapter to the ListView
	            listviewAlbums.setAdapter(adapter);
	            // Close the progressdialog
	            mProgressBar.setVisibility(View.INVISIBLE);
	            //Create listener
	            listviewAlbums.setOnItemClickListener(new OnItemClickListener() {

	    			@Override
	    			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
	    				// TODO Auto-generated method stub
	    				Album album = albums.get(position);
	    				//pass the user to new fragment and change fragment
	    				goToShowAlbum(album);
	    			}
	    		});
	            
        	}
        	else {
        		noAlbums.setVisibility(View.VISIBLE);
        		listviewAlbums.setVisibility(View.INVISIBLE);
        		mProgressBar.setVisibility(View.INVISIBLE);
        	}
        		
        }

		@Override
		protected void onCancelled() {
			super.onCancelled();
			Toast.makeText(getActivity(),"Error download albums",  Toast.LENGTH_LONG).show();
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
