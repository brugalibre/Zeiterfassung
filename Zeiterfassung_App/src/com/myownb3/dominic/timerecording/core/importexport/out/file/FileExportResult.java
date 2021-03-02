package com.myownb3.dominic.timerecording.core.importexport.out.file;

public class FileExportResult {
   private boolean success;
   private String errorMsg;
   private String path;

   public FileExportResult() {
      success = true;
   }

   public final boolean isSuccess() {
      return this.success;
   }

   public final String getErrorMsg() {
      return this.errorMsg;
   }

   public void setErrorMsg(String errorMsg) {
      this.errorMsg = errorMsg;
   }

   public void setSuccess(boolean success) {
      this.success = success;
   }

   public void setPath(String path) {
      this.path = path;
   }

   public String getPath() {
      return path;
   }
}
