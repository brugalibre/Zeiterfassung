package com.adcubum.util.utils;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;

import javax.swing.filechooser.FileSystemView;

public class FileSystemUtil {
   private FileSystemUtil() {
      // private
   }

   /**
    * @return the path as String to the users home directory
    */
   public static String getHomeDir() {
      FileSystemView filesys = FileSystemView.getFileSystemView();
      File homeDirectory = filesys.getHomeDirectory();
      return homeDirectory.getPath();
   }

   /**
    * @return the default {@link FileSystem} separator depending on the current OS
    */
   public static String getDefaultFileSystemSeparator() {
      FileSystem fileSystem = FileSystems.getDefault();
      return fileSystem.getSeparator();
   }
}
