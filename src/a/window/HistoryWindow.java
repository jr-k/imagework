package a.window;


import a.drawing.Calques;
import a.drawing.HistoryGroup;
import a.drawing.SMenuBar;
import a.util.ResizeImage;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


/**
 * Gère la fenêtre de l'historique d'un calque dans l'espace de travail
 * @author Jessym
 */
public class HistoryWindow extends JInternalFrame {

    
    static JPanel histPane = new JPanel();
    
    /**
     * Créer une fenêtre des calques dans l'espace de travail
     * @param w longueur de la fenêtre
     * @param h hauteur de la fenêtre
     * @param x position x de la fenêtre
     * @param y position y de la fenêtre
     */
    public HistoryWindow(int w, int h, int x, int y) {
        this.setTitle("Historique");
        this.setClosable(true);
        this.setResizable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
        this.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        this.setSize(w, h);
        this.setPreferredSize(new Dimension(w, h));
        this.setLocation(x, y);

        try {
                this.setFrameIcon(new ImageIcon(ImageIO.read(getClass().getResource("/icons/historiqueicon.png"))));
            } catch (IOException ex) {
                Logger.getLogger(IWCameraWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
         // Gestion Scrolling                 
        this.setMaximumSize(new Dimension(w,1000));
        this.setMinimumSize(new Dimension(w,100));
             
        
        JScrollPane jsp = new JScrollPane(histPane,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);              
        jsp.getHorizontalScrollBar().setUnitIncrement(92);
        this.setContentPane(jsp);
        
        
        JMenuBar jmb = new JMenuBar();
        
        JMenuItem jmmi = new JMenuItem("Supprimer l'historique");
        jmmi.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){ 
        
            Calques.historiques.get(Calques.current).deleteAll();
            CalquesWindow.refreshCalques(Calques.calques);
        }});
        jmb.add(jmmi);

        this.setJMenuBar(jmb);
        this.setVisible(true);
    }

    /**
     * Rafraîchi la fenêtre historique
     * @param tmp Vecteur d'image du calque courant
     */
    public static void refreshHistory(Vector<BufferedImage> tmp)
    {
       histPane.removeAll();

       //int newHeight = 0;

        for (int i = tmp.size()-1; i >= 0; i--)
        {
            BufferedImage originalImage = tmp.get(i);
	    int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

            ResizeImage.IMG_WIDTH = 85;
            ResizeImage.IMG_HEIGHT = 85;
	    BufferedImage resizeImageHint = ResizeImage.resizeImageWithHint(originalImage, type);
                
            HistoryPane hp = new HistoryPane(resizeImageHint,i,resizeImageHint.getWidth(),resizeImageHint.getHeight());
            
            histPane.add(hp);
            
           // newHeight+=resizeImageHint.getHeight()+100;
        }
       
        histPane.revalidate();
        
         HistoryGroup g = Calques.historiques.get(Calques.current);
         
        

    }

    /**
     * Panneau qui contient les miniatures des historiques
     */
    static class HistoryPane extends JPanel implements MouseListener{
        
        HistoryPaneGI hpgi = null;
        int rang = 0;
        JLabel name ;
        
        /**
         * Constructeur HistoryPane
         * @param tmp Miniature à instérer
         * @param r Rang de l'image historique
         * @param w Longueur du panneau d'affichage
         * @param h Hauteur du panneau d'affichage
         */
        public HistoryPane(BufferedImage tmp, int r,int w, int h){
            hpgi = new HistoryPaneGI(tmp,w,h,Calques.opacity.get(rang));
            rang = r;
            name = new JLabel("n° "+(rang+1));
            name.setPreferredSize(new Dimension(85,20));
            name.setHorizontalAlignment(JTextField.CENTER);
            this.setLayout(new BorderLayout());
            name.setForeground(Color.white);
            name.setFont(new Font("Arial",Font.BOLD,12));
            
            this.add(name,BorderLayout.NORTH);
            this.add(hpgi,BorderLayout.CENTER);
            this.setBorder(BorderFactory.createLineBorder(Color.decode("0xBBBBBB")));
            
            this.addMouseListener(this);
        }
        
        public void paintComponent(Graphics g)
        {
                Graphics2D g2 = (Graphics2D)g;
               Color colors[];
               LinearGradientPaint p;
               
               
               if (rang==Calques.historiques.get(Calques.current).getImages().size()-1)
                    colors = new Color[]{ Color.decode("0x133759"),Color.decode("0xc5cfdf")};
               else colors = new Color[]{ Color.decode("0xc5cfdf"),Color.decode("0xdce5f2")};

                p = new LinearGradientPaint(new Point2D.Float(0, 0), new Point2D.Float(0, this.getHeight()), new float[]{0.0F,1.0F}, colors); 
                g2.setPaint(p);
                g2.fillRect(0,0,this.getWidth(),20);
                g2.fillRect(0,20,this.getWidth(),this.getHeight());
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
          
            if ( ((HistoryPane)e.getSource()).rang < Calques.historiques.get(Calques.current).images.size() - 1)
            {
          
                JOptionPane jop = new JOptionPane();
                int confirm = jop.showConfirmDialog(null,"Voulez-vous revenir à l'image n°"+(((HistoryPane)e.getSource()).rang+1)+" ?",
                                                         "Revenir en arrière", 
                                                         JOptionPane.OK_CANCEL_OPTION);
                if (confirm==JOptionPane.OK_OPTION)
                {
                    for (int i = Calques.historiques.get(Calques.current).images.size() - 1; i >((HistoryPane)e.getSource()).rang;i--)
                    {
                         SMenuBar.annuler.doClick();
                    }
                }
            }
            
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        
    }
    
    /**
     * Classe interne qui gère le panneau d'affichage interne de la miniature
     */
    static class HistoryPaneGI extends JPanel{
        
        BufferedImage preview = null;
        int opacite;
        
        /**
         * Constructeur HistoryPaneGI
         * @param tmp Miniature à insérer
         * @param w Longueur du panneau
         * @param h Largeur du panneau
         * @param opaq Opacity lié au calque à prendre en compte lors de l'affichage de la miniature dans ce panneau
         */
        public HistoryPaneGI(BufferedImage tmp, int w, int h, int opaq){
            preview = tmp;
            opacite=opaq;
            this.setPreferredSize(new Dimension(w,h));
            this.setBorder(BorderFactory.createLineBorder(Color.decode("0xc5cfdf")));
        }
        public void paintComponent(Graphics g)
        {
            Graphics2D g2 = (Graphics2D) g;
            
            // Photoshop font style
            //*/
            BufferedImage img = null;
            try {img = ImageIO.read(getClass().getResource( "/misc/transtexture.png" )); } catch (IOException ex) {}

            TexturePaint tp = new TexturePaint(img,new Rectangle(0,0,20,20));
            g2.setPaint(tp);
            g2.fillRect(0,0,this.getWidth(),this.getHeight());
            //*/
            
             AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
             ac = AlphaComposite.getInstance(ac.getRule(), (float) opacite / 100.0F);

             g2.setComposite(ac);
                
             g2.drawImage(preview,0,0,null);
        }
        
    }
  
}
