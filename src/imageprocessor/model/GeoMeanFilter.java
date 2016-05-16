package imageprocessor.model;


import static imageprocessor.model.MatrixUtils.nPadArray;

public class GeoMeanFilter {
    private int m, n;


    public GeoMeanFilter(int w, int h) {
        m = w;
        n = h;
    }

    public GrayImage runFilter (GrayImage gimg) {
        int padW = m / 2;
        int padH = n / 2;

        // pad image with replicated border pixels
        int[][] padded2D = nPadArray(gimg.getPixels2D(), 1, padW, padH);
        double[][] temp2D = new double[padded2D.length][padded2D[0].length];

        // move mask over image
        for (int y = padH; y < padded2D[0].length - padH; ++y) {
            for (int x = padW; x < padded2D.length - padW; ++x) {
                int[] underMask = MatrixUtils.getMaskedPixels(padded2D, x, y, m, n);

                // Calculate geometric mean of pixels under mask
                // Instead of getting the product of all the terms, we get the sum of logs
                double logSum = 0.0;
                for (int pixel : underMask) {
                    logSum += Math.log((double) pixel);
                }
                logSum = logSum / (double) (m * n);

                // store in temp2D
                temp2D[x][y] = Math.exp(logSum);
            }
        }

        // trim pad off temp2D
        double[][] result2D = MatrixUtils.trimPad(temp2D, padW, padH);
        int[] result1D = MatrixUtils.double2intArr(MatrixUtils.pixels1Dfrom2D(result2D));
        return new GrayImage(result1D, gimg.getWidth(), gimg.getHeight(), gimg.getBitDepth());
    }
}
