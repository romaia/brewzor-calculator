package com.brewzor.converters;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;

import com.brewzor.calculator.preferences.Preferences;
import com.brewzor.utils.NumberFormat;

public class WaterGrainRatio extends Object {
	/**
	 * @param volume
	 * @param mass
	 */
	public WaterGrainRatio(Volume volume, Mass mass) {
		super();
		this.volume = volume;
		this.mass = mass;
		
	}
	
	public WaterGrainRatio(EditText edit, Volume.Unit v, Mass.Unit m, Context context, SharedPreferences prefs) {
		this.volume = new Volume(NumberFormat.parseDouble(edit.getText().toString(), -1), v, context, prefs);
		this.mass = new Mass(1.0, m, context, prefs);
	}
	private Mass mass;
	private Volume volume;
	private String format = new String("%1.2f %s/%s"); 
	
	public final void setMassType(Mass.Unit unit) {
		mass.setValue(1);
		mass.convert(unit);
		volume.setValue(volume.getValue() / mass.getValue());
		mass.setValue(1);
	}
	
	public final void setVolumeType(Volume.Unit unit) {
		volume.convert(unit);
	}
	
	public final double getValue() {
		return volume.getValue();
	}
	
	/**
	 * @return the mass
	 */
	public final Mass getMass() {
		return mass;
	}
	/**
	 * @param mass the mass to set
	 */
	public final void setMass(Mass mass) {
		this.mass = mass;
	}
	/**
	 * @return the volume
	 */
	public final Volume getVolume() {
		return volume;
	}
	/**
	 * @param volume the volume to set
	 */
	public final void setVolume(Volume volume) {
		this.volume = volume;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format(format, volume.getValue(), volume.getLabelAbbr(), mass.getLabelAbbr());
	}

	/**
	 * @return the format
	 */
	public final String getFormat() {
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public final void setFormat(String format) {
		this.format = format;
	}
}
