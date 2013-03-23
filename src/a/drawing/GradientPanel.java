package a.drawing;


import a.window.GradientWindow;
import a.util.ImageFileFilter;
import a.util.CustomComboBoxDemo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ListCellRenderer;

/**
 * Classe qui gère le panneau d'affichage des dégradés
 * @author Jessym
 */
public class GradientPanel extends JPanel {

    JPanel pan = this;
    Vector<ColorPane> liste = new Vector();
    JComboBox gradientcb = null;
    CustomComboBoxDemo ccb = new CustomComboBoxDemo();
    PreviewPane previewG;
    boolean firstTime = true;
    JButton add_bt;
    static MultipleGradientPaint actualGradient = null;
    static String typeg = "linear";
    SGradientList gradientList = new SGradientList();
    JList samplesgradientjlist;
    JButton ajouted = new JButton("Ajouter ce dégradé");
    JButton supprimed = new JButton("Supprimer la séléction");

    /**
     * Constructeur du panneau d'affichage des dégradés
     */
    public GradientPanel() {
        init();
    }

    /**
     * Initialise le panneau d'affichage des dégradés
     */
    public void init() {
        this.setBackground(Color.white);
        this.setPreferredSize(new Dimension(210, 600));
        this.setSize(new Dimension(210, 600));


        // On crée les composants UNE FOIS et on les initialisent par DEFAUT
        if (firstTime) {

            ccb.setOpaque(true); //content panes must be opaque
            ccb.getCombo().setMaximumSize(new Dimension(130, 55));
            gradientcb = ccb.getCombo();

            add_bt = new JButton("+");
            add_bt.addActionListener(new AjouteCouleur());
            add_bt.setEnabled(true);

            previewG = new PreviewPane();

            liste.add(new ColorPane(Color.black));
            liste.add(new ColorPane(Color.red));

            gradientList.setPreferredSize(new Dimension(180, 150));
            ajouted.addActionListener(new AjouteDegrade());
            supprimed.addActionListener(new SupprimeDegrade());

        }

        // On les ajoute lorsque une couleur a été changée ou le type de dégradé a été modifié (linear etc...)
        this.add(ccb.getCombo());
        this.add(add_bt);
        this.add(previewG);
        this.add(ajouted);
        this.add(supprimed);
        this.add(gradientList);



        // Gestion des différents gradient
        gradientcb.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                switch (gradientcb.getSelectedIndex()) {
                    case 0:
                        liste = new Vector();
                        liste.add(new ColorPane(Color.black));
                        liste.add(new ColorPane(Color.red));
                        add_bt.setEnabled(true);
                        typeg = "linear";
                        break;
                    case 1:
                        liste = new Vector();
                        liste.add(new ColorPane(Color.black));
                        liste.add(new ColorPane(Color.red));
                        liste.add(new ColorPane(Color.black));
                        add_bt.setEnabled(false);
                        typeg = "bilinear";
                        break;
                    case 2:
                        liste = new Vector();
                        liste.add(new ColorPane(Color.red));
                        liste.add(new ColorPane(Color.black));
                        add_bt.setEnabled(true);
                        typeg = "radial";
                        break;
                    case 3:
                        liste = new Vector();
                        liste.add(new ColorPane(Color.red));
                        liste.add(new ColorPane(Color.black));
                        add_bt.setEnabled(true);
                        typeg = "circles";
                        break;
                    case 4:
                        liste = new Vector();
                        liste.add(new ColorPane(Color.red));
                        liste.add(new ColorPane(Color.black));
                        add_bt.setEnabled(true);
                        typeg = "relinear";
                        break;
                    case 5:
                        liste = new Vector();
                        liste.add(new ColorPane(Color.red));
                        liste.add(new ColorPane(Color.black));
                        add_bt.setEnabled(true);
                        typeg = "reradial";
                        break;
                }

                pan.removeAll();
                ((GradientPanel) pan).init();
                pan.revalidate();
            }
        });

        // Color composition for Gradient
        for (int i = 0; i < liste.size(); i++) {
            this.add(liste.get(i));
            liste.get(i).reinit(i);

            if (i < 2) {
                liste.get(i).delete.setEnabled(false);
            }
        }



        firstTime = false;
    }

    /**
     * Classe interne écouteur du bouton AJOUTER qui recrée le Vecteur de couleurs pour un nouveau dégradé
     */
    public class AjouteCouleur implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (liste.size() < 6) {
                pan.removeAll();
                liste.add(new ColorPane(Color.black));
                ((GradientPanel) pan).init();
                pan.revalidate();
            } else {
                JOptionPane jop = new JOptionPane();
                jop.showMessageDialog(null, "6 Maximum");
            }
        }
    }

    /**
     *  Classe interne qui gérent le panneau d'affichage des couleurs ajoutées à un dégradé via le bouton "ajouter"
     */
    class ColorPane extends JPanel {

        JPanel cg = this;
        JButton delete = new JButton("-");
        ColorGI colorp;
        int pos;

        /**
         * Construit un panneau de couleur avec la couleur passée en paramètre
         * @param c Cette couleur sera affiché dans le panneau
         */
        public ColorPane(Color c) {
            this.setBackground(Color.white);
            this.setSize(160, 50);
            this.setPreferredSize(new Dimension(180, 30));
            colorp = new ColorGI(c);
            this.add(colorp);
            this.add(delete);

            delete.setPreferredSize(new Dimension(40, 20));
            delete.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {

                    pan.removeAll();
                    liste.remove(pos);
                    ((GradientPanel) pan).init();
                    pan.revalidate();

                    JViewport jv = GradientWindow.jsp.getViewport();
                    jv.setViewPosition(new Point(0, 0));
                }
            });
        }

        public void reinit(int i) {
            pos = i;
        }
    }

    /**
     * Classe interne qui gère le panneau d'affiche d'une couleur ainsi que du choix de la couleur lors du clic
     */
    class ColorGI extends JPanel {

        JPanel cg = this;

        public ColorGI(Color c) {
            this.setBackground(c);
            this.setBorder(BorderFactory.createTitledBorder(""));
            this.setSize(100, 20);
            this.setPreferredSize(new Dimension(100, 20));
            this.addMouseListener(new MouseListener() {

                public void mouseClicked(MouseEvent arg0) {
                }

                public void mouseEntered(MouseEvent arg0) {
                }

                public void mouseExited(MouseEvent arg0) {
                }

                public void mouseReleased(MouseEvent arg0) {
                }

                public void mousePressed(MouseEvent arg0) {
                    Color newColor = JColorChooser.showDialog(null, "Choix de la couleur", Color.black);
                    cg.setBackground(newColor);
                    previewG.prev.repaint();

                    JViewport jv = GradientWindow.jsp.getViewport();
                    jv.setViewPosition(new Point(0, 0));
                }
            });

        }
    }

    /**
     *  Classe interne qui se charge de créer un Panneau d'affichage afin d'afficher le dégradé courant
     */
    class PreviewPane extends JPanel {

        PreviewGI prev = new PreviewGI();

        /**
         * Constructeur PreviewPane
         */
        public PreviewPane() {
            this.setBackground(Color.white);
            this.setBorder(BorderFactory.createTitledBorder("Dégradé"));
            this.setSize(160, 80);
            this.add(prev);
            prev.setSize(100, 30);
            this.setPreferredSize(new Dimension(160, 80));
        }
    }

    /**
     * Classe interne qui se charge d'afficher le dégradé courant
     */
    class PreviewGI extends JPanel {

        PreviewGI pad = this;

        /**
         * Constructeur PreviewGI
         */
        public PreviewGI() {
            this.setBackground(Color.black);
            this.setSize(100, 30);
            this.setPreferredSize(new Dimension(100, 30));
        }

        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            Point2D start = new Point2D.Float(0, 0);
            Point2D end = new Point2D.Float(this.getWidth(), this.getHeight());
            Point2D center = new Point2D.Float(this.getWidth() / 2, this.getHeight() / 2);


            float radius = 25;
            float dist[] = new float[liste.size()];
            Color colors[] = new Color[liste.size()];
            for (int i = 0; i < liste.size(); i++) {

                dist[i] = i * 1.0F / (liste.size() - 1);
                colors[i] = liste.get(i).colorp.getBackground();
            }

            MultipleGradientPaint p = null;

            if (typeg.equals("linear")) {
                p = new LinearGradientPaint(start, end, dist, colors);
            } else if (typeg.equals("bilinear")) {
                p = new LinearGradientPaint(start, end, dist, colors, CycleMethod.REFLECT);
            } else if (typeg.equals("radial")) {
                p = new RadialGradientPaint(center, radius, dist, colors);
            } else if (typeg.equals("circles")) {
                p = new RadialGradientPaint(center, radius, dist, colors, CycleMethod.REFLECT);
            } else if (typeg.equals("relinear")) {
                p = new LinearGradientPaint(start, end, dist, colors, CycleMethod.REPEAT);
            } else if (typeg.equals("reradial")) {
                p = new RadialGradientPaint(center, radius, dist, colors, CycleMethod.REPEAT);
            }


            actualGradient = p;
            g2.setPaint(p);
            g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
    }

    /**
     * Classe interne qui se charge d'afficher un Panneau qui contiendra la liste des échantillons de dégradé
     */
    class PreviewGIListPane extends JPanel {

        PreviewGIList pgi;

        /**
         * Constructeur PreviewGIListPane
         * @param tabColor 
         */
        public PreviewGIListPane(Color[] tabColor) {
            pgi = new PreviewGIList(tabColor);
            this.setBackground(Color.white);
            this.setPreferredSize(new Dimension(145, 25));
            this.add(pgi);

        }
    }

    /**
     * Classe interne qui se charge de peindre le dégradé dans la liste
     */
    class PreviewGIList extends JPanel {

        PreviewGIList pad = this;
        Vector<Color> listepreview = new Vector();
        MultipleGradientPaint p = null;
        JPanel samplePane = new JPanel();

        /**
         * Constructeur previewGIList
         * @param tabColor  Tableau de couleur qui composera le degradé
         */
        public PreviewGIList(Color[] tabColor) {
            this.setBackground(Color.black);
            this.setSize(165, 20);
            this.setPreferredSize(new Dimension(165, 20));

            for (int i = 0; i < tabColor.length; i++) {
                listepreview.add(tabColor[i]);
            }
            this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

            samplePane.add(this);
        }

        /**
         * Ajoute une couleur à un dégradé de la liste
         * @param c couleur à ajouter
         */
        public void add(Color c) {
            listepreview.add(c);
        }

        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            Point2D start = new Point2D.Float(0, 0);
            Point2D end = new Point2D.Float(this.getWidth(), this.getHeight());
            Point2D center = new Point2D.Float(this.getWidth() / 2, this.getHeight() / 2);


            float radius = 25;
            float dist[] = new float[listepreview.size()];
            Color colors[] = new Color[listepreview.size()];
            for (int i = 0; i < listepreview.size(); i++) {

                dist[i] = i * 1.0F / (listepreview.size() - 1);
                colors[i] = listepreview.get(i);
            }


            if (typeg.equals("linear")) {
                p = new LinearGradientPaint(start, end, dist, colors);
            } else if (typeg.equals("bilinear")) {
                p = new LinearGradientPaint(start, end, dist, colors, CycleMethod.REFLECT);
            } else if (typeg.equals("radial")) {
                p = new RadialGradientPaint(center, radius, dist, colors);
            } else if (typeg.equals("circles")) {
                p = new RadialGradientPaint(center, radius, dist, colors, CycleMethod.REFLECT);
            } else if (typeg.equals("relinear")) {
                p = new LinearGradientPaint(start, end, dist, colors, CycleMethod.REPEAT);
            } else if (typeg.equals("reradial")) {
                p = new RadialGradientPaint(center, radius, dist, colors, CycleMethod.REPEAT);
            }


            g2.setPaint(p);
            g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
    }

    /**
     * Classe interne qui gère la liste de degradé enregistrable
     */
    public class SGradientList extends JPanel implements MouseListener, Serializable {

        public JScrollPane scrollPane;
        public Vector<PreviewGIListPane> listeItems = new Vector();
        public int rangSelected = 0;

        /**
         * Constructeur SGradientList : ajoute des dégradés par défauts 
         */
        public SGradientList() {
            super(new BorderLayout());
            listeItems.addElement(new PreviewGIListPane(new Color[]{Color.black, Color.white}));
            listeItems.addElement(new PreviewGIListPane(new Color[]{Color.white, Color.black}));
            listeItems.addElement(new PreviewGIListPane(new Color[]{Color.magenta, Color.blue, Color.cyan, Color.green, Color.yellow, Color.red}));
            listeItems.addElement(new PreviewGIListPane(new Color[]{Color.blue, Color.red, Color.yellow}));
            listeItems.addElement(new PreviewGIListPane(new Color[]{Color.green, Color.blue}));
            listeItems.addElement(new PreviewGIListPane(new Color[]{Color.magenta, Color.white}));
            listeItems.addElement(new PreviewGIListPane(new Color[]{Color.green, Color.black}));
            listeItems.addElement(new PreviewGIListPane(new Color[]{Color.blue, Color.black}));
            listeItems.addElement(new PreviewGIListPane(new Color[]{Color.yellow, Color.black}));
            listeItems.addElement(new PreviewGIListPane(new Color[]{Color.gray, Color.blue, Color.red, Color.PINK}));
            listeItems.addElement(new PreviewGIListPane(new Color[]{Color.black, Color.red, Color.yellow, Color.red, Color.black}));
            listeItems.addElement(new PreviewGIListPane(new Color[]{Color.red, Color.green, Color.blue}));
            listeItems.addElement(new PreviewGIListPane(new Color[]{Color.DARK_GRAY, Color.cyan}));

            samplesgradientjlist = new JList(listeItems);
            samplesgradientjlist.setSelectedIndex(0);


            JListRenderer renderer = new JListRenderer();
            samplesgradientjlist.setCellRenderer(renderer);
            samplesgradientjlist.addMouseListener(this);


            scrollPane = new JScrollPane(samplesgradientjlist);

            add(scrollPane, BorderLayout.CENTER);
        }

        public void init() {
            samplesgradientjlist = new JList(listeItems);
            samplesgradientjlist.setSelectedIndex(0);

            JListRenderer renderer = new JListRenderer();
            samplesgradientjlist.setCellRenderer(renderer);
            samplesgradientjlist.addMouseListener(this);


            scrollPane = new JScrollPane(samplesgradientjlist);

            add(scrollPane, BorderLayout.CENTER);
        }

        /**
         * Ajoute un nouveau dégradé à la liste
         * @param pgi panneau d'affichage du nouveau dégradé
         */
        public void add(PreviewGIListPane pgi) {
            listeItems.addElement(pgi);
        }

        /**
         * Supprime un dégradé de la liste
         * @param i rang du panneau d'affiche du dégradé à supprimer
         */
        public void delete(int i) {
            listeItems.removeElementAt(i);
        }

        public void mouseClicked(MouseEvent arg0) {
        }

        public void mouseEntered(MouseEvent arg0) {
        }

        public void mouseExited(MouseEvent arg0) {
        }

        public void mouseReleased(MouseEvent e) {

            JViewport jv = GradientWindow.jsp.getViewport();
            jv.setViewPosition(new Point(0, 0));

        }

        public void mousePressed(MouseEvent e) {
            pan.removeAll();
            liste = new Vector();
            int rang = samplesgradientjlist.getSelectedIndex();
            for (int i = 0; i < (listeItems.get(rang)).pgi.listepreview.size(); i++) {
                liste.add(new ColorPane((listeItems.get(rang)).pgi.listepreview.get(i)));

            }
            ((GradientPanel) pan).init();
            pan.revalidate();
        }
    }

    /**
     * Classe interne utilitaire qui permet de gérer une JList personnalisée
     */
   public  class JListRenderer implements ListCellRenderer {

        public Component component;

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            component = (Component) value;
            if (isSelected) {
            }
            return component;
        }
    }

    /**
     * Classe interne qui gère l'action du clic sur le bouton Ajouter
     */
    public class AjouteDegrade implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            gradientList.removeAll();

            Color[] tmp = new Color[liste.size()];

            for (int i = 0; i < liste.size(); i++) {
                tmp[i] = liste.get(i).colorp.getBackground();
            }

            gradientList.add(new PreviewGIListPane(tmp));
            gradientList.init();
            gradientList.revalidate();

        }
    }

    /**
     * Classe interne qui gère l'action du clic sur le bouton Supprimer
     */
    public class SupprimeDegrade implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (gradientList.listeItems.size() > 0) {
                gradientList.removeAll();
                int rang = samplesgradientjlist.getSelectedIndex();
                gradientList.delete(rang);
                gradientList.init();
                gradientList.revalidate();

            }
        }
    }

    /**
     * Classe interne qui gère l'action du clic sur l'objet Charger une liste du menu 
     */
    public class ChargeDegrade implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            fc.addChoosableFileFilter(new ImageFileFilter(".iwg", "Fichier ImageWork Gradient List"));

            if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = fc.getSelectedFile();
                    if (fc.getFileFilter().accept(file)) {
                        gradientList.removeAll();
                        gradientList.listeItems.removeAllElements();
                        Vector v = read(file);
                        int n = v.size() - 1, n2 = (Integer) v.get(n);
                        for (int i = 0; i < n2; i++) {

                            Color[] tmp = new Color[((Vector) v.get(i)).size()];

                            for (int j = 0; j < ((Vector) v.get(i)).size(); j++) {
                                tmp[j] = (Color) ((Vector) v.get(i)).get(j);
                            }

                            gradientList.listeItems.addElement(new PreviewGIListPane(tmp));

                        }

                        gradientList.init();
                        gradientList.revalidate();
                    } else {
                        JOptionPane alert = new JOptionPane();
                        alert.showMessageDialog(null, "Votre chargement a échoué !", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception es) {
                    es.printStackTrace();
                }
            }
        }
    }

    /**
     * Classe interne qui gère l'action du clic sur l'objet Sauvegarder une liste du menu 
     */
    public class SauvegardeDegrade implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            fc.removeChoosableFileFilter(fc.getAcceptAllFileFilter());
            fc.addChoosableFileFilter(new ImageFileFilter(".iwg", "Fichier ImageWork Gradient List"));

            if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                try {
                    String name = (fc.getSelectedFile().getAbsolutePath() + fc.getFileFilter().getDescription());

                    Vector tmp = new Vector();

                    for (int i = 0; i < gradientList.listeItems.size(); i++) {
                        tmp.add(gradientList.listeItems.get(i).pgi.listepreview);
                    }

                    write(new File(name), tmp);
                } catch (Exception es) {
                    es.printStackTrace();
                }
            }
        }
    }

    /**
     * Enregistre dans un fichier la liste des dégradés 
     * @param file Fichier dans lequel on enregistre la liste
     * @param o Liste des dégradés
     */
    public void write(File file, Object o) {
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            oos.writeObject(o);
            oos.writeInt(((Vector) o).size());
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Récupère dans un fichier la liste des dégradés 
     * @param file Fichier dans lequel on récupètre la liste
     */
    public Vector read(File file) {
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
            Vector o = (Vector) ois.readObject();
            int nb = ois.readInt();
            o.add(nb);
            ois.close();

            return o;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
