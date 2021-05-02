package de.thkoeln.intermodulationdemo.view;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.thkoeln.intermodulationdemo.App;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

public class ChartPrinter {
	public static void saveAsPng(AnchorPane anchorPane) throws IOException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "image files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(App.stage);
        if (file != null) {
            String fileName = file.getName();
            if (!fileName.toUpperCase().endsWith(".PNG")) {
                file = new File(file.getAbsolutePath() + ".png");
            }
           // Scene printScene = new Scene(new Group(), 595, 400);
            //stage.setTitle("Charts Example");
            //((Group) printScene.getRoot()).getChildren().add(anchorPane);
            //Saving the scene as image
            //WritableImage image = printScene.snapshot(null);
            WritableImage image =anchorPane.snapshot(new SnapshotParameters(), null);
            ImageIO.write(SwingFXUtils.fromFXImage(image, null),
                    "png", file);
        }
    }
}
