/**
 * 
 */
package com.myownb3.dominic.ui.draw.raster.cell.impl;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.myownb3.dominic.ui.draw.impl.ParentComponent;
import com.myownb3.dominic.ui.styles.color.Colors;

/**
 * @author Dominic
 * 
 */
public class MouseSensitivCellImpl extends AbstractMouseSensitivCell {
    protected static final JComponent comp;
    protected boolean drawShape;

    static {
	comp = new JPanel();
    }

    /**
     * @param value
     * @param height
     * @param width
     * @param parent
     */
    public MouseSensitivCellImpl(String value, ParentComponent parent) {
	super(value, parent);
	drawShape = true;
    }

    @Override
    public void draw(Graphics2D g) {
	drawValue(g);
	drawShape(g);
    }

    /**
     * @param g
     */
    @Override
    protected void drawValue(Graphics2D g) {
	setValCoordinates();
	g.setColor(color);
	g.setFont(getFont());
	g.drawString(value, xValueCoordinate, yValueCoordinate);
    }

    /**
     * @param g
     */
    private void drawShape(Graphics2D g) {
	if (drawShape) {
	    g.setColor(Colors.VERTICAL_LINE_COLOR);
	    g.setStroke(stroke);
	    g.draw(shape);
	}
    }

    /**
     * @param font
     * @return
     */
    public static FontMetrics getFontMetrics(Font font) {
	return comp.getFontMetrics(font);
    }

    public void setDrawShape(boolean drawShape) {
	this.drawShape = drawShape;
    }
}
