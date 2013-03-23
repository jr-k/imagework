package a.util;


import java.awt.image.RGBImageFilter;

/**
 * Classe qui gère le filtre négatif d'une image
 * @author Jessym
 */
public class FiltreNegatif extends RGBImageFilter {

    /**
     * Constructeur FiltreNegatif
     */
    public FiltreNegatif() {
        canFilterIndexColorModel = true;
    }

    /**
     * Filtre les composantes de couleurs RGB et retourne la couleur RGB négative associée
     * @param x composante du pixel X
     * @param y composantes du pixel Y
     * @param rgb composante RGB à inverser
     * @return Retourne la nouvelle couleur négative
     */
    public int filterRGB(int x, int y, int rgb) {
        int alpha = rgb & 0xFF000000;
        // Transformation des composantes RGB en leur inverse
        int rougeInverse = (rgb & 0xFF0000) ^ 0xFF0000;
        int vertInverse = (rgb & 0x00FF00) ^ 0x00FF00;
        int bleuInverse = (rgb & 0x0000FF) ^ 0x0000FF;
        return alpha | rougeInverse | vertInverse | bleuInverse;
    }
}