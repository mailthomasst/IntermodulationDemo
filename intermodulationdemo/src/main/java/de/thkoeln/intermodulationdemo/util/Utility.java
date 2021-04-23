package de.thkoeln.intermodulationdemo.util;

import org.apache.commons.math3.complex.Complex;

import de.thkoeln.intermodulationdemo.model.DSPSignal;
import de.thkoeln.intermodulationdemo.model.idealsignal.CosFrequency;
import de.thkoeln.intermodulationdemo.model.idealsignal.CosSignal;
import javafx.util.StringConverter;

/**
 * Utility Class
 *
 * @author Thomas Stein
 */

public class Utility {
	
	public static double dBmtoV(double pdBm, double load) {
		double power = 0.001*Math.pow(10, (pdBm/10));
		double vRms = Math.sqrt(power*50);
		return vRms * Math.sqrt(2);
	}
	
	
	public static double vtodBmV(double volt) {
		return 20 * Math.log10(volt / 0.001);
	}

	public static double dBmVtoV(double dBmV) {
		return Math.pow(10, dBmV / 20) / 1000.0;
	}
	
	public static double dBVoltToFactor(double db) {
		return Math.pow(10, (db/20.0));
	}
	
	public static double wtodBm(double power) {
		return 10 * Math.log10(power / 0.001);
	}

	public static double dBmtoW(double dBm) {
		return Math.pow(10, dBm / 10) / 1000.0;
	}

	public static double dBtoAmplitude(double dB) {
		return Math.pow(10, dB / 20);
	}

	public static StringConverter<Number> getTickLabelFormatterSecond(){
		return (new StringConverter<Number>() {
			
	        @Override
	        public String toString(Number object) {
	    			String prefix = "";
	    			double number;
	    			double value = object.doubleValue();
	    			
	    			if (value == 0 || value < Math.pow(10, -20)) return 0+"s";
	    			
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
	    			number = (Math.round(number*10.0)/10.0);
	    			if (number == (int)number) {
	    				return (int)number + prefix+"s";
	    			} else {
	    				return number + prefix+"s";
	    			}
	        }
	        @Override
	        public Number fromString(String string) {
	            return 0;
	        }			
	    });
	}
	
	public static StringConverter<Number> getTickLabelFormatterVolt(){
		return (new StringConverter<Number>() {
			
	        @Override
	        public String toString(Number object) {
	    			String prefix = "";
	    			String minus = "";
	    			double number;
	    			if(object.doubleValue() < 0) minus = "-";
	    			double value = Math.abs(object.doubleValue());
	    			
	    			if (value == 0 || value < Math.pow(10, -20)) return 0+"V";
	    			
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
	    			number = (Math.round(number*10.0)/10.0);
	    			if (number == (int)number) {
	    				return minus+(int)number + prefix+"V";
	    			} else {
	    				return minus+number + prefix+"V";
	    			}
	        }
	        @Override
	        public Number fromString(String string) {
	            return 0;
	        }			
	    });
	}
	
	public static StringConverter<Number> getTickLabelFormatterHerz(){
		return (new StringConverter<Number>() {
			
	        @Override
	        public String toString(Number object) {
	    			String prefix = "";
	    			double number;
	    			double value = object.doubleValue();
	    			
	    			if (value == 0 || value < Math.pow(10, -20)) return 0+"Hz";
	    			
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
	    			number = (Math.round(number*10.0)/10.0);
	    			if (number == (int)number) {
	    				return (int)number + prefix+"Hz";
	    			} else {
	    				return number + prefix+"Hz";
	    			}
	        }
	        @Override
	        public Number fromString(String string) {
	            return 0;
	        }			
	    });
	}
	
	public static StringConverter<Double> getTickLabelFormatterDB(){
		return (new StringConverter<Double>() {
			
	        @Override
	        public String toString(Double object) {
	        	double number = (Math.round(object.doubleValue()*10.0)/10.0);
    			if (number == (int)number) {
    				return (int)number + "dB";
    			} else {
    				return number + "dB";
    			}
	        }
	        @Override
	        public Double fromString(String string) {
	            return 0.0;
	        }			
	    });
	}
	
	public static StringConverter<Number> getTickLabelFormatterDBm(){
		return (new StringConverter<Number>() {
			
	        @Override
	        public String toString(Number object) {
	        	double number = (Math.round(object.doubleValue()*10.0)/10.0);
    			if (number == (int)number) {
    				return (int)number + "dBm";
    			} else {
    				return number + "dBm";
    			}
	        }
	        @Override
	        public Double fromString(String string) {
	            return 0.0;
	        }			
	    });
	}
	
	public static int factorial(int n) {
	    int fact = 1;
	    for (int i = 2; i <= n; i++) {
	        fact = fact * i;
	    }
	    return fact;
	}
	
	public static DSPSignal frequenciesToSignal(CosSignal cs,int samples, int samplingRate) {
		
		DSPSignal outputSignal = new DSPSignal(samplingRate, samples, true);
		Complex signal[] = outputSignal.getSignal();
		double factor = 2*samples/(double)(samplingRate);
		for (int i = 0; i < samples; i++) {
			signal[i] = new Complex(1e-20);
		}
		for (CosFrequency cos : cs.getFreqSet()) {
			int index = (int) (cos.getFrequency()*factor);
			if (index < samples) {
				signal[index] = signal[index].add(new Complex(cos.getFactor()));
			}
		}
	    return outputSignal;
	}
}
