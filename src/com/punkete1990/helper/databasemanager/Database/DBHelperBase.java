package com.punkete1990.helper.databasemanager.Database;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public abstract class DBHelperBase<E,A> extends SQLiteOpenHelper 
{
	protected SQLiteDatabase mDb;
	private Semaphore lock = new Semaphore(1);
	private boolean isOpen = false;
	private String mUser;
	private String mTableName;
	private String mKey;
	public String TABLE_SQL;
	private  Class<?> mClass;
	public DBHelperBase(Context context,String user,int version,Class<?> c) 
	{
		super(context, user+"_"+c.getSimpleName(), null, version);
		this.mUser = user;
		this.mClass = c;
		this.mTableName = c.getSimpleName();
		this.mKey = "_Id";
		TABLE_SQL = "CREATE TABLE " + c.getSimpleName() + "( "+ mKey +" INTEGER PRIMARY KEY AUTOINCREMENT";
			
		for (Field field : mClass.getDeclaredFields()) {
			field.setAccessible(true); // You might want to set modifier to public first.
			if (Modifier.isPrivate(field.getModifiers()) && !field.getName().equals(mKey) ) {
				if(field.getType() == String.class)
					TABLE_SQL = TABLE_SQL +  ", " + field.getName() + " TEXT";
				else if (field.getType().toString().equals("int") || field.getType().toString().equals("long") )   
					TABLE_SQL = TABLE_SQL +  ", " + field.getName() + " INTEGER";
				else if (field.getType().toString().equals("double"))
					TABLE_SQL = TABLE_SQL +  ", " + field.getName() + " REAL";
			}   
		}
		TABLE_SQL = TABLE_SQL+")";
	}
	public String getUser() {
		return mUser;
	}
	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL(TABLE_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		db.execSQL("DROP TABLE IF EXISTS " + mClass.getSimpleName());
		onCreate(db);
	}

	protected final void openDB()
	{
		try {
			lock.acquire();
			if(!isOpen)
			{
				mDb = getWritableDatabase();
				isOpen = true;
			}
			else
				Log.e("OpenDb","Database was not closed");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	protected final void closeDB()
	{
		if(isOpen)
		{
			mDb.close();
			mDb = null;
			isOpen = false;
			lock.release();
		}
		else
			Log.e("CloseDb","Database was not opened");
	}

	protected final boolean insertInTransaction(String table, ContentValues values)
	{
		boolean wasInserted = false;
		if(isOpen)
		{
			try
			{
				mDb.beginTransaction();
				wasInserted = mDb.insert(table, null, values)>0;
				mDb.setTransactionSuccessful();
			}
			catch(Exception e)
			{
				Log.e("Insert In Transaction", e.getMessage());
			}
			finally
			{
				mDb.endTransaction();
			}
		}
		else{
			Log.e("Insert In Db","Database is closed");
		}
		return wasInserted;
	}

	protected final boolean updateInTransaction(String table, ContentValues values,String where, String[] whereArgs)
	{
		boolean wasUpdated = false;
		if(isOpen)
		{
			try
			{
				mDb.beginTransaction();
				wasUpdated = mDb.update(table, values, where, whereArgs)>0;
				mDb.setTransactionSuccessful();
			}
			catch(Exception e)
			{
				Log.e("Insert In Transaction", e.getMessage());
			}
			finally
			{
				mDb.endTransaction();
			}
		}
		else{
			Log.e("Update In Db","Database is closed");
		}
		return wasUpdated;
	}

	protected final boolean deleteInTransaction(String table,String where, String[] whereArgs)
	{
		boolean wasDeleted = false;
		if(isOpen)
		{
			try
			{
				mDb.beginTransaction();
				wasDeleted = mDb.delete(table, where, whereArgs)>0;
				mDb.setTransactionSuccessful();
			}
			catch(Exception e)
			{
				Log.e("Insert In Transaction", e.getMessage());
			}
			finally
			{
				mDb.endTransaction();
			}
		}
		else{
			Log.e("Delete In Db","Database is closed");
		}
		return wasDeleted;
	}

	public ContentValues getContentValuesFor(E object) {
		ContentValues contentValues = new ContentValues();
		for (Field field : object.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			try {
				if (Modifier.isPrivate(field.getModifiers()) && !field.getName().equals(mKey)){
					if(field.getType() == String.class){
						if (field.get(object)!=null)
							contentValues.put(field.getName(),field.get(object).toString());
						else
							contentValues.put(field.getName(),"");
					}else if (field.getType().toString().equals("int") )    {
						contentValues.put(field.getName(),field.getInt(object));
					}else if (field.getType().toString().equals("long") ){    
						contentValues.put(field.getName(),field.getLong(object));
					}else if (field.getType().toString().equals("double") )    
						contentValues.put(field.getName(),field.getDouble(object));
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return contentValues;
	}

	public abstract E getObjectFromCursor(Cursor cursor);

	public E getObjectFromCursor(Cursor cursor, E object) {

		try {
			for (Field field : object.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				if (Modifier.isPrivate(field.getModifiers())){
					if(field.getType() == String.class)
						field.set(object, cursor.getString(cursor.getColumnIndex(field.getName())));				
					else if (field.getType().toString().equals("int") )    
						field.setInt(object, cursor.getInt(cursor.getColumnIndex(field.getName())));
					else if (field.getType().toString().equals("long") )    
						field.setLong(object, cursor.getLong(cursor.getColumnIndex(field.getName())));
					else if (field.getType().toString().equals("double") )    
						field.setDouble(object, cursor.getDouble(cursor.getColumnIndex(field.getName())));
				}		
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return object;
	}
	//insert
	public boolean insert(E object)
	{
		boolean wasInserted = false;
		openDB();
		wasInserted = insertInTransaction(mTableName, getContentValuesFor(object));
		closeDB();
		return wasInserted;
	}
	//delete
	public boolean delete(A key)
	{
		boolean wasDeleted = false;
		openDB();
		wasDeleted = deleteInTransaction(mTableName, mKey+"=?", new String[]{key.toString()});
		closeDB();
		return wasDeleted;
	}
	
	//update
	public boolean update(A key, E object)
	{
		boolean wasUpadated = false;
		openDB();
		wasUpadated = updateInTransaction(mTableName, getContentValuesFor(object), mKey+"=?", new String[]{key.toString()});
		closeDB();
		return wasUpadated;
	}
	
	//select
	public E select(A key)
	{
		E object = null;
		openDB();
		Cursor cursor = mDb.query(mTableName, null, mKey+"=?", new String[]{key.toString()}, null, null, null);
		if(cursor.moveToNext())	
			object = getObjectFromCursor(cursor);
		cursor.close();
		closeDB();
		return object;
	}
	//Select All
	public ArrayList<E> selectAll()
	{
		ArrayList<E> objects= null;
		openDB();
		Cursor cursor = mDb.query(mTableName, null, null, null, null, null, null);
		objects = new ArrayList<E>();
		while(cursor.moveToNext())	
			objects.add(getObjectFromCursor(cursor));
		cursor.close();
		closeDB();
		return objects;
	}
	
	public boolean exists(A key)
	{
		boolean exists = false;
		openDB();
		Cursor cursor = mDb.query(mTableName, new String[]{mKey}, mKey+"=?", new String[]{key.toString()}, null, null, null);
		exists = cursor.moveToNext();
		cursor.close();
		closeDB();
		return exists;
	}
}
