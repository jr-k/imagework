package a.util;


import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Vector;

/**
 * Classe qui gère la sauvegarde d'un projet ImageWork
 * @author Jessym
 */
public class IwProjectSave implements Serializable {

    Vector project = new Vector();
    int width = 0, height = 0;

    /**
     * Constructeur qui prépare la sauvegarde des Calques et de leurs options dans un Vecteur conteneur
     * @param v1 Vecteur des calques
     * @param v2 Vecteur de la visibilité des calques 
     * @param v3 Vecteur de l'opacité des calques
     * @param v4 Vecteur du nom des calques
     * @param v5 Vecteur des options de fusion
     * @param w Longueur de l'image liée au projet
     * @param h Hauteur de l'image liée au projet
     */
    public IwProjectSave(Vector<BufferedImage> v1, Vector<Boolean> v2, Vector<Integer> v3, 
            Vector<String> v4,Vector<Integer> v5, int w, int h) {

        Vector<Vector<Integer>> images = new Vector();
        for (int i = 0; i < v1.size(); i++) {
            Vector<Integer> tmp = new Vector();
            for (int col = 0; col < v1.get(i).getWidth(); col++) {
                for (int ligne = 0; ligne < v1.get(i).getHeight(); ligne++) {
                    tmp.add(v1.get(i).getRGB(col, ligne));
                }
            }

            images.add(tmp);

        }


        project.add(images);
        project.add(v2);
        project.add(v3);
        project.add(v4);
        project.add(v5);
        width = w;
        height = h;



    }

    /**
     * Retourne le Vecteur conteneur qui contient tout le projet
     * @return le Vecteur conteneur qui contient tout le projet
     */
    public Vector getProject() {
        return this.project;
    }

    /**
     * Retourne la longueur de l'image
     * @return la longueur de l'image
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Retourne la hauteur de l'image
     * @return la hauteur de l'image
     */
    public int getHeight() {
        return this.height;
    }
}
