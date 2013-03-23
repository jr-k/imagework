package a.drawing;


import java.awt.image.BufferedImage;


/**
 * Classe qui gère la séléction d'une image
 * @author Jessym
 */
public class SelectionImage {

    int xo = 0;
    int yo = 0;
    BufferedImage simg;
    PaintPanel canv;
    int ws, hs;

    /**
     * Constructeur SelectionImage
     * @param imgA Image sélectionnée
     * @param x coordonée x de l'image
     * @param y coordonée y de l'image
     * @param c espace de dessin associé
     * @param w longueur de l'image
     * @param h hauteurde l'image
     */
    public SelectionImage(BufferedImage imgA, int x, int y, final PaintPanel c, int w, int h) {

        xo = x;
        yo = y;
        simg = imgA;
        canv = c;
        ws = w;
        hs = h;

    }
}