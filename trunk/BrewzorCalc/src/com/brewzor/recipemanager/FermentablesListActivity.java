package com.brewzor.recipemanager;

import com.brewzor.calculator.OptionsMenuHandler;
import com.brewzor.calculator.R;
import com.brewzor.calculator.preferences.Preferences;
import com.brewzor.converters.BeerColor;
import com.brewzor.converters.Volume;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

public class FermentablesListActivity extends ListActivity {

	private SharedPreferences prefs;

	protected DBHelper db;
	protected Cursor cFermentables;

	private BeerColor.Unit colorUnit = BeerColor.Unit.SRM;
	private BeerColor grainColor; 
	private String colorLabel;

	private final static String CRLF = new String("\n");

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getString(R.string.window_title_format, getString(R.string.app_name), getString(R.string.fermentables)));
		setContentView(R.layout.fermentables);

		prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

		grainColor = new BeerColor(0, BeerColor.Unit.SRM, getBaseContext(), prefs);
		grainColor.setPrecision(0);

		getPrefs();

		db = new DBHelper(this);
		db.open();
		cFermentables = db.getAllFermentables();
		setListAdapter(new FermentablesAdapter(this, cFermentables));	
	}

	/* Creates the menu items */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return OptionsMenuHandler.createFermentableMenu(this, menu);
	}

	/* Handles item selections */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return OptionsMenuHandler.showMenu(this, item);
	}

	@Override
	protected void onDestroy() {
		cFermentables.close();
		db.close();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		getPrefs();
		cFermentables.requery();
		super.onResume();
	}

	private void getPrefs() {
		colorUnit = grainColor.typeFromPref(Preferences.GLOBAL_BEER_COLOR_UNIT, BeerColor.Unit.SRM);
		colorLabel = CRLF + grainColor.getLabelAbbr();
	}

	public final class FermentablesAdapter extends SimpleCursorAdapter {

		private Context context; 
		private BeerColor color;
		private LayoutInflater inflater;

		private TextView nameView;
		private TextView manufacturerView;
		private TextView colorView;

		private int id_idx = -1;
		private int name_idx = -1;
		private int manufacturer_idx = -1;
		private int color_idx = -1;

		public FermentablesAdapter(Context context, Cursor c) {
			super(context, R.layout.fermentables_list_item, c, Fermentable.aFields, Fermentable.aViews);
			this.context = context; 
			this.color = new BeerColor(0, BeerColor.Unit.SRM, context, prefs);
			inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			id_idx = c.getColumnIndex(Fermentable.FIELDS.ID);
			name_idx = c.getColumnIndex(Fermentable.FIELDS.NAME);
			manufacturer_idx = c.getColumnIndex(Fermentable.FIELDS.MANUFACTURER);
			color_idx = c.getColumnIndex(Fermentable.FIELDS.COLOR);

		}

		@Override
		public void bindView(View v, final Context context, Cursor cursor) {

			nameView = (TextView) v.findViewById(R.id.name); 
			nameView.setText(cursor.getString(name_idx)); 

			manufacturerView = (TextView) v.findViewById(R.id.manufacturer); 
			manufacturerView.setText(cursor.getString(manufacturer_idx)); 

			colorView = (TextView) v.findViewById(R.id.color);
			color.setValue(cursor.getFloat(color_idx));
			color.setType(BeerColor.Unit.SRM); // database is in SRM
			colorView.setBackgroundResource(BeerColor.getColorResource(color.getValue()));
			colorView.setTextColor(BeerColor.getTextColorResource(color.getValue()));
			color.convert(colorUnit);
			colorView.setText(color.toString() + colorLabel);

			v.setTag(new FermentableHolder(	cursor.getLong(id_idx),
					cursor.getString(name_idx) ));

			v.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(final View v) {
					//Log.v("DB", "onLongClick()");
					displayModifyDialog(v);
					return false;
				} });

		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			return inflater.inflate(R.layout.fermentables_list_item, null);
		}


		private void displayModifyDialog(final View v) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(((FermentableHolder)v.getTag()).name);
			builder.setItems(getResources().getStringArray(R.array.editDeleteList), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {

					switch (item) {
					case 0: 
						// Edit
						Intent i = new Intent(context, FermentableActivity.class);
						i.putExtra("com.brewzor.recipemanager.FermentableActivity.id", ((FermentableHolder)v.getTag()).id);
						context.startActivity(i);
						break;
					case 1: 
						// Delete
						if (db.deleteFermentable(((FermentableHolder)v.getTag()).id)) {
							Toast.makeText(context.getApplicationContext(), getString(R.string.fermentables_item_deleted), Toast.LENGTH_SHORT).show();
							cFermentables.requery();
						} else {
							Toast.makeText(context.getApplicationContext(), getString(R.string.fermentables_error_item_deleted), Toast.LENGTH_SHORT).show();
						}
						break;
					}
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		}

		private final class FermentableHolder {

			public long id = -1;
			public String name;

			/**
			 * @param id
			 * @param name
			 */
			public FermentableHolder(long id, String name) {
				super();
				this.id = id;
				this.name = name;
			}

		}

	}

}
