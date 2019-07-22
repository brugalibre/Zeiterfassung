/**
 * 
 */
package com.myownb3.dominic.ui.app.pages.stopbusinessday.model.resolver;

import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDayIncrement;
import com.myownb3.dominic.timerecording.work.businessday.ext.BusinessDayInc4Export;
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

	BusinessDayIncrement currentBussinessDayIncremental = TimeRecorder.INSTANCE.getBussinessDay()
		.getCurrentBussinessDayIncremental();
	if (inPageModel == null) {
	    return new StopBusinessDayIncrementPageModel(BusinessDayInc4Export.of(currentBussinessDayIncremental));
	}
	return StopBusinessDayIncrementPageModel.of(inPageModel,
		BusinessDayInc4Export.of(currentBussinessDayIncremental));
    }
}
