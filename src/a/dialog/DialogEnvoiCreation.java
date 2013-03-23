package a.dialog;

import a.drawing.*;
import a.window.*;
import a.util.*;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JTextField;


/**
 * Classe d'envoi de création qui gère le contenu du menu "Envoyer votre création !"
 * @author Jessym
 */

public class DialogEnvoiCreation extends JDialog {

    private JPanel container = null;
    private JFormattedTextField titleField;
    private JFormattedTextField authField;
    private JTextPane descField;
    private JComboBox typeField;
    private JLabel titlelab;
    private JLabel authLab;
    private JLabel typeLab;
    private JLabel desclab;
    private JLabel indec;
    private JDialog dialog = this;
    public boolean connected = false;

    
    /**
     * Constructeur DialogEnvoiCreation
     * @param parent fenêtre parent
     * @param name nom de la fenêtre par défaut
     * @param modal fenêtre bloquante pour l'application ou non
     */
    public DialogEnvoiCreation(JFrame parent, String name, boolean modal) {
        super(parent, name, modal);
        this.setSize(440, 440);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        this.initDialog();


    }

    /**
     * Affiche la boîte de dialogue
     */
    public void showCreationDialog() {
        this.setVisible(true);
    }

    
    /**
     * Initialise la boîte de dialogue
     */
    public void initDialog() {

        JPanel nomPane = new JPanel();
        nomPane.setBackground(Color.white);
        nomPane.setPreferredSize(new Dimension(400, 50));
        titlelab = new JLabel("Nom : " + PaintWindow.NAME.length() + "/12 caractères");
        nomPane.setBorder(BorderFactory.createEtchedBorder());
        titleField = new JFormattedTextField(PaintWindow.NAME);
        titleField.setPreferredSize(new Dimension(100, 30));
        nomPane.add(titlelab);
        nomPane.add(titleField);
        titleField.addKeyListener(new LengthKeyTitle());

        JPanel authPane = new JPanel();
        authPane.setBackground(Color.white);
        authPane.setPreferredSize(new Dimension(400, 50));
        authLab = new JLabel("Auteur : 0/12 caractères");
        authPane.setBorder(BorderFactory.createEtchedBorder());
        authField = new JFormattedTextField("");
        authField.setPreferredSize(new Dimension(100, 30));
        authPane.add(authLab);
        authPane.add(authField);
        authField.addKeyListener(new LengthKeyAuth());


        JPanel typePane = new JPanel();
        typePane.setBackground(Color.white);
        typePane.setPreferredSize(new Dimension(400, 50));
        typeLab = new JLabel("Catégorie : ");
        typePane.setBorder(BorderFactory.createEtchedBorder());
        typeField = new JComboBox();
        typeField.addItem("Art");
        typeField.addItem("Dessin");
        typeField.addItem("Photo");
        typeField.addItem("Jeu");
        typeField.addItem("Divers");
        typeField.setPreferredSize(new Dimension(100, 30));
        typePane.add(typeLab);
        typePane.add(typeField);

        JPanel descPane = new JPanel(new BorderLayout());
        descPane.setBackground(Color.white);
        descPane.setPreferredSize(new Dimension(400, 100));
        desclab = new JLabel("Description : 0/180 caractères");
        desclab.setPreferredSize(new Dimension(300, 30));
        descPane.setBorder(BorderFactory.createEtchedBorder());
        descField = new JTextPane();
        descField.setPreferredSize(new Dimension(200, 80));
        descField.setMaximumSize(new Dimension(200, 80));
        descPane.add(desclab, BorderLayout.NORTH);
        descField.setBorder(BorderFactory.createLineBorder(Color.black));



        JScrollPane areaScroll = new JScrollPane(descField);
        areaScroll.setPreferredSize(new Dimension(200, 80));
        descField.addKeyListener(new MCKeyListener());
        descPane.add(areaScroll, BorderLayout.CENTER);


        JPanel indicPane = new JPanel();
        indicPane.setBackground(Color.white);
        indicPane.setPreferredSize(new Dimension(400, 50));
        indec = new JLabel("Tous les champs doivent être correctement remplis ! ");
        JLabel index2 = new JLabel(" Essayez d'exécuter ImageWork en tant qu'administrateur si jamais cela échoue.");
        indicPane.setBorder(BorderFactory.createEtchedBorder());
        indicPane.add(indec);
        indicPane.add(index2);

        JPanel content = new JPanel();
        content.setBackground(Color.white);
        content.add(nomPane);
        content.add(authPane);
        content.add(typePane);
        content.add(descPane);
        content.add(indicPane);




        content.setBorder(BorderFactory.createTitledBorder("Envoi de votre création"));

        JPanel control = new JPanel();
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("FERMER");
        control.add(ok);
        control.add(cancel);

        ok.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                String nom = (String) titleField.getText();
                String auteur = (String) authField.getText();
                String type = (String) typeField.getSelectedItem();
                String desc = (String) descField.getText();

                if (nom.length() > 0 && auteur.length() > 0 && type.length() > 0 && desc.length() > 0) {


                    //  netsh advfirewall set global StatefulFTP disable

                    // préparation des données pour l'envoi
                    Date Tdate = new Date();
                    String key = (Tdate.toString()).replaceAll(" ", "");
                    key = key.replaceAll(":", "");
                    String timet = key + "tmp.png";
                    BufferedImage tmpBuf = PaintPanel.layers.getFinalImage();

                    String projectName = "." + titleField.getText().length() + titleField.getText();
                    projectName += "." + authField.getText().length() + authField.getText();
                    projectName += "." + ((String) typeField.getSelectedItem()).length() + ((String) typeField.getSelectedItem()).toLowerCase();
                    String name = projectName + "." + key + "tmp.png";


                    // création du fichier temporaie   
                    File file = new File(name);
                    FileImageOutputStream fios;
                    try {
                        fios = new FileImageOutputStream(file);
                        ImageIO.write(tmpBuf, "PNG", fios);
                        fios.close();

                    } catch (Exception foe) {
                        file.delete();
                    }

                    // gestion de l'envoi au FTP
                    try {
                        ConnexionFTP ftpClient = new ConnexionFTP();
                        ftpClient.connect("ftpperso.free.fr", 21, "imagework", "<pzdxotffa");
                        ftpClient.bin();
                        ftpClient.cwd("gallery");
                        ftpClient.cwd(((String) typeField.getSelectedItem()).toLowerCase());

                        InputStream input = new BufferedInputStream(new FileInputStream(file));
                        System.out.println(name);
                        ftpClient.stor(input, name);
                        ftpClient.disconnect();

                        // Gestion de la requête PHP => SQL
                        try {

                            String a = "http://imagework.free.fr/sendGalleryRequest.php?";
                            a += "titre=" + titleField.getText();
                            a += "&auteur=" + authField.getText();
                            a += "&type=" + ((String) typeField.getSelectedItem()).toLowerCase();
                            a += "&timet=" + timet;

                            URL url = new URL(a);
                            URLConnection urlConnection = url.openConnection();
                            urlConnection.setDoInput(true);
                            urlConnection.setDoOutput(true);

                            DataOutputStream outStream = new DataOutputStream(urlConnection.getOutputStream());
                            DataInputStream inStream = new DataInputStream(urlConnection.getInputStream());

                        } catch (IOException ex) {
                            file.delete();
                        }

                        // Suppression du fichier temporaire
                        file.delete();
                        setVisible(false);

                        JLinkDialog oke = new JLinkDialog(null, "Féliciations", true,
                                "Envoi réussi ! ", "www.imagework.fr.nf");
                        oke.showDialog();
                    } catch (Exception efo) {
                        file.delete();
                        efo.printStackTrace();
                    }
                } else {
                    JOptionPane jop = new JOptionPane();
                    jop.showMessageDialog(null, "Tous les champs doivent être"
                            + "remplis !", "Erreur", JOptionPane.ERROR_MESSAGE);
                }

            }
        });


        cancel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });



        this.getContentPane().add(content, BorderLayout.CENTER);
        this.getContentPane().add(control, BorderLayout.SOUTH);
    }

    
    /**
     * Classe interne qui gère la bonne saisie des caractères au sein de la boîte dialogue
     */
    class MCKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent event) {
            JTextPane jtf = ((JTextPane) event.getSource());
            int n = jtf.getText().length();
            desclab.setText("Description : " + n + "/180 caractères");

        }

        @Override
        public void keyPressed(KeyEvent event) {
            JTextPane jtf = ((JTextPane) event.getSource());
            int n = jtf.getText().length();
            desclab.setText("Description : " + n + "/180 caractères");

        }

        @Override
        public void keyReleased(KeyEvent event) {
            JTextPane jtf = ((JTextPane) event.getSource());
            int n = jtf.getText().length();
            desclab.setText("Description : " + n + "/180 caractères");



            if (jtf.getText().length() > 180) {
                jtf.setText(jtf.getText().substring(0, 180));
            }
        }
    }

     /**
     * Classe interne qui gère la bonne saisie des caractères au sein de la boîte dialogue
     */
    class LengthKeyAuth implements KeyListener {

        @Override
        public void keyTyped(KeyEvent event) {
            JTextField jtf = ((JTextField) event.getSource());
            int n = jtf.getText().length();
            authLab.setText("Auteur : " + n + "/12 caractères");

        }

        @Override
        public void keyPressed(KeyEvent event) {
            JTextField jtf = ((JTextField) event.getSource());
            int n = jtf.getText().length();
            authLab.setText("Auteur : " + n + "/12 caractères");

        }

        @Override
        public void keyReleased(KeyEvent event) {
            JTextField jtf = ((JTextField) event.getSource());
            int n = jtf.getText().length();
            authLab.setText("Auteur : " + n + "/12 caractères");

            String test = event.getKeyChar() + "";
            if (!test.matches("[a-zA-Z0-9éèêîôûâàùûïöëäüç]")) {
                jtf.setText(jtf.getText().replace(String.valueOf(event.getKeyChar()), ""));
            }

            if (jtf.getText().length() > 12) {
                jtf.setText(jtf.getText().substring(0, 12));
            }
        }
    }

     /**
     * Classe interne qui gère la bonne saisie des caractères au sein de la boîte dialogue
     */
    class LengthKeyTitle implements KeyListener {

        @Override
        public void keyTyped(KeyEvent event) {
            JTextField jtf = ((JTextField) event.getSource());
            int n = jtf.getText().length();
            titlelab.setText("Nom : " + n + "/12 caractères");

        }

        @Override
        public void keyPressed(KeyEvent event) {
            JTextField jtf = ((JTextField) event.getSource());
            int n = jtf.getText().length();
            titlelab.setText("Nom : " + n + "/12 caractères");

        }

        @Override
        public void keyReleased(KeyEvent event) {
            JTextField jtf = ((JTextField) event.getSource());
            int n = jtf.getText().length();
            titlelab.setText("Nom : " + n + "/12 caractères");

            String test = event.getKeyChar() + "";
            if (!test.matches("[a-zA-Z0-9éèêîôûâàùûïöëäüç]")) {
                jtf.setText(jtf.getText().replace(String.valueOf(event.getKeyChar()), ""));
            }

            if (jtf.getText().length() > 12) {
                jtf.setText(jtf.getText().substring(0, 12));
            }
        }
    }
}
