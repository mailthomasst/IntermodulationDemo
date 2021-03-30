package de.thkoeln.intermodulationdemo;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.application.Platform;
import javafx.stage.Stage;

public class MainApplicationFrameController implements Initializable {
	
	@FXML
	private HBox titleBarHBox;
	
	@FXML
	private HBox titleBarHBoxSymbol;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Label text = new Label("Intermodulation Distortion Demonstration");
		text.setPrefHeight(28);
		text.setPrefWidth(300);
		text.setAlignment(Pos.CENTER);
		text.getStyleClass().add("label-text");
		titleBarHBoxSymbol.getChildren().add(text);
		
		//TODO
		Label minimizeButton = new Label("\u23E4");//IconGenerator.createIconLabel(new FontAwesomeIconView(FontAwesomeIcon.ADJUST));
		minimizeButton.setPrefHeight(28);
		minimizeButton.setPrefWidth(40);
		minimizeButton.setAlignment(Pos.CENTER);
		minimizeButton.getStyleClass().add("button-minimize");
		minimizeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				((Stage)((Label)arg0.getSource()).getScene().getWindow()).setIconified(true);
			}
		});
		titleBarHBox.getChildren().add(minimizeButton);
		
		//TODO
		Label restoreButton = new Label("\u20DE");//IconGenerator.createIconLabel(new FontAwesomeIconView(FontAwesomeIcon.ADJUST));
		restoreButton.setPrefHeight(28);
		restoreButton.setPrefWidth(40);
		restoreButton.setAlignment(Pos.CENTER);
		restoreButton.getStyleClass().add("button-maximize");
		restoreButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent arg0) {
					if(((Stage)((Label)arg0.getSource()).getScene().getWindow()).isMaximized()) {
						((Stage)((Label)arg0.getSource()).getScene().getWindow()).setMaximized(false);
					} else {
						((Stage)((Label)arg0.getSource()).getScene().getWindow()).setMaximized(true);
					}
				}
		});
		
		titleBarHBox.getChildren().add(restoreButton);
		
		Label closeButton = new Label("\u2715");//IconGenerator.createIconLabel(new FontAwesomeIconView(FontAwesomeIcon.CLOSE));
		closeButton.setPrefHeight(28);
		closeButton.setPrefWidth(40);
		closeButton.setAlignment(Pos.CENTER);
		closeButton.getStyleClass().add("button-close");
		closeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				Platform.exit();
			}
		});
		titleBarHBox.getChildren().add(closeButton);
	}

}
