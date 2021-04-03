/**
 * Controller for the signal-pane
 * author: Thomas Stein
 */

package de.thkoeln.intermodulationdemo;


import java.net.URL;
import java.util.ResourceBundle;

import de.thkoeln.intermodulationdemo.model.Context;
import de.thkoeln.intermodulationdemo.model.Cosine;
import de.thkoeln.intermodulationdemo.model.InputSignalModel;
import de.thkoeln.intermodulationdemo.model.graph.TimeDomainGraph;
import de.thkoeln.intermodulationdemo.util.LatexViewFixed;
import de.thkoeln.intermodulationdemo.util.Utility;
import de.thkoeln.intermodulationdemo.view.InputControlItem;
import de.thkoeln.intermodulationdemo.view.JFXTimeDomainGraph;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Tooltip;


public class SignalPaneController implements Initializable {
	
	@FXML
	private Label titleLabel;
	
	@FXML
	private VBox centerVBox;
	
	@FXML
	private VBox gainVBox;
	
	@FXML
	private LineChart<Number,Number> lineChart;

	private Button addFrequencyButton;
	private InputSignalModel signalModel;
	private TimeDomainGraph tdGraph1;
	private JFXTimeDomainGraph jfxGraph;
	private boolean sliderReleased = true;
	private boolean sliderBlocked = false;
	private double minGain = -150;
	private double maxGain = 150;
	private double minGainSlider = -5;
	private double maxGainSlider = 40;
	private double minFreq = 2;
	private double maxFreq = 300e6;
 
 
    EventHandler<ActionEvent> addFrequencyButtonActionEventHandler = new EventHandler<ActionEvent>() {
       @Override 
       public void handle(ActionEvent e) {
    	   Cosine cs = signalModel.getSignalList().get(0);
    	   signalModel.addCosine(cs.getFrequency(), cs.getAmplitude());
    	   refreshFrequencyList();
       }
    };
    
    EventHandler<ActionEvent> deleteFrequencyButtonActionEventHandler = new EventHandler<ActionEvent>() {
        @Override 
        public void handle(ActionEvent e) { 
     	   Button button = (Button)e.getSource();
     	   String[] parts = button.getId().split("DelButton");
     	   int index = Integer.parseInt(parts[1]);
     	   signalModel.removeCosine(index);
     	   refreshFrequencyList();
        } 
     }; 
    
	@Override
	public void initialize(final URL url, final  ResourceBundle rb) {
		addFrequencyButton = new Button("add Frequency...");
		addFrequencyButton.setOnAction(addFrequencyButtonActionEventHandler);
		signalModel = Context.getInstance().getAmplificationModel().getInputSignalModel();
		signalModel.loadDefault();
		
		lineChart.setAnimated(false);
		((NumberAxis)lineChart.getXAxis()).setTickLabelFormatter(Utility.getTickLabelFormatterSecond());
		((NumberAxis)lineChart.getYAxis()).setTickLabelFormatter(Utility.getTickLabelFormatterVolt());
		tdGraph1 = new TimeDomainGraph(signalModel.getSignal());
		jfxGraph = new JFXTimeDomainGraph(tdGraph1, lineChart);
		de.thkoeln.intermodulationdemo.model.ChangeListener signalChangeListener = new de.thkoeln.intermodulationdemo.model.ChangeListener() {
			@Override
			public void onChange() {
				tdGraph1.refreshSignal(signalModel.getSignal(),signalModel.getfMin().getFrequency());
				jfxGraph.refreshChart();
			}
		};
		signalModel.addSoftListener(signalChangeListener);
		centerVBox.getChildren().clear();
		refreshFrequencyList();
		this.initSlider();
	}
	
	private void initSlider() {
		InputControlItem gainInput = new InputControlItem("Gain", "dB",this.minGainSlider, this.maxGainSlider, 0.0, false);
		gainInput.getTextFieldStackPane().setMaxWidth(80);
		Slider slider = new Slider();
		slider.getStyleClass().add("");
		
		slider.setMin(-5);
		slider.setMax(40);
		slider.setValue(0);
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		slider.setMajorTickUnit(5);
		slider.setBlockIncrement(0.01);
		slider.setSnapToTicks(true);
		slider.setLabelFormatter(Utility.getTickLabelFormatterDB());
		
		HBox inputLineHBox = new HBox();
		Label emptyLabel1 = new Label("");
		emptyLabel1.setMinWidth(27);
		inputLineHBox.getChildren().add(emptyLabel1);
		inputLineHBox.getChildren().add(gainInput.getTextFieldStackPane());
		
		gainVBox.getChildren().add(inputLineHBox);
		gainVBox.getChildren().add(slider);
		
		gainInput.addChangeListener(new MyChangeListener() {
			@Override
			public void onChange() {
				signalModel.setGain(gainInput.getValue());
				if(sliderReleased) {
					slider.setValue(gainInput.getValue());
				}
			}
		});
		
		slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                    gainInput.setValue(Math.round(new_val.doubleValue()));
                    gainInput.notifyListeners();
                    if(sliderReleased && sliderBlocked) {
                    	signalModel.notifyChangeListeners();
                    	sliderBlocked = false;
                    }
            }
        });
		
		slider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(
                    ObservableValue<? extends Boolean> observableValue,
                    Boolean wasChanging,
                    Boolean changing) {
            	if (changing) {
            		gainInput.setValue(slider.getValue());
            		sliderReleased = false;
            		sliderBlocked = true;
            	}
            	else {
            		gainInput.setValue(slider.getValue());
                	sliderReleased = true;
                }
            }
        });
	}
	
	public void refreshFrequencyList() {
		centerVBox.getChildren().clear();
		//TODO delete all InputElements
		int i = 0;
		for(Cosine cs : signalModel.getSignalList()) {
			addToFrequencyList(cs, i);
			i++;
		}
		if (i > 5) {
			addFrequencyButton.setDisable(true);
		} else {
			addFrequencyButton.setDisable(false);
		}
		HBox inputLineHBox = new HBox();
		Label emptyLabel = new Label("");
		emptyLabel.setMinWidth(24);
		inputLineHBox.getChildren().add(emptyLabel);
		inputLineHBox.getChildren().add(addFrequencyButton);
		centerVBox.getChildren().add(inputLineHBox);
	}
	
	private void addToFrequencyList(Cosine cs, int index) {
		HBox inputLineHBox = new HBox();
		inputLineHBox.getStyleClass().add("input-hbox");
		LatexViewFixed latexLabel = new LatexViewFixed();
		latexLabel = new LatexViewFixed("\\textcolor{255,255,255}{f_{"+(index+1)+"} }");
		latexLabel.setSize(18);
		latexLabel.getStyleClass().add("input-label");
		inputLineHBox.getChildren().add(latexLabel);
		InputControlItem inputControllerFrequency = new InputControlItem("Frequency","Hz", this.minFreq, this.maxFreq, cs.getFrequency());
		inputLineHBox.getChildren().add(inputControllerFrequency.getTextFieldStackPane());
		InputControlItem inputControllerAmplitude = new InputControlItem("Amplitude", "dBmV",this.minGain, this.maxGain, Utility.vtodBmV(cs.getAmplitude()), false);
		inputLineHBox.getChildren().add(inputControllerAmplitude.getTextFieldStackPane());
		if(index != 0) {
			
			Button delButton = new Button("\u2715");
			delButton.getStyleClass().remove("button");
			delButton.getStyleClass().add("button-trashcan");
			
			delButton.setTooltip(new Tooltip("delete"));
			
			EventHandler<ActionEvent> deleteHandler = new EventHandler<ActionEvent>() {
				@Override 
				public void handle(ActionEvent e) { 
					signalModel.removeCosine(index);
					refreshFrequencyList();
				} 
			};
			delButton.setOnAction(deleteHandler);
			inputLineHBox.getChildren().add(delButton);
		}
		centerVBox.getChildren().add(inputLineHBox);

		MyChangeListener changeListener = new MyChangeListener() {
			@Override
			public void onChange() {
				signalModel.editCosine(index, inputControllerFrequency.getValue(), Utility.dBmVtoV(inputControllerAmplitude.getValue()));
				signalModel.notifyChangeListeners();
			}
		};
		inputControllerFrequency.addChangeListener(changeListener);
		inputControllerAmplitude.addChangeListener(changeListener);
	}
}
