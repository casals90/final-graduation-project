package com.example.prototypetfgv1.model;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import com.example.prototypetfgv1.view.Utils;

public class User {
	
	private String id;
	private String userName;
	private String password;
	private JSONArray photos;
	private String updatedAt;
	
	// This constructor is only in the sign up 
	public User(String id, String userName, String password, String updatedAt) {
		super();
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.updatedAt = updatedAt;
		photos = new JSONArray();
	}
	
	// This constructor is only in the log in 
	public User(String id, String userName, String password, JSONArray photos,String updatedAt) {
		super();
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.photos = photos;
		this.updatedAt = updatedAt;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public JSONArray getPhotos() {
		return photos;
	}

	public void setPhotos(JSONArray photos) {
		this.photos = photos;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	public void addPhoto(String id) {
		photos.put(id);
	}
	
	public void deletePhoto(String id)  {
		photos = Utils.removeElementToJsonArray(photos, id);
		Log.v("prototypev1", "pos a borrar= "+" size photos "+photos.length());
	}
	
	public String getPhoto(int index) {
		try {
			return photos.getString(index);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
} 
