/**
 * 
 */
package com.myownb3.dominic.ui.app;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import com.myownb3.dominic.launch.exception.ApplicationLaunchException;
import com.myownb3.dominic.librarys.pictures.PictureLibrary;
import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.core.callbackhandler.CallbackHandler;
import com.myownb3.dominic.timerecording.core.message.Message;
import com.myownb3.dominic.timerecording.core.message.MessageType;
import com.myownb3.dominic.timerecording.core.work.businessday.BusinessDay;
import com.myownb3.dominic.timerecording.settings.round.RoundMode;
import com.myownb3.dominic.timerecording.settings.round.TimeRounder;
import com.myownb3.dominic.ui.app.pages.mainpage.view.MainWindowPage;
import com.myownb3.dominic.ui.app.settings.hotkey.HotKeyManager;
import com.myownb3.dominic.ui.util.ExceptionUtil;
import com.myownb3.dominic.util.exception.GlobalExceptionHandler;

import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * @author Dominic
 * 
 */
public class TimeRecordingTray {
    private TrayIcon trayIcon;
    private JMenuItem showHoursItem;
    private JMenuItem startTurboBucher;
    private JMenuItem showImportDialogItem;
    private MainWindowPage mainWindowPage;
    private JPopupMenu popupMenu;

    public void registerSystemtray(Stage primaryStage) throws ApplicationLaunchException {

	setLookAndFeel();

	addTrayIconToSystemTray();

	// Create a popup menu components
	JMenu settingsRoundMenu = createSettingsMenu();
	JMenuItem exitItem = createExitMenu();
	createShowHoursMenuItem();
	createStartTurboBucherMenuItem();
	createImportAufzeichnungMenueItem();
	createPopupMenu(settingsRoundMenu, exitItem);

	mainWindowPage = new MainWindowPage(this, primaryStage);

	trayIcon.addMouseMotionListener(getMouseMotionListener());
	trayIcon.addMouseListener(getMouseListener());
	HotKeyManager.INSTANCE.registerHotKey(() -> handleUserInteractionAndShowInputIfStopped());
    }

    private void createImportAufzeichnungMenueItem() {
	showImportDialogItem = new JMenuItem(TextLabel.SHOW_IMPORT_DIALOG_MENU_ITEM);
	showImportDialogItem.addActionListener(actionEvent -> showImportDialog());
	showImportDialogItem.setEnabled(true);
    }

    private void showImportDialog() {
	Platform.runLater(() -> mainWindowPage.showImportDialog());
    }

    private void showOverviewView() {
	Platform.runLater(() -> mainWindowPage.showOverviewView());
    }

    private void showInputMask() {
	Platform.runLater(() -> mainWindowPage.showInputMask());
    }

    private void startWorking() {
	trayIcon.setImage(PictureLibrary.getWorkingImageIcon());
	updateUIStates();
    }

    private void stopWorking() {
	trayIcon.setImage(PictureLibrary.getNotWorkingImageIcon());
	trayIcon.setToolTip(TextLabel.APPLICATION_TITLE + ": " + TextLabel.CAPTURING_INACTIVE);
	showHoursItem.setEnabled(false);
	startTurboBucher.setEnabled(false);
    }

    /**
     * Updates the states of the button & elements
     */
    public void updateUIStates() {
	showHoursItem.setEnabled(TimeRecorder.INSTANCE.hasContent());
	startTurboBucher.setEnabled(TimeRecorder.INSTANCE.hasNotChargedElements());
	showImportDialogItem.setEnabled(hasNoContentAndIsNotRecording());
    }

    private boolean hasNoContentAndIsNotRecording() {
	return !TimeRecorder.INSTANCE.hasContent() && !TimeRecorder.INSTANCE.isRecordindg();
    }

    /**
     * Shows the given {@link Throwable}
     * 
     * @param thread
     *            the thread which caught the throable
     * @param thrown
     *            the thrown throable
     */
    private void showException(Thread thread, Throwable thrown) {
	ExceptionUtil.showException(thread, thrown);
    }

    public void clearBusinessDayContents() {
	TimeRecorder.INSTANCE.clear();
	updateUIStates();
    }

    /**
     * @return the {@link CallbackHandler} which handles interaction between the
     *         {@link TimeRecordingTray} and the {@link TimeRecorder} or the
     *         {@link GlobalExceptionHandler}
     */
    public CallbackHandler getCallbackHandler() {

	return new CallbackHandler() {

	    @Override
	    public void onStop() {
		stopWorking();
	    }

	    @Override
	    public void onStart() {
		startWorking();
	    }

	    @Override
	    public void onResume() {
		startWorking();
	    }

	    @Override
	    public void onException(Throwable thrown, Thread thread) {
		showException(thread, thrown);
	    }

	    @Override
	    public void displayMessage(Message message) {
		TimeRecordingTray.this.displayMessage(message.getMessageTitle(), message.getMessage(), message.getMessageType());
	    }
	};
    }

    private void handleUserInteractionAndShowInputIfStopped() {
	if (TimeRecorder.INSTANCE.handleUserInteraction()) {
	    showInputMask();
	}
    }

    /**
     * Books the content of the current {@link BusinessDay}
     */
    public void book() {
	boolean wasBooked = TimeRecorder.INSTANCE.book();
	if (wasBooked) {
	    displayMessage(null, TextLabel.SUCCESSFULLY_CHARGED_TEXT, MessageType.INFORMATION);
	}
    }

    /**
     * Exports the content of the current {@link BusinessDay}
     */
    public void export() {
	TimeRecorder.INSTANCE.export();
    }

    /**
     * Resumes a previously stopped recording
     */
    public void resume() {
	TimeRecorder.INSTANCE.resume();
    }

    public void importBusinessDayFromFile(File selectedFile) {

	boolean success = TimeRecorder.INSTANCE.importBusinessDayFromFile(selectedFile);
	if (success) {
	    displayMessage(null, TextLabel.IMPORT_SUCESSFULL, MessageType.INFORMATION);
	    updateUIStates();
	} else {
	    displayMessage(TextLabel.IMPORT_NOT_SUCESSFULL_TITLE, TextLabel.IMPORT_NOT_SUCESSFULL_MSG, MessageType.ERROR);
	}
    }

    private void displayMessage(String messageTitle, String message, MessageType messageType) {

	trayIcon.displayMessage(messageTitle, message, getTryIconErrorForMessageType(messageType));
	if (messageType == MessageType.ERROR) {
	    Toolkit.getDefaultToolkit().beep();
	}
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Create Content for UI
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private JMenuItem createExitMenu() {
	JMenuItem exitItem = new JMenuItem(TextLabel.EXIT);

	exitItem.addActionListener(actionEvent -> {
	    Platform.exit();
	    System.exit(0);
	});
	return exitItem;
    }

    private void createStartTurboBucherMenuItem() {
	startTurboBucher = new JMenuItem(TextLabel.CHARGE_LABEL);
	startTurboBucher.addActionListener(actionEvent -> book());
	startTurboBucher.setEnabled(false);
    }

    private void createShowHoursMenuItem() {
	showHoursItem = new JMenuItem(TextLabel.SHOW_WORKING_HOURS);
	showHoursItem.addActionListener(actionEvent -> showOverviewView());
	showHoursItem.setEnabled(false);
    }

    private void addTrayIconToSystemTray() throws ApplicationLaunchException {

	trayIcon = new TrayIcon(PictureLibrary.getNotWorkingImageIcon(), TextLabel.APPLICATION_TITLE + ": " + TextLabel.CAPTURING_INACTIVE);
	if (SystemTray.isSupported()) {
	    try {
		SystemTray tray = SystemTray.getSystemTray();
		tray.add(trayIcon);
	    } catch (AWTException e) {
		throw new ApplicationLaunchException(e);
	    }
	} else {
	    throw new ApplicationLaunchException("SystemTray auf aktuellem System nicht verfügbar!");
	}
    }

    private MouseMotionListener getMouseMotionListener() {
	return new MouseMotionListener() {

	    @Override
	    public void mouseMoved(MouseEvent arg0) {
		trayIcon.setToolTip(TimeRecorder.INSTANCE.getInfoStringForState());
	    }

	    @Override
	    public void mouseDragged(MouseEvent arg0) {
	    }
	};
    }

    private MouseListener getMouseListener() {
	return new MouseListener() {
	    @Override
	    public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
		    popupMenu.setLocation(e.getX(), e.getY());
		    popupMenu.setInvoker(popupMenu);
		    popupMenu.setVisible(true);
		}
	    }

	    @Override
	    public void mousePressed(MouseEvent e) {
	    }

	    @Override
	    public void mouseExited(MouseEvent e) {
	    }

	    @Override
	    public void mouseEntered(MouseEvent e) {
	    }

	    @Override
	    public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
		    handleUserInteractionAndShowInputIfStopped();
		}
	    }
	};
    }

    private void createPopupMenu(JMenu settingsRoundMenu, JMenuItem exitItem) {
	popupMenu = new JPopupMenu();
	popupMenu.add(settingsRoundMenu);
	popupMenu.addSeparator();
	popupMenu.add(showImportDialogItem);
	popupMenu.add(startTurboBucher);
	popupMenu.add(showHoursItem);
	popupMenu.addSeparator();
	popupMenu.add(exitItem);
    }

    private JMenu createSettingsMenu() {

	JRadioButtonMenuItem settingsRoundItem1Min = new JRadioButtonMenuItem(TextLabel.SETTINGS_ROUND_1);
	JRadioButtonMenuItem settingsRoundItem5Min = new JRadioButtonMenuItem(TextLabel.SETTINGS_ROUND_5);
	JRadioButtonMenuItem settingsRoundItem10Min = new JRadioButtonMenuItem(TextLabel.SETTINGS_ROUND_10);
	JRadioButtonMenuItem settingsRoundItem15Min = new JRadioButtonMenuItem(TextLabel.SETTINGS_ROUND_15);

	ButtonGroup buttonGroup = new ButtonGroup();
	buttonGroup.add(settingsRoundItem1Min);
	buttonGroup.add(settingsRoundItem5Min);
	buttonGroup.add(settingsRoundItem10Min);
	buttonGroup.add(settingsRoundItem15Min);

	Map<RoundMode, JRadioButtonMenuItem> roundMode2ButtonMap = new HashMap<>();
	roundMode2ButtonMap.put(RoundMode.ONE_MIN, settingsRoundItem1Min);
	roundMode2ButtonMap.put(RoundMode.FIVE_MIN, settingsRoundItem5Min);
	roundMode2ButtonMap.put(RoundMode.TEN_MIN, settingsRoundItem10Min);
	roundMode2ButtonMap.put(RoundMode.FIFTEEN_MIN, settingsRoundItem15Min);

	// Register for reach an action handler
	for (RoundMode roundMode : roundMode2ButtonMap.keySet()) {
	    roundMode2ButtonMap.get(roundMode).addActionListener(event -> safeRoundSettings(roundMode));
	}

	// mark as selected
	RoundMode currentRoundMode = TimeRounder.INSTANCE.getRoundMode();
	JRadioButtonMenuItem jRadioButtonMenuItem = roundMode2ButtonMap.get(currentRoundMode);
	jRadioButtonMenuItem.setSelected(true);

	JMenu settingsRoundMenu = new JMenu(TextLabel.SETTINGS_ROUND);
	settingsRoundMenu.add(settingsRoundItem1Min);
	settingsRoundMenu.add(settingsRoundItem5Min);
	settingsRoundMenu.add(settingsRoundItem10Min);
	settingsRoundMenu.add(settingsRoundItem15Min);
	return settingsRoundMenu;
    }

    private void safeRoundSettings(RoundMode roundMode) {
	RoundMode currentRoundMode = TimeRounder.INSTANCE.getRoundMode();
	if (currentRoundMode != roundMode) {
	    TimeRounder.INSTANCE.setRoundMode(roundMode);
	    displayMessage(null, TextLabel.SETTINGS_ROUND_SAVED, MessageType.INFORMATION);
	}
    }

    private void setLookAndFeel() {
	try {
	    UIManager.setLookAndFeel(new NimbusLookAndFeel());
	    UIManager.put("control", Color.WHITE);
	} catch (Exception ex) {
	    try {
		UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
	    } catch (Exception e) {
		throw new ApplicationLaunchException(e);
	    }
	}
    }

    private java.awt.TrayIcon.MessageType getTryIconErrorForMessageType(MessageType messageType) {

	switch (messageType) {
	case ERROR:
	    return TrayIcon.MessageType.ERROR;
	case WARNING:
	    return TrayIcon.MessageType.WARNING;
	case INFORMATION:
	    return TrayIcon.MessageType.INFO;
	default:
	    return TrayIcon.MessageType.NONE;
	}
    }
}
