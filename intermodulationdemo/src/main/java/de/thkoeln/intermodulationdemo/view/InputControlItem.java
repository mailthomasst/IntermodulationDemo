package de.thkoeln.intermodulationdemo.view;

import javafx.scene.layout.StackPane;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.thkoeln.intermodulationdemo.App;
import de.thkoeln.intermodulationdemo.MyChangeListener;
import de.thkoeln.intermodulationdemo.util.LatexViewFixed;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;

/**
 * Custom JavaFX Control for Textfields
 *
 * @author Thomas Stein
 */

public class InputControlItem {
	private StackPane textFieldStackPane;
	private TextField textField;
	private Label label;
	LatexViewFixed latexLabel;
	private boolean latex;
	private boolean prefix;
	private String input;
	private String unit;
	private Double value;
	private Double minValue;
	private Double maxValue;
	private boolean valueChanged;
	private boolean valid;
	private Set<MyChangeListener> changeListeners;

	public InputControlItem(String label, String unit, double minValue, double maxValue, double value) {
		this(label, unit, minValue, maxValue, value, true);
	}

	public InputControlItem(String label, String unit, double minValue, double maxValue, double value, boolean prefix) {
		this(label, unit, minValue, maxValue, value, prefix, false);
	}

	public InputControlItem(String label, String unit, double minValue, double maxValue, double value, boolean prefix, boolean latex) {
		changeListeners = new HashSet<MyChangeListener>();
		valueChanged = false;
		this.value = null;
		this.latex = latex;
		this.prefix = prefix;
		this.minValue = minValue;
		this.maxValue = maxValue;

		this.unit = unit;
		this.textFieldStackPane = new StackPane();
		this.textField = new TextField();
		this.label = new Label();

		// Textfield
		this.textField.getStyleClass().add("input-control-text-field");
		this.textField.getStyleClass().remove("text-field");
		textField.setMinWidth(80);
		textField.setPrefWidth(80);

		textField.setOnKeyPressed(new EventHandler<KeyEvent>()
	    {
	        @Override
	        public void handle(KeyEvent ke)
	        {
	            if (ke.getCode().equals(KeyCode.ENTER))
	            {
	            	((TextField)textField).getScene().getRoot().requestFocus();;
	            }
	        }
	    });
		
		textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				if (!newPropertyValue) {
					input = new String(textField.getText());
					Double newValue = validateInput();
					if (newValue != null) {
						setValid(true);
						setValue(newValue);
						if (valueChanged) {
							notifyListeners();
							valueChanged = false;
						}
					} else {
						setValid(false);
					}
				}
			}
		});
		textField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String oldPropertyValue,
					String newPropertyValue) {
				// TODO: Check for parse error
			}
		});

		// Label
		this.label.getStyleClass().add("input-control-label");
		this.label.setText(label);
		this.label.setMinWidth(80);
		this.label.setPrefWidth(80);

		if (latex) {
			this.latexLabel = new LatexViewFixed("\\textcolor{0,0,0}{" + label + "}");
			this.latexLabel.setSize(9);
			this.latexLabel.getStyleClass().add("input-control-label");
		}

		// StackPane
		this.textFieldStackPane.getStyleClass().add("input-control-stack-pane");
		this.textFieldStackPane.setAlignment(Pos.TOP_LEFT);

		if (latex) {
			this.textFieldStackPane.getChildren().add(this.latexLabel);
		} else {
			this.textFieldStackPane.getChildren().add(this.label);
		}
		this.textFieldStackPane.getChildren().add(this.textField);

		setValue(value);
	}

	private Double validateInput() {
		Pattern p = Pattern.compile("^(([+-])?[0-9]*([\\.,][0-9]*)?)\\s?([apnµumkMG])?" + unit);
		Matcher m = p.matcher(input);
		if (m.matches()) {
			double number = Double.parseDouble(m.group(1).replace(',', '.'));
			String prefix = m.group(4);
			double value = number * getMultiplicatorFromPrefex(prefix);
			if (value > this.maxValue || value < this.minValue) {
				Point2D point = this.textField.localToScene(70.0, 20.0);
				Tooltip tt = new Tooltip("Value must be in the Range of "+this.minValue+" to "+this.maxValue);
				tt.setAutoHide(true);
				this.textField.setTooltip(tt);
				tt.show(App.getStage(),point.getX()
				        + textField.getScene().getX() + textField.getScene().getWindow().getX(), point.getY()
				        + textField.getScene().getY() + textField.getScene().getWindow().getY());
				return null;
			}
			this.textField.setTooltip(null);
			return value;
		}
		Point2D point = this.textField.localToScene(70.0, 20.0);
		Tooltip tt = new Tooltip("Can't parse text. Unit must be "+this.unit);
		tt.setAutoHide(true);
		this.textField.setTooltip(tt);
		tt.show(App.getStage(),point.getX()
		        + textField.getScene().getX() + textField.getScene().getWindow().getX(), point.getY()
		        + textField.getScene().getY() + textField.getScene().getWindow().getY());
		return null;
	}

	private double getMultiplicatorFromPrefex(String prefix) {

		if (prefix == null || prefix.equals("") || prefix.equals(" ")) {
			return 1.0;
		} else if (prefix.equals("k") || prefix.equals("K")) {
			return 1000.0;
		} else if (prefix.equals("M")) {
			return 1000000.0;
		} else if (prefix.equals("G") || prefix.equals("g")) {
			return 1000000000.0;
		} else if (prefix.equals("m")) {
			return 0.001;
		} else if (prefix.equals("\\u03BC") || prefix.equals("u")) {
			return 0.000001;
		} else if (prefix.equals("n")) {
			return 0.000000001;
		} else if (prefix.equals("p")) {
			return 0.000000000001;
		} else {
			return 1.0;
		}
	}

	public void setValue(double value) {
		if (this.value != null && this.value != value) {
			this.valueChanged = true;
		}
		this.value = value;
		String prefix = "";
		double number;

		if (this.prefix) {
			if (Math.abs(value) / 1000000000.0 >= 1.0) {
				prefix = "G";
				number = value / 1000000000.0;
			} else if (Math.abs(value) / 1000000.0 >= 1.0) {
				prefix = "M";
				number = value / 1000000.0;
			} else if (Math.abs(value) / 1000.0 >= 1.0) {
				prefix = "k";
				number = value / 1000.0;
			} else if (Math.abs(value) >= 1.0) {
				prefix = "";
				number = value;
			} else if (Math.abs(value) * 1000.0 >= 1.0) {
				prefix = "m";
				number = value * 1000.0;
			} else if (Math.abs(value) * 1000000.0 >= 1.0) {
				prefix = "\u03BC";
				number = value * 1000000.0;
			} else if (Math.abs(value) * 1000000000.0 >= 1.0) {
				prefix = "n";
				number = value * 1000000000.0;
			} else {
				prefix = "p";
				number = value * 1000000000000.0;
			}
		} else {
			prefix = "";
			number = value;
		}
		String numberString = Double.toString(number);
		Pattern p = Pattern.compile("^([+-]?[0-9]*)(\\.[0-9]*)?");
		Matcher m = p.matcher(numberString);

		if (m.matches() && m.group(2).equals(".0")) {
			numberString = m.group(1);
		}
		String text = numberString + " " + prefix + this.unit;
		textField.setText(text);
		// TODO \u2009 für leerzeichen
	}

	public void notifyListeners() {
		for (MyChangeListener listener : changeListeners) {
			listener.onChange();
		}
	}

	public void setValid(boolean valid) {
		if (valid) {
			this.textFieldStackPane.getStyleClass().remove("input-control-stack-pane-invalid");
			this.textFieldStackPane.getStyleClass().add("input-control-stack-pane");
		} else {
			this.textFieldStackPane.getStyleClass().remove("input-control-stack-pane");
			this.textFieldStackPane.getStyleClass().add("input-control-stack-pane-invalid");
		}
		this.valid = valid;
	}

	public double getValue() {
		return value;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Set<MyChangeListener> getChangeListeners() {
		return changeListeners;
	}

	public void setChangeListeners(Set<MyChangeListener> changeListeners) {
		this.changeListeners = changeListeners;
	}

	public void addChangeListener(MyChangeListener listener) {
		this.changeListeners.add(listener);
	}

	public void removeChangeListener(MyChangeListener listener) {
		this.changeListeners.remove(listener);
	}

	public boolean isValid() {
		return valid;
	}

	public StackPane getTextFieldStackPane() {
		return textFieldStackPane;
	}

	public void setTextFieldStackPane(StackPane textFieldStackPane) {
		this.textFieldStackPane = textFieldStackPane;
	}

	public TextField getTextField() {
		return textField;
	}

	public void setTextField(TextField textField) {
		this.textField = textField;
	}

	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label = label;
	}
}
