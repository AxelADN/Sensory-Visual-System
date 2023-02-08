/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.components;

import MiniPrograms.RFlist;
import MiniPrograms.RF;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author HumanoideFilms
 */
public class VisPanel extends javax.swing.JPanel {

    /**
     * Creates new form VisPanel
     */
    int ovalw = 0;
    int ovalh = 0;
    int ovalx = -50;
    int ovaly = -50;

    public VisPanel() {
        initComponents();
        setBackground(Color.BLACK);
        repaint();

    }

    public int tx(int x, int w) {
        int width = getWidth();
        int newX = x + width / 2 - w / 2;
        return newX;
    }

    public int ty(int y, int h) {
        int height = getHeight();
        int newY = height / 2 - y - h / 2;
        return newY;
    }

    public void Oval(Graphics g, int x, int y, int w, int h, double angle) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));
        g2d.rotate(Math.toRadians(angle), tx(x, 0), ty(y, 0));
        g.drawOval(tx(x, 2 * w), ty(y, 2 * h), 2 * w, 2 * h);
        g2d.rotate(Math.toRadians(-angle), tx(x, 0), ty(y, 0));
    }

    public void Oval2(Graphics g, int x, int y, int w, int h, Color color) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(1));
        g2d.rotate(Math.toRadians(0), tx(x, 0), ty(y, 0));
        g.drawOval(tx(x, 2 * w), ty(y, 2 * h), 2 * w, 2 * h);
        g2d.rotate(Math.toRadians(-0), tx(x, 0), ty(y, 0));
    }

    public void setSelectionOval(int x, int y, double w, double h, Color color) {
        ovalw = (int) w;
        ovalh = (int) h;
        ovalx = x;
        ovaly = y;
        ovalColor = color;
        showOval = true;
    }

    boolean showOval = false;

    public void hideOval() {
        showOval = false;
        repaint();
    }

    Color ovalColor = Color.black;

    public void dString(Graphics g, String string, int x, int y) {
        g.drawString(string, tx(x, 0), ty(y, 0));
    }

    public void paintComponent(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);

        g.setColor(Color.DARK_GRAY);
        g.drawLine(0, height / 2, width, height / 2);
        g.drawLine(width / 2, 0, width / 2, height);
        g.setColor(Color.cyan);

        if (RFlist.RFs.size() > 0) {
            int sizeRect = RFlist.RFs.get(0).size;
            g.drawRect(tx(0, (int) (sizeRect * RFlist.scale)), ty(0, (int) (sizeRect * RFlist.scale)), (int) (sizeRect * RFlist.scale), (int) (sizeRect * RFlist.scale));
            for (RF rf : RFlist.RFs) {
                int comb = RFlist.combinations.indexOf(rf.combination) + 1;
                double frac = (double) comb / RFlist.combinations.size();
                g.setColor(new Color((int) (frac * 255), (int) (1.5 * frac * 255) % 255, 255 - (int) (frac * 255)));
                dString(g, "" + rf.combination, (int) (rf.px * RFlist.scale), (int) (rf.py * RFlist.scale));
                Oval(g, (int) (rf.px * RFlist.scale), (int) (rf.py * RFlist.scale), (int) (rf.rx * RFlist.scale), (int) (rf.ry * RFlist.scale), rf.angle);
            }
            if (showOval) {
                Oval2(g, (int) (ovalx * RFlist.scale), (int) (ovaly * RFlist.scale), (int) (ovalw * RFlist.scale), (int) (ovalh * RFlist.scale), ovalColor);
            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
