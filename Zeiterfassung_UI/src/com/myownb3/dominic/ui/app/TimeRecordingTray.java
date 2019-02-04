/**
 * 
 */
package com.myownb3.dominic.ui.app;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import com.myownb3.dominic.librarys.PictureLibrary;
import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.ui.styles.color.Colors;
import com.myownb3.dominic.ui.util.ExceptionUtil;

/**
 * @author Dominic
 * 
 */
public class TimeRecordingTray {
    private TrayIcon trayIcon;
    private MainWindow mainWindow;
    private MenuItem showHoursItem;
    private MenuItem startTurboBucher;

    public void registerSystemtray() {

	setLookAndFeel();
	PopupMenu popup = new PopupMenu();

	mainWindow = new MainWindow(this);

	trayIcon = new TrayIcon(PictureLibrary.getNotWorkingImageIcon(),
		TextLabel.APPLICATION_TITLE + ": " + TextLabel.CAPTURING_INACTIVE);

	// Create a popup menu components
	MenuItem exitItem = new MenuItem(TextLabel.EXIT);
	showHoursItem = new MenuItem(TextLabel.SHOW_WORKING_HOURS);
	startTurboBucher = new MenuItem(TextLabel.CHARGE_LABEL);

	popup.add(startTurboBucher);
	popup.add(showHoursItem);
	popup.addSeparator();
	popup.add(exitItem);
	trayIcon.setPopupMenu(popup);

	SystemTray tray = SystemTray.getSystemTray();
	try {
	    tray.add(trayIcon);
	} catch (Exception e) {
	    throw new RuntimeException("Unable to add tray icon!", e);
	}

	trayIcon.addMouseMotionListener(new MouseMotionListener() {

	    @Override
	    public void mouseMoved(MouseEvent arg0) {
		trayIcon.setToolTip(TimeRecorder.getInfoStringForState());
	    }

	    @Override
	    public void mouseDragged(MouseEvent arg0) {
	    }
	});
	trayIcon.addMouseListener(new MouseListener() {
	    @Override
	    public void mouseReleased(MouseEvent e) {
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
	});

	showHoursItem.addActionListener(actionEvent -> showOverviewView());
	showHoursItem.setEnabled(false);
	startTurboBucher.addActionListener(actionEvent -> startTurboBucher());
	startTurboBucher.setEnabled(false);

	exitItem.addActionListener(actionEvent -> {
	    tray.remove(trayIcon);
	    System.exit(0);
	});
    }

    private void startTurboBucher() {
	mainWindow.chargeOff();
    }

    /**
    * 
    */
    protected void showOverviewView() {
	mainWindow.showOverviewView(TimeRecorder.getBussinessDay());
    }

    /**
    * 
    */
    protected void showInputMask() {
	mainWindow.showInputMask(TimeRecorder.getBussinessDay().getCurrentBussinessDayIncremental());
    }

    private void setLookAndFeel() {
	try {
	    UIManager.setLookAndFeel(new NimbusLookAndFeel());
	    UIManager.put("control", Colors.BACKGROUND_COLOR); // change the
							       // color of the
							       // background of
							       // Nimbus L&F
	} catch (Exception ex) {
	    try {
		UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
	    } catch (Exception e) {
		throw new RuntimeException(e);
	    }
	}
    }

    /**
     * Starts or resume
     */
    public void startWorking() {
	trayIcon.setImage(PictureLibrary.getWorkingImageIcon());
	updateUIStates(true);
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
    public void updateUIStates(boolean isWorking) {
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
}
