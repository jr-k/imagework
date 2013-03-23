package a.window;

import a.drawing.Calques;
import a.drawing.PaintPanel;
import a.drawing.SToolBar;
import a.util.ResizeImage;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Gestion de la fenêtre des Calques
 * @author Jessym
 */
public class CalquesWindow extends JInternalFrame {

    static JPanel calquesPane = new JPanel();
    JMenuBar cmenubar = new JMenuBar();
    JMenu fichier = new JMenu("Calques");
    public static JMenuItem ajouter = new JMenuItem("Ajouter un nouveau calque");
    public static JMenuItem supprimer = new JMenuItem("Supprimer le calque actif");
    public static JMenuItem dupliquer = new JMenuItem("Dupliquer le calque actif");
    JMenu position = new JMenu("Positions");
    public static JMenuItem cup = new JMenuItem("Monter le calque actif"),
            cdown = new JMenuItem("Descendre le calque actif");
    JMenu fusion = new JMenu("Fusion");
    public static JMenuItem fusprec = new JMenuItem("Fusionner avec le calque inférieur");
    public JMenuItem dajouter = new JMenuItem("Ajouter un nouveau calque");
    public JMenuItem dsupprimer = new JMenuItem("Supprimer le calque actif");
    public JMenuItem ddupliquer = new JMenuItem("Dupliquer le calque actif");
    public JMenuItem dcup = new JMenuItem("Monter le calque actif");
    public JMenuItem dcdown = new JMenuItem("Descendre le calque actif");
    public JMenuItem dfusprec = new JMenuItem("Fusionner avec le calque inférieur");
    public static JPopupMenu jpmcalques = new JPopupMenu();

    /**
     * Créer une fenêtre des calques dans l'espace de travail
     * @param w longueur de la fenêtre
     * @param h hauteur de la fenêtre
     * @param x position x de la fenêtre
     * @param y position y de la fenêtre
     */
    public CalquesWindow(int w, int h, int x, int y) {
        this.setTitle("Calques");
        this.setClosable(true);
        this.setResizable(true);
        this.setMaximizable(false);
        this.setIconifiable(true);
        this.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        this.setSize(w, h);
        this.setPreferredSize(new Dimension(w, h));
        this.setLocation(x, y);

        try {
            this.setFrameIcon(new ImageIcon(ImageIO.read(getClass().getResource("/icons/calqueicon.png"))));
        } catch (IOException ex) {
            Logger.getLogger(IWCameraWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Gestion Scrolling                 
        this.setMaximumSize(new Dimension(w, 1000));
        this.setMinimumSize(new Dimension(w, 100));

        JScrollPane jsp = new JScrollPane(calquesPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.setContentPane(jsp);
        jsp.getVerticalScrollBar().setUnitIncrement(25);

        this.setJMenuBar(cmenubar);

        jpmcalques.add(dsupprimer);
        jpmcalques.add(ddupliquer);
        jpmcalques.add(dfusprec);
        jpmcalques.addSeparator();
        jpmcalques.add(dcup);
        jpmcalques.add(dcdown);

        dajouter.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    ajouter.doClick();
                }
            });
            dsupprimer.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    supprimer.doClick();
                }
            });
            ddupliquer.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    dupliquer.doClick();
                }
            });
            dcup.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    cup.doClick();
                }
            });
            dcdown.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    cdown.doClick();
                }
            });
            dfusprec.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    fusprec.doClick();
                }
            });
            
        cmenubar.add(fichier);
        fichier.add(ajouter);
        fichier.add(supprimer);
        fichier.add(dupliquer);

        cmenubar.add(position);
        position.add(cup);
        position.add(cdown);

        cmenubar.add(fusion);
        fusion.add(fusprec);


        fusprec.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (Calques.calques.size() > 1) {

                    Calques.mergeCalques(Calques.current, Calques.current - 1);
                    refreshCalques(Calques.calques);
                }
            }
        });

        cup.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (Calques.calques.size() > 1 && Calques.current != Calques.calques.size() - 1) {

                    Calques.swapPositions(Calques.current, Calques.current + 1);
                    Calques.current = Calques.current + 1;
                    refreshCalques(Calques.calques);
                }
            }
        });

        cdown.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (Calques.calques.size() > 1 && Calques.current != 0) {

                    Calques.swapPositions(Calques.current, Calques.current - 1);
                    Calques.current = Calques.current - 1;
                    refreshCalques(Calques.calques);
                }
            }
        });

        ajouter.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int w = PaintPanel._bufImage.getWidth();
                int h = PaintPanel._bufImage.getHeight();

                BufferedImage tmp = new BufferedImage(w, h, PaintPanel.type);
                Calques.current = Calques.calques.size();
                Calques.add(tmp);
                refreshCalques(Calques.calques);
            }
        });
        supprimer.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                if (Calques.calques.size() > 1) {

                    if (Calques.current == Calques.calques.size() - 1) {
                        Calques.removeAt(Calques.current);
                        Calques.current = Calques.current - 1;
                        refreshCalques(Calques.calques);
                        SToolBar.refresh(calquesPane);
                    } else {
                        Calques.removeAt(Calques.current);
                        refreshCalques(Calques.calques);
                        SToolBar.refresh(calquesPane);
                    }
                }
            }
        });
        dupliquer.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                int w = PaintPanel._bufImage.getWidth();
                int h = PaintPanel._bufImage.getHeight();
                int oldRang = Calques.current;

                BufferedImage tmp = PaintPanel.newCopy(Calques.calques.get(oldRang), BufferedImage.TYPE_INT_ARGB);

                Calques.current = Calques.calques.size();
                Calques.add(tmp, "Copie de " + Calques.noms.get(oldRang), Calques.fusion.get(oldRang));

                refreshCalques(Calques.calques);

            }
        });

        this.setVisible(true);
    }

    /**
     * Rafraîchi la fenêtre des calques (majoritairement appelée lors de la modification de l'image par l'utilisateur)
     * @param tmp Vecteur des calques
     */
    public static void refreshCalques(Vector<BufferedImage> tmp) {
        calquesPane.removeAll();

        int newHeight = 0;

        for (int i = tmp.size() - 1; i >= 0; i--) {
            BufferedImage originalImage = tmp.get(i);
            int type = PaintPanel.type;

            ResizeImage.IMG_WIDTH = 45;
            ResizeImage.IMG_HEIGHT = 45;
            BufferedImage resizeImageHint = ResizeImage.resizeImageWithHint(originalImage, type);

            CalquesPane hp = new CalquesPane(resizeImageHint, i, resizeImageHint.getWidth(), resizeImageHint.getHeight(),
                    Calques.visibility.get(i), Calques.opacity.get(i), Calques.noms.get(i));

            if (Calques.current != i) {
                hp.setBorder(BorderFactory.createEtchedBorder());
                hp.activ = true;
            } else {
                hp.setBorder(BorderFactory.createLoweredBevelBorder());
                hp.activ = false;
            }
            calquesPane.add(hp);
            calquesPane.repaint();

            newHeight += 66;
        }
        calquesPane.setPreferredSize(new Dimension(calquesPane.getWidth(), newHeight));
        calquesPane.setMinimumSize(new Dimension(calquesPane.getWidth(), newHeight));
        calquesPane.revalidate();

        HistoryWindow.refreshHistory(Calques.historiques.get(Calques.current).getImages());


        checkOptions();
    }

    /**
     * Vérifie la possibilité de fusionner 2 Calques et rend l'option inaccessible si besoin 
     */
    static void checkOptions() {
        if (Calques.calques.size() > 1) {
            if (Calques.current == 0) {
                fusprec.setEnabled(false);
            } else if (Calques.visibility.get(Calques.current) == false || Calques.visibility.get(Calques.current - 1) == false) {
                fusprec.setEnabled(false);
            } else {
                fusprec.setEnabled(true);
            }
        } else {
            fusprec.setEnabled(false);
        }
    }

    /**
     * Classe interne qui gère l'affichage calques dans la fenêtre
     */
    static class CalquesPane extends JPanel implements MouseListener {

        JPanel cpad = this;
        CalquesPaneGI hpgi = null;
        int rang = 0;
        JTextField name;
        JButton eye;
        JTextField jtf;
        JSpinner opacspin;
        JComboBox optfusion = new JComboBox();
        public boolean activ = false;
        String[] optfusionlist = new String[]{"Normal", "Multiplier", "Diviser", "Ecran", "Eclaircir", "Assombrir", "Lumière Dure", "Lumière Douce", "Extraction de grain", "Fusion de grain", "Différence", "Addition", "Soustraction", "Noircir Seulement", "Eclaircir Seulement"};

        /**
         * Créer une fenêtre "preview" des calques
         * @param tmp l'image à afficher en miniature
         * @param r rang du calque
         * @param w longueur du composant 
         * @param h hauteur du composant
         * @param visible option de visibilité du calque à affecter par défaut
         * @param opaq option d'opacité du calque à affecter par défaut
         * @param name option du nom du calque à affecter par défaut
         */
        public CalquesPane(BufferedImage tmp, int r, int w, int h, boolean visible, int opaq, String name) {

            


            hpgi = new CalquesPaneGI(tmp, w, h);
            rang = r;

            SpinnerModel model = new SpinnerNumberModel(opaq, 0, 100, 1);
            opacspin = new JSpinner(model);


            opacspin.setPreferredSize(new Dimension(40, 23));
            opacspin.setBorder(BorderFactory.createLineBorder(Color.black));

            jtf = new JTextField(name, 25);
            jtf.setPreferredSize(new Dimension(100, 23));
            jtf.setBorder(BorderFactory.createLineBorder(Color.black));
            jtf.setMargin(new Insets(3, 8, 3, 3));
            jtf.setCaretPosition(0);

            JPanel zone = new JPanel();
            zone.setPreferredSize(new Dimension(90, 40));
            zone.setLayout(new BorderLayout());
            zone.add(jtf, BorderLayout.CENTER);

            if (visible) {
                eye = new JButton(new ImageIcon(getClass().getResource("/misc/visibleeye.png")));
            } else {
                eye = new JButton(new ImageIcon(getClass().getResource("/misc/visibleeye.png")));
                eye.setIcon(null);
            }

            eye.setPreferredSize(new Dimension(16, 16));
            eye.setBackground(Color.LIGHT_GRAY);


            for (int i = 0; i < optfusionlist.length; i++) {
                optfusion.addItem(optfusionlist[i]);
            }
            optfusion.setFont(new Font("Arial", Font.BOLD, 9));
            optfusion.setPreferredSize(new Dimension(100, 19));
            optfusion.setSelectedIndex(Calques.fusion.get(rang));


            this.add(eye, BorderLayout.WEST);
            this.add(hpgi, BorderLayout.CENTER);
            this.add(zone);
            this.add(opacspin, BorderLayout.EAST);
            zone.add(optfusion, BorderLayout.SOUTH);


            optfusion.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    Calques.fusion.setElementAt(optfusion.getSelectedIndex(), rang);
                    CalquesWindow.refreshCalques(Calques.calques);

                }
            });

            jtf.addKeyListener(new KeyListener() {

                public void keyTyped(KeyEvent e) {
                    Calques.noms.setElementAt(((JTextField) e.getSource()).getText(), rang);
                }

                public void keyPressed(KeyEvent e) {
                    Calques.noms.setElementAt(((JTextField) e.getSource()).getText(), rang);
                }

                public void keyReleased(KeyEvent e) {
                    Calques.noms.setElementAt(((JTextField) e.getSource()).getText(), rang);
                }
            });

            opacspin.addChangeListener(new ChangeListener() {

                public void stateChanged(ChangeEvent e) {

                    int val = Integer.parseInt(((JSpinner) e.getSource()).getValue() + "");
                    Calques.opacity.setElementAt(val, rang);
                }
            });

            eye.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    JButton eyeb = ((JButton) e.getSource());

                    if (Calques.visibility.get(rang) == true) {
                        Calques.visibility.setElementAt(false, (rang));
                        eyeb.setIcon(null);
                    } else {
                        Calques.visibility.setElementAt(true, (rang));
                        eyeb.setIcon(new ImageIcon(getClass().getResource("/misc/visibleeye.png")));
                    }
                    SToolBar.refresh(PaintPanel.pad._canvas);
                    checkOptions();
                }
            });

            this.setBorder(BorderFactory.createRaisedBevelBorder());

            this.addMouseListener(this);
        }

        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;

            Color colors[] = null;
            if (!activ) {
                colors = new Color[]{Color.decode("0xc5cfdf"), Color.decode("0x133759")};
            } else {
                colors = new Color[]{Color.decode("0xdce5f2"), Color.decode("0xc5cfdf")};
            }
            LinearGradientPaint p = new LinearGradientPaint(new Point2D.Float(0, 0), new Point2D.Float(0, this.getHeight()), new float[]{0.0F, 1.0F}, colors);
            g2.setPaint(p);
            g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                jpmcalques.show(cpad, e.getX(), e.getY());
            }

            Calques.current = ((CalquesPane) e.getSource()).rang;
            refreshCalques(Calques.calques);

        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
    }

    /**
     * Classe interne qui gère le panneau d'affichage de la miniature
     */
    static class CalquesPaneGI extends JPanel {

        BufferedImage preview = null;

        /**
         * Création du panneau d'affichage de la miniature
         * @param tmp Image à afficher en miniature
         * @param w longueur du composant
         * @param h hauteur du composant
         */
        public CalquesPaneGI(BufferedImage tmp, int w, int h) {
            preview = tmp;
            this.setPreferredSize(new Dimension(w, h));
            this.setBorder(BorderFactory.createLineBorder(Color.black));
        }

        public void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g;
            // Photoshop font style
            //*/
            BufferedImage img = null;
            try {
                img = ImageIO.read(getClass().getResource("/misc/transtexture.png"));
            } catch (IOException ex) {
            }

            TexturePaint tp = new TexturePaint(img, new Rectangle(0, 0, 20, 20));
            g2.setPaint(tp);
            g2.fillRect(0, 0, this.getWidth(), this.getHeight());
            //*/
            g.drawImage(preview, 0, 0, null);
        }
    }
}
