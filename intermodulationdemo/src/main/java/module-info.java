module intermodulationdemo {
    requires javafx.controls;
    requires javafx.fxml;
	requires commons.math3;
	requires javafx.web;
	requires org.jfree.fxgraphics2d;
	requires jlatexmath;
	requires javafx.graphics;
	requires java.desktop;
	requires javafx.swing;

    opens de.thkoeln.intermodulationdemo to javafx.fxml;
    exports de.thkoeln.intermodulationdemo;
}