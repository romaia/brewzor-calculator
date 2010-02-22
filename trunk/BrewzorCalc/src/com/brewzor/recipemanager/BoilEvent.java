package com.brewzor.recipemanager;

public class BoilEvent extends Object implements Comparable {
	/**
	 * @param time
	 * @param pause
	 * @param description
	 */
	public BoilEvent(Integer boilLength, Integer time, Boolean pause, String description, String format) {
		super();
		this.time = time;
		this.pause = pause;
		this.description = description;
		this.format = format;
		this.boilLength = boilLength;
	}
	private Integer time;
	private Boolean pause;
	private String description;
	static private Integer boilLength = 0;
	private String format;
	/**
	 * @return the time
	 */
	public final Integer getTime() {
		return time;
	}
	/**
	 * @param time the time to set
	 */
	public final void setTime(Integer time) {
		this.time = time;
	}
	/**
	 * @return the pause
	 */
	public final Boolean getPause() {
		return pause;
	}
	/**
	 * @param pause the pause to set
	 */
	public final void setPause(Boolean pause) {
		this.pause = pause;
	}
	/**
	 * @return the description
	 */
	public final String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public final void setDescription(String description) {
		this.description = description;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((pause == null) ? 0 : pause.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BoilEvent other = (BoilEvent) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (pause == null) {
			if (other.pause != null)
				return false;
		} else if (!pause.equals(other.pause))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format(format, time.intValue(), description);
		//return "BoilEvent [description=" + description + ", pause=" + pause
		//		+ ", time=" + time + "]";
	}
	/**
	 * @return the boilLength
	 */
	public static final Integer getBoilLength() {
		return boilLength;
	}
	/**
	 * @param boilLength the boilLength to set
	 */
	public static final void setBoilLength(Integer boilLength) {
		BoilEvent.boilLength = boilLength;
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
	@Override
	public int compareTo(Object another) {
		// TODO Auto-generated method stub
		//a negative integer if this instance is less than another; 
		//a positive integer if this instance is greater than another; 
		//0 if this instance has the same order as another.
		BoilEvent other = (BoilEvent) another;
		if (this.getTime() < other.getTime()) return -1;
		if (this.getTime() > other.getTime()) return 1;
		return 0;
	}
}
