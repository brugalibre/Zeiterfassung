/**
 * 
 */
package com.myownb3.dominic.librarys;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

/**
 * @author Dominic
 * 
 */
public class PictureLibrary {
    private static final String PATH = getPath();

    private static boolean loaded;
    private static Image workingImageIcon;
    private static Image notWorkingImageIcon;
    private static Image clockImageIcon;

    private PictureLibrary(){
	// Private constructor
    }
    
    /**
     * Loads the pictures. Therefore it creates an entity of itself to load the
     * pictures in a none static method
     */
    public static void loadPictures() {
	if (loaded){
	    return;
	}
	new PictureLibrary().getPictures();
	loaded = true;
    }

    /**
     * loads the pictures from the directory
     */
    private void getPictures() {
	final URL notWorkingUrl = getClass().getResource(PATH + "/not_working.png");
	setNotWorkingImageIcon((new ImageIcon(notWorkingUrl).getImage()));

	final URL workingUrl = getClass().getResource(PATH + "/working.png");
	setWorkingImageIcon((new ImageIcon(workingUrl).getImage()));

	final URL clockUrl = getClass().getResource(PATH + "/clock.png");
	setClockImageIcon((new ImageIcon(clockUrl).getImage()));
    }

    /**
     * @return a string which represents the path to the pictures
     */
    private static String getPath() {
	String path = "/" + PictureLibrary.class.getPackage().getName() + "/pictures/res";
	return path.replace(".", "/");
    }

    public static Image getWorkingImageIcon() {
	return workingImageIcon;
    }

    public static void setWorkingImageIcon(Image working) {
	PictureLibrary.workingImageIcon = working;
    }

    public static Image getNotWorkingImageIcon() {
	return notWorkingImageIcon;
    }

    public static void setNotWorkingImageIcon(Image notWorking) {
	PictureLibrary.notWorkingImageIcon = notWorking;
    }

    public static Image getClockImageIcon() {
	return clockImageIcon;
    }

    public static void setClockImageIcon(Image clockImageIcon) {
	PictureLibrary.clockImageIcon = clockImageIcon;
    }
}
