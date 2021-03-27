package com.adcubum.timerecording.jira.defaulttickets;

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

   /**
    * Imports the given File and returns a {@link List} of Strings, one for each line in the file
    * 
    * @param file
    *        the {@link File} to import
    * @return a {@link List} of Strings, one for each line in the file
    */
   List<String> importFile(File file);

}
