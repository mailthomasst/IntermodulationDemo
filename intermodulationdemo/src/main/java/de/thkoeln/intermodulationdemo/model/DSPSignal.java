package de.thkoeln.intermodulationdemo.model;

import java.util.List;

import org.apache.commons.math3.complex.Complex;

import de.thkoeln.intermodulationdemo.util.Utility;

public class DSPSignal {
	private Complex[] signal;
	private int samples;
	private int samplingRate;
	
	public DSPSignal(int samplingRate, int samples) {
		super();
		this.signal = new Complex[samples];
		this.samples = samples;
		this.samplingRate = samplingRate;
	}
	
	public DSPSignal(int samplingRate, int samples, boolean flatLine) {
		this(samplingRate, samples);
		if (flatLine) {
			Complex[] tds = new Complex[samples];
			for (int i = 0; i < samples; i++) {
				tds[i] = new Complex(0.0);
			}
			this.setSignal(tds);
		}
	}
	
	public DSPSignal(int samplingRate, int samples, List<Cosine> cosList) {
		this(samplingRate, samples, true);
		for(Cosine cs : cosList) {
			for (int i = 0; i < samples; i++) {
				this.signal[i] = this.signal[i].add(new Complex(cs.getAmplitude()*Math.cos(2*Math.PI*(double)cs.getFrequency()*((double)i/(double)samplingRate))));
			}
		}
	}

	public void addGain(double gain) {
		for (int i = 0; i < signal.length; i++) {
			signal[i] = signal[i].multiply(Utility.dBVoltToFactor(gain));
		}
	}
	
	public void amplify(SimpleAmplifier amp) {
		Complex[] newSignal = new Complex[samples];
		for (int i = 0; i<samples;i++) {
			newSignal[i] = amp.amplify(signal[i]);
		}
		setSignal(newSignal);
	}
	
	public Complex[] getSignal() {
		return signal;
	}
	
	public void setSignal(Complex[] timeDomainSignal) {
		this.signal = timeDomainSignal;
	}

	public int getSamples() {
		return samples;
	}
	
	public void setSamples(int samples) {
		this.samples = samples;
	}
	
	public int getSamplingRate() {
		return samplingRate;
	}
	
	public void setSamplingRate(int samplingRate) {
		this.samplingRate = samplingRate;
	}
	
	public void filterMean() {
		double sum = 0;
		for (Complex element : this.signal) {
			sum = sum+(element.getReal());
		}
		double mean = sum/samples;
		Complex[] newTds = new Complex[samples];
		for (int i = 0; i < samples; i++) {
			newTds[i] = new Complex(this.signal[i].getReal()-mean);
		}
		setSignal(newTds);
	}
}
