package com.brewzor.recipemanager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
    private static final String TAG = "DBAdapter";
    
    private static final String DATABASE_NAME = "brewzor";

    private static final int DATABASE_VERSION = 4;
    
    private static final String TABLE_RECIPES = "recipes";
    private static final String TABLE_MASH_EVENTS = "mash_events";
    private static final String TABLE_BOIL_EVENTS = "boil_events";

    public static final String FIELD_ROWID = "_id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_BOIL_TIME = "boil_time";
    
    public static final String FIELD_SCHEDULE_ID = "schedule_id";
    public static final String FIELD_DURATION = "duration";    
    public static final String FIELD_TEMPERATURE = "temperature";
    public static final String FIELD_TEMPERATURE_LABEL = "temperature_label";
    public static final String FIELD_DESCRIPTION = "description";
    
    public static final String FIELD_ALARM_TIME = "alarm_time";
    public static final String FIELD_PAUSE = "pause";
    
    private static final String TABLE_FERMENTABLES = "fermentables";
    private static final String FIELD_TYPE = "type";
    private static final String FIELD_POTENTIAL = "potential";
    private static final String FIELD_YIELD = "yield";
    private static final String FIELD_COARSE_FINE = "coarse_fine";
    private static final String FIELD_MOISTURE = "moisture";
    private static final String FIELD_COLOR = "color";
    private static final String FIELD_DIASTATIC_POWER = "diastatic_power";
    private static final String FIELD_PROTEIN = "protein";
    private static final String FIELD_MAX_IN_BATCH = "max_in_batch";    
    private static final String FIELD_ADD_AFTER_BOIL = "add_after_boil";
    private static final String FIELD_MUST_MASH = "must_mash";
    private static String DATABASE_PATH = "/data/data/com.brewzor.calculator/databases/";
    
    private final Context context; 
    
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) 
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
        
    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
    	private Context ctx;
    	
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.ctx = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {
        	Log.v(TAG, "onCreate()");
        	boolean dbExist = checkDataBase();
            
        	if(dbExist){
        		//do nothing - database already exists
        	}else{
     
        		//By calling this method and empty database will be created into the default system path
                //of your application so we are gonna be able to overwrite that database with our database.
            	this.getReadableDatabase();
     
            	try {
     
        			copyDataBase();
     
        		} catch (IOException e) {
     
            		throw new Error("Error copying database");
     
            	}
        	}
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
        int newVersion) 
        {
           	Log.v(TAG, "onUpgrade()");
        }
     
        /**
         * Check if the database already exist to avoid re-copying the file each time you open the application.
         * @return true if it exists, false if it doesn't
         */
        private boolean checkDataBase(){
     
        	SQLiteDatabase checkDB = null;
     
        	try{
        		String myPath = DATABASE_PATH + DATABASE_NAME;
        		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
     
        	}catch(SQLiteException e){
     
        		//database does't exist yet.
     
        	}
     
        	if(checkDB != null){
     
        		checkDB.close();
     
        	}
     
        	return checkDB != null ? true : false;
        }
     
        /**
         * Copies your database from your local assets-folder to the just created empty database in the
         * system folder, from where it can be accessed and handled.
         * This is done by transferring byte stream.
         * */
        private void copyDataBase() throws IOException{
        	Log.v(TAG, "copyDataBase()");
        	//Open your local db as the input stream
        	InputStream myInput = ctx.getAssets().open(DATABASE_NAME);
     
        	// Path to the just created empty db
        	String outFileName = DATABASE_PATH + DATABASE_NAME;
     
        	//Open the empty db as the output stream
        	OutputStream myOutput = new FileOutputStream(outFileName);
     
        	//transfer bytes from the inputfile to the outputfile
        	byte[] buffer = new byte[1024];
        	int length;
        	while ((length = myInput.read(buffer))>0){
        		myOutput.write(buffer, 0, length);
        	}
     
        	//Close the streams
        	myOutput.flush();
        	myOutput.close();
        	myInput.close();
     
        }
     
    }    
/*    
    public DBAdapter open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() 
    {
        DBHelper.close();
    }
*/
    public void openDataBase() throws SQLException{
        
    	//Open the database
        String myPath = DATABASE_PATH + DATABASE_NAME;
    	db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    }
 
	public void close() {
 
    	    if(db != null)
    		    db.close();
  
	}
    
    
    /*
     * Brew Session Table
     */

    public long insertBrewSession(String name, Integer boilTime) {
    	ContentValues session = new ContentValues();
    	session.put(FIELD_NAME, name);
    	session.put(FIELD_BOIL_TIME, boilTime.intValue());
        return db.insert(TABLE_RECIPES, null, session);
    }
    
    public boolean deleteBrewSession(long rowId) {
        return db.delete(TABLE_RECIPES, FIELD_ROWID + "=" + rowId, null) > 0;
    }
    
    public Cursor getAllBrewSessions() {
        return db.query(TABLE_RECIPES, 
        		new String[] {FIELD_ROWID, FIELD_NAME, FIELD_BOIL_TIME}, 
                null, null, null, null, null);
    }

    public Cursor getBrewSession(long rowId) throws SQLException {
        Cursor mCursor =
                db.query(true, TABLE_RECIPES, 
                		new String[] {FIELD_ROWID, FIELD_NAME, FIELD_BOIL_TIME}, 
                		FIELD_ROWID + "=" + rowId, 
                		null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean updateBrewSession(long rowId, String name, Integer boilTime) {
        ContentValues args = new ContentValues();
        args.put(FIELD_NAME, name);
        args.put(FIELD_BOIL_TIME, boilTime.intValue());
        return db.update(TABLE_RECIPES, args, FIELD_ROWID + "=" + rowId, null) > 0;
    }

    /*
     * Mash Events Table
     */
    
    public long insertMashEvent(Integer schedule_id, Integer duration, Double temperature, String temperatureUnit, String description) {
    	ContentValues event = new ContentValues();
    	event.put(FIELD_SCHEDULE_ID, schedule_id.intValue());
    	event.put(FIELD_NAME, duration.intValue());
    	event.put(FIELD_DURATION, duration.intValue());
    	event.put(FIELD_TEMPERATURE, temperature.intValue());
    	event.put(FIELD_TEMPERATURE_LABEL, temperatureUnit);
    	event.put(FIELD_DESCRIPTION, description);
        return db.insert(TABLE_MASH_EVENTS, null, event);
    }
    
    public boolean deleteMashEvent(long rowId) {
        return db.delete(TABLE_MASH_EVENTS, FIELD_ROWID + "=" + rowId, null) > 0;
    }
    
    public Cursor getAllMashEvents() {
        return db.query(TABLE_MASH_EVENTS, 
        		new String[] {FIELD_ROWID, FIELD_SCHEDULE_ID, FIELD_DURATION, FIELD_TEMPERATURE, FIELD_TEMPERATURE_LABEL, FIELD_DESCRIPTION}, 
                null, null, null, null, null);
    }

    public Cursor getMashEvent(long rowId) throws SQLException {
        Cursor mCursor =
                db.query(true, TABLE_MASH_EVENTS, 
                		new String[] {FIELD_ROWID, FIELD_SCHEDULE_ID, FIELD_DURATION, FIELD_TEMPERATURE, FIELD_TEMPERATURE_LABEL, FIELD_DESCRIPTION}, 
                		FIELD_ROWID + "=" + rowId, 
                		null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean updateMashEvent(long rowId, String name, Integer duration, Double temperature, String temperatureUnit, String description) {
		ContentValues event = new ContentValues();
		event.put(FIELD_SCHEDULE_ID, name);
		event.put(FIELD_DURATION, duration.intValue());
		event.put(FIELD_TEMPERATURE, temperature.intValue());
		event.put(FIELD_TEMPERATURE_LABEL, temperatureUnit);
		event.put(FIELD_DESCRIPTION, description);
        return db.update(TABLE_MASH_EVENTS, event, FIELD_ROWID + "=" + rowId, null) > 0;
    }

    /*
     * Boil Events Table
     */
    
    public long insertBoilEvent(Integer schedule_id, Integer time, Boolean paused, String description) {
    	ContentValues event = new ContentValues();
    	event.put(FIELD_SCHEDULE_ID, schedule_id.intValue());
    	event.put(FIELD_ALARM_TIME, time.intValue());
    	event.put(FIELD_PAUSE, paused.booleanValue());
    	event.put(FIELD_DESCRIPTION, description);
        return db.insert(TABLE_BOIL_EVENTS, null, event);
    }
    
    public boolean deleteBoilMashEvent(long rowId) {
        return db.delete(TABLE_BOIL_EVENTS, FIELD_ROWID + "=" + rowId, null) > 0;
    }
    
    public Cursor getAllBoilEvents() {
        return db.query(TABLE_BOIL_EVENTS, 
        		new String[] {FIELD_ROWID, FIELD_SCHEDULE_ID, FIELD_ALARM_TIME, FIELD_PAUSE, FIELD_DESCRIPTION}, 
                null, null, null, null, null);
    }

    public Cursor getBoilEvent(long rowId) throws SQLException {
        Cursor mCursor =
                db.query(true, TABLE_BOIL_EVENTS, 
                		new String[] {FIELD_ROWID, FIELD_SCHEDULE_ID, FIELD_ALARM_TIME, FIELD_PAUSE, FIELD_DESCRIPTION}, 
                		FIELD_ROWID + "=" + rowId, 
                		null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean updateBoilEvent(long rowId, String name, Integer time, Boolean pause, String description) {
		ContentValues event = new ContentValues();
		event.put(FIELD_SCHEDULE_ID, name);
		event.put(FIELD_ALARM_TIME, time.intValue());
		event.put(FIELD_PAUSE, pause.booleanValue());
		event.put(FIELD_DESCRIPTION, description);
        return db.update(TABLE_BOIL_EVENTS, event, FIELD_ROWID + "=" + rowId, null) > 0;
    }



}