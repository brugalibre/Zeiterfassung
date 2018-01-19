/**
 * 
 */
package com.myownb3.dominic.timerecording.charge;

import com.myownb3.dominic.librarys.text.res.TextLabel;

/**
 * @author Dominic
 *
 */
public class ChargeType {
    public static final String[] CHARGE_TYPES;

    static {
	CHARGE_TYPES = new String[] { TextLabel.CHARGE_TYPE_PROGRAMMIERUNG, TextLabel.CHARGE_TYPE_INTERNE_WEITERBILDUNG,
		TextLabel.CHARGE_TYPE_ALL_VERWALTUNG, TextLabel.CHARGE_TYPE_MEETING };
    }
}
