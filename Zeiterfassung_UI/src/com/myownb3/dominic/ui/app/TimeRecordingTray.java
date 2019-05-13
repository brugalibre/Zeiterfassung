/**
 * 
 */
package com.myownb3.dominic.ui.app;

import java.awt.Color;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import com.myownb3.dominic.librarys.pictures.PictureLibrary;
import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.settings.round.RoundMode;
import com.myownb3.dominic.timerecording.settings.round.TimeRounder;
import com.myownb3.dominic.ui.core.pages.mainpage.view.MainWindowPage;
import com.myownb3.dominic.ui.util.ExceptionUtil;

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
    private MainWindowPage mainWindowPage;
    private JPopupMenu popupMenu;

    public void registerSystemtray(Stage primaryStage) {

	setLookAndFeel();
	trayIcon = new TrayIcon(PictureLibrary.getNotWorkingImageIcon(),
		TextLabel.APPLICATION_TITLE + ": " + TextLabel.CAPTURING_INACTIVE);

	JMenu settingsRoundMenu = createSettingsMenu();

	// Create a popup menu components
	JMenuItem exitItem = new JMenuItem(TextLabel.EXIT);
	showHoursItem = new JMenuItem(TextLabel.SHOW_WORKING_HOURS);
	startTurboBucher = new JMenuItem(TextLabel.CHARGE_LABEL);

	popupMenu = new JPopupMenu();
	popupMenu.add(settingsRoundMenu);
	popupMenu.addSeparator();
	popupMenu.add(startTurboBucher);
	popupMenu.add(showHoursItem);
	popupMenu.addSeparator();
	popupMenu.add(exitItem);

	mainWindowPage = new MainWindowPage(this, primaryStage);
	getTrayAndAddTrayIcon();

	trayIcon.addMouseMotionListener(getMouseMotionListener());
	trayIcon.addMouseListener(getMouseListener());

	showHoursItem.addActionListener(actionEvent -> showOverviewView());
	showHoursItem.setEnabled(false);
	startTurboBucher.addActionListener(actionEvent -> TimeRecorder.book());
	startTurboBucher.setEnabled(false);

	exitItem.addActionListener(actionEvent -> {
	    Platform.exit();
	    System.exit(0);
	});
    }

    private void showOverviewView() {
	Platform.runLater(() -> mainWindowPage.showOverviewView());
    }

    private void showInputMask() {
	Platform.runLater(() -> mainWindowPage.showInputMask());
    }

    /**
     * Starts or resume
     */
    public void startWorking() {
	trayIcon.setImage(PictureLibrary.getWorkingImageIcon());
	updateUIStates();
    }

    /**
    * 
    */
    public void stopWorking() {
	trayIcon.setImage(PictureLibrary.getNotWorkingImageIcon());
	trayIcon.setToolTip(TextLabel.APPLICATION_TITLE + ": " + TextLabel.CAPTURING_INACTIVE);
	showHoursItem.setEnabled(false);
	startTurboBucher.setEnabled(false);
    }

    /**
     * Updates the states of the button & elements
     */
    public void updateUIStates() {
	showHoursItem.setEnabled(TimeRecorder.hasContent());
	startTurboBucher.setEnabled(TimeRecorder.hasNotChargedElements());
    }

    /**
     * @param thread
     * @param thrown
     */
    public void showException(Thread thread, Throwable thrown) {
	ExceptionUtil.showException(thread, thrown);
    }

    public void clearBusinessDayContents() {
	TimeRecorder.clear();
	showHoursItem.setEnabled(false);
	startTurboBucher.setEnabled(false);
    }

    private void getTrayAndAddTrayIcon() {
	SystemTray tray = SystemTray.getSystemTray();
	try {
	    tray.add(trayIcon);
	} catch (Exception e) {
	    throw new RuntimeException("Unable to add tray icon!", e);
	}
    }

    private MouseMotionListener getMouseMotionListener() {
	return new MouseMotionListener() {

	    @Override
	    public void mouseMoved(MouseEvent arg0) {
		trayIcon.setToolTip(TimeRecorder.getInfoStringForState());
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
		    if (TimeRecorder.handleUserInteraction()) {
			showInputMask();
		    }
		}
	    }
	};
    }

    private JMenu createSettingsMenu() {
	ButtonGroup buttonGroup = new ButtonGroup();

	JRadioButtonMenuItem settingsRoundItem1Min = new JRadioButtonMenuItem(TextLabel.SETTINGS_ROUND_1, true);
	JRadioButtonMenuItem settingsRoundItem5Min = new JRadioButtonMenuItem(TextLabel.SETTINGS_ROUND_5);
	JRadioButtonMenuItem settingsRoundItem10Min = new JRadioButtonMenuItem(TextLabel.SETTINGS_ROUND_10);
	JRadioButtonMenuItem settingsRoundItem15Min = new JRadioButtonMenuItem(TextLabel.SETTINGS_ROUND_15);

	buttonGroup.add(settingsRoundItem1Min);
	buttonGroup.add(settingsRoundItem5Min);
	buttonGroup.add(settingsRoundItem10Min);
	buttonGroup.add(settingsRoundItem15Min);

	settingsRoundItem1Min.addActionListener(event -> TimeRounder.INSTANCE.setRoundMode(RoundMode.ONE_MIN));
	settingsRoundItem5Min.addActionListener(event -> TimeRounder.INSTANCE.setRoundMode(RoundMode.FIVE_MIN));
	settingsRoundItem10Min.addActionListener(event -> TimeRounder.INSTANCE.setRoundMode(RoundMode.TEN_MIN));
	settingsRoundItem15Min.addActionListener(event -> TimeRounder.INSTANCE.setRoundMode(RoundMode.FIFTEEN_MIN));

	JMenu settingsRoundMenu = new JMenu(TextLabel.SETTINGS_ROUND);
	settingsRoundMenu.add(settingsRoundItem1Min);
	settingsRoundMenu.add(settingsRoundItem5Min);
	settingsRoundMenu.add(settingsRoundItem10Min);
	settingsRoundMenu.add(settingsRoundItem15Min);
	return settingsRoundMenu;
    }

    private void setLookAndFeel() {
	try {
	    UIManager.setLookAndFeel(new NimbusLookAndFeel());
	    UIManager.put("control", Color.WHITE);
	} catch (Exception ex) {
	    try {
		UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
	    } catch (Exception e) {
		throw new RuntimeException(e);
	    }
	}
    }
}
