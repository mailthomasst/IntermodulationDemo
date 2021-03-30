package de.thkoeln.intermodulationdemo.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import de.thkoeln.intermodulationdemo.model.idealsignal.CosFrequency;
import de.thkoeln.intermodulationdemo.model.idealsignal.CosSignal;
import de.thkoeln.intermodulationdemo.util.PropertyLoader;
import de.thkoeln.intermodulationdemo.util.Utility;

public class IdealAmplifier implements Amplifier {
	InputSignalModel inputSignal;
	OutputSignalModel outputSignal;
	private ModelParameter I_s;
	private ModelParameter V_T;
	private ModelParameter V_BE;
	
	private ModelParameter R_BE;
	private ModelParameter R_C;
	
	private ModelParameter B;
	private ModelParameter R_in;
	private ModelParameter R_out;
	
	private ModelParameter T_0;
	private ModelParameter D_f;
	private ModelParameter F;
	
	private Set<ModelParameter> modelParameters;
	private Set<ChangeListener> listeners;
	
	private FastFourierTransformer bumblebee;
	
	public Set<ModelParameter> getModelParameter() {
		return modelParameters;
	}

	public void setModelParameter(Set<ModelParameter> modelParameter) {
		this.modelParameters = modelParameter;
	}
	
	public IdealAmplifier(InputSignalModel inputSignal, OutputSignalModel outputSignal, double i_s, double v_T, double r_BE, double r_C, double r_in,double r_out, double t_0, double d_f, double f, double v_BE, double B) {
		super();
		this.inputSignal = inputSignal;
		this.outputSignal = outputSignal;
		this.listeners = new HashSet<ChangeListener>();
		this.modelParameters = new HashSet<ModelParameter>();
		this.I_s = new ModelParameter("I_s", "A", i_s, 10e-15,10e0);
		this.modelParameters.add(this.I_s);
		this.V_T = new ModelParameter("V_T", "V", v_T, 10e-6,10e0);
		this.modelParameters.add(this.V_T);
		this.R_BE = new ModelParameter("r_{BE}", "\u03A9", r_BE, 10e-3,10e9);
		this.modelParameters.add(this.R_BE);
		this.R_C = new ModelParameter("R'_{C}", "\u03A9", r_C, 10e-3,10e9);
		this.modelParameters.add(this.R_C);
		this.V_BE = new ModelParameter("V_{BE}", "V", v_BE, 10e-3,10e3);
		this.modelParameters.add(this.V_BE);
		
		this.B = new ModelParameter("B", "", B, 0.0,1000.0, 1);
		this.modelParameters.add(this.B);
		this.R_in = new ModelParameter("R_{S}", "\u03A9", r_in, 10e-6,10e9, 1);
		this.modelParameters.add(this.R_in);
		this.R_out = new ModelParameter("R_{L}", "\u03A9", r_out, 10e-6,10e9, 1);
		this.modelParameters.add(this.R_out);
		
		this.T_0 = new ModelParameter("T_0", "K", t_0, 0.0, 10e6, 2);
		this.modelParameters.add(this.T_0);
		this.D_f = new ModelParameter("\u0394 f", "Hz", d_f, 1, 10e9, 2);
		this.modelParameters.add(this.D_f);
		this.F = new ModelParameter("F", "dB", f, 0.0, 100.0, 2);
		this.modelParameters.add(this.F);
		
		this.bumblebee = new FastFourierTransformer(DftNormalization.STANDARD);
	}
	
	public void refreshModel() { 
		outputSignal.setNoiseLevel(getNoiseLevel());
		notifyChangeListeners();
	}

	public void amplify() {
		Complex[] input = inputSignal.getSignal().getSignal();
		DSPSignal outputDSPSignal = new DSPSignal(inputSignal.getSignal().getSamplingRate(), inputSignal.getSignal().getSamples());
		Complex[] output = outputDSPSignal.getSignal();
		
		for(int i = 0; i < output.length; i++) {
			output[i] = new Complex(amlifyValue(input[i].getReal()));
		}
		filterMean(output);
		
		outputSignal.setTimeSignal(outputDSPSignal);
		
		List<Cosine> cosList = inputSignal.getSignalList();
		CosSignal idealSignalIn = new CosSignal();
		for(Cosine cos : cosList) {
			double u_BE = Utility.dBtoAmplitude(inputSignal.getGain())*cos.getAmplitude()*R_BE.getValue()/(R_BE.getValue()+R_in.getValue());
			idealSignalIn.add(new CosFrequency("", u_BE/V_T.getValue(), cos.getFrequency()));
		}
		CosSignal idealSignalOut = new CosSignal();
		int taylorDepth = PropertyLoader.getIntProp("conf.taylorDepth");
		for (int i = 1 ; i <= taylorDepth; i++) {
			CosSignal cTmp = idealSignalIn.power(i);
			cTmp.multiply(Utility.factorial(i));
			idealSignalOut.add(cTmp);
		}
		double I_C = this.B.getValue()*I_s.getValue()*Math.exp((this.V_BE.getValue())/V_T.getValue());
		idealSignalOut.multiply((1/((1/R_C.getValue())+(1/R_out.getValue())))*I_C);
		
		outputSignal.setFrequencySignal(Utility.frequenciesToSignal(idealSignalOut,outputSignal.getSignal().getSamples()/2+1, outputSignal.getSignal().getSamplingRate()));
		outputSignal.setNoiseLevel(getNoiseLevel());
		outputSignal.notifyChangeListeners();
	}
	
	public double amlifyValue(double u_in) {
		double u_BE = u_in*R_BE.getValue()/(R_BE.getValue()+R_in.getValue());
		double i_C = this.B.getValue()*this.I_s.getValue()*Math.exp((this.V_BE.getValue()+u_BE)/V_T.getValue());
		double u_out = -1*(1/((1/R_C.getValue())+(1/R_out.getValue())))*i_C;

		return u_out;
	}
	
	public void filterMean(Complex[] input) {
		double sum = 0;
		for (Complex a : input) sum += a.getReal();
		double mean = sum/input.length;
		for(int i = 0; i < input.length; i++) {
			input[i] = input[i].add(-mean);
		}
	}
	

	public DSPSignal getAmplifiedSpectrum(DSPSignal inputSignal) {
		Complex[] input = inputSignal.getSignal();
		Complex[] output = new Complex[input.length];
		for(int i = 0; i < output.length; i++) {
			output[i] = new Complex(amlifyValue(input[i].getReal()));
		}
		filterMean(output);
		Complex[] complexFFTResult = bumblebee.transform(output,TransformType.FORWARD);
		int resultLength = (complexFFTResult.length / 2) + 1;	
		Complex[] fftoutputArray = new Complex[resultLength];
		for (int i = 0; i < resultLength; i++) {
			fftoutputArray[i] = new Complex(2*complexFFTResult[i].abs()/inputSignal.getSamples());
		}	
		DSPSignal outputDSPSignal = new DSPSignal(inputSignal.getSamplingRate(), resultLength);
		outputDSPSignal.setSignal(fftoutputArray);
		return outputDSPSignal;
	}
	
	public double getGain() {
		double in = 0.001;
		double out = Math.abs(amlifyValue(in))-Math.abs(amlifyValue(0));
		return Utility.vtodBmV(out);
	}
	
	@Override
	public double getNoiseLevel() {
		double k = PropertyLoader.getDoubleProp("const.k");
		
		double F = this.F.getValue();     	//Noise Figure in dB
		double T_0 = this.T_0.getValue(); 	//Temperature in K
		double D_f = this.D_f.getValue(); 	//Bandwidth in Hz
		double G = getGain();				//Gain in dB
		
		double Nin = Utility.wtodBm(k*T_0*D_f);	//Noise input (dBm)
		double Nout = F+G+Nin;
		
		return Nout;
	}
	
	@Override
	public String getName() {
		return "Simple Common Emitter";
	}

	@Override
	public String getGraphic() {
		return "AC_VoltageAmp.jpg";
	}

	@Override
	public void addChangeListener(Set<ChangeListener> clSet) {
		for (ChangeListener cl : clSet) {
			this.addChangeListener(cl);
		}
		
	}

	@Override
	public Set<ChangeListener> getChangeListeners() {
		return listeners;
	}

	@Override
	public void removeAllChangeListener() {
		listeners.clear();		
	}

	public void addChangeListener(ChangeListener cl) {
		listeners.add(cl);
	}
	
	public void removeChangeListener(ChangeListener cl) {
		listeners.remove(cl);
	}
	
	public void notifyChangeListeners() {
		for (ChangeListener cl : listeners) {
			cl.onChange();
		}
	}

}
