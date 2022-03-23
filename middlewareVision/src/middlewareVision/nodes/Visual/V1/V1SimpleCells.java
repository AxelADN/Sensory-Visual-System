package middlewareVision.nodes.Visual.V1;

import VisualMemory.V1Cells.V1Bank;
import static VisualMemory.V1Cells.V1Bank.SC;
import generator.ProcessList;
import spike.Location;
import java.util.logging.Level;
import java.util.logging.Logger;
import middlewareVision.config.AreaNames;
import gui.Visualizer;
import kmiddle2.nodes.activities.Activity;
import org.opencv.core.Mat;
import spike.Modalities;
import utils.Config;
import utils.Convertor;
import utils.Functions;
import utils.LongSpike;
import utils.numSync;

/**
 *
 *
 */
public class V1SimpleCells extends Activity {

    /**
     * *************************************************************************
     * init variables
     * *************************************************************************
     */
    int nFrame = 3 * Config.gaborOrientations;

    //mapa de saliencia, no se recibe aún
    public Mat saliencyMap;
    //no se para que sirve esto
    boolean init = false;

    /**
     * constructor
     */
    public V1SimpleCells() {
        this.ID = AreaNames.V1SimpleCells;
        this.namer = AreaNames.class;
        ProcessList.addProcess(this.getClass().getSimpleName(), true);
    }

    @Override
    public void init() {
    }

    //sync that receive 3 indexes
    numSync sync = new numSync(3);

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            if ((boolean) ProcessList.ProcessMap.get(this.getClass().getSimpleName())) {
                /*
            receive spike
                 */
                LongSpike spike = new LongSpike(data);

                if (spike.getModality() == Modalities.VISUAL) {
                    
                    convolveSimpleCells(V1Bank.DOC[0][0].Cells[2].mat, V1Bank.DOC[0][1].Cells[2].mat);

                    visualize();

                    LongSpike sendSpike1 = new LongSpike(Modalities.VISUAL, new Location(0), 0, 0);
                    
                    send(AreaNames.V1ComplexCells, sendSpike1.getByteArray());
                }

                if (sync.isComplete()) {
                    //edge border detection is performed, with phi angle = 0

                }

                if (spike.getModality() == Modalities.ATTENTION) {
                    for (int i = 0; i < Config.gaborOrientations; i++) {
                        LongSpike sendSpike1 = new LongSpike(Modalities.VISUAL, new Location(i), 0, 0);
                        send(AreaNames.V1ComplexCells, sendSpike1.getByteArray());
                        visualize();
                    }
                }
            }
        } catch (Exception ex) {
            //Logger.getLogger(V1SimpleCells.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Visualize all the Simple Cells
     */
    void visualize() {
        convolveSimpleCells(V1Bank.DOC[0][0].Cells[2].mat, V1Bank.DOC[0][1].Cells[2].mat);
        for (int k = 0; k < Config.gaborBanks; k++) {
            for (int i = 0; i < Config.gaborOrientations; i++) {
                Visualizer.setImage(Convertor.Mat2Img(V1Bank.SC[k][0].Even[i].mat), "even L bank" + k + " " + i, 4 * k + 6, i);
                Visualizer.setImage(Convertor.Mat2Img(V1Bank.SC[k][0].Odd[i].mat), "odd L bank" + k + " " + i, 4 * k + 8, i);

                Visualizer.setImage(Convertor.Mat2Img(V1Bank.SC[k][1].Even[i].mat), "even R bank" + k + " " + i, 4 * k + 7, i);
                Visualizer.setImage(Convertor.Mat2Img(V1Bank.SC[k][1].Odd[i].mat), "odd R bank" + k + " " + i, 4 * k + 9, i);

                //Combined or merged simple cells
                if (i == Config.gaborOrientations - 1) {
                    Visualizer.setImage(Convertor.Mat2Img(Functions.maxSum(V1Bank.SC[k][0].Even)), "Combined even L bank" + k + " " + i, 4 * k + 6, Config.gaborOrientations + 1);
                    Visualizer.setImage(Convertor.Mat2Img(Functions.maxSum(V1Bank.SC[k][0].Odd)), "Combined odd L bank" + k + " " + i, 4 * k + 8, Config.gaborOrientations + 1);

                    Visualizer.setImage(Convertor.Mat2Img(Functions.maxSum(V1Bank.SC[k][1].Even)), "Combined even R bank" + k + " " + i, 4 * k + 7, Config.gaborOrientations + 1);
                    Visualizer.setImage(Convertor.Mat2Img(Functions.maxSum(V1Bank.SC[k][1].Odd)), "Combined odd R bank" + k + " " + i, 4 * k + 9, Config.gaborOrientations + 1);
                }
            }
        }
        Visualizer.addLimit("SCinf", 6);
        Visualizer.addLimit("SCsup", 4 * (Config.gaborBanks - 1) + 10);
    }

    /**
     * Convolve all simple cells with the designed gabor filter
     *
     * @param input
     */
    void convolveSimpleCells(Mat inputL, Mat inputR) {
        int i1 = SC.length;
        for (int x1 = 0; x1 < i1; x1++) {
            convolve(x1, 0, inputL);
            convolve(x1, 1, inputR);
        }
    }

    /**
     * Convolve the simple cells in V1 with Gabor Filters<br>
     * each Simple Cell has a Gabor Filter loaded
     *
     * @param x1 is the filter index
     * @param x2 correspond to the eye
     * @param src source matrix to convolve
     */
    void convolve(int x1, int x2, Mat src) {
        for (int i = 0; i < Config.gaborOrientations; i++) {
            V1Bank.SC[x1][x2].Even[i].mat = Functions.filter(src, V1Bank.SC[x1][x2].evenFilter[i]);
            V1Bank.SC[x1][x2].Odd[i].mat = Functions.filter(src, V1Bank.SC[x1][x2].oddFilter[i]);
        }
    }

}
