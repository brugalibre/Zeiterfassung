/**
 * 
 */
package com.myownb3.dominic.ui.app;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

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
import com.myownb3.dominic.timerecording.core.callbackhandler.UiCallbackHandler;
import com.myownb3.dominic.timerecording.core.message.Message;
import com.myownb3.dominic.timerecording.core.message.MessageType;
import com.myownb3.dominic.timerecording.core.work.businessday.BusinessDay;
import com.myownb3.dominic.timerecording.core.workerfactory.ThreadFactory;
import com.myownb3.dominic.timerecording.settings.round.RoundMode;
import com.myownb3.dominic.timerecording.settings.round.TimeRounder;
import com.myownb3.dominic.timerecording.ticketbacklog.TicketBacklogSPI;
import com.myownb3.dominic.timerecording.ticketbacklog.callback.UiTicketBacklogCallbackHandler.UpdateStatus;
import com.myownb3.dominic.ui.app.pages.mainpage.view.MainWindowPage;
import com.myownb3.dominic.ui.app.settings.hotkey.HotKeyManager;
import com.myownb3.dominic.ui.app.tray.TrayIconDelegate;
import com.myownb3.dominic.ui.app.tray.TrayIconDelegateImpl;
import com.myownb3.dominic.ui.util.ExceptionUtil;
import com.myownb3.dominic.util.exception.GlobalExceptionHandler;

import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * @author Dominic
 * 
 */
public class TimeRecordingTray {
   private JMenuItem showHoursItem;
   private JMenuItem startTurboBucher;
   private JMenuItem showImportDialogItem;
   private JMenuItem refreshTicketBacklogMenuItem;
   private MainWindowPage mainWindowPage;
   private TrayIconDelegate trayIcon;

   public void registerSystemtray(Stage primaryStage) {

      setLookAndFeel();

      addTrayIconToSystemTray();

      // Create a popup menu components
      JPopupMenueSupplier popupMenuSupplier = new JPopupMenueSupplier();
      JMenu settingsRoundMenu = createSettingsMenu();
      JMenuItem exitItem = createExitMenu();
      JMenuItem closePopupItem = createClosePopupMenu(popupMenuSupplier);
      createRefreshTicketBacklogMenuItem();
      createShowHoursMenuItem();
      createStartTurboBucherMenuItem();
      createImportAufzeichnungMenueItem();
      JPopupMenu popupMenu = createPopupMenu(settingsRoundMenu, exitItem, closePopupItem);
      popupMenuSupplier.setJMenuePopup(popupMenu);

      mainWindowPage = new MainWindowPage(this, primaryStage);

      trayIcon.addMouseMotionListener(getMouseMotionListener());
      trayIcon.addMouseListener(getMouseListener(popupMenu));
      HotKeyManager.INSTANCE.registerHotKey(this::handleUserInteractionAndShowInputIfStopped);
      initTicketBacklog();
   }

   private void initTicketBacklog() {
      refreshTicketBacklogMenuItem.setEnabled(false);
      TicketBacklogSPI.getTicketBacklog().initTicketBacklog(this::handleTicketBacklogUpdated);
   }

   private void handleTicketBacklogUpdated(UpdateStatus status) {
      Platform.runLater(() -> {
         mainWindowPage.refreshStopBusinessDayPage();
         refreshTicketBacklogMenuItem.setEnabled(true);
         displayMessage(status);
      });
   }

   private void displayMessage(UpdateStatus status) {
      switch (status) {
         case SUCCESS:
            displayMessage(null, TextLabel.REFRESH_TICKET_BACKLOG_DONE, MessageType.INFORMATION);
            break;
         case NOT_CONFIGURED:
            displayMessage(null, TextLabel.REFRESH_TICKET_BACKLOG_NOT_POSSIBLE, MessageType.WARNING);
            break;
         case FAIL:
            displayMessage(null, TextLabel.REFRESH_TICKET_BACKLOG_FAILED, MessageType.ERROR);
            break;
      }
   }

   private static final class JPopupMenueSupplier implements Supplier<JPopupMenu> {
      private JPopupMenu jPopupMenu;

      @Override
      public JPopupMenu get() {
         return jPopupMenu;
      }

      private void setJMenuePopup(JPopupMenu popupMenu) {
         this.jPopupMenu = popupMenu;
      }
   }

   private JMenuItem createClosePopupMenu(Supplier<JPopupMenu> popupMenuSupplier) {
      JMenuItem closePopupMenuItem = new JMenuItem(TextLabel.CLOSE_POPUP_MENU);
      closePopupMenuItem.addActionListener(actionEvent -> popupMenuSupplier.get().setVisible(false));
      return closePopupMenuItem;
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
      trayIcon.setImage(PictureLibrary.getWorkingImage());
      updateUIStates();
   }

   private void stopWorking() {
      trayIcon.setImage(PictureLibrary.getNotWorkingImage());
      trayIcon.setToolTip(TextLabel.APPLICATION_TITLE + ": " + TextLabel.CAPTURING_INACTIVE);
      showHoursItem.setEnabled(false);
      startTurboBucher.setEnabled(false);
   }

   /**
    * Updates the states of the button & elements
    */
   public void updateUIStates() {
      boolean isNotBooking = !TimeRecorder.INSTANCE.isBooking();
      showHoursItem.setEnabled(TimeRecorder.INSTANCE.hasContent() && isNotBooking);
      startTurboBucher.setEnabled(TimeRecorder.INSTANCE.hasNotChargedElements() && isNotBooking);
      showImportDialogItem.setEnabled(hasNoContentAndIsNotRecording() && isNotBooking);
   }

   private boolean hasNoContentAndIsNotRecording() {
      return !TimeRecorder.INSTANCE.hasContent() && !TimeRecorder.INSTANCE.isRecordindg();
   }

   public void clearBusinessDayContents() {
      TimeRecorder.INSTANCE.clear();
      updateUIStates();
   }

   /**
    * @return the {@link UiCallbackHandler} which handles interaction between the
    *         {@link TimeRecordingTray} and the {@link TimeRecorder} or the
    *         {@link GlobalExceptionHandler}
    */
   public UiCallbackHandler getCallbackHandler() {

      return new UiCallbackHandler() {

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
            ExceptionUtil.showException(thread, thrown);
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
      ThreadFactory.INSTANCE.execute(this::getBookAndRefreshRunnable);
   }

   private void getBookAndRefreshRunnable() {
      try {
         bookInternal();
      } finally {
         // Make sure the UI is refreshed after the booking
         updateUIStates();
      }
   }

   private void bookInternal() {
      boolean wasBooked = TimeRecorder.INSTANCE.book();
      if (wasBooked) {
         displayMessage(null, TextLabel.SUCCESSFULLY_CHARGED_TEXT, MessageType.INFORMATION);
         updateUIStates();
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
         showOverviewView();
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
      JMenuItem exitItem = new JMenuItem(TextLabel.EXIT_APP);

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

   private void createRefreshTicketBacklogMenuItem() {
      refreshTicketBacklogMenuItem = new JMenuItem(TextLabel.REFRESH_TICKET_BACKLOG);
      refreshTicketBacklogMenuItem.addActionListener(actionEvent -> initTicketBacklog());
   }

   private void createShowHoursMenuItem() {
      showHoursItem = new JMenuItem(TextLabel.SHOW_WORKING_HOURS);
      showHoursItem.addActionListener(actionEvent -> showOverviewView());
      showHoursItem.setEnabled(false);
   }

   private void addTrayIconToSystemTray() {
      trayIcon = new TrayIconDelegateImpl();
      trayIcon.show();
   }

   private MouseMotionListener getMouseMotionListener() {
      return new MouseMotionListener() {

         @Override
         public void mouseMoved(MouseEvent arg0) {
            trayIcon.setToolTip(TimeRecorder.INSTANCE.getInfoStringForState());
         }

         @Override
         public void mouseDragged(MouseEvent mouseEvent) {
            trayIcon.onMouseDragged(mouseEvent);
         }
      };
   }

   private MouseListener getMouseListener(JPopupMenu popupMenu) {
      return new MouseListener() {
         @Override
         public void mouseReleased(MouseEvent e) {
            handleMouseEvent(popupMenu, e);
         }

         @Override
         public void mousePressed(MouseEvent e) {
            handleMouseEvent(popupMenu, e);
         }

         @Override
         public void mouseExited(MouseEvent e) {/*no-op*/}

         @Override
         public void mouseEntered(MouseEvent e) {/*no-op*/}

         @Override
         public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
               handleUserInteractionAndShowInputIfStopped();
            } else if (e.isPopupTrigger()) {
               handleMouseEvent(popupMenu, e);
            }
         }

         private void handleMouseEvent(JPopupMenu popupMenu, MouseEvent e) {
            trayIcon.onMouseEvent(popupMenu, e);
         }
      };
   }

   private JPopupMenu createPopupMenu(JMenu settingsRoundMenu, JMenuItem exitItem, JMenuItem closePopupItem) {
      JPopupMenu popupMenu = new JPopupMenu();
      popupMenu.add(settingsRoundMenu);
      popupMenu.addSeparator();
      popupMenu.add(refreshTicketBacklogMenuItem);
      popupMenu.addSeparator();
      popupMenu.add(showImportDialogItem);
      popupMenu.add(startTurboBucher);
      popupMenu.add(showHoursItem);
      popupMenu.addSeparator();
      popupMenu.add(closePopupItem);
      popupMenu.add(exitItem);
      return popupMenu;
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

      Map<RoundMode, JRadioButtonMenuItem> roundMode2ButtonMap = new EnumMap<>(RoundMode.class);
      roundMode2ButtonMap.put(RoundMode.ONE_MIN, settingsRoundItem1Min);
      roundMode2ButtonMap.put(RoundMode.FIVE_MIN, settingsRoundItem5Min);
      roundMode2ButtonMap.put(RoundMode.TEN_MIN, settingsRoundItem10Min);
      roundMode2ButtonMap.put(RoundMode.FIFTEEN_MIN, settingsRoundItem15Min);

      // Register for reach an action handler
      for (Entry<RoundMode, JRadioButtonMenuItem> roundMode : roundMode2ButtonMap.entrySet()) {
         roundMode2ButtonMap.get(roundMode.getKey()).addActionListener(event -> safeRoundSettings(roundMode.getKey()));
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
