/**
 * 
 */
package com.myownb3.dominic.ui.draw.raster.cell.impl;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

import com.myownb3.dominic.ui.draw.impl.DrawableMouseSensitive;
import com.myownb3.dominic.ui.draw.impl.ParentComponent;
import com.myownb3.dominic.ui.draw.raster.cell.Cell;
import com.myownb3.dominic.ui.draw.raster.cell.impl.CellContraints.PositionContraints;
import com.myownb3.dominic.ui.styles.color.Colors;
import com.myownb3.dominic.ui.styles.font.Fonts;
import com.myownb3.dominic.ui.util.Util;

/**
 * @author Dominic
 * 
 */
public abstract class AbstractMouseSensitivCell extends DrawableMouseSensitive<String> implements Cell {
    private static final int ADDITIONALLY_HEIGHT;
    private static final int ADDITIONALLY_WIDTH;
    protected static final int CELL_MARGIN; // space between the border of the
					    // cell and it's value
    protected int xValueCoordinate;
    protected int yValueCoordinate;
    protected Color color;
    protected PositionContraints contraint;
    protected Font font;
    protected Stroke stroke;

    static {
	CELL_MARGIN = 5;
	ADDITIONALLY_HEIGHT = 7;
	ADDITIONALLY_WIDTH = 15;
    }

    public AbstractMouseSensitivCell(String value, Font f, Color c, PositionContraints cont, ParentComponent parent) {
	super(value, parent);

	this.value = value;
	this.color = c;
	this.font = (f);
	this.contraint = cont;

	stroke = new BasicStroke(2);

	this.height = Util.getTextHight(this) + ADDITIONALLY_HEIGHT;
	this.width = Util.getTextLength(value, this) + ADDITIONALLY_WIDTH;
	shape = new Rectangle(x, y, width, height);
	dimension = new Dimension(height, width);

	setValCoordinates();
    }

    public AbstractMouseSensitivCell(String value, ParentComponent parent) {
	this(value, Fonts.DIALOG_LABEL_FONT, Colors.OVERVIEW_TEXT_COLOR, PositionContraints.RIGHT, parent);
    }

    protected abstract void drawValue(Graphics2D g);

    /**
    * 
    */
    protected void setValCoordinates() {
	switch (contraint) {
	case RIGHT:
	    xValueCoordinate = x + CELL_MARGIN;
	    yValueCoordinate = y + (height / 2) + (Util.getTextHight(this) / 3);
	    break;

	case CENTERED:
	    xValueCoordinate = x + (width - Util.getTextLength(value, this)) / 2;
	    ;
	    yValueCoordinate = y + (height / 2) + (Util.getTextHight(this) / 3);
	    break;

	default:
	    throw new IllegalArgumentException("unsupported constraint '" + contraint + "'");
	}
    }

    @Override
    public int getHeight() {
	return height;
    }

    @Override
    public int getWidth() {
	return width;
    }

    @Override
    public void setWidth(int width) {
	this.width = width;
    }

    @Override
    public int getX() {
	return x;
    }

    public void setX(int x) {
	this.x = x;
    }

    @Override
    public int getY() {
	return y;
    }

    public void setY(int y) {
	this.y = y;
    }

    public Font getFont() {
	return font;
    }
}
