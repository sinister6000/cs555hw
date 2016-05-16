package imageprocessor.model;

/**
 * Collection of static methods for array and matrix functions.
 */

class MatrixUtils {

    // return a random m-by-n matrix with values between 0 and 1
    static double[][] random(int m, int n) {
        double[][] C = new double[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = Math.random();
        return C;
    }

    // return n-by-n identity matrix I
    static double[][] identity(int n) {
        double[][] I = new double[n][n];
        for (int i = 0; i < n; i++)
            I[i][i] = 1;
        return I;
    }

    // return x^T y
    static double dot(double[] x, double[] y) {
        if (x.length != y.length) throw new RuntimeException("Illegal vector dimensions.");
        double sum = 0.0;
        for (int i = 0; i < x.length; i++)
            sum += x[i] * y[i];
        return sum;
    }

    // return x^T y
    static int dot(int[] x, int[] y) {
        if (x.length != y.length) throw new RuntimeException("Illegal vector dimensions.");
        int sum = 0;
        for (int i = 0; i < x.length; i++)
            sum += x[i] * y[i];
        return sum;
    }

    // return C = A^T
    static double[][] transpose(double[][] A) {
        int m = A.length;
        int n = A[0].length;
        double[][] C = new double[n][m];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                C[j][i] = A[i][j];
        return C;
    }

    // return C = A + B
    static double[][] add(double[][] A, double[][] B) {
        int m = A.length;
        int n = A[0].length;
        double[][] C = new double[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = A[i][j] + B[i][j];
        return C;
    }

    // return C = A - B
    static double[][] subtract(double[][] A, double[][] B) {
        int m = A.length;
        int n = A[0].length;
        double[][] C = new double[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = A[i][j] - B[i][j];
        return C;
    }

    // return C = A - B
    static int[][] subtract(int[][] A, int[][] B) {
        int m = A.length;
        int n = A[0].length;
        int[][] C = new int[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = A[i][j] - B[i][j];
        return C;
    }

    // return C = A - B
    static int[] subtract(int[] A, int[] B) {
        int m = A.length;
        int[] C = new int[m];
        for (int i = 0; i < m; i++)
            C[i]= A[i] - B[i];
        return C;
    }

    // return C = A - B
    static double[] subtract(double[] A, double[] B) {
        int m = A.length;
        double[] C = new double[m];
        for (int i = 0; i < m; i++)
            C[i]= A[i] - B[i];
        return C;
    }

    // return C = A + scalar
    static double[][] add(double[][] A, double num) {
        int m = A.length;
        int n = A[0].length;
        double [][] C = new double[m][n];
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                C[i][j] = A[i][j] + num;
            }
        }
        return C;
    }

    // return C = A + scalar
    static int[][] add(int[][] A, int num) {
        int m = A.length;
        int n = A[0].length;
        int [][] C = new int[m][n];
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                C[i][j] = A[i][j] + num;
            }
        }
        return C;
    }

    // return sum of elements
    static int sum(int[] A) {
        int sum = 0;
        for (int a : A) {
            sum += a;
        }
        return sum;
    }

    // return C = A * B
    static double[][] multiply(double[][] A, double[][] B) {
        int mA = A.length;
        int nA = A[0].length;
        int mB = B.length;
        int nB = B[0].length;
        if (nA != mB) throw new RuntimeException("Illegal matrix dimensions.");
        double[][] C = new double[mA][nB];
        for (int i = 0; i < mA; i++)
            for (int j = 0; j < nB; j++)
                for (int k = 0; k < nA; k++)
                    C[i][j] += A[i][k] * B[k][j];
        return C;
    }

    // matrix-vector multiplication (y = A * x)
    static double[] multiply(double[][] A, double[] x) {
        int m = A.length;
        int n = A[0].length;
        if (x.length != n) throw new RuntimeException("Illegal matrix dimensions.");
        double[] y = new double[m];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                y[i] += A[i][j] * x[j];
        return y;
    }


    // vector-matrix multiplication (y = x^T A)
    static double[] multiply(double[] x, double[][] A) {
        int m = A.length;
        int n = A[0].length;
        if (x.length != m) throw new RuntimeException("Illegal matrix dimensions.");
        double[] y = new double[n];
        for (int j = 0; j < n; j++)
            for (int i = 0; i < m; i++)
                y[j] += A[i][j] * x[i];
        return y;
    }

    // matrix-scalar multiplication (C = a * B)
    static double[][] multiply(double a, double[][] B) {
        int m = B.length;
        int n = B[0].length;
        double[][] C = new double[m][n];
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                C[i][j] = a * B[i][j];
            }
        }
        return C;
    }

    // matrix-scalar multiplication (C = a * B)
    static int[][] multiply(double a, int[][] B) {
        int m = B.length;
        int n = B[0].length;
        int[][] C = new int[m][n];
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                C[i][j] = (int) Math.round(a * B[i][j]);
            }
        }
        return C;
    }

    static int[] getMaskedPixels(int[][] pixels, int x, int y, int maskWidth, int maskHeight) {
        int[][] masked2D = new int[maskWidth][maskHeight];
        int mPad = maskWidth / 2;
        int nPad = maskHeight / 2;

        for (int xOffset = -1 * mPad; xOffset <= mPad; ++xOffset) {
            for (int yOffset = -1 * nPad; yOffset <= nPad; ++yOffset) {
                masked2D[xOffset + mPad][yOffset + nPad] = pixels[x + xOffset][y + yOffset];
            }
        }
        return pixels1Dfrom2D(masked2D);
    }

    static double[][] trimPad(double[][] paddedDouble2D, int padW, int padH) {
        double[][] result2D = new double[paddedDouble2D.length - 2*padW][paddedDouble2D[0].length - 2*padH];
        for (int x = 0; x < result2D.length; ++x) {
            for (int y = 0; y < result2D[0].length; ++y) {
                int paddedX = x + padW;
                int paddedY = y + padH;
                result2D[x][y] = paddedDouble2D[paddedX][paddedY];
            }
        }
        return result2D;
    }

    static int[][] trimPad(int[][] paddedDouble2D, int padW, int padH) {
        int[][] result2D = new int[paddedDouble2D.length - 2*padW][paddedDouble2D[0].length - 2*padH];
        for (int x = 0; x < result2D.length; ++x) {
            for (int y = 0; y < result2D[0].length; ++y) {
                int paddedX = x + padW;
                int paddedY = y + padH;
                result2D[x][y] = paddedDouble2D[paddedX][paddedY];
            }
        }
        return result2D;
    }

    static double[] int2doubleArr(int[] intArr) {
        double[] result = new double[intArr.length];
        for (int i = 0; i < result.length; ++i) {
            result[i] = (double) intArr[i];
        }
        return result;
    }

    static double[][] int2doubleArr(int[][] intArr) {
        double[][] result = new double[intArr.length][intArr[0].length];
        for (int i = 0; i < intArr.length; ++i) {
            for (int j = 0; j < intArr[0].length; ++j) {
                result[i][j] = (double) intArr[i][j];
            }
        }
        return result;
    }

    static int[] double2intArr(double[] doubleArr) {
        int[] result = new int[doubleArr.length];
        for (int i = 0; i < result.length; ++i) {
            result[i] = (int) doubleArr[i];
        }
        return result;
    }

    static int[][] double2intArr(double[][] doubleArr) {
        int[][] result = new int[doubleArr.length][doubleArr[0].length];
        for (int i = 0; i < doubleArr.length; ++i) {
            for (int j = 0; j < doubleArr[0].length; ++j) {
                result[i][j] = (int) doubleArr[i][j];
            }
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
    static int[] normalizePixelValues(double[] pixels, double targetMin, double targetMax) {
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
    static int[] normalizePixelValues(int[] pixels, double targetMin, double targetMax) {
        double[] doublePixels = int2doubleArr(pixels);
        return normalizePixelValues(doublePixels, targetMin, targetMax);
    }

    /**
     * Finds the max value in an array.
     *
     * @param arr array of doubles
     * @return max
     */
    static double arrayMax(double[] arr) {
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
    static double arrayMin(double[] arr) {
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
    static int arrayMax(int[] arr) {
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
    static int arrayMin(int[] arr) {
        int min = Integer.MAX_VALUE;
        for (int x : arr) {
            if (x < min) {
                min = x;
            }
        }
        return min;
    }
    
    /**
     * Given a 2D array of pixels, returns equivalent 1D array. Helper function for class constructor.
     *
     * @return 1D pixel array
     * @param pixels2D 2D array
     */
    static int[] pixels1Dfrom2D(int[][] pixels2D) {
        int[] result = new int[pixels2D.length * pixels2D[0].length];
        int resultIndex = 0;
        for (int y = 0; y < pixels2D[0].length; ++y) {
            for (int x = 0; x < pixels2D.length; ++x) {
                result[resultIndex++] = pixels2D[x][y];
            }
        }
        return result;
    }

    /**
     * Given a 2D array of pixels, returns equivalent 1D array. Helper function for class constructor.
     *
     * @return 1D pixel array
     * @param pixels2D 2D array
     */
    static double[] pixels1Dfrom2D(double[][] pixels2D) {
        double[] result = new double[pixels2D.length * pixels2D[0].length];
        int resultIndex = 0;
        for (int y = 0; y < pixels2D[0].length; ++y) {
            for (int x = 0; x < pixels2D.length; ++x) {
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
    static int[][] pixels2Dfrom1D(int[] pixels1D, int width, int height) {
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
     * Adds padding to all sides of a 2D array. Simply replicates the border pixels.
     *
     * @param old2D pixels2D
     * @param padType what to pad with. 0 means pad with 0's. 1 means pad with replicated border pixels.
     * @param padWidth width
     * @param padHeight height
     * @return padded array[][]
     */
    static int[][] nPadArray(int[][] old2D, int padType, int padWidth, int padHeight) {
        int oldW = old2D.length;
        int oldH = old2D[0].length;

        int newW = oldW + 2 * padWidth;
        int newH = oldH + 2 * padHeight;

        int[][] new2D = new int[newW][newH];

        // pad with replication
        if (padType == 1) {
            // Pad the left side
            for (int x = 0; x < padWidth; ++x) {
                for (int y = 0; y < padHeight; ++y) {
                    new2D[x][y] = old2D[0][0];
                }
                System.arraycopy(old2D[0], 0, new2D[x], padHeight, oldH);
                for (int y = newH - padHeight; y < newH; ++y) {
                    new2D[x][y] = old2D[0][oldH - 1];
                }
            }

            // Pad the center on top and bottom.
            for (int x = padWidth; x < newW - padWidth; ++x) {
                for (int y = 0; y < padHeight; ++y) {
                    new2D[x][y] = old2D[x - padWidth][0];
                }
                System.arraycopy(old2D[x - padWidth], 0, new2D[x], padHeight, oldH);
                for (int y = newH - padHeight; y < newH; ++y) {
                    new2D[x][y] = old2D[x - padWidth][oldH - 1];
                }
            }

            // Pad the right side
            for (int x = newW - padWidth; x < newW; ++x) {
                for (int y = 0; y < padHeight; ++y) {
                    new2D[x][y] = old2D[oldW - 1][0];
                }
                System.arraycopy(old2D[oldW - 1], 0, new2D[x], padHeight, oldH);
                for (int y = newH - padHeight; y < newH; ++y) {
                    new2D[x][y] = old2D[oldW - 1][oldH - 1];
                }
            }
        }

        // pad with 0's
        else if (padType == 0) {
            // Pad the left side
            for (int x = 0; x < padWidth; ++x) {
                for (int y = 0; y < newH; ++y) {
                    new2D[x][y] = 0;
                }
            }

            // Pad the center region.
            for (int x = padWidth; x < newW - padWidth; ++x) {

                // pad above
                for (int y = 0; y < padHeight; ++y) {
                    new2D[x][y] = 0;
                }

                // copy old array into center
                System.arraycopy(old2D[x - padWidth], 0, new2D[x], padHeight, oldH);

                // pad below
                for (int y = newH - padHeight; y < newH; ++y) {
                    new2D[x][y] = 0;
                }
            }

            // Pad the right side
            for (int x = newW - padWidth; x < newW; ++x) {
                for (int y = 0; y < newH; ++y) {
                    new2D[x][y] = 0;
                }
            }
        }

        return new2D;
    }
}
