package com.adcubum.timerecording.ui.app.tray;

import java.awt.Image;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPopupMenu;

import javafx.stage.Window;

/**
 * The {@link TrayIconDelegate} wrapps either a {@link TrayIcon} if available or a {@link Window} which simulates a {@link TrayIcon}.
 * It therefore displays simply the same image, the {@link TrayIcon} would display
 * 
 * @author DStalder
 *
 */
public interface TrayIconDelegate {

   /**
    * Adds the given MouseMotionListener
    * 
    * @param mouseMotionListener
    *        the MouseMotionListener to add
    */
   void addMouseMotionListener(MouseMotionListener mouseMotionListener);

   /**
    * Adds the given MouseListener
    * 
    * @param mouseListener
    *        the MouseListener to add
    */
   void addMouseListener(MouseListener mouseListener);

   /**
    * Sets the given image
    * 
    * @param image
    *        the image to set
    */
   void setImage(Image image);

   /**
    * Sets the given text value as tool tip
    * 
    * @param toolTipText
    */
   void setToolTip(String toolTipText);

   /**
    * Displays the given message with the given title and priority
    * 
    * @param messageTitle
    *        the title
    * @param message
    *        the actual message
    * @param messageType
    *        the type of message (info, waring, error)
    */
   void displayMessage(String messageTitle, String message, MessageType messageType);

   /**
    * Called when a mouse dragged was recognized
    * 
    * @param mouseEvent
    */
   void onMouseDragged(MouseEvent mouseEvent);

   /**
    * Called on every MouseEvent. If the given MouseEvent is a popup-trigger, then the given Popups is shown
    * 
    * @param popupMenu
    *        the JPopupMenu
    * @param mouseEvent
    *        the MouseEvent
    */
   void onMouseEvent(JPopupMenu popupMenu, MouseEvent mouseEvent);

   /**
    * Shows this {@link TrayIconDelegate}
    */
   void show();
}
