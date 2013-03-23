package a.drawing;

import a.util.ResizeImage;
import a.window.CalquesWindow;
import a.window.PaintWindow;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * Classe qui gère le redimensionnement en temps réel
 * @author Jessym
 */
public class TrackerResizer extends JPanel implements MouseMotionListener, MouseListener {

    public int x_tracker = 0;
    public int y_tracker = 0;
    public int x_tracker2 = 0;
    public int y_tracker2 = 0;
    public int w_tracker = 0;
    public int h_tracker = 0;
    public boolean dragg_tracker = false;
    public PaintPanel _canvas;

    /**
     * Constructeur TrackerResizer
     * @praram can Panneau de dessin associé
     */
    public TrackerResizer(PaintPanel can) {

        x_tracker = _canvas.WSIZE;
        y_tracker = _canvas.HSIZE;
        x_tracker2 = _canvas.WSIZE;
        y_tracker2 = _canvas.HSIZE;
        w_tracker = _canvas.WSIZE;
        h_tracker = _canvas.HSIZE;
        _canvas = can;
        this.setSize(new Dimension(10000, 10000));
        this.setPreferredSize(new Dimension(10000, 10000));
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
    }

    /**
     * Peint le curseur noir de séléction
     * @param g Object graphique lié au dessin de la classe TrackerResizer
     */
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        int w=8,h=8;
        
        Color colors[] =  new Color[]{Color.decode("0x000000"), Color.decode("0x0000FF")};
        LinearGradientPaint  p = new LinearGradientPaint(new Point2D.Float(0, 0), new Point2D.Float(0, h), new float[]{0.0F, 1.0F}, colors);
        g2.setPaint(p);

        g2.fillRect(0, 0, w, h);
    }

    /**
     * Fonction appelé lors du redimensionnement en drag'n'drop
     * @param e Evénement lié au glisser-déposer de la souris
     */
    public void mouseDragged(MouseEvent e) {
  
        x_tracker = _canvas.WSIZE + (int)(e.getX()/_canvas.zoom);
        y_tracker = _canvas.HSIZE + (int)(e.getY()/_canvas.zoom);
        
        x_tracker2 = (int)Math.round(x_tracker*_canvas.zoom);
        y_tracker2 = (int) Math.round(y_tracker*_canvas.zoom);

        if (x_tracker<=0) x_tracker=1;
        if (y_tracker<=0) y_tracker=1;
        
        if (x_tracker>3000) x_tracker=3000;
        if (y_tracker>3000) y_tracker=3000;
        
        dragg_tracker = true;
        
       
        _canvas._state = PaintPanel.State.DRAGGING;
        _canvas._start = e.getPoint();
        _canvas._prev = e.getPoint();
        _canvas._end = e.getPoint();

        _canvas.drawCurrentShape((Graphics2D) _canvas._bufImage.getGraphics(), false);
        _canvas.drawCurrentShape((Graphics2D) _canvas._bufImage.getGraphics(), false);
        
         PaintWindow.dessin.setTitle(_canvas.titre+" | "+x_tracker+"x"+y_tracker);
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {

    }

    /**
     * Fonction appelé lors du relâchement du clic de la souris
     * @param e Evénement lié au relâchement du clic de la souris
     */
    public void mouseReleased(MouseEvent e) {


        resize();
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
    
    
    public void resize()
    {
        dragg_tracker = false;
        _canvas.WSIZE = x_tracker;
        _canvas.HSIZE = y_tracker;
        

        for (int i=0; i<Calques.calques.size();i++)
        {
            BufferedImage originalImage = Calques.calques.get(i);
	    int type = _canvas.type;

            ResizeImage.IMG_WIDTH = x_tracker;
            ResizeImage.IMG_HEIGHT = y_tracker;
            
	    BufferedImage newImage;
            
            if (_canvas.bratioresize)
            newImage = ResizeImage.resizeImageWithHint(originalImage, type);           
            else
            newImage = _canvas.newCopyResized(Calques.calques.get(i), x_tracker, y_tracker , _canvas.type, _canvas.COLORCHOOSEN);   
         
            Calques.calques.setElementAt(newImage, i);
        }
        
        Calques.historiques.get(Calques.current).add(_canvas.newCopy(_canvas._bufImage, _canvas.type));
        CalquesWindow.refreshCalques(Calques.calques);

        this.setLocation((int)Math.round(x_tracker*_canvas.zoom),(int) Math.round(y_tracker*_canvas.zoom));
        _canvas.repaint();
    }
    
}
