package com.punkete1990.helper.databasemanager.Database;

import android.content.Context;
import android.database.Cursor;

import com.punkete1990.helper.databasemanager.Model.Client;

public class ClientDBHelper extends DBHelperBase<Client, Integer> {

	private static final int VERSION = 2;
	
	public ClientDBHelper(Context context, String user)  {
		super(context, user,VERSION,Client.class);
	}

	public Client getObjectFromCursor(Cursor cursor) {
		Client object = new Client();
		return getObjectFromCursor(cursor, object);
	}
	
}
