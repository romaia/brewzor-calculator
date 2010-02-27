/*
    This file is part of Brewzor.

    Copyright (C) 2010 James Whiddon

    Brewzor is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Brewzor is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Brewzor.  If not, see <http://www.gnu.org/licenses/>.

*/
package com.brewzor.converters;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;

public abstract class Unit<T> {

	protected SharedPreferences prefs;
	
	static protected String UNKNOWN = "UNKNOWN";
	private T type;
	
	protected Context context;
	private double value;
	private int precision = 2;
	
	/**
	 * @param value
	 * @param prefs
	 */
	public Unit(double value, T type, int precision, Context c, SharedPreferences prefs) {				
		this.context = c;
		this.precision = precision;
		setValue(value);
		setType(type);
		setPrefs(prefs);
	}

	/**
	 * @return the value
	 */
	public final double getValue(final int p) {
		return (((double)((int)((value * Math.pow(10, p))))) / Math.pow(10, p));
	}
	
	/**
	 * @return the value
	 */
	public final Double getValue() {
		return getValue(precision);
	}
	
	public final double getRawValue() {
		return value;
	}
	
	public final void setPrecision(int p) {
		this.precision = p;
	}
	
	/**
	 * @param value the value to set
	 */
	public final void setValue(double value) {
		this.value = value;
	}
	
	/**
	 * @param e EditText 
	 * @param defaultvalue double default value 
	 */
	public final void setValue(EditText e, double defaultValue) {
		setValue(e.getText().toString(), defaultValue);
	}
	
	/**
	 * @param doubleString the double value to set as a String
	 * @param defaultvalue double default value 
	 */
	public final void setValue(String doubleString, double defaultValue)  {
		double fieldValue;
		try {
			fieldValue = Double.valueOf(doubleString);
			setValue(fieldValue);
		} catch (NumberFormatException e) {
			setValue(defaultValue);
		}
	};
	
	/**
	 * @return the prefs
	 */
	public final SharedPreferences getPrefs() {
		return prefs;
	}

	/**
	 * @param prefs the prefs to set
	 */
	public final void setPrefs(SharedPreferences prefs) {
		this.prefs = prefs;
	}

	/**
	 * @return the formatted value as a String
	 */
	@Override
	public final String toString() {
		return toString(precision);
	}

	/**
	 * @param format String to use for formatting instead of this.format 
	 * @return the formatted value as a String
	 */
	public final String toString(int p) {
		if (p == 0) return Integer.toString((int)getValue(p));
		return Double.toString(getValue(p));
	}

	/**
	 * @return the type
	 */
	public final T getType() {
		return type;
	}
	
	/**
	 * @param the generic type T
	 */
	public final void setType(T toType) {
		type = toType;
	}

	/**
	 * @param the generic type T
	 */
	public abstract void setType(String toType);

	/**
	 * @param toType String as stored in the preferences 
	 */
	public final void setTypeFromPref(String toType) {		
		setType(typeFromPref(toType, type));
	}
	
	/**
	 * @param the generic type T
	 */
	public final void convert(T toType) {
		setValue(compare(toType));
		setType(toType);
	}
	
	public abstract double compare(T toType);	
	public abstract T typeFromPref(String toType, T defaultUnit);
	public abstract String getLabel();
	public abstract String getLabelPlural();
	public abstract String getLabelAbbr();

}
