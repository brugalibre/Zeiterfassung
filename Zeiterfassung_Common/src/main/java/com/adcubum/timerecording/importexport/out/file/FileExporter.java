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
import com.adcubum.util.utils.FileSystemUtil;

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
    * @param content
    *        the content to export
    */
   public FileExportResult exportWithResult(List<String> content) {
      FileExportResult exportResult = new FileExportResult();
      String path = null;
      try {
         path = export(content);
      } catch (FileExportException e) {
         LOG.error(e.getMessage());
         exportResult.setErrorMsg(e.getLocalizedMessage());
         exportResult.setSuccess(false);
      }
      exportResult.setPath(path);
      return exportResult;
   }

   /**
    * Exports the given list of {@link String} to the Desktop
    * 
    * @param content
    *        the content to export
    * @return the path to the exported file
    * @throws FileExportException
    *         if there was a {@link IOException}
    */
   public String export(List<String> content) {
      String dateDetails = DateFormat.getDateInstance().format(new Date());
      float randomNo = System.currentTimeMillis();

      String getDefaultPath = evalDefaultPath(dateDetails, randomNo);
      File file = new File(getDefaultPath);

      try (FileWriter writer = new FileWriter(file)) {
         file.createNewFile();
         writeLines(content, writer);
      } catch (IOException e) {
         throw new FileExportException(e);
      }
      return file.getPath();
   }

   private static String evalDefaultPath(String dateDetails, float randomNo) {
      String path = FileSystemUtil.getHomeDir() + FileSystemUtil.getDefaultFileSystemSeparator();
      return path + dateDetails + "_" + randomNo + "." + FILE_EXTENSION;
   }

   private void writeLines(List<String> content, FileWriter writer) throws IOException {
      for (String element : content) {
         writer.write(element);
      }
   }
}
