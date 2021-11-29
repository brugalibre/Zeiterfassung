
/**
 * 
 */
package com.adcubum.timerecording.ui.app;

import static com.adcubum.timerecording.ui.app.settings.hotkey.HotKeyManager.COME_OR_GO_HOT_KEY;
import static com.adcubum.timerecording.ui.app.settings.hotkey.HotKeyManager.START_STOP_HOT_KEY;

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

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.app.TimeRecorder;
import com.adcubum.timerecording.app.startstopresult.UserInteractionResult;
import com.adcubum.timerecording.application.ApplicationLaunchException;
import com.adcubum.timerecording.core.callbackhandler.UiCallbackHandler;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGoes;
import com.adcubum.timerecording.librarys.pictures.PictureLibrary;
import com.adcubum.timerecording.message.Message;
import com.adcubum.timerecording.message.MessageType;
import com.adcubum.timerecording.security.login.auth.AuthenticationService;
import com.adcubum.timerecording.security.login.callback.LoginCallbackHandler;
import com.adcubum.timerecording.security.login.callback.LoginEndState;
import com.adcubum.timerecording.settings.round.RoundMode;
import com.adcubum.timerecording.settings.round.TimeRounder;
import com.adcubum.timerecording.ticketbacklog.TicketBacklogSPI;
import com.adcubum.timerecording.ticketbacklog.callback.UpdateStatus;
import com.adcubum.timerecording.ui.app.pages.mainpage.view.MainWindowPage;
import com.adcubum.timerecording.ui.app.settings.hotkey.HotKeyManager;
import com.adcubum.timerecording.ui.app.tray.TrayIconDelegate;
import com.adcubum.timerecording.ui.app.tray.TrayIconDelegateImpl;
import com.adcubum.timerecording.ui.app.web.WebUiHelper;
import com.adcubum.timerecording.ui.security.login.auth.UiAuthenticationService;
import com.adcubum.timerecording.ui.util.ExceptionUtil;
import com.adcubum.timerecording.workerfactory.ThreadFactory;
import com.adcubum.util.exception.GlobalExceptionHandler;

import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * @author Dominic
 * 
 */
public class TimeRecordingTray implements LoginCallbackHandler {
   private JMenuItem showHoursItem;
   private JMenuItem startTurboBucher;
   private JMenuItem showImportDialogItem;
   private JMenuItem refreshTicketBacklogMenuItem;
   private JMenuItem doUserAuthenticationMenuItem;
   private JMenuItem comeAndGoItem;
   private JMenuItem showComeAndGoItem;
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
      createDoUserAuthenticationMenuItem();
      createImportAufzeichnungMenueItem();
      createComeAndGoMenueItem();
      JPopupMenu popupMenu = createPopupMenu(settingsRoundMenu, exitItem, closePopupItem);
      popupMenuSupplier.setJMenuePopup(popupMenu);

      mainWindowPage = new MainWindowPage(this, primaryStage);

      trayIcon.addMouseMotionListener(getMouseMotionListener());
      trayIcon.addMouseListener(getMouseListener(popupMenu));
      registerHotKeys();
      // Update the ui-states, depending on the current BusinessDay
      updateUIStates();
   }

   private void registerHotKeys() {
      com.adcubum.timerecording.ui.app.settings.hotkey.callback.UiCallbackHandler a =
            new com.adcubum.timerecording.ui.app.settings.hotkey.callback.UiCallbackHandler() {

               @Override
               public void onHotKeyPressed() {
                  handleUserInteractionAndShowInputIfStopped(false);
               }

               @Override
               public String getSettingsValue(String key) {
                  return TimeRecorder.INSTANCE.getSettingsValue(key);
               }
            };
      com.adcubum.timerecording.ui.app.settings.hotkey.callback.UiCallbackHandler comeOrGoCallbackHandler =
            new com.adcubum.timerecording.ui.app.settings.hotkey.callback.UiCallbackHandler() {

               @Override
               public void onHotKeyPressed() {
                  handleUserInteractionAndShowInputIfStopped(true);
               }

               @Override
               public String getSettingsValue(String key) {
                  return TimeRecorder.INSTANCE.getSettingsValue(key);
               }
            };
      HotKeyManager.INSTANCE.registerHotKey(a, START_STOP_HOT_KEY);
      HotKeyManager.INSTANCE.registerHotKey(comeOrGoCallbackHandler, COME_OR_GO_HOT_KEY);
   }

   private void initTicketBacklog() {
      refreshTicketBacklogMenuItem.setEnabled(false);
      TicketBacklogSPI.getTicketBacklog().initTicketBacklog(this::handleTicketBacklogUpdated);
   }

   private void handleTicketBacklogUpdated(UpdateStatus status) {
      runInFxThread(() -> {
         mainWindowPage.refresh();
         refreshTicketBacklogMenuItem.setEnabled(AuthenticationService.INSTANCE.isUserAuthenticated());
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

   private JMenuItem createShowWebUiMenueItem() {
      JMenuItem showWebUiItem = new JMenuItem(TextLabel.SHOW_WEB_UI);
      WebUiHelper webUiHelper = new WebUiHelper(getCallbackHandler());
      showWebUiItem.addActionListener(actionEvent -> webUiHelper.openUrlInBrowser());
      showWebUiItem.setEnabled(true);
      return showWebUiItem;
   }

   private void createComeAndGoMenueItem() {
      comeAndGoItem = new JMenuItem(TextLabel.COME_OR_GO);
      comeAndGoItem.addActionListener(actionEvent -> comeOrGo());
      comeAndGoItem.setEnabled(true);
      showComeAndGoItem = new JMenuItem(TextLabel.SHOW_COME_OR_GO);
      showComeAndGoItem.addActionListener(actionEvent -> showComeAndGoOverview());
      showComeAndGoItem.setEnabled(false);
   }

   private void comeOrGo() {
      TimeRecorder.INSTANCE.handleUserInteraction(true);
      showComeAndGoItem.setEnabled(true);
   }

   private void createImportAufzeichnungMenueItem() {
      showImportDialogItem = new JMenuItem(TextLabel.SHOW_IMPORT_DIALOG_MENU_ITEM);
      showImportDialogItem.addActionListener(actionEvent -> showImportDialog());
      showImportDialogItem.setEnabled(true);
   }

   private void showImportDialog() {
      runInFxThread(mainWindowPage::showImportDialog);
   }

   private void doUserAuthentication() {
      doUserAuthenticationMenuItem.setEnabled(false);
      runInFxThread(() -> UiAuthenticationService.doUserAuthentication(this));
   }

   @Override
   public void onLoginEnd(LoginEndState loginEndState) {
      if (loginEndState == LoginEndState.SUCCESSFULLY) {
         initTicketBacklog();
         TimeRecorder.INSTANCE.onSuccessfullyLogin();
      }
      updateUIStates();
   }

   private void showOverviewView() {
      runInFxThread(mainWindowPage::showOverviewView);
   }

   private void showComeAndGoOverview() {
      runInFxThread(mainWindowPage::showComeAndGoOverview);
   }

   private void showInputMask() {
      runInFxThread(mainWindowPage::showInputMask);
   }

   private void startWorking() {
      trayIcon.setImage(PictureLibrary.getWorkingImage());
      updateUIStates();
   }

   private void handleCome() {
      trayIcon.setImage(PictureLibrary.getComeOrGoImage());
      updateUIStates();
   }

   private void handleGo() {
      trayIcon.setImage(PictureLibrary.getNotWorkingImage());
      runInFxThread(mainWindowPage::refresh);
      updateUIStates();
   }

   private void stopWorking() {
      trayIcon.setImage(PictureLibrary.getNotWorkingImage());
      showHoursItem.setEnabled(false);
      startTurboBucher.setEnabled(false);
   }

   /**
    * Updates the states of the button & elements
    */
   public void updateUIStates() {
      BusinessDay bussinessDay = TimeRecorder.INSTANCE.getBussinessDay();
      ComeAndGoes comeAndGoes = bussinessDay.getComeAndGoes();
      boolean isNotBooking = !TimeRecorder.INSTANCE.isBooking();
      boolean isUserAuthenticated = AuthenticationService.INSTANCE.isUserAuthenticated();
      boolean hasNotChargedElements = TimeRecorder.INSTANCE.hasNotChargedElements();
      boolean hasNoComeAndGoes = comeAndGoes.getComeAndGoEntries().isEmpty();
      showHoursItem.setEnabled(TimeRecorder.INSTANCE.hasContent() && isNotBooking);
      startTurboBucher.setEnabled(TimeRecorder.INSTANCE.hasNotChargedElements() && isNotBooking);
      startTurboBucher.setEnabled(isUserAuthenticated && hasNotChargedElements && isNotBooking);
      doUserAuthenticationMenuItem.setEnabled(!isUserAuthenticated);
      showImportDialogItem.setEnabled(hasNoContentAndIsNotRecording() && isNotBooking && hasNoComeAndGoes);
      comeAndGoItem.setEnabled(!TimeRecorder.INSTANCE.isRecording());
      showComeAndGoItem.setEnabled(!comeAndGoes.getComeAndGoEntries().isEmpty());
      setTrayIconImage();
   }

   private void setTrayIconImage() {
      if (TimeRecorder.INSTANCE.isComeAndGoActive()) {
         trayIcon.setImage(PictureLibrary.getComeOrGoImage());
      } else if (TimeRecorder.INSTANCE.isRecording()) {
         trayIcon.setImage(PictureLibrary.getWorkingImage());
      } else {
         trayIcon.setImage(PictureLibrary.getNotWorkingImage());
      }
   }

   private boolean hasNoContentAndIsNotRecording() {
      return !TimeRecorder.INSTANCE.hasContent() && !TimeRecorder.INSTANCE.isRecording();
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

         @Override
         public void onCome() {
            handleCome();
         }

         @Override
         public void onGo() {
            handleGo();
         }
      };
   }

   private void handleUserInteractionAndShowInputIfStopped(boolean comeOrGo) {
      UserInteractionResult handleUserInteraction = TimeRecorder.INSTANCE.handleUserInteraction(comeOrGo);
      if (handleUserInteraction.isUserInteractionRequired()) {
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
         TimeRecorder.INSTANCE.book();
      } finally {
         // Make sure the UI is refreshed after the booking
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
      refreshTicketBacklogMenuItem.setEnabled(AuthenticationService.INSTANCE.isUserAuthenticated());
   }

   private void createDoUserAuthenticationMenuItem() {
      doUserAuthenticationMenuItem = new JMenuItem(TextLabel.LOGIN_LABEL);
      doUserAuthenticationMenuItem.addActionListener(actionEvent -> doUserAuthentication());
      doUserAuthenticationMenuItem.setEnabled(false);// initially false, because the login is triggered during start up
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

   private static void runInFxThread(Runnable runnable) {
      Platform.runLater(runnable);
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
               handleUserInteractionAndShowInputIfStopped(false);
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
      JMenuItem showWebUiMenueItem = createShowWebUiMenueItem();
      JPopupMenu popupMenu = new JPopupMenu();
      popupMenu.add(settingsRoundMenu);
      popupMenu.addSeparator();
      popupMenu.add(refreshTicketBacklogMenuItem);
      popupMenu.addSeparator();
      popupMenu.add(showImportDialogItem);
      popupMenu.add(startTurboBucher);
      popupMenu.addSeparator();
      popupMenu.add(showHoursItem);
      popupMenu.add(showWebUiMenueItem);
      popupMenu.addSeparator();
      popupMenu.add(showComeAndGoItem);
      popupMenu.add(comeAndGoItem);
      popupMenu.addSeparator();
      popupMenu.add(doUserAuthenticationMenuItem);
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
