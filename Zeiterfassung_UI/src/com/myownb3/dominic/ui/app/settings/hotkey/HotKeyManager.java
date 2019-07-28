package com.myownb3.dominic.ui.app.settings.hotkey;

import static com.myownb3.dominic.timerecording.settings.common.Const.TURBO_BUCHER_PROPERTIES;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.KeyStroke;

import com.myownb3.dominic.ui.app.callback.UiCallbackHandler;
import com.myownb3.dominic.ui.app.settings.hotkey.exception.HotKeyRegisterException;
import com.tulskiy.keymaster.common.Provider;

public class HotKeyManager {

    public static final HotKeyManager INSTANCE = new HotKeyManager();
    public static final String START_STOP_HOT_KEY = "StartStopHotKey";

    private HotKeyManager() {
	// Private constructor
    }

    /**
     * Registers the hot key to start or stop a recording with a combination of key
     * pressing
     */
    public void registerHotKey(UiCallbackHandler callbackHandler) {
	String hotKeyAsString = evalHotKey();
	if (hotKeyAsString != null) {
	    Provider provider = Provider.getCurrentProvider(false);
	    provider.register(KeyStroke.getKeyStroke(hotKeyAsString), hotKey -> callbackHandler.onHotKeyPressed());
	}
    }

    private String evalHotKey() {
	String hotKey = null;
	try (InputStream resourceStream = new FileInputStream(TURBO_BUCHER_PROPERTIES)) {
	    hotKey = evalHotKeyFromProperties(resourceStream);
	} catch (IOException e) {
	    e.printStackTrace();
	    throw new HotKeyRegisterException(e);
	}
	return hotKey;
    }

    private String evalHotKeyFromProperties(InputStream resourceStream) throws IOException {
	Properties prop = new Properties();
	prop.load(resourceStream);
	return (String) prop.get(START_STOP_HOT_KEY);
    }
}
