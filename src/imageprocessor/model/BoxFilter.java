package imageprocessor.model;

import static imageprocessor.model.MatrixUtils.*;

public class BoxFilter {

    private int m, n;
    private int[][] mask;


    public BoxFilter(int w, int h) {
        m = w;
        n = h;
        mask = add(new int[m][n], 1);
    }

    public GrayImage runFilter(GrayImage gimg) {
        // pad gimg with 0's.
        int padW = m / 2;
        int padH = n / 2;

        int[][] padded2D = nPadArray(gimg.getPixels2D(), 0, padW, padH);
        int[][] result2D = new int[padded2D.length][padded2D[0].length];

        int r, rOld, cellsOld, cellsNew;

        int maskSum = sum(pixels1Dfrom2D(mask));

        for (int y = padH; y < padded2D[0].length - padH; ++y) {

            // Calculate initial response r. r = sum (pixels * mask)
            int x = padW;
            int[] underMask = getMaskedPixels(padded2D, x, y, m, n);
            r = dot(underMask, pixels1Dfrom2D(mask));
            result2D[x][y] = r;
            rOld = r;

            // Now, continue along row, each new rNew = rOld - oldColumn + newColumn
            for (x = padW + 1; x < padded2D.length - padW; ++x) {
                // move mask 1 pixel to right
                // calc cellsOld (sum of left-most column that is subtracted when moving mask)
                cellsOld = 0;
                for (int j = -1 * padH; j <= padH; ++j) {
                    cellsOld += padded2D[x - 1 - padW][y + j];
                }
                // calc cellsNew (sum of incoming column on right)
                cellsNew = 0;
                for (int j = -1 * padH; j <= padH; ++j) {
                    cellsNew += padded2D[x + padW][y + j];
                }
                // get new response (rNew = rOld - oldColumn + newColumn)
                r = rOld - cellsOld + cellsNew;
                result2D[x][y] = r;
                rOld = r;
            }
        }

        // trim result2D (remove padding) and scale with 1/maskSum
        int[][] new2D = multiply((1/(double) maskSum), trimPad(result2D, padW, padH));

        return new GrayImage(new2D, gimg.getBitDepth());
    }
}
