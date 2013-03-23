package a.dialog;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Classe de Boîte de Dialogue qui gère le choix calques ou nouveau fichier lors d'un Drag and Drop
 * @author Jessym
 */
public class DialogImportImage extends JDialog {

    private JDialog dialog = this;
    public String type = "calque";

    /**
     * Constructeur DialogConvolution
     * @param parent fenêtre parent
     * @param name nom de la fenêtre par défaut
     * @param modal fenêtre bloquante pour l'application ou non
     */
    public DialogImportImage(JFrame parent, String name,String content, boolean modal) {
        super(parent, name, modal);
        this.setSize(440, 90);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        this.initDialog(content);


    }

    /**
     * Affiche la boîte de dialogue
     */
    public void showImportDialog() {
        this.setVisible(true);
    }

    
    /**
     * Initialise la boîte de dialogue
     * @param content Message à afficher
     */
    public void initDialog(String content) {

        JPanel labPane = new JPanel();
        labPane.add(new JLabel(content));

        JPanel contents = new JPanel();
        contents.add(labPane);

        JPanel control = new JPanel();
        JButton calque = new JButton("Nouveau calque");
        JButton projet = new JButton("Nouveau projet");
        control.add(calque);
        control.add(projet);

        calque.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                type = "calque";
                setVisible(false);
            }
        });


        projet.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                type = "projet";
                setVisible(false);
            }
        });



        this.getContentPane().add(contents, BorderLayout.CENTER);
        this.getContentPane().add(control, BorderLayout.SOUTH);
    }

  
}
