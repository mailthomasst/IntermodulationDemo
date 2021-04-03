/**
 * Interface for a simple Amplifier
 * author: Thomas Stein
 */

package de.thkoeln.intermodulationdemo.model;

import java.util.Set;
import org.apache.commons.math3.complex.Complex;

public interface SimpleAmplifier {
	public Complex amplify (Complex input);
	public void refreshModel();
	public Set<ModelParameter> getModelParameter();
	public void addChangeListener(ChangeListener cl);
	public void addChangeListener(Set<ChangeListener> clSet);
	public Set<ChangeListener> getChangeListeners();
	public void removeChangeListener(ChangeListener cl);
	public void removeAllChangeListener();
	public void notifyChangeListeners();
	public String getName();
	public String getGraphic();
}
