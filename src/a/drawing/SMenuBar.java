package a.drawing;


import a.util.TImage;
import a.util.Shape;
import a.util.ImageFileFilter;
import a.util.IwProjectSave;
import a.util.MPanelPrinter;
import a.dialog.DialogConvolution;
import a.dialog.DialogEnvoiCreation;
import a.dialog.DialogNouveauInfo;
import a.dialog.DialogNouveau;
import a.dialog.DialogApropos;
import a.util.JSFileChooser;
import a.window.PaintWindow;
import a.window.CalquesWindow;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

/**
 * Classe qui gère la barre de menu du logiciel
 * @author Jessym
 */
public class SMenuBar extends JMenuBar {

    PaintWindow win;
   public static JMenu fichier = new JMenu("Fichier"),
            edition = new JMenu("Edition"),
            filtre = new JMenu("Filtre"),
            image = new JMenu("Image"),
            transformation = new JMenu("Transformation"),
            rendering = new JMenu("Rendering"),
            affichage = new JMenu("Affichage"),
            iwmenu = new JMenu("ImageWork");
   public  JMenuItem SP = new JMenuItem("SP");
    public static JMenuItem nouveau = new JMenuItem("Nouveau");
    public static JMenuItem ouvrir = new JMenuItem("Ouvrir"),
            pouvrir = new JMenuItem("Ouvrir un Projet"),
            imprimer = new JMenuItem("Imprimer"),
            enregistrersous = new JMenuItem("Enregistrer sous");
    public static JMenuItem apropos = new JMenuItem("À propos"),
            enregistrer = new JMenuItem("Enregistrer"),
            quitter = new JMenuItem("Quitter"),
            document = new JMenuItem("Document"),
            copier = new JMenuItem("Copier"),
            couper = new JMenuItem("Couper"),
            supprimer = new JMenuItem("Supprimer"),
            coller = new JMenuItem("Coller"),
            selectall = new JMenuItem("Tout séléctionner"),
            flou = new JMenuItem("Flou"),
            flougaussien = new JMenuItem("Flou Gaussien"),
            floumvt = new JMenuItem("Flou de mouvement"),
            detectioncontours = new JMenuItem("Détéction de contours"),
            ameliorationbords = new JMenuItem("Amélioration de contours"),
            repoussage = new JMenuItem("Repoussage"),
            median = new JMenuItem("Median"),
            contraste = new JMenuItem("Affûtage"),
            luminosite = new JMenuItem("Augmentation de luminosité"),
            matriceconvolution = new JMenuItem("Matrice de convolution 2D"),
            roberts = new JMenuItem("Effet de Roberts"),
            prewit = new JMenuItem("Effet de Prewit"),
            sobel = new JMenuItem("Effet de Sobel"),
            estampage = new JMenuItem("Estampage"),
            rotation90 = new JMenuItem("Rotation 90° Horaire"),
            rotation180 = new JMenuItem("Rotation 180° Horaire"),
            rotation270 = new JMenuItem("Rotation 270° Horaire"),
            symetrieh = new JMenuItem("Symétrie Horizontale"),
            negatif = new JMenuItem("Négatif");
   public  static JMenuItem echellebase = new JMenuItem("Echelle de base"),
            filtreprec = new JMenuItem("Filtre précédent"),
            envoicrea = new JMenuItem("Envoyer votre création !");
    public JMenuItem zoomp = new JMenuItem("Zoom in"),
            zoomm = new JMenuItem("Zoom out"),
            ajouterCalque = new JMenuItem("Nouveau calque"),
            supprimerCalque = new JMenuItem("Supprimer le calque"),
            dupliquerCalque = new JMenuItem("Dupliquer le calque"),
            fusionnerCalque = new JMenuItem("Fusionner le calque actif avec le suivant"),
            upCalque = new JMenuItem("Monter la position du calque actif"),
            downCalque = new JMenuItem("Descendre la position du calque actif"),
            symetriev = new JMenuItem("Symétrie Verticale"),
            selectallaffichage = new JMenuItem("Sélectionner tout"),
            selectallhints = new JMenuItem("Sélectionner tout"),
            deselectallaffichage = new JMenuItem("Déselectionner tout");
            public static JMenuItem deselectallhints = new JMenuItem("Déselectionner tout");
    public static JMenuItem annuler = new JMenuItem("Annuler");
    JCheckBoxMenuItem antialiasing = new JCheckBoxMenuItem("Anti-crénelage", false),
            textantialiasing = new JCheckBoxMenuItem("Text-Anti-crénelage", false),
            renderingquality = new JCheckBoxMenuItem("Qualité de Rendu", false),
            colorrendering = new JCheckBoxMenuItem("Qualité de Rendu Couleur", false),
            interpolation = new JCheckBoxMenuItem("Interpolation", false),
            alphainterpolation = new JCheckBoxMenuItem("Alpha Interpolation", false),
            dithering = new JCheckBoxMenuItem("Dithering", false),
            fractionalmetrics = new JCheckBoxMenuItem("Fractional Metrics", false),
            strokecontrol = new JCheckBoxMenuItem("Contrôle épaisseur", false);
    public static JCheckBoxMenuItem couleur = new JCheckBoxMenuItem("Couleurs", true),
            history = new JCheckBoxMenuItem("Historique", true),
            gradient = new JCheckBoxMenuItem("Dégradés", false),
            cpu = new JCheckBoxMenuItem("Statistiques CPU", false),
            optionsf = new JCheckBoxMenuItem("Option de forme", true),
            dessinz = new JCheckBoxMenuItem("Zone de dessin", true),
            iwcam = new JCheckBoxMenuItem("IW Caméra", false),
            calques = new JCheckBoxMenuItem("Calques", true);
    public static JCheckBoxMenuItem zoominter = new JCheckBoxMenuItem("Zoom d'interpolation", false);
    static JRadioButtonMenuItem couleurgris = new JRadioButtonMenuItem("Niveaux de gris"),
            couleurrvb = new JRadioButtonMenuItem("Couleurs RVB", true);
    ButtonGroup colorimage = new ButtonGroup();
    JSFileChooser fc = new JSFileChooser();
    JSFileChooser pfc = new JSFileChooser();
    JSFileChooser ofc = new JSFileChooser();
    JMenu[] jmt;
    JMenuItem[] jmit0, jmit1, jmit2, jmit3, jmit4, jmit5, jmit6, jmit7;
    KeyStroke[] kst;

    
    /**
     * Constructeur de la barre de menu
     * @param mf Sauvegarde la fenêtre qui englobe la barre de menu
     */
    public SMenuBar(PaintWindow mf) {
        win = mf;

        fc.removeChoosableFileFilter(fc.getAcceptAllFileFilter());
        fc.addChoosableFileFilter(new ImageFileFilter(".iwp", "Fichier image au format ImageWork Project"));
        fc.addChoosableFileFilter(new ImageFileFilter(".jpg", "Fichier image au format JPG/JPEG"));
        fc.addChoosableFileFilter(new ImageFileFilter(".png", "Fichier image au format PNG"));
        fc.addChoosableFileFilter(new ImageFileFilter(".bmp", "Fichier image au format Bitmap"));

        ofc.removeChoosableFileFilter(ofc.getAcceptAllFileFilter());
        ofc.addChoosableFileFilter(new ImageFileFilter(".jpg", "Fichier image au format JPG/JPEG"));
        ofc.addChoosableFileFilter(new ImageFileFilter(".png", "Fichier image au format PNG"));
        ofc.addChoosableFileFilter(new ImageFileFilter(".bmp", "Fichier image au format Bitmap"));
        
        pfc.removeChoosableFileFilter(pfc.getAcceptAllFileFilter());
        pfc.addChoosableFileFilter(new ImageFileFilter(".iwp", "Fichier image au format ImageWork Project"));

        colorimage.add(couleurgris);
        colorimage.add(couleurrvb);

        initJMenu();
        initJMenuItem();

        win.setJMenuBar(this);
    }

    /**
     * Initialise les principaux Menus (JMenu)
     */
    public void initJMenu() {
        jmt = new JMenu[]{fichier, edition, image, filtre, transformation, rendering, affichage, iwmenu};

        for (int i = 0; i < jmt.length; i++) {
            add(jmt[i]);
            jmt[i].setMnemonic(jmt[i].getText().charAt(0));
        }
    }

    /**
     * Initilise les boutons du menu (JMenuItem)
     */
    public void initJMenuItem() {

        jmit0 = new JMenuItem[]{
            nouveau, ouvrir, pouvrir, SP, enregistrersous, SP, imprimer, SP, quitter
        };
        kst = new KeyStroke[]{
            KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK),
            KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK),
            KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_MASK),
            null,//KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK),
            KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK),
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK),
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_MASK)
        };
        chargeIn(jmit0, 0, kst);


        nouveau.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {


                // On demande les infos de base pour créer un fichier
                DialogNouveau dn = new DialogNouveau(null, "Nouveau fichier", true);
                DialogNouveauInfo dni = dn.showNouveauDialog();
                int w = dni.getWidth();
                int h = dni.getHeight();
                PaintPanel.COLOR_BACKGROUND = dni.getCouleur();

                if (win._canvas == null) {
                    win._canvas = new PaintPanel(win, w, h);
                    win.NAME = dni.getNomFichier();
                    win.init();
                } else {

                    Shape saveShape = win._canvas._shape;
                    win._canvas.removeAll();

                    win.dessin.getContentPane().removeAll();
                    win._canvas.setImage(null);
                    win._canvas = new PaintPanel(win, w, h);
                    win.dessin.setTitle(dni.getNomFichier());
                    PaintPanel.COLOR_BACKGROUND = dni.getCouleur();
                    win.dessin.setCanvas(win._canvas, true);
                    SToolBar._canvas = win._canvas;
                    win.dessin.revalidate();
                    win._canvas.revalidate();

                    win._canvas.requestFocus();
                    win._canvas.addKeyListener(win._canvas);
                    win._canvas.INITIAL_SHAPE = saveShape;

                    refresh(win.dessin);
                }

                win._canvas.layers.deleteAll();

                deselectallhints.doClick();

                if (!dessinz.isSelected()) {
                    dessinz.doClick();
                }

                win._canvas.removeImageSelection(true);
            }
        });



        ouvrir.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {

                if (ofc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = new File(ofc.getSelectedFile().getAbsolutePath());
                    win.openImage(file);

                }

            }
        });


        pouvrir.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {

                if (pfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = new File(pfc.getSelectedFile().getAbsolutePath());
                    win.openProject(file);
                    
                }

            }
        });


        imprimer.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {

                echellebase.doClick();

                win.dessin.setSize(win._canvas._bufImage.getWidth() + 25, win._canvas._bufImage.getHeight() + 25);

                // Récupère un PrinterJob
                PrinterJob job = PrinterJob.getPrinterJob();
                HashPrintRequestAttributeSet printRequestSet = new HashPrintRequestAttributeSet();
                printRequestSet.add(OrientationRequested.LANDSCAPE);

                // Définit son contenu à imprimer
                job.setPrintable(new MPanelPrinter(new TImage(win._canvas.layers.getFinalImage())));
                // Affiche une boîte de choix d'imprimante
                if (job.printDialog(printRequestSet)) {
                    try {
                        // Effectue l'impression
                        job.print();
                    } catch (PrinterException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        enregistrer.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
            }
        });

        enregistrersous.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {

                if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {

                    try {

                        String ext = fc.getFileFilter().getDescription();
                        File file = new File(fc.getSelectedFile().getAbsolutePath() + ext);
                        FileImageOutputStream fios = new FileImageOutputStream(file);

                        if (ext.substring(1, ext.length()).toLowerCase().equals("iwp")) {
                            ObjectOutputStream oos;
                            try {
                                oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
                                IwProjectSave iwps = new IwProjectSave(Calques.calques, Calques.visibility,
                                        Calques.opacity, Calques.noms,Calques.fusion, win._canvas.WSIZE, win._canvas.HSIZE);

                                oos.writeObject(iwps);
                                oos.close();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {

                            BufferedImage tmpBuf = null;

                            if (ext.substring(1, ext.length()).toLowerCase().equals("jpg")) {
                                tmpBuf = win._canvas.newCopy(win._canvas.layers.getFinalImage(), BufferedImage.TYPE_INT_RGB);
                            } else if (ext.substring(1, ext.length()).toLowerCase().equals("bmp")) {
                                tmpBuf = win._canvas.newCopy(win._canvas.layers.getFinalImage(), BufferedImage.TYPE_BYTE_INDEXED);
                            } else if (ext.substring(1, ext.length()).toLowerCase().equals("png")) {
                                tmpBuf = win._canvas.newCopy(win._canvas.layers.getFinalImage(), BufferedImage.TYPE_INT_ARGB);
                            }


                            ImageIO.write(tmpBuf, ext.substring(1, ext.length()), fios);

                        }
                        fios.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        quitter.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                System.exit(0);
            }
        });

        jmit1 = new JMenuItem[]{
            annuler,  SP, copier, coller, couper, supprimer,selectall
        };
        kst = new KeyStroke[]{
            KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_MASK),
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK),
            KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK),
            KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK),
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK)};
        chargeIn(jmit1, 1, kst);

        selectall.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (PaintPanel.selectPane == null) {
                    SToolBar.cursorButton.doClick();
                    win._canvas.selectAll();
                }
            }
        });

        copier.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (PaintPanel.selectPane != null) {
                    PaintPanel.pressbuffer = PaintPanel.newCopy(PaintPanel.selectPane.simg, PaintPanel.type);
                    Toolkit toolKit = Toolkit.getDefaultToolkit();
                    Clipboard cb = toolKit.getSystemClipboard();
                    ImageSelection data = new ImageSelection(PaintPanel.pressbuffer);
                    cb.setContents( data, null);
                    win._canvas.removeImageSelection(true);
                }
            }
        });

        couper.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (PaintPanel.selectPane != null) {

                    PaintPanel.pressbuffer = PaintPanel.newCopy(PaintPanel.selectPane.simg, PaintPanel.type);
                    Toolkit toolKit = Toolkit.getDefaultToolkit();
                    Clipboard cb = toolKit.getSystemClipboard();
                    ImageSelection data = new ImageSelection(PaintPanel.pressbuffer);
                    cb.setContents( data, null);
                                 
                    int xt = PaintPanel.selectPane.xo;
                    int yt = PaintPanel.selectPane.yo;
                    int wt = PaintPanel.selectPane.simg.getWidth();
                    int ht = PaintPanel.selectPane.simg.getHeight();
                    PaintPanel.selectPane.simg = PaintPanel.newCopyAlpha(PaintPanel.selectPane.simg, PaintPanel.type);
                    
                    win._canvas.dessinerSelection(true);
             
                    xt = (int) Math.round(xt * (double) wt / PaintPanel.cachedWidth2);
                    yt = (int) Math.round(yt * (double) ht / PaintPanel.cachedHeight2);

                    for (int ws = xt; ws < xt + wt; ws++) {
                        for (int hs = yt; hs < yt + ht; hs++) {
                            if (ws < PaintPanel._bufImage.getWidth() && hs < PaintPanel._bufImage.getHeight()
                                    && ws >= 0 && hs >= 0) {
                                PaintPanel._bufImage.setRGB(ws, hs, PaintPanel.makeRGBA(0, 0, 0, 0));
                            }
                        }
                    }
                }
            }
        });
        
        supprimer.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (PaintPanel.selectPane != null) {

                                 
                    int xt = PaintPanel.selectPane.xo;
                    int yt = PaintPanel.selectPane.yo;
                    int wt = PaintPanel.selectPane.simg.getWidth();
                    int ht = PaintPanel.selectPane.simg.getHeight();
                    PaintPanel.selectPane.simg = PaintPanel.newCopyAlpha(PaintPanel.selectPane.simg, PaintPanel.type);
                    
                    win._canvas.dessinerSelection(true);
             
                    xt = (int) Math.round(xt * (double) wt / PaintPanel.cachedWidth2);
                    yt = (int) Math.round(yt * (double) ht / PaintPanel.cachedHeight2);

                    for (int ws = xt; ws < xt + wt; ws++) {
                        for (int hs = yt; hs < yt + ht; hs++) {
                            if (ws < PaintPanel._bufImage.getWidth() && hs < PaintPanel._bufImage.getHeight()
                                    && ws >= 0 && hs >= 0) {
                                PaintPanel._bufImage.setRGB(ws, hs, PaintPanel.makeRGBA(0, 0, 0, 0));
                            }
                        }
                    }
                }
            }
        });

        coller.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
               
                SToolBar.cursorButton.doClick();
                CalquesWindow.ajouter.doClick();
                                     
                Toolkit toolKit = Toolkit.getDefaultToolkit();
                Clipboard cb = toolKit.getSystemClipboard();
                Transferable clipData = cb.getContents(cb);
                BufferedImage bi = null;
                        try {
                            if (clipData.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                                 bi = (BufferedImage) (clipData.getTransferData(DataFlavor.imageFlavor));
                               
                            }
                        } catch (Exception ufe) {
                        }

                    
                        if (bi.getWidth()>win._canvas.WSIZE && bi.getHeight()>win._canvas.HSIZE && bi!=null)
                        {
                            win._canvas.tracker.x_tracker = bi.getWidth();
                            win._canvas.tracker.y_tracker = bi.getHeight();
                            win._canvas.tracker.resize();
                        }
                        else if (bi.getWidth()>win._canvas.WSIZE && bi!=null)
                        {
                            win._canvas.tracker.x_tracker = bi.getWidth();
                            win._canvas.tracker.resize();
                        }
                        else if (bi.getHeight()>win._canvas.HSIZE && bi!=null){
                            win._canvas.tracker.y_tracker = bi.getHeight();
                            win._canvas.tracker.resize();
                        }
                        
                     if (bi!=null)   
                    Calques.calques.get(Calques.current).getGraphics().drawImage(bi, 0, 0, null);
                    CalquesWindow.refreshCalques(Calques.calques);
                }
            
        });

        annuler.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {

                win._canvas.removeImageSelection(true);

                HistoryGroup g = Calques.historiques.get(Calques.current);

                if (g.images.size() >= 2) {

                    if (g.images.size() > 1) {

                        g.images.removeElementAt((g.images.size() - 1));
                        Calques.calques.setElementAt(g.images.get(g.images.size() - 1), Calques.current);
                        CalquesWindow.refreshCalques(win._canvas.layers.calques);
                        win._canvas.repaint();
                    }
                } else {
                    int w = win._canvas.WSIZE;
                    int h = win._canvas.HSIZE;
                    BufferedImage bufImage = (BufferedImage) new BufferedImage(w, h, PaintPanel.type);
                    Graphics2D gc = bufImage.createGraphics();
                    gc.drawImage(win._canvas.layers.historiques.get(Calques.current).getImages().get(0),0,0,null);
                    Calques.calques.setElementAt(bufImage, Calques.current);
                    CalquesWindow.refreshCalques(win._canvas.layers.calques);
                    win._canvas.repaint();
                }

            }
        });


        jmit2 = new JMenuItem[]{
            ajouterCalque, supprimerCalque, dupliquerCalque, fusionnerCalque, SP, upCalque, downCalque,
            SP, couleurgris, couleurrvb,
            SP, negatif, SP, zoominter, zoomp, zoomm, echellebase
        };
        kst = new KeyStroke[]{
            KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK + KeyEvent.SHIFT_MASK),
            KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_MASK + KeyEvent.SHIFT_MASK),
            KeyStroke.getKeyStroke(KeyEvent.VK_J, KeyEvent.CTRL_MASK + KeyEvent.SHIFT_MASK),
            KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_MASK + KeyEvent.SHIFT_MASK),
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.CTRL_MASK + KeyEvent.SHIFT_MASK),
            KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.CTRL_MASK + KeyEvent.SHIFT_MASK),
            null,
            null, null, null, KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_MASK), null, null,
            KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.CTRL_MASK),
            KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.CTRL_MASK),
            KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.CTRL_MASK),};
        chargeIn(jmit2, 2, kst);


        upCalque.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                CalquesWindow.cup.doClick();
            }
        });

        downCalque.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                CalquesWindow.cdown.doClick();
            }
        });

        ajouterCalque.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                CalquesWindow.ajouter.doClick();
            }
        });

        supprimerCalque.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                CalquesWindow.supprimer.doClick();
            }
        });

        dupliquerCalque.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                CalquesWindow.dupliquer.doClick();
            }
        });

        couleurgris.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                PaintPanel.type = BufferedImage.TYPE_BYTE_GRAY;
                PaintPanel._bufImage = win._canvas.newCopy(PaintPanel._bufImage, PaintPanel.type);
                CalquesWindow.refreshCalques(Calques.calques);
            }
        });

        couleurrvb.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                PaintPanel.type = BufferedImage.TYPE_INT_ARGB;
                PaintPanel._bufImage = win._canvas.newCopy(PaintPanel._bufImage, PaintPanel.type);
                CalquesWindow.refreshCalques(Calques.calques);
            }
        });

        negatif.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                win._canvas.setNegative(PaintPanel._bufImage);
            }
        });
        echellebase.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                win._canvas.zoom = 1;
                win._canvas.rescaleImage();
            }
        });
        zoomp.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                win._canvas.increaseZoom();
                win._canvas.rescaleImage();
            }
        });
        zoomm.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                win._canvas.decreaseZoom();
                win._canvas.rescaleImage();
            }
        });
        zoominter.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                win._canvas.bzoominter = zoominter.isSelected();
            }
        });




        jmit3 = new JMenuItem[]{
            filtreprec, SP, flou, flougaussien,floumvt, SP, detectioncontours,
            ameliorationbords, SP, repoussage, contraste, median, luminosite, estampage, SP,
            roberts, prewit, sobel, SP, matriceconvolution
        };
        kst = new KeyStroke[]{
            KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_MASK),
            null, null, null, null,null, null, null, null, null, null, null, null, null, null, null, null, null, null, null
        };
        chargeIn(jmit3, 3, kst);

        filtreprec.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                win._canvas.filtre(win._canvas.getImage(), "convo", win._canvas.getLastFiltreMatrice());
            }
        });
        flou.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                win._canvas.filtre(win._canvas.getImage(), "flou", null);
            }
        });
        flougaussien.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                win._canvas.filtre(win._canvas.getImage(), "flouGaussien", null);
            }
        });
        floumvt.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                win._canvas.filtre(win._canvas.getImage(), "floumvt", null);
            }
        });
        detectioncontours.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                win._canvas.filtre(win._canvas.getImage(), "contours", null);
            }
        });
        ameliorationbords.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                win._canvas.filtre(win._canvas.getImage(), "ameliorationbords", null);
            }
        });
        repoussage.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                win._canvas.filtre(win._canvas.getImage(), "repoussage", null);
            }
        });
        contraste.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                win._canvas.filtre(win._canvas.getImage(), "contrastep", null);
            }
        });
        luminosite.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                win._canvas.filtre(win._canvas.getImage(), "luminosite", null);
            }
        });
        estampage.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                win._canvas.filtre(win._canvas.getImage(), "estampage", null);
            }
        });
        roberts.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                win._canvas.filtre(win._canvas.getImage(), "roberts", null);
            }
        });
        prewit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                win._canvas.filtre(win._canvas.getImage(), "prewit", null);
            }
        });
        sobel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                win._canvas.filtre(win._canvas.getImage(), "sobel", null);
            }
        });
        median.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                win._canvas.filtre(win._canvas.getImage(), "median", null);
            }
        });
        matriceconvolution.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                DialogConvolution dc = new DialogConvolution(null, "Matrice de convolution 2D", true);
                dc.showConvolutionDialog();
                win._canvas.filtre(win._canvas.getImage(), "convo", dc.getInfos().getMatrice());
            }
        });


        jmit4 = new JMenuItem[]{
            rotation90, rotation180, rotation270, SP, symetrieh, symetriev
        };
        kst = new KeyStroke[]{
            KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_MASK),
            null, null, null,
            KeyStroke.getKeyStroke(KeyEvent.VK_1, KeyEvent.CTRL_MASK),
            KeyStroke.getKeyStroke(KeyEvent.VK_2, KeyEvent.CTRL_MASK),};
        chargeIn(jmit4, 4, kst);
        rotation90.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                win._canvas.filtre(win._canvas.getImage(), "r90", null);
            }
        });
        rotation180.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                win._canvas.filtre(win._canvas.getImage(), "r180", null);
            }
        });
        rotation270.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                win._canvas.filtre(win._canvas.getImage(), "r270", null);
            }
        });
        symetrieh.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                win._canvas.filtre(win._canvas.getImage(), "hsymetry", null);
            }
        });
        symetriev.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                win._canvas.filtre(win._canvas.getImage(), "vsymetry", null);
            }
        });

        jmit5 = new JMenuItem[]{
            selectallhints, deselectallhints, SP, antialiasing, textantialiasing, SP, renderingquality, colorrendering,
            SP, interpolation, alphainterpolation, SP, dithering, fractionalmetrics, SP, strokecontrol
        };
        kst = new KeyStroke[]{
            null, null, null,
            KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK + KeyEvent.SHIFT_MASK),
            KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_MASK + KeyEvent.SHIFT_MASK),
            null, null, null, null, null, null, null, null, null, null, null
        };
        chargeIn(jmit5, 5, kst);
        selectallhints.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                for (int i = 3; i < jmit5.length; i++) {
                    if (!jmit5[i].isSelected()) {
                        jmit5[i].doClick();
                    }
                }
            }
        });
        deselectallhints.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                for (int i = 3; i < jmit5.length; i++) {
                    if (jmit5[i].isSelected()) {
                        jmit5[i].doClick();
                    }
                }
            }
        });
        antialiasing.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                win._canvas.bantialiasing = antialiasing.isSelected();

            }
        });
        textantialiasing.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                win._canvas.btextantialiasing = textantialiasing.isSelected();
            }
        });
        renderingquality.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                win._canvas.brendering = renderingquality.isSelected();
            }
        });
        colorrendering.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                win._canvas.bcolorrendering = colorrendering.isSelected();
            }
        });
        interpolation.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                win._canvas.binterpolation = interpolation.isSelected();
            }
        });
        alphainterpolation.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                win._canvas.balphainterpolation = alphainterpolation.isSelected();
            }
        });
        dithering.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                win._canvas.bdithering = dithering.isSelected();
            }
        });
        fractionalmetrics.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                win._canvas.bfractionalmetrics = fractionalmetrics.isSelected();
            }
        });
        strokecontrol.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                win._canvas.bstrokecontrol = strokecontrol.isSelected();
            }
        });

        jmit6 = new JMenuItem[]{
            selectallaffichage, deselectallaffichage, SP, dessinz, couleur, calques, gradient, history, optionsf, cpu, iwcam
        };
        kst = new KeyStroke[]{
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        };
        chargeIn(jmit6, 6, kst);
        dessinz.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                win.dessin.setVisible(dessinz.isSelected());
            }
        });
        couleur.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                win.cpicker.setVisible(couleur.isSelected());
            }
        });
        calques.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                win.calqueswin.setVisible(calques.isSelected());
            }
        });
        gradient.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                win.gradientwin.setVisible(gradient.isSelected());
            }
        });
        history.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                win.histwin.setVisible(history.isSelected());
            }
        });
        optionsf.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                win.optionwin.setVisible(optionsf.isSelected());
            }
        });
        cpu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                win.cpuwin.setVisible(cpu.isSelected());
            }
        });
        iwcam.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (win.iwcamwin.init) {
                    win.iwcamwin.init();
                } else {
                    win.iwcamwin.setVisible(iwcam.isSelected());
                }
            }
        });
        selectallaffichage.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                for (int i = 3; i < jmit6.length; i++) {
                    if (jmit6[i] != iwcam) {
                        if (!jmit6[i].isSelected()) {
                            jmit6[i].doClick();
                        }
                    }
                }
            }
        });
        deselectallaffichage.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                for (int i = 3; i < jmit6.length; i++) {
                    if (jmit6[i].isSelected()) {
                        jmit6[i].doClick();
                    }
                }
            }
        });



        jmit7 = new JMenuItem[]{
            apropos, SP, envoicrea
        };
        kst = new KeyStroke[]{
            null, null, null
        };
        chargeIn(jmit7, 7, kst);
        apropos.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                DialogApropos apn = new DialogApropos(null, "À propos - ImageWork", true);
                apn.showNouveauDialog();
            }
        });
        envoicrea.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                DialogEnvoiCreation dec = new DialogEnvoiCreation(null, "Envoi de votre Image", true);
                dec.showCreationDialog();
            }
        });

    }

    /**
     * Charge dans un menu principal (JMenu) un bouton d'action (JMenuItem)
     * @param jmat Tableau des boutons d'actions à insérer
     * @param jmenuIndex Menu principal qui va contenir les boutons d'actions
     */
    public void chargeIn(JMenuItem[] jmat, int jmenuIndex) {
        for (int i = 0; i < jmat.length; i++) {

            if (jmat[i].getText().equals("SP")) {
                jmt[jmenuIndex].addSeparator();
            } else {
                jmt[jmenuIndex].add(jmat[i]);
            }
        }
    }

    /**
     * Charge dans un menu principal (JMenu) un bouton d'action (JMenuItem) ainsi que leurs raccourcis associés
     * @param jmat Tableau des boutons d'actions à insérer
     * @param jmenuIndex Menu principal qui va contenir les boutons d'actions
     * @param kmat Tableau des raccourcis liés aux boutons d'actions
     */
    public void chargeIn(JMenuItem[] jmat, int jmenuIndex, KeyStroke[] kmat) {
        for (int i = 0; i < jmat.length; i++) {

            if (jmat[i].getText().equals("SP")) {
                jmt[jmenuIndex].addSeparator();
            } else {
                jmt[jmenuIndex].add(jmat[i]);
                jmat[i].setAccelerator(kmat[i]);
            }
        }
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
