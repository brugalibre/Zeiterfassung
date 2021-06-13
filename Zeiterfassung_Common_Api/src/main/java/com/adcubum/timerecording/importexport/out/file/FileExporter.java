package com.adcubum.timerecording.importexport.out.file;

import java.io.IOException;
import java.util.List;

public interface FileExporter {

   public static final FileExporter INTANCE = FileExporterFactory.createNew();

   /**
    * Exports the given list of {@link String} to the Desktop and returns a {@link FileExportResult} which may be used if there was an error
    * occurred
    * 
    * @param filePath
    *        the path incl. name of the file
    * @param content
    *        the content to export
    */
   FileExportResult exportWithResult(List<String> content, String filePath);

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
   String export(List<String> content, String filePath);

}
