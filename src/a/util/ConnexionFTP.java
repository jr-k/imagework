package a.util;

/* 
Copyright Paul James Mutton, 2001-2004, http://www.jibble.org/

This file is part of SimpleFTP.

This software is dual-licensed, allowing you to choose between the GNU
General Public License (GPL) and the www.jibble.org Commercial License.
Since the GPL may be too restrictive for use in a proprietary application,
a commercial license is also provided. Full license information can be
found at http://www.jibble.org/licenses/

$Author: pjm2 $
$Id: SimpleFTP.java,v 1.2 2004/05/29 19:27:37 pjm2 Exp $

*/
;


import java.io.*;
import java.net.*;
import java.util.*;


/**
 * Classe qui gère l'envoi par FTP
 * @author Jessym
 */
public class ConnexionFTP {
    
    
    /**
     * Crée une instance ConnexionFTP
     */
    public ConnexionFTP() {
        
    }
    
    
    /**
     * Connecte par défaut au port 21 et se connecte au serveur
     * @param host Serveur FTP
     */
    public synchronized void connect(String host) throws IOException {
        connect(host, 21);
    }
    
    
    /**
     * Se connecte à un serveur par le port indiqué en paramètre
     * @param host Serveur FTP
     * @param port Port
     */
    public synchronized void connect(String host, int port) throws IOException {
        connect(host, port, "anonymous", "anonymous");
    }
    
    
     /**
     * Se connecte à un serveur par le port et en utilisant des identifiants indiqués en paramètre
     * @param host Serveur FTP
     * @param port Port
     * @param user Nom d'utilisateur
     * @param pass Mot de passe
     */
    public synchronized void connect(String host, int port, String user, String pass) throws IOException {
        if (socket != null) {
            throw new IOException("Déjà connecté. Déconnecté vous avant.");
        }
        socket = new Socket(host, port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        
        String response = readLine();
        if (!response.startsWith("220 ")) {
            throw new IOException("Réponse inconnue pendant la connexion au serveur: " + response);
        }
        
        sendLine("USER " + user);
        
        response = readLine();
        if (!response.startsWith("331 ")) {
            throw new IOException("Réponse inconnue après l'envoi des identifiants: " + response);
        }
        
        sendLine("PASS " + pass);
        
        response = readLine();
        if (!response.startsWith("230 ")) {
            throw new IOException("Impossible de se connecter avec un tel mot de passe: " + response);
        }
     
        // Connecté !
    }
    
    
    /**
     * Se déconnecte du serveur
     */
    public synchronized void disconnect() throws IOException {
        try {
            sendLine("QUIT");
        }
        finally {
            socket = null;
        }
    }
    
    
    /**
     * Retourne le répertoire de travail courant du serveur
     * @return le répretoire de travail courant du serveur
     */
    public synchronized String pwd() throws IOException {
        sendLine("PWD");
        String dir = null;
        String response = readLine();
        if (response.startsWith("257 ")) {
            int firstQuote = response.indexOf('\"');
            int secondQuote = response.indexOf('\"', firstQuote + 1);
            if (secondQuote > 0) {
                dir = response.substring(firstQuote + 1, secondQuote);
            }
        }
        return dir;
    }


    /**
     * Change le répertoire de travail courant (comme cd). Retourne false si l'opération a échouée
     * @param dir Répertoire dans lequel on veut aller
     * @return flase si l'opération a échouée, true sinon
     */   
    public synchronized boolean cwd(String dir) throws IOException {
        sendLine("CWD " + dir);
        String response = readLine();
        return (response.startsWith("250 "));
    }
    
    
    /**
     * Envoi un fichier sur le serveur FTP
     * Retourne false si le transfère a échouée
     * Le fichier est envoyé en mode PASSIF afin d'éviter les problème de pare-feu ou les problèmes liés aux NAT
     * @param file Fichier à transférer
     * @return false si le transfère a échouée, true sinon
     */
    public synchronized boolean stor(File file) throws IOException {
        if (file.isDirectory()) {
            throw new IOException("Ne peut pas transférer un répertoire.");
        }
        
        String filename = file.getName();

        return stor(new FileInputStream(file), filename);
    }
    
    
    /**
     * Envoi un fichier sur le serveur FTP
     * Retourne false si le transfère a échouée
     * Le fichier est envoyé en mode PASSIF afin d'éviter les problème de pare-feu ou les problèmes liés aux NAT
     * @param inputStream flux du fichier
     * @param filename nom du fichier à transférer
     * @return false si le transfère a échouée, true sinon
     */
    public synchronized boolean stor(InputStream inputStream, String filename) throws IOException {

        BufferedInputStream input = new BufferedInputStream(inputStream);
        
        sendLine("PASV");
        String response = readLine();
        if (!response.startsWith("227 ")) {
            throw new IOException("Requête du mode passif échouée: " + response);
        }
        
        String ip = null;
        int port = -1;
        int opening = response.indexOf('(');
        int closing = response.indexOf(')', opening + 1);
        if (closing > 0) {
            String dataLink = response.substring(opening + 1, closing);
            StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");
            try {
                ip = tokenizer.nextToken() + "." + tokenizer.nextToken() + "." + tokenizer.nextToken() + "." + tokenizer.nextToken();
                port = Integer.parseInt(tokenizer.nextToken()) * 256 + Integer.parseInt(tokenizer.nextToken());
            }
            catch (Exception e) {
                throw new IOException("Mauvise information: " + response);
            }
        }
        
        sendLine("STOR " + filename);
        
        Socket dataSocket = new Socket(ip, port);
        
        response = readLine();
        if (!response.startsWith("150 ")) {
            throw new IOException("Erreur lors de l'envoi du fichier: " + response);
        }
        
        BufferedOutputStream output = new BufferedOutputStream(dataSocket.getOutputStream());
        byte[] buffer = new byte[4096];
        int bytesRead = 0;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        output.flush();
        output.close();
        input.close();
        
        response = readLine();
        return response.startsWith("226 ");
    }


    /**
     * Passer en mode binaire 
     * @return Retourne vrai si l'action a réussi, false sinon
     */
    public synchronized boolean bin() throws IOException {
        sendLine("TYPE I");
        String response = readLine();
        return (response.startsWith("200 "));
    }
    
    
     /**
     * Passer en mode ascii 
     * @return Retourne vrai si l'action a réussi, false sinon
     */
    public synchronized boolean ascii() throws IOException {
        sendLine("TYPE A");
        String response = readLine();
        return (response.startsWith("200 "));
    }
    
    
    /**
     * Envoi une commande FTP au serveur
     * @param line Commande à envoyer
     */
    private void sendLine(String line) throws IOException {
        if (socket == null) {
            throw new IOException("Connecté.");
        }
        try {
            writer.write(line + "\r\n");
            writer.flush();
            if (DEBUG) {
                System.out.println("> " + line);
            }
        }
        catch (IOException e) {
            socket = null;
            throw e;
        }
    }
    
    private String readLine() throws IOException {
        String line = reader.readLine();
        if (DEBUG) {
            System.out.println("< " + line);
        }
        return line;
    }
    
    private Socket socket = null;
    private BufferedReader reader = null;
    private BufferedWriter writer = null;
    
    private static boolean DEBUG = false;
    
    
}