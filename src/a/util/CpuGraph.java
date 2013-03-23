package a.util;


import a.window.CpuWindow;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Classe qui gère le dessin des statistiques
 * @author Jessym
 */
public class CpuGraph extends JPanel {

    public static boolean go = false;

    /**
     * Constructeur CpuGrah
     */
    public CpuGraph() {
        this.setSize(150, 60);

    }
    static Vector<Integer> dotsSys = new Vector();
    static Vector<Integer> dotsProc = new Vector();

    /**
     * Fonction qui dessine le graphe dans le composant
     * @param g Object graphique lié au panneau engloant
     */
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        BufferedImage img = null;
        try {
            img = ImageIO.read(getClass().getResource("/misc/graph.png"));
        } catch (IOException ex) {
        }

        TexturePaint tp = new TexturePaint(img, new Rectangle(0, 0, this.getWidth(), this.getHeight()));
        g2.setPaint(tp);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());

        if (!CpuWindow.processCpuLoad.toString().equals("-1.0") && !CpuWindow.systemCpuLoad.toString().equals("-1.0")
                && !CpuWindow.processCpuLoad.toString().equals("0") && !CpuWindow.systemCpuLoad.toString().equals("0")) {

            int PercentVPCL = (int) Math.round(Double.parseDouble(CpuWindow.processCpuLoad.toString()) * 100);
            int PercentVSCL = (int) Math.round(Double.parseDouble(CpuWindow.systemCpuLoad.toString()) * 100);

            g2.setColor(Color.GREEN);
            g2.setFont(new Font("Arial", Font.BOLD, 10));
            g2.drawString("System : " + PercentVSCL + "%", this.getWidth() - 76, 10);

            g2.setColor(Color.decode("0x00ffCC"));
            g2.setFont(new Font("Arial", Font.BOLD, 10));
            g2.drawString("Process : " + PercentVPCL + "%", this.getWidth() - 78, 20);

            if (go) {
                dotsSys.add(PercentVSCL);
                dotsProc.add(PercentVPCL);
                go = false;
            }

            int ecart = 3;

            g2.setStroke(new BasicStroke(2F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));

            int re = 0;


            g2.setColor(Color.green);
            for (int i = 1; i < dotsSys.size(); i++) {
                if (this.getHeight() - dotsSys.get(i - 1) > 90) {
                    re = 10;
                } else {
                    re = 0;
                }
                g2.drawLine(((i - 1) * ecart), this.getHeight() - dotsSys.get(i - 1) - re, (i * ecart), this.getHeight() - dotsSys.get(i));
            }

            g2.setColor(Color.decode("0x00ffCC"));
            for (int i = 1; i < dotsProc.size(); i++) {
                g2.drawLine(((i - 1) * ecart), this.getHeight() - dotsProc.get(i - 1), (i * ecart), this.getHeight() - dotsProc.get(i));
            }

            if (dotsSys.size() > (this.getWidth() / ecart)) {
                dotsSys.removeAllElements();
            }

            if (dotsProc.size() > (this.getWidth() / ecart)) {
                dotsProc.removeAllElements();
            }
        }




    }
}
