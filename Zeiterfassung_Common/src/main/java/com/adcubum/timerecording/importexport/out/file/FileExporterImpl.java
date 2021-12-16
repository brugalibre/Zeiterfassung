/**
 * 
 */
package com.adcubum.timerecording.importexport.out.file;

import com.adcubum.timerecording.importexport.out.file.exception.FileExportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Dominic
 *
 */
public class FileExporterImpl implements FileExporter {

   /**
    * The file extension of files to export
    */
   public static final String FILE_EXTENSION = "csv";
   private static final Logger LOG = LoggerFactory.getLogger(FileExporterImpl.class);

   private FileExporterImpl() {
      // private Constructor
   }

   @Override
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

   @Override
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
