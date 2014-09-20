package com.example.prototypetfgv1.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.example.prototypetfgv1.model.Photo;
import com.example.prototypetfgv1.model.User;
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
		parseUser.put("friends",new JSONArray());
		parseUser.put("friendsRequest",new JSONArray());
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
					//String id = imgupload.getObjectId();
					//appClass.getUser().addPhoto(id);
					//addPhotoInUser(id);
				} else {
					Toast.makeText(activity.getApplicationContext(), "Error update photo",Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	//Com que al final guardo l'usuari a la foto no em cal (Demanar al Santi)
	//Per ara no ho utilitzo
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
								//appClass.getUser().setUpdatedAt(String.valueOf((ParseUser.getCurrentUser().getUpdatedAt())));							
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
	
	public void logout() {
		ParseUser.logOut();
	}
	
	public void deletePhoto(final String id) {
		//First delete from photo object
		ParseQuery<ParseObject> photoQuery = ParseQuery.getQuery("SimpleImage");
		photoQuery.whereEqualTo("objectId",id);
		photoQuery.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> photos, ParseException e) {
				// TODO Auto-generated method stub
				if(e == null) {
					ParseObject p = photos.get(0);
					JSONArray usersId = p.getJSONArray("usersId");
					//only one user, and can delete photo from parse
					if(usersId.length() == 1) {
						//Delete object directly
						p.deleteInBackground();
					}
					//More than one user
					else {
						usersId = Utils.removeElementToJsonArray(usersId, id);
						p.put("usersId",usersId);
						p.saveInBackground();
					}
				}
			}
		});	
		//Next delete from ParseUser
		/*ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
		userQuery.whereEqualTo("objectId",ParseUser.getCurrentUser().getObjectId());
		userQuery.findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(List<ParseUser> users, ParseException e) {
				if(e == null) {
					ParseUser user = users.get(0);
					JSONArray photos = user.getJSONArray("photos");
					Log.v("prototypev1", "array abans "+photos.length());
					photos = Utils.removeElementToJsonArray(photos, id);
					Log.v("prototypev1", "array despres "+photos.length());
					user.put("photos",photos);
					user.saveInBackground(new SaveCallback() {
						@Override
						public void done(ParseException arg0) {
							// TODO Auto-generated method stub
							Log.v("prototypev1", "array user foto borrada");
						}
					});
				} 
				else {
					Log.v("prototypev1", "error borrar foto");
				}
				
			}
		});	*/
	}
	
	public ArrayList<Photo> downloadPhotos() {
		ArrayList<Photo> myPhotos = new ArrayList<Photo>();
		List<ParseObject> ob;
		
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("SimpleImage");
        query.whereEqualTo("usersId",appClass.getUser().getId());
        query.orderByDescending("createdAt");
        try {
			ob = query.find();
			for (ParseObject p : ob) {
	            ParseFile image = (ParseFile) p.get("image");
	            Photo photo = new Photo();
	            photo.setId(p.getObjectId());
	            photo.setTitle("title");
	            photo.setPhoto(image.getUrl());
	            photo.setCreatedAt(String.valueOf(p.getCreatedAt()));
	            myPhotos.add(photo);
	        }
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
        return myPhotos;
	}
	
	 public ArrayList<User> getUsers(String username) {
		ArrayList<User> users = new ArrayList<User>();
		List<ParseUser> parseUsers;
		
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereStartsWith("username",username);
		query.orderByDescending("username");
		try {
			parseUsers = query.find();
			for(ParseUser u : parseUsers) {
				users.add(new User(u.getObjectId(),u.getUsername(),null,0));
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v("prototypev1", "error download search users");
			return null;
		}
		Log.v("prototypev1", "size users search "+users.size());
		return users;
	} 
	 
	public JSONArray getFriends() {
		JSONArray f = ParseUser.getCurrentUser().getJSONArray("friends");
		return f;
	}
	
	public JSONArray getFriendsRequest() {
		JSONArray r = ParseUser.getCurrentUser().getJSONArray("friendsRequest");
		return r;
	}
	
	public void addFriendsRequest(final String idNewFriend) {
		//Add new friend in request list of current user
		ParseUser currentUser = ParseUser.getCurrentUser();
		currentUser.getJSONArray("friendsRequest").put(idNewFriend);
		currentUser.saveInBackground();
		
		//Add request in other user
		/*ParseQuery<ParseUser> addFriendsRequest = ParseUser.getQuery();
		addFriendsRequest.whereEqualTo("objectId",idNewFriend);
		addFriendsRequest.findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(List<ParseUser> users, ParseException e) {
				// TODO Auto-generated method stub
				if(e == null) {
					ParseUser newFriend = users.get(0);
					newFriend.getJSONArray("friendsRequest").put(idNewFriend);
					newFriend.saveInBackground(new SaveCallback() {
						
						@Override
						public void done(ParseException e) {
							// TODO Auto-generated method stub
							if(e == null)
								Log.v("prototypev1", "save callback");
							else
								Log.v("prototypev1", "save callback error"+e);
						}
					});
					Log.v("prototypev1", "save in background new user "+newFriend.getUsername());
				}
				else {
					//error
					Log.v("prototypev1", "error newFriend");
				}
			}
		});*/
	}
}
