package imageprocessor.model;


import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GrayImage {

    private int width;
    private int height;
    private int[] pixels1D;
    private int[][] pixels2D;
    private int bitDepth;
    private long[] histogram;

    public GrayImage() {
    }

    public GrayImage(Image img, int bitDepth) {
        width = (int) img.getWidth();
        height = (int) img.getHeight();
        BufferedImage bimg = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        bimg = SwingFXUtils.fromFXImage(img, bimg);

        pixels2D = buffImage2GrayPixels2D(bimg);
        pixels1D = pixels1Dfrom2D(pixels2D);
        this.bitDepth = bitDepth;
        histogram = calcHistogram(pixels1D);
    }

    private GrayImage(int[][] pixArray2D, int bitDepth) {
        width = pixArray2D.length;
        height = pixArray2D[0].length;
        pixels2D = pixArray2D;
        pixels1D = pixels1Dfrom2D(pixels2D);
        this.bitDepth = bitDepth;
        histogram = calcHistogram(pixels1D);
    }

    private GrayImage(int[] pixArray1D, int w, int h, int bitDepth) {
        width = w;
        height = h;
        pixels1D = pixArray1D;
        pixels2D = pixels2Dfrom1D(pixels1D, w, h);
        this.bitDepth = bitDepth;
        histogram = calcHistogram(pixels1D);
    }

    public GrayImage(File f) {
        try {
            BufferedImage bimg = ImageIO.read(f);
            width = bimg.getWidth();
            height = bimg.getHeight();

            pixels2D = buffImage2GrayPixels2D(bimg);
            pixels1D = pixels1Dfrom2D(pixels2D);
            bitDepth = 8;
            histogram = calcHistogram(pixels1D);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GrayImage(BufferedImage bimg) {
        width = bimg.getWidth();
        height = bimg.getHeight();

        pixels2D = buffImage2GrayPixels2D(bimg);
        pixels1D = pixels1Dfrom2D(pixels2D);
        bitDepth = 8;
        histogram = calcHistogram(pixels1D);
    }

    /**
     * Normalizes intensity values in pixels[] to a desired range. In almost all cases, the target range is [0, 255].
     *
     * @param pixels An array of pixel values that we want to normalize
     * @param targetMin Minimum value in target range
     * @param targetMax Maximum value in target range
     * @return Array of normalized pixel values
     */
    private static int[] normalizePixelValues(double[] pixels, double targetMin, double targetMax) {
        double min = arrayMin(pixels);
        double max = arrayMax(pixels);

        int[] result = new int[pixels.length];
        double temp;
        double linearChange;
        for (int i=0; i<pixels.length; ++i) {
            linearChange = (pixels[i] - min) / (max - min);
            temp = ((targetMax - targetMin) * linearChange) + targetMin;
            result[i] = (int) (temp + 0.5);
        }
        return result;
    }

    /**
     * Normalizes intensity values in pixels[] to a desired range. In almost all cases, the target range is [0, 255].
     *
     * @param pixels An array of pixel values that we want to normalize
     * @param targetMin Minimum value in target range
     * @param targetMax Maximum value in target range
     * @return Array of normalized pixel values
     */
    private static int[] normalizePixelValues(int[] pixels, double targetMin, double targetMax) {
        double[] doublePixels = int2doubleArr(pixels);
        return normalizePixelValues(doublePixels, targetMin, targetMax);
    }

    /**
     * Finds the max value in an array.
     *
     * @param arr array of doubles
     * @return max
     */
    private static double arrayMax(double[] arr) {
        double max = Double.MIN_VALUE;
        for (double x : arr) {
            if (x > max) {
                max = x;
            }
        }
        return max;
    }

    /**
     * Finds min value in an array.
     *
     * @param arr array of doubles
     * @return min
     */
    private static double arrayMin(double[] arr) {
        double min = Double.MAX_VALUE;
        for (double x : arr) {
            if (x < min) {
                min = x;
            }
        }
        return min;
    }

    /**
     * Finds max value in an array.
     *
     * @param arr array of ints
     * @return max
     */
    private static int arrayMax(int[] arr) {
        int max = Integer.MIN_VALUE;
        for (int x : arr) {
            if (x > max) {
                max = x;
            }
        }
        return max;
    }

    /**
     * Finds min value in an array.
     *
     * @param arr array of ints
     * @return min
     */
    private static int arrayMin(int[] arr) {
        int min = Integer.MAX_VALUE;
        for (int x : arr) {
            if (x < min) {
                min = x;
            }
        }
        return min;
    }

    public static void main(String[] args) {
        int[][] p = new int[10][10];
        for (int y=0; y<10; ++y) {
            for (int x=0; x<5; ++x) {
                p[x][y] = 80;
            }
            for (int x=5; x<10; ++x) {
                p[x][y] = 150;
            }
        }

        GrayImage gi = new GrayImage(p, 8);

        File f = new File("C:\\imgs\\test.png");
        gi.toFile(f);

    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int[] getPixels1D() {
        return pixels1D;
    }

    public void setPixels1D(int[] pixels1D) {
        this.pixels1D = pixels1D;
    }

    public int[][] getPixels2D() {
        return pixels2D;
    }

    public void setPixels2D(int[][] pixels2D) {
        this.pixels2D = pixels2D;
    }

    public int getBitDepth() {
        return bitDepth;
    }

    private void setBitDepth(int bitDepth) {
        this.bitDepth = bitDepth;
    }

    public long[] getHistogram() {
        return histogram;
    }

    public void setHistogram(long[] histogram) {
        this.histogram = histogram;
    }

    /**
     * Converts a color BufferedImage to int[][] of grayscale brightness values.
     *
     * @param bi BufferedImage to convert
     * @return int[][] of brightness values
     */
    private int[][] buffImage2GrayPixels2D(BufferedImage bi) {

        int[][] result = new int[width][height];
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                Color c = new Color(bi.getRGB(x, y));
                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();
                int gray;
                if (red == green && green == blue) {
                    gray = red;
                } else {
                    red = (int) (c.getRed() * 0.299);
                    green = (int) (c.getGreen() * 0.587);
                    blue = (int) (c.getBlue() * 0.114);
                    gray = red + green + blue;
                }
                result[x][y] = gray;
            }
        }
        return result;
    }

    /**
     * Given a 2D array of pixels, returns equivalent 1D array. Helper function for class constructor.
     *
     * @return 1D pixel array
     * @param pixels2D
     */
    private int[] pixels1Dfrom2D(int[][] pixels2D) {
        int[] result = new int[width * height];
        int resultIndex = 0;
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                result[resultIndex++] = pixels2D[x][y];
            }
        }
        return result;
    }

    /**
     * Given a 1D array of pixels, returns equivalent 2D array. Helper function for class constructor.
     *
     * @param pixels1D 1D array of pixels
     * @param width width of returned 2D array
     * @param height height of returned 2D array
     * @return 2D array of pixels
     */
    private static int[][] pixels2Dfrom1D(int[] pixels1D, int width, int height) {
        int[][] result = new int[width][height];
        int index1D = 0;
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int gray = pixels1D[index1D++];
                result[x][y] = gray;
            }
        }
        return result;
    }

    /**
     * Takes a 1D array of pixel values and returns a histogram.
     *
     * @param grayPixelValues pixels1D
     * @return array - index is intensity value, value is total number of pixels that have that intensity value.
     */
    private long[] calcHistogram(int[] grayPixelValues) {
        int bins = (int) Math.pow(2, this.bitDepth);
        long[] grayPixelCounts = new long[bins];

        // for each pixel, get the brightness value, and increment counter for that brightness.
        for (int grayValue : grayPixelValues) {
            grayPixelCounts[grayValue]++;
        }
        return grayPixelCounts;
    }

    /**
     * Converts to a BufferedImage type
     *
     * @return BufferedImage representation of this
     */
    private BufferedImage toBufferedImage() {
        BufferedImage bimg = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int gray = pixels2D[x][y];
                Color grayColor;
                try {
                    grayColor = new Color(gray, gray, gray);
                    bimg.setRGB(x, y, grayColor.getRGB());
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    if (gray < 0) {
                        gray = 0;
                    } else if (gray > 255) {
                        gray = 255;
                    }
                    grayColor = new Color(gray, gray, gray);
                    bimg.setRGB(x, y, grayColor.getRGB());
                }
            }
        }
        return bimg;
    }

    /**
     * Makes a String of pixel values
     *
     * @return String representation
     */
    @Override
    public String toString() {
        String result = "";
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                result += pixels2D[x][y] + " ";
            }
            result += "\n";
        }
        return result;
    }

    /**
     * Prepares GrayImage for output to display. Display output requires JavaFX Image.
     *
     * @return WritableImage ready for display output
     */
    public WritableImage toFXImage() {
        BufferedImage bi;

        // If bitDepth != 8, we need to scale pixel intensities to [0, 255] before displaying on screen.
        if (bitDepth != 8) {
            int[] scaledPixels1D = normalizePixelValues(pixels1D, 0, 255);
            GrayImage scaledGrayImage = new GrayImage(scaledPixels1D, width, height, bitDepth);
            bi = scaledGrayImage.toBufferedImage();
        } else {
            bi = this.toBufferedImage();
        }

        WritableImage fxi = new WritableImage(bi.getWidth(), bi.getHeight());
        SwingFXUtils.toFXImage(bi, fxi);
        return fxi;
    }

    /**
     * Saves GrayImage as *.png file. First converts to BufferedImage.
     *
     * @param f File to save image into
     */
    public void toFile(File f) {
        BufferedImage bi = toBufferedImage();
        try {
            ImageIO.write(bi, "png", f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Performs log transformation
     *
     * @param c User entered double. Constant in equation.
     * @return GrayImage modified by log transform
     */
    public GrayImage logTrans(double c) {
        int[] origPixels = pixels1D;
        double[] tempPixels = new double[origPixels.length];

        int grayLevels = (int) Math.pow(2, bitDepth);

        for (int i = 0; i < origPixels.length; ++i) {
//            double logTransGrayValue = c * Math.log((double) (origPixels[i] / (grayLevels - 1)) + 1.0);
            double logTransGrayValue = c * Math.log((double) (origPixels[i] + 1.0));
            tempPixels[i] = logTransGrayValue;
        }

        int[] resultPixels = normalizePixelValues(tempPixels, 0, 255);

        return new GrayImage(resultPixels, this.width, this.height, this.bitDepth);
    }

    /**
     * Applies the power-law transformation to leftGrayImage and returns a new GrayImage containing result.
     *
     * @param c user-supplied
     * @param gamma user-supplied
     * @return GrayImage with power transformed values
     */
    public GrayImage powerTrans(double c, double gamma) {
        int[] origPixels = pixels1D;
        double[] tempPixels = new double[origPixels.length];

        for (int i = 0; i < origPixels.length; ++i) {
            // min(255, 255*c*(p/255)^gamma) clamps output intensities to 255. This is needed for c > 1.
            tempPixels[i] = Math.min(255.0, 255.0 * c * Math.pow(origPixels[i] / 255.0, gamma));
        }

        int[] resultPixels = normalizePixelValues(tempPixels, 0, 255);

        return new GrayImage(resultPixels, this.width, this.height, this.bitDepth);
    }

    /**
     * Lowers the gray-scale resolution (bit-depth) of a GrayImage by 1 bit. Divides all pixel values by 2 and
     * truncates to floor. displayFXI() rescales pixel values in order to display from black to white.
     *
     * @return GrayImage with decreased gray resolution
     */
    public GrayImage decreaseGrayRes() {
        int[] origPixels = pixels1D;
        int[] tempPixels = new int[origPixels.length];
        for (int i = 0; i < origPixels.length; ++i) {
            tempPixels[i] = origPixels[i] / 2;
        }

        return new GrayImage(tempPixels, width, height, this.bitDepth - 1);
    }

    /**
     * Resizes image. Calculates new pixel values using replication.
     *
     * @param newW width of image after resize
     * @param newH height of image after resize
     * @return resized GrayImage
     */
    public GrayImage zoomReplication(int newW, int newH) {
        int oldW = width;
        int oldH = height;
        double xRatio = oldW / (double) newW;
        double yRatio = oldH / (double) newH;

        int[][] result2D = new int[newW][newH];

        int oldX, oldY;

        for (int y = 0; y < newH; ++y) {
            for (int x = 0; x < newW; ++x) {
                oldX = (int) Math.min(oldW - 1, Math.floor(x * xRatio));
                oldY = (int) Math.min(oldH - 1, Math.floor(y * yRatio));
                result2D[x][y] = pixels2D[oldX][oldY];
            }
        }

        return new GrayImage(result2D, this.bitDepth);
    }

    /**
     * Resizes image. Calculates new pixel values using nearest neighbor.
     *
     * @param newW width of image after resize
     * @param newH height of image after resize
     * @return resized GrayImage
     */
    public GrayImage nearestNeighbor(int newW, int newH) {
        int oldW = width;
        int oldH = height;
        double xRatio = oldW / (double) newW;
        double yRatio = oldH / (double) newH;

        int[][] result2D = new int[newW][newH];

        int oldX, oldY;

        for (int y = 0; y < newH; ++y) {
            for (int x = 0; x < newW; ++x) {
                oldX = (int) Math.min(oldW - 1, Math.round(x * xRatio));
                oldY = (int) Math.min(oldH - 1, Math.round(y * yRatio));
                result2D[x][y] = pixels2D[oldX][oldY];
            }
        }

        return new GrayImage(result2D, this.bitDepth);
    }

    /**
     * Resizes image. Calculates new pixel values using bilinear interpolation.
     *
     * @param newW width of image after resize
     * @param newH height of image after resize
     * @return resized GrayImage
     */
    public GrayImage bilinearInterpolation(int newW, int newH) {
        int[][] result2D = new int[newW][newH];
        int[][] paddedOrig2D = padArray(pixels2D);

        int oldW = width;
        int oldH = height;
        double xRatio = oldW / (double) newW;
        double yRatio = oldH / (double) newH;

        double translatedX, translatedY, fracX, fracY;
        int x1, x2, y1, y2;
        double q11, q12, q21, q22;

        for (int y = 0; y < newH; ++y) {
            for (int x = 0; x < newW; ++x) {
                translatedX = x * xRatio;
                translatedY = y * yRatio;

                x1 = (int) Math.min(oldW - 1, Math.floor(translatedX));
                x2 = x1 + 1;
                y1 = (int) Math.min(oldH - 1, Math.floor(translatedY));
                y2 = y1 + 1;

                q11 = paddedOrig2D[x1][y1];
                q21 = paddedOrig2D[x2][y1];
                q12 = paddedOrig2D[x1][y2];
                q22 = paddedOrig2D[x2][y2];

                // Perform linear interpolation for x
                fracX = translatedX - Math.floor(translatedX);
                fracY = translatedY - Math.floor(translatedY);
                double fx1 = q11 + fracX * (q21 - q11);
                double fx2 = q12 + fracX * (q22 - q12);

                // Perform linear interpolation for y using the values found above
                double temp = fx1 + fracY * (fx2 - fx1);

                int result = (int) (temp + 0.5);
                result2D[x][y] = result;
            }
        }

        return new GrayImage(result2D, this.bitDepth);
    }

    /**
     * Performs global histogram equalization
     *
     * @return equalized GrayImage
     */
    public GrayImage histogramEqGlobal() {
        int w = width;
        int h = height;
        int numPixels = w * h;
        int L = (int) Math.pow(2, bitDepth);

        if (this.histogram == null) {
            this.histogram = calcHistogram(this.pixels1D);
        }

        // Calculate cumulative histogram
        long[] cumulHistogram = new long[this.histogram.length];
        cumulHistogram[0] = histogram[0];
        for (int i = 1; i < histogram.length; ++i) {
            cumulHistogram[i] = cumulHistogram[i-1] + histogram[i];
        }

        // Create new pixel array with equalized values using cumulative histogram
        int[] newPixels = new int[numPixels];
        for (int i = 0; i < numPixels; ++i) {
            int hIndex = pixels1D[i];
            newPixels[i] = (int) (cumulHistogram[hIndex] * ((L - 1) / (double) numPixels));
        }

        // Create a new GrayImage from newPixels
        return new GrayImage(newPixels, this.width, this.height, this.bitDepth);
    }

    /**
     * Performs local histogram equalization with a window size of windowSize x windowSize.
     *
     * @param windowSize size of neighborhood
     * @return GrayImage modified by local histogram equalization
     */
    public GrayImage histogramEqLocal(int windowSize) {
        int padSize = windowSize / 2;
        int[][] padded2D = nPadArray(pixels2D, padSize);

        int[][] result2D = new int[width][height];

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                int paddedX = x + padSize;
                int paddedY = y + padSize;
                result2D[x][y] = processNeighborhood(padded2D, paddedX, paddedY, windowSize);
            }
        }

        return new GrayImage(result2D, 8);
    }

    /**
     * Adds padding to all sides of a 2D array. Simply replicates the border pixels.
     *
     * @param old2D pixels2D
     * @param padSize how many pixels to pad
     * @return padded array[][]
     */
    private int[][] nPadArray(int[][] old2D, int padSize) {
        /**
         * TODO: debug
         */

        int oldW = old2D.length;
        int oldH = old2D[0].length;

        int newW = oldW + 2 * padSize;
        int newH = oldH + 2 * padSize;

        int[][] new2D = new int[newW][newH];

        // Pad the left side
        for (int x = 0; x < padSize; ++x) {
            for (int y = 0; y < padSize; ++y) {
                new2D[x][y] = old2D[0][0];
            }
            System.arraycopy(old2D[0], 0, new2D[x], padSize, oldH);
            for (int y = newH - padSize; y < newH; ++y) {
                new2D[x][y] = old2D[0][oldH - 1];
            }
        }

        // Pad the center on top and bottom.
        for (int x = padSize; x < newW - padSize; ++x) {
            for (int y = 0; y < padSize; ++y) {
                new2D[x][y] = old2D[x - padSize][0];
            }
            System.arraycopy(old2D[x - padSize], 0, new2D[x], padSize, oldH);
            for (int y = newH - padSize; y < newH; ++y) {
                new2D[x][y] = old2D[x - padSize][oldH - 1];
            }
        }

        // Pad the right side
        for (int x = newW - padSize; x < newW; ++x) {
            for (int y = 0; y < padSize; ++y) {
                new2D[x][y] = old2D[oldW - 1][0];
            }
            System.arraycopy(old2D[oldW - 1], 0, new2D[x], padSize, oldH);
            for (int y = newH - padSize; y < newH; ++y) {
                new2D[x][y] = old2D[oldW - 1][oldH - 1];
            }
        }
        return new2D;
    }

    /**
     * Helper function for histogramEqLocal. Processes an individual pixel's value based on its neighborhood.
     * A pixel's equalized brightness value is equal to the number of pixels in the neighborhood that are darker
     * than the pixel.
     *
     * @param padded2D pixel array, padded to account for mask
     * @param xCoord x coordinate of pixel within padded2D
     * @param yCoord y coordinate of pixel within padded2D
     * @param windowSize mask dimension
     * @return the equalized value of the center pixel
     */
    private int processNeighborhood(int[][] padded2D, int xCoord, int yCoord, int windowSize) {
        int w = windowSize / 2;

        int currentBrightness = padded2D[xCoord][yCoord];
        int counter = 0;

        for (int x = xCoord - w; x <= xCoord + w; ++x) {
            for (int y = yCoord - w; y <= yCoord + w; ++y) {
                if (padded2D[x][y] <= currentBrightness) {
                    counter++;
                }
            }
        }
        return (int) ((counter * 255.0) / (double) (windowSize * windowSize));
    }

    /**
     * Performs histogram specification. Modifies the image to match intensity distribution of reference.
     *
     * @param reference The image we are trying to match.
     * @return modified GrayImage
     */
    public GrayImage histogramMatching(GrayImage reference) {
        /**
         * TODO: need to write this
         */
        long[] hMod = this.histogram;
        long[] hRef = reference.histogram;
        double[] cdfMod = this.cdf();
        double[] cdfRef = reference.cdf();

        return this;
    }


    /**
     * Calculates the cumulative distribution function based on the histogram. This is used for histogram matching.
     *
     * @return double[] that stores the CDF.
     */
    private double[] cdf() {
        int numPixels = width * height;
        double[] result = new double[this.histogram.length];
        int runningTotal = 0;
        for (int i = 0; i < result.length; ++i) {
            runningTotal += this.histogram[i];
            result[i] = (double) runningTotal / numPixels;
        }
        return result;
    }

    /**
     * Pads array with 1 extra row and 1 extra column of replicated pixels.
     *
     * @param pixels pixels2D
     * @return padded array
     */
    private int[][] padArray(int[][] pixels) {
        int h = pixels[0].length + 1;
        int w = pixels.length + 1;
        int[][] result = new int[w][h];
        for (int x = 0; x < w-1; ++x) {
            System.arraycopy(pixels[x], 0, result[x], 0, h - 1);
            result[x][h-1] = pixels[x][h-2];
        }
        result[w-1] = result[w-2];
        return result;
    }

    private static double[] int2doubleArr(int[] intArr) {
        double[] result = new double[intArr.length];
        for (int i = 0; i < result.length; ++i) {
            result[i] = (double) intArr[i];
        }
        return result;
    }

}
