package imageprocessor.model;


import static imageprocessor.model.MatrixUtils.nPadArray;

public class ContraHarmonicMeanFilter {
    private int m, n;
    private double Q;


    public ContraHarmonicMeanFilter(int w, int h, double q) {
        m = w;
        n = h;
        Q = q;
    }

    public GrayImage runFilter(GrayImage gimg) {
        int padW = m / 2;
        int padH = n / 2;

        // pad image with replicated border pixels
        int[][] padded2D = nPadArray(gimg.getPixels2D(), 1, m / 2, n / 2);
        double[][] temp2D = new double[padded2D.length][padded2D[0].length];

        // move mask over image
        for (int y = padH; y < padded2D[0].length - padH; ++y) {
            for (int x = padW; x < padded2D.length - padW; ++x) {
                int[] underMask = MatrixUtils.getMaskedPixels(padded2D, x, y, m, n);

                // Calculate contraHarmonic mean of pixels currently under mask
                double numeratorSum = 0.0;
                double denominatorSum = 0.0;
                for (int pixelValue : underMask) {
                    numeratorSum += Math.pow((double) pixelValue, Q+1);
                    denominatorSum += Math.pow((double) pixelValue, Q);
                }
                double contraHarmonicMean = numeratorSum / denominatorSum;
                // store in temp2D
                temp2D[x][y] = contraHarmonicMean;
            }
        }

        // trim pad off temp2D
        double[][] result2D = MatrixUtils.trimPad(temp2D, padW, padH);
        int[] result1D = MatrixUtils.double2intArr(MatrixUtils.pixels1Dfrom2D(result2D));
        return new GrayImage(result1D, gimg.getWidth(), gimg.getHeight(), gimg.getBitDepth());
    }
}
