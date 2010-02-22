package com.brewzor.recipemanager;
/*
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.brewzor.utils.NumberFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class HopScheduleEntryActivity extends Activity {
	
	private SharedPreferences prefs;
	
	private Button SaveButton; 
	private Button CancelButton; 
	
    private List<Integer> BoilTimers = new ArrayList<Integer>(); 
    private List<Integer> MashTimers = new ArrayList<Integer>(); 
    private BoilTimerAdapter BoilAdapter; 
    private MashTimerAdapter MashAdapter; 
    private LinearLayout boilList; 
    private LinearLayout mashList; 
    
    private EditText nameEntry;

    private ImageView AddBoilEventButton;
    private ImageView AddMashEventButton;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_hop_schedule);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
	
        nameEntry = (EditText) findViewById(R.id.nameEntry);

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
        
        SaveButton = (Button) findViewById(R.id.save_schedule);
        SaveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				saveSchedule();
			}
        });

        CancelButton = (Button) findViewById(R.id.cancel_schedule);
        CancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
        });
        
        BoilAdapter = new BoilTimerAdapter(getBaseContext(), R.layout.hop_schedule_list_item, BoilTimers); 
        boilList = (TableLayout) findViewById(R.id.boilList); 
        boilList.set(BoilAdapter);         
        BoilAdapter.notifyDataSetChanged(); 
   
        MashAdapter = new MashTimerAdapter(getBaseContext(), R.layout.hop_schedule_list_item, MashTimers); 
        mashList = (ListView) findViewById(R.id.mashList); 
        mashList.setAdapter(MashAdapter);         
        MashAdapter.notifyDataSetChanged(); 
   
	}

	private void saveSchedule() {
		if (nameEntry.length() < 1) {
			Toast.makeText(this, getString(R.string.brew_schedule_error_name), Toast.LENGTH_LONG).show();
			return;
		}
		
		if (BoilTimers.size() < 1) {
			Toast.makeText(this, getString(R.string.brew_schedule_error_empty), Toast.LENGTH_LONG).show();
			return;
		}
		
		
	}
	
	private void deleteBoilEvent(Integer time) {
		if (BoilTimers.contains(time) && time.intValue() >= 0) {
			BoilTimers.remove(time);
	        BoilAdapter.notifyDataSetChanged(); 
			Log.v("deleteBoilEvent()", "size=" + BoilTimers.size());
		}
	}
	
	private void addBoilEvent(Integer time) {
		if (!BoilTimers.contains(time) && time.intValue() >= 0) {
			BoilTimers.add(time);
			Collections.sort(BoilTimers, Collections.reverseOrder());
			BoilAdapter.notifyDataSetChanged(); 
			Log.v("deleteBoilEvent()", "size=" + BoilTimers.size());
		}
	}
	
	private void deleteMashEvent(Integer time) {
		if (MashTimers.contains(time) && time.intValue() >= 0) {
			MashTimers.remove(time);
	        MashAdapter.notifyDataSetChanged(); 
			Log.v("deleteMashEvent()", "size=" + MashTimers.size());
		}
	}
	
	private void addMashEvent(Integer time) {
		if (!MashTimers.contains(time) && time.intValue() >= 0) {
			MashTimers.add(time);
			Collections.sort(MashTimers, Collections.reverseOrder());
			MashAdapter.notifyDataSetChanged(); 
			Log.v("deleteMashEvent()", "size=" + MashTimers.size());
		}
	}
	
	private void addBoilEventDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle(getString(R.string.brew_schedule_add_dialog_title));
		alert.setMessage(getString(R.string.brew_schedule_add_dialog_message));

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_NUMBER);
		alert.setView(input);

		alert.setPositiveButton(getString(R.string.brew_schedule_add_hop), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Editable value = input.getText();
				// Do something with value!
				addBoilEvent(NumberFormat.parseInt(value.toString(), -1));
				Log.v("AddHop", value.toString());		  
			}
		});

		alert.setNegativeButton(getString(R.string.brew_schedule_cancel), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});

		alert.show();

	}
	
	private void addMashEventDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle(getString(R.string.brew_schedule_add_dialog_title));
		alert.setMessage(getString(R.string.brew_schedule_add_dialog_message));

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_NUMBER);
		alert.setView(input);

		alert.setPositiveButton(getString(R.string.brew_schedule_add_hop), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Editable value = input.getText();
				// Do something with value!
				addMashEvent(NumberFormat.parseInt(value.toString(), -1));
				Log.v("AddMashEvent", value.toString());		  
			}
		});

		alert.setNegativeButton(getString(R.string.brew_schedule_cancel), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});

		alert.show();

	}
	
	public class BoilTimerAdapter extends ArrayAdapter<Integer> { 
	     int resource; 
	      
	     public BoilTimerAdapter(Context _cont, int _resource, List<Integer> _items) { 
	          super (_cont, _resource, _items); 
	          resource = _resource; 
	     } 

	     
	     @Override 
	     public View getView(int position, View convertView, ViewGroup parent) { 
	          LinearLayout layout; 
	           
	          Integer time = getItem(position); 
	          if (convertView == null) { 
	               layout = new LinearLayout(getContext()); 
	               LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
	               vi.inflate(resource, layout, true); 
	          } else { 
	               layout = (LinearLayout)convertView; 
	          } 

	          TextView description = (TextView)layout.findViewById(R.id.description); 
	          description.setText(getString(R.string.brew_schedule_list_item_format, time.toString())); 

	          ImageView image = (ImageView)layout.findViewById(R.id.delete);
	          image.setTag(time);
	          image.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Integer time = (Integer) v.getTag();
						if (BoilTimers.contains(time)) {
		  					deleteBoilEvent(time);
		  				}
					} });

	          
	          return layout; 
	     } 
	}
	
	public class MashTimerAdapter extends ArrayAdapter<Integer> { 
	     int resource; 
	      
	     public MashTimerAdapter(Context _cont, int _resource, List<Integer> _items) { 
	          super (_cont, _resource, _items); 
	          resource = _resource; 
	     } 

	     
	     @Override 
	     public View getView(int position, View convertView, ViewGroup parent) { 
	          LinearLayout layout; 
	           
	          Integer time = getItem(position); 
	          if (convertView == null) { 
	               layout = new LinearLayout(getContext()); 
	               LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
	               vi.inflate(resource, layout, true); 
	          } else { 
	               layout = (LinearLayout)convertView; 
	          } 

	          TextView description = (TextView)layout.findViewById(R.id.description); 
	          description.setText(getString(R.string.brew_schedule_list_item_format, time.toString())); 

	          ImageView image = (ImageView)layout.findViewById(R.id.delete);
	          image.setTag(time);
	          image.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Integer time = (Integer) v.getTag();
						if (MashTimers.contains(time)) {
		  					deleteMashEvent(time);
		  				}
					} });

	          
	          return layout; 
	     } 
	}
	
}
*/