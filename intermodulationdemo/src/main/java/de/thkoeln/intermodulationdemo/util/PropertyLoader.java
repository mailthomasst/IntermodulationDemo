package de.thkoeln.intermodulationdemo.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import de.thkoeln.intermodulationdemo.App;

public class PropertyLoader {
	
    private final static PropertyLoader instance = new PropertyLoader();
    
    public Properties prop;

    PropertyLoader(){   	
    	this.prop = new Properties();
    	String path = "config.properties";
    	String defaultPath = "config.properties";
    	URL defaultResource = App.class.getResource(defaultPath);
    	try {
    		this.prop.load(new FileReader(path));
		} catch (FileNotFoundException e) {
			try {
				this.prop.load(new FileReader(new File(defaultResource.toURI())));
			} catch (FileNotFoundException e2) {
				String wDir= "Can't find config.properties in Working Directory = " + System.getProperty("user.dir");
				System.out.println(wDir);
				e.printStackTrace();
			} catch (IOException e2) {
				String wDir= "Can't find config.properties in Working Directory = " + System.getProperty("user.dir");
				System.out.println(wDir);
				e.printStackTrace();
			} catch (URISyntaxException e1) {
				String wDir= "Can't find config.properties in Working Directory = " + System.getProperty("user.dir");
				System.out.println(wDir);
				e1.printStackTrace();
			}
		} catch (IOException e) {
			String wDir= "Can't find config.properties in Working Directory = " + System.getProperty("user.dir");
			System.out.println(wDir);
			e.printStackTrace();
		}
    }
    
    public static PropertyLoader getInstance() {
        return instance;
    }
	
    public static String getStringProp(String name) {
    	return PropertyLoader.getInstance().prop.getProperty(name);
    }
    
    public static double getDoubleProp(String name) {
    	return Double.parseDouble(PropertyLoader.getInstance().prop.getProperty(name));
    }
    
    public static int getIntProp(String name) {
    	return Integer.parseInt(PropertyLoader.getInstance().prop.getProperty(name));
    }

}
