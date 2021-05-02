/**
 * Sweep Model Class
 * author: Thomas Stein
 */
package de.thkoeln.intermodulationdemo.model;

import java.util.ArrayList;
import java.util.List;
import de.thkoeln.intermodulationdemo.model.graph.FrequencyDomainGraph;
import de.thkoeln.intermodulationdemo.util.Utility;

public class SweepModel {
	private Amplifier amp;
	private List<Cosine> cosList;
	private double inputPower;
	
	private double[] k1series;
	private double[] k2series;
	private double[] k3series;
	
	private double[] k1IdealSeries;
	private double[] k2IdealSeries;
	private double[] k3IdealSeries;
	
	private int oip2x,oip3x;
	private double oip2y,oip3y;
	
	private int noisek1x,noisek2x,noisek3x;
	
	private double[] noiseSeries;

	private double[] xseries;
	
	private int sweepDeltaDB;
	
	public SweepModel(Amplifier amp, double inputPowerdB, int sweepDeltaDB) {
		super();
		
		this.cosList = new ArrayList<Cosine>();
		
		this.amp = amp;
		this.inputPower = inputPowerdB;
		this.sweepDeltaDB = sweepDeltaDB;
		this.k1series = new double[sweepDeltaDB+1];
		this.k2series = new double[sweepDeltaDB+1];
		this.k3series = new double[sweepDeltaDB+1];
		this.xseries = new double[sweepDeltaDB+1];
		
		this.k1IdealSeries = new double[sweepDeltaDB+1];
		this.k2IdealSeries = new double[sweepDeltaDB+1];
		this.k3IdealSeries = new double[sweepDeltaDB+1];
		
		this.noiseSeries = new double[sweepDeltaDB+1];
	}
	
	public void sweep() {
		
		for (int i = 0; i < sweepDeltaDB+1; i++ ) {
			xseries[i] = i+this.inputPower;
			cosList.clear();
			cosList.add(new Cosine(10, Utility.dBmtoV(xseries[i],50.0)));
			cosList.add(new Cosine(11, Utility.dBmtoV(xseries[i],50.0)));
			
			//DSPSignal timeSignal = new DSPSignal(256, (int)Math.pow(2, 10), cosList);
			DSPSignal frequencySignal = amp.getAmplifiedSpectrum(cosList);
			FrequencyDomainGraph fdg = new FrequencyDomainGraph(frequencySignal, frequencySignal.getSamples());
			
			for (int j = 0; j < frequencySignal.getSamples(); j++) {
				if (fdg.getXSeries()[j] == 10.0) {
					this.k1series[i] = fdg.getYSeries()[j];
				}
				if (fdg.getXSeries()[j] == 20.0) {
					this.k2series[i] = fdg.getYSeries()[j];
				}
				if (fdg.getXSeries()[j] == 9.0) {
					this.k3series[i] = fdg.getYSeries()[j];
				}
			}
		}
		
		noise();
		idealSweep();
	}
	
	public void noise() {
		double Nout = amp.getNoiseLevel();
		for (int i = 0; i < xseries.length; i++ ) {
			this.noiseSeries[i] = Nout;
		}
	}
	
	
	public void idealSweep() {
		int k1, k2, k3;
		k1 = k2 = k3 = 0;
		for (int i = 0; i < xseries.length; i++ ) {
			if(this.k1series[i] <= this.noiseSeries[i]) k1 = i;
			if(this.k2series[i] <= this.noiseSeries[i]) k2 = i;
			if(this.k3series[i] <= this.noiseSeries[i]) k3 = i;
		}
		for (int i = 0; i < xseries.length; i++ ) {
			this.k1IdealSeries[i] = this.k1series[k1]+(i-k1);
			this.k2IdealSeries[i] = this.k2series[k2]+(i-k2)*2;
			this.k3IdealSeries[i] = this.k3series[k3]+(i-k3)*3;
		}
		
		double y = -(xseries[k3]) + this.k1series[k3];
		double y2 = -2*xseries[k3] + this.k2series[k3];
		double y3 = -3*xseries[k3] + this.k3series[k3];
		
		this.noisek1x = this.noisek2x = this.noisek3x = -1000;
		double noiseLevel = amp.getNoiseLevel();
		for (int i = 0; i < xseries.length; i++ ) {
			if(this.k1series[i] <= noiseLevel) this.noisek1x = (int)xseries[i];
			if(this.k2series[i] <= noiseLevel) this.noisek2x = (int)xseries[i];
			if(this.k3series[i] <= noiseLevel) this.noisek3x = (int)xseries[i];
		}
		
		this.oip2x = (int)Math.round(y-y2);
		this.oip2y = y+this.oip2x;
		this.oip3x = (int)Math.round((y-y3)/2);
		this.oip3y = y+this.oip3x;
	}
	
	
	public double[] getK1IdealSeries() {
		return k1IdealSeries;
	}

	public double[] getK2IdealSeries() {
		return k2IdealSeries;
	}

	public double[] getK3IdealSeries() {
		return k3IdealSeries;
	}

	public double[] getK1series() {
		return k1series;
	}

	public double[] getK2series() {
		return k2series;
	}

	public double[] getK3series() {
		return k3series;
	}

	public double[] getXseries() {
		return xseries;
	}
	
	public double[] getNoiseSeries() {
		return noiseSeries;
	}

	public double getOip2x() {
		return oip2x;
	}

	public double getOip2y() {
		return oip2y;
	}

	public double getOip3x() {
		return oip3x;
	}

	public double getOip3y() {
		return oip3y;
	}
	
	public int getNoisek1x() {
		return noisek1x;
	}

	public int getNoisek2x() {
		return noisek2x;
	}

	public int getNoisek3x() {
		return noisek3x;
	}

	public double getNoisek1y() {
		return amp.getNoiseLevel();
	}

	public double getNoisek2y() {
		return amp.getNoiseLevel();
	}

	public double getNoisek3y() {
		return amp.getNoiseLevel();
	}
}
