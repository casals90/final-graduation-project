package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Album;
import com.example.prototypetfgv2.model.CurrentAlbum;

public class FragmentDialogChooseCurrentAlbum extends DialogFragment {

	private ListView listAlbums;
	private Controller controller;
	private ArrayList<Album> albums;
	private ArrayList<CurrentAlbum> currentAlbums;
	private ProgressBar mProgressBarDialog;
	private TextView noAlbums;
	private CurrentAlbum currentAlbum;
	 
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		controller = (Controller)getActivity().getApplicationContext();
	}
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_fragment_choose_album,null);
        listAlbums = (ListView) view.findViewById(R.id.list_choose_album);
        mProgressBarDialog = (ProgressBar) view.findViewById(R.id.progressBarDialog);
        noAlbums = (TextView) view.findViewById(R.id.no_albums);
        getDialog().setTitle("My Dialog Title");
   
        new DownloadCurrentAlbumTask().execute();
        return view;
    }
	
	private class DownloadCurrentAlbumTask extends AsyncTask<Void, Void, Boolean> {
    	
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        	listAlbums.setVisibility(View.INVISIBLE);
        	mProgressBarDialog.setVisibility(View.VISIBLE);
        	noAlbums.setVisibility(View.INVISIBLE);
        }
 
        @Override
        protected Boolean doInBackground(Void... params) {
        	albums = controller.getAlbums();
            if(albums != null)
            	return true;
            return false;		
        }
 
        @Override
        protected void onPostExecute(final Boolean success) {
			if(success) {
                currentAlbums = new ArrayList<CurrentAlbum>();
                for(Album a: albums) {
                	currentAlbums.add(new CurrentAlbum(a.getId(),a.getAlbumTitle(),a.getAlbumCover()));
                }
                listAlbums.setAdapter(new AdapterForCurrentAlbum(getActivity().getApplicationContext(),currentAlbums));
                listAlbums.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
						//Put the selected album like a current album
						//controller.setCurrentAlbum(currentAlbums.get(position));
						currentAlbum = currentAlbums.get(position);
						new SetCurrentAlbumTask().execute();
					}
				});
                listAlbums.setVisibility(View.VISIBLE);
            	mProgressBarDialog.setVisibility(View.INVISIBLE);
            	noAlbums.setVisibility(View.INVISIBLE);
        	}
        	else {
        		noAlbums.setVisibility(View.VISIBLE);
        		listAlbums.setVisibility(View.INVISIBLE);
        		mProgressBarDialog.setVisibility(View.INVISIBLE);
        	}	
        }

		@Override
		protected void onCancelled() {
			super.onCancelled();
			Toast.makeText(getActivity(),"Error download albums",  Toast.LENGTH_LONG).show();
		}
    }

	private class SetCurrentAlbumTask extends AsyncTask<Void, Void, Boolean> {
	
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressBarDialog.setVisibility(View.VISIBLE);
			listAlbums.setVisibility(View.INVISIBLE);
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			return controller.setCurrentAlbum(currentAlbum);		
		}
		
		@Override
		protected void onPostExecute(final Boolean success) {
			if(success) {
				mProgressBarDialog.setVisibility(View.INVISIBLE);
				//finish dialog
				getDialog().dismiss();
			}
			else
				Toast.makeText(getActivity(),"Error current album",  Toast.LENGTH_LONG).show();
		}
		
		@Override
		protected void onCancelled() {
			super.onCancelled();
			Toast.makeText(getActivity(),"Error download albums",  Toast.LENGTH_LONG).show();
		}
 	}
}
