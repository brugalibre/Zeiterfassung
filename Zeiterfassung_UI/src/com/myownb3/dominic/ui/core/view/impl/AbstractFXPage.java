/**
 * 
 */
package com.myownb3.dominic.ui.core.view.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import com.myownb3.dominic.ui.core.control.Controller;
import com.myownb3.dominic.ui.core.model.PageModel;
import com.myownb3.dominic.ui.core.view.Page;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * @author Dominic Stalder
 * @since rel2.00.00
 */
public abstract class AbstractFXPage<IN_VO extends PageModel, OUT_VO extends PageModel>
	extends AbstractPage<IN_VO, OUT_VO> {

    /**
     * Creates a new AbstractFXPage without a Scene
     * 
     */
    protected AbstractFXPage() {
	this(null);
    }

    @Override
    protected void initialize() {
	// nothing to do by default
    }

    /**
     * Creates a new AbstractFXPage
     * 
     * @param stage
     * 
     */
    protected AbstractFXPage(Stage stage) {
	super();
	initializeFXMLContent(Optional.ofNullable(stage));
    }

    @Override
    public void hide() {
	getStage().ifPresent(stage -> stage.hide());
    }

    @Override
    public void show() {
	getStage().ifPresent(stage -> stage.show());
    }

    protected Optional<Stage> getStage() {
	FXPageContent content = (FXPageContent) getContent();
	return content.getStage();
    }

    protected void initializeFXMLContent(Optional<Stage> stage) {
	FXMLLoader loader = new FXMLLoader();
	try {
	    initializeFXMLoader(loader);
	    initializeScene(loader, stage);
	    initializeController(loader);
	    applyStyle(stage);
	} catch (IOException e) {
	    throw new RuntimeException("Unable to load the fxml file located at '" + getUIResource() + "'!", e);
	}
    }

    /**
     * Initializes the FXMLLoader. This means it sets the location of the FXML file
     * is set according the given String and calls the {@link FXMLLoader#load()} in
     * order to load the content
     * 
     * @param loader
     *            - the FXMLLoader
     * @throws IOException
     */
    protected void initializeFXMLoader(FXMLLoader loader) throws IOException {
	URL fxmlLocation = getFXMLLocation();
	loader.setLocation(fxmlLocation);
	loader.load();
    }

    /**
     * 
     * Initializes the current content of the page. The default behavior is to load
     * the FXMLLoaders content, create a new Scene and a new FXPageContent for the
     * current page
     * 
     * @param loader
     *            - the FXMLLoader which loads the FXML-file
     * @param optionalStage
     *            the optional {@link Stage}
     * @throws IOException
     */
    protected void initializeScene(FXMLLoader loader, Optional<Stage> optionalStage) throws IOException {
	Pane content = loader.getRoot();
	setContent(new FXPageContent(optionalStage, content));
    }

    /**
     * Initializes the {@link Controller} of this {@link Page}
     * 
     * @param loader
     *            - the FXMLLoader
     */
    protected void initializeController(FXMLLoader loader) {
	Controller<IN_VO, OUT_VO> controller = loader.getController();
	controller.initialize(this);
	setController(controller);
    }

    /**
     * Apply the css-file for the current page if there is such a file available
     * 
     * @param optionalStage
     *            the optional {@link Stage}
     */
    protected void applyStyle(Optional<Stage> optionalStage) {
	String styleResource = getUIStyleResource();
	applyStileForResource(styleResource, optionalStage);
    }

    /**
     * @param styleResource
     * @param optionalStage
     *            the optional {@link Stage}
     * @throws MalformedURLException
     */
    protected void applyStileForResource(String styleResource, Optional<Stage> optionalStage) {
	URL resource = getClass().getResource(styleResource);
	if (resource == null) {
	    return;
	}
	optionalStage.ifPresent(stage -> stage.getScene().getStylesheets().addAll(resource.toExternalForm()));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GETTER AND SETTER //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @param uiResource
     * @return
     * @throws MalformedURLException
     */
    protected URL getFXMLLocation() throws MalformedURLException {
	String uiResource = getUIResource();
	return getClass().getResource(uiResource);
    }

    /**
     * Returns the location for the FXML-file this AbstractFXPage needs
     * 
     * @return the location for the FXML-file this AbstractFXPage needs
     */
    protected String getUIResource() {
	return getClass().getSimpleName() + ".fxml";
    }

    /**
     * Returns the location for the CSS-file this AbstractFXPage needs
     * 
     * @return the location for the CSS-file this AbstractFXPage needs
     */
    protected String getUIStyleResource() {
	String path = "/" + getClass().getPackage().getName() + "/" + getClass().getSimpleName() ;
	return path.replace(".", "/") + ".css";
    }

    @Override
    public void refresh() {
	getRootNode().requestLayout();
    }

    @Override
    public boolean isVisible() {
	return getRootNode().isVisible();
    }

    /**
     * Returns the root node of this {@link FXPageContent}
     * 
     * @return
     */
    private Region getRootNode() {
	FXPageContent fxContent = getFXContent();
	if (fxContent.getRootParent() instanceof Region) {
	    return (Region) fxContent.getRootParent();
	}
	throw new IllegalStateException("Root-Node '" + fxContent.getRootParent() + "' is not an instance-of Region!");
    }

    public Parent getRootParent() {
	return getRootNode();
    }

    protected FXPageContent getFXContent() {
	return (FXPageContent) getContent();
    }
}
