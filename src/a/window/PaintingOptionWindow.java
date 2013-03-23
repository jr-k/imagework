package a.window;



import a.drawing.PaintPanel;
import a.drawing.SMenuBar;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.text.AttributedString;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;


/**
 * Gère la fenêtre des options de forme dans l'espace de travail
 * @author Jessym
 */
public class PaintingOptionWindow extends JInternalFrame {
    public static JTabbedPane optionTabbedPane = new JTabbedPane();
    static JPanel optionpane = new JPanel();
    static JLabel stroke = new JLabel("Bordure : ");
    public static JSlider stroke_s = new JSlider(0, 100, 0);
    static JLabel size = new JLabel("Taille : ");
    public static JSlider size_s = new JSlider(1, 50, 1);
    static JPanel textpane = new JPanel();
    static JPanel arcpane = new JPanel();
    static JLabel startAngle = new JLabel("Angle de départ : ");
    public static JSlider startAngle_s = new JSlider(0, 360, 360);
    static JLabel arcAngle = new JLabel("Angle de l'arc : ");
    public static JSlider arcAngle_s = new JSlider(0, 360, 180);
    public static JCheckBox filledArc = new JCheckBox("Remplissage");
    static JLabel police = new JLabel("Police : ");
    static JComboBox police_cb = new JComboBox();
    static JButton bold;
    public static boolean b_bold = false;
    static JButton italic;
    public static boolean b_italic = false;
    static JButton underline;
    public static boolean b_underline = false;
    static JButton strike;
    public static boolean b_strike = false;
    static JButton opaque;
    public static boolean b_opaque = false;
    
    
    static AttributedString as;

    
    /**
     * Créer une fenêtre des calques dans l'espace de travail
     * @param w longueur de la fenêtre
     * @param h hauteur de la fenêtre
     * @param x position x de la fenêtre
     * @param y position y de la fenêtre
     */
    public PaintingOptionWindow(int w, int h, int x, int y) {
        this.setTitle("Options de forme");
        this.setClosable(true);
        this.setResizable(true);
        this.setMaximizable(false);
        this.setIconifiable(true);
        this.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        this.setSize(w, h);
        this.setPreferredSize(new Dimension(w, h));
        this.setLocation(x, y);

        try {
                this.setFrameIcon(new ImageIcon(ImageIO.read(getClass().getResource("/icons/poptionicon.png"))));
            } catch (IOException ex) {
                Logger.getLogger(IWCameraWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        // Gestion Scrolling                 
        this.setMaximumSize(new Dimension(w, 1000));
        this.setMinimumSize(new Dimension(w, 100));

        JScrollPane jsp = new JScrollPane(optionTabbedPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.setContentPane(jsp);
        jsp.getVerticalScrollBar().setUnitIncrement(10);

        optionTabbedPane.setPreferredSize(new Dimension(w - 20, h));
        optionpane.setPreferredSize(new Dimension(w - 20, h));
        textpane.setPreferredSize(new Dimension(w - 20, h));
        arcpane.setPreferredSize(new Dimension(w - 20, h));

        stroke.setText("Bordure : " + stroke_s.getValue() + "px");
        stroke_s.setPreferredSize(new Dimension(100, 30));
        stroke_s.setPaintTicks(true);
        stroke_s.setMinorTickSpacing(25);




        size.setText("Taille : 1px");
        size_s.setPreferredSize(new Dimension(100, 30));
        size_s.setMinorTickSpacing(5);
        size_s.setPaintTicks(true);

        optionpane.add(size);
        optionpane.add(size_s);
        optionpane.add(stroke);
        optionpane.add(stroke_s);





        textpane.add(police);
        textpane.add(police_cb);
        police_cb.setPreferredSize(new Dimension(120, 20));
        police_cb.addItem("Arial");
        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = e.getAllFonts(); // Get the fonts
        for (Font f : fonts) {
            police_cb.addItem(f.getFontName());
        }


        police_cb.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

              if (PaintPanel.tf!=null)  PaintPanel.tf.setFont(getFontThis(PaintPanel.sizeText,PaintPanel.tf.getText()));
            }
        });

        bold = new JButton(new ImageIcon(getClass().getResource("/tools/bold.png")));
        bold.setPreferredSize(new Dimension(30, 30));
        bold.setToolTipText("Gras");
        bold.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (b_bold) {
                    b_bold = false;
                    bold.setBackground(null);
                } else {
                    b_bold = true;
                    bold.setBackground(Color.black);
                }
              if (PaintPanel.tf!=null)  PaintPanel.tf.setFont(getFontThis(PaintPanel.sizeText,PaintPanel.tf.getText()));
            }
        });

        italic = new JButton(new ImageIcon(getClass().getResource("/tools/italic.png")));
        italic.setPreferredSize(new Dimension(30, 30));
        italic.setToolTipText("Italique");
        italic.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (b_italic) {
                    b_italic = false;
                    italic.setBackground(null);
                } else {
                    b_italic = true;
                    italic.setBackground(Color.black);
                }
              if (PaintPanel.tf!=null)  PaintPanel.tf.setFont(getFontThis(PaintPanel.sizeText,PaintPanel.tf.getText()));
            }
        });

        underline = new JButton(new ImageIcon(getClass().getResource("/tools/underline.png")));
        underline.setPreferredSize(new Dimension(30, 30));
        underline.setToolTipText("Souligné");
        underline.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (b_underline) {
                    b_underline = false;
                    underline.setBackground(null);
                } else {
                    b_underline = true;
                    underline.setBackground(Color.black);
                }
              if (PaintPanel.tf!=null)  PaintPanel.tf.setFont(getFontThis(PaintPanel.sizeText,PaintPanel.tf.getText()));
            }
        });
        
        
        strike = new JButton(new ImageIcon(getClass().getResource("/tools/strike.png")));
        strike.setPreferredSize(new Dimension(30, 30));
        strike.setToolTipText("Barré");
        strike.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (b_strike) {
                    b_strike = false;
                    strike.setBackground(null);
                } else {
                    b_strike = true;
                    strike.setBackground(Color.black);
                }
              if (PaintPanel.tf!=null)  PaintPanel.tf.setFont(getFontThis(PaintPanel.sizeText,PaintPanel.tf.getText()));
            }
        });
        
        opaque = new JButton(new ImageIcon(getClass().getResource("/tools/opaquetext.png")));
        opaque.setPreferredSize(new Dimension(30, 30));
        opaque.setToolTipText("Fond transparent");
        opaque.setBackground(Color.black);
        opaque.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (b_opaque) {
                    b_opaque = false;
                    opaque.setBackground(null);
                    PaintPanel.tf.setOpaque(b_opaque);
                } else {
                    b_opaque = true;
                    opaque.setBackground(Color.black);
                    PaintPanel.tf.setOpaque(b_opaque);
                }
                    
            }
        });


        stroke_s.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                stroke.setText("Bordure : " + stroke_s.getValue() + "px");
            }
        });


        size_s.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {

                size.setText("Taille : " + (size_s.getValue()) + "px");
            }
        });
        
        


        this.addInternalFrameListener(new InternalFrameListener() {

            public void internalFrameOpened(InternalFrameEvent e) {
            }

            public void internalFrameClosing(InternalFrameEvent e) {
                SMenuBar.optionsf.doClick();
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
        
        
        
        JPanel formattext = new JPanel();
        formattext.setPreferredSize(new Dimension(200,40));
        
        
        formattext.add(bold);
        formattext.add(italic);
        formattext.add(opaque);
       // formattext.add(underline);
       // formattext.add(strike);
        
        textpane.add(formattext);
        
       
        
      
        
        startAngle.setText("Angle de départ : 360°");
        startAngle_s.setPreferredSize(new Dimension(70, 30));
        startAngle_s.setMinorTickSpacing(50);
        startAngle_s.setPaintTicks(true);
        
        startAngle_s.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {

                startAngle.setText("Angle de départ : " + (startAngle_s.getValue()) + "°");
            }
        });
        
        arcAngle.setText("Angle de l'arc : 180°");
        arcAngle_s.setPreferredSize(new Dimension(70, 30));
        arcAngle_s.setMinorTickSpacing(50);
        arcAngle_s.setPaintTicks(true);
        
        arcAngle_s.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {

                arcAngle.setText("Angle de l'arc : " + (arcAngle_s.getValue()) + "°");
            }
        });

        
        arcpane.add(startAngle);
        arcpane.add(startAngle_s);
        arcpane.add(arcAngle);
        arcpane.add(arcAngle_s);
        arcpane.add(filledArc);
        
        
        optionTabbedPane.add(textpane, "Texte");
        optionTabbedPane.add(optionpane, "Taille");
        optionTabbedPane.add(arcpane, "Arc");


        this.setVisible(true);
    }

    public static Font getFontThis(int size,String s) {

        Font plainFont = null;
        if (b_bold && b_italic==true) {
            plainFont = new Font((String) police_cb.getSelectedItem(), Font.BOLD + Font.ITALIC, size);
        } else if (b_bold && !b_italic) {
            plainFont = new Font((String) police_cb.getSelectedItem(), Font.BOLD, size);
        } else if (!b_bold && b_italic) {
            plainFont = new Font((String) police_cb.getSelectedItem(), Font.ITALIC, size);
        } else {
            plainFont = new Font((String) police_cb.getSelectedItem(), Font.PLAIN, size);
        }
        as = new AttributedString(s);
        as.addAttribute(TextAttribute.FONT, plainFont);
        if (b_underline) {
            as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        } else {
            as.addAttribute(TextAttribute.UNDERLINE, null);
        }
        if (b_strike) {
            as.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
        } else {
            as.addAttribute(TextAttribute.STRIKETHROUGH, null);
        }
        

        return plainFont;
    }

    public static String fill3zero(int n) {
        if (n < 10) {
            return "00" + n;
        } else if (n < 100) {
            return "0" + n;
        } else {
            return "" + n;
        }
    }
}