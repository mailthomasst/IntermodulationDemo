package de.thkoeln.intermodulationdemo.view;

import de.thkoeln.intermodulationdemo.model.graph.TimeDomainGraph;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;

public class JFXTimeDomainGraph {
	private TimeDomainGraph tdg;
	private XYChart<Number, Number> chart;

	public JFXTimeDomainGraph(TimeDomainGraph tdg, XYChart<Number, Number> chart) {
		super();
		this.tdg = tdg;
		this.chart = chart;
		refreshChart();
	}
	
	public void refreshChart() {
		chart.getData().clear(); //TODO: schlecht bei mehreren
		final XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
		double[] xSeries = tdg.getXSeries();
		double[] ySeries = tdg.getYSeries();
		for(int i = 0; i < tdg.getSamples(); i++) {
			plotPoint(xSeries[i],ySeries[i], series);
		}
		chart.getData().add(series);
		for (XYChart.Data<Number, Number> data : series.getData()) {
		    StackPane stackPane = (StackPane) data.getNode();
		    stackPane.setVisible(false);
		}
		
		NumberAxis xAxis = (NumberAxis) chart.getXAxis();
		xAxis.setAutoRanging(false);
		xAxis.setUpperBound(tdg.getxMax());
		xAxis.setLowerBound(tdg.getxMin());
		xAxis.setTickUnit((tdg.getxMax()-tdg.getxMin())/4);
	}
	
	private void plotPoint(final double x, final double y, final XYChart.Series<Number, Number> series) {
		series.getData().add(new XYChart.Data<Number, Number>(x, y));
	}
	
}
