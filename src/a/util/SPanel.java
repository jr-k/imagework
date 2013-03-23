package a.util;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.geom.Point2D;
import javax.swing.JPanel;

/**
 * Classe utilitaire qui gère un composant Special JPanel qui s'affiche sous un dégradé Gris ou Noir
 * @author Jessym
 */
public class SPanel extends JPanel {

    public final static int GRAY = 0;
    public final static int BLACK = 1;
    public int mode = GRAY;

    /**
     * Constructeur SPanel 
     * @param mode Dégradé de mode Gris ou Noir
     */
    public SPanel(int mode) {
        this.mode = mode;

    }

    /**
     * Constructeur SPanel
     */
    public SPanel() {
        super();
    }

    /**
     * Peint la SPanel
     * @param g 
     */
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Color colors[];
        LinearGradientPaint p;

        switch (mode) {
            case GRAY:

                colors = new Color[]{Color.decode("0xf3f3f3"), Color.decode("0xfefefe")};

                p = new LinearGradientPaint(new Point2D.Float(0, 0), new Point2D.Float(0, this.getHeight()), new float[]{0.0F, 1.0F}, colors);
                g2.setPaint(p);
                g2.fillRect(0, 0, this.getWidth(), this.getHeight());
                break;

            case BLACK:

                colors = new Color[]{Color.decode("0x000000"), Color.decode("0x444444")};

                p = new LinearGradientPaint(new Point2D.Float(0, 0), new Point2D.Float(0, this.getHeight()), new float[]{0.0F, 1.0F}, colors);
                g2.setPaint(p);
                g2.fillRect(0, 0, this.getWidth(), this.getHeight());
                break;


        }


    }
}
