package com.brewzor.recipemanager;

import com.brewzor.converters.Gravity;
import com.brewzor.converters.Mass;

public class Fermentable extends Object {

	static public enum Type {
		GRAIN,
		EXTRACT,
		SUGAR,
		ADJUNCT,
		DRY_EXTRACT,
		OTHER
	};

    public static final String TABLE = "fermentables";
    
    public final class FIELDS {
    	public static final String ID = "_id";
	    public static final String NAME = "name";
	    public static final String TYPE = "type";
	    public static final String MANUFACTURER = "manufacturer";
	    public static final String ORGANIC = "organic";
	    public static final String URL = "url";
	    public static final String POTENTIAL = "potential";
	    public static final String YIELD = "yield";
	    public static final String COARSE_FINE = "coarse_fine";
	    public static final String MOISTURE = "moisture";
	    public static final String COLOR = "color";
	    public static final String DIASTATIC_POWER = "diastatic_power";
	    public static final String PROTEIN = "protein";
	    public static final String MAX_IN_BATCH = "max_in_batch";    
	    public static final String ADD_AFTER_BOIL = "add_after_boil";
	    public static final String MUST_MASH = "must_mash";
	    public static final String NOTES = "notes";
	    public static final String USER_CREATED= "user_created";
    }
	
	private String name = new String("");
	private Fermentable.Type type = Type.OTHER;
	private String manufacturer = new String();
	private Boolean organic = new Boolean(false);
	private String url = new String();
	private Gravity potential;
	private Double yield = new Double(0);
	private Double coarseFine = new Double(0);
	private Double moisture = new Double(0);
	private Double color = new Double(0);
	private Double diastaticPower = new Double(0);
	private Double protein = new Double(0);
	private Double maxInBatch = new Double(0);
	private Integer addAfterBoil = new Integer(0);
	private Integer mustMash = new Integer(0);
	private String notes = new String();
	
	private Mass amount;
	
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
	 * @return the type
	 */
	public final Fermentable.Type getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public final void setType(Fermentable.Type type) {
		this.type = type;
	}

	/**
	 * @return the potential
	 */
	public final Gravity getPotential() {
		return potential;
	}

	/**
	 * @param potential the potential to set
	 */
	public final void setPotential(Gravity potential) {
		this.potential = potential;
	}

	/**
	 * @return the yield
	 */
	public final Double getYield() {
		return yield;
	}

	/**
	 * @param yield the yield to set
	 */
	public final void setYield(Double yield) {
		this.yield = yield;
	}

	/**
	 * @return the coarseFine
	 */
	public final Double getCoarseFine() {
		return coarseFine;
	}

	/**
	 * @param coarseFine the coarseFine to set
	 */
	public final void setCoarseFine(Double coarseFine) {
		this.coarseFine = coarseFine;
	}

	/**
	 * @return the moisture
	 */
	public final Double getMoisture() {
		return moisture;
	}

	/**
	 * @param moisture the moisture to set
	 */
	public final void setMoisture(Double moisture) {
		this.moisture = moisture;
	}

	/**
	 * @return the color
	 */
	public final Double getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public final void setColor(Double color) {
		this.color = color;
	}

	/**
	 * @return the diastaticPower
	 */
	public final Double getDiastaticPower() {
		return diastaticPower;
	}

	/**
	 * @param diastaticPower the diastaticPower to set
	 */
	public final void setDiastaticPower(Double diastaticPower) {
		this.diastaticPower = diastaticPower;
	}

	/**
	 * @return the protein
	 */
	public final Double getProtein() {
		return protein;
	}

	/**
	 * @param protein the protein to set
	 */
	public final void setProtein(Double protein) {
		this.protein = protein;
	}

	/**
	 * @return the maxInBatch
	 */
	public final Double getMaxInBatch() {
		return maxInBatch;
	}

	/**
	 * @param maxInBatch the maxInBatch to set
	 */
	public final void setMaxInBatch(Double maxInBatch) {
		this.maxInBatch = maxInBatch;
	}

	/**
	 * @return the addAfterBoil
	 */
	public final Integer getAddAfterBoil() {
		return addAfterBoil;
	}

	/**
	 * @param addAfterBoil the addAfterBoil to set
	 */
	public final void setAddAfterBoil(Integer addAfterBoil) {
		this.addAfterBoil = addAfterBoil;
	}

	/**
	 * @return the mustMash
	 */
	public final Integer getMustMash() {
		return mustMash;
	}

	/**
	 * @param mustMash the mustMash to set
	 */
	public final void setMustMash(Integer mustMash) {
		this.mustMash = mustMash;
	}

	/**
	 * @return the amount
	 */
	public final Mass getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public final void setAmount(Mass amount) {
		this.amount = amount;
	}

	/**
	 * @return the manufacturer
	 */
	public final String getManufacturer() {
		return manufacturer;
	}

	/**
	 * @param manufacturer the manufacturer to set
	 */
	public final void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	/**
	 * @return the url
	 */
	public final String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public final void setUrl(String url) {
		this.url = url;
	}
	
}
