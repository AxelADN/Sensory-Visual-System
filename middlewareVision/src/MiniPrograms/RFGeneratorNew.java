/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MiniPrograms;

import utils.GaussianFilter;
import gui.components.VisPanel;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import matrix.matrix;
import org.math.plot.Plot3DPanel;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import utils.Convertor;
import utils.FileUtils;
import utils.MatrixUtils;

/**
 *
 * @author HumanoideFilms
 */
public class RFGeneratorNew extends javax.swing.JFrame {

    AngularCombinations combFrame;
    private DefaultMutableTreeNode root;
    private DefaultTreeModel treeModel;
    VisPanel visPanel1;
    GaussianVisualizer gvis;
    boolean gvisVisible = false;
    matrix[] gaussians;
    public Plot3DPanel plot;
    ScaleFrame sframe;

    /**
     * Creates new form RFGeneratorNew
     */
    public RFGeneratorNew() {
        initComponents();
        sframe = new ScaleFrame(scaleField);
        combFrame = new AngularCombinations(this);
        combFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        gvis = new GaussianVisualizer();
        gvis.setVisible(false);
        gvis.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        gvis.jCheckBox1.setSelected(false);
        gvis.change2GaussStatus();

        listPanel1.setColumnNames("σx", "σy", "x0", "y0", "A", "θ", "comb", "size");
        //change the copy paste orders for using in the visualizer
        changeCopyPasteOrders();
        //keyword for the clipboard
        listPanel1.setClipboardString("♦♣♠");
        //column 7 will have the same value in all rows
        listPanel1.setColumnToDuplicate(7);
        //default values
        listPanel1.setDefaultVector(new Object[]{null, null, null, null, null, null, null, null});
        listPanel1.disableSaveButton();

        //for the file explorer
        root = new DefaultMutableTreeNode("RFs", true);

        //visualization panel
        visPanel1 = new VisPanel();
        visPanel1.setVisible(true);
        visPanel1.setSize(370, 340);
        jInternalFrame1.add(visPanel1);
        //initialize the 3D plot
        plot = new Plot3DPanel();
        listPanel1.removeAction = false;
        listPanel1.table.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                generate();
                updateVisualization();
                updateOval();
            }
        });
        listPanel1.table.addMouseListener(new java.awt.event.MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //hrow new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                if (RFlist.folder.equals("RFV4")) {
                    combFrame.setComb("" + listPanel1.table.getValueAt(listPanel1.table.getSelectedRow(), 6));
                }
                generate();
                updateVisualization();
                updateOval();

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        });
        listPanel1.removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listPanel1.removeRow();
                generate();
                updateVisualization();
            }
        });
        listPanel1.pasteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listPanel1.paste();
                generate();
                updateVisualization();
            }
        });
        initCombo();

        updateTree();
        RFlist.initList();
        renderTree();
    }

    /**
     * Render the file tree that shows the folder of the images
     */
    void renderTree() {
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) jTree1.getCellRenderer();
        Icon closedIcon = new ImageIcon("icon.png");
        Icon openIcon = new ImageIcon("circle.png");
        Icon leafIcon = new ImageIcon("circle.png");
        renderer.setClosedIcon(closedIcon);
        renderer.setOpenIcon(openIcon);
        renderer.setLeafIcon(leafIcon);
    }

    void addComb(String comb) {
        listPanel1.table.setValueAt(comb, listPanel1.table.getSelectedRow(), 6);
        generate();
        updateVisualization();
        updateOval();
    }

    void updateOval() {
        try {
            visPanel1.setSelectionOval(Integer.parseInt("" + listPanel1.table.getValueAt(listPanel1.table.getSelectedRow(), 2)),
                    Integer.parseInt("" + listPanel1.table.getValueAt(listPanel1.table.getSelectedRow(), 3)),
                    Double.parseDouble("" + listPanel1.table.getValueAt(listPanel1.table.getSelectedRow(), 0)) + 2,
                    Double.parseDouble("" + listPanel1.table.getValueAt(listPanel1.table.getSelectedRow(), 1)) + 2, Color.lightGray);
            visPanel1.repaint();
        } catch (Exception ex) {
            visPanel1.hideOval();
        }
    }

    /**
     * Update the plots
     */
    void updateVisualization() {
        if (jToggleButton1.isSelected()) {
            makePlot();
        } else {
            jInternalFrame1.setContentPane(visPanel1);
            visPanel1.repaint();
        }
    }

    /**
     * Creates the 3D plot
     */
    void makePlot() {
        ArrayList<RF> rfs = RFlist.RFs;
        Mat sumMat;
        if (rfs.size() > 0) {
            Mat filterMat[] = new Mat[rfs.size()];
            gaussians = new matrix[rfs.size()];
            //clean the plots
            plot.removeAllPlots();
            int i = 0;
            //for each receptive field
            for (RF rf : rfs) {
                int comb = RFlist.combinations.indexOf(rf.combination) + 1;
                double frac = (double) comb / RFlist.combinations.size();
                Color color = new Color((int) (frac * 255), (int) (1.5 * frac * 255) % 255, 255 - (int) (frac * 255));
                //create the abstraction of the gaussian filter
                GaussianFilter filter = new GaussianFilter(rf);
                //make the filter, it creates a OpenCV mat
                filterMat[i] = filter.makeFilter2();
                if (!jToggleButton2.isSelected()) {
                    //convert the opencv mat to matrix
                    gaussians[i] = Convertor.MatToMatrix(filterMat[i]);
                    //graph an individual plot, with different colors
                    graphOnePlot("Plot " + i, color, gaussians[i]);
                }
                i++;
            }
            /**
             * Plot the sum of Gaussians
             */
            if (jToggleButton2.isSelected()) {
                //sum all the filters
                sumMat = MatrixUtils.sum(filterMat, 1, 0);
                graphOnePlot("Plot ", Color.BLUE, Convertor.MatToMatrix(sumMat));
            }
            //fix the bounds
            plot.setFixedBounds(2, -1 / zZoom, 1 / zZoom);
            jInternalFrame1.setContentPane(plot);
            jInternalFrame1.repaint();
        }
    }

    /**
     * Adding one plot to the 3D plot
     *
     * @param name name of the plot
     * @param m 2d matrix
     */
    public void graphOnePlot(String name, Color color, matrix m) {
        double[] x;
        double[] y;
        double[][] z;
        //reduce the resolution if the plot is big
        int inc = m.getWidth() / 30;
        if (inc == 0) {
            inc = 1;
        }
        x = new double[m.getWidth() / inc];
        y = new double[m.getHeight() / inc];
        z = new double[x.length][y.length];
        for (int i = 0; i < x.length; i++) {
            x[i] = i * inc;
        }
        for (int i = 0; i < y.length; i++) {
            y[i] = i * inc;
        }
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < y.length; j++) {
                z[j][i] = m.getValue((int) x[i], (int) y[y.length - j - 1]);
            }
        }
        plot.addGridPlot(name, color, x, y, z);
    }

    /**
     * Change the copy paste orders
     */
    void changeCopyPasteOrders() {
        if (gvisVisible) {
            listPanel1.setCopyOrder(7, 4, 2, 3, 0, 1, 5, 6);
            listPanel1.setPasteOrder(4, 5, 2, 3, 1, 6, -1, 0);
        } else {
            listPanel1.defaultCopyPasteOrder();
        }
    }

    /**
     * Update the file tree
     */
    public void updateTree() {
        root = new DefaultMutableTreeNode();
        getList(root, new File(RFlist.folder + "/"));
        treeModel = new DefaultTreeModel(root);
        jTree1.setModel(treeModel);
    }
    String names[];

    /**
     * Get the file list
     *
     * @param node
     * @param f
     */
    public void getList(DefaultMutableTreeNode node, File f) {
        File fList[] = f.listFiles();
        names = new String[fList.length];
        //NEW
        int i = 0;
        for (File fi : fList) {
            names[i] = fi.getPath();
            i++;
        }
        Arrays.sort(names);
        for (int j = 0; j < names.length; j++) {
            node.add(new DefaultMutableTreeNode(new File(names[j])));
        }
        //OLD

        /*for (File fi : fList) {
            node.add(new DefaultMutableTreeNode(fi));
            System.out.println(fi.getPath());
        }*/
    }

    /**
     * Initialize the combo box with the receptive field folders
     */
    public void initCombo() {
        String[] folders = RFlist.initFolderList();
        DefaultComboBoxModel model = new DefaultComboBoxModel(folders);
        jComboBox1.setModel(model);
        RFlist.folder = jComboBox1.getSelectedItem().toString();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        listPanel1 = new gui.components.ListPanel();
        jLabel1 = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        scaleField = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        deleteButton = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jSlider1 = new javax.swing.JSlider();
        jLabel4 = new javax.swing.JLabel();
        aButton = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton3 = new javax.swing.JButton();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        jLabel5 = new javax.swing.JLabel();
        jSlider2 = new javax.swing.JSlider();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jButton4 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Receptive Field Editor");
        setBackground(new java.awt.Color(61, 61, 61));
        setResizable(false);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                formComponentMoved(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 328, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 0, -1, 328));

        jPanel2.setBackground(new java.awt.Color(62, 62, 62));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTree1.setBackground(new java.awt.Color(229, 229, 229));
        jTree1.setForeground(new java.awt.Color(0, 0, 0));
        jTree1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTree1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTree1);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 60, 180, 330));
        jPanel2.add(listPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(206, 6, 540, -1));

        jLabel1.setForeground(new java.awt.Color(204, 204, 204));
        jLabel1.setText("Name:");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 390, -1, -1));

        nameField.setBackground(new java.awt.Color(188, 195, 200));
        jPanel2.add(nameField, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 380, 116, 30));

        jLabel2.setForeground(new java.awt.Color(204, 204, 204));
        jLabel2.setText("Scales:");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 390, -1, -1));

        scaleField.setBackground(new java.awt.Color(188, 195, 200));
        jPanel2.add(scaleField, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 380, 160, 30));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jPanel2.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 28, 180, -1));

        deleteButton.setBackground(new java.awt.Color(151, 126, 122));
        deleteButton.setText("Delete");
        deleteButton.setToolTipText("Delete the file");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        jPanel2.add(deleteButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 140, -1));

        jButton2.setBackground(new java.awt.Color(150, 182, 204));
        jButton2.setForeground(new java.awt.Color(0, 0, 0));
        jButton2.setText("Save");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 380, 90, -1));

        jLabel3.setForeground(new java.awt.Color(204, 204, 204));
        jLabel3.setText("Folder:");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));

        jSlider1.setMaximum(200);
        jSlider1.setMinimum(1);
        jSlider1.setValue(10);
        jSlider1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jSlider1MouseDragged(evt);
            }
        });
        jPanel2.add(jSlider1, new org.netbeans.lib.awtextra.AbsoluteConstraints(745, 432, 384, -1));

        jLabel4.setForeground(new java.awt.Color(204, 204, 204));
        jLabel4.setText("Scale:");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 410, -1, -1));

        aButton.setBackground(new java.awt.Color(95, 134, 160));
        aButton.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        aButton.setText("<html>Edit<br> angular <br>combination</html>");
        aButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aButtonActionPerformed(evt);
            }
        });
        jPanel2.add(aButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 460, 90, 40));

        jCheckBox1.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        jCheckBox1.setForeground(new java.awt.Color(204, 204, 204));
        jCheckBox1.setText("Copy/Paste for the visualizer");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        jPanel2.add(jCheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 470, -1, -1));

        jButton3.setBackground(new java.awt.Color(95, 134, 160));
        jButton3.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        jButton3.setText("<html>Copy to<br> visualizer</html>");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 460, 80, 40));

        jInternalFrame1.setVisible(true);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 388, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel2.add(jInternalFrame1, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 10, 390, 368));

        jLabel5.setForeground(new java.awt.Color(204, 204, 204));
        jLabel5.setText("Z AxisScale:");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(745, 454, -1, -1));

        jSlider2.setMaximum(200);
        jSlider2.setMinimum(1);
        jSlider2.setValue(20);
        jSlider2.setEnabled(false);
        jSlider2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jSlider2MouseDragged(evt);
            }
        });
        jPanel2.add(jSlider2, new org.netbeans.lib.awtextra.AbsoluteConstraints(745, 476, 384, -1));

        jToggleButton1.setBackground(new java.awt.Color(95, 134, 160));
        jToggleButton1.setText("3D Plot");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jToggleButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(906, 380, 102, -1));

        jToggleButton2.setBackground(new java.awt.Color(95, 134, 160));
        jToggleButton2.setText("Summation");
        jToggleButton2.setEnabled(false);
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jToggleButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1014, 380, -1, -1));

        jButton4.setBackground(new java.awt.Color(95, 134, 160));
        jButton4.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        jButton4.setText("<html>Open Gaussian<br> Visualizer</html>");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 460, 80, 40));

        jButton1.setBackground(new java.awt.Color(51, 51, 51));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MiniPrograms/gear.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 380, 30, 30));

        jButton5.setBackground(new java.awt.Color(151, 126, 122));
        jButton5.setText("Delete w all scales");
        jButton5.setToolTipText("Delete all the patches with different scales");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 440, 140, -1));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1170, 510));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
        RFlist.folder = jComboBox1.getSelectedItem().toString();
        if (RFlist.folder.equals("RFV4")) {
            aButton.setVisible(true);
        } else {
            aButton.setVisible(false);
        }
        updateTree();
    }//GEN-LAST:event_jComboBox1ItemStateChanged
    String filename;

    /**
     * Load the receptive field information when a click is done in the left
     * list
     *
     * @param evt
     */
    private void jTree1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree1MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            String name = "";
            String node = "";
            try {
                node = jTree1.getPathForLocation(evt.getX(), evt.getY()).getPath()[1].toString();
            } catch (Exception e) {
                return;
            }
            name = node.replace(RFlist.folder + File.separator, "").replace(".txt", "");
            filename = node;
            nameField.setText(name);
            RFlist.loadList(node);
            fillList(node);
            updateVisualization();
            visPanel1.hideOval();
            if (RFlist.folder.equals("RFV4")) {
                combFrame.setComb("" + listPanel1.table.getValueAt(listPanel1.table.getSelectedRow(), 6));
            }
        }
    }//GEN-LAST:event_jTree1MouseClicked

    /**
     * Delete the selected file
     *
     * @param evt
     */
    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        // TODO add your handling code here:
        String name = jTree1.getSelectionPath().getLastPathComponent().toString();
        FileUtils.deleteFile(name);
        updateTree();
    }//GEN-LAST:event_deleteButtonActionPerformed

    /**
     * Change the scale of the 2D plot
     *
     * @param evt
     */
    private void jSlider1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider1MouseDragged
        // TODO add your handling code here:
        RFlist.scale = (jSlider1.getValue() * 0.1);
        updateVisualization();
    }//GEN-LAST:event_jSlider1MouseDragged

    /**
     * Save different receptive fields when different scales are introduced
     *
     * @param evt
     */
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        generate();
        RFlist.saveList(nameField.getText());
        if (scaleField.getText().length() > 0) {
            String scales[] = scaleField.getText().split(",");

            if (scales.length > 0) {
                for (String sscale : scales) {
                    double scale = Double.parseDouble(sscale);
                    RFlist.saveListScaled(nameField.getText(), scale);
                }
            }
            scaleField.setText("");
        }
        updateTree();
        updateVisualization();
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * make the gaussian visualizer visible
     *
     * @param evt
     */
    private void aButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aButtonActionPerformed
        // TODO add your handling code here:
        combFrame.setVisible(true);
        combFrame.setLocation(this.getWidth() / 2 - combFrame.getWidth() / 2 + this.getX(), this.getHeight() / 2 + this.getY());
    }//GEN-LAST:event_aButtonActionPerformed

    /**
     * if the checkbox is selected the copy paste orders changes
     *
     * @param evt
     */
    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
        gvisVisible = jCheckBox1.isSelected();
        changeCopyPasteOrders();
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    /**
     * copy the values to the visualizer
     *
     * @param evt
     */
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        gvisVisible = true;
        jCheckBox1.setSelected(true);
        changeCopyPasteOrders();
        listPanel1.copy();
        gvis.paste1(1);
        gvis.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed
    double zZoom = 1;

    /**
     * Change the Z axis min and max value
     *
     * @param evt
     */
    private void jSlider2MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider2MouseDragged
        // TODO add your handling code here:
        zZoom = (double) jSlider2.getValue() / 20;
        updateVisualization();
    }//GEN-LAST:event_jSlider2MouseDragged

    /**
     * Toggle between the 2D plot and the 2D plot
     *
     * @param evt
     */
    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // TODO add your handling code here:
        if (jToggleButton1.isSelected()) {
            jToggleButton2.setEnabled(true);
            jSlider1.setEnabled(false);
            jSlider2.setEnabled(true);
            jToggleButton1.setText("2D Plot");
            visPanel1.setVisible(false);
            plot.setVisible(true);
        } else {
            jSlider1.setEnabled(true);
            jSlider2.setEnabled(false);
            jToggleButton2.setEnabled(false);
            jToggleButton1.setText("3D Plot");
            plot.setVisible(false);
            visPanel1.setVisible(true);

        }
        updateVisualization();
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    /**
     * Toggle between show the composite gaussians or the sum of gaussians when
     * the 3D plot is active
     *
     * @param evt
     */
    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
        // TODO add your handling code here:
        if (jToggleButton2.isSelected()) {
            jToggleButton2.setText("Composite");
        } else {
            jToggleButton2.setText("Summation");
        }
        updateVisualization();
    }//GEN-LAST:event_jToggleButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        gvisVisible = true;
        jCheckBox1.setSelected(true);
        gvis.setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        sframe.setVisible(true);
        sframe.setLocation(jButton1.getLocationOnScreen().x - 170, jButton1.getLocationOnScreen().y - sframe.getHeight() - 10);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentMoved
        // TODO add your handling code here:
        try {
            if (sframe.isVisible()) {
                sframe.setLocation(jButton1.getLocationOnScreen().x - 170, jButton1.getLocationOnScreen().y - sframe.getHeight() - 10);
            }
        } catch (Exception ex) {

        }
    }//GEN-LAST:event_formComponentMoved

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        String name = jTree1.getSelectionPath().getLastPathComponent().toString();
        for (String n : names) {
            if (getBaseName(n).equals(getBaseName(name))) {
                FileUtils.deleteFile(n);

            }
        }
        updateTree();
    }//GEN-LAST:event_jButton5ActionPerformed

    String getBaseName(String name) {
        String ar[] = name.replace(".txt", "").split("_");
        return ar[0];
    }

    /**
     * Add each value of the table into the RFlist
     */
    public void generate() {
        RFlist.clearList();
        for (int i = 0; i < listPanel1.table.getRowCount(); i++) {
            if (listPanel1.CompleteRow(i)) {
                RFlist.addElement(listPanel1.table.getValueAt(i, 0), listPanel1.table.getValueAt(i, 1), listPanel1.table.getValueAt(i, 2), listPanel1.table.getValueAt(i, 3),
                        listPanel1.table.getValueAt(i, 4), listPanel1.table.getValueAt(i, 5), listPanel1.table.getValueAt(i, 6), listPanel1.table.getValueAt(i, 7));
            }
        }
    }

    /**
     * Fill the RF list from a file
     *
     * @param filename
     */
    public void fillList(String filename) {
        listPanel1.setFilePath(filename.replace(".txt", ""), "txt");
        listPanel1.loadFile();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
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
            java.util.logging.Logger.getLogger(RFGeneratorNew.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RFGeneratorNew.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RFGeneratorNew.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RFGeneratorNew.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RFGeneratorNew().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSlider jSlider2;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JTree jTree1;
    private gui.components.ListPanel listPanel1;
    private javax.swing.JTextField nameField;
    private javax.swing.JTextField scaleField;
    // End of variables declaration//GEN-END:variables
}
