package com.punkete1990.helper.databasemanager.Database;

import com.punkete1990.helper.databasemanager.DataBaseManagerApplication;

public class DataBaseManager 
{
	private static DataBaseManager sDBManagerInstance;
	private DataBaseManagerApplication mContext;
	
	private ClientDBHelper mClientSystem;
	
	public static DataBaseManager getInstance(DataBaseManagerApplication context)
	{
		if(sDBManagerInstance == null)
			sDBManagerInstance = new DataBaseManager(context);
		return sDBManagerInstance;
	}

	private DataBaseManager(DataBaseManagerApplication context) 
	{
		this.mContext = context;
	}

	public ClientDBHelper OpenOrCreateClientInstance(String user)
	{
		if(mClientSystem == null || (mClientSystem!=null && !mClientSystem.getUser().equals(user)))
		{
			mClientSystem = new ClientDBHelper(mContext,user);
		}
		return mClientSystem;
	}
}
