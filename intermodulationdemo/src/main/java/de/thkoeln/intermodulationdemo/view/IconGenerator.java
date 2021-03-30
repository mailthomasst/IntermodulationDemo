package de.thkoeln.intermodulationdemo.view;

//import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class IconGenerator {

    public final static String FONT_AWESOME_TTF_PATH = "de/thkoeln/intermodulationdemo/view/fontawesome-webfont.ttf";
    public final static String DEFAULT_ICON_SIZE = "20.0";
    public final static String DEFAULT_FONT_SIZE = "1em";

    static {
        Font.loadFont(IconGenerator.class.getResource(FONT_AWESOME_TTF_PATH).toExternalForm(), 10.0);
    }

//    public static Label createIconLabel(FontAwesomeIconView icon) {
//        return createIconLabel(icon, DEFAULT_ICON_SIZE);
//    }
//
//    public static Label createIconLabel(FontAwesomeIconView icon, String iconSize) {
//        Label label = new Label(icon.getText());
//        label.getStyleClass().add("awesome");
//        label.setStyle("-fx-font-family: FontAwesome; -fx-font-size: " + iconSize + ";");
//        return label;
//    }
//    
//    public static Button createIconButton(FontAwesomeIconView icon) {
//        return createIconButton(icon, DEFAULT_ICON_SIZE);
//    }
//    
//    public static Button createIconButton(FontAwesomeIconView icon, String iconSize) {
//    	Button button = new Button(icon.getText());
//    	button.getStyleClass().add("awesome");
//    	button.setStyle("-fx-font-family: FontAwesome; -fx-font-size: " + iconSize + ";");
//        return button;
//    }
}
