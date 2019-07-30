/**
 * 
 */
package com.myownb3.dominic.ui.app.pages.overview.model.resolver;

import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.core.work.businessday.extern.BusinessDay4Export;
import com.myownb3.dominic.ui.app.pages.overview.model.OverviewPageModel;
import com.myownb3.dominic.ui.core.model.resolver.PageModelResolver;

/**
 * @author Dominic
 *
 */
public class OverviewPageModelResolver implements PageModelResolver<OverviewPageModel, OverviewPageModel> {

    @Override
    public OverviewPageModel resolvePageVO(OverviewPageModel inPageModel) {
	if (inPageModel == null) {
	    return new OverviewPageModel(BusinessDay4Export.of(TimeRecorder.INSTANCE.getBussinessDay()));
	}
	return OverviewPageModel.of(inPageModel, BusinessDay4Export.of(TimeRecorder.INSTANCE.getBussinessDay()));
    }
}
