//package com.capestone.classpoll;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.RandomAccessFile;
//import java.nio.MappedByteBuffer;
//import java.nio.channels.FileChannel;
//
//
//
//import android.app.Activity;
//import android.graphics.Bitmap;
//import android.graphics.Bitmap.CompressFormat;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.ColorMatrix;
//import android.graphics.ColorMatrixColorFilter;
//import android.graphics.Paint;
//import android.os.Bundle;
//import android.os.Environment;
//import android.util.Log;
//import android.view.Menu;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//
//public class MainActivity extends Activity {
//	Bitmap myBitmap;
//	private Button processImage;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//		setContentView(R.layout.activity_main);
//		
//		
//	   //Initializing widgets	
//	   processImage = (Button) findViewById (R.id.button1);
//	   
//	   processImage.setOnClickListener(new OnClickListener(){ public  void onClick(View v){process(myBitmap);}});
//	   System.out.println("DONE!");
//		
//
//
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//	
//	
//	public static void process(Bitmap myBitmap){
//		CannyEdgeDetector detector = new CannyEdgeDetector();
//		File imageFile = new File("/mnt/sdcard/picture.jpg");
//		myBitmap = BitmapFactory.decodeFile("/mnt/sdcard/picture.jpg");
//		Bitmap edged = detector.filter(convertToMutable(new MainActivity().toGrayscale(myBitmap)));
//		System.out.println("Saving Bitmap");
//		new MainActivity().saveBitmap(edged);
//		
//		
//	}
//
//	/**
//	 * Converts a immutable bitmap to a mutable bitmap. This operation doesn't allocates
//	 * more memory that there is already allocated.
//	 * 
//	 * @param imgIn - Source image. It will be released, and should not be used more
//	 * @return a copy of imgIn, but muttable.
//	 */
//	public static Bitmap convertToMutable(Bitmap imgIn) {
//        try {
//            //this is the file going to use temporally to save the bytes.
//            // This file will not be a image, it will store the raw image data.
//            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "temp.tmp");
// 
//            //Open an RandomAccessFile
//            //Make sure you have added uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
//            //into AndroidManifest.xml file
//            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
// 
//            // get the width and height of the source bitmap.
//            int width = imgIn.getWidth();
//            int height = imgIn.getHeight();
//            Bitmap.Config type = imgIn.getConfig();
// 
//            //Copy the byte to the file
//            //Assume source bitmap loaded using options.inPreferredConfig = Config.ARGB_8888;
//            FileChannel channel = randomAccessFile.getChannel();
//            MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, imgIn.getRowBytes()*height);
//            imgIn.copyPixelsToBuffer(map);
//            //recycle the source bitmap, this will be no longer used.
//            imgIn.recycle();
//            System.gc();// try to force the bytes from the imgIn to be released
// 
//            //Create a new bitmap to load the bitmap again. Probably the memory will be available.
//            imgIn = Bitmap.createBitmap(width, height, type);
//            map.position(0);
//            //load it back from temporary
//            imgIn.copyPixelsFromBuffer(map);
//            //close the temporary file and channel , then delete that also
//            channel.close();
//            randomAccessFile.close();
// 
//            // delete the temp file
//            file.delete();
// 
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
// 
//        return imgIn;
//    }
//	public void saveBitmap(Bitmap bm)
//	{
//	    try
//	    {
//	        String mBaseFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/";
//	        String mFilePath = mBaseFolderPath + "L55H90.jpg";
//
//	        FileOutputStream stream = new FileOutputStream(mFilePath);
//	        bm.compress(CompressFormat.JPEG, 100, stream);
//	        stream.flush();
//	        stream.close();
//	    }
//	    catch(Exception e)
//	    {
//	        Log.e("Could not save", e.toString());
//	    }
//	}
//	
//	public Bitmap toGrayscale(Bitmap bmpOriginal)
//	{        
//	    int width, height;
//	    height = bmpOriginal.getHeight();
//	    width = bmpOriginal.getWidth();    
//
//	    Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
//	    Canvas c = new Canvas(bmpGrayscale);
//	    Paint paint = new Paint();
//	    ColorMatrix cm = new ColorMatrix();
//	    cm.setSaturation(0);
//	    ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
//	    paint.setColorFilter(f);
//	    c.drawBitmap(bmpOriginal, 0, 0, paint);
//	    return bmpGrayscale;
//	}
//}


package com.capestone.classpoll;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import com.capestone.classpoll.BlobDetection.Blob;

//import photo.viewer.R;



import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	Bitmap myBitmap;
	ImageView img,img1;
	static Bitmap edged;
	static Bitmap converted;
	static Bitmap blurred;
	static Bitmap blobbed;
	static Bitmap yourSelectedImage;
	static final int SELECT_PHOTO = 100;
	static final int TAKE_PICTURE = 1;    
	private Uri imageUri;
	
	//Buttons
	static Button processImageButton;
	static Button chooseImageButton;
	static Button takePhotoButton;
	
	static ImageView imgView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		
		

	   //Initializing widgets
		imgView = (ImageView) findViewById(R.id.imageView1);
		processImageButton = (Button) findViewById (R.id.button1);
		processImageButton.setEnabled(false);
		chooseImageButton = (Button) findViewById (R.id.button2);
		takePhotoButton = (Button) findViewById (R.id.button3);
		//display the picture to be processed
		//imgView.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath()+"/picture.jpg"));
		
		/*********************************Button Events***********************/
		//Choosing and image to process from gallery
		chooseImageButton.setOnClickListener(new OnClickListener(){ public  void onClick(View v){
			//process(myBitmap);
			processImageButton.setEnabled(true);//############need to check if everything went well
			Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
			photoPickerIntent.setType("image/*");
			startActivityForResult(photoPickerIntent, SELECT_PHOTO);
			
			}});
		//Take the image using the device's camera
		takePhotoButton.setOnClickListener(new OnClickListener(){ public  void onClick(View v){
			//process(myBitmap);
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		    File photo = new File(Environment.getExternalStorageDirectory(),  "picture.jpg");
		    intent.putExtra(MediaStore.EXTRA_OUTPUT,
		            Uri.fromFile(photo));
		    imageUri = Uri.fromFile(photo);
		    startActivityForResult(intent, TAKE_PICTURE);
		    processImageButton.setEnabled(true);
		}});
		//Process the image chosen of taken using the camera
		processImageButton.setOnClickListener(new OnClickListener(){ public  void onClick(View v){
			process(yourSelectedImage);
			processImageButton.setEnabled(false);//Disable button to stop further processing
			//Display the processed image
			imgView.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/Camera/L60H90.jpg"));
		}});
		
		
		System.out.println("DONE!");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	/**
	 * Called by clicking process button
	 * @param myBitmap
	 */
	public static void process(Bitmap myBitmap){
		CannyEdgeDetector detector = new CannyEdgeDetector();
		long startTime = System.nanoTime();
		
		converted = convertToMutable(new MainActivity().toGrayscale(myBitmap));
		//blurred = detector.applyGaussianBlur(converted);
		//////////////////////////
		ConvolutionMatrix cm = new ConvolutionMatrix();
		//blurred = cm.fastblur(converted, 4);
		/////////////////////////
		edged = detector.filter(converted);
		
		BlobDetection blob = new BlobDetection(edged);
		blobbed = blob.getBlob(edged);
		
		
		//to send original image to be compared with the location of blobs
		calculatePoll(blob.blobList, myBitmap);
		
		
		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		Log.i("information", ""+duration);
		System.out.println("Saving Bitmap");
		new MainActivity().saveBitmap(blobbed);
			
		
	}

	
	 /**
	  * Convert bitmap to mutable to allow editing of pixels
	  * @param imgIn
	  * @return
	  */
	public static Bitmap convertToMutable(Bitmap imgIn) {
        try {
            //this is the file going to use temporally to save the bytes.
            // This file will not be a image, it will store the raw image data.
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "temp.tmp");
 
            //Open an RandomAccessFile
            //Make sure you have added uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
            //into AndroidManifest.xml file
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
 
            // get the width and height of the source bitmap.
            int width = imgIn.getWidth();
            int height = imgIn.getHeight();
            Bitmap.Config type = imgIn.getConfig();
 
            //Copy the byte to the file
            //Assume source bitmap loaded using options.inPreferredConfig = Config.ARGB_8888;
            FileChannel channel = randomAccessFile.getChannel();
            MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, imgIn.getRowBytes()*height);
            imgIn.copyPixelsToBuffer(map);
            //recycle the source bitmap, this will be no longer used.
            imgIn.recycle();
            System.gc();// try to force the bytes from the imgIn to be released
 
            //Create a new bitmap to load the bitmap again. Probably the memory will be available.
            imgIn = Bitmap.createBitmap(width, height, type);
            map.position(0);
            //load it back from temporary
            imgIn.copyPixelsFromBuffer(map);
            //close the temporary file and channel , then delete that also
            channel.close();
            randomAccessFile.close();
 
            // delete the temp file
            file.delete();
 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        return imgIn;
    }
	/**
	 * Saving image to the device
	 * @param bm
	 */
	public void saveBitmap(Bitmap bm)
	{
	    try
	    {
	        String mBaseFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/DCIM/Camera/";
	        String mFilePath = mBaseFolderPath + "/L60H90.jpg";

	        FileOutputStream stream = new FileOutputStream(mFilePath);
	        bm.compress(CompressFormat.JPEG, 100, stream);
	        stream.flush();
	        stream.close();
	    }
	    catch(Exception e)
	    {
	        Log.e("Could not save", e.toString());
	    }
	}
	/**
	 * Converting to grayscale
	 * @param bmpOriginal
	 * @return a gryscaled image
	 */
	public Bitmap toGrayscale(Bitmap bmpOriginal)
	{        
	    int width, height;
	    height = bmpOriginal.getHeight();
	    width = bmpOriginal.getWidth();    

	    Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
	    Canvas c = new Canvas(bmpGrayscale);
	    Paint paint = new Paint();
	    ColorMatrix cm = new ColorMatrix();
	    cm.setSaturation(0);
	    ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
	    paint.setColorFilter(f);
	    c.drawBitmap(bmpOriginal, 0, 0, paint);
	    return bmpGrayscale;
	}
	/**
	 * This method calculates the poll 
	 * @param blobList 
	 * @param original
	 */
	public static void calculatePoll(List<Blob> blobList, Bitmap original){
		String[] dims;
		int x1,x2,y1,y2,X,Y;
		int pixel;
		int black=0,green=0,red=0,blue=0;
	
		
		for(int i=0; i<blobList.size(); i++){
			System.out.println("From Method:"+blobList.get(i));
			
			dims = blobList.get(i).toString().split(",");
			x1 = Integer.parseInt(dims[0].trim());
			x2 = Integer.parseInt(dims[1].trim());
			y1 = Integer.parseInt(dims[2].trim());
			y2 = Integer.parseInt(dims[3].trim());
			
			X = (x1+x2)/2;
			Y = (y1+y2)/2;
			pixel = original.getPixel(X,Y);
			System.out.println("PXL"+pixel);
			if(pixel == Color.BLACK || (pixel<-15000000 && pixel >-17000000)){
				black++;
			}
			if(pixel == Color.GREEN){
				green++;
			}
			if(pixel == Color.RED){
				red++;
			}
			if(pixel == Color.BLUE){
				blue++;
			}
		}
		System.out.println("Green-"+green);
		System.out.println("Red-"+red);
		System.out.println("Blue-"+blue);
		System.out.println("Black-"+black);
	}

	/**
	 * To deal with the resulting image
	 */
	//##########################################################################################
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 
	    super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 

	    switch(requestCode) { 
	    case SELECT_PHOTO:
	        if(resultCode == RESULT_OK){  
	            Uri selectedImage = imageReturnedIntent.getData();
	            InputStream imageStream = null;
				try {
					imageStream = getContentResolver().openInputStream(selectedImage);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//################We need to use this file/bitmap for processing 
	            yourSelectedImage = BitmapFactory.decodeStream(imageStream);
	            imgView.setImageBitmap(yourSelectedImage);
	        }break;
	    case TAKE_PICTURE:
	        if (resultCode == Activity.RESULT_OK) {
	            Uri selectedImage = imageUri;
	            getContentResolver().notifyChange(selectedImage, null);
	            ContentResolver cr = getContentResolver();
	            InputStream imageStream = null;
	            try {
	            	imageStream = getContentResolver().openInputStream(selectedImage);
	            	
	            	yourSelectedImage = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);
	
	            	yourSelectedImage = BitmapFactory.decodeStream(imageStream);
		            imgView.setImageBitmap(yourSelectedImage);
	                Toast.makeText(this, selectedImage.toString(),
	                        Toast.LENGTH_LONG).show();
	            } catch (Exception e) {
	                Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
	                        .show();
	                Log.e("Camera", e.toString());
	            }
	        }break;
	    }
	}
}
