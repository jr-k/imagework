package a.dialog;


import javax.swing.JFormattedTextField;

/**
 * Classe relative aux informations de la matrice de convolution 2D 
 * @author Jessym
 */
public class DialogConvolutionInfo {

	
	float[] matrice = new float[9];
	
        /**
         * Constructeur des informations de la matrice de convolution
         * @param m valeurs de la matrice dans des champs texte
         */
	public DialogConvolutionInfo(JFormattedTextField[] m) {
		
		for (int i = 0; i<m.length;i++)
                {
                    try {this.matrice[i] = Float.parseFloat(m[i].getText());}
                    catch(NumberFormatException e)
                    {
                        this.matrice[i] = 0F;
                    }
                }
	}

        /**
         * Renvoi la matrice de convolution
         * @return la matrice de convolution
         */
	public float[] getMatrice() {
		return matrice;
	}

	
	
	
	
}
