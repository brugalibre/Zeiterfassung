package com.myownb3.dominic.ui.draw.impl;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import com.myownb3.dominic.ui.styles.color.Colors;
import com.myownb3.dominic.ui.styles.font.Fonts;
import com.myownb3.dominic.util.utils.StringUtil;

/**
 * The {@link ToolTip} is used as tool-tip for all {@link Drawable} objects. It
 * paints any String value as it's tool-tip. It is possible to paint a
 * multi-line tool-tip
 * 
 * @author Dominic
 *
 */
public class ToolTip extends Drawable<String> {
    public static final String NEW_LINE_MARKER = "\n"; // at each such marker, the given string is separated, and
						       // continued on the next line
    protected Stroke toolTipBorderStroke; // the stroke
    private String toolTipValue; // the actually String to paint as tool-tip

    /**
     * Creates a new {@link ToolTip} instance
     * 
     * @param toolTipText
     * @param parent
     */
    public ToolTip(String toolTipText, ParentComponent parent) {
	super(toolTipText, parent);
	toolTipBorderStroke = new BasicStroke(1);
    }

    @Override
    public void draw(Graphics2D g) {
	int xToolTip = x + 35;
	int yToolTip = y + 40;
	int widthToolTip = caluclateMaxWidth(Fonts.TOOL_TIP_LABEL_FONT, false) + 15;
	int heightToolTip = getToolTipHeight(g) + 5;
	g.setStroke(toolTipBorderStroke);

	g.setFont(Fonts.TOOL_TIP_LABEL_FONT);
	g.setColor(Colors.TOOL_TIP_BACKGROUND);

	if (xToolTip + widthToolTip > parent.getWidth()) {
	    xToolTip = (xToolTip - widthToolTip);
	}

	g.fillRect(xToolTip, yToolTip, widthToolTip, heightToolTip);
	g.drawRect(xToolTip, yToolTip, widthToolTip, heightToolTip);

	g.setColor(Colors.TOOL_TIP_LABEL_COLOR);

	drawToolTipInternal(g, xToolTip, yToolTip, false);
    }

    /*
     * finally draws the given value for tool-tip
     */
    private void drawToolTipInternal(Graphics2D g, int xToolTip, int yToolTip, boolean b) {
	String[] toolTips = toolTipValue.split(NEW_LINE_MARKER);
	int yAxisIncrement = StringUtil.getStringHeight(g.getFont());
	for (String toolTip : toolTips) {
	    yToolTip = yToolTip + yAxisIncrement;
	    toolTip = toolTip.replace(NEW_LINE_MARKER, "").replace("null", ""); // replace all the "\n"
	    g.drawString(toolTip, xToolTip, yToolTip);
	}
    }

    protected int getToolTipHeight(Graphics g) {
	String[] toolTips = toolTipValue.split(NEW_LINE_MARKER);
	int yAxisIncrement = StringUtil.getStringHeight(g.getFont());
	int sum = 0;

	// for each tool-tip element which is not null, increment the y-axis margin
	for (String toolTip : toolTips) {
	    if (toolTip != null && !toolTip.equals("null")) {
		sum = sum + yAxisIncrement;
	    }
	}
	return sum;
    }

    /*
     * If the tool-tip is a single line value, then it's length is returned. If the
     * tool-tip consist of multiple line, the maximal length of each line is
     * returned
     */
    protected int caluclateMaxWidth(Font currentFont, boolean shortTooLongNames) {
	String[] singleLines = toolTipValue.split("\n");
	int maxLength = width;

	for (String line : singleLines) {
	    maxLength = Math.max(maxLength, StringUtil.getStringLength(line, currentFont));
	}
	return maxLength;
    }

    public void setToolTipValue(String toolTipValue) {
	this.toolTipValue = toolTipValue;
    }
}
