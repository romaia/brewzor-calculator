package com.brewzor.recipemanager;

public class MashProfile extends Object {
	static public enum Type {
		GRAIN,
		EXTRACT,
		SUGAR,
		ADJUNCT,
		DRY_EXTRACT,
		OTHER
	};

    public static final String TABLE = "mash_profiles";
    
    public final class FIELDS {
    	public static final String ID = "_id";
	    public static final String NAME = "name";
	    public static final String INFUSION_TEMPERATURE = "infusion_temperature";
	    public static final String SPARGE_TEMPERATURE = "sparge_temperature";
	    public static final String SPARGE_TYPE = "sparge_type";
	    public static final String USER_CREATED = "user_created";
	    public static final String SORT_INDEX = "sort_index";
    };
    
    public static final String[] aFields = {	
    	FIELDS.ID, 
    	FIELDS.NAME, 
    	FIELDS.INFUSION_TEMPERATURE, 
    	FIELDS.SPARGE_TEMPERATURE,
    	FIELDS.SPARGE_TYPE,
    	FIELDS.USER_CREATED,
    	FIELDS.SORT_INDEX
	};
}
