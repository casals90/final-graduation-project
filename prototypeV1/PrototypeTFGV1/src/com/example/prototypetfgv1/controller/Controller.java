package com.example.prototypetfgv1.controller;

import com.example.prototypetfgv1.model.User;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

public class Controller {
	
	private ParseFunctions parseFunctions;
	private ApplicationClass appClass;
	private User user;
	
	public Controller(Context context) {
		super();
		this.parseFunctions = new ParseFunctions(context);
		
		appClass = (ApplicationClass) context.getApplicationContext();
		user = appClass.getUser();
	}

	public ParseFunctions getParseFunctions() {
		return parseFunctions;
	}

	public ApplicationClass getAppClass() {
		return appClass;
	}

	public void setAppClass(ApplicationClass appClass) {
		this.appClass = appClass;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean signUp(String username,String password) {
		ParseUser parseUser = parseFunctions.signUpInParse(username, password);
		if(parseUser != null) {
			//if true I create a local user
			appClass.newUser(parseUser,password);
			return true;
		}
		return false;
	}
	
	public boolean logIn(String username, String password) {
		if(parseFunctions.logInParse(username, password)) {
			appClass.downloadUser(ParseUser.getCurrentUser());
			return true;
		}
		return false;
	}
	
	public void updatePhoto(Bitmap photo,Activity activity) {
		parseFunctions.updatePhoto(photo,activity);
	}
	
}
