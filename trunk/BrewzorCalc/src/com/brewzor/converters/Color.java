package com.brewzor.converters;

import com.brewzor.calculator.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public final class Color extends Unit<com.brewzor.converters.Color.Unit> {

	public Color(double value, Unit type, String format, Context context,
			SharedPreferences prefs) {
		super(value, type, format, context, prefs);
		// TODO Auto-generated constructor stub
	}

	static public enum Unit {
		SRM,
		EBC
	}

	public final int getColorResource() {
		return getColorResource(compare(Unit.SRM));
	}
	
	static public final int getColorResource(double color) {
		int int_color = (int)color;
		if (int_color < 1) int_color = 1;
		switch (int_color) {
			case 1: return R.color.srm_1;
			case 2: return R.color.srm_2;
			case 3: return R.color.srm_3;
			case 4: return R.color.srm_4;
			case 5: return R.color.srm_5;
			case 6: return R.color.srm_6;
			case 7: return R.color.srm_7;
			case 8: return R.color.srm_8;
			case 9: return R.color.srm_9;
			case 10: return R.color.srm_10;
			case 11: return R.color.srm_11;
			case 12: return R.color.srm_12;
			case 13: return R.color.srm_13;
			case 14: return R.color.srm_14;
			case 15: return R.color.srm_15;
			case 16: return R.color.srm_16;
			case 17: return R.color.srm_17;
			case 18: return R.color.srm_18;
			case 19: return R.color.srm_19;
			case 20: return R.color.srm_20;
			case 21: return R.color.srm_21;
			case 22: return R.color.srm_22;
			case 23: return R.color.srm_23;
			case 24: return R.color.srm_24;
			case 25: return R.color.srm_25;
			case 26: return R.color.srm_26;
			case 27: return R.color.srm_27;
			case 28: return R.color.srm_28;
			case 29: return R.color.srm_29;
			case 30: return R.color.srm_30;
			case 31: return R.color.srm_31;
			case 32: return R.color.srm_32;
			case 33: return R.color.srm_33;
			case 34: return R.color.srm_34;
			case 35: return R.color.srm_35;
			case 36: return R.color.srm_36;
			case 37: return R.color.srm_37;
			case 38: return R.color.srm_38;
			case 39: return R.color.srm_39;
			case 40: return R.color.srm_40;
			default: return R.color.srm_41;

		}
			
	}
	
	@Override
	public double compare(Unit toType) {
		switch (getType()) {
			case SRM:	return SRMToUnits(getValue(), toType);
			case EBC:	return EBCToUnits(getValue(), toType);
			default:	return getValue();
		}
	}

	@Override
	public String getLabel() {
		switch (getType()) {
			case SRM:	return context.getString(R.string.srm);
			case EBC:	return context.getString(R.string.ebc);
			default:	return UNKNOWN;
		}
	}

	@Override
	public String getLabelAbbr() {
		switch (getType()) {
			case SRM:	return context.getString(R.string.srm_abbr);
			case EBC:	return context.getString(R.string.ebc_abbr);
			default:	return UNKNOWN;
		}
	}

	@Override
	public String getLabelPlural() {
		switch (getType()) {
			case SRM:	return context.getString(R.string.srm_plural);
			case EBC:	return context.getString(R.string.ebc_plural);
			default:	return UNKNOWN;
		}
	}

	@Override
	public void setType(String toType) {
		for (Color.Unit unit : Color.Unit.values()) {
			//Log.v("ColorUnit", "name: " + unit.name() + " newType: " + newType);
			if (unit.name().equals(toType)){
				//Log.v("ColorUnit", "type=" + unit.name());
				setType(unit);
			}
		}
	}

	@Override
	public Color.Unit typeFromPref(String toType, Unit defaultUnit) {
		String newType = prefs.getString(toType, UNKNOWN);
		for (Color.Unit unit : Color.Unit.values()) {
			//Log.v("ColorUnit", "name: " + unit.name() + " newType: " + newType);
			if (unit.name().equals(newType)){
				//Log.v("ColorUnit", "type=" + unit.name());
				return unit;
			}
		}
		return defaultUnit;
	}

	static public final double SRMToUnits(double value, Color.Unit toType) {
		switch (toType) {
			case SRM:	return value;
			case EBC:	return 1.97 * value;
			default:	return value;
		}
	}
	
	static public final double EBCToUnits(double value, Color.Unit toType) {
		switch (toType) {
			case SRM:	return value / 1.97;
			case EBC:	return value;
			default:	return value;
		}
	}
	
}
