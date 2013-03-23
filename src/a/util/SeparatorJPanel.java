package a.util;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.geom.Point2D;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * Classe utilitaire qui défini un séparateur vertical entre 2 Panneau
 * @author Jessym
 */
public class SeparatorJPanel extends JPanel {

    SPanel leftSeparator;
    SPanel separator;
    SPanel rightSeparator;

    /**
     * Constructeur SeparatorJPanel
     * @param c Couleur du séparateur
     * @param leftMargin Marge à gauche en pixels
     * @param rightMargin Marge à droite en pixels
     */
    public SeparatorJPanel(Color c, int leftMargin, int rightMargin) {

        leftSeparator = new SPanel(SPanel.GRAY);
        leftSeparator.setPreferredSize(new Dimension(leftMargin, 30));

        separator = new SPanel(SPanel.GRAY);
        separator.setPreferredSize(new Dimension(1, 30));
        separator.setBorder(BorderFactory.createLineBorder(c));

        rightSeparator = new SPanel(SPanel.GRAY);
        rightSeparator.setPreferredSize(new Dimension(rightMargin, 30));

        this.add(leftSeparator);
        this.add(separator);
        this.add(rightSeparator);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Color colors[];
        LinearGradientPaint p;

        colors = new Color[]{Color.decode("0xf3f3f3"), Color.decode("0xfefefe")};

        p = new LinearGradientPaint(new Point2D.Float(0, 0), new Point2D.Float(0, this.getHeight()), new float[]{0.0F, 1.0F}, colors);
        g2.setPaint(p);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());

    }
}
