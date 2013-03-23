package a.util;

import a.drawing.SMenuBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JDesktopPane;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * Classe qui gère un espace de travail personnalisé
 * @author Jessym
 */
public class SDesktopPane extends JDesktopPane implements MouseListener {

    public static JPopupMenu jpmdesktop = new JPopupMenu();
    public SDesktopPane cpad = this;

    JMenuItem dnouveau = new JMenuItem("Nouveau");
    JMenuItem douvrir = new JMenuItem("Ouvrir");
    JMenuItem dpouvrir = new JMenuItem("Ouvrir un projet");
    JMenuItem dsave = new JMenuItem("Enregistrer sous");
    JMenuItem dcopier = new JMenuItem("Copier");
    JMenuItem dcouper = new JMenuItem("Couper");
    JMenuItem dsa = new JMenuItem("Tout sélectionner");
    JMenuItem dcoller = new JMenuItem("Coller");
    
    /**
     * Constructeur SDesktopPane
     */
    public SDesktopPane() {
        super();
        this.addMouseListener(this);
        
        
         dnouveau.addActionListener(new ActionListener(){  public void actionPerformed(ActionEvent e) { SMenuBar.nouveau.doClick();}});
         douvrir.addActionListener(new ActionListener(){  public void actionPerformed(ActionEvent e) { SMenuBar.ouvrir.doClick();}});
         dpouvrir.addActionListener(new ActionListener(){  public void actionPerformed(ActionEvent e) { SMenuBar.pouvrir.doClick();}});
         dsave.addActionListener(new ActionListener(){  public void actionPerformed(ActionEvent e) { SMenuBar.enregistrersous.doClick();}});
         dcopier.addActionListener(new ActionListener(){  public void actionPerformed(ActionEvent e) { SMenuBar.copier.doClick();}});
         dcouper.addActionListener(new ActionListener(){  public void actionPerformed(ActionEvent e) { SMenuBar.couper.doClick();}});
         dsa.addActionListener(new ActionListener(){  public void actionPerformed(ActionEvent e) { SMenuBar.selectall.doClick();}});
         dcoller.addActionListener(new ActionListener(){  public void actionPerformed(ActionEvent e) { SMenuBar.coller.doClick();}});

        
        jpmdesktop.add(dnouveau);
        jpmdesktop.add(douvrir);
        jpmdesktop.add(dpouvrir);
        jpmdesktop.add(dsave);
        jpmdesktop.addSeparator();
        jpmdesktop.add(dcopier);
        jpmdesktop.add(dcouper);
        jpmdesktop.add(dcoller);
        jpmdesktop.add(dsa);

    }

    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            jpmdesktop.show(cpad, e.getX(), e.getY());
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
