package com.tchip.filemanager;

import android.app.Application;

public class MyApplication extends Application {

	@Override
	public void onCreate() {

	}

	// File Manager
	AppPreferences appPreferences = null;
	FavouritesManager favouritesManager = null;
	FileIconResolver fileIconResolver = null;

	public AppPreferences getAppPreferences() {
		if (appPreferences == null)
			appPreferences = AppPreferences
					.loadPreferences(getApplicationContext());

		return appPreferences;
	}

	public FavouritesManager getFavouritesManager() {
		if (favouritesManager == null)
			favouritesManager = new FavouritesManager(getApplicationContext());
		return favouritesManager;
	}

	public FileIconResolver getFileIconResolver() {
		if (fileIconResolver == null)
			fileIconResolver = new FileIconResolver(getApplicationContext());
		return fileIconResolver;
	}

}
