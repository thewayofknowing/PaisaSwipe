package com.example.backup.db;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.backup.data.Advertisement;
import com.example.backup.data.Stats;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.BitmapFactory;

public class DataBaseHelper extends SQLiteOpenHelper {

	// Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "SwipeNAd";
	
	// Table Names
    private static final String TABLE_ADVERTISEMENT = "advertisement";
    private static final String TABLE_STATISTICS = "statistics";
	
 // Advertisement column names
    private static final String AD_ID = "id";
    private static final String AD_TYPE = "type";
    private static final String AD_IMAGE_NAME = "image_name";
    private static final String AD_IMPRESSION_COUNT = "ad_impressions";
    private static final String AD_COMP_ID = "company_id";
    private static final String AD_AUDIENCE = "audience";
    private static final String AD_URL = "ad_url";
    
 // Statistics column names
    private static final String STATS_AD_ID = "ad_id";
    private static final String STATS_COMP_ID = "company_id";
    private static final String STATS_USER_ID = "user_id";
    private static final String STATS_TYPE = "type";
    private static final String STATS_APPEARED_AT = "appeared_at";
    private static final String STATS_CLICKED_AT = "clicked_at";
    private static final String STATS_SWIPED_AT = "swiped_at";
    private static final String STATS_COMPLETION_TIME = "completion_time";
    private static final String STATS_PREVIEWS = "previews";

    
	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Table Create Statements
	    // Advertisement table create statement
	    final String CREATE_TABLE_ADVERTISEMENT = "CREATE TABLE IF NOT EXISTS "
	            + TABLE_ADVERTISEMENT + "(" + AD_ID + " INTEGER PRIMARY KEY," + AD_TYPE
	            + " INT," + AD_IMAGE_NAME + " VARCHAR," + AD_IMPRESSION_COUNT + " INT,"  
	            + AD_COMP_ID + " INT," + AD_AUDIENCE + " VARCHAR," + AD_URL + " VARCHAR)";
	    
	    //Statistics table create statement
	    final String CREATE_TABLE_STATISTICS = "CREATE TABLE IF NOT EXISTS " 
	    	+ TABLE_STATISTICS + "(" + STATS_AD_ID + " INTEGER," + STATS_COMP_ID + " INTEGER,"
	    	+ STATS_USER_ID + " INTEGER,"  + STATS_APPEARED_AT + " DATETIME,"
	    	+ STATS_SWIPED_AT + " DATETIME," + STATS_CLICKED_AT + " DATETIME," + STATS_COMPLETION_TIME 
	    	+ " INT," + STATS_PREVIEWS + " INT)";
	     
	    db.execSQL(CREATE_TABLE_ADVERTISEMENT);
	    db.execSQL(CREATE_TABLE_STATISTICS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADVERTISEMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATISTICS);
 
        // Create tables again
        onCreate(db);
	}
	
	// Adding new contact
    public void addAdvertisement(Advertisement advertisement) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(AD_ID, advertisement.getId()); 
        values.put(AD_COMP_ID, advertisement.getCompanyId());
        values.put(AD_TYPE, advertisement.getType());
        values.put(AD_IMAGE_NAME, advertisement.getImageName());
        values.put(AD_IMPRESSION_COUNT, advertisement.getImpressionCount());
        values.put(AD_AUDIENCE, advertisement.getAudience() + "");
        values.put(AD_URL, advertisement.getURL());
        
 
        // Inserting Row
        db.insert(TABLE_ADVERTISEMENT, null, values);
        db.close(); // Closing database connection
    }
    
    //Adding stat
    public void addStat(Stats stats) {
    	 SQLiteDatabase db = this.getWritableDatabase();
    	 
         ContentValues values = new ContentValues();
         values.put(STATS_AD_ID, stats.getAdId());
         values.put(STATS_COMP_ID, stats.getCompanyId());
         values.put(STATS_USER_ID, stats.getUserId());
         values.put(STATS_APPEARED_AT, stats.getAppearedAtTime());
         values.put(STATS_SWIPED_AT, stats.getSwipedAtTime());
         values.put(STATS_CLICKED_AT, stats.getClickedAtTime());
         values.put(STATS_COMPLETION_TIME, stats.getCompletionTime());
         values.put(STATS_PREVIEWS, stats.getPreviews());
         
      // Inserting Row
         db.insert(TABLE_ADVERTISEMENT, null, values);
         db.close(); // Closing database connection
    }
    
    // Getting All Ads
    public List<Advertisement> getAllAds() {
       List<Advertisement> adList = new ArrayList<Advertisement>();
       // Select All Query
       String selectQuery = "SELECT  * FROM " + TABLE_ADVERTISEMENT;
    
       SQLiteDatabase db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery(selectQuery, null);
    
       // looping through all rows and adding to list
       if (cursor.moveToFirst()) {
           do {
               Advertisement advertisement = new Advertisement();
               advertisement.setId(Integer.parseInt(cursor.getString(0)));
               advertisement.setType(Integer.parseInt(cursor.getString(1)));
               advertisement.setImageName(cursor.getString(2));
               advertisement.setImpressionCount(cursor.getInt(3));
               advertisement.setCompanyId(Integer.parseInt(cursor.getString(4)));
               advertisement.setAudience(cursor.getString(5).charAt(0));
               advertisement.setURL(cursor.getString(6));
               
               // Adding ad to list
               adList.add(advertisement);
           } while (cursor.moveToNext());
       }
    
       // return ad list
       return adList;
   }
    
 // Getting All Stats
    public List<Stats> getAllStats() {
       List<Stats> statsList = new ArrayList<Stats>();
       // Select All Query
       String selectQuery = "SELECT  * FROM " + TABLE_STATISTICS;
    
       SQLiteDatabase db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery(selectQuery, null);
    
       // looping through all rows and adding to list
       if (cursor.moveToFirst()) {
           do {
               Stats stats = new Stats();
               stats.setAdId(cursor.getInt(0));
               stats.setCompanyId(cursor.getInt(1));
               stats.setUserId(cursor.getInt(2));
               stats.setAppearedAtTime(cursor.getString(3));
               stats.setSwipedAtTime(cursor.getString(4));
               stats.setClickedAtTime(cursor.getString(5));
               stats.setCompletionTime(cursor.getInt(6));
               stats.setPreviews(cursor.getInt(7));
               
               // Adding ad to list
               statsList.add(stats);
           } while (cursor.moveToNext());
       }
    
       // return ad list
       return statsList;
   }
    
    // Deleting single stat
public void deleteContact(Stats stat) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(TABLE_STATISTICS, STATS_APPEARED_AT + " = ?",
            new String[] { String.valueOf(stat.getAppearedAtTime()) });
    db.close();
}

}
