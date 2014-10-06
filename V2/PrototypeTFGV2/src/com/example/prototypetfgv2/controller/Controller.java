package com.example.prototypetfgv2.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.example.prototypetfgv2.model.Photo;
import com.example.prototypetfgv2.model.User;
import com.example.prototypetfgv2.view.ImageLoader;
import com.parse.ParseUser;

public class Controller {
	
	private ParseFunctions parseFunctions;
	private Context context;
	//private ApplicationClass appClass;
	
	public Controller(Context context) {
		super();
		this.parseFunctions = new ParseFunctions(context);
		this.context = context;
		//appClass = (ApplicationClass) context.getApplicationContext();
	}

	public ParseFunctions getParseFunctions() {
		return parseFunctions;
	}

	public boolean signUp(String username,String password) {
		ParseUser parseUser = parseFunctions.signUpInParse(username, password);
		if(parseUser != null) {
			//if true I create a local user
			//comprovar si l'usuari vol guardar la contrassenya i guardar-la a sharedPReferences!!
			//appClass.newUser(parseUser);
			return true;
		}
		return false;
	}
	
	public boolean logIn(String username, String password) {
		if(parseFunctions.logInParse(username, password)) {
			//appClass.downloadUser(ParseUser.getCurrentUser());
			return true;
		}
		return false;
	}
	
	public void updatePhoto(Bitmap photo,Activity activity) {
		parseFunctions.updatePhoto(photo,activity);
	}
	
	public void logout() {
		parseFunctions.logout();
		//appClass.deleteUser();
		//Log.v("prototypev1", "users logout "+ParseUser.getCurrentUser()+" local "+appClass.getUser());
	}
	
	public ArrayList<Photo> downloadPhotos() {
		return parseFunctions.downloadPhotos();
	}
	
	public void deletePhoto(String id) {
		parseFunctions.deletePhoto(id);
	}
	
	public ArrayList<User> getUsers(String username) {
		return parseFunctions.getUsers(username);
	}
	
	public JSONArray getFriends() {
		return parseFunctions.getFriends();
	}
	
	public JSONArray getFriendsRequest() {
		return parseFunctions.getFriendsRequest();
	}
	
	public boolean addFriend(String id) {
		return parseFunctions.addFriend(id);
	}
	
	public String getUsername() {
		return parseFunctions.getUsername();
	}
	
	public Bitmap getProfilePicture() {
		return parseFunctions.getProfilePicture();
	}
	
	public boolean setProfilePicture(Bitmap b) {
		return parseFunctions.setProfilePicture(b);
	}
	
	public boolean removeProfilePicture() {
		return parseFunctions.removeProfilePicture();
	}
	
	public boolean isMyFriend(String id) {
		return parseFunctions.isMyFriend(id);
	}
	
	public boolean deleteFriend(String id) {
		return parseFunctions.deleteFriend(id);
	}
	
	public String getPhotosNumber() {
		return parseFunctions.getPhotosNumber();
	}
	
	public String getFriendsNumber() {
		return parseFunctions.getFriendsNumber();
	}
	
	public boolean logInTwitter(Activity activity) {
		parseFunctions.logInTwitter(activity);
		if(parseFunctions.isParseUserExist())
			return true;
		return false;
	}
	
	public Bitmap downloadBitmap(String url) {
		ImageLoader imgLoader = new ImageLoader(context);
		return imgLoader.getBitmapFromURL(url);
	}
	
	public Bitmap getProfilePictureTwitterURL() {
		String url = parseFunctions.getProfilePictureTwitterURL();
		Log.v("prototypev1", "url "+url);
		//put the image in profile picture parse
		Bitmap b = Bitmap.createScaledBitmap(getBitmapFromURL(url),80,80, true);
		Log.v("prototypev1", "bytecount "+b.getByteCount());
		if(url == null || b == null)
			return null;
		parseFunctions.setProfilePicture(b);
		return b;
	}
	
	public Bitmap getBitmapFromURL(String imageUrl) {
		try {
			URL url = new URL(imageUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
