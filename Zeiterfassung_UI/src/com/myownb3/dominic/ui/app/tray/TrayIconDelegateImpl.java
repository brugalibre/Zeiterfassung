package com.myownb3.dominic.ui.app.tray;

import static java.util.Objects.nonNull;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;

import com.adcubum.librarys.text.res.TextLabel;
import com.myownb3.dominic.launch.exception.ApplicationLaunchException;
import com.myownb3.dominic.librarys.pictures.PictureLibrary;
import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.ui.core.dialog.DialogManager;

import javafx.embed.swing.SwingNode;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TrayIconDelegateImpl implements TrayIconDelegate {

   private TrayIcon trayIcon;
   private JLabel dummyTrayIconLabel;
   private Stage stage;
   private double xOffset;
   private double yOffset;

   public TrayIconDelegateImpl() {
      yOffset = 0;
      xOffset = 0;
   }

   @Override
   public void addMouseMotionListener(MouseMotionListener mouseMotionListener) {
      if (nonNull(trayIcon)) {
         trayIcon.addMouseMotionListener(mouseMotionListener);
      } else {
         dummyTrayIconLabel.addMouseMotionListener(mouseMotionListener);
      }
   }

   @Override
   public void addMouseListener(MouseListener mouseListener) {
      if (nonNull(trayIcon)) {
         trayIcon.addMouseListener(mouseListener);
      } else {
         dummyTrayIconLabel.addMouseListener(mouseListener);
      }
   }

   @Override
   public void setImage(Image image) {
      if (nonNull(trayIcon)) {
         trayIcon.setImage(image);
      } else {
         dummyTrayIconLabel.setIcon(new ImageIcon(image));
      }
   }

   @Override
   public void setToolTip(String tooltip) {
      if (nonNull(trayIcon)) {
         trayIcon.setToolTip(tooltip);
      } else {
         dummyTrayIconLabel.setToolTipText(tooltip);
      }
   }

   @Override
   public void onMouseEvent(JPopupMenu popupMenu, MouseEvent mouseEvent) {
      if (mouseEvent.isPopupTrigger()) {
         showPopup(popupMenu, mouseEvent);
      } else if (nonNull(stage) && mouseEvent.getID() == MouseEvent.MOUSE_PRESSED) {
         xOffset = stage.getX() - mouseEvent.getXOnScreen();
         yOffset = stage.getY() - mouseEvent.getYOnScreen();
      }
   }

   private void showPopup(JPopupMenu popupMenu, MouseEvent mouseEvent) {
      if (nonNull(dummyTrayIconLabel)) {
         popupMenu.setInvoker(dummyTrayIconLabel);
      } else {
         popupMenu.setInvoker(popupMenu);
      }
      popupMenu.setLocation((int) (mouseEvent.getXOnScreen() - xOffset), (int) (mouseEvent.getYOnScreen() - yOffset));
      popupMenu.setVisible(true);
   }

   @Override
   public void onMouseDragged(MouseEvent mouseEvent) {
      if (nonNull(dummyTrayIconLabel)) {
         stage.setX(mouseEvent.getXOnScreen() + xOffset);
         stage.setY(mouseEvent.getYOnScreen() + yOffset);
      }
   }

   @Override
   public void displayMessage(String messageTitle, String message, MessageType messageType) {
      if (nonNull(trayIcon)) {
         trayIcon.displayMessage(messageTitle, message, messageType);
      } else {
         DialogManager.showDialog(null, messageTitle, message, messageType);
      }
   }

   @Override
   public void show() {
      if (SystemTray.isSupported()) {
         addTrayIcon2SystemTray();
      } else {
         createDummyTrayStage();
      }
   }

   private void createDummyTrayStage() {
      ImageIcon notWorkingImage = PictureLibrary.getNotWorkingImageIcon();
      dummyTrayIconLabel = new JLabel(notWorkingImage);
      dummyTrayIconLabel.setOpaque(false);
      Parent swingNodeParent = buildSwingNodeParent();
      Scene scene = buildScene(swingNodeParent, notWorkingImage.getIconWidth(), notWorkingImage.getIconHeight());
      buildStage(scene, notWorkingImage.getIconHeight());
      stage.show();
   }

   private static Scene buildScene(Parent swingNodeParent, int width, int height) {
      Scene scene = new Scene(swingNodeParent, width, height);
      scene.setFill(Color.TRANSPARENT);
      return scene;
   }

   private Parent buildSwingNodeParent() {
      setProperties4TransparentSwingNode();
      SwingNode swingNode = new SwingNode();
      swingNode.setContent(dummyTrayIconLabel);

      StackPane stackPane = new StackPane();
      stackPane.setBackground(Background.EMPTY);
      stackPane.getChildren().add(swingNode);
      return stackPane;
   }

   private Stage buildStage(Scene scene, int iconHeight) {
      this.stage = new Stage();
      stage.setScene(scene);
      stage.setAlwaysOnTop(true);
      stage.setResizable(false);
      stage.initStyle(StageStyle.TRANSPARENT);
      stage.sizeToScene();
      stage.getIcons().add(PictureLibrary.getClockImageIcon());
      stage.setTitle(TextLabel.APPLICATION_TITLE + " v" + TimeRecorder.VERSION);
      Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
      stage.setX(screenBounds.getMaxX() - iconHeight * 1.5);
      stage.setY(screenBounds.getMaxY() - iconHeight * 1.5);
      return stage;
   }

   /*
    * https://stackoverflow.com/questions/28330906/swingnode-with-transparent-content
    */
   private static void setProperties4TransparentSwingNode() {
      // In order to get the SwingNode transparent 
      Properties props = System.getProperties();
      props.setProperty("swing.jlf.contentPaneTransparent", Boolean.TRUE.toString());
   }

   private void addTrayIcon2SystemTray() {
      try {
         SystemTray tray = SystemTray.getSystemTray();
         trayIcon = new TrayIcon(PictureLibrary.getNotWorkingImage(), TextLabel.APPLICATION_TITLE + ": " + TextLabel.CAPTURING_INACTIVE);
         tray.add(trayIcon);
      } catch (AWTException e) {
         throw new ApplicationLaunchException(e);
      }
   }
}
