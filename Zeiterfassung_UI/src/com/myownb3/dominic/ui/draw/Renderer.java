package com.myownb3.dominic.ui.draw;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

/**
 * 
 * @author Dominic
 *
 */
public interface Renderer {
    /**
     * Sets the bounds the layout manager has assigned to this renderer. Those, of
     * course, have to be considered in the rendering process.
     * 
     * @param bounds
     *            the new bounds for the renderer.
     */
    public void setBounds(Rectangle bounds);

    /**
     * Gets the bounds for this renderer.
     * 
     * @return the {@link Shape} of this renderer. If <code>setBounds</code> has not
     *         been called before, the bounds computed from
     *         <code>getPreferredSize</code> is returned.
     */
    public Shape getShape();

    /**
     * Finally renders the Object in the Graphics object.
     * 
     * @param g
     *            the Graphics2D object in which to render
     */
    public void draw(Graphics2D g);
}
