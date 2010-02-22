package com.brewzor.recipemanager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.brewzor.calculator.R.array;
import com.brewzor.calculator.R.id;
import com.brewzor.calculator.R.layout;
import com.brewzor.calculator.R.string;
import com.brewzor.calculator.preferences.Preferences;
import com.brewzor.calculator.R;
import com.brewzor.converters.Mass;
import com.brewzor.converters.Temperature;
import com.brewzor.converters.Volume;
import com.brewzor.converters.WaterGrainRatio;
import com.brewzor.utils.NumberFormat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class CreateRecipeActivity extends Activity {
	
	private SharedPreferences prefs;
	
	private Button SaveButton; 
	private Button CancelButton;
	
    private List<BoilEvent> BoilTimers = new ArrayList<BoilEvent>(); 
    private List<MashEvent> MashTimers = new ArrayList<MashEvent>(); 
    private LinearLayout boilList; 
    private LinearLayout mashList; 
    
    private EditText nameEntry;
    private EditText boilDurationEntry;
    
    private ImageView AddMashEventButton;
    private ImageView AddBoilEventButton;
    
    private Temperature temperatureBoiling;
    private Temperature.Unit temperatureUnit = Temperature.Unit.FAHRENHEIT;
    private Mass grainWeight;
    private Mass.Unit grainMassUnit = Mass.Unit.POUND;
    private Volume volume;
    private Volume.Unit volumeUnit = Volume.Unit.GALLON;
    
	DBAdapter db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        db = new DBAdapter(this);
        db.open();
        
        temperatureBoiling = new Temperature(100.0, Temperature.Unit.CELSIUS, getBaseContext(), prefs);
        temperatureBoiling.convert(temperatureUnit);
        
        grainWeight = new Mass(0, grainMassUnit, getBaseContext(), prefs);
        volume = new Volume(0, volumeUnit, getBaseContext(), prefs);
        
        nameEntry = (EditText) findViewById(R.id.nameEntry);
        boilDurationEntry = (EditText) findViewById(R.id.boilDurationEntry);

        AddBoilEventButton = (ImageView) findViewById(R.id.add_boil_event_button);
        AddBoilEventButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				addBoilEventDialog();
			}
        });
        
        AddMashEventButton = (ImageView) findViewById(R.id.add_mash_event_button);
        AddMashEventButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				addMashEventDialog();
			}
        });
        
        mashList = (LinearLayout) findViewById(R.id.mashList); 
        boilList = (LinearLayout) findViewById(R.id.boilList); 
   
        getPrefs();
        
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	
	private void getPrefs() {
        boilDurationEntry.setText(prefs.getString(Preferences.BATCH_BOIL_MINUTES, "60"));
        temperatureUnit = temperatureBoiling.typeFromPref(Preferences.GLOBAL_TEMPERATURE_UNIT, Temperature.Unit.FAHRENHEIT);
        grainMassUnit = grainWeight.typeFromPref(Preferences.BATCH_GRAIN_MASS_UNIT, Mass.Unit.POUND);
        volumeUnit = volume.typeFromPref(Preferences.BATCH_VOLUME_UNIT, Volume.Unit.GALLON);
	}	

	
	private void saveSchedule() {
		if (nameEntry.length() < 1) {
			Toast.makeText(this, getString(R.string.recipe_error_name), Toast.LENGTH_LONG).show();
			return;
		}
		
		if (boilDurationEntry.length() < 1) {
			Toast.makeText(this, getString(R.string.recipe_error_boil_duration), Toast.LENGTH_LONG).show();
			return;
		}

		long schedule_id;
		long id;
		
		schedule_id = db.insertBrewSession(nameEntry.getText().toString(), new Integer(NumberFormat.parseInt(boilDurationEntry.getText().toString(), -1)));
		if (schedule_id < 0) {
			Toast.makeText(this, getString(R.string.recipe_error_database), Toast.LENGTH_LONG).show();
			return;
		}
		Log.v("save", "schedule_id=" + schedule_id);

		Iterator<MashEvent> iterator = MashTimers.iterator();
		while ( iterator.hasNext() ){
			MashEvent e = iterator.next();
			//id = db.insertMashEvent(new Integer((int)schedule_id), e.getDuration(), e.getTemperature(), e.getTemperature(), e.getTemperature());
			id = 1;
			if (id < 0) {
				Toast.makeText(this, getString(R.string.recipe_error_database), Toast.LENGTH_LONG).show();
				return;
			}

			Log.v("save", "mash_event_id=" + id);
		}

		Iterator<BoilEvent> biterator = BoilTimers.iterator();
		while ( biterator.hasNext() ){
			BoilEvent be = biterator.next();
			id = db.insertBoilEvent(new Integer((int)schedule_id), be.getTime(), be.getPause(), be.getDescription());
			
			if (id < 0) {
				Toast.makeText(this, getString(R.string.recipe_error_database), Toast.LENGTH_LONG).show();
				return;
			}

			Log.v("save", "mash_event_id=" + id);
		}

		BoilTimers.clear();
		MashTimers.clear();
		
		finish();
	}
	
	private void deleteBoilEvent(BoilEvent e) {
		Log.v("deleteBoilEvent()", e.toString());
		if (BoilTimers.contains(e) && e.getTime() >= 0) {
			boilList.removeViewAt(BoilTimers.indexOf(e));
			BoilTimers.remove(e);
			Log.v("deleteBoilEvent()", "size=" + BoilTimers.size());
		}
	}
	
	private boolean addBoilEvent(EditText timeEntry, CheckBox pauseEntry, EditText descriptionEntry) {
		BoilEvent event = new BoilEvent(	new Integer(NumberFormat.parseInt(boilDurationEntry.getText().toString(), -1)),
											new Integer(NumberFormat.parseInt(timeEntry.getText().toString(), -1)),
											new Boolean(pauseEntry.isChecked()),
											("" + descriptionEntry.getText().toString()).trim(),
											getString(R.string.recipe_boil_list_item_format));

		if (event.getTime() < 0) {
			Toast.makeText(this, getString(R.string.recipe_error_boil_time), Toast.LENGTH_LONG).show();
			return false;
		}

		if (BoilTimers.contains(event)) {
			Toast.makeText(this, getString(R.string.recipe_error_boil_event_exists), Toast.LENGTH_LONG).show();
			return false;
		}

		if (event.getTime() > BoilEvent.getBoilLength()) {
			Toast.makeText(this, getString(R.string.recipe_error_boil_time_over), Toast.LENGTH_LONG).show();
			return false;
		}

		LinearLayout layout = new LinearLayout(this); 
		LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		vi.inflate(R.layout.recipe_list_item, layout, true); 

		TextView description = (TextView)layout.findViewById(R.id.description); 
		description.setText(event.toString()); 

		ImageView image = (ImageView)layout.findViewById(R.id.delete);
		image.setTag(event);
		image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BoilEvent e = (BoilEvent) v.getTag();
				deleteBoilEvent(e);
				Log.v("onClick()", e.toString());
			} });



		BoilTimers.add(event);
		Collections.sort(BoilTimers, Collections.reverseOrder());
		boilList.addView(layout, BoilTimers.indexOf(event));
		Log.v("addBoilEvent()", "size=" + BoilTimers.size());
		return true;
	}
	
	private void addBoilEventDialog() {
		final Dialog alert = new Dialog(this);	
		
		alert.setContentView(R.layout.dialog_add_boil_event);
		alert.setTitle(getString(R.string.recipe_add_boil_event_title));

		final EditText timeEntry = (EditText) alert.findViewById(R.id.time);
		final CheckBox pauseCheck = (CheckBox) alert.findViewById(R.id.pause);
		final EditText descriptionEntry = (EditText) alert.findViewById(R.id.description);
		final Button cancel = (Button) alert.findViewById(R.id.cancel);
		final Button save = (Button) alert.findViewById(R.id.save);

		save.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (addBoilEvent(	timeEntry, 
									pauseCheck, 
									descriptionEntry)) 
					alert.dismiss();
			}
		});

		cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Canceled.
				alert.dismiss();
			}
		});

		alert.show();

	}
	
	private void deleteMashEvent(MashEvent e) {
		if (MashTimers.contains(e)) {
			mashList.removeViewAt(MashTimers.indexOf(e));
			MashTimers.remove(e);
			Log.v("deleteMashEvent()", "size=" + MashTimers.size());
		}
	}
	
	private boolean addMashEvent(Spinner stepType, EditText name, EditText duration, EditText temperature, EditText waterGrainRatio) {

		MashEvent event = new MashEvent(	getResources().getStringArray(R.array.mashStepTypesIdList)[stepType.getSelectedItemPosition()],
											("" + name.getText().toString()).trim(),
											new Integer(NumberFormat.parseInt(duration.getText().toString(), -1)),
											new Temperature(NumberFormat.parseDouble(temperature.getText().toString(), -1), temperatureUnit, getBaseContext(), prefs),
											new WaterGrainRatio(waterGrainRatio, volumeUnit, grainMassUnit, getBaseContext(), prefs),
											1);
		
		if (event.getName().length() < 1) {
			Toast.makeText(this, getString(R.string.recipe_error_mash_name), Toast.LENGTH_LONG).show();
			return false;
		}
		
		if (event.getDuration() < 1) {
			Toast.makeText(this, getString(R.string.recipe_error_mash_duration), Toast.LENGTH_LONG).show();
			return false;
		}
		
		if (event.getRatio().getValue() < 0) {
			Toast.makeText(this, getString(R.string.recipe_error_mash_ratio), Toast.LENGTH_LONG).show();
			return false;
		}
		
		if (event.getTemperature().getValue() < 0) {
			Toast.makeText(this, getString(R.string.recipe_error_mash_temperature), Toast.LENGTH_LONG).show();
			return false;
		}
		
		if (event.getTemperature().getValue() > temperatureBoiling.getValue()) {
			Toast.makeText(this, getString(R.string.recipe_error_mash_too_hot), Toast.LENGTH_LONG).show();
			return false;
		}
		
		if (MashTimers.contains(event)) {
			Toast.makeText(this, getString(R.string.recipe_error_mash_step_exists), Toast.LENGTH_LONG).show();
			return false;
		}
		
		LinearLayout layout = new LinearLayout(this); 
		LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		vi.inflate(R.layout.recipe_list_item, layout, true); 

		TextView descriptionView = (TextView)layout.findViewById(R.id.description); 
		descriptionView.setText(event.toString()); 

		ImageView image = (ImageView)layout.findViewById(R.id.delete);
		image.setTag(event);
		image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MashEvent e = (MashEvent) v.getTag();
				deleteMashEvent(e);
			} });

		MashTimers.add(event);
		//Collections.sort(MashTimers, Collections.reverseOrder());
		mashList.addView(layout, MashTimers.indexOf(event));
		Log.v("addMashEvent()", event.toString());		  
		Log.v("addMashEvent()", "index=" + MashTimers.indexOf(event));		  
		Log.v("addMashEvent()", "size=" + MashTimers.size());

		return true;
		
	}
	
	private void addMashEventDialog() {
		final Dialog alert = new Dialog(this);	
		
		alert.setContentView(R.layout.dialog_add_mash_event);
		alert.setTitle(getString(R.string.recipe_add_mash_event_title));

		final EditText nameEntry = (EditText) alert.findViewById(R.id.name);
		final EditText durationEntry = (EditText) alert.findViewById(R.id.duration);
		final EditText temperatureEntry = (EditText) alert.findViewById(R.id.temperature);
		final TextView temperatureUnit = (TextView) alert.findViewById(R.id.temperatureUnit);
		temperatureUnit.setText(temperatureBoiling.getLabelAbbr());
		final EditText waterGrainRatio = (EditText) alert.findViewById(R.id.water_to_grain_ratio);
		final Button cancel = (Button) alert.findViewById(R.id.cancel);
		final Button save = (Button) alert.findViewById(R.id.save);

		final Spinner StepTypeSpinner = (Spinner) alert.findViewById(R.id.step_type);
        StepTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				MashEvent.Type type = MashEvent.typeFromString(getResources().getStringArray(R.array.mashStepTypesIdList)[StepTypeSpinner.getSelectedItemPosition()]);
				
				Log.v("onItemSelected()", getResources().getStringArray(R.array.mashStepTypesIdList)[StepTypeSpinner.getSelectedItemPosition()]);

				switch (type) {
		    	case INFUSION:
			    	Log.v("onItemSelected()", "INFUSION");
		    	case TEMPERATURE:
			    	Log.v("onItemSelected()", "TEMPERATURE");
		    		waterGrainRatio.setEnabled(true);
		    		waterGrainRatio.setFocusable(true);
		    		break;
		    	case DECOCTION:
			    	Log.v("onItemSelected()", "DECOCTION");
		    		waterGrainRatio.setEnabled(false);
		    		waterGrainRatio.setFocusable(false);
		    		break;
		    	default:
			    	Log.v("onItemSelected()", "UNKNOWN");

		    	}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) { }
        });

		save.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (addMashEvent(StepTypeSpinner, nameEntry, durationEntry, temperatureEntry, waterGrainRatio)) alert.dismiss();
			}
		});

		cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Canceled.
				alert.dismiss();
			}
		});

		alert.show();
	}
			
}
