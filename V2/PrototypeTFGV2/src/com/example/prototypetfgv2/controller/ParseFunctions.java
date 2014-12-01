package com.example.prototypetfgv2.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

import com.example.prototypetfgv2.model.Album;
import com.example.prototypetfgv2.model.Comment;
import com.example.prototypetfgv2.model.CurrentAlbum;
import com.example.prototypetfgv2.model.CurrentUser;
import com.example.prototypetfgv2.model.Photo;
import com.example.prototypetfgv2.model.User;
import com.example.prototypetfgv2.utils.Utils;
import com.example.prototypetfgv2.view.InputUsernameActivity;
import com.example.prototypetfgv2.view.MainActivity;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;

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
		parseUser.put("photosNumber",0);
		parseUser.put("friendsNumber",0);
		parseUser.put("albumsNumber",0);
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
    
    public ArrayList<Album> downloadCommonAlbums(CurrentUser currentUser,String idFriend) {
    	ArrayList<String> albumsCurrentUser = new ArrayList<String>();
		ArrayList<String> albumsToDownload = new ArrayList<String>();
		ArrayList<Album> commonAlbums = new ArrayList<Album>();
		//Download idAlbums from current user
		ParseQuery<ParseObject> query = ParseQuery.getQuery("AlbumMember");
		query.whereEqualTo("idUser",currentUser.getId());
		query.orderByDescending("createdAt");
		try {
			List<ParseObject> parseAlbumsMembers = query.find();
			for(ParseObject o:parseAlbumsMembers) {
				albumsCurrentUser.add(o.getString("idAlbum"));
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		//Download idAlbums from friend user
		ParseQuery<ParseObject> queryFriend = ParseQuery.getQuery("AlbumMember");
		queryFriend.whereEqualTo("idUser",idFriend);
		queryFriend.orderByDescending("createdAt");
		try {
			List<ParseObject> parseAlbumsMembers = queryFriend.find();
			for(ParseObject o:parseAlbumsMembers) {
				String idAlbum = o.getString("idAlbum");
				if(albumsCurrentUser.contains(idAlbum))
					albumsToDownload.add(idAlbum);
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		//Download album list
		commonAlbums = downloadAlbums(albumsToDownload, currentUser);
    	return commonAlbums;
    }
    
    public ArrayList<String> getPhotosFromAlbumLikedCurrentUser(String idUser,String idAlbum) {
    	ArrayList<String> likes = new ArrayList<String>();
    	
    	ParseQuery<ParseObject> query = ParseQuery.getQuery("Like");
    	query.whereEqualTo("idUser",idUser);
    	query.whereEqualTo("ownerAlbum",idAlbum);
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
    
    public void uploadPhoto(Bitmap photo,String title,final Activity activity,String idAlbum) {
    	// Create the ParseFile
        ParseFile file = new ParseFile("photo.jpeg",Utils.bitmapToByteArray(photo));
        // Upload the image into Parse Cloud
        file.saveInBackground();
        final ParseObject photoUpload = new ParseObject("Photo");
        photoUpload.put("title",title);
        photoUpload.put("likesNumber",0);
        photoUpload.put("commentsNumber",0);
        //Save photo file in other ParseObject
        final ParseObject uploadParseFile = new ParseObject("PhotoFile");
        uploadParseFile.put("photo",file);
        try {
			uploadParseFile.save();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
        //Save the owner of the photo
        photoUpload.put("ownerUser",ParseUser.getCurrentUser().getObjectId());
		photoUpload.put("ownerAlbum",idAlbum);
		photoUpload.put("idPhotoFile",uploadParseFile.getObjectId());
		photoUpload.put("photoFileUrl",file.getUrl());
			
		try {
			photoUpload.save();
			//Increment number of photos
			incrementPhotosNumberInAlbum(idAlbum);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    }
    
    public void incrementPhotosNumberInAlbum(String idAlbum) {
    	ParseQuery<ParseObject> query = ParseQuery.getQuery("Album");
    	query.whereEqualTo("objectId",idAlbum);
    	try {
			ParseObject album = query.getFirst();
			album.increment("photosNumber");
			album.save();
		} catch (ParseException e) {
			e.printStackTrace();
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
		ParseQuery<ParseObject> query = ParseQuery.getQuery("PhotoFile");
		query.whereEqualTo("objectId",idPhotoFile);
		try {
			List<ParseObject> obs = query.find();
			ParseObject file = obs.get(0);
			ParseFile f = file.getParseFile("photo");
			String url = f.getUrl();
			return url;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<Photo> downloadPhotosFromAlbum(String idAlbum) {
		ArrayList<Photo> photos = new ArrayList<Photo>();
		HashMap<String, User> ownerUsers = new HashMap<String, User>();
		User ownerUser;
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
		query.whereEqualTo("ownerAlbum",idAlbum);
		query.orderByDescending("createdAt");
		try {
			List<ParseObject> obs = query.find();
			for(ParseObject o : obs) {
				String idUser = o.getString("ownerUser");
				//Search likes and comments number
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
		User user;
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("objectId",id);
		try {
			ParseUser u = query.getFirst();
			String url = u.getString("profilePictureUrl");
			if(url == null)
				user = new User(u.getObjectId(),u.getUsername(),null,u.getInt("friendsNumber"),u.getInt("photosNumber"),u.getInt("AlbumsNumber"));
			else
				user = new User(u.getObjectId(),u.getUsername(),url,u.getInt("friendsNumber"),u.getInt("photosNumber"),u.getInt("AlbumsNumber"));
			return user;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public int getAlbumsNumber(String idUser) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("AlbumMember");
		query.whereEqualTo("idUser",idUser);
		try {
			return query.count();
		} catch (ParseException e) {
			return -1;
		}
	}
	
	public ArrayList<Album> getAlbums(CurrentUser currentUser) {
		ArrayList<Album> albums = new ArrayList<Album>();
		ArrayList<String> albumsId = new ArrayList<String>();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("AlbumMember");
		query.whereEqualTo("idUser",currentUser.getId());
		query.orderByDescending("createdAt");
			
		try {
			List<ParseObject> listAlbumMember = query.find();
			if(listAlbumMember.size() == 0) {
				return null;
			}
			for(ParseObject albumMember: listAlbumMember) {
				albumsId.add(albumMember.getString("idAlbum"));
			}
			albums = downloadAlbums(albumsId, currentUser);
			return albums;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}	
	}
	
	public ArrayList<Album> downloadAlbums(ArrayList<String> idAlbums,CurrentUser currentUser) {
		ArrayList<String> albumsAdmin = new ArrayList<String>();
		ArrayList<Album> albums = new ArrayList<Album>();
		
		for(int i = 0; i < idAlbums.size(); i++) {
			//Log.v("prototypev1", "downlaod album "+i);
			ParseQuery<ParseObject> query = ParseQuery.getQuery(ALBUM);
			query.whereEqualTo("objectId",idAlbums.get(i));
			try {
				ParseObject a = query.getFirst();
				if(a.getString("idAdmin").compareTo(currentUser.getId())== 0)
					albumsAdmin.add(a.getObjectId());
				int photoNumber = a.getInt("photosNumber");
				//download members
				ArrayList<String> members = getMembers(a.getObjectId());
				if(photoNumber > 0) {
					//ArrayList<String> idPhotos = getPhotosFromAlbum(a.getObjectId());
					//int random = Utils.getRandomInt(idPhotos.size());
					//Download Random photo
					//Photo photo = downloadPhoto(idPhotos.get(random));
					String photoURL = getLastPhotoFromAlbum(a.getObjectId());
					//albums.add(new Album(a.getObjectId(),photo.getPhoto(),a.getString("albumTitle"),members,a.getInt("photosNumber"),a.getInt("membersNumber")));
					//getPhotoMoreLikesInAlbum(a.getObjectId());
					albums.add(new Album(a.getObjectId(),photoURL,a.getString("albumTitle"),members,a.getInt("photosNumber"),a.getInt("membersNumber")));
				}
				else 
					albums.add(new Album(a.getObjectId(),null,a.getString("albumTitle"),members,a.getInt("photosNumber"),a.getInt("membersNumber")));
			} catch (ParseException e) {
				e.printStackTrace();
				Log.v("prototypev1", "error downlaod album "+e);
				return null;
			}
			Log.v("prototypev1", "end downlaod album "+i);
		}
		return albums;
	}
	
	public String getLastPhotoFromAlbum(String idAlbum) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
		query.whereEqualTo("ownerAlbum",idAlbum);
		query.orderByDescending("createdAt");
		try {
			ParseObject photo = query.getFirst();
			return photo.getString("photoFileUrl");
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<String> getMembers(String idAlbum) {
		ArrayList<String> members = new ArrayList<String>();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("AlbumMembers");
		query.whereEqualTo("idAlbum",idAlbum);
		try {
			List<ParseObject> obs = query.find();
			for(ParseObject o : obs) {
				members.add(o.getString("idUser"));
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return members;
	}
	
	public String getPhotoUrl(String id) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
		query.whereEqualTo("objectId",id);
		try {
			ParseObject photo = query.getFirst();
			return photo.getString("photoFileUrl");
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Photo downloadPhoto(String idPhoto) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
		query.whereEqualTo("objectId",idPhoto);
		try {
			ParseObject p = query.getFirst();
			String url = p.getString("photoFileUrl");
			Photo photo =  new Photo(p.getObjectId(),p.getString("title"),url,String.valueOf(p.getCreatedAt()));
			return photo;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}	
	}
	
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
	
	public ArrayList<User> downloadFriends(CurrentUser currentUser) {
		ArrayList<User> users = new ArrayList<User>();
		ArrayList<String> friends = getFriends(currentUser);
		
		for(int i = 0; i < friends.size(); i++) {
			ParseQuery<ParseUser> query = ParseUser.getQuery();
			query.whereEqualTo("objectId",friends.get(i));
			query.orderByAscending("username");
			try {
				ParseUser user = query.getFirst();
				String urlProfilePicture = user.getString("profilePictureUrl");
				if(urlProfilePicture == null)
					users.add(new User(user.getObjectId(),user.getUsername(), null,user.getInt("friendsNumber"),user.getInt("photosNumber"),user.getInt("AlbumsNumber")));
				else
					users.add(new User(user.getObjectId(),user.getUsername(),urlProfilePicture,user.getInt("friendsNumber"),user.getInt("photosNumber"),user.getInt("AlbumsNumber")));
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
		return users;
	}
	
	public ArrayList<Photo> downloadMyPhotos(String idUser) {
		ArrayList<Photo> photos = new ArrayList<Photo>();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
		query.whereEqualTo("ownerUser",idUser);
		query.orderByDescending("createdAt");
		try {
			List<ParseObject> parsePhotos = query.find();
			for(ParseObject p : parsePhotos) {
				photos.add(new Photo(p.getObjectId(),p.getString("title"),p.getString("photoFileUrl"),p.getCreatedAt().toString(),null,p.getInt("likesNumber"),p.getInt("commentsNumber"),p.getString("ownerAlbum")));
			}
			return photos;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<User> downloadFriendsInputSearch(String input,CurrentUser currentUser) {
		ArrayList<User> users = new ArrayList<User>();
		ArrayList<String> friends = getFriends(currentUser);
		
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereStartsWith("username",input);
		query.whereContainedIn("objectId",friends);
		query.orderByAscending("username");
		try {
			List<ParseUser> parseUsers = query.find();
			for(ParseUser u : parseUsers) {
				String profilePictureUrl = u.getString("profilePictureUrl");
				users.add(new User(u.getObjectId(),u.getUsername(),profilePictureUrl,u.getInt("friendsNumber"),u.getInt("photosNumber"),u.getInt("AlbumsNumber")));
			}
			return users;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	 public ArrayList<User> getUsers(String username,CurrentUser currentUser) {
		ArrayList<User> users = new ArrayList<User>();
		List<ParseUser> parseUsers;
		
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereStartsWith("username",username);
		query.whereNotEqualTo("objectId",currentUser.getId());
		query.orderByDescending("username");
		try {
			parseUsers = query.find();
			for(ParseUser u : parseUsers) {
				String profilePictureUrl = u.getString("profilePictureUrl");
				users.add(new User(u.getObjectId(),u.getUsername(),profilePictureUrl,u.getInt("friendsNumber"),u.getInt("photosNumber"),u.getInt("AlbumsNumber")));
			}
			return users;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	} 
	 
	public ArrayList<String> getFriends(CurrentUser currentUser) {
		ArrayList<String> friends = new ArrayList<String>();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Friendship");
		query.whereEqualTo("idUser",currentUser.getId());
		try {
			List<ParseObject> friendship = query.find();
			for(ParseObject f : friendship) {
				friends.add(f.getString("idFriend"));
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return friends;
	}
	
	public JSONArray getFriendsRequest() {
		JSONArray r = ParseUser.getCurrentUser().getJSONArray("friendsRequest");
		return r;
	}
	
	public boolean addFriend(String idNewFriend,CurrentUser currentUser) {
		ParseObject friendship = new ParseObject("Friendship");
		friendship.put("idUser",currentUser.getId());
		friendship.put("idFriend",idNewFriend);
		try {
			friendship.save();
			incrementFriendsNumber();
			return true;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void incrementFriendsNumber() {
		ParseUser currentUser = ParseUser.getCurrentUser();
		currentUser.increment("friendsNumber");
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
	
	public boolean deleteFriend(String id,CurrentUser currentUser) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Friendship");
		query.whereEqualTo("idUser",currentUser.getId());
		query.whereEqualTo("idFriend",id);
		try {
			ParseObject friendship = query.getFirst();
			friendship.delete();
			return decrementFriendsNumber();
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean decrementFriendsNumber() {
		ParseUser currentUser = ParseUser.getCurrentUser();
		int n = currentUser.getInt("friendsNumber");
		if(n > 0)
			n--;
		else
			n = 0;
		currentUser.put("friendsNumber",n);
		try {
			currentUser.save();
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
					user.put("photosNumber",0);
					user.put("friendsNumber",0);
					user.put("albumsNumber",0);
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
	
	public boolean addMembersInAlbum(ArrayList<String> members,String idAlbum) {
		
		for(int i = 0; i < members.size(); i++) {
			ParseObject albumMember = new ParseObject("AlbumMember");
			albumMember.put("idAlbum",idAlbum);
			albumMember.put("idUser",members.get(i));
			try {
				albumMember.save();
			} catch (ParseException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	public boolean newAlbum(ArrayList<String> members,String albumName,CurrentUser currentUser) {
		ParseObject newAlbum = new ParseObject(ALBUM);
		//Admin is current user
		newAlbum.put("idAdmin",currentUser.getId());
		//Put current user in members
		members.add(currentUser.getId());
		newAlbum.put("albumTitle",albumName);
		newAlbum.put("membersNumber",members.size());
		newAlbum.put("photosNumber",0);
		//Increment number of albums
		ParseUser user = ParseUser.getCurrentUser();
		user.increment("albumsNumber");
		user.saveInBackground();
		try {
			newAlbum.save();
			return addMembersInAlbum(members,newAlbum.getObjectId());
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
					downloads.add(new User(u.getObjectId(),u.getUsername(), null,u.getInt("friendsNumber"),u.getInt("photosNumber"),u.getInt("AlbumsNumber")));
				else
					downloads.add(new User(u.getObjectId(),u.getUsername(),profilePicture.getUrl(),u.getInt("friendsNumber"),u.getInt("photosNumber"),u.getInt("AlbumsNumber")));
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
		return downloads;
	}
	
	public CurrentAlbum getCurrentAlbum() {
		JSONObject currentAlbum = ParseUser.getCurrentUser().getJSONObject("currentAlbum");
		CurrentAlbum album;
		if(currentAlbum != null) {
			album = new CurrentAlbum(currentAlbum);
			//Album photo
			ArrayList<String> photos = getPhotosFromAlbum(album.getId());
			Photo photo = downloadPhoto(photos.get(0));
			album.setCoverPhoto(photo.getPhoto());
			return album;
		}
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
	
	public boolean likePhoto(String idPhoto,String idAlbum) {
		ParseObject like = new ParseObject("Like");
		like.put("idUser",ParseUser.getCurrentUser().getObjectId());
		like.put("idPhoto",idPhoto);
		like.put("ownerAlbum",idAlbum);
		try {
			like.save();
			incrementLikesNumber(idPhoto);
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
			incrementCommentsNumber(idPhoto);
			return true;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean incrementCommentsNumber(String id) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
		query.whereEqualTo("objectId",id);
		
			ParseObject photo;
			try {
				photo = query.getFirst();
				photo.increment("commentsNumber");
				photo.save();
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
			return i;
		} catch (ParseException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public int countPhotoComments(String id) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Comment");
		query.whereEqualTo("idPhoto",id);
		try {
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
	
	public ArrayList<String> getLikesFromMyPhotos(String idUser) {
		ArrayList<String> likes = new ArrayList<String>();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Like");
		query.whereEqualTo("idUser",idUser);
		try {
			List<ParseObject> parseLikes = query.find();
			for(ParseObject l:parseLikes) {
				likes.add(l.getString("idPhoto"));
			}
			return likes;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public CurrentUser getCurrentUser() {
		ParseUser parseUser = ParseUser.getCurrentUser();
		JSONObject currentAlbum = parseUser.getJSONObject("currentAlbum");
		if(currentAlbum != null) {
			String idAlbum;
			try {
				idAlbum = currentAlbum.getString("id");
				int albumsNumber = getAlbumsNumber(parseUser.getObjectId());
				return new CurrentUser(parseUser.getObjectId(),parseUser.getUsername(),parseUser.getString("profilePictureUrl"),idAlbum,parseUser.getInt("friendsNumber"),parseUser.getInt("photosNumber"),albumsNumber);
			} catch (JSONException e) {
				e.printStackTrace();
				Log.v("prototypev1", "error getCurrentUser"+e);
				return null;
			}
		}
		else {
			int albumsNumber = getAlbumsNumber(parseUser.getObjectId());
			return new CurrentUser(parseUser.getObjectId(),parseUser.getUsername(),parseUser.getString("profilePictureUrl"),null,parseUser.getInt("friendsNumber"),parseUser.getInt("photosNumber"),albumsNumber);
		}
	}
}