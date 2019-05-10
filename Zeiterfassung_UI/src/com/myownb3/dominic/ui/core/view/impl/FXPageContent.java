/**
 * 
 */
package com.myownb3.dominic.ui.core.view.impl;

import java.util.Optional;

import com.myownb3.dominic.ui.core.view.PageContent;

import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * The FXPageContent implements the PageContent for the Java-FX environment.
 * Therefore it contains the (main)-Stage object of the application
 * 
 * @author Dominic Stalder
 */
public class FXPageContent implements PageContent {
    private Optional<Stage> stage;
    private Parent rootParent; // the main node of this content

    /**
    * 
    */
    public FXPageContent(Optional<Stage> stage, Parent rootParent) {
	this.stage = stage;
	this.rootParent = rootParent;
    }

    /**
     * @return the stage
     */
    public Optional<Stage> getStage() {
	return stage;
    }

    /**
     * @return the rootParent
     */
    public Parent getRootParent() {
	return rootParent;
    }
}
