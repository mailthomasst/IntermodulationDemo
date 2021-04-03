/**
 * Class for modeling the amplification process
 * author: Thomas Stein
 */

package de.thkoeln.intermodulationdemo.model;

import java.util.ArrayList;
import java.util.List;

import de.thkoeln.intermodulationdemo.util.PropertyLoader;

public class AmplificationModel {
	private InputSignalModel inputSignalModel;
	private OutputSignalModel outputSignalModel;
	private Amplifier amp;
	private List<Amplifier> amps;

	public AmplificationModel() {
		int samplesPow = PropertyLoader.getIntProp("def.SamplesPow");
		double I_S = PropertyLoader.getDoubleProp("def.I_S");
		double V_T = PropertyLoader.getDoubleProp("def.V_T");
		double r_BE = PropertyLoader.getDoubleProp("def.r_BE");
		double R_C = PropertyLoader.getDoubleProp("def.R_C");
		double R_S = PropertyLoader.getDoubleProp("def.R_S");
		double R_L = PropertyLoader.getDoubleProp("def.R_L");
		double T_0 = PropertyLoader.getDoubleProp("def.T_0");
		double D_f = PropertyLoader.getDoubleProp("def.D_f");
		double F = PropertyLoader.getDoubleProp("def.F");
		double V_BE = PropertyLoader.getDoubleProp("def.V_BE");
		double B = PropertyLoader.getDoubleProp("def.B");
		
		inputSignalModel = new InputSignalModel((int)Math.pow(2, samplesPow));
		outputSignalModel = new OutputSignalModel((int)Math.pow(2, samplesPow));
		amps = new ArrayList<Amplifier>();
		amps.add(new IdealAmplifier(inputSignalModel, outputSignalModel, I_S, V_T, r_BE, R_C, R_S, R_L, T_0, D_f, F, V_BE, B));
		amp = amps.get(0);
		inputSignalModel.addChangeListener(new ChangeListener() {
			@Override
			public void onChange() {
				amp.amplify();
			}
		});
		amp.addChangeListener(new ChangeListener() {
			@Override
			public void onChange() {
				amp.amplify();
			}
		});
	}
	
	public void setAllListeners(ChangeListener cl) {
		for(Amplifier myAmp: amps) {
			myAmp.addChangeListener(cl);
		}
	}
	
	public void amplify() {
		amp.amplify();
	}

	public InputSignalModel getInputSignalModel() {
		return inputSignalModel;
	}
	
	public OutputSignalModel getOutputSignalModel() {
		return outputSignalModel;
	}

	public void setInputSignalModel(InputSignalModel inputSignalModel) {
		this.inputSignalModel = inputSignalModel;
	}

	public Amplifier getAmp() {
		return amp;
	}
	
	public void changeAmp(int index) {
		amp = amps.get(index);
	}
	
	public List<Amplifier> getAllAmps(){
		return amps;
	}
}
