package com.myownb3.dominic.timerecording.work.businessday;

import com.myownb3.dominic.librarys.text.res.TextLabel;

public enum ValueTypes {

    /** Number of the Jira-Ticket */
    TICKET_NR,
    /** The additionally description of the charging */
    DESCRIPTION,
    /** The Begin of a {@link BusinessDayIncremental} */
    BEGIN,
    /** The end of a {@link BusinessDayIncremental} */
    END;

    /**
     * Returns the type of value for a specific representation of a value
     * 
     * @param valueName
     *            the String representation of a value
     * @return a {@link ValueTypes} instance matching to the given representation of a value
     */
    public static ValueTypes getValueTypeForIndex(String valueName) {

	if (TextLabel.BIS_LABEL.equals(valueName)) {
	    return ValueTypes.END;
	} else if (TextLabel.VON_LABEL.equals(valueName)) {
	    return ValueTypes.BEGIN;
	} else if (TextLabel.DESCRIPTION_LABEL.equals(valueName)) {
	    return ValueTypes.DESCRIPTION;
	} else if (TextLabel.TICKET.equals(valueName)) {
	    return ValueTypes.TICKET_NR;
	}
	throw new IllegalStateException("Unknown changed Property '" + valueName + "'!");
    }
}
