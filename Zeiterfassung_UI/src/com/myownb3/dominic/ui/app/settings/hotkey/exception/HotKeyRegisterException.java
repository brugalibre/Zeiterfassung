package com.myownb3.dominic.ui.app.settings.hotkey.exception;

import java.io.IOException;

public class HotKeyRegisterException extends RuntimeException{

    public HotKeyRegisterException(IOException e) {
	super(e);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

}
