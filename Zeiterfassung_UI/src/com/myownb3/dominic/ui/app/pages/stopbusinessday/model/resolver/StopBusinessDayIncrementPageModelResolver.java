/**
 * 
 */
package com.myownb3.dominic.ui.app.pages.stopbusinessday.model.resolver;

import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.core.work.businessday.extern.BusinessDayInc4Export;
import com.myownb3.dominic.ui.app.pages.stopbusinessday.model.StopBusinessDayIncrementPageModel;
import com.myownb3.dominic.ui.core.model.resolver.PageModelResolver;

/**
 * @author Dominic
 *
 */
public class StopBusinessDayIncrementPageModelResolver
	implements PageModelResolver<StopBusinessDayIncrementPageModel, StopBusinessDayIncrementPageModel> {

    @Override
    public StopBusinessDayIncrementPageModel resolvePageVO(StopBusinessDayIncrementPageModel inPageModel) {

	BusinessDayInc4Export currentBussinessDayIncremental = TimeRecorder.INSTANCE.getCurrentBussinessDayIncrement();
	if (inPageModel == null) {
	    return new StopBusinessDayIncrementPageModel(currentBussinessDayIncremental);
	}
	return StopBusinessDayIncrementPageModel.of(inPageModel, currentBussinessDayIncremental);
    }
}
