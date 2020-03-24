package com.myownb3.dominic.timerecording.core.importexport.out.file;

public class FileExportResult {
   private boolean success;
   private String errorMsg;

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
}
