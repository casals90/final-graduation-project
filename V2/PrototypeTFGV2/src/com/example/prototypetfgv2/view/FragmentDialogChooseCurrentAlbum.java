package com.example.prototypetfgv2.view;

import java.util.ArrayList;

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
import com.parse.ParseUser;

public class FragmentDialogChooseCurrentAlbum extends DialogFragment {

	private ListView listAlbums;
	private Controller controller;
	private ArrayList<Album> albums;
	private ProgressBar mProgressBarDialog;
	private CurrentAlbum currentAlbum;
	private String currentAlbumId;
	private int positionCurrentAlbum;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		controller = (Controller)getActivity().getApplicationContext();
		
		Bundle data = getArguments();
		currentAlbumId = data.getString("currentAlbumId");
		Log.v("prototypev1", "idCurrentAlbum "+currentAlbumId);
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
	
	public int getPositionCurrentAlbum(String id) {
		
		for(int i = 0; i < albums.size(); i++) {
			if(id.compareTo(albums.get(i).getId()) == 0)
					return i;
		}
		Log.v("prototypev1","return -1 ");
		return -1;
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
				//Only find current album if user assigned one, else the user haven't current album
				if(currentAlbumId != null)
					positionCurrentAlbum = getPositionCurrentAlbum(currentAlbumId);
				else
					positionCurrentAlbum = -1;
				
				Log.v("prototypev1", "pos current album "+positionCurrentAlbum);
				listAlbums.setAdapter(new ListViewAlbumsAdapter(albums, getActivity().getApplicationContext(),positionCurrentAlbum));
				listAlbums.setOnItemClickListener(new OnItemClickListener() {
	
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
						if(positionCurrentAlbum != position) {
							Album album = albums.get(position);
							currentAlbum = new CurrentAlbum(album.getId(),album.getAlbumTitle(),album.getAlbumCover());
							//Put the selected album like a current album
							controller.setCurrentAlbum(currentAlbum);
							//callback.onSetCurrentAlbum(currentAlbum);
							new SetCurrentAlbumTask().execute();
						}
						else
							getDialog().dismiss();
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
