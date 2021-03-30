package de.thkoeln.intermodulationdemo;

import java.net.URL;
import java.util.ResourceBundle;

import de.thkoeln.intermodulationdemo.model.Context;
import de.thkoeln.intermodulationdemo.model.InputSignalModel;
import de.thkoeln.intermodulationdemo.model.OutputSignalModel;
import de.thkoeln.intermodulationdemo.model.graph.TimeDomainGraph;
import de.thkoeln.intermodulationdemo.util.Utility;
import de.thkoeln.intermodulationdemo.view.JFXTimeDomainGraph;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;

public class VoutPaneController implements Initializable {
	
	@FXML
	private LineChart<Number,Number> lineChart;

	private OutputSignalModel outputSignalModel;
	private InputSignalModel inputSignalModel;
	private de.thkoeln.intermodulationdemo.model.ChangeListener modelChangeListener;
	private TimeDomainGraph tdGraph1;
	private JFXTimeDomainGraph jfxGraph;
    
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