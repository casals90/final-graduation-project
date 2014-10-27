package com.example.prototypetfgv2.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Photo implements Parcelable {
	private String id;
    private String title;
    private String photo;
    private String createdAt;
    private ArrayList<Comment> comments;
    private ArrayList<String> likes;
    
	public Photo(String id, String title, String photo, String createdAt,JSONArray comments, JSONArray likes) {
		super();
		this.id = id;
		this.title = title;
		this.photo = photo;
		this.createdAt = createdAt;
		if(comments != null)
			this.comments = jsonArrayToArrayListComment(comments);
		else
			this.comments = null;
		if(likes != null)
			this.likes = jsonArrayToArrayListLike(likes);
		else
			this.likes = null;
	}
	
	public Photo(Parcel in) {
		readParcel(in);
	}
	
	public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
		public Photo createFromParcel(Parcel in) {
		    return new Photo(in);
		}
		
		public Photo[] newArray(int size) {
		    return new Photo[size];
		}
	};

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

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public ArrayList<Comment> getComments() {
		return comments;
	}

	public void setComments(ArrayList<Comment> comments) {
		this.comments = comments;
	}

	public ArrayList<String> getLikes() {
		return likes;
	}

	public void setLikes(ArrayList<String> likes) {
		this.likes = likes;
	}
	
	public ArrayList<Comment> jsonArrayToArrayListComment(JSONArray comments) {
		ArrayList<Comment> c = new ArrayList<Comment>();
		for(int i = 0; i < comments.length(); i++) {
			try {
				JSONObject comment = comments.getJSONObject(i);
				c.add(new Comment(comment.getString("idUser"), comment.getString("comment")));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		return c;
	}
	
	public ArrayList<String>  jsonArrayToArrayListLike(JSONArray likes) {
		ArrayList<String> l = new ArrayList<String>();
		for(int i = 0; i < likes.length(); i++) {
			try {
				l.add(likes.getString(i));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		return l;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(id);
		dest.writeString(title);
		dest.writeString(photo);
		dest.writeString(createdAt);
		dest.writeTypedList(comments);
		dest.writeSerializable(likes);
	}
	
	private void readParcel(Parcel in) {
		// TODO Auto-generated method stub
		id = in.readString();
		title = in.readString();
		photo = in.readString();
		createdAt = in.readString();
		in.readTypedList(comments, Comment.CREATOR);
		likes = (ArrayList<String>) in.readSerializable();
	}
}
