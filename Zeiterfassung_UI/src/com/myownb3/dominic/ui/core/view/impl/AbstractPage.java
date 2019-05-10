package com.myownb3.dominic.ui.core.view.impl;

import java.awt.Panel;

import com.myownb3.dominic.ui.core.control.Controller;
import com.myownb3.dominic.ui.core.model.PageModel;
import com.myownb3.dominic.ui.core.view.Page;
import com.myownb3.dominic.ui.core.view.PageContent;

/**
 * An {@link AbstractPage} is a kind of huge {@link Panel} which may content
 * different sub-Panels. Those sub Panels can be added <br>
 * to a {@link Page} in order to display them.
 * 
 * @author Dominic Stalder
 * @param <OUT_VO>
 *            - the outgoing data-model
 * @param <IN_VO>
 *            - the incoming data-model
 *
 */
public abstract class AbstractPage<IN_VO extends PageModel, OUT_VO extends PageModel> implements Page<IN_VO, OUT_VO> {
    private Controller<IN_VO, OUT_VO> controller;
    protected PageContent pageContent;
    protected boolean isDirty;

    protected AbstractPage() {
	super();
	initialize();
    }

    @Override
    public void initialize() {
	// So far nothing to initialize
    }

    /**
     * Defines if the content of a {@link Page} (changed by a user) is valid or not
     * 
     * @return true if the page content is valid. False if not
     */
    @Override
    public abstract void refresh();

    /**
     * @return the controller
     */
    @Override
    public Controller<IN_VO, OUT_VO> getController() {
	return controller;
    }

    /**
     * @param controller
     *            the controller to set
     */
    public void setController(Controller<IN_VO, OUT_VO> controller) {
	this.controller = controller;
    }

    protected void setContent(PageContent pageContent) {
	this.pageContent = pageContent;
    }

    @Override
    public PageContent getContent() {
	return pageContent;
    }

    @Override
    public void setDirty(boolean isDirty) {
	this.isDirty = isDirty;
    }
}
