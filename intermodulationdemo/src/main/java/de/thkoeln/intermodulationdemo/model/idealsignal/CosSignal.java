/**
 * Representation of a superposition of cosine signals
 * author: Thomas Stein
 */

package de.thkoeln.intermodulationdemo.model.idealsignal;

import java.util.HashSet;
import java.util.Set;

public class CosSignal {
	private Set<CosFrequency> freqSet;
	
	public CosSignal() {
		super();
		this.freqSet = new HashSet<CosFrequency>();;
	}

	public Set<CosFrequency> getFreqSet() {
		return freqSet;
	}

	public void setFreqSet(Set<CosFrequency> freqSet) {
		this.freqSet = freqSet;
	}

	public void add(CosFrequency newFrequency) {
		for (CosFrequency oldFrequency : freqSet) {
			if(oldFrequency.getFrequency() == newFrequency.getFrequency()) {
				oldFrequency.add(newFrequency);
				return;
			}
		}
		freqSet.add(newFrequency);
	}
	
	public void add(CosSignal signal) {
		for (CosFrequency cf : signal.getFreqSet()) {
			this.add(cf);
		}
	}
	
	public CosSignal multiply(CosSignal multiplier) {
		CosSignal returnSignal = new CosSignal();
		
		for (CosFrequency f1 : this.freqSet) {
			for (CosFrequency f2 : multiplier.getFreqSet()) {
				returnSignal.add(f1.multiply(f2));
			}
		}
		return returnSignal;
	}
	
	public void multiply(double factor) {
		for(CosFrequency cf : freqSet) {
			cf.setFactor(cf.getFactor()*factor);
		}
	}
	
	public Set<String> getNameAsString(){
		Set<String> returnSet = new HashSet<String>();
		for (CosFrequency f : this.freqSet) {
			returnSet.add(f.getString());
		}
		return returnSet;
	}
	
	public CosSignal power(int power) {
		CosSignal returnSignal = new CosSignal();
		returnSignal.copy(this);
		for (int i = 1; i < power; i++) {
			returnSignal = returnSignal.multiply(this);
		}
		return returnSignal;
	}
	
	public void copy(CosSignal original) {
		this.freqSet.clear();
		for (CosFrequency cf : original.getFreqSet()) {
			this.freqSet.add(new CosFrequency(cf.getName(), cf.getFactor(), cf.getFrequency()));
		}
	}
	
	public CosSignal getXOrderDisplay(int power) {
		CosSignal newCs = new CosSignal();
		CosSignal power3 = this.power(power);
		for (CosFrequency f : power3.getFreqSet()) {
			boolean exists = false;
			for (CosFrequency old : freqSet) {
				if(old.getFrequency() == f.getFrequency() || f.getFrequency() == 0.0) {
					exists = true;
				}
			}
			if(!exists) {
				newCs.add(f);
			}
		}
		return newCs;
	}
}
