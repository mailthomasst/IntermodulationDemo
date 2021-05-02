/**
 * Controller for the spectrum-pane
 * author: Thomas Stein
 */

package de.thkoeln.intermodulationdemo;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import de.thkoeln.intermodulationdemo.model.Context;
import de.thkoeln.intermodulationdemo.model.InputSignalModel;
import de.thkoeln.intermodulationdemo.model.OutputSignalModel;
import de.thkoeln.intermodulationdemo.model.graph.FrequencyDomainGraph;
import de.thkoeln.intermodulationdemo.util.Utility;
import de.thkoeln.intermodulationdemo.view.ChartPrinter;
import de.thkoeln.intermodulationdemo.view.JFXFrequencyDomainGraph;
import de.thkoeln.intermodulationdemo.view.LineChartWithMarkers;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class SpectrumPaneController implements Initializable {
	
	@FXML
	private LineChart<Number,Number> lineChart;

	@FXML
	private AnchorPane anchorPane;
	
	@FXML
	private CheckBox check2;
	
	@FXML
	private CheckBox check3; 
	
	@FXML
	private VBox vboxSpectrum;
	
	//@FXML
	//private Button btnPrint; 
	
	private LineChart<Number,Number> lineChart1;
	private Axis<Number> xAxis;
	private Axis<Number> yAxis;
	
	private OutputSignalModel outputSignalModel;
	private InputSignalModel inputSignalModel;
	private de.thkoeln.intermodulationdemo.model.ChangeListener modelChangeListener;
	private FrequencyDomainGraph fdGraph;
	private JFXFrequencyDomainGraph jfxGraph;

	@Override
	public void initialize(final URL url, final  ResourceBundle rb) {
		lineChart.setAnimated(false);
		outputSignalModel = Context.getInstance().getAmplificationModel().getOutputSignalModel();
		inputSignalModel = Context.getInstance().getAmplificationModel().getInputSignalModel();
		modelChangeListener = new de.thkoeln.intermodulationdemo.model.ChangeListener() {
			@Override
			public void onChange() {
				refreshOutput();
			}
		};
		check3.setOnAction(new EventHandler<ActionEvent>(){
		       @Override 
		       public void handle(ActionEvent e) { 
		   			refreshOutput();
		       } 
		    });
		check2.setOnAction(new EventHandler<ActionEvent>(){
		       @Override 
		       public void handle(ActionEvent e) { 
		    	   refreshOutput();
		       } 
		    });
		Button btnPrint= new Button("Print...");
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
		vboxSpectrum.getChildren().add(btnPrint);
		
		outputSignalModel.addChangeListener(modelChangeListener);
		//Context.getInstance().getAmplificationModel().setAllListeners(modelChangeListener);
		
		//TODO 
		//outputSignal.filterMean();
		xAxis = new NumberAxis();
		yAxis = new NumberAxis();
		yAxis.setAutoRanging(false);
		xAxis.setAutoRanging(false);
		
		((NumberAxis)yAxis).setUpperBound(80);
		((NumberAxis)yAxis).setLowerBound(0);
		
		xAxis.setLabel("Frequency");
		xAxis.setSide(Side.BOTTOM);
		((NumberAxis)xAxis).setTickLabelFormatter(Utility.getTickLabelFormatterHerz());
		((NumberAxis)yAxis).setTickLabelFormatter(Utility.getTickLabelFormatterDBm());
		yAxis.setLabel("Output Power");
		yAxis.setSide(Side.LEFT);
		
		lineChart1 = new LineChartWithMarkers<Number,Number>(xAxis,yAxis);
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
		
		fdGraph = new FrequencyDomainGraph(outputSignalModel.getSpectrum(),outputSignalModel.getSpectrum().getSamples());// (outputSignal.getSamples()/2)+1);
		jfxGraph = new JFXFrequencyDomainGraph(fdGraph, lineChart1);
		refreshOutput();
	}

	private void refreshOutput() {
		fdGraph.refreshSignal(outputSignalModel.getSpectrum());
		jfxGraph.refreshChart();
		((NumberAxis)yAxis).setUpperBound(fdGraph.getUpperBound());
		double lowBound = (((int)outputSignalModel.getNoiseLevel())/10)*10-10.0;
		((NumberAxis)yAxis).setLowerBound(lowBound);
		((NumberAxis)xAxis).setUpperBound(fdGraph.getUpperXBound());
		((NumberAxis)xAxis).setTickUnit(fdGraph.getUpperXBound()/8);
		
		if(check3.isSelected()) {
			jfxGraph.plotThirdOrderDistortion(inputSignalModel.genIdealSignal().getXOrderDisplay(3));
		} else {
			jfxGraph.removeThirdOrderDistortion();
		}
		if(check2.isSelected()) {
			jfxGraph.plotSecondOrderDistortion(inputSignalModel.genIdealSignal().getXOrderDisplay(2));
		} else {
			jfxGraph.removeSecondOrderDistortion();
		}
		
	}
}
