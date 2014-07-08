package com.capestone.classpoll;

import android.graphics.Bitmap;
import android.graphics.Color;

/** Canny Egde Detector Class
 * 
 * @author Robin and Monde
 * Ref: Stackoverflow and pastebin
 *
 */
public class CannyEdgeDetector {
    
    
    private double lowThreshold = 200;
    private double highThreshhold = 300;
    
    
    public static final int WHITE = -1;
    public static final int BLACK = -16777216;
    public static final int GRAY = -7829368;
    

    Bitmap bitmap;
    
    
    public Bitmap applyGaussianBlur(Bitmap src) {
    	  //set gaussian blur configuration
    	     double[][] GaussianBlurConfig = new double[][] {
    	         { 1, 2, 1 },
    	         { 2, 4, 2 },
    	         { 1, 2, 1 }
    	     };
    	     // create instance of Convolution matrix
    	     ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
    	     // Apply Configuration
    	     convMatrix.applyConfig(GaussianBlurConfig);
    	     //convMatrix.Factor = 16;
    	     //convMatrix.Offset = 0;
    	     //return out put bitmap
    	     return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
    	 }


    /**
     * Filter method will return an edged image 
     * @return the filtered image.
     */
    public Bitmap filter(Bitmap gray) {
        double[][] sobelX, sobelY;
        double[][] gd, gm;

        sobelX = sobelFilterX(gray);
        sobelY = sobelFilterY(gray);
        
        // Height and Width of Bitmap
        int width = gray.getWidth();
        int height = gray.getHeight();
        gd = new double[width][height];
        gm = new double[width][height];
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // setting gradient magnitude and gradient direction
                if (sobelX[x][y] != 0) {
                    gd[x][y] = Math.atan(sobelY[x][y] / sobelX[x][y]);
                } else {
                    gd[x][y] = Math.PI / 2.0;
                }
                gm[x][y] = Math.hypot(sobelY[x][y], sobelX[x][y]);
            }
        }
        // Non-maximum suppression
        for (int x = 0; x < width; x++) {
            gray.setPixel(x, 0, WHITE);
            gray.setPixel(x, height - 1, WHITE);
        }
        for (int y = 0; y < height; y++) {
            gray.setPixel(0, y, WHITE);
            gray.setPixel(width - 1, y,WHITE);
        }
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                if (gd[x][y] < (Math.PI / 8.0) && gd[x][y] >= (-Math.PI / 8.0)) {
                    if (gm[x][y] > gm[x + 1][y] && gm[x][y] > gm[x - 1][y])
                        setPixel(x, y, gray, gm[x][y]);
                    else
                        gray.setPixel(x,y, WHITE);
                } else if (gd[x][y] < (3.0 * Math.PI / 8.0) && gd[x][y] >= (Math.PI / 8.0)) {
                    if (gm[x][y] > gm[x - 1][y - 1] && gm[x][y] > gm[x - 1][y - 1])
                        setPixel(x, y, gray, gm[x][y]);
                    else
                        gray.setPixel(x,y, WHITE);
                } else if (gd[x][y] < (-3.0 * Math.PI / 8.0) || gd[x][y] >= (3.0 * Math.PI / 8.0)) {
                    if (gm[x][y] > gm[x][y + 1] && gm[x][y] > gm[x][y + 1])
                        setPixel(x, y, gray, gm[x][y]);
                    else
                        gray.setPixel(x,y, WHITE);
                } else if (gd[x][y] < (-Math.PI / 8.0) && gd[x][y] >= (-3.0 * Math.PI / 8.0)) {
                    if (gm[x][y] > gm[x + 1][y - 1] && gm[x][y] > gm[x - 1][y + 1])
                        setPixel(x, y, gray, gm[x][y]);
                    else
                        gray.setPixel(x, y, WHITE);
                } else {
                    gray.setPixel(x,y, WHITE);
                }
            }
        }
        // hysteresis ... walk along lines of strong pixels and make the weak ones strong.
      
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                if (gray.getPixel(x, y) < 50) {
                    // It's a strong pixel, lets find the neighbouring weak ones.
                    replaceWeakPixels(x, y, gray);
                }
            }
        }

        //inverting image
        for (int x = 0; x < width ; x++) {
            for (int y = 0; y < height ; y++) {
            	//####NOTE####
                if (gray.getPixel(x, y)> GRAY ) {
                    gray.setPixel(x, y, BLACK );
                } else gray.setPixel(x,y, WHITE);
            }
        }
        	
      
        return gray;
    }

    /**
     * removing weak pixels 
     *
     * @param x
     * @param y
     * @param gray
     */
    private void replaceWeakPixels(int x, int y, Bitmap gray) {
        for (int xx = x - 1; xx <= x + 1; xx++)
            for (int yy = y - 1; yy <= y + 1; yy++) {
                if (isWeak(xx, yy, gray)) {
                    gray.setPixel(xx, yy, BLACK);
                    replaceWeakPixels(xx, yy, gray);
                }
            }
    }

    private boolean isWeak(int x, int y, Bitmap gray) {
        return (gray.getPixel(x, y) > 0 && gray.getPixel(x, y) > -1);
    }

    private void setPixel(int x, int y, Bitmap gray, double v) {
        if (v > highThreshhold) gray.setPixel(x, y, BLACK);
        else if (v > lowThreshold) gray.setPixel(x, y, GRAY);
        else gray.setPixel(x,y, WHITE);
    }
    /**
     * Sobel filter Y 
     * @param gray
     * @return array of pixels
     */

    private double[][] sobelFilterY(Bitmap gray) {
        double[][] finalTotal = new double[gray.getWidth()][gray.getHeight()];
        int totalG = 0;
        
        for (int x = 1; x < gray.getWidth() - 1; x++) {
            for (int y = 1; y < gray.getHeight() - 1; y++) {
                totalG = 0;
                totalG += Color.green(gray.getPixel(x - 1, y - 1));
                totalG += Color.green(2 * gray.getPixel(x, y - 1));
                totalG += Color.green(gray.getPixel(x + 1, y - 1));
                totalG -= Color.green(gray.getPixel(x - 1, y + 1));
                totalG -= Color.green(2 * gray.getPixel(x, y + 1));
                totalG -= Color.green(gray.getPixel(x + 1, y + 1));
                finalTotal[x][y] = totalG;
            }
        }
        for (int x = 0; x < gray.getWidth(); x++) {
            finalTotal[x][0] = 0;
            finalTotal[x][gray.getHeight() - 1] = 0;
        }
        for (int y = 0; y < gray.getHeight(); y++) {
            finalTotal[0][y] = 0;
            finalTotal[gray.getWidth() - 1][y] = 0;
        }
        return finalTotal;
    }
    /**
     * Sobel filter X
     * @param gray
     * @return array of pixels
     */
    
    private double[][] sobelFilterX(Bitmap gray) {
        double[][] finalTotal = new double[gray.getWidth()][gray.getHeight()];
        int totalG = 0;
        for (int x = 1; x < gray.getWidth() - 1; x++) {
            for (int y = 1; y < gray.getHeight() - 1; y++) {
                totalG = 0;
                
                totalG += Color.green(gray.getPixel(x - 1, y - 1));
                totalG += Color.green(2 * gray.getPixel(x - 1, y));
                totalG += Color.green(gray.getPixel(x - 1, y + 1));
                totalG -= Color.green(gray.getPixel(x + 1, y - 1));
                totalG -= Color.green(2 * gray.getPixel(x + 1, y));
                totalG -= Color.green(gray.getPixel(x + 1, y + 1));
                finalTotal[x][y] = totalG;
               
            }
        }
        for (int x = 0; x < gray.getWidth(); x++) {
            finalTotal[x][0] = 0;
            finalTotal[x][gray.getHeight() - 1] = 0;
        }
        for (int y = 0; y < gray.getHeight(); y++) {
            finalTotal[0][y] = 0;
            finalTotal[gray.getWidth() - 1][y] = 0;
        }
        return finalTotal;
    }


}