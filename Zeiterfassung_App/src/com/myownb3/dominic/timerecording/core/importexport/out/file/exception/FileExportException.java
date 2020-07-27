package com.myownb3.dominic.timerecording.core.importexport.out.file.exception;

public class FileExportException extends RuntimeException {

   public FileExportException(Exception e) {
      super(e);
   }

   public FileExportException(String msg) {
      super(msg);
   }

   /**
    * 
    */
   private static final long serialVersionUID = 1L;

}
