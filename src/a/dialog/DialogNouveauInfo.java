package a.dialog;


import a.window.ColorWindow;
import java.awt.Color;

/**
 * Classe relative aux informations du nouveau fichier crée
 * @author Jessym
 */
public class DialogNouveauInfo {

    private String nomFichier, extension;
    private int width, height, couleur;

    /**
     * Constructeur de l'objet qui contiendra les informations sur le nouveau fichier crée
     * @param nom Nom du fichier
     * @param w Taille en longueur de l'image
     * @param h Taille en largeur de l'image
     * @param ext Extension du fichier
     * @param coul Couleur de fond à appliquer sur l'image lors de la création
     */
    public DialogNouveauInfo(String nom, int w, int h, String ext, int coul) {

        this.nomFichier = nom;
        this.width = w;
        this.height = h;
        this.extension = ext;
        this.couleur = coul;
    }

    /**
     * Retourne le nom du fichier
     * @return le nom du fichier
     */
    public String getNomFichier() {
        return nomFichier;
    }

    /**
     * Retourne la longueur de l'image
     * @return la longueur de l'image
     */
    public int getWidth() {
        return width;
    }

    /**
     * Retourne la largeur de l'image
     * @return la largeur de l'image
     */
    public int getHeight() {
        return height;
    }

    /**
     * Retourne l'extension
     * @return l'extension
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Retourne la couleur de fond à dessiner sur l'image
     * @return la couleur de fond à dessiner sur l'image
     */
    public Color getCouleur() {
        if (couleur == 0) {
            return Color.white;
        } else if (couleur == 1) {
            return Color.black;
        } else if (couleur == 2) {
            return ColorWindow.uColor1;
        } else if (couleur == 3) {
            return ColorWindow.uColor2;
        } else {
            return Color.black;
        }

    }
}
