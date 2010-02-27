package com.brewzor.converters;

import android.content.Context;
import android.content.SharedPreferences;

import com.brewzor.calculator.R;
import com.brewzor.calculator.preferences.Preferences;

// http://en.wikipedia.org/wiki/%C2%B0Lintner
public final class DiastaticPower extends Unit<com.brewzor.converters.DiastaticPower.Unit> {

	public enum Unit {
		LINTNER,
		WINDISCH_KOLBACH 
	};
	
	public DiastaticPower(double value, Unit type, Context context, SharedPreferences prefs) {
		super(value, type, Preferences.DOUBLE_PRECISION, context, prefs);
	}

	@Override
	public double compare(Unit toType) {
		switch (getType()) {
			case LINTNER:			return LintnerToUnits(getValue(), toType);
			case WINDISCH_KOLBACH:	return WindischKolbachToUnits(getValue(), toType);
			default:				return getValue();
		}
	}

	@Override
	public String getLabel() {
		switch (getType()) {
			case LINTNER:			return context.getString(R.string.lintner);
			case WINDISCH_KOLBACH:	return context.getString(R.string.windisch_kolbach);
			default:				return UNKNOWN;
		}
	}

	@Override
	public String getLabelAbbr() {
		switch (getType()) {
			case LINTNER:			return context.getString(R.string.lintner_abbr);
			case WINDISCH_KOLBACH:	return context.getString(R.string.windisch_kolbach_abbr);
			default:				return UNKNOWN;
		}
	}

	@Override
	public String getLabelPlural() {
		switch (getType()) {
			case LINTNER:			return context.getString(R.string.lintner_plural);
			case WINDISCH_KOLBACH:	return context.getString(R.string.windisch_kolbach_plural);
			default:				return UNKNOWN;
		}
	}

	@Override
	public void setType(String toType) {
		for (DiastaticPower.Unit unit : DiastaticPower.Unit.values()) {
			//Log.v("DiastaticPowerUnit", "name: " + unit.name() + " newType: " + newType);
			if (unit.name().equals(toType)){
				//Log.v("DiastaticPowerUnit", "type=" + unit.name());
				setType(unit);
			}
		}
	}

	@Override
	public Unit typeFromPref(String toType, Unit defaultUnit) {
		String newType = prefs.getString(toType, UNKNOWN);
		for (DiastaticPower.Unit unit : DiastaticPower.Unit.values()) {
			//Log.v("DiastaticPowerUnit", "name: " + unit.name() + " newType: " + newType);
			if (unit.name().equals(newType)){
				//Log.v("DiastaticPowerUnit", "type=" + unit.name());
				return unit;
			}
		}
		return defaultUnit;
	}

	static public final double LintnerToUnits(double value, DiastaticPower.Unit toType) {
		switch (toType) {
			case LINTNER:			return value;
			case WINDISCH_KOLBACH:	return (3.5 * value) - 16.0;
			default:				return value;
		}
	}

	static public final double WindischKolbachToUnits(double value, DiastaticPower.Unit toType) {
		switch (toType) {
			case LINTNER:			return (value + 16.0) / 3.5;
			case WINDISCH_KOLBACH:	return value;
			default:				return value;
		}
	}


}
