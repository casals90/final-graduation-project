package com.example.prototypetfgv2.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.security.auth.callback.Callback;

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

import com.example.prototypetfgv2.model.Album;
import com.example.prototypetfgv2.model.Comment;
import com.example.prototypetfgv2.model.CurrentAlbum;
import com.example.prototypetfgv2.model.CurrentUser;
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
	
	// All customs ParseObjects TODO
	//Album
	private final String ALBUM = "Album";
	private final String ALBUMPHOTO = "idPhotos";
	//Photo
	private final String PHOTO = "SimpleImage";
	//User
	private final String PHOTOS = "photos";
	private final String FRIENDS = "friends";
	private final String ALBUMS = "albums";
	private final String PHOTOSNUMBER = "photosNumber";
	private final String FRIENDSNUMBER = "friendsNumber";
	
	
	public ParseFunctions(Context context) {
		super();
	}
	
	public void initParse(Context context) {
		Parse.initialize(context, "Pz2ope2OFVDLDypgpdFMpeZiXhnPjm62tDv40b35", "ISRt37kcr6frHkhzvJ3Y9cxhvZxyocO7bP795y4c");
		ParseTwitterUtils.initialize("1LRilPY6fB23EKrqq6LkD6DPN", "oOsUsmOcRihiBpdy8ILSvjX4lcKTyb2Dnqaz9ChaQado7ZFyFj");
	}

    public ParseUser signUpInParse(String username,String password) {
		ParseUser parseUser = new ParseUser();
		parseUser.setUsername(username);
		parseUser.setPassword(password);
		parseUser.put("friends",new JSONArray());
		//parseUser.put("photos",new JSONArray());
		//parseUser.put("friendsRequest",new JSONArray());
		//parseUser.put("albums",new JSONArray());
		//parseUser.put("photosNumber",0);
		//parseUser.put("friendsNumber",0);
		//parseUser.put("albumNumber",0);
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
			ParseUser user = query.getFirst();
			if(user != null)
				return true;	
			else 
				return false;		
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return true;
    }
    
    // No testejada
    public int getAlbumsNumber() {
    	ParseQuery<ParseObject> query = ParseQuery.getQuery("Album");
    	query.whereEqualTo("idMembers",ParseUser.getCurrentUser().getObjectId());
    	try {
			return query.count();
		} catch (ParseException e) {
			e.printStackTrace();
			return -1;
		}
    }
    
    public ArrayList<String> getPhotosLikedCurrentUser() {
    	ArrayList<String> likes = new ArrayList<String>();
    	
    	ParseQuery<ParseObject> query = ParseQuery.getQuery("Like");
    	query.whereEqualTo("idUser",ParseUser.getCurrentUser().getObjectId());
    	try {
			List<ParseObject> parseLikes = query.find();
			for(ParseObject l: parseLikes) {
				likes.add(l.getString("idPhoto"));
			}
			return likes;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
    }
    
    /*public ArrayList<String> getPhotosCommentedUser() {
    	ArrayList<String> comments = new ArrayList<String>();
    	
    	ParseQuery<ParseObject> query = ParseQuery.getQuery("Comment");
    	query.whereEqualTo("idUser",ParseUser.getCurrentUser().getObjectId());
    	try {
			List<ParseObject> parseComments = query.find();
			for(ParseObject c: parseComments) {
				comments.add(c.getString("idPhoto"));
			}
			return comments;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
    }*/
	
    
    public void updatePhoto(Bitmap photo,String title,final Activity activity) {
    	// Create the ParseFile
        ParseFile file = new ParseFile("photo.jpeg",Utils.bitmapToByteArray(photo));
        // Upload the image into Parse Cloud
        file.saveInBackground();
        final ParseObject photoUpload = new ParseObject("Photo");
        //photoUpload.put("photo",file);
        photoUpload.put("title",title);
        photoUpload.put("likesNumber",0);
        photoUpload.put("commentsNumber",0);
        //Save photo file in other ParseObject
        final ParseObject uploadParseFile = new ParseObject("PhotoFile");
        uploadParseFile.put("photo",file);
        try {
			uploadParseFile.save();
			//Log.v("prototypev1", "file save");
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        //Save the owner of the photo
        photoUpload.put("ownerUser",ParseUser.getCurrentUser().getObjectId());
        //Log.v("prototypev1", "upload photo put current album ");
        String album;
		try {
			album = ParseUser.getCurrentUser().getJSONObject("currentAlbum").getString("id");
			photoUpload.put("ownerAlbum",album);
			photoUpload.put("idPhotoFile",uploadParseFile.getObjectId());
			photoUpload.put("photoFileUrl",file.getUrl());
			//add more atributes
			try {
				photoUpload.save();
				//Log.v("prototypev1", "save photo class");
				Toast.makeText(activity.getApplicationContext(), "Update Photo",Toast.LENGTH_LONG).show();
			} catch (ParseException e) {
				e.printStackTrace();
				//Log.v("prototypev1", "erro save photo"+e);
			}

		} catch (JSONException e) {
			e.printStackTrace();
			//Log.v("prototypev1", "erro save photo"+e);
		}
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
				e.printStackTrace();
				return false;
			}
		}
	}
	
	public void logout() {
		ParseUser.logOut();
	}
	
	
	public ArrayList<String> getPhotosFromAlbum(String idAlbum) {
		ArrayList<String> photos = new ArrayList<String>();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
		query.whereEqualTo("ownerAlbum",idAlbum);
		query.orderByDescending("createdAt");
		try {
			List<ParseObject> obs = query.find();
			for(ParseObject ob: obs) {
				photos.add(ob.getObjectId());
			}
			return photos;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getURLPhoto(String idPhotoFile) {
		Log.v("prototypev1", "get url start idPhotoFile ="+idPhotoFile);
		ParseQuery<ParseObject> query = ParseQuery.getQuery("PhotoFile");
		query.whereEqualTo("objectId",idPhotoFile);
		try {
			//ParseObject file = query.getFirst();
			List<ParseObject> obs = query.find();
			ParseObject file = obs.get(0);
			Log.v("prototypev1", "despres get first recive file PArseOBject "+file.getObjectId());
			ParseFile f = file.getParseFile("photo");
			String url = f.getUrl();
			Log.v("prototypev1", "get url correct ");
			return url;
		} catch (ParseException e) {
			e.printStackTrace();
			Log.v("prototypev1", "error get url "+e);
			return null;
		}
	}
	
	public ArrayList<Photo> downloadPhotosFromAlbum(String idAlbum) {
		//Log.v("prototypev1", "downloadphotos from album ");
		ArrayList<Photo> photos = new ArrayList<Photo>();
		HashMap<String, User> ownerUsers = new HashMap<String, User>();
		User ownerUser;
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
		query.whereEqualTo("ownerAlbum",idAlbum);
		query.orderByDescending("createdAt");
		try {
			List<ParseObject> obs = query.find();
			//Log.v("prototypev1", "downloadphotos from album "+obs.size());
			for(ParseObject o : obs) {
				String idUser = o.getString("ownerUser");
				//Search likes and comments number
				//int likes = countPhotoComments(o.getObjectId());
				//int comments = countPhotoLikes(o.getObjectId());
				//Log.v("prototypev1", "downloadphotos from album likes and comments "+likes+" "+comments);
				if(ownerUsers.containsKey(idUser)) {
					ownerUser = ownerUsers.get(idUser);
				}
				else {
					//download and put inside the hasmap
					ownerUser = getUser(o.getString("ownerUser"));
					ownerUsers.put(idUser, ownerUser);
				}
				Photo photo = new Photo(o.getObjectId(),o.getString("title"),o.getString("photoFileUrl"),String.valueOf(o.getCreatedAt()),ownerUser,o.getInt("likesNumber"),o.getInt("commentsNumber"));
				photos.add(photo);
			}	
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return photos;
	}
	
	public int countPhotosNumberFromAlbum(String idAlbum) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
		query.whereEqualTo("ownerAlbum",idAlbum);
		try {
			return query.count();
		} catch (ParseException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public User getUser(String id) {
		//Log.v("prototypev1", "get user start");
		User user;
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("objectId",id);
		try {
			ParseUser u = query.getFirst();
			String url = u.getString("profilePictureUrl");
			if(url == null)
				user = new User(u.getObjectId(),u.getUsername(),null);
			else
				user = new User(u.getObjectId(),u.getUsername(),url);
			Log.v("prototypev1", "get user end");
			return user;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<Album> getAlbums() {
		ArrayList<Album> albums = new ArrayList<Album>();
		List<ParseObject> ob;
		//Log.v("prototypev1", "Start download albums");
		ParseQuery<ParseObject> query = ParseQuery.getQuery(ALBUM);
		query.whereEqualTo("idMembers",ParseUser.getCurrentUser().getObjectId());
		query.orderByDescending("createdAt");
		try {
			ob = query.find();
			if(ob.size() == 0) {
				Log.v("prototypev1", "return null");
				return null;
			}
			//Log.v("prototypev1", "hi ha "+ob.size()+" albums");
			//int i = 0;
			for(ParseObject a : ob) {
				//Log.v("prototypev1", "download album "+i);
				//i++;
				List<String> members = Utils.jsonArrayToListString(a.getJSONArray("idMembers"));
				//Put a random cover photo of album
				//miro si te mes de 1 foto
				int photoNumber = countPhotosNumberFromAlbum(a.getObjectId());
				if(photoNumber > 0) {
					ArrayList<String> idPhotos = getPhotosFromAlbum(a.getObjectId());
					int random = Utils.getRandomInt(idPhotos.size());
					//Download Random photo
					Photo photo = downloadPhoto(idPhotos.get(random));
					//String url = getURLPhoto(idPhotos.get(random));
					//download photo id random
					albums.add(new Album(a.getObjectId(),photo.getId(),a.getString("albumTitle"),members));
					//getPhotoMoreLikesInAlbum(a.getObjectId());
				}
				else 
					albums.add(new Album(a.getObjectId(),null,a.getString("albumTitle"),members));
			}	
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		//Log.v("prototypev1", "end download albums");
		return albums;
	}
	
	public String getPhotoUrl(String id) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
		query.whereEqualTo("objectId",id);
		try {
			ParseObject photo = query.getFirst();
			return photo.getString("photoFileUrl");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public Photo downloadPhoto(String idPhoto) {
		//Log.v("prototypev1", "download photo function start idPhoto "+idPhoto);
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
		query.whereEqualTo("objectId",idPhoto);
		try {
			//Log.v("prototypev1", "dins el try");
			ParseObject p = query.getFirst();
			//List<ParseObject> photos = query.find();
			//ParseObject p = photos.get(0);
			//ParseObject p = query.getFirst();
			//Log.v("prototypev1", "despres getFirst");
			//String url = getURLPhoto(p.getString("idPhotoFile"));
			String url = p.getString("photoFileUrl");
			//Log.v("prototypev1", "despres geturl");
			User ownerUser = getUser(p.getString("ownerUser"));
			//Log.v("prototypev1", "despres ownerUser");
			//Log.v("prototypev1", "final download only 1 photo");
			//Error aqui
			Photo photo =  new Photo(p.getObjectId(),p.getString("title"),url,String.valueOf(p.getCreatedAt()));
			//Log.v("prototypev1", "return correct download photo function");
			return photo;
		} catch (ParseException e) {
			e.printStackTrace();
			//Log.v("prototypev1", "error download photo function");
			return null;
		}	
	}
	
	//Actulitzar amb el nombre que hi ha a la classe foto directament
	public String getPhotoMoreLikesInAlbum(String idAlbum) {
		//Download Album (getFirst)
		ArrayList<String> photos = getPhotosFromAlbum(idAlbum);
		HashMap<String,Integer> likes = new HashMap<String,Integer>();
		//Mirar like per cada photo
		for(int i = 0; i < photos.size(); i++) {
			likes.put(photos.get(i),countPhotoLikes(photos.get(i)));
		}
		//Falta buscar el mes gran a dins al hashmap i returnar la key
		return null;	
	}
	
	public ArrayList<User> downloadFriends() {
		ArrayList<User> users = new ArrayList<User>();
		JSONArray idFriends = getFriends();
		
		for(int i = 0; i < idFriends.length(); i ++) {
			ParseQuery<ParseUser> query = ParseUser.getQuery();
			try {
				query.whereEqualTo("objectId",idFriends.get(i));
				query.orderByDescending("username");
				
				ParseUser u = query.getFirst();
				ParseFile profilePicture = (ParseFile)u.get("profilePicture");
				if(profilePicture == null)
					users.add(new User(u.getObjectId(),u.getUsername(), null));
				else
					users.add(new User(u.getObjectId(),u.getUsername(),profilePicture.getUrl()));
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return users;
	 }
	
	public ArrayList<User> downloadFriendsInputSearch(String input) {
		ArrayList<User> users = new ArrayList<User>();
		JSONArray f = ParseUser.getCurrentUser().getJSONArray("friends");
		ArrayList<String> friends = Utils.jsonArrayToArrayListString(f);
		
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereStartsWith("username",input);
		query.whereContainedIn("objectId",friends);
		query.orderByAscending("username");
		try {
			List<ParseUser> parseUsers = query.find();
			for(ParseUser u : parseUsers) {
				ParseFile profilePicture = (ParseFile)u.get("profilePicture");
				if(profilePicture == null)
					users.add(new User(u.getObjectId(),u.getUsername(), null));
				else
					users.add(new User(u.getObjectId(),u.getUsername(),profilePicture.getUrl()));
			}
			return users;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
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
					users.add(new User(u.getObjectId(),u.getUsername(),profilePicture.getUrl()));
				}
				//Not have a profile picture (default)
				else 
					users.add(new User(u.getObjectId(),u.getUsername(),null));	
			}
			return users;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
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
		push.setMessage("Hellow world");
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
	
	public String getProfilePictureUrl() {
		return ParseUser.getCurrentUser().getString("profilePictureUrl");
	}
	
	public boolean setProfilePicture(Bitmap b) {
		ParseUser user = ParseUser.getCurrentUser();
		ParseFile img = new ParseFile("profile.jpeg",Utils.bitmapToByteArray(b));
		try {
			img.save();
			user.put("profilePictureUrl",img.getUrl());
			return true;
		} catch (ParseException e1) {
			e1.printStackTrace();
			return false;
		}
		/*user.put("profilePicture",img);
		try {
			user.save();
			return true;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}*/
	}
	
	public boolean removeProfilePicture() {
		ParseUser user = ParseUser.getCurrentUser();
		user.remove("profilePicture");
		try {
			user.save();
			return true;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean isMyFriend(String id) {
		JSONArray friends = getFriends();
		return Utils.isElementExist(friends, id);
	}
	
	//Revisar
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
						user.put("friends",new JSONArray());
						try {
							user.save();
							goToInputUsername(activity);
						} 
						catch (ParseException e) {
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
				String result = EntityUtils.toString(response.getEntity());
				JSONObject root = new JSONObject(result);
				return root;
			}
						
		} catch (ClientProtocolException e) {
			Log.v("prototypev1", "error "+e);
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			Log.v("prototypev1", "error "+e);
			return null;
		} catch (JSONException e) {
			Log.v("prototypev1", "error "+e);
			return null;
		}
		return null; 	
	}
	
	public String getProfilePictureTwitterURL() {
		try {
			return getTwitterData().getString("profile_image_url");
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}	
	}
		
	public boolean isParseUserExist() {
		if(ParseUser.getCurrentUser() != null)
			return true;
		return false;
	}
	
	public boolean newAlbum(JSONArray members,String albumName) {
		ParseObject newAlbum = new ParseObject(ALBUM);
		//Admin is current user
		newAlbum.put("idAdmin",ParseUser.getCurrentUser().getObjectId());
		//Put current user in members
		members.put(ParseUser.getCurrentUser().getObjectId());
		if(albumName == null)
			newAlbum.put("albumTitle","Default name");
		else
			newAlbum.put("albumTitle",albumName);
		newAlbum.put("idMembers",members);
		//Increment number of albums
		ParseUser user = ParseUser.getCurrentUser();
		user.increment("albumsNumber");
		user.saveInBackground();
		try {
			newAlbum.save();
			return true;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public ArrayList<User> downloadUsersList(ArrayList<String> users) {
		ArrayList<User> downloads = new ArrayList<User>();
		List<ParseUser> parseUsers;
		
		for(int i = 0; i < users.size(); i ++) {
			ParseQuery<ParseUser> query = ParseUser.getQuery();
			query.whereEqualTo("objectId",users.get(i));
			query.orderByDescending("username");
			try {
				parseUsers = query.find();
				ParseUser u = parseUsers.get(0);
				ParseFile profilePicture = (ParseFile)u.get("profilePicture");
				if(profilePicture == null)
					downloads.add(new User(u.getObjectId(),u.getUsername(), null));
				else
					downloads.add(new User(u.getObjectId(),u.getUsername(),profilePicture.getUrl()));
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
		return downloads;
	}
	
	public CurrentAlbum getCurrentAlbum() {
		JSONObject currentAlbum = ParseUser.getCurrentUser().getJSONObject("currentAlbum");
		if(currentAlbum != null)
			return new CurrentAlbum(currentAlbum);
		return null;
		
	}
	
	public boolean setCurrentAlbum(CurrentAlbum currentAlbum) {
		ParseUser currentUser = ParseUser.getCurrentUser();
		currentUser.put("currentAlbum",currentAlbum.getCurrentAlbum());
		try {
			currentUser.save();
			return true;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void setCoverPhotoFromAlbum(String idAlbum,final String idPhoto) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Album");
		query.whereEqualTo("objectId",idAlbum);
		//Canviar a find
		query.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> albums, ParseException e) {
				if(e == null) {
					ParseObject album = albums.get(0);
					album.put("albumCover",idPhoto);
					album.saveInBackground();
				}
			}
		});
	}
	
	public String getUsername(String id) {
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("objectId",id);
		try {
			List<ParseUser> users = query.find();
			return users.get(0).getUsername();
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
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
	
	public boolean likePhoto(String id) {
		ParseObject like = new ParseObject("Like");
		like.put("idUser",ParseUser.getCurrentUser().getObjectId());
		like.put("idPhoto",id);
		try {
			like.save();
			incrementLikesNumber(id);
			return true;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean incrementLikesNumber(String id) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
		query.whereEqualTo("objectId",id);
		try {
			ParseObject photo = query.getFirst();
			photo.increment("likesNumber");
			photo.save();
			return true;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/*public boolean decrementLikesNumber(String id) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
		query.whereEqualTo("objectId",id);
		try {
			ParseObject photo = query.getFirst();
			int 
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}*/
	
	public boolean unlikePhoto(String id) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Like");
		query.whereEqualTo("idPhoto",id);
		query.whereEqualTo("idUser",ParseUser.getCurrentUser().getObjectId());
		try {
			ParseObject like = query.getFirst();
			if(like != null)
				like.delete();
			return true;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	public boolean newComment(String idPhoto,String text) {
		ParseObject comment = new ParseObject("Comment");
		comment.put("idPhoto",idPhoto);
		comment.put("idUser",ParseUser.getCurrentUser().getObjectId());
		comment.put("comment",text);
		try {
			comment.save();
			return true;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public int countPhotoLikes(String id) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Like");
		query.whereEqualTo("idPhoto",id);
		try {
			int i = query.count();
			//Log.v("prototypev1", "end likes ");
			return i;
		} catch (ParseException e) {
			e.printStackTrace();
			//Log.v("prototypev1", "error likes "+e);
			return -1;
		}
	}
	
	public int countPhotoComments(String id) {
		//Log.v("prototypev1", "comments "+id);
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Comment");
		query.whereEqualTo("idPhoto",id);
		try {
			//Log.v("prototypev1", "end comments "+id);
			return query.count();
		} catch (ParseException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public ArrayList<Comment> getCommentsFromPhoto(String idPhoto) {
		ArrayList<Comment> comments = new ArrayList<Comment>();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Comment");
		query.whereEqualTo("idPhoto",idPhoto);
		try {
			List<ParseObject> c = query.find();
			for(ParseObject o : c) {
				String idUser = o.getString("idUser");
				//Download user
				User user = getUser(idUser);
				String comment = o.getString("comment");
				String date = o.getUpdatedAt().toString();
				comments.add(new Comment(user, comment, date));
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return comments;
	}
	
	public ArrayList<String> getLikes(String idPhoto) {
		ArrayList<String> likes = new ArrayList<String>();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Like");
		query.whereEqualTo("idPhoto",idPhoto);
		try {
			List<ParseObject> obs = query.find();
			for(ParseObject o : obs) {
				likes.add(o.getString("idUser"));
			}
			return likes;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean currentUserLikesCurrentPhoto(String id) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Like");
		query.whereEqualTo("idPhoto",id);
		query.whereEqualTo("idUser",ParseUser.getCurrentUser().getObjectId());
		try {
			int c = query.count();
			if(c > 0)
				return true;
			return false;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/*public ArrayList<String> getAlbumsIdForCurrentUser() {
		Array
	}*/
	
	public CurrentUser getCurrentUser() {
		ParseUser parseUser = ParseUser.getCurrentUser();
		ArrayList<String> likes = getPhotosLikedCurrentUser();
		//ArrayList<Albums> albums = get;
		return new CurrentUser(parseUser.getObjectId(),parseUser.getUsername(),parseUser.getString("profilePictureUrl"), likes,null);
	}
}
// Canviar lu de friends i albums memebers!!!!! Enlloc de tenir una arrayList fer una classe per cada un al parse i baxar-los (Friends (idUser1 idUser2...) Members (idUser - idAlbum))
