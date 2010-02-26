package com.brewzor.recipemanager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.brewzor.calculator.R;

public class FermentableEditActivity extends Activity {
	
	private SharedPreferences prefs;

	DBAdapter db;

	Fermentable fermentable;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.window_title_format, getString(R.string.app_name), getString(R.string.fermentables)));
        setContentView(R.layout.recipe);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        db = new DBAdapter(this);
        db.openDataBase();
        
	}

	public void onCreate(Bundle savedInstanceState, Fermentable newFermentable) {
		this.fermentable = newFermentable;
		onCreate(savedInstanceState);
	}
	
}
