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
	BusinessDay4Export businessDay4Export = TimeRecorder.INSTANCE.getBussinessDayReadOnly();
	if (inPageModel == null) {
	    return new OverviewPageModel(businessDay4Export);
	}
	return OverviewPageModel.of(inPageModel, businessDay4Export);
    }
}
