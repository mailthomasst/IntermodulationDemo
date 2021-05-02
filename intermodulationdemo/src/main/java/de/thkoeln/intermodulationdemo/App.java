/**
 * Launcher for the Application
 * author: Thomas Stein
 */

package de.thkoeln.intermodulationdemo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
 
public class App extends Application {
	
    private double xOffset = 0;
    private double yOffset = 0;
    public static Stage stage = null;

    public static void main(String[] args) {
        launch();
    }
    
	@Override
	public void start(Stage stage) throws Exception {
		try {
			App.stage = stage;
			Parent root = FXMLLoader.load(App.class.getResource("view/MainApplicationFrame.fxml"));
			Scene scene = new Scene(root);
            root.setOnMousePressed(new EventHandler<MouseEvent>() {
            	@Override
            	public void handle(MouseEvent event) {
            		xOffset = event.getSceneX();
            		yOffset = event.getSceneY();
            	}
            });
            root.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                        if(mouseEvent.getClickCount() == 2){
                        	if(((Stage)((Parent)mouseEvent.getSource()).getScene().getWindow()).isMaximized()) {
        						((Stage)((Parent)mouseEvent.getSource()).getScene().getWindow()).setMaximized(false);
        					} else {
        						((Stage)((Parent)mouseEvent.getSource()).getScene().getWindow()).setMaximized(true);
        					}
                        }
                    }
                }
            });
       
            root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            	@Override
            	public void handle(MouseEvent event) {
            		stage.setX(event.getScreenX() - xOffset);
            		stage.setY(event.getScreenY() - yOffset);
            	}
            });
			scene.getStylesheets().add((App.class.getResource("view/css.css")).toExternalForm());
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setScene(scene);
			stage.setTitle("TestInterfaceI");
			stage.show();
			
		} catch (Exception e) {
			System.out.print(e);
		}
	}
	
	public static Stage getStage() {
		return App.stage;
	}
}
