/**
 * Context Class for static access to recources
 * author: Thomas Stein
 */
package de.thkoeln.intermodulationdemo.model;

public class Context {
    private final static Context instance = new Context();

    private AmplificationModel amplificationModel;

    Context(){
    	amplificationModel = new AmplificationModel();
    }
    
    public static Context getInstance() {
        return instance;
    }

	public AmplificationModel getAmplificationModel() {
		return amplificationModel;
	}
	public void setAmplificationModel(AmplificationModel amplificationModel) {
		this.amplificationModel = amplificationModel;
	}
    
}
