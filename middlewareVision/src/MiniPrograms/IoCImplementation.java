/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MiniPrograms;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author Laptop
 */
public class IoCImplementation extends javax.swing.JFrame {

    /**
     * Creates new form IoCImplementation
     */
    GPanel gpanel;
    ArrayList<vector> vlist;
    ArrayList<vector> vlist2;
    vector average;
    vector average2;

    public IoCImplementation() {
        initComponents();
        gpanel = new GPanel(this);
        gpanel.setVisible(true);
        gpanel.setSize(200, 200);
        gpanel.setLocation(150, 10);
        jPanel1.add(gpanel);
        vlist = new ArrayList();
        vlist2 = new ArrayList();
        average=new vector(0,0);
        average2=new vector(0,0);
        repaint();
    }

    public boolean CompleteRow(int row) {
        boolean band = true;
        for (int i = 0; i < jTable1.getColumnCount(); i++) {
            if (jTable1.getValueAt(row, i) == null) {
                band = false;
                break;
            }
        }
        return band;
    }

    public void generate() {
        vlist.clear();
        vlist2.clear();
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            if (CompleteRow(i)) {
                vlist.add(new vector((double) jTable1.getValueAt(i, 0), Math.toRadians((double) (jTable1.getValueAt(i, 1)))));
                System.out.println(" " + jTable1.getValueAt(i, 0) + "  " + jTable1.getValueAt(i, 1));
            }
        }
    }

    int px = 200;
    int py = 0;

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Intensity", "Direction"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Double.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("Calculate");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
                .addContainerGap(224, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        generate();
        IoCProccess();
        gpanel.repaint();
        System.out.println("repainting l " + vlist.size());
    }//GEN-LAST:event_jButton1ActionPerformed

    public void IoCProccess() {
        if (vlist.size() > 1) {
            for (int i = 0; i < vlist.size(); i++) {
                for (int j = i + 1; j < vlist.size(); j++) {
                    IoCVector(vlist.get(i), vlist.get(j));
                }
            }
        }
        vectorAverage();
        vectorAverage2();
    }

    public void IoCVector(vector v1, vector v2) {
        double q1 = v1.intensity * Math.cos(v1.angle);
        double q2 = v1.intensity * Math.sin(v1.angle);
        double q3 = v1.intensity * Math.cos(v1.angle + Math.PI *0.5);
        double q4 = v1.intensity * Math.sin(v1.angle + Math.PI *0.5);
        
        System.out.println("q1:"+q1+" q2"+q2+" q3:"+q3+" q4:"+q4);

        double w1 = v2.intensity * Math.cos(v2.angle);
        double w2 = v2.intensity * Math.sin(v2.angle);
        double w3 = v2.intensity * Math.cos(v2.angle + Math.PI *0.5);
        double w4 = v2.intensity * Math.sin(v2.angle + Math.PI *0.5);
        
        

        double t0 = (-q4 * (w1 - q1) + q3 * (w2 - q2)) / (q4 * w3 - q3 * w4);
        double t1 = (w1 - q1 + w3 * t0) / q3;
        System.out.println("w1:"+w1+" w2"+w2+" w3:"+w3+" w4:"+w4+" t0"+t0+" t1"+t1);
        double px = q1 + q3 * t1;
        double py = q2 + q4 * t1;

        double in = Math.sqrt(px * px + py * py);
        System.out.println("intensity is "+in+" px "+px+" py "+py);
        double ang = Math.atan2(py, px);

        vector vec = new vector(in, ang);
        vlist2.add(vec);
    }
    
    public void vectorAverage(){
        double sx=0;
        double sy=0;
        for(vector v:vlist2){
            double px=v.intensity*Math.cos(v.angle);
            double py=v.intensity*Math.sin(v.angle);
            sx=sx+px;
            sy=sy+py;
        }
        double tx=sx/vlist2.size();
        double ty=sy/vlist2.size();
        double in = Math.sqrt(tx * tx + ty * ty);
        double ang = Math.atan2(ty, tx);
        average=new vector(in,ang);
    }
    
    public void vectorAverage2(){
        double sx=0;
        double sy=0;
        for(vector v:vlist){
            double px=v.intensity*Math.cos(v.angle);
            double py=v.intensity*Math.sin(v.angle);
            sx=sx+px;
            sy=sy+py;
        }
        double tx=sx/vlist.size();
        double ty=sy/vlist.size();
        double in = Math.sqrt(tx * tx + ty * ty);
        double ang = Math.atan2(ty, tx);
        average2=new vector(in,ang);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(IoCImplementation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IoCImplementation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IoCImplementation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IoCImplementation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new IoCImplementation().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}

class GPanel extends javax.swing.JPanel {

    IoCImplementation iocframe;
    /**
     * Creates new form GPanel
     */
    int width = 0;
    int height = 0;

    public GPanel(IoCImplementation iocframe) {
        this.iocframe = iocframe;
        initComponents();
        setBackground(Color.BLACK);
        repaint();
    }

    public void Line(Graphics g, double in, double angle, boolean deg, int wline) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(wline));
        if (deg) {
            g2d.drawLine((int) (width / 2), (int) (height / 2), (int) ((width / 2) + 10 * in * Math.cos(Math.toRadians(-angle))),
                    (int) ((height / 2) + 10 * in * Math.sin(Math.toRadians(-angle))));
        } else {
            g2d.drawLine((int) (width / 2), (int) (height / 2), (int) ((width / 2) + 10 * in * Math.cos(-angle)),
                    (int) ((height / 2) + 10 * in * Math.sin(-angle)));
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    public void paintComponent(Graphics g) {
        System.out.println("repainting " + iocframe.vlist.size());
        width = getWidth();
        height = getHeight();

        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);

        g.setColor(Color.DARK_GRAY);
        g.drawLine(0, height / 2, width, height / 2);
        g.drawLine(width / 2, 0, width / 2, height);
        g.setColor(Color.cyan);

        if (iocframe.vlist.size() > 0) {
            for (vector v : iocframe.vlist) {
                Line(g, v.intensity, v.angle, false,2);
            }
        }

        g.setColor(Color.orange);

        if (iocframe.vlist2.size() > 0) {
            for (vector v : iocframe.vlist2) {
                Line(g, v.intensity, v.angle, false,1);
            }
        }
        
        /*g.setColor(Color.red);
        Line(g,iocframe.average.intensity,iocframe.average.angle,false,4);*/
        
        g.setColor(Color.green);
        Line(g,iocframe.average2.intensity,iocframe.average2.angle,false,4);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
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
    }// </editor-fold>                        

    // Variables declaration - do not modify                     
    // End of variables declaration                   
}

class vector {

    double intensity;
    double angle;

    public vector(double intensity, double angle) {
        this.intensity = intensity;
        this.angle = angle;
    }

    public double getIntensity() {
        return intensity;
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}