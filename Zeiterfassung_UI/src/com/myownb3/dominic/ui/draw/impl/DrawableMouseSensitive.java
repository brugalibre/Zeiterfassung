package com.myownb3.dominic.ui.draw.impl;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.Timer;

/**
 * A abstract class for {@link Drawable} objects, which can be controlled by
 * mouse. Therefore it is mouse sensitive since it implements
 * {@link MouseMotionListener} and {@link MouseListener}
 * 
 * @author Dominic
 *
 * @param <E>
 */
public abstract class DrawableMouseSensitive<E> extends Drawable<E>
	implements MouseListener, MouseMotionListener, ActionListener {
    protected Timer timer; // Timer which is used, e.g. to mark the hover
			   // delayed

    protected boolean isToolTipEnabled; // true if tool tip are drawn
    protected ToolTip toolTipValue; // the value to draw as tool-tip

    protected Stroke borderStroke; // Stroke used for the border

    protected boolean isLocked; // true if this drawable object was clicked and
				// hold with the mouse
    protected boolean isSelected; // true if this drawable object was clicked
				  // (and released!) by the mouse
    protected boolean isHovered; // true if the mouse-pointer is placed up this
				 // Drawable-object

    protected int mouseX; // the x-Coordinate of the mouse
    protected int mouseY; // the y-coordinate of the mouse
    private boolean started;

    /**
     * Creates anew {@link DrawableMouseSensitive} instance with the given
     * representing value and parent
     * 
     * @param value
     * @param parent
     */
    public DrawableMouseSensitive(E value, ParentComponent parent) {
	super(value, parent);
	timer = new Timer(300, this);
	timer.setRepeats(false);
	borderStroke = new BasicStroke(2);
	toolTipValue = new ToolTip(null, parent);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DRAW //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
     * Draws a tool tip
     */
    protected void drawToolTip(Graphics2D g) {
	if (isHovered && isToolTipEnabled) // do not show the redundant tool
					   // tip, if no name was shorted
	{
	    toolTipValue.draw(g);
	}
    }

    /*
     * Draws a border which marks this Drawable-Object when the mouse-pointer
     * clicked on it
     */
    protected void drawBorder(Graphics2D g, Shape shape) {
	g.setStroke(borderStroke);
	g.draw(shape);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ACTION LISTENER FOR TIMER //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void actionPerformed(ActionEvent e) {
	isHovered = true;
	updateParent(UpdateType.REPAINT);
	timer.stop();
	toolTipValue.setBounds(new Rectangle(x, y, toolTipValue.getWidth(), toolTipValue.getHeight()));
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // MOUSE LISTENER //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void mousePressed(MouseEvent e) {
	// = the mouse click must be within the x-range and within the y-range
	if (hasIntersect(new Point(e.getX(), e.getY()))) {
	    isLocked = true;
	}
    }

    @Override
    public void mouseReleased(MouseEvent e) {
	isLocked = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
	if (hasIntersect(new Point(e.getX(), e.getY()))) {
	    isLocked = false; // remove lock if clicked
	    isSelected = !isSelected;
	    if (isSelected)
		isHovered = false; // do not show tool-tip when clicked
	    updateParent(UpdateType.REPAINT, UpdateType.GRAB_FOCUS); // Parent
								     // panel
								     // should
								     // grab
								     // focus,
								     // e.g. for
								     // KeyListener
	}
    }

    @Override
    public void mouseMoved(MouseEvent e) {
	if (!started && hasIntersect(new Point(e.getPoint()))) {
	    mouseX = (int) e.getPoint().getX();
	    mouseY = (int) e.getPoint().getY();
	    timer.start();
	    started = true;
	} else {
	    isHovered = false;
	}
    }

    @Override
    public void mouseEntered(MouseEvent e) {
	if (hasIntersect(e.getPoint())) {
	    mouseX = (int) e.getPoint().getX();
	    mouseY = (int) e.getPoint().getY();
	    timer.start();
	}
    }

    @Override
    public void mouseExited(MouseEvent e) {
	timer.stop();
	started = false;
	isHovered = false;
	updateParent(UpdateType.REPAINT);
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {
	// do nothing, can be overridden by subclasses
    }

    /**
     * Returns true if this {@link DrawableMouseSensitive} has been clicked by the
     * mouse
     * 
     * @return true if this {@link DrawableMouseSensitive} has been clicked by the
     *         mouse
     */
    public boolean isSelected() {
	return isSelected;
    }
}
