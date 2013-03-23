package a.util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Classe qui gère les extensions de fichiers du logiciel
 * @author Jessym
 */
public class ImageFileFilter extends FileFilter {

	private String extension = ".png", description = "Fichier image PNG";
	
	/**
         * Constructeur qui crée une nouvelle extension
         * @param ext Nom de l'extension
         * @param descrip Description de l'extension
         */
	public ImageFileFilter(String ext, String descrip){
		this.extension = ext;
		this.description = descrip;
	}
	
        /**
         * Vérifie l'extension du fichier et retourne s'il correspond bien à celui de la classe
         * @param file Fichier d'extension .ext à tester
         * @return vrai si l'extension est conforme, false sinon
         */
	public boolean accept(File file){
		return (file.isDirectory() || file.getName().endsWith(this.extension));
	}
	

        /**
         * Retourne l'extension du FileFilter
         * @return l'extension du FileFilter
         */
	public String getExt() {
		return this.extension;
	}
	
        /**
         * Retourne la description liée à l'extension du FileFilter
         * @return la description liée à l'extension du FileFilter
         */
	public String getDescription(){
		return this.extension;
	}	
}