/**
 * Representation of a frequency domain graph
 * author: Thomas Stein
 */

package de.thkoeln.intermodulationdemo.model.graph;

import de.thkoeln.intermodulationdemo.model.DSPSignal;

public class FrequencyDomainGraph {
	private int samples;
	private double xMin;
	private double xMax;
	private double yMin;
	private double yMax;
	private boolean xLog;
	private boolean yLog;
	private double[] xSeries;
	private double[] ySeries;
	
	public FrequencyDomainGraph(DSPSignal signal, int samples) {
		super();
		
		this.samples = samples;
		xMin 	= 0;
		xMax	= samples/signal.getSamplingRate();
		xSeries = new double[samples];
		ySeries = new double[samples];
		
		
		refreshSignal(signal);
	}
	
	public void refreshSignal(DSPSignal signal) {
		yMax = 20.0*Math.log10(signal.getSignal()[0].getReal()/(0.001)); //signal.getSignal()[0].getReal();
		yMin = 20.0*Math.log10(signal.getSignal()[0].getReal()/(0.001)); //signal.getSignal()[0].getReal();
		for (int i = 0; i < samples/4; i++) {
			ySeries[i]=20.0*Math.log10(signal.getSignal()[i].getReal()/(0.001));
			xSeries[i]=((double)i)*signal.getSamplingRate()/((samples-1)*2);
			if (ySeries[i] > yMax) {
				yMax = ySeries[i];
			} else if(ySeries[i] < yMin) {
				yMin = ySeries[i];  
			}
		}
	}
	
	public double getUpperBound() {
		return (((int)yMax)/5)*5+5.0;
	}
	
	public double getLowerBound() {
		return 0;
	}
	
	
	public int getSamples() {
		return samples;
	}
	public void setSamples(int samples) {
		this.samples = samples;
	}
	public double getxMin() {
		return xMin;
	}
	public void setxMin(double xMin) {
		this.xMin = xMin;
	}
	public double getxMax() {
		return xMax;
	}
	public void setxMax(double xMax) {
		this.xMax = xMax;
	}
	public double getyMin() {
		return yMin;
	}
	public void setyMin(double yMin) {
		this.yMin = yMin;
	}
	public double getyMax() {
		return yMax;
	}
	public void setyMax(double yMax) {
		this.yMax = yMax;
	}
	public boolean isxLog() {
		return xLog;
	}
	public void setxLog(boolean xLog) {
		this.xLog = xLog;
	}
	public boolean isyLog() {
		return yLog;
	}
	public void setyLog(boolean yLog) {
		this.yLog = yLog;
	}
	public double[] getXSeries() {
		return xSeries;
	}
	public void setXSeries(double[] series) {
		this.xSeries = series;
	}
	
	public double[] getYSeries() {
		return ySeries;
	}
	public void setYSeries(double[] series) {
		this.ySeries = series;
	}

}
