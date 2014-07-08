package com.capestone.classpoll;

import android.graphics.Bitmap;

public class FloodFill {
	
	/**
	 * To fill open large spaces for easy identification
	 * @param binaryimage
	 * @param structure
	 * @return
	 */
	public int[] Erosion(Bitmap binaryimage, int[][] structure) 
	{
	   
	    int picw =  binaryimage.getWidth();  
	    int pich =  binaryimage.getHeight(); 
	    int[] pixels = new int [picw * pich];
	    int[] output = new int[picw * pich];
	    int sw = structure[0].length; // columns
	    int sh = structure.length; // rows
	    binaryimage.getPixels(pixels,0,picw,0,0,picw,pich);
	    for(int y=sh/2; y<pich-sh/2; y++)
	    {
	        for(int x=sw/2;x<picw-sw/2;x++)
	        {
	            int index = y*picw+ x;
	            output[index]=1;
	            if(pixels[index]==0)
	                {
	                output[index]= 0;
	                }
	            else
	            for (int i=-sh/2; i<= sh/2; i++)
	            {
	                for(int j=-sw/2; j<=sw/2; j++)
	                {
	                    int index2=(x+j)+(y+i)*picw;
	                    if(pixels[index2]==0)
	                    {
	                        output[index]=0;
	                    }
	                }
	            }
	        }
	    }
	    return output;
	}
}
