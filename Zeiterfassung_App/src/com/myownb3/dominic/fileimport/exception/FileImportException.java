package com.myownb3.dominic.fileimport.exception;

public class FileImportException extends RuntimeException {

    public FileImportException(Exception e) {
	super(e);
    }

    public FileImportException(String message) {
	super(message);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

}
