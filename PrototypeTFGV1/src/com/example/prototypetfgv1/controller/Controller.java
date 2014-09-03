package com.example.prototypetfgv1.controller;

import android.app.Application;

import com.parse.Parse;
 
public class Controller extends Application {
 
    @Override
    public void onCreate() {
        super.onCreate();
 
        Parse.initialize(this, "Pz2ope2OFVDLDypgpdFMpeZiXhnPjm62tDv40b35", "ISRt37kcr6frHkhzvJ3Y9cxhvZxyocO7bP795y4c");
         
       /* ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
 
        // If you would like all objects to be private by default, remove this line.
        defaultACL.setPublicReadAccess(true);
 
        ParseACL.setDefaultACL(defaultACL, true);*/
    }
 
}
