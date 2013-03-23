package a.drawing;

import a.window.*;
import a.util.*;


import java.awt.print.PageFormat;
import java.awt.print.PrinterException;



import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;
import javax.swing.BorderFactory;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import java.awt.print.Printable;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Classe principale qui gère l'intéraction utilisateur-dessin
 * @author Jessym
 */
public class PaintPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener, Printable {

    //=============================================================== séléction
    static SelectionImage selectPane;
    boolean selectionOnly = false;
    int stx = 0;
    int sty = 0;
    static BufferedImage pressbuffer = null;
    //================================================================ constants
    public static int WSIZE;     // Size of paint area.
    public static int HSIZE;     // Size of paint area.
    public static Shape INITIAL_SHAPE = Shape.CRAYON;
    public static Color INITIAL_COLOR = Color.BLACK;
    public static Color COLOR_BACKGROUND = Color.WHITE;
    // =============================================================== rendering
    public boolean bantialiasing = false;
    public boolean btextantialiasing = false;
    public boolean brendering = false;
    public boolean bcolorrendering = false;
    public boolean binterpolation = false;
    public boolean balphainterpolation = false;
    public boolean bdithering = false;
    public boolean bfractionalmetrics = false;
    public boolean bstrokecontrol = false;
    //=================================================================== fields
    public State _state = State.IDLE;
    public static Shape _shape = INITIAL_SHAPE;
    private Color _color = INITIAL_COLOR;
    public Point _start = null; // Where mouse is pressed.
    public Point _end = null; // Where mouse is dragged to or released.
    public Point _prev = null;
    public Color COLORCHOOSEN = Color.BLACK;
    public static PaintWindow pad;
    public static JTextField tf;
    public static int sizeText;
    public static BufferedImage selection;
    public TrackerResizer tr;
    public static boolean gradientpaint = false;
    public static boolean bratioresize = false;
    public static boolean bzoominter = false;
    public int locx = 0, locy = 0;
    public Point mouse;
    public String titre;
    boolean strict = false;
    public int strictsx,strictsy,strictex,strictey;
    public static enum State {

        IDLE, DRAGGING
    }
    //    Initialized first time paintComponent is called.
    public static BufferedImage _bufImage = null;
    public static Vector<BufferedImage> historiquez = new Vector();
    public static Calques layers;
    public static int type = BufferedImage.TYPE_INT_ARGB;
    public static TrackerResizer tracker;

    //============================================================== constructor
    /**
     * Constructeur de l'espace de dessin
     * @param parent fenetre englobante parent
     * @param w longueur du dessin
     * @param h hauteur du dessin
     */
    public PaintPanel(PaintWindow parent, int w, int h) {

        WSIZE = w;
        HSIZE = h;

        titre = parent.NAME;

        setMouseWheelEnabled(true);

        setPreferredSize(new Dimension(WSIZE, HSIZE));
        setSize(new Dimension(WSIZE, HSIZE));

        pad = parent;

        this.addMouseListener(this);
        this.addMouseMotionListener(this);


        this.setFocusable(true);
        this.requestFocus();
        this.addKeyListener(this);

        tracker = new TrackerResizer(this);
        this.add(tracker);
        this.setLayout(null);
        tracker.setLocation(WSIZE, HSIZE);
        tracker.setVisible(false);
    }

    /**
     * Assigne une forme passé en paramètre
     * @param shape Forme à assigner
     */
    public void setShape(Shape shape) {
        _shape = shape;
    }

    /**
     * Assigne une couleur passée en paramètre
     * @param color couleur à assigner
     */
    public void setColor(Color color) {
        _color = color;
    }

    /**
     * Filtre négatif sur l'image passée en paramètre
     * @param imgArg Image à filtrer
     */
    public void setNegative(BufferedImage imgArg) {

        Image negative = createImage(new FilteredImageSource(imgArg.getSource(), new FiltreNegatif()));
        imgArg.getGraphics().drawImage(negative, 0, 0, null);

        Calques.historiques.get(Calques.current).add(this.newCopy(_bufImage, type));
        CalquesWindow.refreshCalques(layers.calques);
    }
    //=========================================================== paintComponent

    /**
     * Dessin de l'image dans le Panneau de dessin
     * @param g Objet graphique lié à l'image présente sur l'espace de dessin 
     */
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // Paint font style
        //*/ 
        Color colors[] = new Color[]{Color.decode("0xc5cfdf"), Color.decode("0xdce5f2")};
        MultipleGradientPaint p = new LinearGradientPaint(new Point2D.Float(0, 0), new Point2D.Float(0, this.getHeight()), new float[]{0.0F, 1.0F}, colors);
        g2.setPaint(p);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        //*/

        if (_bufImage == null) {
            //... Initialisation la première fois de l'image
            int w = WSIZE;
            int h = HSIZE;
            _bufImage = (BufferedImage) new BufferedImage(w, h, type);
            Graphics2D gc = _bufImage.createGraphics();
            gc.setColor(COLOR_BACKGROUND);
            gc.fillRect(0, 0, w, h); // Remplir l'arrière plan

            setImage(_bufImage);

            layers = new Calques();
            layers.add(_bufImage);
            CalquesWindow.refreshCalques(layers.calques);
        }

        _bufImage = layers.getCurrentImage();

        int image_x = (getWidth() - cachedWidth) / 2;
        int image_y = (getHeight() - cachedHeight) / 2;


        // Photoshop font style
        //*/
        BufferedImage img = null;
        try {
            img = ImageIO.read(getClass().getResource("/misc/transtexture.png"));
        } catch (IOException ex) {
        }

        TexturePaint tp = new TexturePaint(img, new Rectangle(0, 0, 20, 20));
        g2.setPaint(tp);
        g2.fillRect(0, 0, cachedWidth, cachedHeight);
        //*/


        // dessin de l'image final à l'échelle
        if (bzoominter) {
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        }
        g2.drawImage(layers.getFinalImage(), 0, 0, cachedWidth, cachedHeight, this);
        this.rescaleImage();


        // gestion de la séléction
        if (selectPane != null) {
            g2.setColor(Color.black);
            g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, new float[]{3F}, DottedLineSelectionThread.f1));
            g2.drawRect(selectPane.xo - 2, selectPane.yo - 2, cachedWidth2 + 2, cachedHeight2 + 2);
            g2.drawImage(selectPane.simg, selectPane.xo, selectPane.yo, cachedWidth2, cachedHeight2, null);
            this.rescaleImage();
        }




        //... Redessine la forme en train d'être dessinée
        if (_state == State.DRAGGING) {
            //... Dessine la forme qui est en train d'être dessiné sur l'écran,
            //    mais pas dans l'image buffer PRINCIPALE. La forme sera dessinée
            //    lorsque le bouton de la souris sera relaché.

            drawCurrentShape(g2, false);
        }

        SToolBar.refresh(this);
        SToolBar.refresh(tracker);

    }
    
    
   

    /**
     * Dessine la forme assignée dans l'espace de dessin
     * @param g2 Object graphique lié à l'image présente sur l'espace de dessin 
     * @param release true si le clic est relaché, false sinon
     */
    public void drawCurrentShape(Graphics2D g2, boolean release) {

        Graphics2D g = (Graphics2D) _bufImage.getGraphics();


        getRenderingHints(g2);
        getRenderingHints(g);

        boolean stroke = false;



        if ((float) PaintingOptionWindow.stroke_s.getValue() != 0) {
            g2.setStroke(new BasicStroke((float) PaintingOptionWindow.stroke_s.getValue()));
            g.setStroke(new BasicStroke((float) PaintingOptionWindow.stroke_s.getValue()));
            stroke = true;
        }

        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
        ac = AlphaComposite.getInstance(ac.getRule(), (float) ColorWindow.alphaSlider.getValue() / 100.0F);

        g2.setComposite(ac);
        g.setComposite(ac);



        int startScaledX = Math.round(_start.x * (float) _bufImage.getWidth() / cachedWidth);
        int startScaledY = Math.round(_start.y * (float) _bufImage.getHeight() / cachedHeight);
        int prevScaledX = Math.round(_prev.x * (float) _bufImage.getWidth() / cachedWidth);
        int prevScaledY = Math.round(_prev.y * (float) _bufImage.getHeight() / cachedHeight);
        int endScaledX = Math.round(_end.x * (float) _bufImage.getWidth() / cachedWidth);
        int endScaledY = Math.round(_end.y * (float) _bufImage.getHeight() / cachedHeight);

        int xf, yf, w, h, tmp, txo, tyo, vtxo = 0, vtyo = 0,vxf=0,vyf=0;;

        if (release) {
            vtxo = _start.x; vxf=_end.x;
            vtyo = _start.y; vyf=_end.y;
            xf = endScaledX;
            yf = endScaledY;
            w = xf - startScaledX;
            h = yf - startScaledY;
            txo = startScaledX;
            tyo = startScaledY;
            if (w < 0) {
                w = w * -1;
                tmp = txo;
                txo = xf;
                xf = tmp;
            }
            if (h < 0) {
                h = h * -1;
                tmp = tyo;
                tyo = yf;
                yf = tmp;
            }

            _start = new Point(startScaledX, startScaledY);
            _prev = new Point(prevScaledX, prevScaledY);
            _end = new Point(endScaledX, endScaledY);


        } else {
            xf = _end.x;
            yf = _end.y;
            w = xf - _start.x;
            h = yf - _start.y;
            txo = _start.x;
            tyo = _start.y;
            if (w < 0) {
                w = w * -1;
                tmp = txo;
                txo = xf;
                xf = tmp;
            }
            if (h < 0) {
                h = h * -1;
                tmp = tyo;
                tyo = yf;
                yf = tmp;
            }

        }



        // appliquer la couleur ou le dégradé selon le choix de l'utilisateur  
        if (gradientpaint) {
            g.setPaint(getGradient(w, txo, tyo, xf, yf));
            g2.setPaint(getGradient(w, txo, tyo, xf, yf));
        } else {
            g.setPaint(COLORCHOOSEN);
            g2.setPaint(COLORCHOOSEN);
        }


        SToolBar.refresh(this);


        if (tf != null && release == true) {
            if (!tf.contains(_end.x - 20, _end.y - 20)) {
                if (gradientpaint) {
                    g2.setPaint(getGradient(tf.getWidth(), txo, tyo, xf, yf));
                }
                g2.setFont(tf.getFont());
                g2.drawString(tf.getText(), tf.getX(), tf.getY() + tf.getHeight());
                tf.setVisible(false);
                repaint();
            }
        }



        switch (_shape) {

            case RESIZER:

                if (!release) {

                    int xt = tracker.x_tracker2;
                    int yt = tracker.y_tracker2;

                    g2.setColor(Color.black);
                    g2.setXORMode(Color.white);
                    g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, new float[]{2f}, 0f));
                    g2.drawRect(0, 0, xt, yt);
                }
                break;

            case CURSOR:

                break;

            case CRAYON:
      
                int sizeCrayon = PaintingOptionWindow.size_s.getValue();
                g.setStroke(new BasicStroke(sizeCrayon, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
                g.drawLine(prevScaledX, prevScaledY, endScaledX, endScaledY);
                _prev.x = _end.x;
                _prev.y = _end.y;
                
                break;

            case OVAL:


                g2.drawOval(txo, tyo, w, h);

                break;

            case RECTANGLE:
                int sizeCrayon4 = PaintingOptionWindow.size_s.getValue();
                g2.draw(new RoundRectangle2D.Double(txo, tyo, w, h, sizeCrayon4, sizeCrayon4));
                break;

            case LINE:
                int sizeCrayon2 = PaintingOptionWindow.size_s.getValue();
                g2.setStroke(new BasicStroke(sizeCrayon2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
                if (strict) {
                    int ws = _end.x - _start.x;
                    int hs = _end.y - _start.y;
                    int ch = Math.abs((int) (Math.atan2(_end.y - _start.y, _end.x - _start.x) / (Math.PI / 180)));
                    int vch = 180;

                    if (ch <= 22 && ch >= 0) {
                        vch = 0;
                    } else if (ch > 22 && ch <= 45) {
                        vch = 45;
                    } else if (ch > 45 && ch <= 67) {
                        vch = 45;
                    } else if (ch > 67 && ch <= 90) {
                        vch = 90;
                    } else if (ch > 90 && ch <= 112) {
                        vch = 90;
                    } else if (ch > 112 && ch <= 135) {
                        vch = 45;
                    } else if (ch > 135 && ch <= 157) {
                        vch = 45;
                    } else if (ch > 157 && ch <= 180) {
                        vch = 180;
                    }

                    if (vch == 0) {
                        g2.drawLine(_start.x, _start.y, _end.x, _start.y);
                    } else if (vch == 45) {
                        g2.drawLine(_start.x, _start.y, _end.x, _end.y);
                    } else if (vch == 90) {
                        g2.drawLine(_start.x, _start.y, _start.x, _end.y);
                    } else if (vch == 180) {
                        g2.drawLine(_start.x, _start.y, _end.x, _start.y);
                    }
                } else {

                    g2.drawLine(_start.x, _start.y, _end.x, _end.y);
                }
                break;

            case CURVE:

                int sizeCrayon6 = PaintingOptionWindow.size_s.getValue();
                g2.setStroke(new BasicStroke(sizeCrayon6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));

                int angleDepart = PaintingOptionWindow.startAngle_s.getValue();
                int angleArc = PaintingOptionWindow.arcAngle_s.getValue();

                if (PaintingOptionWindow.filledArc.isSelected()) {
                    g2.fillArc(txo, tyo, w, h, angleDepart, angleArc);
                } else {
                    g2.drawArc(txo, tyo, w, h, angleDepart, angleArc);
                }


                break;

            case FRECTANGLE:

                int sizeCrayon5 = PaintingOptionWindow.size_s.getValue();
                RoundRectangle2D.Double r = new RoundRectangle2D.Double(txo, tyo, w, h, sizeCrayon5, sizeCrayon5);

                Area a = new Area(r);
                if (gradientpaint && COLORCHOOSEN == ColorWindow.uColor2) {
                    g2.setPaint(getGradient(25, txo, tyo, xf, yf));
                    g2.fill(a);
                    g2.setPaint(ColorWindow.uColor1);
                    g2.draw(a);
                } else if (gradientpaint && COLORCHOOSEN == ColorWindow.uColor1) {
                    g2.setPaint(ColorWindow.uColor1);
                    g2.fill(a);
                    g2.setPaint(getGradient(25, txo, tyo, xf, yf));
                    g2.draw(a);
                } else if (COLORCHOOSEN == ColorWindow.uColor1 && stroke) {
                    g2.setPaint(COLORCHOOSEN);
                    g2.fill(a);
                    g2.setPaint(ColorWindow.uColor2);
                    g2.draw(a);
                } else if (COLORCHOOSEN == ColorWindow.uColor2 && stroke) {
                    g2.setPaint(COLORCHOOSEN);
                    g2.fill(a);
                    g2.setPaint(ColorWindow.uColor1);
                    g2.draw(a);
                } else if (!stroke) {
                    g2.fillRoundRect(txo, tyo, w, h, sizeCrayon5, sizeCrayon5);
                }
                break;

            case FOVAL:

                Ellipse2D e = new Ellipse2D.Float(txo, tyo, w, h);
                Area ae = new Area(e);
                if (gradientpaint && COLORCHOOSEN == ColorWindow.uColor2) {
                    g2.setPaint(getGradient(25, txo, tyo, xf, yf));
                    g2.fill(ae);
                    g2.setPaint(ColorWindow.uColor1);
                    g2.draw(ae);
                } else if (gradientpaint && COLORCHOOSEN == ColorWindow.uColor1) {
                    g2.setPaint(ColorWindow.uColor1);
                    g2.fill(ae);
                    g2.setPaint(getGradient(25, txo, tyo, xf, yf));
                    g2.draw(ae);
                } else if (COLORCHOOSEN == ColorWindow.uColor1 && stroke) {
                    g2.setPaint(COLORCHOOSEN);
                    g2.fill(ae);
                    g2.setPaint(ColorWindow.uColor2);
                    g2.draw(ae);
                } else if (COLORCHOOSEN == ColorWindow.uColor2 && stroke) {
                    g2.setPaint(COLORCHOOSEN);
                    g2.fill(ae);
                    g2.setPaint(ColorWindow.uColor1);
                    g2.draw(ae);
                } else if (!stroke) {
                    g2.fillOval(txo, tyo, w, h);
                }
                break;

            case GRADIENT:
                if (release) {

                    g2.setPaint(getGradient(w+h,strictsx,strictsy,strictex,strictey));
                    g2.fillRect(0, 0, this.getWidth(), this.getHeight());

                } else {

                    g2.setStroke(new BasicStroke(1F));
                    g2.setXORMode(Color.white);

                    if (strict) {
                        int ws = _end.x - _start.x;
                        int hs = _end.y - _start.y;
                        int ch = Math.abs((int) (Math.atan2(_end.y - _start.y, _end.x - _start.x) / (Math.PI / 180)));
                        int vch = 180;

                        if (ch <= 22 && ch >= 0) {
                            vch = 0;
                        } else if (ch > 22 && ch <= 45) {
                            vch = 45;
                        } else if (ch > 45 && ch <= 67) {
                            vch = 45;
                        } else if (ch > 67 && ch <= 90) {
                            vch = 90;
                        } else if (ch > 90 && ch <= 112) {
                            vch = 90;
                        } else if (ch > 112 && ch <= 135) {
                            vch = 45;
                        } else if (ch > 135 && ch <= 157) {
                            vch = 45;
                        } else if (ch > 157 && ch <= 180) {
                            vch = 180;
                        }

                        if (vch == 0 || vch ==180) {
                            g2.drawLine(_start.x, _start.y, _end.x, _start.y);
                            strictsx = _start.x; strictsy=_start.y; strictex=_end.x; strictey=_start.y;
                        } else if (vch == 45) {
                            g2.drawLine(_start.x, _start.y, _end.x, _end.y);
                            strictsx = _start.x; strictsy=_start.y; strictex=_end.x; strictey=_end.y;
                        } else if (vch == 90) {
                            g2.drawLine(_start.x, _start.y, _start.x, _end.y);
                            strictsx = _start.x; strictsy=_start.y; strictex=_start.x; strictey=_end.y;
                        } 
                    } else {
                        g2.drawLine(_start.x, _start.y, _end.x, _end.y);
                            strictsx = _start.x; strictsy=_start.y; strictex=_end.x; strictey=_end.y;
                    }
                }

                break;

            case GOMME:

                int p = 10 + PaintingOptionWindow.size_s.getValue();

                for (int ws = 0; ws < p; ws++) {
                    for (int hs = 0; hs < p; hs++) {
                        if (endScaledX + ws < _bufImage.getWidth() && endScaledY + hs < _bufImage.getHeight()
                                && endScaledX + ws >= 0 && endScaledY + hs >= 0) {
                            _bufImage.setRGB(endScaledX + ws, endScaledY + hs, makeRGBA(0, 0, 0, 0));
                        }
                    }
                }

                break;

            case POT:

                if (release && COLORCHOOSEN.getRGB() != this.getColorAt(_bufImage, endScaledX, endScaledY)) // this.floodFill(_bufImage, endScaledX,endScaledY, COLORCHOOSEN.getRGB());
                {
                    _bufImage = iterFloodFill(_bufImage, endScaledX, endScaledY, COLORCHOOSEN.getRGB());
                }
                break;

            case SELECT:
                if (release) {

                    if (txo <= 0) {
                        w = w + txo;
                        txo = 0;
                    }
                    if (tyo <= 0) {
                        h = h + tyo;
                        tyo = 0;
                    }
                    if (txo + w > WSIZE) {
                        w = WSIZE - txo;
                    }
                    if (tyo + h > HSIZE) {
                        h = HSIZE - tyo;
                    }

                    selectPane = new SelectionImage(_bufImage.getSubimage(txo, tyo, w, h), txo, tyo, this, w, h);
                    SToolBar.cursorButton.doClick();
                    this.increaseZoom();
                    this.decreaseZoom();

                    /*for (int ws = txo; ws < txo+w; ws++) {
                    for (int hs = tyo; hs < tyo+h; hs++) {     
                    if (ws<_bufImage.getWidth() && hs<_bufImage.getHeight()  && ws >=0 && hs >=0 )
                    _bufImage.setRGB(ws,hs,PaintPanel.makeRGBA(0,0,0,0));
                    }
                    }*/

                } else {
                    g2.setColor(Color.black);
                    g2.setXORMode(Color.white);
                    g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, new float[]{2f}, 0f));
                    g2.drawRect(txo, tyo, w, h);
                    stx = txo;
                    sty = tyo;
                }
                break;


            case TEXTE:

                if (release) {

                    tf = new JTextField();
                    sizeText = h;
                    tf.setForeground(COLORCHOOSEN);
                    tf.setFont(PaintingOptionWindow.getFontThis(h, " "));
                    tf.setPreferredSize(new Dimension(w, h));
                    //tf.setBorder(BorderFactory.createDashedBorder(Color.black));
                    tf.setBorder(BorderFactory.createLineBorder(Color.black));
                    tf.setOpaque(PaintingOptionWindow.b_opaque);
                    tf.setSize(w, h);
                    tf.setLocation(txo, tyo);
                    this.setLayout(null);
                    this.add(tf);

                    tf.addKeyListener(new KeyListener() {

                        public void keyTyped(KeyEvent e) {
                        }

                        public void keyPressed(KeyEvent e) {

                            int scaledTfx = Math.round(tf.getX() * (float) _bufImage.getWidth() / cachedWidth);
                            int scaledTfy = Math.round(tf.getY() * (float) _bufImage.getWidth() / cachedWidth);

                            if (e.getKeyCode() == 37) {
                                tf.setLocation(scaledTfx - 2, scaledTfy);
                            } else if (e.getKeyCode() == 38) {
                                tf.setLocation(scaledTfx, scaledTfy - 2);
                            } else if (e.getKeyCode() == 39) {
                                tf.setLocation(scaledTfx + 2, scaledTfy);
                            } else if (e.getKeyCode() == 40) {
                                tf.setLocation(scaledTfx, scaledTfy + 2);
                            }

                        }

                        public void keyReleased(KeyEvent e) {
                        }
                    });

                } else {
                    zoom = 1;
                    rescaleImage();

                    g2.setColor(ColorWindow.uColor1);
                    g2.setXORMode(Color.white);
                    g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, new float[]{2f}, 0f));
                    g2.drawRect(txo, tyo, w, h);
                }
                break;

            case PIPETTE:

                int rgb = this.getColorAt(_bufImage, endScaledX, endScaledY);
                String rgb2 = Integer.toHexString(rgb);
                Color c = Color.decode("0x" + rgb2.substring(2, rgb2.length()).toUpperCase());

                if (COLORCHOOSEN == ColorWindow.uColor1) {
                    ColorWindow.ROWCOLOR = 1;
                    ColorWindow.userDefButton.setBackground(c);
                    ColorWindow.redSlider.setValue(getRed(rgb));
                    ColorWindow.greenSlider.setValue(getGreen(rgb));
                    ColorWindow.blueSlider.setValue(getBlue(rgb));
                    ColorWindow.uColor1 = c;
                } else if (COLORCHOOSEN == ColorWindow.uColor2) {
                    ColorWindow.ROWCOLOR = 2;
                    ColorWindow.userDefButton2.setBackground(c);
                    ColorWindow.redSlider.setValue(getRed(rgb));
                    ColorWindow.greenSlider.setValue(getGreen(rgb));
                    ColorWindow.blueSlider.setValue(getBlue(rgb));
                    ColorWindow.uColor2 = c;
                }
                break;


            default:

                break;
        }
    }

    /**
     * Fonction POT DE PEINTURE
     * @param img Image à traiter
     * @param x coordonée x
     * @param y coordonée y
     * @param NEWRGB Nouvelle couleur à remplir
     * @return L'image finale traitée par l'algorithme du pot de peinture
     */
    public BufferedImage iterFloodFill(BufferedImage img, int x, int y, final int NEWRGB) {

        final int OLDRGB = this.getColorAt(img, x, y);
        //Arraylist destiné à stocker les coordonnées des pixels à traiter
        ArrayList<Point> pile = new ArrayList<Point>();

        //On récupere les dimenssion du tableau 
        int wid = img.getWidth();
        int hei = img.getHeight();

        //On insert les coordonées de départ dans la pile 
        pile.add(new Point(x, y));

        Point pt;

        while (pile.size() > 0) {

            pt = pile.remove(0);
            x = pt.x;
            y = pt.y;

            //Si le pixel courent est bien de la couleur cible
            if (this.getColorAt(img, x, y) == OLDRGB) {

                //On applique le changement de couleur aux coordonnées courentes
                img.setRGB(x, y, NEWRGB);

                /*Puis on teste les pixels nord sud est ouest,
                 * si il faut y appliquer la nouvelle couleur alors on empile les
                 * coordonnées du pixel
                 */
                if (x + 1 < wid && img.getRGB(x + 1, y) == OLDRGB) {
                    pile.add(new Point(x + 1, y));
                }
                if (y + 1 < hei && img.getRGB(x, y + 1) == OLDRGB) {
                    pile.add(new Point(x, y + 1));
                }
                if (x > 0 && img.getRGB(x - 1, y) == OLDRGB) {
                    pile.add(new Point(x - 1, y));
                }
                if (y > 0 && img.getRGB(x, y - 1) == OLDRGB) {
                    pile.add(new Point(x, y - 1));
                }
            }
        }

        return img;
    }

    /**
     * Renvoi un dégradé
     * @param w longueur du dégardé
     * @param sx coordonée x du point de départ du dégradé
     * @param sy coordonée y du point de départ du dégardé
     * @param ex coordonée y du point d'arrivée du dégradé
     * @param ey coordonée x du point d'arrivée du dégradé
     * @return un dégradé
     */
    public MultipleGradientPaint getGradient(int w, int sx, int sy, int ex, int ey) {
        Point2D start = new Point2D.Float(sx, sy);
        Point2D end = new Point2D.Float(ex, ey);
        Point2D center = new Point2D.Float(sx, sy);

        int radius = w;
        float[] dist = GradientPanel.actualGradient.getFractions();
        Color[] colors = GradientPanel.actualGradient.getColors();

        MultipleGradientPaint p = null;

        if (GradientPanel.typeg.equals("linear")) {
            p = new LinearGradientPaint(start, end, dist, colors);
        } else if (GradientPanel.typeg.equals("bilinear")) {
            p = new LinearGradientPaint(start, end, dist, colors, CycleMethod.REFLECT);
        } else if (GradientPanel.typeg.equals("radial")) {
            p = new RadialGradientPaint(center, radius, dist, colors);
        } else if (GradientPanel.typeg.equals("circles")) {
            p = new RadialGradientPaint(center, radius, dist, colors, CycleMethod.REFLECT);
        } else if (GradientPanel.typeg.equals("relinear")) {
            p = new LinearGradientPaint(start, end, dist, colors, CycleMethod.REPEAT);
        } else if (GradientPanel.typeg.equals("reradial")) {
            p = new RadialGradientPaint(center, radius, dist, colors, CycleMethod.REPEAT);
        }
        return p;
    }

    /**
     * Action suivant la pression du clic sur l'espace de dessin
     * @param e Evénement lié à la souris sur lespace de dessin
     */
    public void mousePressed(MouseEvent e) {

        _state = State.DRAGGING;   // On drag !

        _start = e.getPoint();     // sauvegarde du point de départ du dessin
        _prev = _start;
        _end = _start;           // sauvegarde du point d'arrivé

        // si on clique n'importe ou on vide le champ texte
        if (tf != null) {
            tf.setVisible(false);
        }

        COLORCHOOSEN = ColorWindow.getMouseColor(e);

        if (_shape == Shape.CURSOR && selectPane == null) {
            selectAll();
        }

        if (selectPane != null) {


            Rectangle r = new Rectangle(selectPane.xo - 2, selectPane.yo - 2, cachedWidth2 + 2, cachedHeight2 + 2);

            if (r.contains(_start)) {
                selectionOnly = true;

            } else {
                dessinerSelection(true);
                selectionOnly = false;
            }
        }



    }

    /**
     * Fonction qui séléctionne tout l'image (bascule l'outil sur "séléction" automatiqument)
     */
    public void selectAll() {
        selectPane = new SelectionImage(_bufImage, 0, 0, this, _bufImage.getWidth(), _bufImage.getHeight());
        Calques.calques.setElementAt(this.newCopyAlpha(_bufImage, type), Calques.current);
        SToolBar.cursorButton.doClick();
        SToolBar.refresh(this);
        this.increaseZoom();
        this.decreaseZoom();
    }

    /**
     * Fonction qui dessine la séléction lors du clic en dehors de la séléction
     * @param deleteAfter true si on veut supprimer la séléction après l'opération, false sinon
     */
    public void dessinerSelection(boolean deleteAfter) {
        int vx = Math.round(selectPane.xo * (float) selectPane.ws / cachedWidth2);
        int vy = Math.round(selectPane.yo * (float) selectPane.hs / cachedHeight2);

        Graphics2D bg = (Graphics2D) _bufImage.getGraphics();
        bg.drawImage(selectPane.simg, vx, vy, this);

        if (deleteAfter) {
            selectPane = null;
        }

        Calques.historiques.get(Calques.current).add(this.newCopy(_bufImage, PaintPanel.type));
        CalquesWindow.refreshCalques(layers.calques);
    }

    /**
     * Supprime la séléction
     * @param deleteAfter true si on veut supprimer la séléction après l'opéraiton, false sinon
     */
    public void removeImageSelection(boolean deleteAfter) {
        if (selectPane != null) {
            selectPane.simg = this.newCopyAlpha(selectPane.simg, type);
            if (deleteAfter) {
                selectPane = null;
            }
        }
    }

    /**
     * Action suivant la pression du clic ET le déplacement sur l'espace de dessin
     * @param e Evénement lié à la souris sur lespace de dessin
     */
    public void mouseDragged(MouseEvent e) {
        _state = State.DRAGGING;   // On drag pour une forme

        _end = e.getPoint();       // Point final de drag (change lors du redimensonnement)

        if (_shape == Shape.CURSOR) {
            locx = e.getX();
            locy = e.getY();
        }

        if (selectPane != null) {

            if (selectionOnly) {

                if (selectPane.simg.getWidth() == _bufImage.getWidth() && selectPane.simg.getHeight() == _bufImage.getHeight()) {
                    selectPane.xo = e.getX() - _start.x;
                    selectPane.yo = e.getY() - _start.y;
                } else {
                    selectPane.xo = e.getX();
                    selectPane.yo = e.getY();
                }
            }
        }

        this.repaint();            // Une fois les changements effectués on repeint le dessin
    }

    /**
     * Action suivant le relâchement du clic sur l'espace de dessin
     * @param e Evénement lié à la souris sur lespace de dessin
     */
    public void mouseReleased(MouseEvent e) {
        //... Si on relache la souris on dessine dans le vrai buffer
        //    
        _end = e.getPoint();
        if (_state == State.DRAGGING) {
            _state = State.IDLE;

            //... On dessine dans le buffer final
            drawCurrentShape(_bufImage.createGraphics(), true);
            this.repaint();
        }


        if (_shape != Shape.PIPETTE && _shape != Shape.SELECT) {
            Calques.historiques.get(Calques.current).add(this.newCopy(_bufImage, PaintPanel.type));
            CalquesWindow.refreshCalques(layers.calques);
        }



    }

    /**
     * Action suivant la pression d'une touche du clavier sur l'espace de dessin
     * @param e Evénement lié au clavier sur lespace de dessin
     */
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 16) {
            strict = true;
        } else {
            strict = false;
        }

        if (e.getKeyCode()==127)
            SMenuBar.supprimer.doClick();
        
    }

    /**
     * Action suivant le relâchement d'une touche du clavier sur l'espace de dessin
     * @param e Evénement lié au clavier sur lespace de dessin
     */
    public void keyReleased(KeyEvent e) {
        strict = false;
    }

    /**
     * Action suivant la saisie d'une touche du clavier sur l'espace de dessin
     * @param e Evénement lié au clavier sur lespace de dessin
     */
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Action suivant le déplacement de la souris sur l'espace de dessin
     * @param e Evénement lié à la souris sur lespace de dessin
     */
    public void mouseMoved(MouseEvent e) {

        setCursor(SToolBar.curs);
        mouse = e.getPoint();
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
       
    }

    /**
     * Position les options de rendu de l'image à vrai ou faux
     * @param g Object graphique lié à l'image présente sur l'espace de dessin 
     */
    public void getRenderingHints(Graphics2D g) {
        if (bantialiasing) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        } else {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }
        if (btextantialiasing) {
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        } else {
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        }
        if (brendering) {
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        } else {
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        }
        if (bcolorrendering) {
            g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        } else {
            g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        }
        if (binterpolation) {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        } else {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        }
        if (balphainterpolation) {
            g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        } else {
            g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        }
        if (bdithering) {
            g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        } else {
            g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
        }
        if (bfractionalmetrics) {
            g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        } else {
            g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        }
        if (bstrokecontrol) {
            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        } else {
            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        }
    }
    // Partie traitement de l'IMAGE
    private String lastFiltre = null;
    private float lastFiltreMatrice[] = null;
    private String lastRotation = null;
    private BufferedImage img = null;
    private double scale = 1;

    /**
     * Change l'image du calque courant associé à l'espace de dessin
     * @param b Nouvelle image à associer à l'espace de dessin
     */
    public void setImage(BufferedImage b) {
        _bufImage = b;
        this.setSize(this.getWidth() + 1, this.getHeight());
        this.setSize(this.getWidth() - 1, this.getHeight());
    }

    /**
     * Renvoi l'image du calque courant
     * @return l'image du calque courant
     */
    public BufferedImage getImage() {
        return _bufImage;
    }

    /**
     * Renvoi le nom du dernier filtre utilisé
     * @return le nom du dernier filtre utilisé
     */
    public String getLastFiltre() {
        return this.lastFiltre;
    }

    /**
     * Renvoi la matrice utilisé par le dernier filtre utilisé
     * @return la matrice utilisé par le dernier filtre utilisé
     */
    public float[] getLastFiltreMatrice() {
        return this.lastFiltreMatrice;
    }

    /**
     * Renvoi la dernière rotation utilisée
     * @return la dernière rotiation utilisée
     */
    public String getLastRotation() {
        return lastRotation;
    }

    /**
     * Fonction principale qui gère l'application des filtres sur l'image du calque courant
     * @param imgArg Image à traiter
     * @param type Type de filtre
     * @param matriceArgs Matrice de convolution si besoin
     */
    public void filtre(BufferedImage imgArg, String type, float[] matriceArgs) {

        int wNew = this.getImage().getWidth();
        int hNew = this.getImage().getHeight();
        BufferedImage tmp = new BufferedImage(wNew, hNew, BufferedImage.TYPE_INT_ARGB);
        if (type.equals("z+")) {
            scale += 0.2;
            AffineTransform atz = AffineTransform.getScaleInstance(scale, scale);
            AffineTransformOp atzop = new AffineTransformOp(atz, AffineTransformOp.TYPE_BICUBIC);
            atzop.filter(this.getImage(), tmp);
            this.setPreferredSize(new Dimension(this.getImage().getWidth(), this.getImage().getHeight()));
            this.setSize(new Dimension(this.getImage().getWidth(), this.getImage().getHeight()));
        } else if (type.equals("z-")) {
            scale -= 0.2;
            AffineTransform a90 = AffineTransform.getScaleInstance(scale, scale);
            AffineTransformOp op180 = new AffineTransformOp(a90, AffineTransformOp.TYPE_BICUBIC);
            op180.filter(this.getImage(), tmp);
        } else if (type.equals("r90")) {
            AffineTransform a90 = AffineTransform.getRotateInstance(Math.toRadians(90), wNew / 2, hNew / 2);
            AffineTransformOp op90 = new AffineTransformOp(a90, AffineTransformOp.TYPE_BILINEAR);
            op90.filter(this.getImage(), tmp);
            lastRotation = "r90";
        } else if (type.equals("r180")) {
            AffineTransform a180 = AffineTransform.getRotateInstance(Math.toRadians(180), wNew / 2, hNew / 2);
            AffineTransformOp op180 = new AffineTransformOp(a180, AffineTransformOp.TYPE_BILINEAR);
            op180.filter(this.getImage(), tmp);
            lastRotation = "r180";
        } else if (type.equals("r270")) {
            AffineTransform a270 = AffineTransform.getRotateInstance(Math.toRadians(270), wNew / 2, wNew / 2);
            AffineTransformOp op270 = new AffineTransformOp(a270, AffineTransformOp.TYPE_BILINEAR);
            op270.filter(this.getImage(), tmp);
            lastRotation = "r270";
        } else if (type.equals("hsymetry")) {

            tmp = hSymetry(imgArg);

        } else if (type.equals("vsymetry")) {

            tmp = vSymetry(imgArg);

        } else {
            lastFiltre = type;

            for (int col = 0; col < wNew; col++) {
                for (int ligne = 0; ligne < hNew; ligne++) {
                    tmp.setRGB(col, ligne, (int) getKernel(col, ligne, imgArg, type, matriceArgs));
                }
            }

        }

        this.setImage(tmp);
        layers.setAt(tmp, Calques.current);
        this.setSize(this.getWidth() + 1, this.getHeight());
        this.setSize(this.getWidth() - 1, this.getHeight());

        Calques.historiques.get(Calques.current).add(this.newCopy(_bufImage, PaintPanel.type));
        CalquesWindow.refreshCalques(layers.calques);
    }

    /**
     * Retourne la couleur pointée aux coordonées x,y de l'image passée en paramètre
     * @param tmp Image à analyser
     * @param x coordonée x
     * @param y coordonée y
     * @return La couleur aux coordonée x,y de l'image passée en paramètre
     */
    public int getColorAt(BufferedImage tmp, int x, int y) {
        if ((x >= 0 && x < tmp.getWidth()) && (y >= 0 && y < tmp.getHeight())) {
            return tmp.getRGB(x, y);
        } else {
            return COLORCHOOSEN.getRGB();
        }

    }

    /**
     * Retourne le nouveau noyau pour l'opération de filtre par matrice de convolution
     * @param x coordonée x 
     * @param y coordonée y 
     * @param imgT Image à traiter
     * @param type Type de filtre
     * @param matriceArgs Matrice de convolution à utiliser
     * @return la nouvelle composante couleur à appliquer afin d'obtenir l'effet du filtre
     */
    private float getKernel(int x, int y, BufferedImage imgT, String type, float[] matriceArgs) {

        float[] flou = {1, 1, 1, 1, 1, 1, 1, 1, 1};
        float[] flouGaussien = {1f / 16f, 2f / 16f, 1f / 16f, 2f / 16f, 4f / 16f, 2f / 16f, 1f / 16f, 2f / 16f, 1f / 16f};
        float[] contours = {-1f, 0f, 1f, -2f, 0f, 2f, -1f, 0f, 1f};
        float[] none = {0, 0, 0, 0, 1, 0, 0, 0, 0};
        float[] repoussage = {-2, -1, 0, -1, 1, 1, 0, 1, 2};
        float[] contrastep = {0, -1, 0, -1, 5, -1, 0, -1, 0};
        float[] ameliorationbords = {0, 0, 0, -1, 1, 0, 0, 0, 0};
        float[] luminosite = {0.5f, 0.5f, 1, 0.5f, 0.5f, 1, 0.5f, 0.5f, 1};
        float[] estampage = {-2, -1, 0, -1, 1, 1, 0, 1, 2};
        float[] roberts = {1, 0, 0, 0, -1, 0, 0, 0, 0};
        float[] prewit = {-1, 0, 1, -1, 0, 1, -1, 0, 1};
        float[] sobel = {-1, 0, 1, -2, 0, 2, -1, 0, 1};
        float[] floumvt = {0,0,1,0,0,0,1,0,0};

        float[] matrice = null;

        if (type.equals("flouGaussien")) {
            matrice = flouGaussien;
        } else if (type.equals("flou")) {
            matrice = flou;
        } else if (type.equals("luminosite")) {
            matrice = luminosite;
        } else if (type.equals("contours")) {
            matrice = contours;
        } else if (type.equals("repoussage")) {
            matrice = repoussage;
        } else if (type.equals("contrastep")) {
            matrice = contrastep;
        } else if (type.equals("ameliorationbords")) {
            matrice = ameliorationbords;
        } else if (type.equals("none")) {
            matrice = none;
        } else if (type.equals("estampage")) {
            matrice = estampage;
        } else if (type.equals("roberts")) {
            matrice = roberts;
        } else if (type.equals("prewit")) {
            matrice = prewit;
        } else if (type.equals("sobel")) {
            matrice = sobel;
        } 
        else if (type.equals("floumvt")) {
            matrice = floumvt;
        } 
        
        else {
            matrice = none;
        }

        if (matriceArgs != null) {
            matrice = matriceArgs;
        }


        lastFiltreMatrice = matrice;

        // Calcul des coordonnées de la convolution
        int cm1 = x - 1;
        int cp1 = x + 1;
        int lm1 = y - 1;
        int lp1 = y + 1;

        // Bouclage de pixels bordures
        if (cm1 < 0) {
            cm1 = imgT.getWidth() - 1;
        }
        if (cp1 >= imgT.getWidth()) {
            cp1 = 0;
        }
        if (lm1 < 0) {
            lm1 = imgT.getHeight() - 1;
        }
        if (lp1 >= imgT.getHeight()) {
            lp1 = 0;
        }


        int convoCol[] = new int[]{cm1, x, cp1};
        int covoLigne[] = new int[]{lm1, y, lp1};

        int Rmedian = 0, Gmedian = 0, Bmedian = 0, Amedian = 0;
        ArrayList<Integer> medians = new ArrayList();

        int nb = 0, Rconvo = 0, Gconvo = 0, Bconvo = 0, Aconvo = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Rconvo += matrice[nb] * getRed(imgT.getRGB(convoCol[j], covoLigne[i]));
                Gconvo += matrice[nb] * getGreen(imgT.getRGB(convoCol[j], covoLigne[i]));
                Bconvo += matrice[nb] * getBlue(imgT.getRGB(convoCol[j], covoLigne[i]));
                Aconvo += matrice[nb++] * getAlpha(imgT.getRGB(convoCol[j], covoLigne[i]));

                medians.add(imgT.getRGB(convoCol[j], covoLigne[i]));

            }
        }

        Collections.sort(medians);
        int rgbs = medians.get((int) Math.floor(medians.size() / 2));

        Rconvo = getNorme(Rconvo, matrice);
        Gconvo = getNorme(Gconvo, matrice);
        Bconvo = getNorme(Bconvo, matrice);
        Aconvo = getNorme(Aconvo, matrice);

        if (Rconvo < 0) {
            Rconvo = 0;
        } else if (Rconvo > 255) {
            Rconvo = 255;
        }
        if (Gconvo < 0) {
            Gconvo = 0;
        } else if (Gconvo > 255) {
            Gconvo = 255;
        }
        if (Bconvo < 0) {
            Bconvo = 0;
        } else if (Bconvo > 255) {
            Bconvo = 255;
        }
        if (Aconvo < 0) {
            Aconvo = 0;
        } else if (Aconvo > 255) {
            Aconvo = 255;
        }

        if (type.equals("median")) {
            return makeRGB(getRed(rgbs), getGreen(rgbs), getBlue(rgbs), getAlpha(rgbs));
        } else {
            return makeRGB(Rconvo, Gconvo, Bconvo, Aconvo);
        }

    }

    /**
     * Renvoi la norme d'une matrice de convolution
     * @param v Composante couleur
     * @param mat Matrice utilisée
     * @return la norme d'une matrice de convolution
     */
    private int getNorme(int v, float[] mat) {
        int n = 0;
        for (int i = 0; i < 9; i++) {
            n += mat[i];
        }

        if (n < 0) {
            return v / (-1 * n);
        } else if (n == 0) {
            return v / 1;
        } else {
            return v / n;
        }
    }

    /**
     * Retourne la composante bleu d'un entier RGB
     * @param RGB Couleur à traiter
     * @return la composante bleu d'un entier RGB
     */
    public static int getBlue(int RGB) {
        return RGB & 0xFF;
    }

    /**
     * Retourne la composante verte d'un entier RGB
     * @param RGB Couleur à traiter
     * @return la composante verte d'un entier RGB
     */
    public static int getGreen(int RGB) {
        return (RGB >> 8) & 0xFF;
    }

    /**
     * Retourne la composante rouge d'un entier RGB
     * @param RGB Couleur à traiter
     * @return la composante rouge d'un entier RGB
     */
    public static int getRed(int RGB) {
        return (RGB >> 16) & 0xFF;
    }

    /**
     * Retourne la composante alpha d'un entier RGB
     * @param RGB Couleur à traiter
     * @return la composante alpha d'un entier RGB
     */
    public static int getAlpha(int RGB) {
        return (RGB >> 24) & 0xFF;
    }

    /**
     * Retourne un entier RGB suivant les composantes envoyées en paramètre (sans transparence)
     * @param red Composante rouge
     * @param green Composante verte
     * @param blue Composante bleue
     * @param alpha Composante alpha
     * @return un entier RGB suivant les composantes envoyées en paramètre (sans transparence)
     */
    public static int makeRGB(int red, int green, int blue, int alpha) {
        return ((blue & 0xFF) + ((green & 0xFF) << 8) + ((red & 0xFF) << 16)) + ((255 & 0xFF) << 24);
    }

    /**
     * Retourne un entier RGB suivant les composantes envoyées en paramètre (avec transparence)
     * @param red Composante rouge
     * @param green Composante verte
     * @param blue Composante bleue
     * @param alpha Composante alpha
     * @return un entier RGB suivant les composantes envoyées en paramètre (avec transparence)
     */
    public static int makeRGBA(int red, int green, int blue, int alpha) {
        return ((blue & 0xFF) + ((green & 0xFF) << 8) + ((red & 0xFF) << 16)) + ((alpha & 0xFF) << 24);
    }

    /**
     * Retourne la copie d'une image
     * @param tmpBuf Image à copier
     * @return la copie d'une image
     */
    public BufferedImage copy(BufferedImage tmpBuf) {
        for (int col = 0; col < this.getImage().getWidth(); col++) {
            for (int ligne = 0; ligne < this.getImage().getHeight(); ligne++) {
                tmpBuf.setRGB(col, ligne, this.getImage().getRGB(col, ligne));
            }
        }
        return tmpBuf;
    }

    /**
     * Retourne la copie d'une image suivant un type d'encodage de couleur
     * @param tmpBuf Image à copier
     * @param type Type d'encodage couleur à utiliser
     * @return la copie d'une image
     */
    public static BufferedImage newCopy(BufferedImage tmpBuf, int type) {
        BufferedImage tmp = new BufferedImage(tmpBuf.getWidth(), tmpBuf.getHeight(), type);
        for (int col = 0; col < tmpBuf.getWidth(); col++) {
            for (int ligne = 0; ligne < tmpBuf.getHeight(); ligne++) {
                tmp.setRGB(col, ligne, tmpBuf.getRGB(col, ligne));
            }
        }
        return tmp;
    }

    /**
     * Retourne une image vide (transparente) aux même dimensions que l'image passée en paramètre suivant un type
     * d'encodage de couleur passé en paramètre
     * @param tmpBuf Image à copier (pour les dimensions)
     * @param type Type d'encodage de couleur à utiliser
     * @return une image vide (transparente) aux même dimensions que l'image passée en paramètre suivant un type
     */
    public static BufferedImage newCopyAlpha(BufferedImage tmpBuf, int type) {
        BufferedImage tmp = new BufferedImage(tmpBuf.getWidth(), tmpBuf.getHeight(), type);
        for (int col = 0; col < tmpBuf.getWidth(); col++) {
            for (int ligne = 0; ligne < tmpBuf.getHeight(); ligne++) {
                tmp.setRGB(col, ligne, makeRGBA(0, 0, 0, 0));
            }
        }
        return tmp;
    }

    /**
     * Retourne la sous-image d'une image de base
     * @param tmpBuf Image de base
     * @param x coordonée x où commence la sous-image par rapport à l'image de base
     * @param y coordonée x où commence la sous-image par rapport à l'image de base
     * @param w longueur de la sous-image
     * @param h largeur de la sous-image
     * @param type Type d'encodage couleur à utiliser
     * @return la sous-image d'une image de base
     */
    public static BufferedImage newCopyBetweenFixed(BufferedImage tmpBuf, int x, int y, int w, int h, int type) {
        BufferedImage tmp = new BufferedImage(w, h, type);
        for (int col = x; col < w; col++) {
            for (int ligne = y; ligne < h; ligne++) {
                tmp.setRGB(col, ligne, tmpBuf.getRGB(col, ligne));
            }
        }
        return tmp;
    }

    /**
     * Retourne une image symétrique verticale de l'image passée en paramètre
     * @param imgArg Image à traiter
     * @return une image symétrique verticale de l'image passée en paramètre
     */
    private BufferedImage vSymetry(BufferedImage imgArg) {
        BufferedImage tmp = new BufferedImage(imgArg.getWidth(), imgArg.getHeight(), type);
        for (int ligne = 0; ligne < imgArg.getWidth(); ligne++) {
            int finh = imgArg.getHeight() - 1;
            for (int col = 0; col < imgArg.getHeight(); col++) {
                tmp.setRGB(ligne, col, imgArg.getRGB(ligne, finh--));
            }
        }
        return tmp;

    }

    /**
     * Retourne une image symétrique horizontale de l'image passée en paramètre
     * @param imgArg Image à traiter
     * @return une image symétrique horizontale de l'image passée en paramètre
     */
    private BufferedImage hSymetry(BufferedImage imgArg) {
        BufferedImage tmp = new BufferedImage(imgArg.getWidth(), imgArg.getHeight(), type);
        int h = imgArg.getWidth() - 1;
        for (int ligne = 0; ligne < imgArg.getWidth(); ligne++) {
            for (int col = 0; col < imgArg.getHeight(); col++) {
                tmp.setRGB(ligne, col, imgArg.getRGB(h, col));
            }
            h--;
        }
        return tmp;

    }

    public static BufferedImage newCopyResized(BufferedImage tmpBuf, int newW, int newH, int type, Color color) {
        int ancW = tmpBuf.getWidth(), ancH = tmpBuf.getHeight();
        BufferedImage tmp = new BufferedImage(newW, newH, type);
        for (int col = 0; col < newW; col++) {
            for (int ligne = 0; ligne < newH; ligne++) {
                if (ligne < ancH && col < ancW) {
                    tmp.setRGB(col, ligne, tmpBuf.getRGB(col, ligne));
                } else {
                    tmp.setRGB(col, ligne, COLOR_BACKGROUND.getRGB());
                }
            }
        }
        return tmp;
    }
    // Partie ZOOM
    public static final String ZOOM_CHANGED_PROPERTY = "zoom_level";
    public static final String ZOOM_FACTOR_CHANGED_PROPERTY = "zoom_factor";
    public static final String ZOOM_MAX_CHANGED_PROPERTY = "zoom_max";
    public static final String ZOOM_MIN_CHANGED_PROPERTY = "zoom_min";
    public static final String IMAGE_CHANGED_PROPERTY = "image";
    public double zoom = 1.0;
    private double zoomMax = 6.0;
    private double zoomMin = 0.05;
    private double zoomFactor = 1.1;
    public static boolean zoomEnabled = true;
    public int cachedWidth = 0;
    public int cachedHeight = 0;
    public static int cachedWidth2 = 0;
    public static int cachedHeight2 = 0;
    MouseWheelListener mouseWheelSupport = null;

    /**
     * Test si l'écouteur de la molette est actif
     * @return vrai si l'écouteur de la molette est actif, false sinon
     */
    public boolean isMouseWheelEnabled() {
        return mouseWheelSupport != null;
    }

    /**
     * Rend actif (ou non) l'écouteur de la molette pour le zoom de l'image
     * @param enabled true si on veut activer l'écouteur de la molette pour le zoom de l'image
     */
    public void setMouseWheelEnabled(boolean enabled) {
        if (enabled && mouseWheelSupport == null) {
            mouseWheelSupport = new ZoomSelector();
            addMouseWheelListener(mouseWheelSupport);
        } else if (!enabled && mouseWheelSupport != null) {
            removeMouseWheelListener(mouseWheelSupport);
            mouseWheelSupport = null;
        }
    }

    /**
     * Renvoi le coefficient de zoom utilisé
     * @return le coefficient de zoom utilisé
     */
    public double getZoom() {
        return zoom;
    }

    /**
     * Zoom sur une image avec un coefficient passé en paramètre
     * @param zoom coefficient de zoom à utiliser
     */
    public void setZoom(final double zoom) {
        if (zoom == this.zoom) {
            return;
        }

        double oldValue = this.zoom;

        this.zoom = zoom;
        if (this.zoom > zoomMax) {
            this.zoom = zoomMax;
        } else if (this.zoom < zoomMin) {
            this.zoom = zoomMin;
        }

        firePropertyChange(ZOOM_CHANGED_PROPERTY,
                (double) (oldValue),
                (double) (this.zoom));
        rescaleImage();
    }

    /**
     * Fonction qui permet de dé-zoomer l'image de l'espace de dessin 
     */
    public void decreaseZoom() {
        setZoom(zoom / zoomFactor);
    }

    /**
     * Fonction qui permet de zoomer l'image de l'espace de dessin 
     */
    public void increaseZoom() {
        setZoom(zoom * zoomFactor);
    }

    /**
     * Renvoi le pas du zoom
     * @return le pas du zoom
     */
    public double getZoomFactor() {
        return zoomFactor;
    }

    /**
     * Assigne un nouveau pas de zoom
     * @param zoomFactor nouveau pas de zoom à assigner
     */
    public void setZoomFactor(final double zoomFactor) {
        if (zoomFactor <= 0.0) {
            throw new IllegalArgumentException("zoomFactor ne peut être < 0.0");
        }

        double oldValue = this.zoomFactor;
        this.zoomFactor = zoomFactor;
        firePropertyChange(ZOOM_FACTOR_CHANGED_PROPERTY,
                (double) (oldValue),
                (double) (this.zoomFactor));
    }

    /**
     * Renvoi la valeur de zoom maximale
     * @return la valeur de zoom maximale
     */
    public double getZoomMax() {
        return zoomMax;
    }

    /**
     * Change la valeur du zoom maximal 
     * @param zoomMax Nouvelle valeur pour le zoom maximal
     */
    public void setZoomMax(final double zoomMax) {
        if (zoomMax <= 0.0) {
            throw new IllegalArgumentException("zoomMax ne peut être <= 0.0");
        }
        if (zoomMax < zoomMin) {
            throw new IllegalArgumentException("zoomMax ne peut être < zoomMin");
        }

        if (zoomMax == this.zoomMax) {
            return;
        }

        double oldValue = this.zoomMax;
        this.zoomMax = zoomMax;
        firePropertyChange(ZOOM_MAX_CHANGED_PROPERTY,
                (double) (oldValue),
                (double) (this.zoomFactor));
        setZoom(zoom);
    }

    /**
     * Renvoi la valeur de zoom minimale
     * @return la valeur de zoom minimale
     */
    public double getZoomMin() {
        return zoomMin;
    }

    /**
     * Change la valeur du zoom minimale 
     * @param zoomMin Nouvelle valeur pour le zoom minimale
     */
    public void setZoomMin(final double zoomMin) {
        if (zoomMin <= 0.0) {
            throw new IllegalArgumentException("zoomMin ne peut être <= 0.0");
        }
        if (zoomMin > zoomMin) {
            throw new IllegalArgumentException("zoomMin ne peut être > zoomMax");
        }

        if (zoomMin == this.zoomMin) {
            return;
        }

        double oldValue = this.zoomMin;
        this.zoomMin = zoomMin;
        firePropertyChange(ZOOM_MIN_CHANGED_PROPERTY,
                (double) (oldValue),
                (double) (this.zoomFactor));
        setZoom(zoom);
    }

    /**
     * Associe une nouvelle image zoomé à l'espace de travail
     * @param image Image à traiter
     */
    public void setImage(final Image image) {
        Image oldValue = this._bufImage;
        this._bufImage = (BufferedImage) image;
        if (image != null) {
            this.cachedWidth = image.getWidth(this);
            this.cachedHeight = image.getHeight(this);
        }
        firePropertyChange(IMAGE_CHANGED_PROPERTY,
                oldValue,
                this._bufImage);

        if (selectPane != null) {
            Image oldValue2 = selectPane.simg;
            selectPane.simg = (BufferedImage) image;
            if (image != null) {
                this.cachedWidth2 = image.getWidth(this);
                this.cachedHeight2 = image.getHeight(this);
            }
            firePropertyChange(IMAGE_CHANGED_PROPERTY,
                    oldValue,
                    selectPane.simg);
        }

        repaint();
    }

    /**
     * Renvoi la nouvelle dimension de l'image une fois zoomée
     * @return la nouvelle dimension de l'image une fois zoomée
     */
    public Dimension getPreferredSize() {
        return new Dimension(cachedWidth, cachedHeight);
    }

    /**
     * Met à jour l'image en appliquant la nouvelle échelle fixée
     */
    public void rescaleImage() {

        int imageWidth = _bufImage.getWidth(this);
        int imageHeight = _bufImage.getHeight(this);

        int zoomedWidth = (int) (imageWidth * zoom);
        int zoomedHeight = (int) (imageHeight * zoom);

        if (zoomedWidth != cachedWidth
                || zoomedHeight != cachedHeight) {

            cachedWidth = zoomedWidth;
            cachedHeight = zoomedHeight;


            if (selectPane != null) {
                int imageWidth2 = selectPane.simg.getWidth();
                int imageHeight2 = selectPane.simg.getHeight();

                int zoomedWidth2 = (int) (imageWidth2 * zoom);
                int zoomedHeight2 = (int) (imageHeight2 * zoom);

                if (zoomedWidth2 != cachedWidth2
                        || zoomedHeight2 != cachedHeight2) {

                    cachedWidth2 = zoomedWidth2;
                    cachedHeight2 = zoomedHeight2;
                }
            }

            tracker.setLocation(cachedWidth, cachedHeight);

            revalidate();
            repaint();
        }
    }

    /**
     * Classe interne qui écoute la molette afin de zoomer ou dé-zoomer l'espace de dessin
     */
    private class ZoomSelector implements MouseWheelListener {

        public void mouseWheelMoved(MouseWheelEvent e) {

            if (zoomEnabled) {
                int amount = e.getWheelRotation();
                if (amount < 0) {
                    increaseZoom();
                } else {
                    decreaseZoom();
                }

                rescaleImage();
            }
        }
    }

    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}


/*
 *  public void line(int x,int y,int x2, int y2, int color,Graphics2D g) {
    int w = x2 - x ;
    int h = y2 - y ;
    int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0 ;
    if (w<0)
            dx1 = -1;
        else if (w > 0) {
            dx1 = 1;
        }
        if (h < 0) {
            dy1 = -1;
        } else if (h > 0) {
            dy1 = 1;
        }
        if (w < 0) {
            dx2 = -1;
        } else if (w > 0) {
            dx2 = 1;
        }
        int longest = Math.abs(w);
        int shortest = Math.abs(h);
        if (!(longest > shortest)) {
            longest = Math.abs(h);
            shortest = Math.abs(w);
            if (h < 0) {
                dy2 = -1;
            } else if (h > 0) {
                dy2 = 1;
            }
            dx2 = 0;
        }
        int numerator = longest >> 1;
        for (int i = 0; i <= longest; i++) {
            g.fillRect(x-2, y-2,1,1);
            g.fillRect(x-1, y-1,1,1);
            g.fillRect(x, y,1,1);
            g.fillRect(x+1, y+1,1,1);
            g.fillRect(x+2, y+2,1,1);
            numerator += shortest;
            if (!(numerator < longest)) {
                numerator -= longest;
                x += dx1;
                y += dy1;
            } else {
                x += dx2;
                y += dy2;
            }
        }
    }

 */