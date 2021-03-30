package de.thkoeln.intermodulationdemo.model.graph;

import de.thkoeln.intermodulationdemo.model.DSPSignal;

public class TimeDomainGraph {
	private int samples;
	private double xMin;
	private double xMax;
	private double yMin;
	private double yMax;
	private boolean xLog;
	private boolean yLog;
	private double[] xSeries;
	private double[] ySeries;
	private double fMin;
	private double zoomFactor;
	
	public TimeDomainGraph(DSPSignal signal) {
		this(signal,11);
	}
	
	public TimeDomainGraph(DSPSignal signal, double fMin) {
		refreshSignal(signal, fMin);
		constructGraph(signal);
	}
	
	private void constructGraph(DSPSignal signal) {
		yMax = (signal.getSignal()[0]).getReal();
		yMin = (signal.getSignal()[0]).getReal();

		for (int i = 0; i < samples; i++) {
			ySeries[i]=(signal.getSignal()[i]).getReal();
			xSeries[i]=((double)i)/signal.getSamplingRate();
			if (ySeries[i] > yMax) {
				yMax = ySeries[i];
			} else if(ySeries[i] < yMin) {
				yMin = ySeries[i];
			}
		}
		
	}
	
	public void refreshSignal(DSPSignal signal, double fMin) {
		this.fMin = fMin;
		this.zoomFactor = 20/this.fMin;
		this.samples = (int)Math.round(signal.getSamplingRate()*this.zoomFactor);		
		xMin 	= 0;
		xMax	= (double)samples/signal.getSamplingRate();
		xSeries = new double[samples];
		ySeries = new double[samples];
		constructGraph(signal);
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
