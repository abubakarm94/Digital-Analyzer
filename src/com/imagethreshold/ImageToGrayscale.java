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
import java.awt.image.BufferedImage;

import com.enums.LogAreaEnums;
import com.uielements.LogArea;

public class ImageToGrayscale {

	private Image convertedImage;
	private Picture pic;
	private final int THRESHOLD = 180;
	
	private LogArea logArea = LogArea.getInstance();

	public ImageToGrayscale(String imagePath) {

		logArea.append(LogAreaEnums.ImageConversionStarted);

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
		logArea.append(LogAreaEnums.ImageConversionFinished);

	
	}
	public ImageToGrayscale(BufferedImage imagePath) {

		logArea.append(LogAreaEnums.ImageConversionStarted);

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
		logArea.append(LogAreaEnums.ImageConversionFinished);

	
	}
	
	public Image getConvertedImage(){
		return convertedImage;
	}
	
	
}
