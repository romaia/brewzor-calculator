package com.brewzor.recipemanager;

import com.brewzor.calculator.R;

public class MashProfile extends Object {

	static public enum Type {
		INFUSION,
		DECOCTION,
		TEMPERATURE
	};

	static public enum SpargeType {
		FLY,
		BATCH,
		NONE
	}
	
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
    	FIELDS.NAME 
//    	FIELDS.INFUSION_TEMPERATURE, 
//    	FIELDS.SPARGE_TEMPERATURE,
//    	FIELDS.SPARGE_TYPE,
//    	FIELDS.USER_CREATED,
//    	FIELDS.SORT_INDEX
	};
    
    public static final int[] aViews = {
    	R.id.name
//   	R.id.type,
//    	R.id.manufacturer,
//    	R.id.organic,
//    	R.id.url,
//    	R.id.potential,
//    	R.id.yield,
//    	R.id.coarse_fine,
//    	R.id.moisture,
//    	R.id.color
//    	R.id.diastatic_power,
//    	R.id.protein,
//    	R.id.max_in_batch,
//    	R.id.notes,
//    	R.id.user_created,
//    	R.id.sort_index
    };


}
