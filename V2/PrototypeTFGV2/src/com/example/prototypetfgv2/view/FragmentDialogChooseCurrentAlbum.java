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
import com.example.prototypetfgv2.model.CurrentAlbum;

public class FragmentDialogChooseCurrentAlbum extends DialogFragment {

	private static final int REQUEST_DIALOG_CHOOSE_CURRENT_ALBUM = 2;
	
	private ListView listAlbums;
	private Controller controller;
	private ArrayList<CurrentAlbum> currentAlbums;
	private ProgressBar mProgressBarDialog;
	//private CurrentAlbum currentAlbum;
	
	private int selectedItem;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		controller = (Controller)getActivity().getApplicationContext();
		
		Bundle data = this.getArguments();
		if(data != null) {
			currentAlbums = data.getParcelableArrayList("listCurrentAlbums");
		}
	}
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_fragment_choose_album,null);
        listAlbums = (ListView) view.findViewById(R.id.list_choose_album);
        listAlbums.setAdapter(new AdapterForCurrentAlbum(getActivity().getApplicationContext(),currentAlbums));
        listAlbums.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				//Put the selected album like a current album
				//controller.setCurrentAlbum(currentAlbums.get(position));
				//currentAlbum = currentAlbums.get(position);
				selectedItem = position;
				new SetCurrentAlbumTask().execute();
			}
		});
        
        mProgressBarDialog = (ProgressBar) view.findViewById(R.id.progressBarDialog);
        getDialog().setTitle("My Dialog Title");
        getDialog().setCancelable(false);
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
			//return controller.setCurrentAlbum(currentAlbum);
			return true;
		}
		
		@Override
		protected void onPostExecute(final Boolean success) {
			if(success) {
				mProgressBarDialog.setVisibility(View.INVISIBLE);
				getTargetFragment().onActivityResult(getTargetRequestCode(),-1, getActivity().getIntent().putExtra("selectedItem",selectedItem));
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
