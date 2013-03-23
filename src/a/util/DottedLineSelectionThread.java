package a.util;

/**
 * Classe qui gère périodiquement l'animation du cadre de séléction
 * @author Jessym
 */

public class DottedLineSelectionThread implements Runnable {
 
        public static float f1 = 2F;
       
        public DottedLineSelectionThread(){}
    
        /**
         * Lancement de la routine d'exécution du Thread
         */
        public void run() {
                
           while(1==1){ 
                try {                    
                    Thread.sleep(300);
                    if (f1==2F) f1=4F; else f1=2F;    
                }
                catch (InterruptedException ex) {}
           }
        }
        
        
}