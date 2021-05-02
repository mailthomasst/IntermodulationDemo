/**
 * Controller for the vout-pane
 * author: Thomas Stein
 */

package de.thkoeln.intermodulationdemo;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import de.thkoeln.intermodulationdemo.model.Context;
import de.thkoeln.intermodulationdemo.model.SweepModel;
import de.thkoeln.intermodulationdemo.model.graph.FrequencyDomainGraph;
import de.thkoeln.intermodulationdemo.util.PropertyLoader;
import de.thkoeln.intermodulationdemo.util.Utility;
import de.thkoeln.intermodulationdemo.view.ChartPrinter;
import de.thkoeln.intermodulationdemo.view.InputControlItem;
import de.thkoeln.intermodulationdemo.view.JFXFrequencyDomainGraph;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class SweepPaneController implements Initializable {
	
	@FXML
	private AnchorPane anchorPane;
	
	@FXML 
	private VBox optionsVBox;
	
	@FXML
	private LineChart<Number,Number> lineChart;

	private Axis<Number> xAxis;
	private Axis<Number> yAxis;

	private LineChart<Number,Number> lineChart1;
	private SweepModel sweep;
	
	private double startVoltage;
	private int targetDb;
	private CheckBox checkBox;
    
	@Override
	public void initialize(final URL url, final  ResourceBundle rb) {
		lineChart.setAnimated(false);
		startVoltage = PropertyLoader.getDoubleProp("def.SweepStart");
		targetDb = PropertyLoader.getIntProp("def.SweepDelta");
		
		sweep = new SweepModel(Context.getInstance().getAmplificationModel().getAmp(), startVoltage, targetDb);
		
		Context.getInstance().getAmplificationModel().setAllListeners(new de.thkoeln.intermodulationdemo.model.ChangeListener() {
			@Override
			public void onChange() {
				sweep = new SweepModel(Context.getInstance().getAmplificationModel().getAmp(), startVoltage, targetDb);
				refreshSweep();
			}
		});
		
		HBox startHBox = new HBox();
		HBox endHBox = new HBox();
		HBox optionHBox = new HBox();
		startHBox.getStyleClass().add("input-hbox");
		endHBox.getStyleClass().add("input-hbox");
		optionHBox.getStyleClass().add("input-hbox");
		Label startLabel = new Label("Start:");
		startLabel.setPrefWidth(50);
		startHBox.getChildren().add(startLabel);
		InputControlItem startInput = new InputControlItem("Start", "dBm", -200.0, 200.0, startVoltage ,false);
		startHBox.getChildren().add(startInput.getTextFieldStackPane());
		
		Label endLabel = new Label("Delta:");
		endLabel.setPrefWidth(50);
		endHBox.getChildren().add(endLabel);
		InputControlItem endInput = new InputControlItem("End", "dB",10.0,200.0,targetDb, false);
		endHBox.getChildren().add(endInput.getTextFieldStackPane());
		
		checkBox = new CheckBox();
		checkBox.setOnAction(new EventHandler<ActionEvent>() {
			@Override 
			public void handle(ActionEvent e) { 
				manualSweep(startInput.getValue(), endInput.getValue());
			} 
		});
		Label checkBoxLabel = new Label("show approximation");
		checkBoxLabel.setPrefWidth(100);
		optionHBox.getChildren().add(checkBox);
		optionHBox.getChildren().add(checkBoxLabel);
		
		Button btn = new Button("Refresh");
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override 
			public void handle(ActionEvent e) { 
				manualSweep(startInput.getValue(), endInput.getValue());
			} 
		});
		
		Button btnPrint = new Button("Print...");
		btnPrint.setOnAction(new EventHandler<ActionEvent>() {
			@Override 
			public void handle(ActionEvent e) { 
				try {
					ChartPrinter.saveAsPng(anchorPane);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} 
		});
		
		Label legendLabel = new Label(" Legend");
		legendLabel.setPrefWidth(100);
		
		HBox lHBox1 = new HBox();
		lHBox1.setAlignment(Pos.CENTER_LEFT);
		Label legend11 = new Label("-");
		legend11.setPrefWidth(10);
		legend11.getStyleClass().clear();
		legend11.getStyleClass().add("legend-label-one");
		Label legend12 = new Label("Amplification");
		legend12.setPrefWidth(140);
		lHBox1.getChildren().add(legend11);
		lHBox1.getChildren().add(legend12);
		
		HBox lHBox2 = new HBox();
		lHBox2.setAlignment(Pos.CENTER_LEFT);
		Label legend21 = new Label("-");
		legend21.setPrefWidth(10);
		legend21.getStyleClass().clear();
		legend21.getStyleClass().add("legend-label-two");
		Label legend22 = new Label("2nd order distortion");
		legend22.setPrefWidth(140);
		lHBox2.getChildren().add(legend21);
		lHBox2.getChildren().add(legend22);
		
		HBox lHBox3 = new HBox();
		lHBox3.setAlignment(Pos.CENTER_LEFT);
		Label legend31 = new Label("-");
		legend31.setPrefWidth(10);
		legend31.getStyleClass().clear();
		legend31.getStyleClass().add("legend-label-three");
		Label legend32 = new Label("3rd order distortion");
		legend32.setPrefWidth(140);
		lHBox3.getChildren().add(legend31);
		lHBox3.getChildren().add(legend32);
		
		HBox lHBox4 = new HBox();
		lHBox4.setAlignment(Pos.CENTER_LEFT);
		Label legend41 = new Label("-");
		legend41.setPrefWidth(10);
		legend41.getStyleClass().clear();
		legend41.getStyleClass().add("legend-label-noise");
		Label legend42 = new Label("Noise Floor");
		legend42.setPrefWidth(140);
		lHBox4.getChildren().add(legend41);
		lHBox4.getChildren().add(legend42);
		
		
		optionsVBox.getChildren().add(startHBox);
		optionsVBox.getChildren().add(endHBox);
		//optionsVBox.getChildren().add(optionHBox);
		optionsVBox.getChildren().add(btn);
		optionsVBox.getChildren().add(btnPrint);
		optionsVBox.getChildren().add(new Label(" "));
		optionsVBox.getChildren().add(legendLabel);
		optionsVBox.getChildren().add(lHBox1);
		optionsVBox.getChildren().add(lHBox2);
		optionsVBox.getChildren().add(lHBox3);
		optionsVBox.getChildren().add(lHBox4);
		
		
		
		xAxis = new NumberAxis();
		yAxis = new NumberAxis();
		xAxis.setLabel("In / dBm @ 50 Ohm");
		xAxis.setSide(Side.BOTTOM);
		yAxis.setLabel("Out /dBm");
		yAxis.setSide(Side.LEFT);
		
		xAxis.setAutoRanging(false);
		((NumberAxis)xAxis).setLowerBound(startVoltage);
		((NumberAxis)xAxis).setUpperBound(startVoltage+targetDb);
		yAxis.setAutoRanging(false);
		((NumberAxis)yAxis).setLowerBound(startVoltage);
		((NumberAxis)yAxis).setUpperBound(startVoltage+targetDb);
		
		lineChart1 = new LineChart<Number,Number>(xAxis,yAxis);
		lineChart1.setAnimated(false);
		lineChart1.setLayoutX(-44.0);
		lineChart1.setLayoutY(-103.0);
		AnchorPane.setTopAnchor(lineChart1, 0.0);
		AnchorPane.setBottomAnchor(lineChart1, 0.0);
		AnchorPane.setRightAnchor(lineChart1, 0.0);
		AnchorPane.setLeftAnchor(lineChart1, 0.0);
		anchorPane.getChildren().clear();
		anchorPane.getChildren().add(lineChart1);
		lineChart1.setLegendVisible(false);
		refreshSweep();
	}
	
	public void manualSweep(double startDBmV, double targetDb) {
		this.startVoltage = startDBmV;
		this.targetDb = (int)targetDb;
		sweep = new SweepModel(Context.getInstance().getAmplificationModel().getAmp(),this.startVoltage, this.targetDb);
		((NumberAxis)xAxis).setLowerBound(startVoltage);
		((NumberAxis)xAxis).setUpperBound(startVoltage+targetDb);
		((NumberAxis)yAxis).setLowerBound(startVoltage);
		((NumberAxis)yAxis).setUpperBound(startVoltage+targetDb);
		refreshSweep();
	}
	
	public void refreshSweep() {
		sweep.sweep();
		lineChart1.getData().clear(); 
		
		final XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
		double[] xSeries = sweep.getXseries();
		//double[] ySeries = sweep.getK1series();
		double[] ySeries = sweep.getK1IdealSeries();
		for(int i = 0; i < xSeries.length; i++) {
			series.getData().add(new XYChart.Data<Number, Number>(xSeries[i], ySeries[i]));
		}
		lineChart1.getData().add(series);
		for (XYChart.Data<Number, Number> data : series.getData()) {
		    StackPane stackPane = (StackPane) data.getNode();
		    stackPane.setVisible(false);
		}
		
		final XYChart.Series<Number, Number> series2 = new XYChart.Series<Number, Number>();
		double[] xSeries2 = sweep.getXseries();
		//double[] ySeries2 = sweep.getK2series();
		double[] ySeries2 = sweep.getK2IdealSeries();
		for(int i = 0; i < xSeries2.length; i++) {
			series2.getData().add(new XYChart.Data<Number, Number>(xSeries2[i], ySeries2[i]));
		}
		lineChart1.getData().add(series2);
		for (XYChart.Data<Number, Number> data : series2.getData()) {
		    StackPane stackPane = (StackPane) data.getNode();
		    stackPane.setVisible(false);
		}
		
		final XYChart.Series<Number, Number> series3 = new XYChart.Series<Number, Number>();
		double[] xSeries3 = sweep.getXseries();
		//double[] ySeries3 = sweep.getK3series();
		double[] ySeries3 = sweep.getK3IdealSeries();
		for(int i = 0; i < xSeries3.length; i++) {
			series3.getData().add(new XYChart.Data<Number, Number>(xSeries3[i], ySeries3[i]));
		}
		lineChart1.getData().add(series3);
		for (XYChart.Data<Number, Number> data : series3.getData()) {
		    StackPane stackPane = (StackPane) data.getNode();
		    stackPane.setVisible(false);
		}
		
		final XYChart.Series<Number, Number> series7 = new XYChart.Series<Number, Number>();		
		double[] xSeries7 = sweep.getXseries();
		double[] ySeries7 = sweep.getNoiseSeries();
		for(int i = 0; i < xSeries7.length; i++) {
			series7.getData().add(new XYChart.Data<Number, Number>(xSeries7[i], ySeries7[i]));
		}
		lineChart1.getData().add(series7);
		for (XYChart.Data<Number, Number> data : series7.getData()) {
		    StackPane stackPane = (StackPane) data.getNode();
		    stackPane.setVisible(false);
		}
		
		final XYChart.Series<Number, Number> seriesOIP2 = new XYChart.Series<Number, Number>();
		seriesOIP2.getData().add(new XYChart.Data<Number, Number>(sweep.getOip2x(),sweep.getOip2y()));
		lineChart1.getData().add(seriesOIP2);
		for (XYChart.Data<Number, Number> data : seriesOIP2.getData()) {
		    //StackPane stackPane = (StackPane) data.getNode();
		    //stackPane.setVisible(false);
		}
        for (XYChart.Data<Number, Number> d : seriesOIP2.getData()) {
        	Tooltip myTT = new Tooltip("OIP2: " + ((int)(100.0*d.getYValue().doubleValue())/100.0) + "dBm");
        	myTT.setShowDelay(Duration.millis(10));
        	Tooltip.install(d.getNode(), myTT);
             //Adding class on hover
             d.getNode().setOnMouseEntered(event -> d.getNode().getStyleClass().add("onHover"));
             //Removing class on exit
             d.getNode().setOnMouseExited(event -> d.getNode().getStyleClass().remove("onHover"));
        }
		
		final XYChart.Series<Number, Number> seriesOIP3 = new XYChart.Series<Number, Number>();
		seriesOIP3.getData().add(new XYChart.Data<Number, Number>(sweep.getOip3x(),sweep.getOip3y()));
		lineChart1.getData().add(seriesOIP3);
		for (XYChart.Data<Number, Number> data : seriesOIP3.getData()) {
		    //StackPane stackPane = (StackPane) data.getNode();
		    //stackPane.setVisible(false);
		}
        for (XYChart.Data<Number, Number> d : seriesOIP3.getData()) {
        	Tooltip myTT = new Tooltip("OIP3: " + ((int)(100.0*d.getYValue().doubleValue())/100.0) + "dBm");
        	myTT.setShowDelay(Duration.millis(10));
        	Tooltip.install(d.getNode(), myTT);
             //Adding class on hover
             d.getNode().setOnMouseEntered(event -> d.getNode().getStyleClass().add("onHover"));
             //Removing class on exit
             d.getNode().setOnMouseExited(event -> d.getNode().getStyleClass().remove("onHover"));
        }
        
		final XYChart.Series<Number, Number> seriesNoiseK1 = new XYChart.Series<Number, Number>();
		seriesNoiseK1.getData().add(new XYChart.Data<Number, Number>(sweep.getNoisek1x(),sweep.getNoisek1y()));
		lineChart1.getData().add(seriesNoiseK1);
        for (XYChart.Data<Number, Number> d : seriesNoiseK1.getData()) {
        	Tooltip myTT = new Tooltip(((int)(100.0*d.getYValue().doubleValue())/100.0) + "dBm");
        	myTT.setShowDelay(Duration.millis(10));
        	Tooltip.install(d.getNode(), myTT);
             //Adding class on hover
             d.getNode().setOnMouseEntered(event -> d.getNode().getStyleClass().add("onHover"));
             //Removing class on exit
             d.getNode().setOnMouseExited(event -> d.getNode().getStyleClass().remove("onHover"));
        }
        
        final XYChart.Series<Number, Number> seriesNoiseK2 = new XYChart.Series<Number, Number>();
        seriesNoiseK2.getData().add(new XYChart.Data<Number, Number>(sweep.getNoisek2x(),sweep.getNoisek2y()));
		lineChart1.getData().add(seriesNoiseK2);
        for (XYChart.Data<Number, Number> d : seriesNoiseK2.getData()) {
        	Tooltip myTT = new Tooltip(((int)(100.0*d.getYValue().doubleValue())/100.0) + "dBm");
        	myTT.setShowDelay(Duration.millis(10));
        	Tooltip.install(d.getNode(), myTT);
             //Adding class on hover
             d.getNode().setOnMouseEntered(event -> d.getNode().getStyleClass().add("onHover"));
             //Removing class on exit
             d.getNode().setOnMouseExited(event -> d.getNode().getStyleClass().remove("onHover"));
        }
        
        final XYChart.Series<Number, Number> seriesNoiseK3 = new XYChart.Series<Number, Number>();
        seriesNoiseK3.getData().add(new XYChart.Data<Number, Number>(sweep.getNoisek3x(),sweep.getNoisek3y()));
		lineChart1.getData().add(seriesNoiseK3);
        for (XYChart.Data<Number, Number> d : seriesNoiseK3.getData()) {
        	Tooltip myTT = new Tooltip(((int)(100.0*d.getYValue().doubleValue())/100.0) + "dBm");
        	myTT.setShowDelay(Duration.millis(10));
        	Tooltip.install(d.getNode(), myTT);
             //Adding class on hover
             d.getNode().setOnMouseEntered(event -> d.getNode().getStyleClass().add("onHover"));
             //Removing class on exit
             d.getNode().setOnMouseExited(event -> d.getNode().getStyleClass().remove("onHover"));
        }
        
		
		//Ideal Lines
		if(checkBox.isSelected()) {
			final XYChart.Series<Number, Number> series4 = new XYChart.Series<Number, Number>();		
			double[] xSeries4 = sweep.getXseries();
			double[] ySeries4 = sweep.getK1IdealSeries();
			for(int i = 0; i < xSeries4.length; i++) {
				series4.getData().add(new XYChart.Data<Number, Number>(xSeries4[i], ySeries4[i]));
			}
			lineChart1.getData().add(series4);
			for (XYChart.Data<Number, Number> data : series4.getData()) {
			    StackPane stackPane = (StackPane) data.getNode();
			    stackPane.setVisible(false);
			}
			
			final XYChart.Series<Number, Number> series5 = new XYChart.Series<Number, Number>();		
			double[] xSeries5 = sweep.getXseries();
			double[] ySeries5 = sweep.getK2IdealSeries();
			for(int i = 0; i < xSeries5.length; i++) {
				series5.getData().add(new XYChart.Data<Number, Number>(xSeries5[i], ySeries5[i]));
			}
			lineChart1.getData().add(series5);
			for (XYChart.Data<Number, Number> data : series5.getData()) {
			    StackPane stackPane = (StackPane) data.getNode();
			    stackPane.setVisible(false);
			}
			
			final XYChart.Series<Number, Number> series6 = new XYChart.Series<Number, Number>();		
			double[] xSeries6 = sweep.getXseries();
			double[] ySeries6 = sweep.getK3IdealSeries();
			for(int i = 0; i < xSeries6.length; i++) {
				series6.getData().add(new XYChart.Data<Number, Number>(xSeries6[i], ySeries6[i]));
			}
			lineChart1.getData().add(series6);
			for (XYChart.Data<Number, Number> data : series6.getData()) {
			    StackPane stackPane = (StackPane) data.getNode();
			    stackPane.setVisible(false);
			}
			
			series4.getNode().getStyleClass().add("default-color0dash");
			series5.getNode().getStyleClass().add("default-color1dash");
			series6.getNode().getStyleClass().add("default-color2dash");
		}
		seriesOIP2.getNode().getStyleClass().add("default-color1point");
		seriesOIP3.getNode().getStyleClass().add("default-color2point");
		series7.getNode().getStyleClass().add("default-colornoise");
	}
}
