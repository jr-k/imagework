package a.dialog;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.MaskFormatter;

/**
 * Classe de Boîte de Dialogue qui gère le contenu du menu Nouveau fichier
 * @author Jessym
 */
public class DialogNouveau extends JDialog {

    private JPanel alpha;
    private JPanel container = null;
    private JTextField titleField;
    private JFormattedTextField longuf;
    private JFormattedTextField largf;
    SpinnerModel model = new SpinnerNumberModel(300, 0, 3000, 1);
    SpinnerModel model2 = new SpinnerNumberModel(300, 0, 3000, 1);
    JSpinner longspin = new JSpinner(model);
    JSpinner largspin = new JSpinner(model2);
    private JTextField resof;
    private JComboBox extcb;
    private JLabel logo_bt;
    private JComboBox largPX = new JComboBox();
    private JComboBox longPX = new JComboBox();
    private JComboBox resoPX = new JComboBox();
    private JButton open = new JButton("Parcourir...");
    private DialogNouveauInfo dni = null;
    private JDialog dialog = this;

    
    /**
     * Constructeur DialogApropos
     * @param parent fenêtre parent
     * @param name nom de la fenêtre par défaut
     * @param modal fenêtre bloquante pour l'application ou non
     */
    public DialogNouveau(JFrame parent, String name, boolean modal) {
        super(parent, name, modal);
        this.setSize(440, 350);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.initDialog();

        titleField.setText("New");

    }

    /**
     * Affiche la boîte dialogue et renvoi les informations sur le nouveau fichier crée
     * @return les informations sur le nouveau fichier crée
     */
    public DialogNouveauInfo showNouveauDialog() {
        this.setVisible(true);
        return this.dni;
    }

    /**
     * Initialise la boîte de dialogue
     */
    public void initDialog() {
        MaskFormatter mask = null;
        MaskFormatter mask2 = null;
        try {
            mask = new MaskFormatter("####");
            mask2 = new MaskFormatter("####");
        } catch (Exception e) {
        }


        logo_bt = new JLabel(new ImageIcon(getClass().getResource("/misc/iwicc2.png")));
        JPanel panIcon = new JPanel();
        panIcon.setBackground(Color.white);
        panIcon.setLayout(new BorderLayout());
        panIcon.add(logo_bt);


        // Name
        JPanel panName = new JPanel();
        panName.setBackground(Color.white);
        panName.setPreferredSize(new Dimension(320, 40));
        titleField = new JTextField();
        titleField.setPreferredSize(new Dimension(100, 25));
        panName.setBorder(BorderFactory.createEtchedBorder());
        JLabel titleLab = new JLabel("Nom du fichier:");
        panName.add(titleLab);
        panName.add(titleField);

        // Taille Long
        JPanel panTailleLong = new JPanel();
        panTailleLong.setBackground(Color.white);
        panTailleLong.setPreferredSize(new Dimension(320, 40));
        panTailleLong.setBorder(BorderFactory.createEtchedBorder());
        JLabel longl = new JLabel("Longueur :");
        panTailleLong.add(longl);
        panTailleLong.add(longspin);
        panTailleLong.add(longPX);

        longPX.addItem("pixels");


        // Taille Larg
        JPanel panTailleLarg = new JPanel();
        panTailleLarg.setBackground(Color.white);
        panTailleLarg.setPreferredSize(new Dimension(320, 40));
        panTailleLarg.setBorder(BorderFactory.createEtchedBorder());
        JLabel largl = new JLabel("Largeur :");
        panTailleLarg.add(largl);
        panTailleLarg.add(largspin);
        panTailleLarg.add(largPX);

        largPX.addItem("pixels");


        // Resolution
        JPanel panReso = new JPanel();
        panReso.setBackground(Color.white);
        panReso.setPreferredSize(new Dimension(320, 40));
        panReso.setBorder(BorderFactory.createEtchedBorder());
        JLabel resol = new JLabel("Couleur du fond :");
        panReso.add(resol);
        panReso.add(resoPX);

        resoPX.addItem("Blanc");
        resoPX.addItem("Noir");
        resoPX.addItem("Couleur d'avant plan");
        resoPX.addItem("Couleur d'arrière plan");


        // Ext
        JPanel extPan = new JPanel();
        extPan.setBackground(Color.white);
        extPan.setPreferredSize(new Dimension(320, 40));
        extcb = new JComboBox();
        extcb.addItem("PNG");
        extcb.addItem("JPG");
        extcb.addItem("BMP");
        extcb.setPreferredSize(new Dimension(100, 25));
        extPan.setBorder(BorderFactory.createEtchedBorder());
        JLabel extcblab = new JLabel("Format : ");
        extPan.add(extcblab);
        extPan.add(extcb);

        JPanel panempty = new JPanel();
        panempty.setPreferredSize(new Dimension(200, 10));
        panempty.setBackground(Color.white);

        JPanel content = new JPanel();
        content.setBackground(Color.white);
        content.add(panempty);
        content.add(panName);
        content.add(panTailleLong);
        content.add(panTailleLarg);
        content.add(panReso);
        content.add(extPan);


        content.setBorder(BorderFactory.createTitledBorder("Nouveau projet ImageWork"));

        JPanel control = new JPanel();
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("FERMER");
        control.add(ok);
        control.add(cancel);


        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dni = new DialogNouveauInfo(titleField.getText(), Integer.parseInt(longspin.getValue() + ""),
                        Integer.parseInt(largspin.getValue() + ""), (String) extcb.getSelectedItem(), resoPX.getSelectedIndex());
                setVisible(false);
            }
        });


        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });



        this.getContentPane().add(panIcon, BorderLayout.WEST);
        this.getContentPane().add(content, BorderLayout.CENTER);
        this.getContentPane().add(control, BorderLayout.SOUTH);
    }

    /** 
     * Retourne les informations sur le nouveau fichier crée
     * @return les informations sur le nouveau fichier crée
     */
    public DialogNouveauInfo getInfos() {
        return this.dni;
    }
}
