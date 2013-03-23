package a.window;


import a.drawing.GradientPanel;
import a.drawing.GradientPanel.AjouteCouleur;
import a.drawing.SMenuBar;
import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

/**
 * Gère la fenêtre des dégradés dans l'espace de travail
 * @author Jessym
 */
public class GradientWindow extends JInternalFrame {

    JMenuBar gwbar = new JMenuBar();
    JMenu manager = new JMenu("Fichier");
    JMenuItem addc = new JMenuItem("Ajouter une couleur");
    JMenuItem enregistrer = new JMenuItem("Enregistrer une liste");
    JMenuItem ouvrir = new JMenuItem("Ouvrir une liste");
    public static JScrollPane jsp;

    /**
     * Créer une fenêtre des calques dans l'espace de travail
     * @param pan Panneau des dégardés associé à la fenêtre
     * @param w longueur de la fenêtre
     * @param h hauteur de la fenêtre
     * @param x position x de la fenêtre
     * @param y position y de la fenêtre
     */
    public GradientWindow(GradientPanel pan, int w, int h, int x, int y) {
        this.setTitle("Gradient Manager");
        this.setClosable(true);
        this.setResizable(true);
        this.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);

        this.setMaximizable(false);
        this.setIconifiable(true);
        this.setSize(w, h);
        this.setPreferredSize(new Dimension(w, h));
        this.setLocation(x, y);
        this.setLayout(new FlowLayout());
        this.setJMenuBar(gwbar);

        try {
            this.setFrameIcon(new ImageIcon(ImageIO.read(getClass().getResource("/icons/gradienticon.png"))));
        } catch (IOException ex) {
            Logger.getLogger(IWCameraWindow.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Gestion Scrolling

        this.setMaximumSize(new Dimension(w, 1000));
        this.setMinimumSize(new Dimension(w, 300));

        jsp = new JScrollPane(pan, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.setContentPane(jsp);
        jsp.getVerticalScrollBar().setUnitIncrement(20);

        // Gestion JMenuBar 
        gwbar.add(manager);

        manager.add(addc);
        manager.add(enregistrer);
        manager.add(ouvrir);

        addc.addActionListener(pan.new AjouteCouleur());
        enregistrer.addActionListener(pan.new SauvegardeDegrade());
        ouvrir.addActionListener(pan.new ChargeDegrade());


        // Gestion affichage de la fenetre 

        this.addInternalFrameListener(new InternalFrameListener() {

            public void internalFrameOpened(InternalFrameEvent e) {
            }

            public void internalFrameClosing(InternalFrameEvent e) {
                SMenuBar.gradient.doClick();
            }

            public void internalFrameClosed(InternalFrameEvent e) {
            }

            public void internalFrameIconified(InternalFrameEvent e) {
            }

            public void internalFrameDeiconified(InternalFrameEvent e) {
            }

            public void internalFrameActivated(InternalFrameEvent e) {
            }

            public void internalFrameDeactivated(InternalFrameEvent e) {
            }
        });

        this.setVisible(true);
        this.hide();
    }
}
