/**
 * 
 */
package com.myownb3.dominic.timerecording.charge;

import java.io.File;
import java.util.List;

import com.coolguys.turbo.Booker;
import com.myownb3.dominic.export.ContentSelector;
import com.myownb3.dominic.export.FileExporter;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDay;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDayIncremental;
import com.myownb3.dominic.timerecording.work.businessday.ext.BusinessDay4Export;

/**
 * Is responsible for charging the given {@link BusinessDayIncremental}.
 * 
 * @author Dominic
 */
public class Charger {
	private BusinessDay businessDay;

	public Charger(BusinessDay businessDay) {
		this.businessDay = businessDay;
	}

	public void charge() {
		File file2Charge = createContextAndExportFile();
		chargeInternal(file2Charge);
		flagBusinessDayIncAsChaged();
	}

	private void flagBusinessDayIncAsChaged() {
		businessDay.flagBusinessDayIncAsChaged();
	}

	/*
	 * Does the actual charging
	 */
	private void chargeInternal(File file2Charge) {
		try {
			Booker.main(new String[] { file2Charge.getAbsolutePath() });
			file2Charge.delete();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ChargeException(e);
		}
	}

	/*
	 * Collects the data which has to be charged and exports it into a file
	 * which is later used by the Turbo-Bucher
	 */
	private File createContextAndExportFile() {
		List<String> content4TurboBucher = ContentSelector.collectContent4TurboBucher(new BusinessDay4Export(businessDay));
		return FileExporter.exportAndReturnFile4Charge(content4TurboBucher);
	}
}
