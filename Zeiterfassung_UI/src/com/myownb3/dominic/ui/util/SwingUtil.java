/**
 * 
 */
package com.myownb3.dominic.ui.util;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author Dominic
 * 
 */
public class SwingUtil {
   /**
    * @param areaText
    * @return an {@link JPanel} which contains an {@link JTextArea} and a
    *         {@link JLabel} as a title
    */
   public static JComponent getDisplayArea(String infoMessage, String areaText) {
      JPanel content = new JPanel(new BorderLayout());
      JLabel infoMessageLabel = new JLabel(infoMessage);

      JTextArea area = new JTextArea();
      JScrollPane scrollPane = new JScrollPane(area);
      scrollPane.setAutoscrolls(false);
      area.setText(areaText);
      area.setEditable(false);

      content.add(infoMessageLabel, BorderLayout.PAGE_START);
      content.add(scrollPane, BorderLayout.CENTER);
      content.setSize(new Dimension(785, 200));
      content.setPreferredSize(content.getSize());

      return content;
   }
}
