package de.thkoeln.intermodulationdemo;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import de.thkoeln.intermodulationdemo.model.Context;
import de.thkoeln.intermodulationdemo.model.InputSignalModel;
import de.thkoeln.intermodulationdemo.model.OutputSignalModel;
import de.thkoeln.intermodulationdemo.model.graph.TimeDomainGraph;
import de.thkoeln.intermodulationdemo.util.Utility;
import de.thkoeln.intermodulationdemo.view.ChartPrinter;
import de.thkoeln.intermodulationdemo.view.JFXTimeDomainGraph;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class VoutPaneController implements Initializable {
	
	@FXML
	private AnchorPane anchorPaneOut;
	
	@FXML
	private LineChart<Number,Number> lineChart;
	
	@FXML
	private VBox vboxVout;

	private OutputSignalModel outputSignalModel;
	private InputSignalModel inputSignalModel;
	private de.thkoeln.intermodulationdemo.model.ChangeListener modelChangeListener;
	private TimeDomainGraph tdGraph1;
	private JFXTimeDomainGraph jfxGraph;
    
	@Override
	public void initialize(final URL url, final  ResourceBundle rb) {
		
		Button btnPrint= new Button("Print...");
		btnPrint.setOnAction(new EventHandler<ActionEvent>() {
			@Override 
			public void handle(ActionEvent e) { 
				try {
					ChartPrinter.saveAsPng(anchorPaneOut);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} 
		});
		vboxVout.getChildren().add(btnPrint);
		
		lineChart.setAnimated(false);
		outputSignalModel = Context.getInstance().getAmplificationModel().getOutputSignalModel();
		inputSignalModel = Context.getInstance().getAmplificationModel().getInputSignalModel();
		modelChangeListener = new de.thkoeln.intermodulationdemo.model.ChangeListener() {
			@Override
			public void onChange() {
				refreshOutput();
			}
		};
		outputSignalModel.addChangeListener(modelChangeListener);

		lineChart.setLegendVisible(false);
		((NumberAxis)lineChart.getXAxis()).setTickLabelFormatter(Utility.getTickLabelFormatterSecond());
		((NumberAxis)lineChart.getYAxis()).setTickLabelFormatter(Utility.getTickLabelFormatterVolt());
		//tdGraph1 = new TimeDomainGraph(outputSignal,1024);
		tdGraph1 = new TimeDomainGraph(outputSignalModel.getSignal(),inputSignalModel.getfMin().getFrequency());
		jfxGraph = new JFXTimeDomainGraph(tdGraph1, lineChart);
		
		refreshOutput();
	}
	
	private void refreshOutput() {
		//TODO fmin definieren
		tdGraph1.refreshSignal(outputSignalModel.getSignal(),inputSignalModel.getfMin().getFrequency());
		jfxGraph.refreshChart();
	}
}