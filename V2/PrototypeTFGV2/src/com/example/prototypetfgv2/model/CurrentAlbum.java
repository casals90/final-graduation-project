package com.example.prototypetfgv2.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class CurrentAlbum implements Parcelable {
	
	private String id;
	private String title;
	private String coverPhoto;
	
	public CurrentAlbum(String id, String title, String coverPhoto) {
		super();
		this.id = id;
		this.title = title;
		this.coverPhoto = coverPhoto;
	}
	
	public CurrentAlbum(Parcel in) {
		this.id = in.readString();
		this.title = in.readString();
	}
	
	public static final Parcelable.Creator<CurrentAlbum> CREATOR = new Parcelable.Creator<CurrentAlbum>() {
		public CurrentAlbum createFromParcel(Parcel in) {
		    return new CurrentAlbum(in);
		}
		
		public CurrentAlbum[] newArray(int size) {
		    return new CurrentAlbum[size];
		}
	};

	public CurrentAlbum(JSONObject currentAlbum) {
		try {
			this.id = currentAlbum.getString("id");
			this.title = currentAlbum.getString("title");
		} catch (JSONException e) {
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
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return currentAlbum;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.title);
	}
}
