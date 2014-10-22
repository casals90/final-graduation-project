package com.example.prototypetfgv2.model;

import org.json.JSONException;
import org.json.JSONObject;

public class CurrentAlbum {
	
	private String id;
	private String title;
	private String coverPhoto;
	
	public CurrentAlbum(String id, String title,String coverPhoto) {
		super();
		this.id = id;
		this.title = title;
		this.coverPhoto = coverPhoto;
	}
	
	public CurrentAlbum(JSONObject currentAlbum) {
		try {
			this.id = currentAlbum.getString("id");
			this.title = currentAlbum.getString("title");
			this.coverPhoto = currentAlbum.getString("coverPhoto");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCoverPhoto() {
		return coverPhoto;
	}

	public void setCoverPhoto(String coverPhoto) {
		this.coverPhoto = coverPhoto;
	}

	public JSONObject getCurrentAlbum() {
		JSONObject currentAlbum = new JSONObject();
		try {
			currentAlbum.put("id",id);
			currentAlbum.put("title",title);
			currentAlbum.put("coverPhoto",coverPhoto);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return currentAlbum;
	}
}
