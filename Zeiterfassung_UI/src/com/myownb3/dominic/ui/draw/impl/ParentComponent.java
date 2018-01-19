package com.myownb3.dominic.ui.draw.impl;


import javax.swing.JComponent;
import javax.swing.JPanel;

import com.myownb3.dominic.ui.draw.impl.Drawable.UpdateType;

/**
 * A {@link ParentComponent} is a {@link JComponent} which has one ore more more child components which this one is responsible for.
 * A child component can 'auffordern' to update it's parent e.g. for repainting or recalculating every time the child changes it's values
 * @author Dominic
 *
 */
public abstract class ParentComponent extends JPanel
{
   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   /**
    * Updates this parent, either by repainting or by recalculating values which are depending on it's children's
    * @param drawable, the drawable which caused the update
    * @param types
    */
   public void update (Drawable<?> drawable, Drawable.UpdateType... types)
   {
      for (UpdateType  type : types)
      {
         switch (type)
         {
            case AFTER_ACTION:
               afterAction ();
               break;
            case GRAB_FOCUS:
               grabFocus ();
               break;
            case RECALCULATE:
               recalculate (drawable);
               break;
            case CHECK_CONDITION:
               checkCondition (drawable);
               break;
            case REPAINT:
               repaintComponent ();
               break;
            default:
               throw new RuntimeException ("Unkonw UpdateType '" + type + "'");
         }
      }
   }

   protected abstract void afterAction ();

   protected abstract void checkCondition (Drawable<?> drawable);

   protected abstract void recalculate (Drawable<?> drawable);

   /**
    * A simple implementation of refreshing the UI. Subclasses can do more then just invoke the swing-repaint().
    */
   protected void repaintComponent ()
   {
      repaint ();
   }
}
