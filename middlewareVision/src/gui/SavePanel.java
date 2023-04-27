/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import generator.ProcessList;
import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;
import utils.FileUtils;
import utils.SaveUtils;

/**
 *
 * @author HumanoideFilms
 */
public class SavePanel extends javax.swing.JPanel {

    String savePath;
    RetinaPanel rp;
    int checkSelected[]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    JCheckBox checkArray[];
    

    /**
     * Creates new form SavePanel
     */
    public SavePanel(RetinaPanel panel) {
        initComponents();
        setCheckArray();
        openChecks();
        rp = panel;
        savePath = FileUtils.readFile(new File("ConfigFiles"+File.separator+"savePath.txt")).trim();
        pathField.setText(savePath);
        
        getMap();

    }

    public void getMap() {
        
        activateChecks();      
        ProcessList.openList();
        TreeMap<String, Object> tmap = new TreeMap();
        tmap.putAll(ProcessList.ProcessMap);
        deactivateChecks(tmap);
        
    }
    
    void saveSelectedChecks(){
        String s="";
        setCheckSelectedArray();
        for(int i=0;i<20;i++){
            s=s+checkSelected[i]+"\n";
        }
        FileUtils.write("ConfigFiles"+File.separator+"checks", s, "txt");
    }
    
    void openChecks(){
        String s[];
        try {
            s = FileUtils.fileLinesEx("ConfigFiles"+File.separator+"checks.txt");
            for(int i=0;i<20;i++){
            checkSelected[i]=Integer.parseInt(s[i]);
        }
        selectChecks();
        } catch (Exception ex) {
            saveSelectedChecks();
            openChecks();
        }
        
    }

    void deactivateCheck(JCheckBox box) {
        box.setEnabled(false);
        box.setSelected(false);
    }

    void activateCheck(JCheckBox box) {
        box.setEnabled(true);
    }
    
    void setCheckArray(){
        checkArray=new JCheckBox[20];
        for(int i=0;i<20;i++){
            checkArray[i]=new JCheckBox();
        }
        checkArray[0]=jCheckBox1;
        checkArray[1]=jCheckBox2;
        checkArray[2]=jCheckBox3;
        checkArray[3]=jCheckBox4;
        checkArray[4]=jCheckBox5;
        checkArray[5]=jCheckBox6;
        checkArray[6]=jCheckBox8;
        checkArray[7]=jCheckBox10;
        checkArray[8]=jCheckBox9;
        checkArray[9]=jCheckBox11;
        checkArray[10]=jCheckBox12;
        checkArray[11]=jCheckBox13;
        checkArray[12]=jCheckBox14;
        checkArray[13]=jCheckBox15;
        checkArray[14]=jCheckBox16;
        checkArray[15]=jCheckBox17;
        checkArray[16]=jCheckBox18;
        checkArray[17]=jCheckBox19;
        checkArray[18]=jCheckBox20;
        checkArray[19]=jCheckBox21;
    }

    void activateChecks() {
        for(int i=0;i<checkArray.length;i++){
            activateCheck(checkArray[i]);
        }
    }
    
    void setCheckSelectedArray(){
        for(int i=0;i<checkSelected.length;i++){
            if(checkArray[i].isSelected()){
                checkSelected[i]=1;
            }
            else{
                checkSelected[i]=0;
            }
        }
           
    }
    
    void selectChecks(){
        for(int i=0;i<checkSelected.length;i++){
            if(checkSelected[i]==1){
                checkArray[i].setSelected(true);
            }
        }
    }

    void deactivateChecks(TreeMap<String, Object> map) {
        //Shape
        if (!((boolean) map.get("RetinaProccess"))) {
            deactivateCheck(jCheckBox1);
        }
        if (!(boolean) map.get("LGNSimpleOpponentCells")) {
            deactivateCheck(jCheckBox2);
        }
        if (!(boolean) map.get("V1DoubleOpponent")) {
            deactivateCheck(jCheckBox3);
        }
        if (!(boolean) map.get("V1SimpleCells")) {
            deactivateCheck(jCheckBox4);
        }
        if (!(boolean) map.get("V1ComplexCells")) {
            deactivateCheck(jCheckBox5);
        }
        if (!(boolean) map.get("V1HyperComplex")) {
            deactivateCheck(jCheckBox6);
        }
        if (!(boolean) map.get("V2AngularCells")) {
            deactivateCheck(jCheckBox8);
        }
        if (!(boolean) map.get("V2CurvatureCells")) {
            deactivateCheck(jCheckBox10);
        }
        if (!(boolean) map.get("V4Color")) {
            deactivateCheck(jCheckBox9);
        }
        if (!(boolean) map.get("V4SimpleShapeCells")) {
            deactivateCheck(jCheckBox11);
        }
        if (!(boolean) map.get("V4SimpleShapeScaleInv")) {
            deactivateCheck(jCheckBox12);
        }
        //Binocular
        if (!(boolean) map.get("V1BinocularSimpleCells")) {
            deactivateCheck(jCheckBox13);
        }
        if (!(boolean) map.get("V1BinocularComplexCells")) {
            deactivateCheck(jCheckBox14);
            deactivateCheck(jCheckBox15);
        }
        if (!(boolean) map.get("V1BinocularMergeProcess")) {
            deactivateCheck(jCheckBox16);
        }
        if (!(boolean) map.get("V3DisparityRange")) {
            deactivateCheck(jCheckBox17);
        }
        
        //Motion
        if (!(boolean) map.get("V1MotionCellsNew")) {
            deactivateCheck(jCheckBox18);
        }
        if (!(boolean) map.get("MTComponentCells")) {
            deactivateCheck(jCheckBox19);
        }
        if (!(boolean) map.get("MTPatternCells")) {
            deactivateCheck(jCheckBox20);
        }
        if (!(boolean) map.get("MSTPolarCells")) {
            deactivateCheck(jCheckBox21);
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
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        pathField = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox5 = new javax.swing.JCheckBox();
        jCheckBox6 = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        jCheckBox8 = new javax.swing.JCheckBox();
        jCheckBox10 = new javax.swing.JCheckBox();
        jLabel7 = new javax.swing.JLabel();
        jCheckBox9 = new javax.swing.JCheckBox();
        jCheckBox11 = new javax.swing.JCheckBox();
        jCheckBox12 = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jCheckBox13 = new javax.swing.JCheckBox();
        jCheckBox14 = new javax.swing.JCheckBox();
        jCheckBox15 = new javax.swing.JCheckBox();
        jCheckBox16 = new javax.swing.JCheckBox();
        jCheckBox17 = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        jCheckBox18 = new javax.swing.JCheckBox();
        jCheckBox19 = new javax.swing.JCheckBox();
        jCheckBox20 = new javax.swing.JCheckBox();
        jCheckBox21 = new javax.swing.JCheckBox();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setBackground(new java.awt.Color(57, 57, 57));
        setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Path:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        add(jLabel1, gridBagConstraints);

        pathField.setMinimumSize(new java.awt.Dimension(50, 50));
        pathField.setPreferredSize(new java.awt.Dimension(200, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(pathField, gridBagConstraints);

        jButton1.setBackground(new java.awt.Color(47, 62, 72));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Chose folder");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        add(jButton1, new java.awt.GridBagConstraints());

        jCheckBox1.setForeground(new java.awt.Color(204, 204, 204));
        jCheckBox1.setText("LMS activations");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jCheckBox1, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 204, 204));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("Save groups:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipadx = 18;
        gridBagConstraints.ipady = 18;
        add(jLabel2, gridBagConstraints);

        jLabel3.setForeground(new java.awt.Color(204, 204, 204));
        jLabel3.setText("Retina");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 32;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jLabel3, gridBagConstraints);

        jLabel4.setForeground(new java.awt.Color(204, 204, 204));
        jLabel4.setText("LGN");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jLabel4, gridBagConstraints);

        jLabel5.setForeground(new java.awt.Color(204, 204, 204));
        jLabel5.setText("V1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jLabel5, gridBagConstraints);

        jCheckBox2.setForeground(new java.awt.Color(204, 204, 204));
        jCheckBox2.setText("DKL activations");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jCheckBox2, gridBagConstraints);

        jCheckBox3.setForeground(new java.awt.Color(204, 204, 204));
        jCheckBox3.setText("D'K'L' Activations");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jCheckBox3, gridBagConstraints);

        jCheckBox4.setForeground(new java.awt.Color(204, 204, 204));
        jCheckBox4.setText("Simple Cells");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jCheckBox4, gridBagConstraints);

        jCheckBox5.setForeground(new java.awt.Color(204, 204, 204));
        jCheckBox5.setText("Complex Cells");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jCheckBox5, gridBagConstraints);

        jCheckBox6.setForeground(new java.awt.Color(204, 204, 204));
        jCheckBox6.setText("Hypercomplex Cells");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jCheckBox6, gridBagConstraints);

        jLabel6.setForeground(new java.awt.Color(204, 204, 204));
        jLabel6.setText("V2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jLabel6, gridBagConstraints);

        jCheckBox8.setForeground(new java.awt.Color(204, 204, 204));
        jCheckBox8.setText("Angular Cells");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jCheckBox8, gridBagConstraints);

        jCheckBox10.setForeground(new java.awt.Color(204, 204, 204));
        jCheckBox10.setText("Curvature Cells");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jCheckBox10, gridBagConstraints);

        jLabel7.setForeground(new java.awt.Color(204, 204, 204));
        jLabel7.setText("V4");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jLabel7, gridBagConstraints);

        jCheckBox9.setForeground(new java.awt.Color(204, 204, 204));
        jCheckBox9.setText("Color Labels");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jCheckBox9, gridBagConstraints);

        jCheckBox11.setForeground(new java.awt.Color(204, 204, 204));
        jCheckBox11.setText("Simple Shape Cells");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jCheckBox11, gridBagConstraints);

        jCheckBox12.setForeground(new java.awt.Color(204, 204, 204));
        jCheckBox12.setText("Simple Shape Scale Inv");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jCheckBox12, gridBagConstraints);

        jLabel8.setForeground(new java.awt.Color(204, 204, 204));
        jLabel8.setText("Stereo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jLabel8, gridBagConstraints);

        jCheckBox13.setForeground(new java.awt.Color(204, 204, 204));
        jCheckBox13.setText("B Simple Cells");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jCheckBox13, gridBagConstraints);

        jCheckBox14.setForeground(new java.awt.Color(204, 204, 204));
        jCheckBox14.setText("B Complex Cells");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jCheckBox14, gridBagConstraints);

        jCheckBox15.setForeground(new java.awt.Color(204, 204, 204));
        jCheckBox15.setText("B Normalized Cells");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jCheckBox15, gridBagConstraints);

        jCheckBox16.setForeground(new java.awt.Color(204, 204, 204));
        jCheckBox16.setText("B Merged Cells");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jCheckBox16, gridBagConstraints);

        jCheckBox17.setForeground(new java.awt.Color(204, 204, 204));
        jCheckBox17.setText("B Relative disparity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jCheckBox17, gridBagConstraints);

        jLabel9.setForeground(new java.awt.Color(204, 204, 204));
        jLabel9.setText("Motion");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jLabel9, gridBagConstraints);

        jCheckBox18.setForeground(new java.awt.Color(204, 204, 204));
        jCheckBox18.setText("V1 Motion");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jCheckBox18, gridBagConstraints);

        jCheckBox19.setForeground(new java.awt.Color(204, 204, 204));
        jCheckBox19.setText("MT Component Motion");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jCheckBox19, gridBagConstraints);

        jCheckBox20.setForeground(new java.awt.Color(204, 204, 204));
        jCheckBox20.setText("MT Pattern Motion");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jCheckBox20, gridBagConstraints);

        jCheckBox21.setForeground(new java.awt.Color(204, 204, 204));
        jCheckBox21.setText("MST Radial Motion");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 22;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jCheckBox21, gridBagConstraints);

        jButton2.setBackground(new java.awt.Color(47, 62, 72));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Save");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 23;
        add(jButton2, gridBagConstraints);

        jButton3.setBackground(new java.awt.Color(47, 62, 72));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Open Folder");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 23;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jButton3, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        JFileChooser j = new JFileChooser();
        j.setCurrentDirectory(new File(savePath));

        // set the selection mode to directories only
        j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        // invoke the showsSaveDialog function to show the save dialog
        int r = j.showSaveDialog(this);

        if (r == JFileChooser.APPROVE_OPTION) {
            // set the label to the path of the selected directory
            savePath = j.getSelectedFile().getAbsolutePath();
            pathField.setText(savePath);
            FileUtils.write("ConfigFiles"+File.separator+"savePath", savePath, "txt");
        }


    }//GEN-LAST:event_jButton1ActionPerformed

    boolean isActive(JCheckBox check) {
        return check.isSelected() && check.isEnabled();
    }

    String name = "";
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        String nm = JOptionPane.showInputDialog("Name of the folder", name);
        if (nm != null) {
            if (nm.length() > 0) {
                name = nm;
                if (isActive(jCheckBox1)) {
                    SaveUtils.saveLMS(savePath, nm);
                }
                if (isActive(jCheckBox2)) {
                    SaveUtils.saveSOC(savePath, nm);
                }
                if (isActive(jCheckBox3)) {
                    SaveUtils.saveDOC(savePath, nm);
                }
                if (isActive(jCheckBox4)) {
                    SaveUtils.saveSimpleCells(savePath, nm);
                }
                if (isActive(jCheckBox5)) {
                    SaveUtils.saveComplexCells(savePath, nm);
                }
                if (isActive(jCheckBox6)) {
                    SaveUtils.saveHyperComplexCells(savePath, nm);
                    SaveUtils.saveMergedHyperComplexCells(savePath, nm);
                }
                if (isActive(jCheckBox8)) {
                    SaveUtils.saveAngularCells(savePath, nm);
                    SaveUtils.saveMergedAngularCells(savePath, nm);
                }
                if (isActive(jCheckBox10)) {
                    SaveUtils.saveCurvatureCells(savePath, nm);
                    SaveUtils.saveMergedCurvatureCells(savePath, nm);
                }
                if (isActive(jCheckBox9)) {
                    SaveUtils.saveColorLabels(savePath, nm);
                }
                if (isActive(jCheckBox11)) {
                    SaveUtils.saveSimpleCells(savePath, nm);
                }
                if (isActive(jCheckBox12)) {
                    SaveUtils.saveMergedShapeCells(savePath, nm);
                }
                if (isActive(jCheckBox13)) {
                    SaveUtils.saveBSimpleCells(savePath, nm);
                    SaveUtils.saveCombinedBSimpleCells(savePath, nm);
                }
                if (isActive(jCheckBox14)) {
                    SaveUtils.saveBComplexCells(savePath, nm);
                    SaveUtils.saveCombinedBComplexCells(savePath, nm);
                }
                if (isActive(jCheckBox15)) {
                    SaveUtils.saveBNormCells(savePath, nm);
                    SaveUtils.saveCombinedBNormCells(savePath, nm);
                }
                if (isActive(jCheckBox16)) {
                    SaveUtils.saveBMergedCells(savePath, nm);
                    SaveUtils.saveCombinedBMergedCells(savePath, nm);
                }
                if (isActive(jCheckBox17)) {
                    SaveUtils.saveBRelativeCells(savePath, nm);
                    SaveUtils.saveCombinedBRelativeCells(savePath, nm);
                }
                if (isActive(jCheckBox18)) {
                    SaveUtils.saveV1Motion(savePath, nm);
                }
                if (isActive(jCheckBox19)) {
                    SaveUtils.saveMTComponentMotion(savePath, nm);
                }
                if (isActive(jCheckBox20)) {
                    SaveUtils.saveMTPatternMotion(savePath, nm);
                }
                if (isActive(jCheckBox21)) {
                    SaveUtils.saveMSTRadialMotion(savePath, nm);
                }
                saveSelectedChecks();
            } else {
                JOptionPane.showMessageDialog(null, "The name is required");
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            // TODO add your handling code here:
            Desktop.getDesktop().open(new File(savePath));
        } catch (IOException ex) {
            Logger.getLogger(SavePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox10;
    private javax.swing.JCheckBox jCheckBox11;
    private javax.swing.JCheckBox jCheckBox12;
    private javax.swing.JCheckBox jCheckBox13;
    private javax.swing.JCheckBox jCheckBox14;
    private javax.swing.JCheckBox jCheckBox15;
    private javax.swing.JCheckBox jCheckBox16;
    private javax.swing.JCheckBox jCheckBox17;
    private javax.swing.JCheckBox jCheckBox18;
    private javax.swing.JCheckBox jCheckBox19;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox20;
    private javax.swing.JCheckBox jCheckBox21;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox8;
    private javax.swing.JCheckBox jCheckBox9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField pathField;
    // End of variables declaration//GEN-END:variables
}
