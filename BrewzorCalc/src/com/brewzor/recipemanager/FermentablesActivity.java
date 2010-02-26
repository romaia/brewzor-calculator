package com.brewzor.recipemanager;

import java.io.IOException;
import java.util.ArrayList;

import com.brewzor.calculator.OptionsMenuHandler;
import com.brewzor.calculator.R;
import com.brewzor.calculator.UnitListItem;
import com.brewzor.converters.Color;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class FermentablesActivity extends ListActivity {

	private SharedPreferences prefs;

    public static final String[] displayFields = new String[] {	Fermentable.FIELDS.NAME, 
														        Fermentable.FIELDS.MANUFACTURER,
														        Fermentable.FIELDS.COLOR};

	public static final int[] displayViews = new int[] { 	R.id.name, 
															R.id.manufacturer,
															R.id.color};

	protected DBHelper db;
	protected Cursor cFermentables;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.window_title_format, getString(R.string.app_name), getString(R.string.fermentables)));
        setContentView(R.layout.fermentables);
        
        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        db = new DBHelper(this);
        db.open();
        cFermentables = db.getAllFermentables();
        setListAdapter(new FermentablesAdapter(this, cFermentables));	
    }
	
	static public boolean showMenu(Activity activity, MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.options_menu_add:
	    	activity.startActivity(new Intent(activity.getBaseContext(), FermentableEditActivity.class));
	        return true;
	    }
	    return false;
	}
	
	static public boolean createMenu(Activity activity, Menu menu) {
	    MenuInflater inflater = activity.getMenuInflater();
	    inflater.inflate(R.menu.options_menu, menu);
	    return true;
	}
	
	/* Creates the menu items */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return OptionsMenuHandler.createMenu(this, menu);
	}

	/* Handles item selections */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return OptionsMenuHandler.showMenu(this, item);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		cFermentables.close();
		db.close();
		super.onDestroy();
	}

	public final class FermentablesAdapter extends SimpleCursorAdapter {

		private Context context; 

		public FermentablesAdapter(Context context, Cursor c) {
			super(context, R.layout.fermentables_list_item, c, displayFields, displayViews);
	        this.context = context; 
		}

		/* (non-Javadoc)
		 * @see android.widget.SimpleCursorAdapter#bindView(android.view.View, android.content.Context, android.database.Cursor)
		 */
		@Override
		public void bindView(View view, final Context context, Cursor cursor) {
			View v = view;

			if (v == null) {
	            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            v = vi.inflate(R.layout.fermentables_list_item, null);
			}
			
			TextView nameView = (TextView) v.findViewById(R.id.name); 
			int name_index = cursor.getColumnIndex(Fermentable.FIELDS.NAME); 
			//Log.v("DB", "name_index=" + name_index);
			String name = cursor.getString(name_index); 
			nameView.setText(name); 

			TextView manufacturerView = (TextView) v.findViewById(R.id.manufacturer); 
			int man_index = cursor.getColumnIndex(Fermentable.FIELDS.MANUFACTURER); 
			String manufacturer = cursor.getString(man_index); 
			manufacturerView.setText(manufacturer); 

			TextView colorView = (TextView) v.findViewById(R.id.color);
			int color_index = cursor.getColumnIndex(Fermentable.FIELDS.COLOR);
			Double color = new Double(cursor.getFloat(color_index));
			//Log.v("DB", "color=" + color);
			colorView.setBackgroundResource(Color.getColorResource(color.doubleValue()));
			//Log.v("DB", "color_hex=" + Color.getColorResource(color.doubleValue()));
			
	        v.setTag(new FermentableHolder(
	        								cursor.getInt(cursor.getColumnIndex(Fermentable.FIELDS.ID)),
	        								cursor.getString(cursor.getColumnIndex(Fermentable.FIELDS.NAME)),
	        								manufacturerView.getText().toString()
	        								));
	        
	        v.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(final View v) {
					// TODO Auto-generated method stub
					//Log.v("DB", "onLongClick()");

					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle(((FermentableHolder)v.getTag()).name);
					builder.setItems(getResources().getStringArray(R.array.fermentableModifyList), new DialogInterface.OnClickListener() {
					    public void onClick(DialogInterface dialog, int item) {
					        
					    	switch (item) {
						    	case 0: 
						    		// Edit
						    		break;
						    	case 1: 
						    		// Delete
						    		if (db.deleteFermentable(((FermentableHolder)v.getTag()).id)) {
						    			Toast.makeText(context.getApplicationContext(), getString(R.string.fermentables_item_deleted), Toast.LENGTH_SHORT).show();
						    			cFermentables.requery();
//						    	        setListAdapter(new FermentablesAdapter(this, cFermentables));	
						    		} else {
						    			Toast.makeText(context.getApplicationContext(), getString(R.string.fermentables_error_item_deleted), Toast.LENGTH_SHORT).show();
						    		}
						    		break;
					    	}
					    }
					});
					AlertDialog alert = builder.create();
					alert.show();
					return false;
				} });
	        
		}

		/* (non-Javadoc)
		 * @see android.widget.SimpleCursorAdapter#newView(android.content.Context, android.database.Cursor, android.view.ViewGroup)
		 */
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {

			LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			return vi.inflate(R.layout.fermentables_list_item, null);
		}

		private final class FermentableHolder {

			public int id = -1;
			public String name = new String();
			public String description = new String();
			
			/**
			 * @param id
			 * @param name
			 * @param description
			 */
			public FermentableHolder(int id, String name, String description) {
				super();
				this.id = id;
				this.name = name;
				this.description = description;
			
				
			}

			/* (non-Javadoc)
			 * @see java.lang.Object#toString()
			 */
			@Override
			public String toString() {
				// TODO Auto-generated method stub
				return name + " - " + description;
			}
		}
		
	}

}
