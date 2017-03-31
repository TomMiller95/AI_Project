package data;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PixelMachine {

	public PixelMachine() throws IOException{

		BufferedImage hugeImage = ImageIO.read(Driver.class.getResource("hungryCarly.png"));
		//BufferedImage hugeImage = ImageIO.read(Driver.class.getResource("colorTest.png"));

			//int[][] result = convertTo2DUsingGetRGB(hugeImage);
			genEasyImage(hugeImage);
			//randomPixels();
	}

	private static int[][] convertTo2DUsingGetRGB(BufferedImage image) { 
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] result = new int[height][width];
		
		int n = 0;
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				result[row][col] = image.getRGB(col, row);
				//System.out.println(image.getRGB(col,row));
				n++;
			}
		}
		System.out.println(height + " " + width);
		System.out.println(n);

		return result;
	}
	
	
	
	public static Image getImageFromArray(int[] pixels, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        WritableRaster raster = (WritableRaster) image.getData();
        raster.setPixels(0,0,width,height,pixels);
        return image;
    }
	
	
	
	
	
	private static void genEasyImage(BufferedImage image)
	{
		int width = image.getWidth();
		int height = image.getHeight();
		int h = height % 40;
		int w = width % 40;
		//height -= h;
		//width -= w;
		
		int[][] result = new int[height][width];
													System.out.println(result.length);
	    for (int row = 0; row < height; row++) {
	       for (int col = 0; col < width; col++) {
	          result[row][col] = image.getRGB(col, row);
	       }
	    }
									
		//getImageFromArray(arrayResult, width, height);
		randPixels(result, height, width);
	}
	

	
	private static void randPixels(int[][] pixels, int width, int height)
	{
	     //create buffered image object img
	     BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	     //file object
	     File f = null;
	     //create random image pixel by pixel
	     for(int x = 0; x < width; x++){
	    	 for(int y = 0; y < height; y++){
	         
	    	   int p = pixels[x][y];
	    	   
	    	   img.setRGB(x, y, p);
	       }
	     }
	     //write image
	     try{
	       f = new File("D:\\Image\\Output.png");
	       ImageIO.write(img, "png", f);
	     }catch(IOException e){
	       System.out.println("Error: " + e);
	     }
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private static void mehgenEasyImage(BufferedImage image)
	{
		int width = image.getWidth();
		int height = image.getHeight();
		int h = height % 40;
		int w = width % 40;
		//height -= h;
		//width -= w;
		
		int[] arrayResult = new int[height * width];
		
		int x = 0;
		for (int row = 0; row < width; row++) {
			for (int col = 0; col < height; col++) {
				arrayResult[x] = image.getRGB(row, col);
				x++;
			}
		}
									
		//getImageFromArray(arrayResult, width, height);
		randomPixels(arrayResult, width, height);
	}
	
	
	
	private static void findColor()
	{
		
	}
	
	
	
	
	
	private static void randomPixels(int[] pixels, int width, int height)
	{
	     //create buffered image object img
	     BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	     //file object
	     File f = null;
	     //create random image pixel by pixel
	     
	     //for(int y = 0; y < height; y++) {
	    	// for(int x = 0; x < width; x++){
	     for (int y = height; y > 0; y--) {
	    	 for (int x = width; x > 0; x--) {
	         
	    	   int p = pixels[x+y];
	    	   
	    	   img.setRGB(x, y, p);
	       }
	     }
	     //write image
	     try{
	       f = new File("D:\\Image\\Output.png");
	       ImageIO.write(img, "png", f);
	     }catch(IOException e){
	       System.out.println("Error: " + e);
	     }
	}
}
