package com.adcubum.timerecording.importexport.in.file;

import com.adcubum.timerecording.core.factory.AbstractFactory;

/**
 * The {@link FileImporterFactory} is used in order to create a {@link FileImporter}
 * 
 * @author Dominic
 *
 */
public class FileImporterFactory extends AbstractFactory {
   private static final String BEAN_NAME = "fileimporter";
   private static final FileImporterFactory INSTANCE = new FileImporterFactory();

   private FileImporterFactory() {
      super("spring.xml");
   }

   /**
    * Creates a new {@link FileImporter} or return a already created instance
    * 
    * @return a new {@link FileImporter} or return a already created instance
    */
   public static FileImporter createNew() {
      return INSTANCE.createNewWithAgruments(BEAN_NAME);
   }
}
