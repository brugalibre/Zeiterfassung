/**
 * 
 */
package com.adcubum.timerecording.importexport.out.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.adcubum.timerecording.importexport.out.file.exception.FileExportException;

/**
 * @author Dominic
 *
 */
public class FileExporter {

   /**
    * The file extension of files to export
    */
   public static final String FILE_EXTENSION = "csv";
   public static final FileExporter INTANCE = new FileExporter();
   private static final Logger LOG = Logger.getLogger(FileExporter.class);

   private FileExporter() {
      // private Constructor
   }

   /**
    * Exports the given list of {@link String} to the Desktop and returns a {@link FileExportResult} which may be used if there was an error
    * occured
    * 
    * @param filePath
    *        the path incl. name of the file
    * @param content
    *        the content to export
    */
   public FileExportResult exportWithResult(List<String> content, String filePath) {
      FileExportResult exportResult = new FileExportResult();
      String path = null;
      try {
         path = export(content, filePath);
      } catch (FileExportException e) {
         LOG.error(e.getMessage());
         exportResult.setErrorMsg(e.getLocalizedMessage());
         exportResult.setSuccess(false);
      }
      exportResult.setPath(path);
      return exportResult;
   }

   /**
    * Exports the given list of {@link String} to the given path of a file to create
    * 
    * @param content
    *        the content to export
    * @param filePath
    *        the path incl. name of the file
    * @return the path to the exported file
    * @throws FileExportException
    *         if there was a {@link IOException}
    */
   public String export(List<String> content, String filePath) {
      String effectPath = evalDefaultPath(filePath);
      File file = new File(effectPath);
      try (FileWriter writer = new FileWriter(file)) {
         file.createNewFile();
         writeLines(content, writer);
      } catch (IOException e) {
         throw new FileExportException(e);
      }
      return file.getPath();
   }

   private static String evalDefaultPath(String path) {
      String dateDetails = DateFormat.getDateInstance().format(new Date());
      float randomNo = System.currentTimeMillis();
      return path + dateDetails + "_" + randomNo + "." + FILE_EXTENSION;
   }

   private void writeLines(List<String> content, FileWriter writer) throws IOException {
      for (String element : content) {
         writer.write(element);
      }
   }
}
