package com.imagethreshold;
/******************************************************************************
 *  Dependencies: Picture.java Luminance.java
 *
 *  Reads in a JPEG/GIF/PNG file
 *  converts all pixels to grayscale, and displays those
 *  pixels with a grayscale value >= 180.
 *
 ******************************************************************************/

import java.awt.Color;
import java.awt.Image;

public class ImageToGrayscale {

	private Image convertedImage;
	private Picture pic;
	private final int THRESHOLD = 180;

	public ImageToGrayscale(String imagePath) {

		pic = new Picture(imagePath);

		for (int i = 0; i < pic.width(); i++) {
			for (int j = 0; j < pic.height(); j++) {
				Color color = pic.get(i, j);
				double lum = Luminance.lum(color);
				if (lum >= THRESHOLD)
					pic.set(i, j, Color.WHITE);
				else
					pic.set(i, j, Color.BLACK);
			}
		}

		convertedImage = pic.getImage();
	
	}
	
	public Image getConvertedImage(){
		return convertedImage;
	}
	
	
}
