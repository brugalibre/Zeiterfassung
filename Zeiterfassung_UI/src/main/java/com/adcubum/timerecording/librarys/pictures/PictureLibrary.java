/**
 * 
 */
package com.adcubum.timerecording.librarys.pictures;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;

/**
 * @author Dominic
 * 
 */
public class PictureLibrary {

   private static Image workingImage;
   private static Image notWorkingImage;
   private static javafx.scene.image.Image appIcon;
   private static javafx.scene.image.Image warningIcon;
   private static ImageIcon comeOrGoImageIcon;
   private static Image comeOrGoImage;
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

      URL comeOrGoImageIconUrl = PictureLibrary.class.getResource(getPath() + "/comeorgo.png");
      comeOrGoImageIcon = new ImageIcon(comeOrGoImageIconUrl);
      comeOrGoImage = comeOrGoImageIcon.getImage();

      URL workingUrl = PictureLibrary.class.getResource(getPath() + "/working.png");
      workingImageIcon = new ImageIcon(workingUrl);
      workingImage = workingImageIcon.getImage();

      appIcon = createAppIcon();

      warningIcon = SVGReader.loadSVG(getPath() + "/warning.svg");
   }

   private static javafx.scene.image.Image createAppIcon() {
      File appIconFile = new File("images/icon.png");
      if (appIconFile.exists()) {
        return new javafx.scene.image.Image(appIconFile.toURI().toString());
      }
      return new javafx.scene.image.Image(getPath() + "/clock.png");
   }

   /**
    * @return a string which represents the path to the pictures
    */
   private static String getPath() {
	  return "/images";
   }

   public static Image getWorkingImage() {
      return workingImage;
   }

   public static Image getNotWorkingImage() {
      return notWorkingImage;
   }

   public static ImageIcon getNotWorkingImageIcon() {
      return notWorkingImageIcon;
   }

   public static Image getComeOrGoImage() {
      return comeOrGoImage;
   }

   public static javafx.scene.image.Image getAppIcon() {
      return appIcon;
   }

   public static javafx.scene.image.Image getWarningIcon() {
      return warningIcon;
   }
}
