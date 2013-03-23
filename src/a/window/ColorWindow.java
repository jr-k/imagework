package a.window;


import a.drawing.PaintPanel;
import a.drawing.SMenuBar;
import a.util.TImage;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

/**
 * Gestion de la fenêtre de la palette de couleur
 * @author Jessym
 */
public class ColorWindow extends JInternalFrame {

    static JLabel alphaValue = new JLabel("A: 100%");
    static JLabel redValue = new JLabel("R: 000");
    static JLabel greenValue = new JLabel("V: 000");
    static JLabel blueValue = new JLabel("B: 000");
    public static JSlider alphaSlider = new JSlider(0, 100, 100);
    public static JSlider redSlider = new JSlider(0, 255, 0);
    public static JSlider blueSlider = new JSlider(0, 255, 0);
    public static JSlider greenSlider = new JSlider(0, 255, 0);
    static int udefRedValue = 255;
    static int udefGreenValue = 255;
    static int udefBlueValue = 255;
    public static JPanel userDefButton = new JPanel();
    public static JPanel userDefButton2 = new JPanel();
    public static Color uColor1 = Color.black;
    public static Color uColor2 = Color.white;
    public static int ROWCOLOR = 1;
    static JPanel colorPanel;
    TImage cgrid;
    JButton cgrid2;
    JButton cgrid3;

    /**
     * Créer une fenêtre de palette des couleurs dans l'espace de travail
     * @param w longueur de la fenêtre
     * @param h hauteur de la fenêtre
     * @param x position x de la fenêtre
     * @param y position y de la fenêtre
     */
    public ColorWindow(int w, int h, int x, int y) {
        this.setTitle("Palette");
        this.setClosable(true);
        this.setResizable(true);
        this.setMaximizable(false);
        this.setIconifiable(true);
        this.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        this.setSize(w, h);
        this.setPreferredSize(new Dimension(w, h));
        this.setLocation(x, y);
        this.setLayout(new FlowLayout());

        try {
            this.setFrameIcon(new ImageIcon(ImageIO.read(getClass().getResource("/icons/paletteicon.png"))));
        } catch (IOException ex) {
            Logger.getLogger(IWCameraWindow.class.getName()).log(Level.SEVERE, null, ex);
        }

        userDefButton.setBackground(Color.black);
        userDefButton2.setBackground(Color.white);

        this.addInternalFrameListener(new InternalFrameListener() {

            public void internalFrameOpened(InternalFrameEvent e) {
            }

            public void internalFrameClosing(InternalFrameEvent e) {
                SMenuBar.couleur.doClick();
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

        userDefButton.addMouseListener(new MouseListener() {

            public void mousePressed(MouseEvent e) {
                ROWCOLOR = 1;
            }

            public void mouseClicked(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });

        userDefButton2.addMouseListener(new MouseListener() {

            public void mousePressed(MouseEvent e) {
                ROWCOLOR = 2;
            }

            public void mouseClicked(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });

        redSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                updateRGBValues();
            }
        });
        greenSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                updateRGBValues();
            }
        });
        blueSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                updateRGBValues();
            }
        });

        alphaSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                alphaValue.setText("A: " + fill3empty(alphaSlider.getValue()) + "%");
            }
        });

        alphaValue.setHorizontalAlignment(JLabel.RIGHT);
        redValue.setHorizontalAlignment(JLabel.RIGHT);
        greenValue.setHorizontalAlignment(JLabel.RIGHT);
        blueValue.setHorizontalAlignment(JLabel.RIGHT);

        alphaSlider.setPreferredSize(new Dimension(100, 40));
        redSlider.setPreferredSize(new Dimension(100, 40));
        greenSlider.setPreferredSize(new Dimension(100, 40));
        blueSlider.setPreferredSize(new Dimension(100, 40));



        //... Layout the color buttons
        colorPanel = new JPanel();
        colorPanel.setSize(w - 20, h - 20);
        colorPanel.setPreferredSize(new Dimension(w - 20, h - 20));
        colorPanel.setLayout(new FlowLayout());

        JPanel alphapane = new JPanel();
        alphapane.add(alphaValue);
        alphapane.add(alphaSlider);
        alphapane.setPreferredSize(new Dimension(200, 35));

        JPanel redpane = new JPanel();
        redpane.add(redValue);
        redpane.add(redSlider);
        redpane.setPreferredSize(new Dimension(200, 35));

        JPanel greenpane = new JPanel();
        greenpane.add(greenValue);
        greenpane.add(greenSlider);
        greenpane.setPreferredSize(new Dimension(200, 35));

        JPanel bluepane = new JPanel();
        bluepane.add(blueValue);
        bluepane.add(blueSlider);
        bluepane.setPreferredSize(new Dimension(200, 35));


        userDefButton.setBorder(BorderFactory.createBevelBorder(1));
        userDefButton.setPreferredSize(new Dimension(70, 60));

        userDefButton2.setBorder(BorderFactory.createBevelBorder(1));
        userDefButton2.setPreferredSize(new Dimension(70, 60));


        cgrid = new TImage("/gradient/cgrid.png");
        cgrid.addMouseListener(new MouseListener() {

            public void mousePressed(MouseEvent e) {
                changeColor(e);
            }

            public void mouseClicked(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });



        cgrid2 = new JButton("1");
        cgrid2.setHorizontalAlignment(JButton.CENTER);
        cgrid2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JColorChooser jcc = new JColorChooser();
                Color newColor = JColorChooser.showDialog(null, "Choix de la couleur", userDefButton.getBackground());
                if (newColor != null) {
                    changeColor(1, newColor);
                }
            }
        });

        cgrid3 = new JButton("2");
        cgrid3.setHorizontalAlignment(JButton.CENTER);
        cgrid3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JColorChooser jcc = new JColorChooser();
                Color newColor = JColorChooser.showDialog(null, "Choix de la couleur", userDefButton2.getBackground());
                if (newColor != null) {
                    changeColor(2, newColor);
                }
            }
        });

        cgrid2.setBorder(BorderFactory.createEtchedBorder());
        cgrid2.setPreferredSize(new Dimension(70, 20));

        cgrid3.setBorder(BorderFactory.createEtchedBorder());
        cgrid3.setPreferredSize(new Dimension(70, 20));


        colorPanel.add(cgrid);
        colorPanel.add(cgrid2);
        colorPanel.add(cgrid3);
        colorPanel.add(userDefButton);
        colorPanel.add(userDefButton2);
        colorPanel.add(alphapane);
        colorPanel.add(redpane);
        colorPanel.add(greenpane);
        colorPanel.add(bluepane);



        // Gestion Scrolling

        this.setMaximumSize(new Dimension(w, 1000));
        this.setMinimumSize(new Dimension(w, 300));

        JScrollPane jsp = new JScrollPane(colorPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.setContentPane(jsp);
        jsp.getVerticalScrollBar().setUnitIncrement(10);

        this.setVisible(true);
    }

    
    /**
     * Change la couleur courante de l'utilisateur
     * @param i désigne la couleur d'avant-plan (1) ou d'arrière-plan (2)
     * @param c désigne la couleur à affecter 
     */
    public void changeColor(int i, Color c) {


        switch (i) {
            case 1:
                ROWCOLOR = 1;
                userDefButton.setBackground(c);
                uColor1 = c;

                break;
            case 2:
                ROWCOLOR = 2;
                userDefButton2.setBackground(c);
                uColor2 = c;

                break;
            default:
        }

        if (PaintPanel.tf!=null)PaintPanel.tf.setForeground(c);

    }

    /**
     * Change la couleur courante de l'utilisateur
     * @param e Evénement dû au clic sur l'image pour récupérer la couleur aux coordonées x,y, pour l'affecter à la couleur courante
     */
    public void changeColor(MouseEvent e) {
        int rgb = ((TImage) e.getSource()).getColorAt(e.getX(), e.getY());
        String rgb2 = Integer.toHexString(rgb);

        Color c = Color.decode("0x" + rgb2.substring(2, rgb2.length()).toUpperCase());

        switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                ROWCOLOR = 1;
                userDefButton.setBackground(c);
                uColor1 = c;

                break;
            case MouseEvent.BUTTON2:
                break;
            case MouseEvent.BUTTON3:
                ROWCOLOR = 2;
                userDefButton2.setBackground(c);
                uColor2 = c;

                break;
            default:
        }

        if (PaintPanel.tf!=null)PaintPanel.tf.setForeground(c);

    }

    /**
     * Met à jour les valeurs des composantes Rouges, Vertes, Bleues
     */
    public static void updateRGBValues() {
        udefRedValue = redSlider.getValue();
        udefGreenValue = greenSlider.getValue();
        udefBlueValue = blueSlider.getValue();

        if (udefRedValue > 255) {
            udefRedValue = 255;
        }
        if (udefRedValue < 0) {
            udefRedValue = 0;
        }
        if (udefGreenValue > 255) {
            udefGreenValue = 255;
        }
        if (udefGreenValue < 0) {
            udefGreenValue = 0;
        }
        if (udefBlueValue > 255) {
            udefBlueValue = 255;
        }
        if (udefBlueValue < 0) {
            udefBlueValue = 0;
        }

        redValue.setText("R: " + fill3zero(udefRedValue));
        greenValue.setText("V: " + fill3zero(udefGreenValue));
        blueValue.setText("B: " + fill3zero(udefBlueValue));

        if (ROWCOLOR == 1) {
            uColor1 = new Color(udefRedValue, udefGreenValue, udefBlueValue);
            userDefButton.setBackground(uColor1);
        } else if (ROWCOLOR == 2) {
            uColor2 = new Color(udefRedValue, udefGreenValue, udefBlueValue);
            userDefButton2.setBackground(uColor2);
        }

        if (PaintPanel.tf != null) {
            PaintPanel.tf.setForeground(uColor1);
        }


    }
    
    /**
     * Renvoi la couleur du clic considéré 
     * Exemple : clic gauche sur l'image dessine avec la couleur 1
     *           clic droit sur l'image dessine avec la couleur 2
     * @param e Evénement dû au clic
     * @return la couleur 
     */
    public static Color getMouseColor(MouseEvent e) {
        Color tmp = null;
        switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                tmp = uColor1;
                break;
            case MouseEvent.BUTTON2:
                break;
            case MouseEvent.BUTTON3:
                tmp = uColor2;
                break;
            default:
                tmp = Color.black;
        }
        return tmp;
    }

    /**
     * Méthode utilitaire visant à remplir avec des 0 une chaîne de caractère
     * @param n entier à "remplir" avec des 0
     * @return Nouvelle chaîne de 3 caractères remplie avec des 0
     */
    public static String fill3zero(int n) {
        if (n < 10) {
            return "00" + n;
        } else if (n < 100) {
            return "0" + n;
        } else {
            return "" + n;
        }
    }

    /**
     * Méthode utilitaire visant à remplir de vide une châine de caractère
     * @param n entier à "remplir" avec des " "
     * @return Nouvelle chaîne de 3 caractères remplie avec des blancs
     */
    public static String fill3empty(int n) {
        if (n < 10) {
            return "  " + n;
        } else if (n < 100) {
            return " " + n;
        } else {
            return "" + n;
        }
    }


}
