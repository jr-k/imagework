package a.util;


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Classe utilitaire qui dessine une image dans un JPanel
 * @author Jessym
 */
public class TImage extends JPanel {

    private BufferedImage img = null;
    private String lastFiltre = null;
    private String lastRotation = null;
    private TImage timage = this;
    private boolean onclick = false;
    private double scale = 1;
    private ImageIcon BuffImageIcon;
    private JLabel labelImageIcon;

    /**
     * Constructeur TImage
     * @param imgA Image associée au JPanel
     */
    public TImage(BufferedImage imgA) {

        this.img = imgA;
        this.setSize(img.getWidth(), img.getHeight());
        this.setBorder(BorderFactory.createEtchedBorder());
        BuffImageIcon = new ImageIcon((img));
        labelImageIcon = new JLabel(BuffImageIcon);
        this.add(labelImageIcon);
    }

    /**
     * Constructeur TImage
     * @param fileName Nom du fichier à ouvrir
     */
    public TImage(String fileName) {
        openImage(getClass().getResource(fileName));
        this.setBorder(BorderFactory.createEtchedBorder());


    }

    /**
     * Dessine l'image dans le JPanel associé
     * @param g 
     */
    public void paintComponent(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }

    /**
     * Associe une nouvelle image passée en paramètre au JPanel
     * @param b Image à associer au JPanel
     */
    public void setImage(BufferedImage b) {
        this.img = b;
    }

    /**
     * Renvoi l'image associé au JPanel
     * @return l'image associé au JPanel
     */
    public BufferedImage getImage() {
        return this.img;
    }

    /**
     * Change l'image associé via une URL
     * @param url Url de la nouvelle image associée
     */
    public void openImage(URL url) {
        if (url == null) {
            JOptionPane jop = new JOptionPane();
            jop.showMessageDialog(null, "Erreur", "Information", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                this.img = ImageIO.read(url);
                this.setSize(img.getWidth(), img.getHeight());
                this.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
            } catch (IOException e) {
                JOptionPane jop2 = new JOptionPane();
                jop2.showMessageDialog(null, "Erreur", "Information", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * Renvoi la couleur aux coordonée x,y de l'image
     * @param x coordonée x de l'image
     * @param y coordonée y de l'image
     * @return la couleur aux coordonée x,y de l'image
     */
    public int getColorAt(int x, int y) {
        return img.getRGB(x, y);

    }
}
