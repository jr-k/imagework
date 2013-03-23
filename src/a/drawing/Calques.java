package a.drawing;

import a.window.CalquesWindow;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Vector;

/**
 * Gère les différents calques ainsi que les options de calques comme la visiblitié, l'opacité ou le nom
 * @author Jessym
 */
public class Calques {

    public static Vector<HistoryGroup> historiques = new Vector();
    public static Vector<BufferedImage> calques = new Vector();
    public static Vector<Boolean> visibility = new Vector();
    public static Vector<Integer> opacity = new Vector();
    public static Vector<String> noms = new Vector();
    public static Vector<Integer> fusion = new Vector();
    public static int current = 0;

    /**
     * Constructeur des calques
     */
    public Calques() {
    }

    /**
     * Fusionne les 2 calques donné en paramètre 
     * @param c Calque copié par dessus le 2ème calque
     * @param p 
     */
    public static void mergeCalques(int c, int p) {

        Graphics2D g2 = (Graphics2D) calques.get(p).getGraphics();

        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
        ac = AlphaComposite.getInstance(ac.getRule(), (float) opacity.get(c) / 100.0F);

        g2.setComposite(ac);

        g2.drawImage(calques.get(c), 0, 0, null);

        removeAt(c);

        current = p;

    }

    /**
     * Echange la position de 2 calques
     * @param i
     * @param j 
     */
    public static void swapPositions(int i, int j) {
        Collections.swap(calques, i, j);
        Collections.swap(visibility, i, j);
        Collections.swap(opacity, i, j);
        Collections.swap(noms, i, j);
        Collections.swap(historiques, i, j);
        Collections.swap(fusion, i, j);
    }

    /**
     * Supprime toute la liste des calques avec leurs options 
     */
    public static void deleteAll() {
        calques.removeAllElements();
        visibility.removeAllElements();
        opacity.removeAllElements();
        noms.removeAllElements();
        historiques.removeAllElements();
        fusion.removeAllElements();
        current = 0;
    }

    /**
     * Ajoute un nouveau calque avec l'image passé en paramètre par défaut
     * @param bi Image associé au calque
     */
    public static void add(BufferedImage bi) {
        calques.add(bi);
        visibility.add(true);
        opacity.add(100);
        noms.add(" Calque");
        historiques.add(new HistoryGroup(PaintPanel.newCopy(bi, PaintPanel.type), current));
        fusion.add(0);
    }

    /**
     * Ajoute un nouveau calque avec l'image passé en paramètre par défaut en spécifiant un nom
     * @param bi Image associé au calque
     * @param nom Nom associé au calque
     */
    public static void add(BufferedImage bi, String nom, int fusions) {
        calques.add(bi);
        visibility.add(true);
        opacity.add(100);
        noms.add(nom);
        historiques.add(new HistoryGroup(PaintPanel.newCopy(bi, PaintPanel.type), current));
        fusion.add(fusions);
    }

    /**
     * Change l'image associé au calque numéro "i" passé en paramètre
     * @param bi Nouvelle image à associer au calque de paramètre "i"
     * @param i Numéro du calque à modifier
     */
    public static void setAt(BufferedImage bi, int i) {
        calques.setElementAt(bi, i);
    }

    /**
     * Supprime le calque au rang passé en paramètre 
     * @param i Rang du calque à supprimer
     */
    public static void removeAt(int i) {
        calques.remove(i);
        visibility.remove(i);
        opacity.remove(i);
        noms.remove(i);
        historiques.remove(i);
    }

    /**
     * Renvoi l'image courante positionné par l'attribut "current"
     * @return l'image courante
     */
    public BufferedImage getCurrentImage() {
        return calques.get(current);

    }

    /**
     * Retourne l'image finale de la superposition de tous les calques
     * @return l'image finale de la superposition de tous les calques
     */
    public BufferedImage getFinalImage() {
        BufferedImage tmp = new BufferedImage(calques.get(0).getWidth(), calques.get(0).getHeight(), PaintPanel.type);
        Graphics2D g2 = (Graphics2D) tmp.getGraphics();

        for (int i = 0; i < calques.size(); i++) {
            if (visibility.get(i)) {

                AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
                ac = AlphaComposite.getInstance(ac.getRule(), (float) opacity.get(i) / 100.0F);
                g2.setComposite(ac);

                BufferedImage tmpBuf = Calques.calques.get(i);
                BufferedImage tmpFus = new BufferedImage(tmpBuf.getWidth(), tmpBuf.getHeight(), PaintPanel.type);
                
                if (fusion.get(i) == 0) {
                    tmpFus = tmpBuf;
                }
                else if (fusion.get(i) == 1 && i != 0) {
                    multiplier(tmpFus, tmpBuf, i,tmp);
                } 
                else if (fusion.get(i) == 2 && i != 0) {
                    diviser(tmpFus, tmpBuf, i,tmp);
                } 
                else if (fusion.get(i) == 3 && i != 0) {
                    ecran(tmpFus, tmpBuf, i,tmp);
                } 
                else if (fusion.get(i) == 4 && i != 0) {
                    eclaircir(tmpFus, tmpBuf, i,tmp);
                }
                else if (fusion.get(i) == 5 && i != 0) {
                    assombrir(tmpFus, tmpBuf, i,tmp);
                }
                 else if (fusion.get(i) == 6 && i != 0) {
                    lumieredure(tmpFus, tmpBuf, i,tmp);
                }
                 else if (fusion.get(i) == 7 && i != 0) {
                    fusiongrain(tmpFus, tmpBuf, i,tmp);
                }
                 else if (fusion.get(i) == 8 && i != 0) {
                    extractiongrain(tmpFus, tmpBuf, i,tmp);
                }
                else if (fusion.get(i) == 9 && i != 0) {
                    fusiongrain(tmpFus, tmpBuf, i,tmp);
                }
                else if (fusion.get(i) == 10 && i != 0) {
                   difference(tmpFus, tmpBuf, i,tmp);
                }
                else if (fusion.get(i) == 11 && i != 0) {
                   addition(tmpFus, tmpBuf, i,tmp);
                }
                else if (fusion.get(i) == 12 && i != 0) {
                   soustraction(tmpFus, tmpBuf, i,tmp);
                }
                else if (fusion.get(i) == 13 && i != 0) {
                   noircirseulement(tmpFus, tmpBuf, i,tmp);
                }
                else if (fusion.get(i) == 14 && i != 0) {
                   eclaircirseulement(tmpFus, tmpBuf, i,tmp);
                }
                g2.drawImage(tmpFus,0,0,null);

            }
        }

        return tmp;
    }
    
     /**
     * Retourne l'image finale de la superposition de tous les calques
     * @param tmpFus image actuelle
     * @param tmpBuf image du calque actuel
     * @param i indice du calque
     * @param tmp Image finale
     */
    public void multiplier(BufferedImage tmpFus, BufferedImage tmpBuf, int i,BufferedImage tmp) {
      
        for (int col = 0; col < tmpBuf.getWidth(); col++) {
            for (int ligne = 0; ligne < tmpBuf.getHeight(); ligne++) {
                    int rgbPrec = tmpBuf.getRGB(col, ligne); //M
                    int rgbSuiv = Calques.calques.get(i-1).getRGB(col, ligne); //I
                    
                    int ca = 255;
                    int cr = 0;
                    int cg = 0;
                    int cb = 0;
                    
                    if (getAlpha(rgbPrec) > 200 && getAlpha(rgbSuiv) > 200)
                    {
                    cr = (getRed(rgbPrec) * getRed(rgbSuiv)) / 255;
                    cg = (getGreen(rgbPrec) * getGreen(rgbSuiv)) / 255;
                    cb = (getBlue(rgbPrec) * getBlue(rgbSuiv)) / 255;
                    }
                    
                    else if (getAlpha(rgbPrec) <= 200 && getAlpha(rgbSuiv) > 200)
                    {
                    cr = getRed(rgbSuiv);
                    cg = getGreen(rgbSuiv);
                    cb = getBlue(rgbSuiv);
                    }
                    
                    else if (getAlpha(rgbPrec) > 200 && getAlpha(rgbSuiv) <= 200)
                    {
                    cr = getRed(rgbPrec);
                    cg = getGreen(rgbPrec);
                    cb = getBlue(rgbPrec);
                    }
                    
                    else if (getAlpha(rgbPrec) <= 200 && getAlpha(rgbSuiv) <= 200)
                    {
                    cr = 0;
                    cg = 0;
                    cb = 0;
                    ca = 0;
                    }

                    if (cr < 0) { cr = 0;} else if (cr > 255) { cr = 255; }
                    if (cg < 0) { cg = 0;} else if (cg > 255) {cg = 255; }
                    if (cb < 0) { cb = 0;} else if (cb > 255) { cb = 255;}
                   

                    tmpFus.setRGB(col, ligne, PaintPanel.makeRGBA(cr, cg, cb, ca));

            }
        }
    }
    
    /**
     * Retourne l'image finale de la superposition de tous les calques
     * @param tmpFus image actuelle
     * @param tmpBuf image du calque actuel
     * @param i indice du calque
     * @param tmp Image finale
     */
    public void diviser(BufferedImage tmpFus, BufferedImage tmpBuf, int i,BufferedImage tmp) {
      
        for (int col = 0; col < tmpBuf.getWidth(); col++) {
            for (int ligne = 0; ligne < tmpBuf.getHeight(); ligne++) {
                    int rgbPrec = tmpBuf.getRGB(col, ligne);
                    int rgbSuiv = Calques.calques.get(i-1).getRGB(col, ligne);

                    int ca=255;
                    int cr=0;
                    int cg=0;
                    int cb=0;
                    
                    
                    if (getAlpha(rgbPrec) > 200 && getAlpha(rgbSuiv) > 200)
                    {
                    cr = (256 * getRed(rgbSuiv))/(getRed(rgbPrec)+1);
                     cg = (256 * getGreen(rgbSuiv))/(getGreen(rgbPrec)+1);
                     cb = (256 * getBlue(rgbSuiv))/(getBlue(rgbPrec)+1);
                    }
                    
                    else if (getAlpha(rgbPrec) <= 200 && getAlpha(rgbSuiv) > 200)
                    {
                    cr = getRed(rgbSuiv);
                    cg = getGreen(rgbSuiv);
                    cb = getBlue(rgbSuiv);
                    }
                    
                    else if (getAlpha(rgbPrec) > 200 && getAlpha(rgbSuiv) <= 200)
                    {
                    cr = getRed(rgbPrec);
                    cg = getGreen(rgbPrec);
                    cb = getBlue(rgbPrec);
                    }
                    
                    else if (getAlpha(rgbPrec) <= 200 && getAlpha(rgbSuiv) <= 200)
                    {
                    cr = 0;
                    cg = 0;
                    cb = 0;
                    ca = 0;
                    }
                    
                 
                    

                   if (cr < 0) { cr = 0;} else if (cr > 255) { cr = 255; }
                   if (cg < 0) { cg = 0;}else if (cg > 255) {cg = 255; }
                   if (cb < 0) { cb = 0;} else if (cb > 255) { cb = 255;}
                  
                    tmpFus.setRGB(col, ligne, PaintPanel.makeRGBA(cr, cg, cb, ca));

            }
        }
    }
    
   
    /**
     * Retourne l'image finale de la superposition de tous les calques
     * @param tmpFus image actuelle
     * @param tmpBuf image du calque actuel
     * @param i indice du calque
     * @param tmp Image finale
     */
   public void ecran(BufferedImage tmpFus, BufferedImage tmpBuf, int i,BufferedImage tmp) {
      
        for (int col = 0; col < tmpBuf.getWidth(); col++) {
            for (int ligne = 0; ligne < tmpBuf.getHeight(); ligne++) {
                    int rgbPrec = tmpBuf.getRGB(col, ligne);
                    int rgbSuiv = Calques.calques.get(i-1).getRGB(col, ligne);
                   
                    int ca=255;
                    int cr=0;
                    int cg=0;
                    int cb=0;
                    
                     if (getAlpha(rgbPrec) > 200 && getAlpha(rgbSuiv) > 200)
                    {
                   cr = 255 - ((255-getRed(rgbPrec))*(255-getRed(rgbSuiv)))/255;
                     cg = 255 - ((255-getGreen(rgbPrec))*(255-getGreen(rgbSuiv)))/255;
                     cb = 255 - ((255-getBlue(rgbPrec))*(255-getBlue(rgbSuiv)))/255;
                    }
                    
                    else if (getAlpha(rgbPrec) <= 200 && getAlpha(rgbSuiv) > 200)
                    {
                    cr = getRed(rgbSuiv);
                    cg = getGreen(rgbSuiv);
                    cb = getBlue(rgbSuiv);
                    }
                    
                    else if (getAlpha(rgbPrec) > 200 && getAlpha(rgbSuiv) <= 200)
                    {
                    cr = getRed(rgbPrec);
                    cg = getGreen(rgbPrec);
                    cb = getBlue(rgbPrec);
                    }
                    
                    else if (getAlpha(rgbPrec) <= 200 && getAlpha(rgbSuiv) <= 200)
                    {
                    cr = 0;
                    cg = 0;
                    cb = 0;
                    ca = 0;
                    }
                    
                    
                    
                 
                
                    if (cr < 0) { cr = 0;} else if (cr > 255) { cr = 255; }
                    if (cg < 0) { cg = 0;} else if (cg > 255) {cg = 255; }
                    if (cb < 0) { cb = 0;} else if (cb > 255) { cb = 255;}
               

                    tmpFus.setRGB(col, ligne, PaintPanel.makeRGBA(cr, cg, cb, ca));

            }
        }
    }
   
   /**
     * Retourne l'image finale de la superposition de tous les calques
     * @param tmpFus image actuelle
     * @param tmpBuf image du calque actuel
     * @param i indice du calque
     * @param tmp Image finale
     */
   public void eclaircir(BufferedImage tmpFus, BufferedImage tmpBuf, int i,BufferedImage tmp) {
      
        for (int col = 0; col < tmpBuf.getWidth(); col++) {
            for (int ligne = 0; ligne < tmpBuf.getHeight(); ligne++) {
                    int rgbPrec = tmpBuf.getRGB(col, ligne);
                    int rgbSuiv = Calques.calques.get(i-1).getRGB(col, ligne);
                              
                    int ca=255;
                    int cr=0;
                    int cg=0;
                    int cb=0;        
                                                
                  
                    if (getAlpha(rgbPrec) > 200 && getAlpha(rgbSuiv) > 200)
                    {
                    cr = (256 * getRed(rgbSuiv)) / ((255 - getRed(rgbPrec))+1);
                     cg = (256 * getGreen(rgbSuiv)) / ((255 - getGreen(rgbPrec))+1);
                     cb = (256 * getBlue(rgbSuiv)) / ((255 - getBlue(rgbPrec))+1);
                    }
                    
                    else if (getAlpha(rgbPrec) <= 200 && getAlpha(rgbSuiv) > 200)
                    {
                    cr = getRed(rgbSuiv);
                    cg = getGreen(rgbSuiv);
                    cb = getBlue(rgbSuiv);
                    }
                    
                    else if (getAlpha(rgbPrec) > 200 && getAlpha(rgbSuiv) <= 200)
                    {
                    cr = getRed(rgbPrec);
                    cg = getGreen(rgbPrec);
                    cb = getBlue(rgbPrec);
                    }
                    
                    else if (getAlpha(rgbPrec) <= 200 && getAlpha(rgbSuiv) <= 200)
                    {
                    cr = 0;
                    cg = 0;
                    cb = 0;
                    ca = 0;
                    }
                    
                    
                    if (cr < 0) { cr = 0;} else if (cr > 255) { cr = 255; }
                    if (cg < 0) { cg = 0;} else if (cg > 255) {cg = 255; }
                    if (cb < 0) { cb = 0;} else if (cb > 255) { cb = 255;}
                

                    tmpFus.setRGB(col, ligne, PaintPanel.makeRGBA(cr, cg, cb, ca));

            }
        }
    }
    



   /**
     * Retourne l'image finale de la superposition de tous les calques
     * @param tmpFus image actuelle
     * @param tmpBuf image du calque actuel
     * @param i indice du calque
     * @param tmp Image finale
     */
     public void assombrir(BufferedImage tmpFus, BufferedImage tmpBuf, int i,BufferedImage tmp) {

        for (int col = 0; col < tmpBuf.getWidth(); col++) {
            for (int ligne = 0; ligne < tmpBuf.getHeight(); ligne++) {
                    int rgbPrec = tmpBuf.getRGB(col, ligne); // M
                    int rgbSuiv = Calques.calques.get(i-1).getRGB(col, ligne); // I
                    
                    int ca=255;
                    int cr=0;
                    int cg=0;
                    int cb=0;  
                    
                    if (getAlpha(rgbPrec) > 200 && getAlpha(rgbSuiv) > 200)
                    {
                     cr = 255 - ( (256 * (255 - getRed(rgbSuiv)))/ (getRed(rgbPrec)+1));
                     cg = 255 - ( (256 * (255 - getGreen(rgbSuiv)))/ (getGreen(rgbPrec)+1));
                     cb = 255 - ( (256 * (255 - getBlue(rgbSuiv)))/ (getBlue(rgbPrec)+1));
                    }
                    
                    else if (getAlpha(rgbPrec) <= 200 && getAlpha(rgbSuiv) > 200)
                    {
                    cr = getRed(rgbSuiv);
                    cg = getGreen(rgbSuiv);
                    cb = getBlue(rgbSuiv);
                    }
                    
                    else if (getAlpha(rgbPrec) > 200 && getAlpha(rgbSuiv) <= 200)
                    {
                    cr = getRed(rgbPrec);
                    cg = getGreen(rgbPrec);
                    cb = getBlue(rgbPrec);
                    }
                    
                    else if (getAlpha(rgbPrec) <= 200 && getAlpha(rgbSuiv) <= 200)
                    {
                    cr = 0;
                    cg = 0;
                    cb = 0;
                    ca = 0;
                    }
                    
                    
                   
                    if (cr < 0) { cr = 0;} else if (cr > 255) { cr = 255; }
                    if (cg < 0) { cg = 0;} else if (cg > 255) {cg = 255; }
                    if (cb < 0) { cb = 0;} else if (cb > 255) { cb = 255;}
              
                    tmpFus.setRGB(col, ligne, PaintPanel.makeRGBA(cr, cg, cb, ca));

            }
        }
    }
     
     /**
     * Retourne l'image finale de la superposition de tous les calques
     * @param tmpFus image actuelle
     * @param tmpBuf image du calque actuel
     * @param i indice du calque
     * @param tmp Image finale
     */
    public void lumieredure(BufferedImage tmpFus, BufferedImage tmpBuf, int i,BufferedImage tmp) {

        for (int col = 0; col < tmpBuf.getWidth(); col++) {
            for (int ligne = 0; ligne < tmpBuf.getHeight(); ligne++) {
                    int rgbPrec = tmpBuf.getRGB(col, ligne); // M
                    int rgbSuiv = Calques.calques.get(i-1).getRGB(col, ligne); // I

                    int ca=255;
                    int cr=0;
                    int cg=0;
                    int cb=0;

                    if (getAlpha(rgbPrec) > 200 && getAlpha(rgbSuiv) > 200)
                    {
                    if (getRed(rgbPrec) <= 128)
                    cr = ((2*getRed(rgbPrec)*getRed(rgbSuiv))/255);
                    else 
                    cr = (255 - ((255-(2*(getRed(rgbPrec)-128)))*(255-getRed(rgbSuiv)))/255);
                   

                    if (getGreen(rgbPrec) <= 128)
                    cg = ((2*getGreen(rgbPrec)*getGreen(rgbSuiv))/255);
                    else
                    cg = (255 - ((255-(2*(getGreen(rgbPrec)-128)))*(255-getGreen(rgbSuiv)))/255);
                    

                    if (getBlue(rgbPrec) <= 128)
                    cb = ((2*getBlue(rgbPrec)*getBlue(rgbSuiv))/255);
                    else
                    cb = (255 - ((255-(2*(getBlue(rgbPrec)-128)))*(255-getBlue(rgbSuiv)))/255);
                    
                    }
                    
                    else if (getAlpha(rgbPrec) <= 200 && getAlpha(rgbSuiv) > 200)
                    {
                    cr = getRed(rgbSuiv);
                    cg = getGreen(rgbSuiv);
                    cb = getBlue(rgbSuiv);
                    }
                    
                    else if (getAlpha(rgbPrec) > 200 && getAlpha(rgbSuiv) <= 200)
                    {
                    cr = getRed(rgbPrec);
                    cg = getGreen(rgbPrec);
                    cb = getBlue(rgbPrec);
                    }
                    
                    else if (getAlpha(rgbPrec) <= 200 && getAlpha(rgbSuiv) <= 200)
                    {
                    cr = 0;
                    cg = 0;
                    cb = 0;
                    ca = 0;
                    }
                    
              


                    if (cr < 0) { cr = 0;} else if (cr > 255) { cr = 255; }
                    if (cg < 0) { cg = 0;} else if (cg > 255) {cg = 255; }
                    if (cb < 0) { cb = 0;} else if (cb > 255) { cb = 255;}
                

                    tmpFus.setRGB(col, ligne, PaintPanel.makeRGBA(cr, cg, cb, ca));

            }
        }
    }
    
    /**
     * Retourne l'image finale de la superposition de tous les calques
     * @param tmpFus image actuelle
     * @param tmpBuf image du calque actuel
     * @param i indice du calque
     * @param tmp Image finale
     */
     public void lumieredouce(BufferedImage tmpFus, BufferedImage tmpBuf, int i,BufferedImage tmp) {

        for (int col = 0; col < tmpBuf.getWidth(); col++) {
            for (int ligne = 0; ligne < tmpBuf.getHeight(); ligne++) {
                    int rgbPrec = tmpBuf.getRGB(col, ligne); // M
                    int rgbSuiv = Calques.calques.get(i-1).getRGB(col, ligne); // I

                    int rscr = 255 - (((255-getRed(rgbPrec))*(255-getRed(rgbSuiv)))/255);
                    int rscg = 255 - (((255-getGreen(rgbPrec))*(255-getGreen(rgbSuiv)))/255);
                    int rscb = 255 - (((255-getBlue(rgbPrec))*(255-getBlue(rgbSuiv)))/255);
                    int rsca = 255 - (((255-getAlpha(rgbPrec))*(255-getAlpha(rgbSuiv)))/255);

                    if (rscr < 0) { rscr = 0;} else if (rscr > 255) { rscr = 255; }
                    if (rscg < 0) { rscg = 0;} else if (rscg > 255) {rscg = 255; }
                    if (rscb < 0) { rscb = 0;} else if (rscb > 255) { rscb = 255;}
                    if (rsca < 0) { rsca = 0;} else if (rsca > 255) {rsca = 255;}

                    
                    
                   int cr = ((((255-getRed(rgbSuiv))*(getRed(rgbPrec))+rscr))/255)*getRed(rgbSuiv);
                   int cg = ((((255-getGreen(rgbSuiv))*(getGreen(rgbPrec))+rscg))/255)*getGreen(rgbSuiv);
                   int cb = ((((255-getBlue(rgbSuiv))*(getBlue(rgbPrec))+rscb))/255)*getBlue(rgbSuiv);
                   int ca = ((((255-getAlpha(rgbSuiv))*(getAlpha(rgbPrec))+rsca))/255)*getAlpha(rgbSuiv);



                    if (cr < 0) { cr = 0;} else if (cr > 255) { cr = 255; }
                    if (cg < 0) { cg = 0;} else if (cg > 255) {cg = 255; }
                    if (cb < 0) { cb = 0;} else if (cb > 255) { cb = 255;}
                    if (ca < 0) { ca = 0;} else if (ca > 255) {ca = 255;}

                    tmpFus.setRGB(col, ligne, PaintPanel.makeRGBA(cr, cg, cb, ca));

            }
        }
    }
     
     /**
     * Retourne l'image finale de la superposition de tous les calques
     * @param tmpFus image actuelle
     * @param tmpBuf image du calque actuel
     * @param i indice du calque
     * @param tmp Image finale
     */
     public void extractiongrain(BufferedImage tmpFus, BufferedImage tmpBuf, int i,BufferedImage tmp) {

        for (int col = 0; col < tmpBuf.getWidth(); col++) {
            for (int ligne = 0; ligne < tmpBuf.getHeight(); ligne++) {
                    int rgbPrec = tmpBuf.getRGB(col, ligne); // M
                    int rgbSuiv = Calques.calques.get(i-1).getRGB(col, ligne); // I

                    int ca=255;
                    int cr=0;
                    int cg=0;
                    int cb=0;
                    
                    if (getAlpha(rgbPrec) > 200 && getAlpha(rgbSuiv) > 200)
                    {
                    cr = getRed(rgbSuiv) - getRed(rgbPrec) + 128;
                    cb = getBlue(rgbSuiv) - getBlue(rgbPrec) + 128;
                    cg = getGreen(rgbSuiv) - getGreen(rgbPrec) + 128;
                    }
                    
                    else if (getAlpha(rgbPrec) <= 200 && getAlpha(rgbSuiv) > 200)
                    {
                    cr = getRed(rgbSuiv);
                    cg = getGreen(rgbSuiv);
                    cb = getBlue(rgbSuiv);
                    }
                    
                    else if (getAlpha(rgbPrec) > 200 && getAlpha(rgbSuiv) <= 200)
                    {
                    cr = getRed(rgbPrec);
                    cg = getGreen(rgbPrec);
                    cb = getBlue(rgbPrec);
                    }
                    
                    else if (getAlpha(rgbPrec) <= 200 && getAlpha(rgbSuiv) <= 200)
                    {
                    cr = 0;
                    cg = 0;
                    cb = 0;
                    ca = 0;
                    }
                    
                

                    if (cr < 0) { cr = 0;} else if (cr > 255) { cr = 255; }
                    if (cg < 0) { cg = 0;} else if (cg > 255) {cg = 255; }
                    if (cb < 0) { cb = 0;} else if (cb > 255) { cb = 255;}
            

                    tmpFus.setRGB(col, ligne, PaintPanel.makeRGBA(cr, cg, cb, ca));

            }
        }
    }
     
     /**
     * Retourne l'image finale de la superposition de tous les calques
     * @param tmpFus image actuelle
     * @param tmpBuf image du calque actuel
     * @param i indice du calque
     * @param tmp Image finale
     */
      public void fusiongrain(BufferedImage tmpFus, BufferedImage tmpBuf, int i,BufferedImage tmp) {

        for (int col = 0; col < tmpBuf.getWidth(); col++) {
            for (int ligne = 0; ligne < tmpBuf.getHeight(); ligne++) {
                    int rgbPrec = tmpBuf.getRGB(col, ligne); // M
                    int rgbSuiv = Calques.calques.get(i-1).getRGB(col, ligne); // I
                    
                    int ca=255;
                    int cr=0;
                    int cg=0;
                    int cb=0;
                    
                    if (getAlpha(rgbPrec) > 200 && getAlpha(rgbSuiv) > 200)
                    {
                     cr = getRed(rgbSuiv) + getRed(rgbPrec) - 128;
                    cb = getBlue(rgbSuiv) + getBlue(rgbPrec) - 128;
                    cg = getGreen(rgbSuiv) + getGreen(rgbPrec) - 128;
                    }
                    
                    else if (getAlpha(rgbPrec) <= 200 && getAlpha(rgbSuiv) > 200)
                    {
                    cr = getRed(rgbSuiv);
                    cg = getGreen(rgbSuiv);
                    cb = getBlue(rgbSuiv);
                    }
                    
                    else if (getAlpha(rgbPrec) > 200 && getAlpha(rgbSuiv) <= 200)
                    {
                    cr = getRed(rgbPrec);
                    cg = getGreen(rgbPrec);
                    cb = getBlue(rgbPrec);
                    }
                    
                    else if (getAlpha(rgbPrec) <= 200 && getAlpha(rgbSuiv) <= 200)
                    {
                    cr = 0;
                    cg = 0;
                    cb = 0;
                    ca = 0;
                    }
                    
                  
                  

                    if (cr < 0) { cr = 0;} else if (cr > 255) { cr = 255; }
                    if (cg < 0) { cg = 0;} else if (cg > 255) {cg = 255; }
                    if (cb < 0) { cb = 0;} else if (cb > 255) { cb = 255;}
               
                    tmpFus.setRGB(col, ligne, PaintPanel.makeRGBA(cr, cg, cb, ca));

            }
        }
    }
      
      /**
     * Retourne l'image finale de la superposition de tous les calques
     * @param tmpFus image actuelle
     * @param tmpBuf image du calque actuel
     * @param i indice du calque
     * @param tmp Image finale
     */
        public void difference(BufferedImage tmpFus, BufferedImage tmpBuf, int i,BufferedImage tmp) {

        for (int col = 0; col < tmpBuf.getWidth(); col++) {
            for (int ligne = 0; ligne < tmpBuf.getHeight(); ligne++) {
                    int rgbPrec = tmpBuf.getRGB(col, ligne); // M
                    int rgbSuiv = Calques.calques.get(i-1).getRGB(col, ligne); // I
                    
                    int ca=255;
                    int cr=0;
                    int cg=0;
                    int cb=0;
                    
                    if (getAlpha(rgbPrec) > 200 && getAlpha(rgbSuiv) > 200)
                    {
                    cr = Math.abs(getRed(rgbSuiv) - getRed(rgbPrec));
                    cb = Math.abs(getBlue(rgbSuiv) - getBlue(rgbPrec));
                    cg = Math.abs(getGreen(rgbSuiv) - getGreen(rgbPrec));
                    }
                    
                    else if (getAlpha(rgbPrec) <= 200 && getAlpha(rgbSuiv) > 200)
                    {
                    cr = getRed(rgbSuiv);
                    cg = getGreen(rgbSuiv);
                    cb = getBlue(rgbSuiv);
                    }
                    
                    else if (getAlpha(rgbPrec) > 200 && getAlpha(rgbSuiv) <= 200)
                    {
                    cr = getRed(rgbPrec);
                    cg = getGreen(rgbPrec);
                    cb = getBlue(rgbPrec);
                    }
                    
                    else if (getAlpha(rgbPrec) <= 200 && getAlpha(rgbSuiv) <= 200)
                    {
                    cr = 0;
                    cg = 0;
                    cb = 0;
                    ca = 0;
                    }
                    
                   
                 

                   if (cr > 255) { cr = 255; }
                    if (cg > 255) {cg = 255; }
                     if (cb > 255) { cb = 255;}
                    

                    tmpFus.setRGB(col, ligne, PaintPanel.makeRGBA(cr, cg, cb, ca));

            }
        }
    }
        
        /**
     * Retourne l'image finale de la superposition de tous les calques
     * @param tmpFus image actuelle
     * @param tmpBuf image du calque actuel
     * @param i indice du calque
     * @param tmp Image finale
     */
          public void addition(BufferedImage tmpFus, BufferedImage tmpBuf, int i,BufferedImage tmp) {

        for (int col = 0; col < tmpBuf.getWidth(); col++) {
            for (int ligne = 0; ligne < tmpBuf.getHeight(); ligne++) {
                    int rgbPrec = tmpBuf.getRGB(col, ligne); // M
                    int rgbSuiv = Calques.calques.get(i-1).getRGB(col, ligne); // I
                    
                    int ca=255;
                    int cr=0;
                    int cg=0;
                    int cb=0;
                    
                    if (getAlpha(rgbPrec) > 200 && getAlpha(rgbSuiv) > 200)
                    {
                     cr = (getRed(rgbSuiv) + getRed(rgbPrec));
                    cb = (getBlue(rgbSuiv) + getBlue(rgbPrec));
                    cg = (getGreen(rgbSuiv) + getGreen(rgbPrec));
                    }
                    
                    else if (getAlpha(rgbPrec) <= 200 && getAlpha(rgbSuiv) > 200)
                    {
                    cr = getRed(rgbSuiv);
                    cg = getGreen(rgbSuiv);
                    cb = getBlue(rgbSuiv);
                    }
                    
                    else if (getAlpha(rgbPrec) > 200 && getAlpha(rgbSuiv) <= 200)
                    {
                    cr = getRed(rgbPrec);
                    cg = getGreen(rgbPrec);
                    cb = getBlue(rgbPrec);
                    }
                    
                    else if (getAlpha(rgbPrec) <= 200 && getAlpha(rgbSuiv) <= 200)
                    {
                    cr = 0;
                    cg = 0;
                    cb = 0;
                    ca = 0;
                    }
                    
                    
                    if (cr > 255) { cr = 255; }
                    if (cg > 255) {cg = 255; }
                    if (cb > 255) { cb = 255;}
                

                    tmpFus.setRGB(col, ligne, PaintPanel.makeRGBA(cr, cg, cb, ca));

            }


        }
    }
          
          /**
     * Retourne l'image finale de la superposition de tous les calques
     * @param tmpFus image actuelle
     * @param tmpBuf image du calque actuel
     * @param i indice du calque
     * @param tmp Image finale
     */
          public void soustraction(BufferedImage tmpFus, BufferedImage tmpBuf, int i,BufferedImage tmp) {

        for (int col = 0; col < tmpBuf.getWidth(); col++) {
            for (int ligne = 0; ligne < tmpBuf.getHeight(); ligne++) {
                    int rgbPrec = tmpBuf.getRGB(col, ligne); // M
                    int rgbSuiv = Calques.calques.get(i-1).getRGB(col, ligne); // I
                    
                    int ca=255;
                    int cr=0;
                    int cg=0;
                    int cb=0;
                    
                    if (getAlpha(rgbPrec) > 200 && getAlpha(rgbSuiv) > 200)
                    {
                    cr = (getRed(rgbSuiv) - getRed(rgbPrec));
                    cb = (getBlue(rgbSuiv) - getBlue(rgbPrec));
                    cg = (getGreen(rgbSuiv) - getGreen(rgbPrec));
                    }
                    
                    else if (getAlpha(rgbPrec) <= 200 && getAlpha(rgbSuiv) > 200)
                    {
                    cr = getRed(rgbSuiv);
                    cg = getGreen(rgbSuiv);
                    cb = getBlue(rgbSuiv);
                    }
                    
                    else if (getAlpha(rgbPrec) > 200 && getAlpha(rgbSuiv) <= 200)
                    {
                    cr = getRed(rgbPrec);
                    cg = getGreen(rgbPrec);
                    cb = getBlue(rgbPrec);
                    }
                    
                    else if (getAlpha(rgbPrec) <= 200 && getAlpha(rgbSuiv) <= 200)
                    {
                    cr = 0;
                    cg = 0;
                    cb = 0;
                    ca = 0;
                    }
                    
                   
                  

                    if (cr < 0) { cr = 0;} else if (cr > 255) { cr = 255; }
                    if (cg < 0) { cg = 0;} else if (cg > 255) {cg = 255; }
                    if (cb < 0) { cb = 0;} else if (cb > 255) { cb = 255;}
                   

                    tmpFus.setRGB(col, ligne, PaintPanel.makeRGBA(cr, cg, cb, ca));

            }


        }
    }
      
          /**
     * Retourne l'image finale de la superposition de tous les calques
     * @param tmpFus image actuelle
     * @param tmpBuf image du calque actuel
     * @param i indice du calque
     * @param tmp Image finale
     */
   public void noircirseulement(BufferedImage tmpFus, BufferedImage tmpBuf, int i,BufferedImage tmp) {

        for (int col = 0; col < tmpBuf.getWidth(); col++) {
            for (int ligne = 0; ligne < tmpBuf.getHeight(); ligne++) {
                    int rgbPrec = tmpBuf.getRGB(col, ligne); // M
                    int rgbSuiv = Calques.calques.get(i-1).getRGB(col, ligne); // I
                    
                    int ca=255;
                    int cr=0;
                    int cg=0;
                    int cb=0;
                    
                     if (getAlpha(rgbPrec) > 200 && getAlpha(rgbSuiv) > 200)
                    {
                    if (getRed(rgbPrec) < getRed(rgbSuiv))
                    cr = getRed(rgbPrec);
                    else 
                    cr = getRed(rgbSuiv);
                    
                    if (getGreen(rgbPrec) < getGreen(rgbSuiv))
                    cg = getGreen(rgbPrec);
                    else 
                    cg = getGreen(rgbSuiv);
                    
                    if (getBlue(rgbPrec) < getBlue(rgbSuiv))
                    cb = getBlue(rgbPrec);
                    else 
                    cb = getBlue(rgbSuiv);
                    }
                    
                    else if (getAlpha(rgbPrec) <= 200 && getAlpha(rgbSuiv) > 200)
                    {
                    cr = getRed(rgbSuiv);
                    cg = getGreen(rgbSuiv);
                    cb = getBlue(rgbSuiv);
                    }
                    
                    else if (getAlpha(rgbPrec) > 200 && getAlpha(rgbSuiv) <= 200)
                    {
                    cr = getRed(rgbPrec);
                    cg = getGreen(rgbPrec);
                    cb = getBlue(rgbPrec);
                    }
                    
                    else if (getAlpha(rgbPrec) <= 200 && getAlpha(rgbSuiv) <= 200)
                    {
                    cr = 0;
                    cg = 0;
                    cb = 0;
                    ca = 0;
                    }
      
                    if (cr < 0) { cr = 0;} else if (cr > 255) { cr = 255; }
                    if (cg < 0) { cg = 0;} else if (cg > 255) {cg = 255; }
                    if (cb < 0) { cb = 0;} else if (cb > 255) { cb = 255;}
                   

                    tmpFus.setRGB(col, ligne, PaintPanel.makeRGBA(cr, cg, cb, ca));

            }


        }
    }
         
   /**
     * Retourne l'image finale de la superposition de tous les calques
     * @param tmpFus image actuelle
     * @param tmpBuf image du calque actuel
     * @param i indice du calque
     * @param tmp Image finale
     */
    public void eclaircirseulement(BufferedImage tmpFus, BufferedImage tmpBuf, int i,BufferedImage tmp) {

        for (int col = 0; col < tmpBuf.getWidth(); col++) {
            for (int ligne = 0; ligne < tmpBuf.getHeight(); ligne++) {
                    int rgbPrec = tmpBuf.getRGB(col, ligne); // M
                    int rgbSuiv = Calques.calques.get(i-1).getRGB(col, ligne); // I
                    
                    int ca=255;
                    int cr=0;
                    int cg=0;
                    int cb=0;
                    
                     if (getAlpha(rgbPrec) > 200 && getAlpha(rgbSuiv) > 200)
                    {
                     if (getRed(rgbPrec) > getRed(rgbSuiv))
                    cr = getRed(rgbPrec);
                    else 
                    cr = getRed(rgbSuiv);
                    
                    if (getGreen(rgbPrec) > getGreen(rgbSuiv))
                    cg = getGreen(rgbPrec);
                    else 
                    cg = getGreen(rgbSuiv);
                    
                    if (getBlue(rgbPrec) > getBlue(rgbSuiv))
                    cb = getBlue(rgbPrec);
                    else 
                    cb = getBlue(rgbSuiv);
                    }
                    
                    else if (getAlpha(rgbPrec) <= 200 && getAlpha(rgbSuiv) > 200)
                    {
                    cr = getRed(rgbSuiv);
                    cg = getGreen(rgbSuiv);
                    cb = getBlue(rgbSuiv);
                    }
                    
                    else if (getAlpha(rgbPrec) > 200 && getAlpha(rgbSuiv) <= 200)
                    {
                    cr = getRed(rgbPrec);
                    cg = getGreen(rgbPrec);
                    cb = getBlue(rgbPrec);
                    }
                    
                    else if (getAlpha(rgbPrec) <= 200 && getAlpha(rgbSuiv) <= 200)
                    {
                    cr = 0;
                    cg = 0;
                    cb = 0;
                    ca = 0;
                    }
                    
                   
                    
                    
                  

                    if (cr < 0) { cr = 0;} else if (cr > 255) { cr = 255; }
                    if (cg < 0) { cg = 0;} else if (cg > 255) {cg = 255; }
                    if (cb < 0) { cb = 0;} else if (cb > 255) { cb = 255;}
                   

                    tmpFus.setRGB(col, ligne, PaintPanel.makeRGBA(cr, cg, cb, ca));

            }


        }
    }
         
          
    /**
     * Retourne la composante bleue d'un entier RGB
     * @param RGB Couleur à traiter
     * @return la composante bleue d'un entier RGB
     */
    public static int getBlue(int RGB) {
        return RGB & 0xFF;
    }

    /**
     * Retourne la composante verte d'un entier RGB
     * @param RGB Couleur à traiter
     * @return la composante verte d'un entier RGB
     */
    public static int getGreen(int RGB) {
        return (RGB >> 8) & 0xFF;
    }

    /**
     * Retourne la composante rouge d'un entier RGB
     * @param RGB Couleur à traiter
     * @return la composante rouge d'un entier RGB
     */
    public static int getRed(int RGB) {
        return (RGB >> 16) & 0xFF;
    }

    /**
     * Retourne la composante alpha d'un entier RGB
     * @param RGB Couleur à traiter
     * @return la composante alpha d'un entier RGB
     */
    public static int getAlpha(int RGB) {
        return (RGB >> 24) & 0xFF;
    }
}
