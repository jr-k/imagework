package a.drawing;

import a.util.SPanel;
import a.util.SeparatorJPanel;
import a.util.Shape;
import a.window.PaintWindow;
import a.window.PaintingOptionWindow;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

/**
 * Classe qui gère la barre d'outils 
 * @author Jessym
 */
public class SToolBar extends JToolBar {

    static JPanel toolbar;
    static SPanel shapePanel;
    static JButton newfile;
    static JButton openfile;
    static JButton openproject;
    static JButton save;
    static JButton imprime;
    static JButton annuler;
    static JButton camsm;
    static JButton envoicreat;
    static JButton filtreprec;
    static JButton cursorButton;
    static JButton resizeButton;
    static JButton crayonButton;
    static JButton circleButton;
    static JButton rectangleButton;
    static JButton lineButton;
    static JButton curveButton;
    static JButton frectangleButton;
    static JButton fcircleButton;
    static JButton gradientButton;
    static JButton gommeButton;
    static JButton potButton;
    static JButton selectButton;
    static JButton zoomButton;
    static JButton polygoneButton;
    static JButton texteButton;
    static JButton pipetteButton;
    static JButton gradientok;
    static JButton ratioresize;
    static Cursor curs;
    Toolkit tk = Toolkit.getDefaultToolkit();
    Dimension dimensionCurseur;
    Image imgCursorA;
    Image imgcurs;
    public Color ACTIV_COLOR = Color.black;
    public int current = 0;
    JButton[] listeButton;
    static PaintWindow win;
    public static PaintPanel _canvas;
    public static PaintWindow tbwin;

    /**
     * Constructeur SToolBar
     * @param pw Fenêtre qui englobe la barre d'outil
     * @param can Espace de dessin associé
     */
    public SToolBar(PaintWindow pw, PaintPanel can) {
        
        if (this.isWindows()){
        dimensionCurseur = tk.getBestCursorSize(18, 18);
        imgCursorA = (new ImageIcon(getClass().getResource("/imgcurs/crayon.png"))).getImage();
        imgcurs = imgCursorA.getScaledInstance(dimensionCurseur.width, dimensionCurseur.height, Image.SCALE_DEFAULT);
        curs = tk.createCustomCursor(imgcurs, new Point(1, 30), "crayon");
        }
        

        this.setSize(new Dimension(1000, 63));
        this.setPreferredSize(new Dimension(1000, 63));
       
        tbwin = pw;
        _canvas = can;
        init();
    }

    /**
     * Rend un bouton plus foncé afin de définir l'usage d'un outil
     * @param e Evénement dû au clic sur un bouton 
     */
    public void setDarker(ActionEvent e) {
        JButton source = (JButton) e.getSource();
        for (int i = 0; i < listeButton.length; i++) {
            if ((source) == listeButton[i]) {
                current = i;
            } else {
                listeButton[i].setBackground(null);
            }
        }
        ((JButton) e.getSource()).setBackground(ACTIV_COLOR);
        if ((source) != cursorButton && PaintPanel.selectPane != null) {
            _canvas.dessinerSelection(true);
        }

        if (source != resizeButton) {
            PaintPanel.tracker.setVisible(false);

        }


    }

    /**
     * Intègre dans la barre d'outils les icônes liés à la barre de menu
     */
    public void SMenuBarIcons() {
        newfile = new JButton(new ImageIcon(getClass().getResource("/filetoolbar/newfile.png")));
        newfile.setPreferredSize(new Dimension(30, 30));
        newfile.setToolTipText("Nouveau fichier");
        newfile.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SMenuBar.nouveau.doClick();
            }
        });

        openfile = new JButton(new ImageIcon(getClass().getResource("/filetoolbar/openfile.png")));
        openfile.setPreferredSize(new Dimension(30, 30));
        openfile.setToolTipText("Ouvrir un fichier");
        openfile.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SMenuBar.ouvrir.doClick();
            }
        });

        openproject = new JButton(new ImageIcon(getClass().getResource("/filetoolbar/openproject.png")));
        openproject.setPreferredSize(new Dimension(30, 30));
        openproject.setToolTipText("Ouvrir un projet");
        openproject.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SMenuBar.pouvrir.doClick();
            }
        });

        save = new JButton(new ImageIcon(getClass().getResource("/filetoolbar/save.png")));
        save.setPreferredSize(new Dimension(30, 30));
        save.setToolTipText("Enregistrer sous");
        save.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SMenuBar.enregistrersous.doClick();
            }
        });

        imprime = new JButton(new ImageIcon(getClass().getResource("/filetoolbar/imprime.png")));
        imprime.setPreferredSize(new Dimension(30, 30));
        imprime.setToolTipText("Imprimer");
        imprime.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SMenuBar.imprimer.doClick();
            }
        });

        annuler = new JButton(new ImageIcon(getClass().getResource("/filetoolbar/annuler.png")));
        annuler.setPreferredSize(new Dimension(30, 30));
        annuler.setToolTipText("Annuler");
        annuler.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SMenuBar.annuler.doClick();
            }
        });

        camsm = new JButton(new ImageIcon(getClass().getResource("/tools/camtool.png")));
        camsm.setPreferredSize(new Dimension(30, 30));
        camsm.setToolTipText("IW Caméra");
        camsm.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SMenuBar.iwcam.doClick();
            }
        });

        envoicreat = new JButton(new ImageIcon(getClass().getResource("/filetoolbar/envoicreat.png")));
        envoicreat.setPreferredSize(new Dimension(30, 30));
        envoicreat.setToolTipText("Envoyez votre création !");
        envoicreat.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SMenuBar.envoicrea.doClick();
            }
        });

        filtreprec = new JButton(new ImageIcon(getClass().getResource("/filetoolbar/filtreprect.png")));
        filtreprec.setPreferredSize(new Dimension(30, 30));
        filtreprec.setToolTipText("Filtre précédent");
        filtreprec.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SMenuBar.filtreprec.doClick();
            }
        });
    }

    /**
     * Initialise la barre d'outils
     */
    public void init() {

        SMenuBarIcons();

        cursorButton = new JButton(new ImageIcon(getClass().getResource("/tools/tcurseur.png")));
        cursorButton.setPreferredSize(new Dimension(30, 30));
        cursorButton.setToolTipText("Curseur");
        cursorButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                PaintingOptionWindow.optionTabbedPane.setSelectedIndex(1);

                if (PaintPanel.gradientpaint) {
                    SToolBar.gradientok.doClick();
                }

                     curs = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);

                _canvas.setShape(Shape.CURSOR);
                if (!SMenuBar.optionsf.isSelected()) {
                    SMenuBar.optionsf.doClick();
                }
                setDarker(e);

            }
        });


        crayonButton = new JButton(new ImageIcon(getClass().getResource("/tools/tcrayon.png")));
        crayonButton.setPreferredSize(new Dimension(30, 30));
        crayonButton.setToolTipText("Crayon");
        crayonButton.setBackground(Color.black);
        crayonButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                
                 if (isWindows()){
                dimensionCurseur = tk.getBestCursorSize(18, 18);
                imgCursorA = (new ImageIcon(getClass().getResource("/imgcurs/crayon.png"))).getImage();
                imgcurs = imgCursorA.getScaledInstance(dimensionCurseur.width, dimensionCurseur.height, Image.SCALE_DEFAULT);
                curs = tk.createCustomCursor(imgcurs, new Point(1, 30), "crayon");
                }
                 else {
                     curs = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
                 }
                     
                PaintingOptionWindow.optionTabbedPane.setSelectedIndex(1);
                _canvas.setShape(Shape.CRAYON);
                if (!SMenuBar.optionsf.isSelected()) {
                    SMenuBar.optionsf.doClick();

                }
                setDarker(e);

            }
        });

        resizeButton = new JButton(new ImageIcon(getClass().getResource("/tools/tresize.png")));
        resizeButton.setPreferredSize(new Dimension(30, 30));
        resizeButton.setToolTipText("Redimensionnement");
        resizeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {


                PaintingOptionWindow.optionTabbedPane.setSelectedIndex(1);
                curs = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
                _canvas.setShape(Shape.RESIZER);
                PaintPanel.tracker.setVisible(true);
                setDarker(e);
            }
        });

        circleButton = new JButton(new ImageIcon(getClass().getResource("/tools/toval.png")));
        circleButton.setPreferredSize(new Dimension(30, 30));
        circleButton.setToolTipText("Ellipse");
        circleButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                curs = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
                PaintingOptionWindow.optionTabbedPane.setSelectedIndex(1);

                _canvas.setShape(Shape.OVAL);
                if (!SMenuBar.optionsf.isSelected()) {
                    SMenuBar.optionsf.doClick();
                }
                setDarker(e);
            }
        });

        rectangleButton = new JButton(new ImageIcon(getClass().getResource("/tools/trect.png")));
        rectangleButton.setPreferredSize(new Dimension(30, 30));
        rectangleButton.setToolTipText("Rectangle");
        rectangleButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                curs = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
                PaintingOptionWindow.optionTabbedPane.setSelectedIndex(1);
                _canvas.setShape(Shape.RECTANGLE);
                if (!SMenuBar.optionsf.isSelected()) {
                    SMenuBar.optionsf.doClick();
                }
                setDarker(e);
            }
        });

        lineButton = new JButton(new ImageIcon(getClass().getResource("/tools/tline.png")));
        lineButton.setPreferredSize(new Dimension(30, 30));
        lineButton.setToolTipText("Ligne");
        lineButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                curs = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
                PaintingOptionWindow.optionTabbedPane.setSelectedIndex(1);
                _canvas.setShape(Shape.LINE);
                if (!SMenuBar.optionsf.isSelected()) {
                    SMenuBar.optionsf.doClick();
                }
                setDarker(e);
            }
        });

        curveButton = new JButton(new ImageIcon(getClass().getResource("/tools/tcurve.png")));
        curveButton.setPreferredSize(new Dimension(30, 30));
        curveButton.setToolTipText("Arc");
        curveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                curs = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
                PaintingOptionWindow.optionTabbedPane.setSelectedIndex(2);
                _canvas.setShape(Shape.CURVE);
                if (!SMenuBar.optionsf.isSelected()) {
                    SMenuBar.optionsf.doClick();
                }
                setDarker(e);
            }
        });



        frectangleButton = new JButton(new ImageIcon(getClass().getResource("/tools/tfillrect.png")));
        frectangleButton.setPreferredSize(new Dimension(30, 30));
        frectangleButton.setToolTipText("Rectangle plein");
        frectangleButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                curs = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
                PaintingOptionWindow.optionTabbedPane.setSelectedIndex(1);
                _canvas.setShape(Shape.FRECTANGLE);
                if (!SMenuBar.optionsf.isSelected()) {
                    SMenuBar.optionsf.doClick();
                }
                setDarker(e);
            }
        });

        fcircleButton = new JButton(new ImageIcon(getClass().getResource("/tools/tfilloval.png")));
        fcircleButton.setPreferredSize(new Dimension(30, 30));
        fcircleButton.setToolTipText("Ellipse pleine");
        fcircleButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                curs = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
                PaintingOptionWindow.optionTabbedPane.setSelectedIndex(1);
                _canvas.setShape(Shape.FOVAL);
                if (!SMenuBar.optionsf.isSelected()) {
                    SMenuBar.optionsf.doClick();
                }
                setDarker(e);
            }
        });

        gradientButton = new JButton(new ImageIcon(getClass().getResource("/tools/tgradient.png")));
        gradientButton.setPreferredSize(new Dimension(30, 30));
        gradientButton.setToolTipText("Dégradé");
        gradientButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                curs = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
                PaintingOptionWindow.optionTabbedPane.setSelectedIndex(1);
                _canvas.setShape(Shape.GRADIENT);
                if (!SMenuBar.gradient.isSelected()) {
                    SMenuBar.gradient.doClick();
                }
                setDarker(e);
            }
        });

        gommeButton = new JButton(new ImageIcon(getClass().getResource("/tools/tgomme.png")));
        gommeButton.setPreferredSize(new Dimension(30, 30));
        gommeButton.setToolTipText("Gomme");
        gommeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                if (isWindows()){
                dimensionCurseur = tk.getBestCursorSize(18, 18);
                imgCursorA = (new ImageIcon(getClass().getResource("/imgcurs/gomme2.png"))).getImage();
                imgcurs = imgCursorA.getScaledInstance(dimensionCurseur.width, dimensionCurseur.height, Image.SCALE_DEFAULT);
                curs = tk.createCustomCursor(imgcurs, new Point(1, 20), "gomme");

                }
                 else {
                     curs = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
                 }
                
                PaintingOptionWindow.optionTabbedPane.setSelectedIndex(1);
                _canvas.setShape(Shape.GOMME);
                setDarker(e);
            }
        });

        potButton = new JButton(new ImageIcon(getClass().getResource("/tools/tpot.png")));
        potButton.setPreferredSize(new Dimension(30, 30));
        potButton.setToolTipText("Pot de peinture");
        potButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                
                 if (isWindows()){
                dimensionCurseur = tk.getBestCursorSize(18, 18);
                imgCursorA = (new ImageIcon(getClass().getResource("/imgcurs/pot.png"))).getImage();
                imgcurs = imgCursorA.getScaledInstance(dimensionCurseur.width, dimensionCurseur.height, Image.SCALE_DEFAULT);
                curs = tk.createCustomCursor(imgcurs, new Point(1, 20), "pot");
                }
                 else {
                     curs = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
                 }
                
                if (PaintPanel.gradientpaint) {
                    SToolBar.gradientok.doClick();
                }
                PaintingOptionWindow.optionTabbedPane.setSelectedIndex(1);
                _canvas.setShape(Shape.POT);
                setDarker(e);
            }
        });


        selectButton = new JButton(new ImageIcon(getClass().getResource("/tools/tselect.png")));
        selectButton.setPreferredSize(new Dimension(30, 30));
        selectButton.setToolTipText("Sélection");
        selectButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                PaintingOptionWindow.optionTabbedPane.setSelectedIndex(1);
                curs = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
                _canvas.setShape(Shape.SELECT);
                setDarker(e);
            }
        });


        zoomButton = new JButton(new ImageIcon(getClass().getResource("/tools/tzoom.png")));
        zoomButton.setPreferredSize(new Dimension(30, 30));
        zoomButton.setToolTipText("Utilisez la molette pour zoomer");
        zoomButton.setBackground(ACTIV_COLOR);
        zoomButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                if (PaintPanel._shape != Shape.RESIZER) {
                    if (PaintPanel.zoomEnabled) {
                        PaintPanel.zoomEnabled = false;
                        zoomButton.setBackground(null);
                    } else {
                        PaintPanel.zoomEnabled = true;
                        zoomButton.setBackground(ACTIV_COLOR);
                    }
                }
            }
        });



        polygoneButton = new JButton(new ImageIcon(getClass().getResource("/tools/tpoly.png")));
        polygoneButton.setPreferredSize(new Dimension(30, 30));
        polygoneButton.setToolTipText("Polygone");
        polygoneButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                _canvas.setShape(Shape.POLYGONE);
                setDarker(e);
            }
        });

        texteButton = new JButton(new ImageIcon(getClass().getResource("/tools/ttexte.png")));
        texteButton.setPreferredSize(new Dimension(30, 30));
        texteButton.setToolTipText("Texte");
        texteButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                if (PaintPanel.gradientpaint) {
                    SToolBar.gradientok.doClick();
                }

                curs = Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR);
                PaintingOptionWindow.optionTabbedPane.setSelectedIndex(0);
                _canvas.zoom = 1;
                _canvas.rescaleImage();

                _canvas.setShape(Shape.TEXTE);
                setDarker(e);
            }
        });

        pipetteButton = new JButton(new ImageIcon(getClass().getResource("/tools/tpipette.png")));
        pipetteButton.setPreferredSize(new Dimension(30, 30));
        pipetteButton.setToolTipText("Pipette de couleur");
        pipetteButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                if (isWindows()){
                dimensionCurseur = tk.getBestCursorSize(18, 18);
                imgCursorA = (new ImageIcon(getClass().getResource("/imgcurs/pipette.png"))).getImage();
                imgcurs = imgCursorA.getScaledInstance(dimensionCurseur.width, dimensionCurseur.height, Image.SCALE_DEFAULT);
                curs = tk.createCustomCursor(imgcurs, new Point(1, 20), "pipette");

                }
                 else {
                     curs = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
                 }
                
                _canvas.setShape(Shape.PIPETTE);
                setDarker(e);
            }
        });

        gradientok = new JButton(new ImageIcon(getClass().getResource("/tools/gradientok.png")));
        gradientok.setPreferredSize(new Dimension(30, 30));
        gradientok.setToolTipText("Activer le dégradé pour les dessins");
        gradientok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                if (PaintPanel._shape != Shape.POT && PaintPanel._shape != Shape.CURSOR && PaintPanel._shape != Shape.TEXTE) {
                    if (PaintPanel.gradientpaint) {
                        PaintPanel.gradientpaint = false;
                        gradientok.setBackground(null);
                    } else {
                        PaintPanel.gradientpaint = true;
                        gradientok.setBackground(ACTIV_COLOR);
                        SMenuBar.gradient.doClick();
                    }
                }

            }
        });

        ratioresize = new JButton(new ImageIcon(getClass().getResource("/tools/tratioresize.png")));
        ratioresize.setPreferredSize(new Dimension(30, 30));
        ratioresize.setToolTipText("Conserver les proportions lors du redimensionnement");
        ratioresize.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                if (PaintPanel.bratioresize) {
                    PaintPanel.bratioresize = false;
                    ratioresize.setBackground(null);
                } else {
                    PaintPanel.bratioresize = true;
                    ratioresize.setBackground(ACTIV_COLOR);
                    SToolBar.resizeButton.doClick();
                }
            }
        });


        //--- Layout the  shape buttons
        shapePanel = new SPanel(SPanel.GRAY);
        shapePanel.setLayout(new FlowLayout());

        SeparatorJPanel sjp = new SeparatorJPanel(Color.DARK_GRAY, 5, 5);
        shapePanel.add(newfile);
        shapePanel.add(openfile);
        shapePanel.add(openproject);
        shapePanel.add(save);
        shapePanel.add(imprime);
        shapePanel.add(annuler);
        shapePanel.add(filtreprec);
        shapePanel.add(envoicreat);
        shapePanel.add(camsm);

        shapePanel.add(sjp);

        shapePanel.add(cursorButton);
        shapePanel.add(resizeButton);
        shapePanel.add(crayonButton);
        shapePanel.add(circleButton);
        shapePanel.add(fcircleButton);
        shapePanel.add(rectangleButton);
        shapePanel.add(frectangleButton);
        shapePanel.add(lineButton);
        shapePanel.add(curveButton);
        shapePanel.add(gradientButton);
        shapePanel.add(gommeButton);
        shapePanel.add(potButton);
        shapePanel.add(selectButton);
        // shapePanel.add(polygoneButton); on verra plus tard
        shapePanel.add(pipetteButton);
        shapePanel.add(texteButton);

        SeparatorJPanel sjp2 = new SeparatorJPanel(Color.DARK_GRAY, 5, 5);
        shapePanel.add(sjp2);
        shapePanel.add(gradientok);
        shapePanel.add(zoomButton);
        shapePanel.add(ratioresize);


        listeButton = new JButton[]{cursorButton, resizeButton, crayonButton, circleButton, fcircleButton, rectangleButton,
            frectangleButton, lineButton, curveButton, gradientButton, gommeButton, potButton, selectButton,
            pipetteButton, texteButton
        };
        
        
        JScrollPane jsp = new JScrollPane(shapePanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(jsp);
        jsp.getHorizontalScrollBar().setUnitIncrement(10);

    }

    /**
     * Fonction qui test si l'os utilisé est windows
     * @return true si l'os est windows, false sinon
     */
    public static boolean isWindows() {

        String os = System.getProperty("os.name").toLowerCase();
        // windows
        return (os.indexOf("win") >= 0);

    }

    /**
     * Fonction qui test si l'os utilisé est mac
     * @return true si l'os est mac, false sinon
     */
    public static boolean isMac() {

        String os = System.getProperty("os.name").toLowerCase();
        // Mac
        return (os.indexOf("mac") >= 0);

    }

    /**
     * Fonction qui test si l'os utilisé est unix
     * @return true si l'os est unix, false sinon
     */
    public static boolean isUnix() {

        String os = System.getProperty("os.name").toLowerCase();
        // linux or unix
        return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);

    }

    /**
     * Fonction qui test si l'os utilisé est solaris
     * @return true si l'os est solaris, false sinon
     */
    public static boolean isSolaris() {

        String os = System.getProperty("os.name").toLowerCase();
        // Solaris
        return (os.indexOf("sunos") >= 0);

    }

    /**
     * Méthode utilitaire qui rafraîchi un panneau d'affichage
     * @param o Panneau d'affichage à rafraîchir
     */
    public static void refresh(JComponent o) {
        o.setSize(o.getWidth() + 1, o.getHeight() + 1);
        o.setSize(o.getWidth() - 1, o.getHeight() - 1);
    }
}
