package com.adcubum.timerecording.core.work.businessday.exception;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;

/**
 * Common {@link Exception} which is thrown, when the {@link BusinessDay} is not valid
 * because of business reason
 * 
 * @author dstalder
 *
 */
public class BusinessDayNotValidException extends RuntimeException {

   private static final long serialVersionUID = 1L;
   private final String attrName;
   private final String errorMsg;

   public BusinessDayNotValidException(String errorMsg) {
      this(null, errorMsg);
   }

   public BusinessDayNotValidException(String attrName, String errorMsg) {
      super(errorMsg);
      this.attrName = attrName;
      this.errorMsg = errorMsg;
   }

   public String getAttrName() {
      return attrName;
   }

   public String getErrorMsg() {
      return errorMsg;
   }

}
