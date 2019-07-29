package com.myownb3.dominic.fileimport.exception;

public class BusinessDayImportException extends RuntimeException {

    public BusinessDayImportException(Exception e) {
	super(e);
    }

    public BusinessDayImportException(String message) {
	super(message);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

}
