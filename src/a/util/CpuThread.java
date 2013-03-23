package a.util;


import a.window.CpuWindow;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;

/**
 * Classe qui gère périodiquement l'affichage des statistiques en temps réel 
 * @author Jessym
 */

public class CpuThread implements Runnable {

    /**
     * Constructeur CpuThread
     */
    public CpuThread() {
    }

    /**
     * Lance la routine d'exécution du Thread
     */
    public void run() {

        while (1 == 1) {
            try {
                Thread.sleep(1000);
                searchInfo();
                CpuWindow.graph.repaint();
                CpuWindow.graph.go = true;
            } catch (InterruptedException ex) {
            }
        }
    }

    /**
     * Cherche les informations systèmes relatives à la consommation en temps CPU
     */
    public void searchInfo() {

        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        for (Method method : operatingSystemMXBean.getClass().getDeclaredMethods()) {
            method.setAccessible(true);
            if (method.getName().startsWith("get") || method.getName().startsWith("get")) {
                Object value;
                try {
                    value = method.invoke(operatingSystemMXBean);
                } catch (Exception e) {
                    value = e;
                }
                if (method.getName().equals("getProcessCpuLoad")) {
                    CpuWindow.processCpuLoad = value;
                } else if (method.getName().equals("getSystemCpuLoad")) {
                    CpuWindow.systemCpuLoad = value;
                }

            }
        }
    }
}