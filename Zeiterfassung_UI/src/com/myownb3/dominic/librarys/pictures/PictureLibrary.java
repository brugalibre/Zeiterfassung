/**
 * 
 */
package com.myownb3.dominic.librarys.pictures;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

/**
 * @author Dominic
 * 
 */
public class PictureLibrary {
    private static final String PATH = "/com/myownb3/dominic/librarys/pictures/res";

    private static boolean loaded;
    private static Image workingImageIcon;
    private static Image notWorkingImageIcon;
    private static javafx.scene.image.Image clockImageIcon;

    /**
     * Loads the pictures. Therefore it creates an entity of itself to load the
     * pictures in a none static method
     */
    public static void loadPictures() {
	if (loaded)
	    return;
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

	setClockImageIcon(new javafx.scene.image.Image(PATH + "/clock.png"));
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

    public static javafx.scene.image.Image getClockImageIcon() {
	return clockImageIcon;
    }

    public static void setClockImageIcon(javafx.scene.image.Image clockImageIcon) {
	PictureLibrary.clockImageIcon = clockImageIcon;
    }
}
