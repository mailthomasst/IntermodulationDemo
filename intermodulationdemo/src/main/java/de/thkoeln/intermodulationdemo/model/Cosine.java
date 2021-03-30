package de.thkoeln.intermodulationdemo.model;

public class Cosine {
	private double frequency;
	private double amplitude;
	
	public Cosine(double frequency, double amplitude) {
		super();
		this.frequency = Math.abs(frequency);
		this.amplitude = amplitude;
	}
	
	public Cosine(Cosine old) {
		super();
		this.frequency = old.getFrequency();
		this.amplitude = old.getAmplitude();
	}
	
	public double getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = (Math.abs(frequency));
	}
	public double getAmplitude() {
		return amplitude;
	}
	public void setAmplitude(double amplitude) {
		this.amplitude = amplitude;
	}
	
	public double getAmplitudeAsdBmV() {
		return 20.0*Math.log10(amplitude/0.001);
	}
	
}
