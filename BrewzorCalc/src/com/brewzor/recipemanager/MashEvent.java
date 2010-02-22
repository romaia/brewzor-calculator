package com.brewzor.recipemanager;

import com.brewzor.converters.Temperature;
import com.brewzor.converters.WaterGrainRatio;

public class MashEvent extends Object {
	public enum Type {
		INFUSION,
		DECOCTION,
		TEMPERATURE, 
		UNKNOWN
	};
	
	/**
	 * @param stepType
	 * @param name
	 * @param duration
	 * @param temperature
	 * @param ratio
	 */
	public MashEvent(String stepType, String name, Integer duration,
			Temperature temperature, WaterGrainRatio ratio, Integer displayOrder) {
		super();
		this.stepType = typeFromString(stepType);
		this.name = name;
		this.duration = duration;
		this.temperature = temperature;
		this.ratio = ratio;
		this.displayOrder = displayOrder;
	}

	private Type stepType = Type.UNKNOWN;
	private String name;
	private Integer duration;
	private Temperature temperature;
	private WaterGrainRatio ratio;
	private Integer displayOrder;
	static private String format = new String("%s ");

	public static MashEvent.Type typeFromString(String type) { 
		if (type.equals(Type.INFUSION.toString())) return Type.INFUSION;
		if (type.equals(Type.DECOCTION.toString())) return Type.DECOCTION;
		if (type.equals(Type.TEMPERATURE.toString())) return Type.TEMPERATURE;
		return Type.UNKNOWN;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format(format, temperature, temperature.getLabelAbbr(), duration);
	}

	/**
	 * @return the stepType
	 */
	public final MashEvent.Type getStepType() {
		return stepType;
	}

	/**
	 * @param stepType the stepType to set
	 */
	public final void setStepType(MashEvent.Type stepType) {
		this.stepType = stepType;
	}

	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public final void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the duration
	 */
	public final Integer getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public final void setDuration(Integer duration) {
		this.duration = duration;
	}

	/**
	 * @return the temperature
	 */
	public final Temperature getTemperature() {
		return temperature;
	}

	/**
	 * @param temperature the temperature to set
	 */
	public final void setTemperature(Temperature temperature) {
		this.temperature = temperature;
	}

	/**
	 * @return the ratio
	 */
	public final WaterGrainRatio getRatio() {
		return ratio;
	}

	/**
	 * @param ratio the ratio to set
	 */
	public final void setRatio(WaterGrainRatio ratio) {
		this.ratio = ratio;
	}

	/**
	 * @return the format
	 */
	public static final String getFormat() {
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public static final void setFormat(String format) {
		MashEvent.format = format;
	}

	/**
	 * @return the displayOrder
	 */
	public final Integer getDisplayOrder() {
		return displayOrder;
	}

	/**
	 * @param displayOrder the displayOrder to set
	 */
	public final void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

}
