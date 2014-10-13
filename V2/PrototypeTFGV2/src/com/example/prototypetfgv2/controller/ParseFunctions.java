package com.example.prototypetfgv2.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.example.prototypetfgv2.model.Photo;
import com.example.prototypetfgv2.model.User;
import com.example.prototypetfgv2.view.InputUsernameActivity;
import com.example.prototypetfgv2.view.MainActivity;
import com.example.prototypetfgv2.view.Utils;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class ParseFunctions {
	
	public ParseFunctions(Context context) {
		super();
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
		parseUser.put("photosNumber",0);
		parseUser.put("friendsNumber",0);
		//Add default profile picture
		//parseUser.put("profilePicture",new ParseFile(null));
	
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
    
    public boolean isUsernameExist(String username) {
    	ParseQuery<ParseUser> query = ParseUser.getQuery();
    	query.whereEqualTo("username",username);
        try {
			List<ParseUser> users = query.find();
			if(users.size() > 0)
				return true;	
			else 
				return false;		
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
        final ParseUser user = ParseUser.getCurrentUser();
        userId.put(user.getObjectId());
        imgupload.put("usersId",userId);
        Log.v("prototypev1", "id photo "+imgupload.getObjectId());
        //Save photo
        imgupload.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				// TODO Auto-generated method stub
				if(e == null) {
					//Increment the number photos column
					int photosNumber = user.getInt("photosNumber");
					photosNumber++;
					user.put("photosNumber", photosNumber);
					user.saveInBackground();
					Toast.makeText(activity.getApplicationContext(), "Correct update photo",Toast.LENGTH_LONG).show();
					Log.v("prototypev1", "id photo "+imgupload.getObjectId());
				} else {
					Toast.makeText(activity.getApplicationContext(), "Error update photo",Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	public boolean isLinkedWithTwitter(ParseUser user) {
		return ParseTwitterUtils.isLinked(user);
		
	}
	
	public boolean isLinkedWithFacebook(ParseUser user) {
		return ParseFacebookUtils.isLinked(user);
	}
	
	public boolean setUsername(String username) {
		if(isUsernameExist(username)) {
			return false;
		}
		else {
			ParseUser currentUser = ParseUser.getCurrentUser();
			currentUser.setUsername(username);
			try {
				currentUser.save();
				return true;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
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
				//Decrement the photos number column
				ParseUser user = ParseUser.getCurrentUser();
				int numberPhotos = user.getInt("photosNumber");
				numberPhotos--;
				if(numberPhotos <= 0) 
					user.put("photosNumber",0);
				else 
					user.put("photosNumber",numberPhotos);
				user.saveInBackground();
			}
		});	
	}
	
	public ArrayList<Photo> downloadPhotos() {
		ArrayList<Photo> myPhotos = new ArrayList<Photo>();
		List<ParseObject> ob;
		
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("SimpleImage");
        query.whereEqualTo("usersId",ParseUser.getCurrentUser().getObjectId());
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
	
	public ArrayList<User> downloadFriends() {
		ArrayList<User> users = new ArrayList<User>();
		JSONArray idFriends = getFriends();
		List<ParseUser> parseUsers;
		
		for(int i = 0; i < idFriends.length(); i ++) {
			ParseQuery<ParseUser> query = ParseUser.getQuery();
			try {
				query.whereEqualTo("objectId",idFriends.get(i));
				query.orderByDescending("username");
				parseUsers = query.find();
				ParseUser u = parseUsers.get(0);
				ParseFile profilePicture = (ParseFile)u.get("profilePicture");
				if(profilePicture == null)
					users.add(new User(u.getObjectId(),u.getUsername(), null,0));
				else
					users.add(new User(u.getObjectId(),u.getUsername(),profilePicture.getUrl(),0));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.v("prototypev1", "error getFriends users "+e);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.v("prototypev1", "error getFriends users "+e);
			}
		}
		return users;
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
				if(u.getParseFile("profilePicture") != null) {
					ParseFile profilePicture = (ParseFile)u.get("profilePicture");
					users.add(new User(u.getObjectId(),u.getUsername(),profilePicture.getUrl(),0));
				}
				//Not have a profile picture (default)
				else 
					users.add(new User(u.getObjectId(),u.getUsername(),null,0));	
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v("prototypev1", "error download search users");
			return null;
		}
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
	
	public boolean addFriend(final String idNewFriend) {
		//Add new friend in request list of current user
		ParseUser currentUser = ParseUser.getCurrentUser();
		//currentUser.getJSONArray("friendsRequest").put(idNewFriend);
		currentUser.getJSONArray("friends").put(idNewFriend);
		//Increment the friendsNumber column
		int friendsNumber = currentUser.getInt("friendsNumber");
		friendsNumber++;
		currentUser.put("friendsNumber",friendsNumber);
		try {
			currentUser.save();
			sendPush2(idNewFriend);
			return true;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}		
	}
	
	public void sendPush2(String id) {
		// Find users near a given location
		ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
		userQuery.whereEqualTo("objectId",id);
		 
		// Find devices associated with these users
		ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
		pushQuery.whereMatchesQuery("user",userQuery);
		 
		// Send push notification to query
		ParsePush push = new ParsePush();
		push.setQuery(pushQuery); // Set our Installation query
		push.setMessage("Hiii cardus");
		push.sendInBackground();
			
	}
	
	public void sendPush(String id) {
		Log.v("prototypev1", "send push "+id);
		/*ParseInstallation installation = ParseInstallation.getCurrentInstallation();
		installation.put("userId",id);
		//ParseInstallation installation = new ParseInstallation();
		//installation.put("userId",id);
		try {
			installation.save();
			
			
			// Create our Installation query
			ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
			pushQuery.whereEqualTo("userId",ParseUser.getCurrentUser().getObjectId());
			 
			// Send push notification to query
			ParsePush push = new ParsePush();
			push.setQuery(pushQuery); // Set our Installation query
			push.setMessage("Willie Hayes injured by own pop fly.");
			push.sendInBackground();
					
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v("prototypev1", "error push "+e);
		}*/
		
		// Find users near a given location
		ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
		userQuery.whereEqualTo("objectId",id);
		
		try {
			ParseUser u = userQuery.getFirst();
			Log.v("prototypev1", "send push "+u.getUsername());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		// Find devices associated with these users
		ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
		pushQuery.whereMatchesQuery("user", userQuery);
		
		
		 
		// Send push notification to query
		ParsePush push = new ParsePush();
		push.setQuery(pushQuery); // Set our Installation query
		push.setMessage("Free hotdogs at the Parse concession stand!");
		push.sendInBackground();
		
	}
	
	public String getUsername() {
		return ParseUser.getCurrentUser().getUsername();
	}
	
	public Bitmap getProfilePicture() {
		ParseFile profilePicture = (ParseFile) ParseUser.getCurrentUser().get("profilePicture");
		if(profilePicture != null) {
			try {
				return Utils.byteArrayToBitmap(profilePicture.getData());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public boolean setProfilePicture(Bitmap b) {
		ParseUser user = ParseUser.getCurrentUser();
		ParseFile img = new ParseFile("profile.jpeg",Utils.bitmapToByteArray(b));
		img.saveInBackground();
		user.put("profilePicture",img);
		try {
			user.save();
			return true;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean removeProfilePicture() {
		ParseUser user = ParseUser.getCurrentUser();
		user.remove("profilePicture");
		try {
			user.save();
			return true;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean isMyFriend(String id) {
		JSONArray friends = getFriends();
		return Utils.isElementExist(friends, id);
	}
	
	public boolean deleteFriend(String id) {
		JSONArray friends = Utils.removeElementToJsonArray(getFriends(),id);
		ParseUser user = ParseUser.getCurrentUser();
		user.put("friends",friends);
		//Decrement friendsNumber column
		int friendsNumber = user.getInt("friendsNumber");
		friendsNumber--;
		if(friendsNumber <= 0)
			user.put("friendsNumber",0);
		else
			user.put("friendsNumber",friendsNumber);
		try {
			user.save();
			return true;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public String getPhotosNumber() {
		return String.valueOf(ParseUser.getCurrentUser().getInt("photosNumber"));
	}
	
	public String getFriendsNumber() {
		return String.valueOf(ParseUser.getCurrentUser().getInt("friendsNumber"));
	}
	
	public void logInTwitter(final Activity activity) {
		ParseTwitterUtils.logIn(activity, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException err) {
					if (user == null) {
						Log.v("prototypev1", "logintwitter user cancelled");
					} 
					else if (user.isNew()) { 
						Log.v("prototypev1", "signup twitter new user");
						user.put("photos",new JSONArray());
						user.put("friends",new JSONArray());
						user.put("friendsRequest",new JSONArray());
						user.put("photosNumber",0);
						user.put("friendsNumber",0);
						try {
							user.save();
							//goToMainActivity(activity);
							goToInputUsername(activity);
						} 
						catch (ParseException e) {
							// TODO Auto-generated catch block
								e.printStackTrace();
								user = null; 
						}   
					} 
					else 
					    goToMainActivity(activity);
			}
		});
	}
	
	public JSONObject getTwitterData() {
		String twitterURL = "https://api.twitter.com/1.1/users/show.json?screen_name=#screen_name#";
		twitterURL = twitterURL.replace("#screen_name#",ParseTwitterUtils.getTwitter().getScreenName());
		
		HttpClient client = new DefaultHttpClient();
		HttpGet show = new HttpGet(twitterURL);
		ParseTwitterUtils.getTwitter().signRequest(show);
		try {
			HttpResponse response = client.execute(show);
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				Log.v("prototypev1", "OK http");
				String result = EntityUtils.toString(response.getEntity());
				JSONObject root = new JSONObject(result);
				return root;
			}
						
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.v("prototypev1", "error "+e);
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v("prototypev1", "error "+e);
			return null;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v("prototypev1", "error "+e);
			return null;
		}
		return null; 	
	}
	
	public String getProfilePictureTwitterURL() {
		try {
			return getTwitterData().getString("profile_image_url");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}	
	}
		
	public boolean isParseUserExist() {
		if(ParseUser.getCurrentUser() != null)
			return true;
		return false;
	}
	
	public void getUsernameForAuthUser() {
		ParseUser user = ParseUser.getCurrentUser();
		JSONObject auth = user.getJSONObject("authData");
		
		Log.v("prototypev1", "username auth "+auth);	
	}
	
	//Functions to change activities
	public void goToMainActivity(Activity activity) {
		Intent main = new Intent(activity, MainActivity.class);
        activity.startActivity(main);
	}
	
	public void goToInputUsername(Activity activity) {
		Intent inputUsername = new Intent(activity,InputUsernameActivity.class);
		activity.startActivity(inputUsername);
	}
}
