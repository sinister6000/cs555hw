package imageprocessor.model;


import java.util.Arrays;

import static imageprocessor.model.MatrixUtils.nPadArray;

public class AlphaTrimmedMeanFilter {

    private int m, n, d;


    public AlphaTrimmedMeanFilter(int w, int h, int d) {
        m = w;
        n = h;

        // Assume d is an even integer.
        this.d = d;
    }

    public GrayImage runFilter(GrayImage gimg) {
        int padW = m / 2;
        int padH = n / 2;

        // pad image with replicated border pixels
        int[][] padded2D = nPadArray(gimg.getPixels2D(), 1, padW, padH);
        double[][] temp2D = new double[padded2D.length][padded2D[0].length];

        int trim = d / 2;

        // move mask over image
        for (int y = padH; y < padded2D[0].length - padH; ++y) {
            for (int x = padW; x < padded2D.length - padW; ++x) {
                int[] underMask = MatrixUtils.getMaskedPixels(padded2D, x, y, m, n);

                // Sort underMask, then trim off d/2 from beginning and from end.
                Arrays.sort(underMask);

                int[] trimmedUnderMask = new int[underMask.length - d];
                for (int i = 0; i < trimmedUnderMask.length; ++i) {
                    trimmedUnderMask[i] = underMask[i + trim];
                }
                double trimmedMean = MatrixUtils.sum(trimmedUnderMask) / (double) trimmedUnderMask.length;

                // store in temp2D
                temp2D[x][y] = trimmedMean;
            }
        }

        // trim pad off temp2D
        double[][] result2D = MatrixUtils.trimPad(temp2D, padW, padH);
        int[] result1D = MatrixUtils.double2intArr(MatrixUtils.pixels1Dfrom2D(result2D));
        return new GrayImage(result1D, gimg.getWidth(), gimg.getHeight(), gimg.getBitDepth());
    }
}
