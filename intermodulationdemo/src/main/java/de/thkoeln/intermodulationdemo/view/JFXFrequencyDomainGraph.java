package de.thkoeln.intermodulationdemo.view;

import java.util.HashSet;
import java.util.Set;

import de.thkoeln.intermodulationdemo.model.graph.FrequencyDomainGraph;
import de.thkoeln.intermodulationdemo.model.idealsignal.CosFrequency;
import de.thkoeln.intermodulationdemo.model.idealsignal.CosSignal;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * JavaFX Graph for the Frequency Domain
 *
 * @author Thomas Stein
 */

public class JFXFrequencyDomainGraph {
	private FrequencyDomainGraph fdg;
	private XYChart<Number, Number> chart;
	private Set<VerticalMarker> thirdOrderMarker, secondOrderMarker;

	public JFXFrequencyDomainGraph(FrequencyDomainGraph fdg, XYChart<Number, Number> chart) {
		super();
		this.fdg = fdg;
		this.chart = chart;
		this.thirdOrderMarker = new HashSet<VerticalMarker>();
		this.secondOrderMarker = new HashSet<VerticalMarker>();
		refreshChart();
	}
	
	public void refreshChart() {
		chart.getData().clear(); //TODO: schlecht bei mehreren
		final XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
		double[] xSeries = fdg.getXSeries();
		double[] ySeries = fdg.getYSeries();
		for(int i = 0; i < fdg.getSamples(); i++) {
			plotPoint(xSeries[i],ySeries[i], series);
		}
		chart.getData().add(series);
		series.getNode().getStyleClass().add("default-color0");
		for (XYChart.Data<Number, Number> data : series.getData()) {
		    StackPane stackPane = (StackPane) data.getNode();
		    //stackPane.setVisible(false);
		    data.getNode().getStyleClass().add("default-color0");
		    Tooltip myTT = new Tooltip(((int)(100.0*data.getYValue().doubleValue())/100.0) + "dBm");
		    myTT.setShowDelay(Duration.millis(10));
        	Tooltip.install(data.getNode(), myTT);
		}
	}
	
	private void plotPoint(final double x, final double y, final XYChart.Series<Number, Number> series) {
		series.getData().add(new XYChart.Data<Number, Number>(x, y));
	}
	
	public void plotThirdOrderDistortion(CosSignal cs) {
		removeThirdOrderDistortion();
		Color color = new Color(87.0/255.0,183/255.0,87.0/255.0,1);

		for (CosFrequency cf : cs.getFreqSet()) {
			thirdOrderMarker.add(new VerticalMarker(new Data<Number, Number>(cf.getFrequency(), 0), new Data<Number, Number>(cf.getFrequency(), 0), cf.getFrequencyString(), color, true));
		}
		
		for(VerticalMarker marker : thirdOrderMarker) {
			((LineChartWithMarkers)chart).addVerticalValueMarker(marker);
		}
	}
	
	public void removeThirdOrderDistortion() {
		for(VerticalMarker marker : thirdOrderMarker) {
			((LineChartWithMarkers)chart).removeVerticalValueMarker(marker);
		}
		thirdOrderMarker.clear();
	}
	
	public void plotSecondOrderDistortion(CosSignal cs) {
		removeSecondOrderDistortion();
		Color color = new Color(251.0/255.0, 167.0/255.0, 27/255.0 ,1);
		for (CosFrequency cf : cs.getFreqSet()) {
			secondOrderMarker.add(new VerticalMarker(new Data<Number, Number>(cf.getFrequency(), 0), new Data<Number, Number>(cf.getFrequency(), 0), cf.getFrequencyString(), color, true));
		}
		for(VerticalMarker marker : secondOrderMarker) {
			((LineChartWithMarkers)chart).addVerticalValueMarker(marker);
		}
	}
	
	public void removeSecondOrderDistortion() {
		for(VerticalMarker marker : secondOrderMarker) {
			((LineChartWithMarkers)chart).removeVerticalValueMarker(marker);
		}
		secondOrderMarker.clear();
	}
}

