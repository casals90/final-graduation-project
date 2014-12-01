package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Album;
import com.example.prototypetfgv2.model.CurrentAlbum;

public class FragmentDialogChooseCurrentAlbum extends DialogFragment {
	
	private OnSetCurrentAlbum callback;
	
	
	private ListView listAlbums;
	private Controller controller;
	private ArrayList<Album> albums;
	private ProgressBar mProgressBarDialog;
	private CurrentAlbum currentAlbum;

	//Create a callback interface
	public interface OnSetCurrentAlbum {
        public void onSetCurrentAlbum(CurrentAlbum newCurrentAlbum);
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		controller = (Controller)getActivity().getApplicationContext();
		callback = (OnSetCurrentAlbum) getTargetFragment();
	}
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_fragment_choose_album,null);
        listAlbums = (ListView) view.findViewById(R.id.list_choose_album);
        
        mProgressBarDialog = (ProgressBar) view.findViewById(R.id.progressBarDialog);
        getDialog().setTitle("Set current album");
        getDialog().setCancelable(false);
        
        new DownloadCurrentAlbumsTask().execute();
        
        return view;
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
	
	private class DownloadCurrentAlbumsTask extends AsyncTask<Void, Void, Boolean> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressBarDialog.setVisibility(View.VISIBLE);
			listAlbums.setVisibility(View.INVISIBLE);
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
				mProgressBarDialog.setVisibility(View.INVISIBLE);
				listAlbums.setVisibility(View.VISIBLE);
				listAlbums.setAdapter(new ListViewAlbumsAdapter(albums, getActivity().getApplicationContext()));
				listAlbums.setOnItemClickListener(new OnItemClickListener() {
	
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
						Album album = albums.get(position);
						currentAlbum = new CurrentAlbum(album.getId(),album.getAlbumTitle(),album.getAlbumCover());
						//Put the selected album like a current album
						controller.setCurrentAlbum(currentAlbum);
						callback.onSetCurrentAlbum(currentAlbum);
						new SetCurrentAlbumTask().execute();
					}
				});
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
