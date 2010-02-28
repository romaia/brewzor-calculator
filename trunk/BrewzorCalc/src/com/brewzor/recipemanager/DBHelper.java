package com.brewzor.recipemanager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import com.brewzor.converters.BeerColor;
import com.brewzor.converters.Gravity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper{
	 
    //The Android's default system path of your application database.
    private static final String DATABASE_PATH = "/data/data/com.brewzor.calculator/databases/";
    private static final String DATABASE_NAME = "brewzor.db";
    private static final int DATABASE_VERSION = 1;
    
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
    	//Log.v("DBHelper", "createDataBase()");
    	boolean dbExist = checkDataBase();
    	if (dbExist) {
    		//do nothing - database already exist
    	} else {
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
    	//Log.v("DBHelper", "checkDataBase()");
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DATABASE_PATH + DATABASE_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    	} catch(SQLiteException e) {
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
 
    	//Log.v("DBHelper", "copyDataBase()");
    	
    	// Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DATABASE_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DATABASE_PATH + DATABASE_NAME;
 
    	// Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	// transfer bytes from the inputfile to the outputfile
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
    	//Log.v("DBHelper", "openDataBase()");
    	// Open the database
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
    	if(db != null) db.close();
    	super.close();
 	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
 	
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}

	// Fermentables
	
	public Cursor getAllFermentables() {
        return db.query(Fermentable.TABLE, 
        		new String[] {	Fermentable.FIELDS.ID, 
        						Fermentable.FIELDS.NAME, 
        						Fermentable.FIELDS.TYPE, 
        						Fermentable.FIELDS.MANUFACTURER, 
        						Fermentable.FIELDS.POTENTIAL,
        						Fermentable.FIELDS.YIELD,
        						Fermentable.FIELDS.COLOR }, 
                null, null, null, null, Fermentable.FIELDS.SORT_INDEX + " ASC");
    }
	
	public Cursor getFermentable(long rowId) throws SQLException {
       Cursor mCursor =
               db.query(true, Fermentable.TABLE, 
               		new String[] {	Fermentable.FIELDS.ID, 
            		   				Fermentable.FIELDS.NAME,
            						Fermentable.FIELDS.TYPE, 
            						Fermentable.FIELDS.MANUFACTURER, 
            						Fermentable.FIELDS.POTENTIAL,
            						Fermentable.FIELDS.YIELD,
            						Fermentable.FIELDS.COLOR }, 
               		Fermentable.FIELDS.ID + "=" + rowId, 
               		null, null, null, Fermentable.FIELDS.SORT_INDEX + " ASC", null);
       if (mCursor != null) {
           mCursor.moveToFirst();
       }
       return mCursor;
   }

	public boolean updateFermentable(long rowId, String name, String type, String manufacturer, Gravity potential, double yield, BeerColor color) {
	   ContentValues args = new ContentValues();
	   args.put(Fermentable.FIELDS.NAME, name);
	   args.put(Fermentable.FIELDS.TYPE, type);
	   args.put(Fermentable.FIELDS.MANUFACTURER, manufacturer);
	   args.put(Fermentable.FIELDS.POTENTIAL, potential.compare(Gravity.Unit.SG));
	   args.put(Fermentable.FIELDS.YIELD, yield);
	   args.put(Fermentable.FIELDS.COLOR, color.compare(BeerColor.Unit.SRM));
	   args.put(Fermentable.FIELDS.SORT_INDEX, (name + manufacturer).toUpperCase());
	   return db.update(Fermentable.TABLE, args, Fermentable.FIELDS.ID + "=" + rowId, null) > 0;
	}

	public long insertFermentable(String name, String type, String manufacturer, Gravity potential, double yield, BeerColor color) {
		ContentValues args = new ContentValues();
		args.put(Fermentable.FIELDS.NAME, name);
		args.put(Fermentable.FIELDS.TYPE, type);
		args.put(Fermentable.FIELDS.MANUFACTURER, manufacturer);
		args.put(Fermentable.FIELDS.POTENTIAL, potential.compare(Gravity.Unit.SG));
		args.put(Fermentable.FIELDS.YIELD, yield);
		args.put(Fermentable.FIELDS.COLOR, color.compare(BeerColor.Unit.SRM));
		args.put(Fermentable.FIELDS.SORT_INDEX, (name + manufacturer).toUpperCase());
		return db.insert(Fermentable.TABLE, null, args);
	}
   
	public boolean deleteFermentable(long rowId) {
		return db.delete(Fermentable.TABLE, Fermentable.FIELDS.ID + "=" + rowId, null) > 0;
	}

	
	// Mash Profiles

	public Cursor getAllMashProfiles() {
		return db.query(MashProfile.TABLE, 
				new String[] {	MashProfile.FIELDS.ID, 
								MashProfile.FIELDS.NAME, 
								MashProfile.FIELDS.INFUSION_TEMPERATURE, 
								MashProfile.FIELDS.SPARGE_TEMPERATURE,
								MashProfile.FIELDS.SPARGE_TYPE,
								MashProfile.FIELDS.USER_CREATED,
								MashProfile.FIELDS.SORT_INDEX
								}, 
	                null, null, null, null, MashProfile.FIELDS.SORT_INDEX + " ASC");
	    }
		
	public Cursor getMashProfile(long rowId) throws SQLException {
			Cursor mCursor =
	               db.query(true, Fermentable.TABLE, 
	               		new String[] {	MashProfile.FIELDS.ID, 
						        		MashProfile.FIELDS.NAME, 
						        		MashProfile.FIELDS.INFUSION_TEMPERATURE, 
						        		MashProfile.FIELDS.SPARGE_TEMPERATURE,
						        		MashProfile.FIELDS.SPARGE_TYPE,
						        		MashProfile.FIELDS.USER_CREATED,
						        		MashProfile.FIELDS.SORT_INDEX }, 
	               		MashProfile.FIELDS.ID + "=" + rowId, 
	               		null, null, null, null, null);
			if (mCursor != null) {
				mCursor.moveToFirst();
	       }
	       return mCursor;
	   }

	public boolean deleteMashProfile(long rowId) {
			return db.delete(MashProfile.TABLE, MashProfile.FIELDS.ID + "=" + rowId, null) > 0;
	}
	   

	   
	   
	// Mash Profile Steps
	   
	   
	public Cursor getAllMashProfileSteps(long profileId) {
        return db.query(MashProfile.TABLE, 
        		new String[] {	MashProfile.FIELDS.ID, 
				        		MashProfile.FIELDS.NAME, 
				        		MashProfile.FIELDS.INFUSION_TEMPERATURE, 
				        		MashProfile.FIELDS.SPARGE_TEMPERATURE,
				        		MashProfile.FIELDS.SPARGE_TYPE,
				        		MashProfile.FIELDS.USER_CREATED,
				        		MashProfile.FIELDS.SORT_INDEX
				        		}, 
                null, null, null, null, MashProfile.FIELDS.SORT_INDEX + " ASC");
		
		
	}
	
	public Cursor getMashProfileStep(long rowId) throws SQLException {
	       Cursor mCursor =
	               db.query(true, Fermentable.TABLE, 
	               		new String[] {	MashProfile.FIELDS.ID, 
						        		MashProfile.FIELDS.NAME, 
						        		MashProfile.FIELDS.INFUSION_TEMPERATURE, 
						        		MashProfile.FIELDS.SPARGE_TEMPERATURE,
						        		MashProfile.FIELDS.SPARGE_TYPE,
						        		MashProfile.FIELDS.USER_CREATED,
						        		MashProfile.FIELDS.SORT_INDEX }, 
	               		MashProfile.FIELDS.ID + "=" + rowId, 
	               		null, null, null, null, null);
	       if (mCursor != null) {
	           mCursor.moveToFirst();
	       }
	       return mCursor;
	}

	public boolean deleteMashProfileStep(long rowId) {
//		return db.delete(MashProfileStep.TABLE, MashProfileStep.FIELDS.ID + "=" + rowId, null) > 0;
		return false;
	}

}