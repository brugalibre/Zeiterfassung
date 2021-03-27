/**
 * 
 */
package com.adcubum.timerecording.jira.defaulttickets;

import static java.util.Objects.nonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * The {@link FileImporterImpl} imports a {@link File} and put its content line by line into a {@link List} of {@link String}
 * 
 * @author Dominic
 *
 */
public class FileImporterImpl implements FileImporter {

   /**
    * The file extension of files to import
    */
   public static final String FILE_EXTENSION = "csv";
   public static final FileImporter INTANCE = new FileImporterImpl();

   private FileImporterImpl() {
      // private Constructor
   }

   /**
    * Imports the given file and returns a list which contains each line of the
    * file as a String
    * 
    * @param file
    *        the file to import
    * @return a {@link List} which contains each line of the .csv file as a String
    */
   @Override
   public List<String> importFile(File file) {

      try (FileReader fileReader = new FileReader(file)) {

         BufferedReader bufferedReader = new BufferedReader(fileReader);
         String readLine = bufferedReader.readLine();
         List<String> importedLines = new ArrayList<>();
         while (nonNull(readLine)) {
            importedLines.add(readLine);
            readLine = bufferedReader.readLine();
         }
         return importedLines;
      } catch (IOException e) {
         e.printStackTrace();
         throw new IllegalStateException(e);
      }
   }
}
