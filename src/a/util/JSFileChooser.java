package a.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import javax.swing.JFileChooser;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import sun.swing.FilePane;

/**
 * Permet d'utiliser des FileChooser avec le Look and Feel du système
 * @author Simon
 */
public class JSFileChooser extends JFileChooser{
   
    
    public JSFileChooser()    
    {
        super();
          try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (UnsupportedLookAndFeelException e) {
        }
    }
    
}
