package com.example.testnews;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ListNewsAdapter extends BaseAdapter {

	//Declare Used Variables
    private Activity activity;
    private ArrayList<New> data;
    private static LayoutInflater inflater;
    
    //CustomAdapter Constructor
    public ListNewsAdapter(Activity activity, ArrayList<New> data) {
		this.activity = activity;
		this.data = data;
		
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
    
    //What is the size of Passed Arraylist Size 
    public int getCount() {
         
        if(data.size()<=0)
            return 1;
        return data.size();
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }
    
    static class ViewHolder {
    	public TextView username;
    	public ImageView photoUser;
    	public ImageView photo;
    	public TextView description;
    	public ImageButton bLike;
    	public ImageButton bComment;
    }
    
    // Depends upon data size called for each row , Create each ListView row 
    public View getView(int position, View convertView, ViewGroup parent) {
         
        View vi = convertView;
        
        //Crear classe tipu l'exemple
        Log.v("news","position"+position); 
        
        if(convertView == null) {
        	       	
            vi = inflater.inflate(R.layout.new_struct, null);
             
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            
            viewHolder.photoUser = (ImageView) vi.findViewById(R.id.photo_user);
            viewHolder.username = (TextView) vi.findViewById(R.id.username);
            viewHolder.photo = (ImageView) vi.findViewById(R.id.image);
            viewHolder.description = (TextView) vi.findViewById(R.id.photo_description);
            viewHolder.bLike = (ImageButton) vi.findViewById(R.id.button_like); 
            viewHolder.bComment = (ImageButton) vi.findViewById(R.id.button_comment);
            
	        //All xml views that contain in new_Struct.xml
	        /*ImageView photoUser = (ImageView) vi.findViewById(R.id.photo_user);
	        TextView userName = (TextView) vi.findViewById(R.id.username);
	        ImageView image = (ImageView) vi.findViewById(R.id.image);
	        TextView imgDescription = (TextView) vi.findViewById(R.id.photo_description);
	        ImageButton bLike = (ImageButton) vi.findViewById(R.id.button_like);
	        ImageButton bComment = (ImageButton) vi.findViewById(R.id.button_comment);*/
	        
	        
	        
	        vi.setTag(viewHolder);
        }
                
        // fill data
        ViewHolder holder = (ViewHolder) vi.getTag();
        final New n = data.get(position);
        
        holder.username.setText(n.getUsername());
        holder.description.setText(n.getDescription());
        holder.photo.setImageResource(n.getIdPhoto());
        
        // Listeners of bLike and bComment
        holder.bLike.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO Auto-generated method stub
				Log.v("cardusiiin","nom d'usuari del que clico el boto"+n.getUsername());
				Toast.makeText(activity.getApplicationContext(), "T'agrada la foto de "+n.getUsername(), Toast.LENGTH_SHORT).show();
			}
		});
        
        holder.bComment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(activity.getApplicationContext(), "Vui comentar la foto de "+n.getUsername(), Toast.LENGTH_SHORT).show();
			}
		});
        
        return vi;
    }
    
    /********* Called when Item click in ListView ***********
    private class OnItemClickListener  implements OnClickListener{           
        private int mPosition;
         
        OnItemClickListener(int position){
             mPosition = position;
        }
         
        @Override
        public void onClick(View arg0) {

   
          CustomListViewAndroidExample sct = (CustomListViewAndroidExample)activity;

         /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )***

            sct.onItemClick(mPosition);
        }               
    }  */ 
        
}
