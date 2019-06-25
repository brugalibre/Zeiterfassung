/**
 * 
 */
package com.myownb3.dominic.timerecording.settings.round;

import static com.myownb3.dominic.timerecording.settings.common.Const.TURBO_BUCHER_PROPERTIES;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.myownb3.dominic.timerecording.settings.round.exception.RounderInitException;
import com.myownb3.dominic.timerecording.settings.round.exception.RounderStoreValueException;

/**
 * @author Dominic
 *
 */
public class TimeRounder {

    public static final TimeRounder INSTANCE = new TimeRounder();
    private RoundMode roundMode;

    private TimeRounder() {
	init();
    }

    public void init() {
//	InputStream resourceStream = loader.getResourceAsStream(TURBO_BUCHER_PROPERTIES) Works but the properties file is empty..
//	InputStream resourceStream = new FileInputStream(TURBO_BUCHER_PROPERTIES) Works perfectly fine within eclipse but not outside
	RoundMode roundMode = evalRoundMode();
	setRoundMode(roundMode);
    }

    private RoundMode evalRoundMode() {
	RoundMode roundMode = null;
	try (InputStream resourceStream = new FileInputStream(TURBO_BUCHER_PROPERTIES)) {
	    roundMode = evalRoundModeFromProperties(resourceStream);
	} catch (IOException e) {
	    e.printStackTrace();
	    throw new RounderInitException(e);
	}
	return roundMode;
    }

    private RoundMode evalRoundModeFromProperties(InputStream resourceStream) throws IOException {
	Properties prop = new Properties();
	prop.load(resourceStream);
	String roundModeAsString = (String) prop.get(RoundMode.PROPERTY_KEY);
	return RoundMode.getRoundMode(roundModeAsString);
    }

    public void setRoundMode(RoundMode roundMode) {
	this.roundMode = roundMode;
	saveValueToProperties(roundMode);
    }

    private void saveValueToProperties(RoundMode roundMode) {
	Properties prop = new Properties();
	try (InputStream resourceStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(TURBO_BUCHER_PROPERTIES)) {
	    prop.load(resourceStream);
	    prop.put(RoundMode.PROPERTY_KEY, String.valueOf(roundMode.getAmount()));
	    prop.store(new FileOutputStream(TURBO_BUCHER_PROPERTIES), null);
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new RounderStoreValueException(e);
	}
    }

    public final RoundMode getRoundMode() {
	return this.roundMode;
    }
}
