package de.thkoeln.intermodulationdemo.view;

import javafx.scene.chart.XYChart.Data;
import javafx.scene.paint.Color;

/**
 * Vertical Markers for the line Chart
 *
 * @author -
 */

public class VerticalMarker {
	private Data<Number,Number> markerData, labelData;
	private String text;
	private Color color;
	private Boolean dashed;
	
	public VerticalMarker(Data<Number, Number> markerData, Data<Number, Number> labelData, String text, Color color,
			Boolean dashed) {
		super();
		this.markerData = markerData;
		this.labelData = labelData;
		this.text = text;
		this.color = color;
		this.dashed = dashed;
	}

	public Data<Number, Number> getMarkerData() {
		return markerData;
	}

	public void setMarkerData(Data<Number, Number> markerData) {
		this.markerData = markerData;
	}

	public Data<Number, Number> getLabelData() {
		return labelData;
	}

	public void setLabelData(Data<Number, Number> labelData) {
		this.labelData = labelData;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Boolean getDashed() {
		return dashed;
	}

	public void setDashed(Boolean dashed) {
		this.dashed = dashed;
	}
	
}
