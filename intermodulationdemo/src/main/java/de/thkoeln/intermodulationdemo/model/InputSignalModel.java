package de.thkoeln.intermodulationdemo.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.thkoeln.intermodulationdemo.model.idealsignal.CosFrequency;
import de.thkoeln.intermodulationdemo.model.idealsignal.CosSignal;

public class InputSignalModel {
	private List<Cosine> cosList;
	private DSPSignal compoundSignal;
	private double gain;
	private Cosine fMin;
	private int samples;
	private int samplingRate;
	private Set<ChangeListener> listeners;
	private Set<ChangeListener> softListeners;
	
	public InputSignalModel(int samples) {
		this.cosList = new ArrayList<Cosine>();
		this.listeners = new HashSet<ChangeListener>();
		this.softListeners = new HashSet<ChangeListener>();
		this.samplingRate = 256; //TODO (auslagern) Default Sampling Rate
		this.compoundSignal = new DSPSignal(samplingRate, samples, true);
		this.samples = samples;
		this.gain = 0;
	}
	
	public void loadDefault() {
		addCosine(10.0, 0.001);//TODO (auslagern)
		addCosine(11.0, 0.001);//TODO (auslagern)
	}
	
	public double getGain() {
		return gain;
	}

	public void setGain(double gain) {
		this.gain = gain;
		refreshCompoundSignal();
	}

	public void addChangeListener(ChangeListener cl) {
		listeners.add(cl);
	}
	
	public void removeChangeListener(ChangeListener cl) {
		listeners.remove(cl);
	}
	
	public Set<ChangeListener> getSoftListeners() {
		return softListeners;
	}

	public void addSoftListener(ChangeListener softListener) {
		this.softListeners.add(softListener);
	}

	public void notifySoftChangeListeners() {
		for (ChangeListener cl : softListeners) {
			cl.onChange();
		}
	}
	
	public void notifyChangeListeners() {
		notifySoftChangeListeners();
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
	
	public void addCosine(double frequency, double amplitude) {
		Cosine cs = new Cosine(frequency,amplitude);
		cosList.add(cs);
		refreshFMin();
		this.refreshCompoundSignal(); //TODO delete
		notifyChangeListeners();
	}
	
	public void editCosine(int index, double frequency, double amplitude) {
		cosList.remove(index);
		Cosine cs = new Cosine(frequency, amplitude);
		cosList.add(index, cs);
		refreshFMin();
		this.refreshCompoundSignal();
	}
	
	public void removeCosine(int index) {
		cosList.remove(index);
		refreshFMin();
		this.refreshCompoundSignal();
	}
	
	public void removeAllCosine() {
		cosList.clear();
		refreshFMin();
		this.refreshCompoundSignal();
	}
	
	public void refreshCompoundSignal() {
		checkSamplingRate();
		compoundSignal = new DSPSignal(samplingRate, samples, cosList);
		compoundSignal.addGain(gain);
		notifySoftChangeListeners();
	}
	
	public DSPSignal getSignal() {
		return compoundSignal;
	}
	
	public List<Cosine> getSignalList(){
		return this.cosList;
	}
	
	public double getF1() {
		if(this.cosList.get(0) != null) {
			return this.cosList.get(0).getFrequency();
		}
		return 0;
	}
	
	public double getF2() {
		if(this.cosList.get(1) != null) {
			return this.cosList.get(1).getFrequency();
		}
		return 0;
	}
	
	public CosSignal genIdealSignal() {
		CosSignal cs = new CosSignal();
		int i = 1;
		for (Cosine cosine : cosList) {
			cs.add(new CosFrequency("f_{"+i+"}", cosine.getAmplitude(), cosine.getFrequency()));
			i++;
		}
		return cs;
	}
	
	public Cosine getfMin() {
		return fMin;
	}
}
