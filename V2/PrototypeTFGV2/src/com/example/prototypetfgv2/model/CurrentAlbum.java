package com.example.prototypetfgv2.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
/**
 * Class that define current album of application
 * @author jordi
 *
 */
public class CurrentAlbum implements Parcelable {
	
	private String id;
	private String title;
	private String coverPhoto;
	/**
	 * Constructor of current album
	 * @param id id of current album
	 * @param title title of current album
	 * @param coverPhoto url of current album cover
	 */
	public CurrentAlbum(String id, String title, String coverPhoto) {
		super();
		this.id = id;
		this.title = title;
		this.coverPhoto = coverPhoto;
	}
	/**
	 * Parcel constructor
	 * @param in parcel data
	 */
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
	/**
	 * Method that split atributes of JSONObject that downlaod of database
	 * @param currentAlbum
	 */
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
	/**
	 * Method that join atributes of JSONObject to save to database
	 * @return
	 */
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
	/*
	 * (non-Javadoc)
	 * @see android.os.Parcelable#describeContents()
	 */
	@Override
	public int describeContents() {
		return 0;
	}
	/*
	 * Method that read Parcel data
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.title);
	}
}
