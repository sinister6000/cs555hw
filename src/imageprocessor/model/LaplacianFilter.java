package imageprocessor.model;


public class LaplacianFilter {

    private int m;
    private int[][] mask;

    public LaplacianFilter(int w) {
        m = w;
        if (m == 3) {
            mask = new int[][]{
                  {1,  1, 1},
                  {1, -8, 1},
                  {1,  1, 1}};
        } else if (m == 5) {
            mask = new int[][]{
                  {4,  1,  0,  1, 4},
                  {1, -2, -3, -2, 1},
                  {0, -3, -4, -3, 0},
                  {1, -2, -3, -2, 1},
                  {4,  1,  0,  1, 4}};
        } else if (m == 7) {
            mask = new int[][]{
                  {10, 5,  2,  1,  2,  5, 10},
                  {5,  0, -3, -4, -3,  0, 5},
                  {2, -3, -6, -7, -6, -3, 2},
                  {1, -4, -7, -8, -7, -4, 1},
                  {2, -3, -6, -7, -6, -3, 2},
                  {5,  0, -3, -4, -3,  0, 5},
                  {10, 5,  2,  1,  2,  5, 10}};
        }
    }


    public GrayImage runFilter (GrayImage gimg) {
        int padSize = m / 2;
        int[][] padded2D = MatrixUtils.nPadArray(gimg.getPixels2D(), 1, padSize, padSize);
        double[][] temp2D = new double[padded2D.length][padded2D[0].length];
        int[] mask1D = MatrixUtils.pixels1Dfrom2D(mask);

        for (int y = padSize; y < padded2D[0].length - padSize; ++y) {
            for (int x = padSize; x < padded2D.length - padSize; ++x) {
                int[] underMask = MatrixUtils.getMaskedPixels(padded2D, x, y, m, m);
                temp2D[x][y] = (double) MatrixUtils.dot(underMask, mask1D);
            }
        }

        // trim pad off temp2D and divide by maskSum
        double[][] filter2D = MatrixUtils.trimPad(temp2D, padSize, padSize);
        int[] filter1D = MatrixUtils.normalizePixelValues(MatrixUtils.pixels1Dfrom2D(filter2D), 0, 255);

        // g(x,y) = f(x,y) + c[(laplacian(x,y)], c = -1
        int[] result1D = MatrixUtils.subtract(gimg.getPixels1D(), filter1D);
        int[] normedResult1D = MatrixUtils.normalizePixelValues(result1D, 0, 255);
        return new GrayImage(normedResult1D, gimg.getWidth(), gimg.getHeight(), gimg.getBitDepth());
    }
}
