package imageprocessor.model;


import java.util.Arrays;

import static imageprocessor.model.MatrixUtils.*;

public class RankFilter {
    private String type;
    private int m, n;


    public RankFilter(String t, int w, int h) {
        type = t;
        m = w;
        n = h;
    }

    public GrayImage runFilter (GrayImage gimg) {
        int padW = m / 2;
        int padH = n / 2;

        // pad image with replicated border pixels
        int[][] padded2D = nPadArray(gimg.getPixels2D(), 1, m / 2, n / 2);
        double[][] temp2D = new double[padded2D.length][padded2D[0].length];

        // move mask over image
        for (int y = padH; y < padded2D[0].length - padH; ++y) {
            for (int x = padW; x < padded2D.length - padW; ++x) {
                int[] underMask = getMaskedPixels(padded2D, x, y, m, n);

                switch (type) {
                    case "Max":
                        temp2D[x][y] = arrayMax(underMask);
                        break;
                    case "Min":
                        temp2D[x][y] = arrayMin(underMask);
                        break;
                    case "Median":
                        Arrays.sort(underMask);
                        temp2D[x][y] = underMask[(m * n) / 2];
                        break;
                    case "Midpoint":
                        temp2D[x][y] = 0.5 * (arrayMax(underMask) + arrayMin(underMask));
                        break;
                }
            }
        }

        // trim pad off temp2D
        int[][] result2D = double2intArr(trimPad(temp2D, padW, padH));
        return new GrayImage(result2D, gimg.getBitDepth());
    }
}
