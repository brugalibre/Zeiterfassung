package com.adcubum.timerecording.importexport.in.file;

import java.io.File;
import java.util.List;

/**
 * The {@link FileImporter} is responsible for importing a text based file. It does so by reading it's content line by line
 * resulting in returning a {@link List} of {@link String}s
 * 
 * @author Dominic
 *
 */
@FunctionalInterface
public interface FileImporter {

   /**
    * The file extension of files to import
    */
   public static final String FILE_EXTENSION = "csv";

   /**
    * Imports the given File and returns a {@link List} of Strings, one for each line in the file
    * 
    * @param file
    *        the {@link File} to import
    * @return a {@link List} of Strings, one for each line in the file
    */
   List<String> importFile(File file);

}
