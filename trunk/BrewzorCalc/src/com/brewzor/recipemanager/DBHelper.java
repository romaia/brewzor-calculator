package com.brewzor.recipemanager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper{
	 
    //The Android's default system path of your application database.
    private static String DATABASE_PATH = "/data/data/com.brewzor.calculator/databases/";
    private static String DATABASE_NAME = "brewzor.db";
    private static final int DATABASE_VERSION = 1;

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
    
    
    private SQLiteDatabase db; 
 
    private final Context myContext;
 
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DBHelper(Context context) {
 
    	super(context, DATABASE_NAME, null, 1);
        this.myContext = context;
    }	
 
  /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException {
 
    	Log.v("DBHelper", "createDataBase()");
    	
    	boolean dbExist = checkDataBase();
 
    	if(dbExist){
    		//do nothing - database already exist
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
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
 
    	Log.v("DBHelper", "checkDataBase()");
    	
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
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
 
    	Log.v("DBHelper", "copyDataBase()");
    	
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DATABASE_NAME);
 
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
 
    public void openDataBase() throws SQLException {
 
    	Log.v("DBHelper", "openDataBase()");
    	//Open the database
        String myPath = DATABASE_PATH + DATABASE_NAME;
    	db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
 
    }
 
    public void open() {
        try {
        	createDataBase();
	 	} catch (IOException ioe) {
	 		throw new Error("Unable to create database");
	 	}
	 
	 	try {
	 		openDataBase();
	 	}catch(SQLException sqle){
	 		throw sqle;
	 	}    	
    }
    
    @Override
	public synchronized void close() {
 
    	    if(db != null)
    		    db.close();
 
    	    super.close();
 
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
 	
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}
	
   public Cursor getAllFermentables() {
        return db.query(Fermentable.TABLE, 
        		new String[] {	Fermentable.FIELDS.ID, 
        						Fermentable.FIELDS.NAME, 
        						Fermentable.FIELDS.MANUFACTURER, 
        						Fermentable.FIELDS.POTENTIAL,
        						Fermentable.FIELDS.COLOR }, 
                null, null, null, null, null);
    }
	
   public boolean deleteFermentable(long rowId) {
       return db.delete(Fermentable.TABLE, Fermentable.FIELDS.ID + "=" + rowId, null) > 0;
   }
   

}