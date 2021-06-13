package com.adcubum.timerecording.importexport.out.file;

import com.adcubum.timerecording.core.factory.AbstractFactory;

/**
 * The {@link FileExporterFactory} is used in order to create a {@link FileExporter}
 * 
 * @author Dominic
 *
 */
public class FileExporterFactory extends AbstractFactory {
   private static final String BEAN_NAME = "fileexporter";
   private static final FileExporterFactory INSTANCE = new FileExporterFactory();

   private FileExporterFactory() {
      super("spring.xml");
   }

   /**
    * Creates a new {@link FileExporter} or return a already created instance
    * 
    * @return a new {@link FileExporter} or return a already created instance
    */
   public static FileExporter createNew() {
      return INSTANCE.createNewWithAgruments(BEAN_NAME);
   }
}
