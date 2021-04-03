/**
 * Ideal Cosine Signal Name
 * author: Thomas Stein
 */

package de.thkoeln.intermodulationdemo.model.idealsignal;

import java.util.ArrayList;
import java.util.List;

public class CosName {
	List<Integer> factors;
	List<String> symbols;
	
	public CosName(List<Integer> factors, List<String> symbols) {
		super();
		this.factors = new ArrayList<Integer>();
		this.symbols = new ArrayList<String>();
	}

}
