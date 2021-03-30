package de.thkoeln.intermodulationdemo.model.idealsignal;

public class CosFrequency {
	private String name;
	private double factor;
	private double frequency;
	
	public CosFrequency(String name, double factor, double frequency) {
		super();
		this.name = name;
		this.factor = factor;
		this.frequency = frequency;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getFactor() {
		return factor;
	}
	public void setFactor(double factor) {
		this.factor = factor;
	}
	public double getFrequency() {
		return frequency;
	}
	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}
	
	public void add(CosFrequency newFreq) {
		this.setFactor(this.getFactor()+newFreq.getFactor());
	}
	
	public CosSignal multiply(CosFrequency newFreq) {
		CosSignal returnSet = new CosSignal();
		double factor = this.getFactor()*newFreq.getFactor()/2;
		returnSet.add(new CosFrequency(this.name+"-"+newFreq.getName(), factor, Math.abs(this.getFrequency()-newFreq.getFrequency())));
		returnSet.add(new CosFrequency(this.name+"+"+newFreq.getName(), factor, Math.abs(this.getFrequency()+newFreq.getFrequency())));
		return returnSet;
	}
	
	public String getString() {
		return Double.toString(this.getFactor())+this.getName()+ "->"+this.getFrequency();
	}
	
	public String getFrequencyString() {
		return this.getName();
	}
}
