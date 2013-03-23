package a.dialog;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Classe de Boîte de Dialogue qui gère le contenu du menu Matrice de Convolution 2D
 * @author Jessym
 */
public class DialogConvolution extends JDialog {

    private JPanel container = null;
    private JFormattedTextField[] titleField = new JFormattedTextField[9];
    private DialogConvolutionInfo dci = null;
    private JDialog dialog = this;

    /**
     * Constructeur DialogConvolution
     * @param parent fenêtre parent
     * @param name nom de la fenêtre par défaut
     * @param modal fenêtre bloquante pour l'application ou non
     */
    public DialogConvolution(JFrame parent, String name, boolean modal) {
        super(parent, name, modal);
        this.setSize(440, 350);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        this.initDialog();


    }

    /**
     * Affiche la boîte de dialogue
     */
    public void showConvolutionDialog() {
        this.setVisible(true);
    }

    
    /**
     * Initialise la boîte de dialogue
     */
    public void initDialog() {

        JPanel matricePane = new JPanel();
        matricePane.setBackground(Color.white);
        matricePane.setPreferredSize(new Dimension(200, 200));

        matricePane.setBorder(BorderFactory.createEtchedBorder());
        JPanel emptypane = new JPanel();
        emptypane.setPreferredSize(new Dimension(140, 30));
        emptypane.setBackground(Color.white);
        matricePane.add(emptypane);

        JPanel subMatricePane = new JPanel();
        subMatricePane.setLayout(new GridLayout(3, 3));


        for (int i = 0; i < titleField.length; i++) {
            titleField[i] = new JFormattedTextField("0");
            titleField[i].setPreferredSize(new Dimension(40, 40));
            titleField[i].addKeyListener(new MCKeyListener());
            titleField[i].setHorizontalAlignment(JFormattedTextField.CENTER);
            subMatricePane.add(titleField[i]);
        }

        matricePane.add(subMatricePane);




        JPanel content = new JPanel();
        content.setBackground(Color.white);
        content.add(matricePane);

        content.setBorder(BorderFactory.createTitledBorder("Matrice de convolution 2D"));

        JPanel control = new JPanel();
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("FERMER");
        control.add(ok);
        control.add(cancel);

        ok.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dci = new DialogConvolutionInfo(titleField);
                setVisible(false);
            }
        });


        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });



        this.getContentPane().add(content, BorderLayout.CENTER);
        this.getContentPane().add(control, BorderLayout.SOUTH);
    }

    /**
     * Renvoi les informations relatives à la fenêtre
     * @return Information de la matrice de convolution
     */
    public DialogConvolutionInfo getInfos() {
        return this.dci;
    }

    
    /**
     * Classe interne qui vérifie la bonne saisie des caractères
     */
    class MCKeyListener implements KeyListener {

        public void keyTyped(KeyEvent e) {
        }

        public void keyPressed(KeyEvent e) {
        }

        public void keyReleased(KeyEvent event) {
            JFormattedTextField jtf = ((JFormattedTextField) event.getSource());

            if (event.getKeyChar() != '.' && event.getKeyChar() != '-') {
                if (!isNumeric(event.getKeyChar()) || jtf.getText().length() > 5) {
                    jtf.setText(jtf.getText().replace(String.valueOf(event.getKeyChar()), ""));
                }
            }

        }

        private boolean isNumeric(char carac) {
            try {
                Double.parseDouble(String.valueOf(carac));
            } catch (NumberFormatException e) {
                return false;
            }
            return true;
        }
    }
}
