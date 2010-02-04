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
package com.brewzor.calculator;

public class UnitListItem {

	/**
	 * @param value
	 * @param label
	 */
	public UnitListItem(String value, String label) {
		super();
		this.value = value;
		this.label = label;
	}
	private String value;
    private String label;
    
	/**
	 * @return the value
	 */
	public final String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public final void setValue(String value) {
		this.value = value;
	}
	/**
	 * @return the label
	 */
	public final String getLabel() {
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public final void setLabel(String label) {
		this.label = label;
	}
    
}
