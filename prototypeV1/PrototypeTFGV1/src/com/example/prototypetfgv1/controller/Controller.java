package com.example.prototypetfgv1.controller;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.prototypetfgv1.model.User;
import com.parse.ParseUser;

public class Controller {
	
	private ParseFunctions parseFunctions;
	private ApplicationClass appClass;
	
	public Controller(Context context) {
		super();
		this.parseFunctions = new ParseFunctions(context);
		
		appClass = (ApplicationClass) context.getApplicationContext();
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
		return appClass.getUser();
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
	
	public void logout() {
		parseFunctions.logout();
		appClass.deleteUser();
		Log.v("prototypev1", "users logout "+ParseUser.getCurrentUser()+" local "+appClass.getUser());
	}
}
