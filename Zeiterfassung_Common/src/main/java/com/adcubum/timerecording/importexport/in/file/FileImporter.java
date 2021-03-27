package com.adcubum.timerecording.importexport.in.file;

import java.io.File;
import java.util.List;

/**
 * The {@link FileImporterImpl} is an abstraction of an actual implementation which imports a text based {@link File}
 * 
 * @author Dominic
 *
 */
@FunctionalInterface
public interface FileImporter {

   public static final FileImporter INTANCE = new FileImporterImpl();

   /**
    * Imports the given File and returns a {@link List} of Strings, one for each line in the file
    * 
    * @param file
    *        the {@link File} to import
    * @return a {@link List} of Strings, one for each line in the file
    */
   List<String> importFile(File file);

}
