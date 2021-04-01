/**
 * 
 */
package com.adcubum.timerecording.librarys.pictures;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

/**
 * @author Dominic
 * 
 */
public class PictureLibrary {

   private static Image workingImage;
   private static Image notWorkingImage;
   private static javafx.scene.image.Image clockImageIcon;
   private static javafx.scene.image.Image warningIcon;
   private static ImageIcon notWorkingImageIcon;
   private static ImageIcon workingImageIcon;

   private PictureLibrary() {
      // Private constructor
   }

   /**
    * Loads the pictures. Therefore it creates an entity of itself to load the
    * pictures in a none static method
    */
   public static void loadPictures() {
      URL notWorkingUrl = PictureLibrary.class.getResource(getPath() + "/not_working.png");
      notWorkingImageIcon = new ImageIcon(notWorkingUrl);
      notWorkingImage = notWorkingImageIcon.getImage();

      URL workingUrl = PictureLibrary.class.getResource(getPath() + "/working.png");
      workingImageIcon = new ImageIcon(workingUrl);
      workingImage = workingImageIcon.getImage();
      clockImageIcon = new javafx.scene.image.Image(getPath() + "/clock.png");
      warningIcon = SVGReader.loadSVG(getPath() + "/warning.svg");
   }

   /**
    * @return a string which represents the path to the pictures
    */
   private static String getPath() {
      String path = "/" + PictureLibrary.class.getPackage().getName() + "/res";
      return path.replace(".", "/");
   }

   public static Image getWorkingImage() {
      return workingImage;
   }

   public static Image getNotWorkingImage() {
      return notWorkingImage;
   }

   public static ImageIcon getWorkingImageIcon() {
      return workingImageIcon;
   }

   public static ImageIcon getNotWorkingImageIcon() {
      return notWorkingImageIcon;
   }

   public static javafx.scene.image.Image getClockImageIcon() {
      return clockImageIcon;
   }

   public static javafx.scene.image.Image getWarningIcon() {
      return warningIcon;
   }
}
