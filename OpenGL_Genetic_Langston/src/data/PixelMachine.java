package data;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;

public class PixelMachine {

	public PixelMachine() throws IOException {

		BufferedImage hugeImage = ImageIO.read(Driver.class.getResource("test.png"));
        //BufferedImage hugeImage = ImageIO.read(Driver.class.getResource("blueTile.png"));
		genEasyImage(hugeImage);
	}


	private static void genEasyImage(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int h = height % 40;
		int w = width % 40;
		//height -= h;
		//width -= w;

        HashMap<String,Integer> hmap = new HashMap<>();
        int[] pixel;
		int[][] result = new int[height][width];
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				result[row][col] = image.getRGB(col, row);
                pixel = image.getRaster().getPixel(row, col, new int[3]);

                if (hmap.get(pixel[0] + " - " + pixel[1] + " - " + pixel[2]) == null)
                {
                    hmap.put(pixel[0] + " - " + pixel[1] + " - " + pixel[2],1);
                }
                else
                {
                    hmap.put(pixel[0] + " - " + pixel[1] + " - " + pixel[2], hmap.get(pixel[0] + " - " + pixel[1] + " - " + pixel[2]) + 1);
                }
                //System.out.println(pixel[0] + " - " + pixel[1] + " - " + pixel[2]);
			}
		}
        Iterator it = hmap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }

        buildPixelatedImage(result, height, width);

		//rebuildImage(result, height, width);
	}








    private static void buildPixelatedImage(int[][] pixels, int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        //file object
        File f = null;
        //create random image pixel by pixel

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                int p = pixels[y][x];
                img.setRGB(x, y, p);
            }
        }


        //write image
        try {
            f = new File("output.png");
            ImageIO.write(img, "png", f);
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }













	private static void rebuildImage(int[][] pixels, int width, int height) {
		//create buffered image object img
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		//file object
		File f = null;
		//create random image pixel by pixel

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {

				int p = pixels[y][x];
                img.setRGB(x, y, p);
			}
		}


		//write image
		try {
			f = new File("output.png");
			ImageIO.write(img, "png", f);
		} catch (IOException e) {
			System.out.println("Error: " + e);
		}
	}
}
