package com.punkete1990.helper.databasemanager;

import com.punkete1990.helper.databasemanager.Database.ClientDBHelper;
import com.punkete1990.helper.databasemanager.Database.DataBaseManager;

import android.app.Application;
import android.content.SharedPreferences;

public class DataBaseManagerApplication extends Application {
	private final String PREF_NAME_USER_ID = "user_key";
	private final String PREF_NAME = "PrefDataBaseManager";
	
	
	private DataBaseManager mDbManager;
	private Integer mUserId=null;
	
	public DataBaseManager getDbManager() 
	{
		if(mDbManager==null)
			mDbManager = DataBaseManager.getInstance(this);
		return mDbManager;
	}
	public ClientDBHelper getCurrentClientDataBase()
	{
		if(getUserId() !=null)
			return getDbManager().OpenOrCreateClientInstance(String.valueOf(getUserId()));
		return null;
	}
	
	public Integer getUserId() {
		if (this.mUserId == null){
			SharedPreferences prefs =  getApplicationContext().getSharedPreferences(PREF_NAME, 0);
			this.mUserId = prefs.getInt(PREF_NAME_USER_ID, 0);
		}
		return this.mUserId;
	}
	public void setUserId(int userId) {
		SharedPreferences prefs =  getApplicationContext().getSharedPreferences(PREF_NAME, 0);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(PREF_NAME_USER_ID, userId);
		editor.commit();
		
		this.mUserId = userId;
	}
}
