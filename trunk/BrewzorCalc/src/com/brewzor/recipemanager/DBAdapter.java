package com.brewzor.recipemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
    private static final String TAG = "DBAdapter";
    
    private static final String DATABASE_NAME = "brewzor";

    private static final int DATABASE_VERSION = 2;
    
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
    
    private static final String DATABASE_FERMENTABLES_CREATE = 
    	"create table if not exists fermentables ("
    	+ "_id integer primary key autoincrement, "
    	+ "name text not null, "
    	+ "type text not null, "
    	+ "potential float not null, "
    	+ "yield float float not null,"
    	+ "coarse_fine float not null, "
    	+ "moisture float not null, "
    	+ "color float not null, "
    	+ "diastatic_power float not null, "
    	+ "protein float not null, "
    	+ "max_in_batch float not null, "
    	+ "add_after_boil integer not null, "
    	+ "must_mash integer not null "
    	+ ");";
    
    private static final String DATABASE_RECIPES_CREATE =
        "create table if not exists recipes ("
    	+ "_id integer primary key autoincrement, "
        + "name text not null, "
        + "boil_time integer not null, "
        + "start_time integer, "
        + "paused_time integer, "
        + "running integer "
        + ");";

//    private static final String DATABASE_FERMENTABLE_ADDITIONS = 
    
    private static final String DATABASE_MASH_EVENTS_CREATE =
        "create table if not exists mash_events ("
    	+ "_id integer primary key autoincrement, "
        + "schedule_id integer not null, "
        + "step_type text not null, "
        + "water_to_grain_ratio float not null, "
        + "duration integer not null, "
        + "temperature integer not null, "
        + "temperature_unit text not null, "
        + "volume_unit text not null, "
        + "mass_unit text not null, "
        + "description text"
        + ");";
    
    private static final String DATABASE_BOIL_EVENTS_CREATE =
        "create table if not exists boil_events ("
    	+ "_id integer primary key autoincrement, "
        + "schedule_id integer not null, "
        + "alarm_time integer not null, "
        + "pause boolean not null, "
        + "description text"
        + ");";        
        
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
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {
        	Log.v("DBAdapter", "onCreate()");
            db.execSQL(DATABASE_RECIPES_CREATE);
        	db.execSQL(DATABASE_MASH_EVENTS_CREATE);
        	db.execSQL(DATABASE_BOIL_EVENTS_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
        int newVersion) 
        {
           	Log.v("DBAdapter", "onUpgrade()");
        	db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        	db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASH_EVENTS);
        	db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOIL_EVENTS);
            onCreate(db);
        }
    }    
    
    public DBAdapter open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() 
    {
        DBHelper.close();
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
    
    public boolean deletebOILMashEvent(long rowId) {
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