/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory.V1Cells;

import VisualMemory.Cell;

/**
 *
 * @author Laptop
 */
public class DoubleOpponentCells {

    public int scale;
    public Cell[] Cells;

    /**
     * Constructor with scale and the array of DOC, not used
     *
     * @param scale
     * @param DoubleOpponentCells
     */
    public DoubleOpponentCells(int scale, Cell[] DoubleOpponentCells) {
        this.scale = scale;
        this.Cells = DoubleOpponentCells;
    }

    /**
     * Constructor for DOC, with scale and number
     *
     * @param scale
     * @param number
     */
    public DoubleOpponentCells(int scale, int number) {
        this.scale = scale;
        Cells = new Cell[number];
        for (int i = 0; i < number; i++) {
            Cells[i] = new Cell();
        }
    }

    /**
     * Constructor for DOC with only the number of cells
     *
     * @param number
     */
    public DoubleOpponentCells(int number) {
        Cells = new Cell[number];
        for (int i = 0; i < number; i++) {
            Cells[i] = new Cell();
        }
    }

}
