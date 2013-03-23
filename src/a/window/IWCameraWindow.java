package a.window;


import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import javax.swing.JComponent;
import JMyron.*;
import a.drawing.Calques;
import a.drawing.PaintPanel;
import a.drawing.SMenuBar;
import a.util.SPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

/**
 * Gère l'affiche de la fenêtre Caméra dans l'espace de travail
 */
public class IWCameraWindow extends JInternalFrame {

    static JMyron m;//a camera object
    int width = 320, vwidth = 320;
    int height = 240, vheight = 240;
    VideoPane vp;
    int frameRate = 15; //fps
    JButton play = new JButton(new ImageIcon(getClass().getResource("/camera/record.png")));
    JButton stop = new JButton(new ImageIcon(getClass().getResource("/camera/stop.png")));
    JButton shot = new JButton(new ImageIcon(getClass().getResource("/camera/shot.png")));
    JPanel cont = new JPanel();
    SPanel control = new SPanel(SPanel.BLACK);
    BufferedImage stopPreview;
    boolean start = true;
    Timer videoTimer;
    public boolean init = true;
    int x, y;

    /**
     * Créer la fenêtre aux coordonées passées en paramètres
     * @param xs coordonée x d'affichage
     * @param ys coordonnée y d'affichage
     */
    public IWCameraWindow(int xs, int ys) {

        x = xs;
        x = ys;
    }

    /**
     * Initilisaise les pilotes de reconnaissance de la webcam
     */
    public void init() {
        m = new JMyron();


        m.start(width, height);
        m.findGlobs(0);//disable the intelligence to speed up frame rate

        vp = new VideoPane();

        try {
            stopPreview = ImageIO.read(getClass().getResource("/camera/camsplash.png"));
        } catch (IOException ex) {
            Logger.getLogger(IWCameraWindow.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.add(cont);
        cont.setLayout(new BorderLayout());
        cont.add(control, BorderLayout.NORTH);
        cont.add(vp, BorderLayout.CENTER);

        control.setBorder(BorderFactory.createLineBorder(Color.black));


        play.setBackground(null);
        play.setPreferredSize(new Dimension(49, 49));
        play.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        stop.setBackground(null);
        stop.setPreferredSize(new Dimension(49, 49));
        stop.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        shot.setBackground(null);
        shot.setPreferredSize(new Dimension(49, 49));
        shot.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        control.add(play);
        control.add(stop);
        control.add(shot);


        play.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!start) {
                    m.start(width, height);
                    start = true;
                }
            }
        });

        stop.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (start) {
                    m.stop();
                    start = false;
                }
            }
        });

        shot.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                if (start) {

                    BufferedImage bi = new BufferedImage(PaintPanel.WSIZE, PaintPanel.HSIZE, PaintPanel.type);
                    bi.getGraphics().drawImage(vp.bi, 0, 0, PaintPanel.WSIZE, PaintPanel.HSIZE, null);

                    Calques.historiques.get(Calques.current).add(PaintPanel.newCopy(bi, PaintPanel.type));
                    Calques.calques.setElementAt(PaintPanel.newCopy(bi, PaintPanel.type), Calques.current);
                    CalquesWindow.refreshCalques(Calques.calques);
                }
            }
        });


        videoTimer = new Timer(1000 / frameRate, new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                vp.repaint();
            }
        });
        videoTimer.start();

        this.setTitle("IW Caméra");
        this.setClosable(true);
        this.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        this.setResizable(false);
        this.setIconifiable(true);
        try {
            this.setFrameIcon(new ImageIcon(ImageIO.read(getClass().getResource("/icons/camicon.png"))));
        } catch (IOException ex) {
            Logger.getLogger(IWCameraWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.setLocation(x, y);
        this.setSize(width, height + 89);
        this.addInternalFrameListener(new InternalFrameListener() {

            public void internalFrameOpened(InternalFrameEvent e) {
            }

            public void internalFrameClosing(InternalFrameEvent e) {
                if (start) {
                    m.stop();
                    start = false;
                }
                SMenuBar.iwcam.doClick();
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
        stop.doClick();
        init = false;
    }

    /**
     * Classe interne qui gère le panneau d'affiche du rendu de la webcam
     */
    class VideoPane extends JComponent {

        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        public void paint(Graphics g) {
            if (start) {
                m.update();
                int[] img = m.image();
                bi.setRGB(0, 0, width, height, img, 0, width);
                g.drawImage(bi, 0, 0, vwidth, vheight, this);
            } else {
                g.drawImage(stopPreview, 0, 0, vwidth, vheight, null);
            }

        }
    }
}
