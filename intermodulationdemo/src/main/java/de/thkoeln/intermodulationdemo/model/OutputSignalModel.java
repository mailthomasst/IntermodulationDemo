/**
 * Output Signal Model
 * author: Thomas Stein
 */
package de.thkoeln.intermodulationdemo.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OutputSignalModel {
	private int samples;
	private int samplingRate;
	
	private List<Cosine> cosList;
	private Cosine fMin;
	
	private DSPSignal timeSignal;
	private DSPSignal frequencySignal;
	private double noiseLevel;

	private Set<ChangeListener> listeners;
	
	public OutputSignalModel(int samples) {
		this.cosList = new ArrayList<Cosine>();
		this.listeners = new HashSet<ChangeListener>();
		
		this.samplingRate = 256; //Default Sampling Rate
		this.timeSignal = new DSPSignal(samplingRate, samples, true);
		this.frequencySignal = new DSPSignal(samplingRate, samples/2+1, true);
		this.samples = samples;
	}

	public void addChangeListener(ChangeListener cl) {
		listeners.add(cl);
	}
	
	public void removeChangeListener(ChangeListener cl) {
		listeners.remove(cl);
	}
	
	public void notifyChangeListeners() {
		for (ChangeListener cl : listeners) {
			cl.onChange();
		}
	}
	
	private void refreshFMin() {
		fMin = null;
		for (Cosine cs : cosList) {
			if(fMin == null) {
				fMin = cs;
			} else {
				if (fMin.getFrequency() > cs.getFrequency()) {
					fMin = cs;
				}
			}
		}
	}
	
	private void checkSamplingRate() {
		double lowestFrequency = cosList.get(0).getFrequency();
		for (Cosine cs : cosList) {
			if(cs.getFrequency() < lowestFrequency) {
				lowestFrequency = cs.getFrequency();
			}
		}
		int minimalSamplingRate = (int)(128 * (lowestFrequency/10));
		int newSamplingRate = samplingRate;
		for (int i = 8;i < 100; i++) {
			if(Math.pow(2,i) >= minimalSamplingRate) {
				newSamplingRate = (int)Math.pow(2,i);
				break;
			}
		}
		if (newSamplingRate != samplingRate) {
			samplingRate = newSamplingRate;
		}
	}

	public void setCosList(List<Cosine> cosList) {
		this.cosList = cosList;
		refreshFMin();
		checkSamplingRate();
		this.timeSignal = new DSPSignal(samplingRate, samples, cosList);
		notifyChangeListeners();
	}
	
	public void setTimeSignal(DSPSignal timeSignal) {
		this.timeSignal = timeSignal;
		this.samples = timeSignal.getSamples();
		this.samplingRate = timeSignal.getSamplingRate();
		//TODO FIND PEAKS
	}
	
	public void setFrequencySignal(DSPSignal frequencySignal) {
		this.frequencySignal = frequencySignal;
		//TODO FIND PEAKS
	}
	
	public Cosine getfMin() {
		return fMin;
	}
	
	public DSPSignal getSignal() {
		return timeSignal;
	}
	
	public DSPSignal getSpectrum() {
		return this.frequencySignal;
	}
	
	public double getNoiseLevel() {
		return noiseLevel;
	}

	public void setNoiseLevel(double noiseLevel) {
		this.noiseLevel = noiseLevel;
	}
}
