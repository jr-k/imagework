package a.util;


import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Classe utilitaire qui redimensionne une image
 * @author Jessym
 */
public class ResizeImage {

    public static int IMG_WIDTH = 100;
    public static int IMG_HEIGHT = 100;

    /**
     * Redimensionne une image et retourne l'image redimensionnée
     * @param originalImage Image à redimensionner
     * @param type Type d'encodage des couleurs de l'image à redimensionner
     * @return Image redimensionnée
     */
    public static BufferedImage resizeImage(BufferedImage originalImage, int type) {
        BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
        g.dispose();

        return resizedImage;
    }

    /**
     * Redimensionne une image et retourne l'image redimensionnée de qualité supérieure
     * @param originalImage Image à redimensionner
     * @param type Type d'encodage des couleurs de l'image à redimensionner
     * @return Image redimensionnée
     */
    public static BufferedImage resizeImageWithHint(BufferedImage originalImage, int type) {

        BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
        g.dispose();
        g.setComposite(AlphaComposite.Src);

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        return resizedImage;
    }
}