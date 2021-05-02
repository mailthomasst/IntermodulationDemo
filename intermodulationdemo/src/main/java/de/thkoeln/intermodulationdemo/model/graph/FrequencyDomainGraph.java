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
	private double upperXBound;
	
	public FrequencyDomainGraph(DSPSignal signal, int samples) {
		super();
		
		this.samples = samples;
		this.upperXBound = 40;
		xMin 	= 0;
		xMax	= samples/signal.getSamplingRate();
		xSeries = new double[samples];
		ySeries = new double[samples];
		
		
		refreshSignal(signal);
	}
	
	public void refreshSignal(DSPSignal signal) {
		setSamples(signal.getSamples());
		yMax = 10.0*Math.log10(signal.getSignal()[0].getReal()/(0.001)); //signal.getSignal()[0].getReal();
		yMin = 10.0*Math.log10(signal.getSignal()[0].getReal()/(0.001)); //signal.getSignal()[0].getReal();
		for (int i = 0; i < samples; i++) {
			ySeries[i]=10.0*Math.log10(signal.getSignal()[i].getReal()/(0.001));
			xSeries[i]=((double)i)*signal.getSamplingRate()/((samples-1)*2);
			if (ySeries[i] > yMax) {
				yMax = ySeries[i];
			} else if(ySeries[i] < yMin) {
				yMin = ySeries[i];  
			}
		}
		this.upperXBound = upperXBound(xSeries[samples-1]/4); //(((int)xSeries[samples-1])/40)*10+10.0;
	}
	
	public double upperXBound(double x) {
		if(x/10 <= 10) {
			return (((int)x)/10)*10+10.0;
		} else {
			return upperXBound(x/10)*10;
		}
	}
	
	public double getUpperBound() {
		if (((((int)yMax)/10)*10+10.0) < 10) {
			return 10.0;
		}
		return (((int)yMax)/10)*10+10.0;
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

	public double getUpperXBound() {
		return upperXBound;
	}

}
