package a.window;


import a.drawing.SMenuBar;
import a.util.CpuGraph;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

/**
 * Gère la fenêtre des statistiques
 * @author Jessym
 */
public class CpuWindow extends JInternalFrame {

    static JPanel cpuPanel = new JPanel();
    public static Object processCpuLoad = 0;
    public static Object systemCpuLoad = 0;
    public static CpuGraph graph = new CpuGraph();

    /**
     * Créer une fenêtre des statistiqus dans l'espace de travail
     * @param w longueur de la fenêtre
     * @param h hauteur de la fenêtre
     * @param x position x de la fenêtre
     * @param y position y de la fenêtre
     */
    public CpuWindow(int w, int h, int x, int y) {
        this.setTitle("Statistiques CPU");
        this.setClosable(true);
        this.setResizable(false);
        this.setMaximizable(false);
        this.setIconifiable(true);
        this.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        this.setSize(w, h);
        this.setPreferredSize(new Dimension(w, h));
        this.setLocation(x, y);
        this.setLayout(new FlowLayout());

        try {
            this.setFrameIcon(new ImageIcon(ImageIO.read(getClass().getResource("/icons/cpuicon.png"))));
        } catch (IOException ex) {
            Logger.getLogger(IWCameraWindow.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Création des panneaux pour les 2 Infos
        cpuPanel.setLayout(new BorderLayout());


        JPanel graphpane = new JPanel();
        graphpane.setBackground(Color.black);
        graphpane.add(graph);

        graph.setPreferredSize(new Dimension(200, 100));
        graph.setBackground(Color.white);
        graph.setBorder(BorderFactory.createLoweredBevelBorder());

        // Positionnement    
        cpuPanel.add(graphpane, BorderLayout.CENTER);





        this.addInternalFrameListener(new InternalFrameListener() {

            public void internalFrameOpened(InternalFrameEvent e) {
            }

            public void internalFrameClosing(InternalFrameEvent e) {
                SMenuBar.cpu.doClick();
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


        // Gestion Scrolling

        this.setMaximumSize(new Dimension(w, 100));
        this.setMinimumSize(new Dimension(w, 100));

        JScrollPane jsp = new JScrollPane(cpuPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.setContentPane(jsp);


        this.setVisible(true);
        this.hide();
    }

  

  
}
