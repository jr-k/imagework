package a.window;

import a.dialog.DialogImportImage;
import a.dialog.DialogNouveau;
import a.dialog.DialogNouveauInfo;
import a.drawing.Calques;
import a.drawing.GradientPanel;
import a.drawing.HistoryGroup;
import a.drawing.PaintPanel;
import a.drawing.SMenuBar;
import a.drawing.SToolBar;
import a.util.CpuThread;
import a.util.DottedLineSelectionThread;
import a.util.IwProjectSave;
import a.util.SDesktopPane;
import a.util.Shape;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

/**
 * Classe principale du projet qui gère le lancement du logiciel et de toutes les fenêtres associées
 * @author Jessym
 */
public class PaintWindow extends JFrame {
    //=================================================================== fields

    public PaintPanel _canvas;
    public SDesktopPane centerPanel;
    SMenuBar mb = new SMenuBar(this);
    public static DrawWindow dessin;
    public ColorWindow cpicker;
    public HistoryWindow histwin;
    public GradientWindow gradientwin;
    public PaintingOptionWindow optionwin;
    public CalquesWindow calqueswin;
    public CpuWindow cpuwin;
    public IWCameraWindow iwcamwin;
    GradientPanel gradientpane = new GradientPanel();
    public static String EXT, NAME;

    /**
     * Constructeur PaintWindow
     */
    public PaintWindow() {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("ImageWork");
        this.setSize((int)getToolkit().getScreenSize().getWidth(), ((int)getToolkit().getScreenSize().getHeight() - 40));
        this.setLocationRelativeTo(null); // Center window.


        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (UnsupportedLookAndFeelException e) {
        }

        this.setVisible(true);
        try {
            this.setIconImage(ImageIO.read(getClass().getResource("/misc/logicon.png")));
        } catch (IOException es) {
        }

        DialogNouveau dn = new DialogNouveau(null, "Nouveau fichier", true);
        DialogNouveauInfo dni = dn.showNouveauDialog();


        int w = dni.getWidth();
        int h = dni.getHeight();
        EXT = dni.getExtension();
        NAME = dni.getNomFichier();
        PaintPanel.COLOR_BACKGROUND = dni.getCouleur();

        _canvas = new PaintPanel(this, w, h);

        init();
        defineDnD();

    }

    /**
     * Initialise l'espace de travail
     */
    public void init() {
        // init & positionne toolbar
        SToolBar wintb = new SToolBar(this, _canvas);
        setLayout(new BorderLayout());
        add(wintb, BorderLayout.NORTH);

        // init & dimensionne le Desktop y ajoute le Dessin et le ColorPicker
        centerPanel = new SDesktopPane();
        centerPanel.setBackground(Color.DARK_GRAY);
        dessin = new DrawWindow(400, 400, 320, 20);
        dessin.setCanvas(_canvas, true);
        cpicker = new ColorWindow(200, 400, 0, 0);
        gradientwin = new GradientWindow(gradientpane, 240, 300, this.getWidth()-250, 290);
        histwin = new HistoryWindow(200, 190, 0, 400);
        optionwin = new PaintingOptionWindow(250, 155,this.getWidth()-260, 0);
        calqueswin = new CalquesWindow(250, 440, this.getWidth()-260, 155);
        cpuwin = new CpuWindow(240, 150, this.getWidth() / 2 - 120, this.getHeight() - 250);
        iwcamwin = new IWCameraWindow(320, 20);

        centerPanel.add(dessin);
        centerPanel.add(cpicker);
        centerPanel.add(gradientwin);
        centerPanel.add(histwin);
        centerPanel.add(optionwin);
        centerPanel.add(calqueswin);
        centerPanel.add(cpuwin);
        centerPanel.add(iwcamwin);


        // infos CPU "threadées"
        Thread t = new Thread(new CpuThread());
        t.start();

        // infos CPU "threadées"
        Thread t2 = new Thread(new DottedLineSelectionThread());
        t2.start();


        // ajoute le desktop panel à la Frame
        add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * Classe interne qui gère la fenêtre de dessin dans l'espace de travail
     */
    public class DrawWindow extends JInternalFrame {

        PaintPanel pp;

        /**
         * Créer une fenêtre de dessin dans l'espace de travail
         * @param w longueur de la fenêtre
         * @param h hauteur de la fenêtre
         * @param x position x de la fenêtre
         * @param y position y de la fenêtre
         */
        public DrawWindow(int w, int h, int x, int y) {
            this.setTitle(NAME + " | " + _canvas.WSIZE + "x" + _canvas.HSIZE);
            this.setClosable(true);
            this.setResizable(true);
            this.setMaximizable(true);
            this.setIconifiable(true);
            this.setLocation(x, y);
            this.setLayout(new FlowLayout());
            this.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);

            try {
                this.setFrameIcon(new ImageIcon(ImageIO.read(getClass().getResource("/icons/dessinicon.png"))));
            } catch (IOException ex) {
                Logger.getLogger(IWCameraWindow.class.getName()).log(Level.SEVERE, null, ex);
            }

            this.addInternalFrameListener(new InternalFrameListener() {

                public void internalFrameOpened(InternalFrameEvent e) {
                }

                public void internalFrameClosing(InternalFrameEvent e) {
                    SMenuBar.dessinz.doClick();
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
        }

        /**
         * Associe un Canvas de la classe PaintPanel
         * @param can PaintPanel associé à l'espace de dessin
         * @param nouveau true si il s'agit d'un nouveau projet vide, false sinon
         */
        public void setCanvas(PaintPanel can, boolean nouveau) {
            pp = can;

            int w = can.getWidth();
            int h = can.getHeight();

            if (w > 800 && h > 600) {
                this.setSize(800, 600);
                this.setPreferredSize(new Dimension(800, 600));
                if (!nouveau) {
                    can.zoom = 0.5;
                    can.rescaleImage();
                }
            } else if (w > 800 && h < 600) {
                this.setSize(800, h);
                this.setPreferredSize(new Dimension(800, h));
                if (!nouveau) {
                    can.zoom = 0.5;
                    can.rescaleImage();
                }
            } else if (w < 800 && h > 600) {
                this.setSize(w, 600);
                this.setPreferredSize(new Dimension(w, 600));
                if (!nouveau) {
                    can.zoom = 0.5;
                    can.rescaleImage();
                }
            } else if (w < 100 || h < 100) {
                this.setSize(100, 100);
                this.setPreferredSize(new Dimension(100, 100));
                if (!nouveau) {
                    can.zoom = 0.5;
                    can.rescaleImage();
                }
            } else {
                this.setSize(w+100, h+100);
                this.setPreferredSize(new Dimension(w+100, h+100));
            }


            JScrollPane jsp = new JScrollPane(can, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            jsp.setSize(w, h);
            jsp.setPreferredSize(new Dimension(w, h));
            this.setContentPane(jsp);
            jsp.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            jsp.getHorizontalScrollBar().setUnitIncrement(10);
            jsp.getVerticalScrollBar().setUnitIncrement(10);


        }
    }

    /* A partir d'ici on gère l'ouverture de fichier grâce au Drag And Drop */
    /**
     * Ouvre une image donnée en paramètre
     * @param file image à ouvrir
     */
    public void openImage(File file) { 

        DialogImportImage dii = new DialogImportImage(this, "Type d'import",
                "Voulez-vous ajouter cette image en tant que NOUVEAU CALQUE ou NOUVEAU PROJET ?", true);
        dii.showImportDialog();

        if (dii.type.equals("calque")) {
            
            try {
                FileImageInputStream fis = new FileImageInputStream(file);
                BufferedImage tmp = ImageIO.read(fis);
                tmp = PaintPanel.newCopy(tmp, BufferedImage.TYPE_INT_ARGB);
                
                 if (tmp.getWidth()>_canvas.WSIZE && tmp.getHeight()>_canvas.HSIZE && tmp!=null)
                        {
                            _canvas.tracker.x_tracker = tmp.getWidth();
                            _canvas.tracker.y_tracker = tmp.getHeight();
                            _canvas.tracker.resize();
                        }
                        else if (tmp.getWidth()>_canvas.WSIZE && tmp!=null)
                        {
                            _canvas.tracker.x_tracker = tmp.getWidth();
                            _canvas.tracker.resize();
                        }
                        else if (tmp.getHeight()>_canvas.HSIZE && tmp!=null){
                            _canvas.tracker.y_tracker = tmp.getHeight();
                            _canvas.tracker.resize();
                        }
                        
                
                
                Calques.current = Calques.calques.size();
                Calques.add(tmp);
                CalquesWindow.refreshCalques(Calques.calques);
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PaintWindow.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(PaintWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } else if (dii.type.equals("projet")) {
            try {

                FileImageInputStream fis = new FileImageInputStream(file);
                BufferedImage tmp = ImageIO.read(fis);

                tmp = PaintPanel.newCopy(tmp, BufferedImage.TYPE_INT_ARGB);

                if (_canvas == null) {

                    _canvas.layers.deleteAll();

                    _canvas = new PaintPanel(this, tmp.getWidth(), tmp.getHeight());
                    NAME = file.getName();
                    init();
                    _canvas.setImage(tmp);
                    _canvas.layers = new Calques();
                    _canvas.layers.add(_canvas.getImage());
                    CalquesWindow.refreshCalques(_canvas.layers.calques);
                } else {

                    Shape saveShape = _canvas._shape;

                    _canvas.removeAll();

                    dessin.getContentPane().removeAll();
                    _canvas.setImage(tmp);
                    _canvas = new PaintPanel(this, tmp.getWidth(), tmp.getHeight());
                    dessin.setTitle(file.getName());
                    dessin.setCanvas(_canvas, false);
                    SToolBar._canvas = _canvas;
                    dessin.revalidate();
                    _canvas.revalidate();

                    _canvas.requestFocus();
                    _canvas.addKeyListener(_canvas);
                    _canvas.INITIAL_SHAPE = saveShape;

                    SMenuBar.refresh(dessin);

                    _canvas.layers.deleteAll();
                    _canvas.layers = new Calques();
                    _canvas.layers.add(_canvas.getImage());
                    CalquesWindow.refreshCalques(_canvas.layers.calques);
                }

                if (!SMenuBar.dessinz.isSelected()) {
                    SMenuBar.dessinz.doClick();
                }
                _canvas.removeImageSelection(true);

            } catch (FileNotFoundException excep) {
                excep.printStackTrace();
            } catch (java.io.IOException excep) {
                excep.printStackTrace();
            }
        }
    }


    /**
     * Ouvre une liste d'image séléctionnée à partir d'une séléction de fichier image dans l'OS
     * @param data Liste des fichiers déposés dans le logiciel
     */
    public void openListImage(List data) {

            for (int i=0; i<data.size();i++) {

            try {
                FileImageInputStream fis = new FileImageInputStream((File)data.get(i));
                BufferedImage tmp = ImageIO.read(fis);
                tmp = PaintPanel.newCopy(tmp, BufferedImage.TYPE_INT_ARGB);

                 if (tmp.getWidth()>_canvas.WSIZE && tmp.getHeight()>_canvas.HSIZE && tmp!=null)
                        {
                            _canvas.tracker.x_tracker = tmp.getWidth();
                            _canvas.tracker.y_tracker = tmp.getHeight();
                            _canvas.tracker.resize();
                        }
                        else if (tmp.getWidth()>_canvas.WSIZE && tmp!=null)
                        {
                            _canvas.tracker.x_tracker = tmp.getWidth();
                            _canvas.tracker.resize();
                        }
                        else if (tmp.getHeight()>_canvas.HSIZE && tmp!=null){
                            _canvas.tracker.y_tracker = tmp.getHeight();
                            _canvas.tracker.resize();
                        }



                Calques.current = Calques.calques.size();
                Calques.add(tmp);
                CalquesWindow.refreshCalques(Calques.calques);

            } catch (FileNotFoundException ex) {
                Logger.getLogger(PaintWindow.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(PaintWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     * Ouvre un fichier projet donné en paramètre
     * @param file fichier projet à ouvrir
     */
    public void openProject(File file) {

        ObjectInputStream ois;
        IwProjectSave iwps = null;
        try {
            ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
            iwps = (IwProjectSave) ois.readObject();
            ois.close();

            Vector<Vector<Integer>> vimg = (Vector<Vector<Integer>>) iwps.getProject().get(0);
            Vector<Boolean> vvis = (Vector<Boolean>) iwps.getProject().get(1);
            Vector<Integer> vop = (Vector<Integer>) iwps.getProject().get(2);
            Vector<String> vnom = (Vector<String>) iwps.getProject().get(3);
            Vector<Integer> vfusion = (Vector<Integer>) iwps.getProject().get(4);

            Vector<BufferedImage> tmpBufVect = new Vector();


            for (int i = 0; i < vimg.size(); i++) {
                int nb = 0;
                BufferedImage tmp = new BufferedImage(iwps.getWidth(), iwps.getHeight(), BufferedImage.TYPE_INT_ARGB);
                for (int col = 0; col < iwps.getWidth(); col++) {
                    for (int ligne = 0; ligne < iwps.getHeight(); ligne++) {
                        tmp.setRGB(col, ligne, vimg.get(i).get(nb++));
                    }
                }
                tmpBufVect.add(tmp);
            }

            if (_canvas == null) {
                _canvas = new PaintPanel(this, iwps.getWidth(), iwps.getHeight());
                NAME = file.getName();
                init();
                _canvas.setImage(tmpBufVect.get(0));


            } else {

                Shape saveShape = _canvas._shape;
                _canvas.removeAll();

                dessin.getContentPane().removeAll();
                _canvas.setImage(tmpBufVect.get(0));
                _canvas = new PaintPanel(this, iwps.getWidth(), iwps.getHeight());
                dessin.setTitle(file.getName());
                PaintPanel.COLOR_BACKGROUND = Color.WHITE;
                dessin.setCanvas(_canvas, true);
                SToolBar._canvas = _canvas;
                dessin.revalidate();
                _canvas.revalidate();

                _canvas.requestFocus();
                _canvas.addKeyListener(_canvas);
                _canvas.INITIAL_SHAPE = saveShape;

                SMenuBar.refresh(dessin);


                _canvas.layers.deleteAll();

            }


            _canvas.layers.calques = tmpBufVect;
            _canvas.layers.visibility = vvis;
            _canvas.layers.opacity = vop;
            _canvas.layers.noms = vnom;
            _canvas.layers.fusion = vfusion;


            for (int i = 0; i < _canvas.layers.calques.size(); i++) {
                _canvas.layers.historiques.add(new HistoryGroup(_canvas.layers.calques.get(i), i));
            }

            CalquesWindow.refreshCalques(Calques.calques);

            SMenuBar.deselectallhints.doClick();
            _canvas.removeImageSelection(true);

            if (!SMenuBar.dessinz.isSelected()) {
                SMenuBar.dessinz.doClick();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * Défini le comportement du glisser-déposer sur la fenêtre principal du logiciel
     */
    public void defineDnD() {
        final PaintWindow area = this;
        DropTargetListener dropListener = new DropTargetListener() {

            public void dragEnter(DropTargetDragEvent dtde) {
                doDrag(dtde);
            }

            public void dragOver(DropTargetDragEvent dtde) {
                doDrag(dtde);
            }

            public void dropActionChanged(DropTargetDragEvent dtde) {
                doDrag(dtde);
            }

            public void dragExit(DropTargetEvent dte) {
            }

            private void doDrag(DropTargetDragEvent dtde) {
                if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    dtde.acceptDrag(dtde.getDropAction());
                } else {
                    dtde.rejectDrag();
                }
            }

            public void drop(DropTargetDropEvent dtde) {
                dtde.acceptDrop(dtde.getDropAction());
                List data;
                try {
                    data = (List) dtde.getTransferable().
                            getTransferData(DataFlavor.javaFileListFlavor);
                } catch (UnsupportedFlavorException e) {
                    dtde.dropComplete(false);
                    return;
                } catch (IOException e) {
                    dtde.dropComplete(false);
                    return;
                }
                if (((File) data.get(0)).getAbsoluteFile().toString().endsWith("iwp")) {
                    openProject((File) data.get(0));
                } else if ( data.size()==1){
                    openImage((File) data.get(0));
                }
                 else {
                    openListImage(data);
                 }
                dtde.dropComplete(true);
            }
        };
        area.setDropTarget(new DropTarget(area, DnDConstants.ACTION_COPY_OR_MOVE, dropListener));
    }

    //===================================================================== main
    public static void main(String[] args) {

        // Lance l'appli
        PaintWindow window = new PaintWindow();


    }
}