package imageprocessor.model;


public class HighBoostFilter {
    private double A;
    private int m, n;

    public HighBoostFilter(int w, int h, double A) {
        m = w;
        n = h;
        this.A = A;
    }

    public GrayImage runFilter(GrayImage gimg) {
        // Blur a copy of the original image
        BoxFilter box = new BoxFilter(m, n);
        GrayImage blur = box.runFilter(gimg);

        // mask = original - blur
        int[][] mask = MatrixUtils.subtract(gimg.getPixels2D(), blur.getPixels2D());
        double[][] doubleMask = MatrixUtils.int2doubleArr(mask);

        // result = original + A*mask
        double[][] original = MatrixUtils.int2doubleArr(gimg.getPixels2D());
        double[][] scaledMask = MatrixUtils.multiply(A, doubleMask);
        double[][] doubleResult = MatrixUtils.add(original, scaledMask);

        // scale result to [0, 255]
        double[] doubleResult1D = MatrixUtils.pixels1Dfrom2D(doubleResult);
        int[] result = MatrixUtils.normalizePixelValues(doubleResult1D, 0, 255);

        return new GrayImage(result, gimg.getWidth(), gimg.getHeight(), gimg.getBitDepth());
    }
}
