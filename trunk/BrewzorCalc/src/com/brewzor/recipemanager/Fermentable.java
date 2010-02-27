package com.brewzor.recipemanager;

import com.brewzor.calculator.R;
import com.brewzor.converters.Gravity;
import com.brewzor.converters.Mass;

public class Fermentable extends Object {

	static public enum Type {
		GRAIN,
		EXTRACT,
		SUGAR,
		ADJUNCT,
		DRY_EXTRACT,
		OTHER
	};

    public static final String TABLE = "fermentables";
    
    public final class FIELDS {
    	public static final String ID = "_id";
	    public static final String NAME = "name";
	    public static final String TYPE = "type";
	    public static final String MANUFACTURER = "manufacturer";
	    public static final String ORGANIC = "organic";
	    public static final String URL = "url";
	    public static final String POTENTIAL = "potential";
	    public static final String YIELD = "yield";
	    public static final String COARSE_FINE = "coarse_fine";
	    public static final String MOISTURE = "moisture";
	    public static final String COLOR = "color";
	    public static final String DIASTATIC_POWER = "diastatic_power";
	    public static final String PROTEIN = "protein";
	    public static final String MAX_IN_BATCH = "max_in_batch";    
	    public static final String ADD_AFTER_BOIL = "add_after_boil";
	    public static final String MUST_MASH = "must_mash";
	    public static final String NOTES = "notes";
	    public static final String USER_CREATED = "user_created";
	    public static final String SORT_INDEX = "sort_index";
    }

    public static final String[] aFields = {
    	FIELDS.NAME,
    	FIELDS.TYPE,
    	FIELDS.MANUFACTURER,
//    	FIELDS.ORGANIC,
//    	FIELDS.URL,
    	FIELDS.POTENTIAL,
    	FIELDS.YIELD,
//    	FIELDS.COARSE_FINE,
//    	FIELDS.MOISTURE,
    	FIELDS.COLOR
//    	FIELDS.DIASTATIC_POWER,
//    	FIELDS.PROTEIN,
//    	FIELDS.MAX_IN_BATCH,
//    	FIELDS.NOTES,
//    	FIELDS.USER_CREATED,
//    	FIELDS.SORT_INDEX
    };
    
    public static final int[] aViews = {
    	R.id.name,
    	R.id.type,
    	R.id.manufacturer,
//    	R.id.organic,
//    	R.id.url,
    	R.id.potential,
    	R.id.yield,
//    	R.id.coarse_fine,
//    	R.id.moisture,
    	R.id.color
//    	R.id.diastatic_power,
//    	R.id.protein,
//    	R.id.max_in_batch,
//    	R.id.notes,
//    	R.id.user_created,
//    	R.id.sort_index
    };

}






















