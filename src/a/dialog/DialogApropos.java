package a.dialog;


import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Classe de Boîte de Dialogue qui gère le contenu du menu APROPOS
 * @author Jessym
 */

public class DialogApropos extends JDialog {

    private JPanel container = null;
    private JDialog dialog = this;
    private JLabel logo_bt;

    /**
     * Constructeur DialogApropos
     * @param parent fenêtre parent
     * @param name nom de la fenêtre par défaut
     * @param modal fenêtre bloquante pour l'application ou non
     */
    public DialogApropos(JFrame parent, String name, boolean modal) {
        super(parent, name, modal);
        this.setSize(559, 360);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.initDialog();


    }

    
    /**
     * Initialise la boite de dialogue
     */
    public void initDialog() {

        logo_bt = new JLabel(new ImageIcon(getClass().getResource("/misc/iwsplashapropos.png")));
        JPanel panIcon = new JPanel();
        panIcon.setBackground(Color.black);
        panIcon.setLayout(new BorderLayout());
        panIcon.add(logo_bt, BorderLayout.NORTH);

        panIcon.setFocusable(true);
        panIcon.requestFocus();
        panIcon.addMouseListener(new MouseListener() {

            @Override
            public void mousePressed(MouseEvent e) {
                dialog.setVisible(false);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        content.setBackground(Color.black);
        content.add(panIcon, BorderLayout.NORTH);


        this.getContentPane().add(content, BorderLayout.CENTER);

    }

    /**
     * Affichage de la boite de dialogue
     */
    public void showNouveauDialog() {
        this.setVisible(true);
    }
}
