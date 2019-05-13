/**
 * 
 */
package com.myownb3.dominic.ui.app.pages.overview.model.resolver;

import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.work.businessday.ext.BusinessDay4Export;
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
	    return new OverviewPageModel(BusinessDay4Export.of(TimeRecorder.getBussinessDay()));
	}
	return OverviewPageModel.of(inPageModel, BusinessDay4Export.of(TimeRecorder.getBussinessDay()));
    }
}
