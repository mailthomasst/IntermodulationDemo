package de.thkoeln.intermodulationdemo.model;

public class ModelParameter {
	private String label;
	private String unit;
	private double value;
	private double minValue;
	private double maxValue;
	private boolean prefix;
	private int group;
	
	public ModelParameter(String label, String unit, double value, double minValue, double maxValue) {
		this(label,unit,value,minValue,maxValue,true);
	}

	public ModelParameter(String label, String unit, double value, double minValue, double maxValue, int group) {
		this(label,unit,value,minValue,maxValue,true, group);
	}
	
	public ModelParameter(String label, String unit, double value, double minValue, double maxValue, boolean prefix) {
		this(label,unit,value,minValue,maxValue,true, 0);
	}
	
	public ModelParameter(String label, String unit, double value, double minValue, double maxValue, boolean prefix, int group) {
		super();
		this.label = label;
		this.unit = unit;
		this.value = value;
		this.prefix = prefix;
		this.group = group;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public boolean isPrefix() {
		return prefix;
	}
	public void setPrefix(boolean prefix) {
		this.prefix = prefix;
	}
	public int getGroup() {
		return group;
	}
	public void setGroup(int group) {
		this.group = group;
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}
	
}
