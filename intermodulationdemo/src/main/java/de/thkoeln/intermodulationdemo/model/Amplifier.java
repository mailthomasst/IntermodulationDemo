/**
 * Amplifier interface
 * author: Thomas Stein
 */

package de.thkoeln.intermodulationdemo.model;

import java.util.List;
import java.util.Set;

public interface Amplifier {
	public void amplify();
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
	public DSPSignal getAmplifiedSpectrum(DSPSignal inputSignal);
	public DSPSignal getAmplifiedSpectrum(List<Cosine> inputSignal);
	public double getNoiseLevel();
}
