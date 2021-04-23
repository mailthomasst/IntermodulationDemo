/**
 * Controller for the model-pane
 * author: Thomas Stein
 */

package de.thkoeln.intermodulationdemo;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import de.thkoeln.intermodulationdemo.model.Amplifier;
import de.thkoeln.intermodulationdemo.model.Context;
import de.thkoeln.intermodulationdemo.model.ModelParameter;
import de.thkoeln.intermodulationdemo.view.InputControlItem;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.web.WebView;

public class ModelPaneController implements Initializable {
	
	@FXML
	private Label titleLabel;
	
	@FXML
	private WebView webView;
	@FXML
	private FlowPane parameterList1;
	@FXML
	private FlowPane parameterList2;
	@FXML
	private FlowPane parameterList3;

	private Amplifier amplifierModel;
	private de.thkoeln.intermodulationdemo.model.ChangeListener modelChangeListener;
    
	@Override
	public void initialize(final URL url, final  ResourceBundle rb) {
		ObservableList<String> list = FXCollections.observableArrayList();
		List<Amplifier> amps = Context.getInstance().getAmplificationModel().getAllAmps();
		for(Amplifier amp : amps) {
			list.add(amp.getName());
		}
		ChoiceBox<String> myChoiceBox = new ChoiceBox<String>(list);
		myChoiceBox.setValue(amps.get(0).getName());
		myChoiceBox.setPrefWidth(250);
		
		myChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
		      @Override
		      public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
		        initAmp((Integer)number2);
		      }
		    });
		initAmp(0);
	}
	
	private void initAmp(int index) {
		amplifierModel = Context.getInstance().getAmplificationModel().getAllAmps().get(index);
		Context.getInstance().getAmplificationModel().changeAmp(index);
		refreshParameterList();
		amplifierModel.refreshModel();
		modelChangeListener = new de.thkoeln.intermodulationdemo.model.ChangeListener() {
			@Override
			public void onChange() {
				refreshParameterList();
			}
		};
		amplifierModel.addChangeListener(modelChangeListener);
		URL uri = ModelPaneController.class.getResource("view/"+amplifierModel.getGraphic());
		webView.getEngine().loadContent("<div class=\"img-div\"><img src='"+uri+"\' width='"+(webView.getPrefWidth()+180)+"'/></div>");

		webView.getEngine().setUserStyleSheetLocation(ModelPaneController.class.getResource("view/browser.css").toString());
		
		parameterList1.setPrefWrapLength(340);
		parameterList1.setVgap(3);
		parameterList1.setHgap(3);
		parameterList1.getStyleClass().add("model-flow-pane");
		
		parameterList2.setPrefWrapLength(340);
		parameterList2.setVgap(3);
		parameterList2.setHgap(3);
		parameterList2.getStyleClass().add("model-flow-pane");
		
		parameterList3.setPrefWrapLength(340);
		parameterList3.setVgap(3);
		parameterList3.setHgap(3);
		parameterList3.getStyleClass().add("model-flow-pane");
	}
	
	private void refreshParameterList() {
		
		parameterList1.getChildren().clear();
		parameterList2.getChildren().clear();
		parameterList3.getChildren().clear();
		for (ModelParameter modelParameter : amplifierModel.getModelParameter()) {
			InputControlItem inputItem = new InputControlItem(modelParameter.getLabel(), modelParameter.getUnit(),modelParameter.getMinValue(), modelParameter.getMaxValue(), modelParameter.getValue(),true, true);
			inputItem.addChangeListener(new MyChangeListener() {
				@Override
				public void onChange() {
					modelParameter.setValue(inputItem.getValue());
					amplifierModel.refreshModel();
				}
			});
			if (modelParameter.getGroup() == 1) parameterList1.getChildren().add(inputItem.getTextFieldStackPane());
			if (modelParameter.getGroup() == 2) parameterList2.getChildren().add(inputItem.getTextFieldStackPane());
			if (modelParameter.getGroup() == 3) parameterList3.getChildren().add(inputItem.getTextFieldStackPane());
		}
	}
	
}
