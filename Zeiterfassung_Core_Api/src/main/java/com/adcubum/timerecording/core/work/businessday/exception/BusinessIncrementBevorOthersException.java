package com.adcubum.timerecording.core.work.businessday.exception;

public class BusinessIncrementBevorOthersException extends BusinessDayNotValidException {

   private static final long serialVersionUID = 1L;

   public BusinessIncrementBevorOthersException(String attrName, String errorMsg) {
      super(attrName, errorMsg);
   }

   public BusinessIncrementBevorOthersException(String errorMsg) {
      super(errorMsg);
   }
}
