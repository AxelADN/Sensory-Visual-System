package middlewareVision.nodes.Visual.V4;

import VisualMemory.V4Cells.V4Bank;
import spike.Location;
import generator.ProcessList;
import gui.Visualizer;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import middlewareVision.config.AreaNames;
import spike.Modalities;
import utils.LongSpike;
import utils.MatrixUtils;

/**
 *
 *
 */
public class V4SimpleShapeScaleInv extends Activity {

    public V4SimpleShapeScaleInv() {
        this.ID = AreaNames.V4SimpleShapeScaleInv;
        this.namer = AreaNames.class;
        ProcessList.addProcess(this.getClass().getSimpleName(), true);
    }

    @Override
    public void init() {

    }

    @Override
    public void receive(int nodeID, byte[] data) {
        if ((boolean) ProcessList.ProcessMap.get(this.getClass().getSimpleName())) {
            try {
                LongSpike spike = new LongSpike(data);
                if (spike.getModality() == Modalities.VISUAL) {

                    mergeActivationProcess();

                    visualize();
                }

            } catch (Exception ex) {
                Logger.getLogger(V4SimpleShapeScaleInv.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Visualize the activations of the Shape Merge Cells
     */
    void visualize() {
        int size = V4Bank.SMC.length;
        for (int i = 0; i < size; i++) {
            Visualizer.setImage(V4Bank.SMC[i][0].cell.mat, "shape " + i + " " + V4Bank.SMC[i][0].nameCell + " scale ", 30, i, "V4");
            Visualizer.setImage(V4Bank.SMC[i][1].cell.mat, "shape " + i + " " + V4Bank.SMC[i][1].nameCell + " scale ", 31, i, "V4");
        }
    }

    /**
     * Merge the activations from the Simple Shape Cells into the Shape Merge Cells
     */
    void mergeActivationProcess() {
        int size = V4Bank.SMC.length;
        for (int i = 0; i < size; i++) {
            mergeActivationOneCell(i, 0);
            mergeActivationOneCell(i, 1);
        }
    }

    /**
     * Performs a max sum using the previous cells of each SMC cell<br>
     * the previous cells were assigned in the class V4Bank
     * @param index index of shape
     * @param eye eye index
     */
    void mergeActivationOneCell(int index, int eye) {
            V4Bank.SMC[index][eye].cell.mat = MatrixUtils.maxSum(V4Bank.SMC[index][eye].cell.previous);
    }

}