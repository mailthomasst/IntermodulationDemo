package de.thkoeln.intermodulationdemo.view;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import de.thkoeln.intermodulationdemo.util.LatexViewFixed;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.shape.Line;

public class LineChartWithMarkers<X,Y> extends LineChart {

    private ObservableList<Data<X, Y>> horizontalMarkers;
    private Set<VerticalMarker> verticalMarkers;

    public LineChartWithMarkers(Axis<X> xAxis, Axis<Y> yAxis) {
        super(xAxis, yAxis);
        horizontalMarkers = FXCollections.observableArrayList(data -> new Observable[] {data.YValueProperty()});
        verticalMarkers = new HashSet<VerticalMarker>();
    }

    public void addHorizontalValueMarker(Data<X, Y> marker) {
        Objects.requireNonNull(marker, "the marker must not be null");
        if (horizontalMarkers.contains(marker)) return;
        Line line = new Line();
        marker.setNode(line );
        getPlotChildren().add(line);
        horizontalMarkers.add(marker);
    }

    public void removeHorizontalValueMarker(Data<X, Y> marker) {
        Objects.requireNonNull(marker, "the marker must not be null");
        if (marker.getNode() != null) {
            getPlotChildren().remove(marker.getNode());
            marker.setNode(null);
        }
        horizontalMarkers.remove(marker);
    }
    
  public void addVerticalValueMarker(VerticalMarker vMarker) {
	  Objects.requireNonNull(vMarker, "the marker must not be null");
	  if (verticalMarkers.contains(vMarker)) return;
	  Line line = new Line();
	  line.getStyleClass().add("vertical-marker");
	  if(vMarker.getDashed()) line.getStrokeDashArray().addAll(4d, 4d);
	  line.setStroke(vMarker.getColor());
	  vMarker.getMarkerData().setNode(line );
	  getPlotChildren().add(line);
	  
	  //String ltxColor = ""+(int)(vMarker.getColor().getRed() * 255)+","
	  //		  		  +(int)(vMarker.getColor().getGreen() * 255)+","
	  //				  +(int)(vMarker.getColor().getBlue() * 255);
	  
	  //OVERRIDE BY BLACK
	  String ltxColor = ""+(int)(0)+","
	  		  +(int)(0)+","
			  +(int)(0);
	  
	  LatexViewFixed LatexViewFixed = new LatexViewFixed("\\textcolor{"+ltxColor+"}{\\textbf{$"+vMarker.getText()+"$}}");
	  LatexViewFixed.setSize(18);

	  vMarker.getLabelData().setNode(LatexViewFixed);
      getPlotChildren().add(LatexViewFixed);
  
      verticalMarkers.add(vMarker);
  }
  
  public void removeVerticalValueMarker(VerticalMarker vMarker) {
	  Objects.requireNonNull(vMarker, "the marker must not be null");
	  getPlotChildren().remove(vMarker.getMarkerData().getNode());
	  vMarker.getMarkerData().setNode(null);
	  getPlotChildren().remove(vMarker.getLabelData().getNode());
	  vMarker.getLabelData().setNode(null);
	  
	  verticalMarkers.remove(vMarker);
  }

    @Override
    protected void layoutPlotChildren() {
        super.layoutPlotChildren();
        for (Data<X, Y> horizontalMarker : horizontalMarkers) {
            Line line = (Line) horizontalMarker.getNode();
            line.setStartX(0);
            line.setEndX(getBoundsInLocal().getWidth());
            line.setStartY(getYAxis().getDisplayPosition(horizontalMarker.getYValue()) + 0.5); // 0.5 for crispness
            line.setEndY(line.getStartY());
            line.toFront();
        }
        for (VerticalMarker verticalMarker : verticalMarkers) {
            Line line = (Line) verticalMarker.getMarkerData().getNode();
            line.setStartX(getXAxis().getDisplayPosition(verticalMarker.getMarkerData().getXValue()) + 0.5);  // 0.5 for crispness
            line.setEndX(line.getStartX());
            line.setStartY(0d);
            line.setEndY(getBoundsInLocal().getHeight());
            line.toFront();
            
            LatexViewFixed ltxView = (LatexViewFixed) verticalMarker.getLabelData().getNode();
            ltxView.setRotate(-90);
            ltxView.setLayoutX(getXAxis().getDisplayPosition(verticalMarker.getLabelData().getXValue())-ltxView.getLayoutBounds().getWidth()/2-ltxView.getLayoutBounds().getHeight()/2);
            ltxView.setLayoutY(ltxView.getLayoutBounds().getWidth()-ltxView.getLayoutBounds().getHeight()/2);
            ltxView.toFront();
            }
        }   

}
