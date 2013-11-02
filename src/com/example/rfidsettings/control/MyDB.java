package com.example.rfidsettings.control;

import com.example.rfidsettings.dao.MyDatabaseHelper;
import com.example.rfidsettings.dao.TagObj;
import com.example.rfidsettings.model.RFIDSettings;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MyDB{  

	private RFIDSettings rfidsettings;
	
	private MyDatabaseHelper dbHelper;  

	private SQLiteDatabase database;
	
	private Context context;

	public final static String EMP_TABLE="Tags"; // name of table 
	
	public final static String EMP_NAME="Name";  // name of employee
	public final static String EMP_3G= "ThreeG";  // name of employee
	public final static String EMP_BT="BT";  // name of employee
	public final static String EMP_WIFI="Wifi";  // name of employee
	public final static String EMP_VOLUME="Volume";  // name of employee
	public final static String EMP_VIBRATE="Vibrate";  // name of employee
	public final static String EMP_TAGID="TagID";  // name of employee
	/** 
	 * 
	 * @param context 
	 */  
	public MyDB(Context context){  
		this.context = context;
		dbHelper = new MyDatabaseHelper(this.context);  
		database = dbHelper.getWritableDatabase();
		database.execSQL("DROP TABLE IF EXISTS " + EMP_TABLE);
		//database.execSQL("CREATE TABLE IF NOT EXISTS " + EMP_TABLE + " (TagID VARCHAR(50) PRIMARY KEY, Name VARCHAR(50), ThreeG Boolean, BT Boolean, Wifi Boolean, Volume Boolean, Vibrate Boolean)");
		database.execSQL("CREATE TABLE IF NOT EXISTS " + EMP_TABLE + " (TagID VARCHAR(50) PRIMARY KEY, Name VARCHAR(50), ThreeG Integer, BT Integer, Wifi Integer, Volume Integer, Vibrate Integer)");
	}


	public void createRecords(String tagid, String name, Integer tg, Integer bt, Integer wifi, Integer volume, Integer vibrate){
		/*
		ContentValues values = new ContentValues();
		values.put(EMP_TAGID,tagid);
		values.put(EMP_3G, tg);
		values.put(EMP_BT, bt);
		values.put(EMP_WIFI, wifi);
		values.put(EMP_VOLUME, volume);
		values.put(EMP_VIBRATE,vibrate);
		
		return database.insert(EMP_TABLE, null, values);
		*/
		database.execSQL("INSERT INTO " + EMP_TABLE + " ("+ EMP_TAGID + "," + EMP_NAME + "," + EMP_3G + "," + EMP_BT + "," +  EMP_WIFI + "," +  EMP_VOLUME + "," +  EMP_VIBRATE + ") VALUES('" + tagid + "','" + name + "'," + tg + "," + bt + "," + wifi + "," + volume + "," + vibrate + ")" );
		//database.execSQL("INSERT INTO " + EMP_TABLE + " ("+ EMP_TAGID + "," + EMP_NAME + "," + EMP_3G + "," + EMP_BT + "," +  EMP_WIFI + "," +  EMP_VOLUME + "," +  EMP_VIBRATE + ") VALUES('" + tagid + "','" + name + "'," + tg + "," + bt + "," + wifi + "," + volume + "," + vibrate + ")" );
		//database.execSQL("INSERT INTO " + EMP_TABLE + " VALUES('" + tagid + "','" + name + "'," + tg + "," + bt + "," + wifi + "," + volume + "," + vibrate + ")" );
	}    

	public TagObj selectRecords() {
		String[] cols = new String[] {EMP_TAGID, EMP_NAME, EMP_3G, EMP_BT, EMP_WIFI, EMP_VOLUME, EMP_VIBRATE};  
		Cursor mCursor = database.query(true, EMP_TABLE, cols, null, null, null, null, null, null);
		TagObj tag = new TagObj();
		if (mCursor != null){
			mCursor.moveToFirst();
			tag.setTagID(mCursor.getString(0));
            tag.setName(mCursor.getString(1));
            tag.set3g(Integer.parseInt(mCursor.getString(2)));
            tag.setBluetooth(Integer.parseInt(mCursor.getString(3)));
            tag.setWifi(Integer.parseInt(mCursor.getString(4)));
            tag.setVolume(Integer.parseInt(mCursor.getString(5)));
            tag.setVibrate(Integer.parseInt(mCursor.getString(6)));
            mCursor.close();
		}
		return tag; // iterate to get each value.
	}
	
	public TagObj Get(String tagid){
		TagObj tag = new TagObj();
		String sql = "SELECT * FROM " + EMP_TABLE + " WHERE " + EMP_TAGID + " = '" + tagid + "';";
		Cursor mCursor = database.rawQuery(sql, null);
		mCursor.moveToFirst();
		
		tag.setTagID(mCursor.getString(0));
		tag.setName(mCursor.getString(1));
		tag.set3g(mCursor.getInt(2));
		tag.setBluetooth(mCursor.getInt(3));
		tag.setWifi(mCursor.getInt(4));
		tag.setVolume(mCursor.getInt(5));
		tag.setVibrate(mCursor.getInt(6));
		
		System.out.println(mCursor.getString(0));
		System.out.println(mCursor.getString(1));
		System.out.println(mCursor.getInt(2));
		System.out.println(mCursor.getInt(3));
		System.out.println(mCursor.getInt(4));
		System.out.println(mCursor.getInt(5));
		System.out.println(mCursor.getInt(6));
		
        mCursor.close();
        return tag;
	}
	
	public void deleteRecords(String TagID){
		String sql = "DELETE FROM " + EMP_TABLE + " WHERE TagID='" + TagID + "';";
		database.execSQL(sql);
	}
	
	public void deleteAll(){
		database.execSQL("DELETE FROM " + EMP_TABLE + ";");
	}
	
	public void ActivateChanges(String TagID){
		TagObj tag = this.Get(TagID);
		System.out.println(tag.getBWifi());
		System.out.println(tag.getTagID());
		RFIDSettings.changeWifi(this.context, tag.getBWifi());
		//RFIDSettings.changeBluetooth(this.context, tag.getBluetooth());
		//RFIDSettings.changeVibrate(this.context, tag.getVibrate());
		//RFIDSettings.changeVolume(this.context, tag.getVolume());
	}

}