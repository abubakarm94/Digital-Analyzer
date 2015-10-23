package com.main;

import com.connectedcomponents.ConnectedComponentsFinder;
import com.imagethreshold.ImageToGrayscale;

public class AnalyzeDigits {
	
	
	public static void main(String[] args){
		
		//convert the image to black and white
		ImageToGrayscale imgToG = new ImageToGrayscale("images/handwriting.png");
		
		//find the connnected components in the converted image
		ConnectedComponentsFinder testCCF = new ConnectedComponentsFinder(imgToG.getConvertedImage());
		
		//check images/connectedComponentsImags folder for each section
		testCCF.exportConnectedComponentsAsImages();
		
		
	}

}
