/**
 * 
 */
package com.myownb3.dominic.timerecording.charge;

import java.util.List;

import com.coolguys.turbo.Booker;
import com.myownb3.dominic.export.ContentSelector;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDay;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDayIncremental;
import com.myownb3.dominic.timerecording.work.businessday.ext.BusinessDay4Export;

/**
 * Is responsible for booking the given {@link BusinessDay}.
 * 
 * @author Dominic
 */
public class BookerHelper {
    private BusinessDay businessDay;

    public BookerHelper(BusinessDay businessDay) {
	this.businessDay = businessDay;
    }

    /**
     * Collects from each {@link BusinessDayIncremental} the content to book
     * and calls finally the {@link Booker#bookList(List)}. Additionally all booked {@link BusinessDayIncremental}
     * are flagged as charged
     * @see Booker#bookList(List)
     */
    public void book() {
	List<String> content2Charge = createBookContent();
	chargeInternal(content2Charge);
	flagBusinessDayAsCharged();
    }

    private void flagBusinessDayAsCharged() {
	businessDay.flagBusinessDayAsCharged();
    }

    /*
     * Does the actual charging
     */
    private void chargeInternal(List<String> content) {
	try {
	    Booker booker = new Booker();
	    booker.bookList(content);
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new ChargeException(e);
	}
    }

    /*
     * Collects the data which has to be charged and exports it into a file which is
     * later used by the Turbo-Bucher
     */
    private List<String> createBookContent() {
	return ContentSelector.collectContent4TurboBucher(new BusinessDay4Export(businessDay));
    }
}
