package com.example.prototypetfgv1.controller;

import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.example.prototypetfgv1.view.Utils;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ParseFunctions {
	
	private ApplicationClass appClass;
	
	public ParseFunctions(Context context) {
		super();
		appClass = (ApplicationClass) context.getApplicationContext();
	}
	
	public void initParse(Context context) {
		Parse.initialize(context, "Pz2ope2OFVDLDypgpdFMpeZiXhnPjm62tDv40b35", "ISRt37kcr6frHkhzvJ3Y9cxhvZxyocO7bP795y4c");
	}

    public ParseUser signUpInParse(String username,String password) {
		ParseUser parseUser = new ParseUser();
		parseUser.setUsername(username);
		parseUser.setPassword(password);
		parseUser.put("photos",new JSONArray());
		//add more atributes
		try {
			parseUser.signUp();
			return parseUser;
		} catch (ParseException e) {
			e.printStackTrace();
			Log.v("prototypev1","error signup "+e);
			return null;
		}
	}
    
    public boolean logInParse(String username, String password) {
		try {
			ParseUser.logIn(username, password);	
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v("prototypev1", "error log in Parse "+e);
			return false;
		}
		return true;
	}
	
    //Activity param is temporal
	public void updatePhoto(Bitmap photo,final Activity activity) {
		// Create the ParseFile
        ParseFile file = new ParseFile("photo2.jpeg",Utils.bitmapToByteArray(photo));
        // Upload the image into Parse Cloud
        file.saveInBackground();
        final ParseObject imgupload = new ParseObject("SimpleImage");
        imgupload.put("image", file);
        //Add JSONArray with id of users that belong to
        JSONArray userId = new JSONArray();
        userId.put(ParseUser.getCurrentUser().getObjectId());
        imgupload.put("usersId",userId);
        Log.v("prototypev1", "id photo "+imgupload.getObjectId());
        //Save photo
        imgupload.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				// TODO Auto-generated method stub
				if(e == null) {
					Toast.makeText(activity.getApplicationContext(), "Correct update photo",Toast.LENGTH_LONG).show();
					Log.v("prototypev1", "id photo "+imgupload.getObjectId());
					// Add photo of user (local user and ParseUser)
					String id = imgupload.getObjectId();
					appClass.getUser().addPhoto(id);
					//addPhotoInUser(id);
				} else {
					Toast.makeText(activity.getApplicationContext(), "Error update photo",Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	//Com que al final guardo l'usuari a la foto no em cal (Demanar al Santi)
	public void addPhotoInUser(final String id) {
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("objectId",ParseUser.getCurrentUser().getObjectId());
		query.findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(List<ParseUser> users, ParseException e) {
				// TODO Auto-generated method stub
				if (e == null) {
			        // The query was successful.
					//Always is 0 because the objectId is unique for all ParseUsers
					ParseUser user = users.get(0);
					user.getJSONArray("photos").put(id);
					user.saveInBackground(new SaveCallback() {
						@Override
						public void done(ParseException e) {
							// TODO Auto-generated method stub
							if(e == null) {
								Log.v("prototypev1", "foto afegida al usuari i update canviat");
								//Change date of update
								//Poder no caldra perquè ho guardare en local
								appClass.getUser().setUpdatedAt(String.valueOf((ParseUser.getCurrentUser().getUpdatedAt())));							
							}
							else 
								Log.v("prototypev1", "error al afegir foto");
						}
					});
			    } else {
			        // Something went wrong.
			    	Log.v("prototypev1", "error al afegir foto");
			    }
			}
		});
	}
}
