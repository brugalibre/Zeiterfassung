/**
 * 
 */
package com.myownb3.dominic.util.utils;

import java.awt.Component;
import java.awt.Font;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * @author Dominic
 * 
 */
public class StringUtil
{
   private static final JComponent fontMetricComp;

   public static boolean isValid (String toCheck)
   {
      return toCheck != null && !toCheck.equals ("") && !toCheck.equals (" ") && !toCheck.equals (" ");
   }

   static
   {
      fontMetricComp = new JPanel ();
   }

   /**
    * @param amount
    * @return a String that consist only of the unicode "\u0020" character
    */
   public static String getSpace (int amount)
   {
      StringBuilder space = new StringBuilder ();

      for (int i = 0; i < amount; i++)
      {
         space.append ("\u0020");
      }
      return space.toString ();
   }

   /**
    * @param stringToDecode , the string to decode in utf-8
    * @return an in UTF-8 decoded version of the given string
    */
   public static String getDecodedString (String stringToDecode)
   {
      return getDecodedString (stringToDecode, "UTF-8");
   }

   /**
    * Decodes the given String with the given encoding
    * @param stringToDecode
    * @param encoding
    * @return an encoded String
    */
   public static String getDecodedString (String stringToDecode, String encoding)
   {
      try
      {
         return new String (stringToDecode.getBytes ("ISO-8859-1"), encoding);
      }
      catch (UnsupportedEncodingException e)
      {
         return stringToDecode;
      }
   }

   /**
    * Separates an entire string in multiple fragments.
    * 
    * @param stringToSeparat , the string to separate
    * @param separator , the separator which separates the different fragments
    * @return an ArrayList with the separated elements
    */
   public static ArrayList<String> separateString (String stringToSeparat, String separator)
   {
      ArrayList<String> fragmentList = new ArrayList<String> ();
      if (null == stringToSeparat) return fragmentList;

      boolean finish = !stringToSeparat.contains (separator);
      int indexOfSeparator = stringToSeparat.indexOf (separator);
      String fragment = null;

      while (!finish)
      {
         indexOfSeparator = stringToSeparat.indexOf (separator); // get the index of the underline

         if (indexOfSeparator != -1)
         {
            // get the parameter
            fragment = stringToSeparat.substring (0, indexOfSeparator);
            stringToSeparat = stringToSeparat.substring (indexOfSeparator + 1);
         }
         else
         {
            // because there is no underline left, the process must be finished!
            finish = true;
            fragment = stringToSeparat;
         }
         fragmentList.add (fragment);
      }
      return fragmentList;
   }

   public static String getStringPlacedWithinSpace (String viewName, int totalSpaceLenght, int spaceElementLength)
   {

      int amountOfSpaceElements = totalSpaceLenght / spaceElementLength;
      String space = getSpace (amountOfSpaceElements);
      return String.format ("%" + amountOfSpaceElements + "s", space);
   }

   /**
    * Returns the height of the given font
    * @param font
    * @return the height of the given font
    */
   public static int getStringHeight (Font font)
   {
      return fontMetricComp.getFontMetrics (font).getHeight ();
   }

   /**
    * Returns the length of the given String, with the given {@link Font}
    * @param string
    * @param the {@link Component} whose {@link FontMetrics} is used
    * @return the length of the given String, with the given Font
    */
   public static int getStringLength (String string, Component comp)
   {
      return comp.getFontMetrics (comp.getFont ()).stringWidth (string);
   }

   /**
    * Returns the length of the given String, with the given {@link Font}
    * @param string
    * @param font
    * @return the length of the given String, with the given Font
    */
   public static int getStringLength (String string, Font font)
   {
      return fontMetricComp.getFontMetrics (font).stringWidth (string);
   }
}
