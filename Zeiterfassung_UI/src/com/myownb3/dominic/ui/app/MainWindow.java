/**
 * 
 */
package com.myownb3.dominic.ui.app;

import java.awt.CardLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.myownb3.dominic.librarys.PictureLibrary;
import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.callback.handler.BusinessDayChangedOwner;
import com.myownb3.dominic.timerecording.callback.handler.impl.BusinessDayChangedCallbackHandlerImpl;
import com.myownb3.dominic.timerecording.callback.handler.impl.ChangedValue;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDay;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDayIncremental;
import com.myownb3.dominic.timerecording.work.businessday.ext.BusinessDay4Export;
import com.myownb3.dominic.ui.views.overview.OverviewView;
import com.myownb3.dominic.ui.views.userinput.InputMask;

/**
 * @author Dominic
 * 
 */
public class MainWindow  implements BusinessDayChangedOwner, KeyListener {
    private JFrame mainWindow;
    private JPanel content;
    private InputMask inputMask;
    private OverviewView overviewView;
    private TimeRecordingTray timeRecordingTray;

    public MainWindow(TimeRecordingTray timeRecordingTray) {
	content = new JPanel(new CardLayout());
	inputMask = new InputMask(this);
	overviewView = new OverviewView(this);
	content.add(inputMask, ViewList.INPUT_MASK.toString());
	content.add(overviewView, ViewList.OVERVIEW_VIEW.toString());

	mainWindow = new JFrame(TextLabel.APPLICATION_TITLE + " v" + TimeRecorder.VERSION);
	mainWindow.setIconImage(PictureLibrary.getClockImageIcon());
	setLocation();
	mainWindow.add(content);
	mainWindow.addKeyListener(this);

	this.timeRecordingTray = timeRecordingTray;
    }

    /**
    * 
    */
    private void setLocation() {
	int top = (Toolkit.getDefaultToolkit().getScreenSize().height - mainWindow.getSize().height) / 2;
	int left = (Toolkit.getDefaultToolkit().getScreenSize().width - mainWindow.getSize().width) / 2;
	mainWindow.setLocation(left, top);
    }

    public void showInputMask(BusinessDayIncremental bussinessDayIncremental) {
	inputMask.initializeFields(bussinessDayIncremental);
	CardLayout cl = (CardLayout) (content.getLayout());
	cl.show(content, ViewList.INPUT_MASK.toString());
	mainWindow.setVisible(true);
	mainWindow.setResizable(false);
	mainWindow.setSize(inputMask.getDimension());
	setLocation();
    }

    public void showOverviewView(BusinessDay bussinessDay) {
	overviewView.initialize(new BusinessDay4Export(bussinessDay),
		new BusinessDayChangedCallbackHandlerImpl(bussinessDay, this));
	CardLayout cl = (CardLayout) (content.getLayout());
	cl.show(content, ViewList.OVERVIEW_VIEW.toString());
	mainWindow.setResizable(true);
	mainWindow.pack();
	mainWindow.setVisible(true);
	setLocation();
    }

    /**
     * Lets the current shown window disappears. If the given boolean is true, the
     * {@link BusinessDay} is checked for redundant entry
     * 
     * @param done
     */
    public void finishOrAbortAndDispose(boolean done) {
	if (done) {
	    TimeRecorder.checkForRedundancy();
	    timeRecordingTray.updateUIStates(false);
	} else {
	    TimeRecorder.resume();
	}
	mainWindow.dispose();
    }

    /**
     * Lets the current shown window disappears. If the given boolean is true, the
     * {@link BusinessDay} is checked for redundant entry
     * 
     * @param done
     */
    public void dispose() {
	mainWindow.dispose();
    }

    @Override
    public void keyPressed(KeyEvent e) {
	if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
	    finishOrAbortAndDispose(false);
	}
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * 
     */
    public void clearBusinessDayContents() {
	timeRecordingTray.clearBusinessDayContents();
    }

    /**
     * 
     */
    public void export() {
	TimeRecorder.export();
    }

    public void chargeOff() {
	TimeRecorder.charge();
	refresh();
    }

    private void refresh() {
	// First check for increments which can be merged (e.g. if there was an charged
	// and a not charged increment which are equal but still handled separately
	// (because the one was already charged and the 2nd not). And as soon as the 2nd
	// one is charged, they can be merged
	TimeRecorder.checkForRedundancy();
	if (overviewView.isVisible()) {
	    showOverviewView(TimeRecorder.getBussinessDay());
	}
	timeRecordingTray.updateUIStates(false);
    }

    @Override
    public void afterBusinessDayChanged(ChangedValue changeValue) {
	BusinessDay bussinessDay = TimeRecorder.getBussinessDay();
	overviewView.refresh(new BusinessDay4Export(bussinessDay));
    }
}
