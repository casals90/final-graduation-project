package com.example.testnews;

public class New {
	
	private int idPictureUser;
	private String username;
	private int idPhoto;
	private String description;
	private int idIconLike;
	private int idIconComment;
	
	
	public New() {
		super();
		// TODO Auto-generated constructor stub
	}


	/*public New(int idPictureUser, String username, int idPhoto,
			String description, int idIconLike, int idIconComment) {
		super();
		this.idPictureUser = idPictureUser;
		this.username = username;
		this.idPhoto = idPhoto;
		this.description = description;
		this.idIconLike = idIconLike;
		this.idIconComment = idIconComment;
	}
	
	public New(String username, String description) {
		super();
		this.username = username;
		this.description = description;
	}*/
	
	public New(String username, int idPhoto, String description) {
		super();
		this.username = username;
		this.idPhoto = idPhoto;
		this.description = description;
	}


	public int getIdPictureUser() {
		return idPictureUser;
	}


	public void setIdPictureUser(int idPictureUser) {
		this.idPictureUser = idPictureUser;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public int getIdPhoto() {
		return idPhoto;
	}


	public void setIdPhoto(int idPhoto) {
		this.idPhoto = idPhoto;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public int getIdIconLike() {
		return idIconLike;
	}


	public void setIdIconLike(int idIconLike) {
		this.idIconLike = idIconLike;
	}


	public int getIdIconComment() {
		return idIconComment;
	}


	public void setIdIconComment(int idIconComment) {
		this.idIconComment = idIconComment;
	}

}
