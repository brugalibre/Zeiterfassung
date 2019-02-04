package com.myownb3.dominic.ui.draw.impl;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.myownb3.dominic.ui.draw.Renderer;

/**
 * The {@link Drawable} class can be used to draw a representation of any
 * instantiated object on the {@link MainUserInterface} component
 * 
 * @author Dominic
 * @param <E>
 *            the value this Drawable represents on the
 *            {@link MainUserInterface}
 * 
 */
public abstract class Drawable<E> implements Renderer {
    public enum UpdateType {
	REPAINT, RECALCULATE, GRAB_FOCUS // after the this Drawable has done some action or before doing something next,
					 // the parent component can be forced to grab focus
	, CHECK_CONDITION // e.g. during the drawable is moving, the condition can be checked
	, AFTER_ACTION // after this Drawable is finished with actions, that should be updated at the
		       // end, instead of permanently
    }

    protected ParentComponent parent; // the parent of this drawable, responsible for refreshing
    protected E value; // the value this Drawable represents on a panel
    protected int x; // the x-coordinate in relation to it's position on the TimeLine (used for
		     // painting)
    protected int y; // the y-coordinate in relation to it's position on the TimeLine (used for
		     // painting)
    protected int height;
    protected int width;
    protected Dimension dimension;
    protected Shape shape;

    /**
     * Creates a new {@link Drawable} with the given parent and value
     * 
     * @param parent,
     *            the parent of this component
     * @param value
     */
    public Drawable(E value, ParentComponent parent) {
	this.value = value;
	this.parent = parent;
    }

    /**
     * Creates a new {@link Drawable} with the given parent and value
     * 
     * @param parent,
     *            the parent of this component
     * @param value
     */
    public Drawable(E value, ParentComponent parent, Shape shape) {
	this.value = value;
	this.parent = parent;
	this.shape = shape;
    }

    /**
     * Sets the bounds the layout manager has assigned to this {@link Drawable}.
     * Those, of course, have to be considered in the rendering process.
     * 
     * @param bounds
     *            the new bounds for the Drawable.
     */
    @Override
    public void setBounds(Rectangle bounds) {
	this.shape = bounds;
	this.x = bounds.x;
	this.y = bounds.y;
	this.height = bounds.height;
	this.width = bounds.width;
    }

    public void setBounds(int x, int y, int height, int width) {
	Rectangle rec = new Rectangle(x, y, width, height);
	setBounds(rec);
    }

    /**
     * Finally renders the Object in the Graphics object.
     * 
     * @param g
     *            the Graphics2D object in which to render
     */
    @Override
    public abstract void draw(Graphics2D g);

    /**
     * Gets the bounds for this Drawable.
     * 
     * @return the bounds of this {@link Drawable}.
     */
    @Override
    public Shape getShape() {
	return shape;
    }

    /*
     * Returns true if the given point is within the area of this Drawable-object
     */
    protected boolean hasIntersect(Point e) {
	return (e.getX() >= x && e.getX() <= x + width && e.getY() >= y && e.getY() <= y + height);
    }

    @Override
    public String toString() {
	Field[] fields = getClass().getDeclaredFields();
	StringBuilder builder = new StringBuilder(
		"\n/**************/ " + getClass().getSimpleName() + " START /**************/");
	for (Field field : fields) {
	    if (!Modifier.isStatic(field.getModifiers())) {
		try {
		    builder.append("\n" + field.getName() + ": " + field.get(this));
		} catch (IllegalArgumentException | IllegalAccessException e) {
		    throw new RuntimeException(e);
		}
	    }
	}
	builder.append("\n/**************/ " + getClass().getSimpleName() + " ENDE /**************/ \n");
	return builder.toString();
    }

    /**
     * Forwards the specific type of {@link UpdateType} to it's
     * {@link ParentComponent} in order it can take further stepts according the
     * kind of update
     * 
     * @param type,
     *            kind of update to do
     */
    protected void updateParent(UpdateType... type) {
	parent.update(this, type);
    }

    public void setX(int x) {
	this.x = x;
    }

    public int getX() {
	return x;
    }

    public void setY(int y) {
	this.y = y;
    }

    public int getY() {
	return y;
    }

    public int getHeight() {
	return this.height;
    }

    public int getWidth() {
	return this.width;
    }

    /**
     * Returns the {@link #value} field
     * 
     * @return the {@link #value} field
     */
    public void setValue(E value) {
	this.value = value;
    }

    /**
     * Returns the {@link #value} field
     * 
     * @return the {@link #value} field
     */
    public E getValue() {
	return value;
    }
}
