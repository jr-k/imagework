package a.drawing;


import java.awt.image.BufferedImage;
import java.util.Vector;

/**
 * Classe qui gère l'historique d'un calque
 * @author Jessym
 */
public class HistoryGroup {

    public Vector<BufferedImage> images = new Vector();
    public int position = 0;

    /**
     * Renvoi l'historique d'un calque sous forme d'un Vecteur
     * @return l'historique d'un calque sous forme d'un Vecteur
     */
    public Vector<BufferedImage> getImages() {
        return this.images;
    }

    /**
     * Renvoi le numéro du calque
     * @return numéro du calque associé 
     */
    public int getPosition() {
        return this.position;
    }

    /**
     * Constructeur qui crée un nouvel historique pour un calque
     * @param bi Première image de l'historique
     * @param i Numéro du calque
     */
    public HistoryGroup(BufferedImage bi, int i) {
        images.add(bi);
        position = i;
    }

    /**
     * Supprime tout l'historique
     */
    public void deleteAll() {
        images.removeAllElements();

    }

    /**
     * Ajoute une image à l'historique
     * @param bi image à ajouter à l'historique
     */
    public void add(BufferedImage bi) {
        images.add(bi);
    }

    /**
     * Supprime l'image du rang passé en paramètre dans l'historique
     * @param i rang de l'image à supprimer
     */
    public void removeAt(int i) {
        images.remove(i);
    }
}
