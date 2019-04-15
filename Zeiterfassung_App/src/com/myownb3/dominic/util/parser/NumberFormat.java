package com.myownb3.dominic.util.parser;

import java.text.ParseException;

import com.myownb3.dominic.timerecording.app.TimeRecorder;

public class NumberFormat {

    public static float parse(float time, int factor) throws NumberFormatException {

	java.text.NumberFormat format = java.text.NumberFormat.getInstance();
	Number number = Float.valueOf(0);
	try {
	    number = format.parse(TimeRecorder.formater.format((time = time / factor)));
	} catch (ParseException e) {
	    e.printStackTrace();
	    throw new NumberFormatException(e.getLocalizedMessage());
	}
	return number.floatValue();
    }

    public static float parseFloat(String float2Parse) {
	java.text.NumberFormat format = java.text.NumberFormat.getInstance();
	try {
	    Number number = format.parse(float2Parse);
	    return number.floatValue();
	} catch (ParseException e) {
	    e.printStackTrace();
	    throw new NumberFormatException(e.getLocalizedMessage());
	}
    }

    public static String format(float number) {
	java.text.NumberFormat format = java.text.NumberFormat.getInstance();
	return format.format(number);
    }

}
