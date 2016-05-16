package imageprocessor.model;

import java.util.ArrayList;

import static imageprocessor.model.MatrixUtils.nPadArray;


public class MedianFilter {
    protected int m, n;

    public MedianFilter(int w, int h) {
        m = w;
        n = h;
    }


    public GrayImage runFilter(GrayImage gimg) {
        // pad gimg with border pixels.
        int padW = m/2;
        int padH = n/2;

        int[][] padded2D = nPadArray(gimg.getPixels2D(), 1, padW, padH);
        int[][] result2D = new int[padded2D.length][padded2D[0].length];

        for (int y = padH; y < padded2D[0].length - padH; ++y) {
            // Calculate initial masked cells
            int x = padW;
            ArrayList<Integer> masked = new ArrayList<>(m*n);

            for (int xOffset = -1 * padW; xOffset <= padW; ++xOffset) {
                for (int yOffset = -1 * padH; yOffset <= padH; ++yOffset) {
                    masked.ensureCapacity(m*n);
                    masked.add(padded2D[x+xOffset][y+yOffset]);
                }
            }
            // find median of masked
            masked.sort(null);
            int median = masked.get(m*n/2);

            // store median in result
            result2D[x][y] = median;

            // calculate for rest of row
            for (x = padW+1; x < padded2D.length - padW; ++x) {
                // put left-most cells into cellsOld & add incoming cells to cellsNew
                ArrayList<Integer> cellsOld = new ArrayList<>(n);
                ArrayList<Integer> cellsNew = new ArrayList<>(n);
                for (int yOffset = -1*padH; yOffset<=padH; ++yOffset) {
                    cellsOld.add(padded2D[x-1-padW][y+yOffset]);
                    cellsNew.add(padded2D[x+padW][y+yOffset]);
                }
                for (Integer i: cellsOld) {
                    masked.remove(i);
                }
                masked.ensureCapacity(m*n);
                masked.addAll(cellsNew);

                // find median and store in result2D
                masked.sort(null);
                median = masked.get(m*n/2);
                result2D[x][y] = median;
            }
        }

        // trim pad off result2D
        int[][] new2D = MatrixUtils.trimPad(result2D, padW, padH);

        return new GrayImage(new2D, gimg.getBitDepth());
    }
}
