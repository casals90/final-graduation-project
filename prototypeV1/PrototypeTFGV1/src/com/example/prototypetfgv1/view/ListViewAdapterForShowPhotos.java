package com.example.prototypetfgv1.view;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.prototypetfgv1.R;
import com.example.prototypetfgv1.controller.Controller;
import com.example.prototypetfgv1.model.Photo;
 
public class ListViewAdapterForShowPhotos extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    ImageLoader imageLoader;
    private List<Photo> myPhotos = null;
    private ArrayList<Photo> arraylist;
    private Controller controller;
    
    // variables for refresh
    RelativeLayout headerRelativeLayout;
    ImageView arrowImage;
    ProgressBar progressBar;
    TextView headerTextView, lastUpdateDateTextView;
    
 
    public ListViewAdapterForShowPhotos(Context context,List<Photo> myPhotos) {
        this.context = context;
        this.myPhotos = myPhotos;
        inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<Photo>();
        this.arraylist.addAll(myPhotos);
        imageLoader = new ImageLoader(context);
        
        controller = new Controller(context);
        
        //init reload
    }
    
    public class ViewHolder {
        TextView title;
        ImageView photo;
        TextView createdAt;
        ImageButton dButton;
    }
 
    @Override
    public int getCount() {
        return myPhotos.size();
    }
 
    @Override
    public Object getItem(int position) {
        return myPhotos.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_item, null);
            // Locate the TextViews in listview_item.xml
            holder.title = (TextView) view.findViewById(R.id.title);
            holder.createdAt = (TextView) view.findViewById(R.id.createdAt);
            // Locate the ImageView in listview_item.xml@string/head_arrowImage
            holder.photo = (ImageView) view.findViewById(R.id.photo);
            holder.dButton = (ImageButton) view.findViewById(R.id.b_delete);		
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.title.setText(myPhotos.get(position).getTitle());
        holder.createdAt.setText(myPhotos.get(position).getCreatedAt());
        // Set the results into ImageView
        imageLoader.DisplayImage(myPhotos.get(position).getPhoto(),holder.photo);
        holder.dButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//delete photo
				String id = myPhotos.get(position).getId();
				//controller.deletePhoto(id);
				//show confirm dialog
				confirmDelete(id,position);
				Log.v("prototypev1", "pos in arraylist "+position);
			}
		});
        
        return view;
    }
    
    public void deletePhotoFromList(int position) {
    	myPhotos.remove(position);
    	notifyDataSetChanged();
    }
    
    public void confirmDelete(final String idDeletePhoto,final int position) {
    	// Instantiate an AlertDialog.Builder with its constructor
    	AlertDialog.Builder builder = new AlertDialog.Builder(context);

    	// Chain together various setter methods to set the dialog characteristics
    	builder.setMessage(R.string.dialog_delete_photo)
    	       .setTitle(R.string.dialog_delete_photo_message);
   	
    	// Add the buttons
    	builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	               // User clicked OK button
    	        	   controller.deletePhoto(idDeletePhoto);
    	        	   //reload listview
    	        	   //delete object to arraylist
    	        	   deletePhotoFromList(position);
    	        	   //notifyDataSetChanged();
    	           }
    	       });
    	builder.setNegativeButton(R.string.cancel,null);
    	/*builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	               // User cancelled the dialog
    	           }
    	       });*/
    	// Set other dialog properties
    	
    	// Create the AlertDialog
    	builder.create().show();
    }
}
