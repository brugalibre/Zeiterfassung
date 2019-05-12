/**
 * 
 */
package com.myownb3.dominic.ui.core.pages.userinput.model.resolver;

import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDayIncrement;
import com.myownb3.dominic.timerecording.work.businessday.ext.BusinessDayInc4Export;
import com.myownb3.dominic.ui.core.model.resolver.PageModelResolver;
import com.myownb3.dominic.ui.core.pages.userinput.model.StopBusinessDayIncrementPageModel;

/**
 * @author Dominic
 *
 */
public class StopBusinessDayIncrementPageModelResolver
	implements PageModelResolver<StopBusinessDayIncrementPageModel, StopBusinessDayIncrementPageModel> {

    @Override
    public StopBusinessDayIncrementPageModel resolvePageVO(StopBusinessDayIncrementPageModel inPageModel) {

	BusinessDayIncrement currentBussinessDayIncremental = TimeRecorder.getBussinessDay()
		.getCurrentBussinessDayIncremental();
	if (inPageModel == null) {
	    return new StopBusinessDayIncrementPageModel(BusinessDayInc4Export.of(currentBussinessDayIncremental));
	}
	return StopBusinessDayIncrementPageModel.of(inPageModel,
		BusinessDayInc4Export.of(currentBussinessDayIncremental));
    }
}
