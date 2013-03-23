package a.util;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Classe qui gère la boîte de dialogue qui contiendra un JLinkButton
 * @author Jessym
 */
public class JLinkDialog extends JDialog {

    private JPanel container = null;
    private JTextField titleField = new JTextField();
    private JDialog dialog = this;

    /**
     * Constructeur JLinkDialog
     * @param parent fenêtre parent
     * @param name nom de la fenêtre par défaut
     * @param modal fenêtre bloquante pour l'application ou non
     * @param lab nom du lien
     * @param hlab hyperlien lié au nom
     */
    public JLinkDialog(JFrame parent, String name, boolean modal, String lab, String hlab) {
        super(parent, name, modal);
        this.setSize(300, 150);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.initDialog(lab, hlab);
    }

    /**
     * Affiche de la boîte de dialogue
     */
    public void showDialog() {
        this.setVisible(true);
    }

    
    /**
     * Initialise la boîte de dialogue
     * @param lab nom du lien
     * @param hlab hyperlien lié au nom
     */
    public void initDialog(String lab, String hlab) {

        JPanel content = new JPanel();


        JLabel labs = new JLabel(lab);
        JLinkButton hbt = new JLinkButton(hlab);

        content.add(labs);
        content.add(hbt);

        JPanel control = new JPanel();
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("FERMER");
        control.add(ok);
        control.add(cancel);

        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
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
}
