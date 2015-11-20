package com.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;

import javax.imageio.ImageIO;

import com.abstractClasses.AbstractViewOptionPane;
import com.connectedcomponents.Pixel;
import com.uielements.LogArea;

import net.vivin.neural.NeuralNetwork;

public class PreTrainedNeuralNetwork {

	static PreTrainedNeuralNetwork instance = null;

	private NeuralNetwork neuralNetwork = null;

	private final int imageSize = 28;

	private static LogArea logArea = null;

	public static PreTrainedNeuralNetwork getInstance(AbstractViewOptionPane view) {

		logArea = view.getLogArea();
		
		if (instance == null) {
			try {
				instance = new PreTrainedNeuralNetwork();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
				System.out.println("Unable To Load Trained Neural Network");
				System.exit(0);
			}
		}

		return instance;

	}

	public PreTrainedNeuralNetwork() throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream("TrainedNeuralNetworkToBeUsed.net"));

		neuralNetwork = neuralNetwork = (NeuralNetwork) in.readObject();
	}

	/*
	 * takes an input an returns an array of 3 possible output
	 */
	public int processInput(double[] data) {
		neuralNetwork.setInputs(data);

		int[] finalResult = new int[3];
		double[] output = neuralNetwork.getOutput();

		double first = 0;
		double second = 0;
		double third = 0;

		double firstConfidence = output[0];
		double secondConfidence = output[0];
		double thirdConfidence = output[0];
		
		

		for (int j = 0; j < output.length; j++) {

			if (output[j] > firstConfidence) {
				thirdConfidence = secondConfidence;
				secondConfidence = firstConfidence;
				firstConfidence = output[j];

				third = second;
				second = first;
				first = j;
			}

			else if (output[j] > secondConfidence) {
				thirdConfidence = secondConfidence;
				secondConfidence = output[j];

				third = second;
				second = j;
			}

			else if (output[j] > thirdConfidence) {
				thirdConfidence = output[j];
				third = j;
			}
		}

		finalResult[0] = (int) first;
		finalResult[1] = (int) second;
		finalResult[2] = (int) third;

		logArea.append(((int)((firstConfidence) * 100)) + "% sure found element is " + (int) first + ". But it could also be "
				+ (int) second +" at " +((int)(secondConfidence  * 100)) +"%" +" OR "+ (int) third +" at " +((int)(thirdConfidence  * 100)) +"%");
		return ((int) first);

	}

	/*
	 * Converts a BufferedImage to an array of 0s (whites) and 255s (blacks)
	 */
	public double[] convertTo2DArray(BufferedImage image) {
		double[][] data = new double[20][20];
		for (int x = 0; x < data.length; x++) {
			for (int y = 0; y < data.length; y++) {
				double info = image.getRGB(y,x);
				
				if (info != 0.0) {
					info = 255;
				}
				data[x][y] = info;
			}
		}
		
		return (imageDataToSingleArray(convertToNeededSize(data)));

	}

	/*
	 * Pads the 20 * 20 array to 28 * 28
	 */
	private double[][] convertToNeededSize(double[][] data) {
		double[][] padded = new double[imageSize][imageSize];

		for (int x = 0; x < imageSize; x++) {
			for (int y = 0; y < imageSize; y++) {

				if((y - 4 == 0) && (x - 4 == 0)) {
					padded[x][y] = data[x-4][y-4];
				} 
				else if(((y-4 >= 0) && (y-4 != 0)) && ((x-4 >= 0) && (x-4 != 0)) ){
					if(y-4 >= 20 || x-4 >= 20){
						padded[x][y] = 0;
					}else{
					padded[x][y] = data[x-4][y-4];
					}
				}else{
					padded[x][y] =0;
				}

			}
		}

		return padded;
	}


	
	  int whatCount =0;
	/*
	 * Converts a matrix to a single array
	 */
	private double[] imageDataToSingleArray(double[][] data) {
BufferedImage bmp = new BufferedImage(28, 28, BufferedImage.TYPE_INT_ARGB);

		
		double[] imageData = new double[imageSize * imageSize];

		int count = 0;
		for (int x = 0; x < imageSize; x++) {
			for (int y = 0; y < imageSize; y++) {
				
				if(data[x][y] == 255){
				bmp.setRGB(y, x, Color.black.getRGB());
				}else{
					bmp.setRGB(y, x, Color.white.getRGB());

				}
				
				
				
				imageData[count] = data[x][y];
				
				count++;
			}

			
		}
		try {
			ImageIO.write(bmp, "png", new File("images/connectedComponentsImages/tempit"+whatCount+".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		whatCount++;

		return imageData;
	}
	
	/*
	 * might consider normalizing from grayscale to black and white
	 */
	 private double[] normalizeToBlackWhite(double[] data) {
	    	
	        int[] histogram = new int[256];

	        for(double datum : data) {
	            histogram[(int) datum]++;
	        }

	        double sum = 0;
	        for(int i = 0; i < histogram.length; i++) {
	            sum += i * histogram[i];
	        }

	        double sumB = 0;
	        int wB = 0;
	        int wF = 0;

	        double maxVariance = 0;
	        int threshold = 0;

	        int i = 0;
	        boolean found = false;

	        while(i < histogram.length && !found) {
	            wB += histogram[i];

	            if(wB != 0) {
	                wF = data.length - wB;

	                if(wF != 0) {
	                    sumB += (i * histogram[i]);

	                    double mB = sumB / wB;
	                    double mF = (sum - sumB) / wF;

	                    double varianceBetween = wB * Math.pow((mB - mF), 2);

	                    if(varianceBetween > maxVariance) {
	                        maxVariance = varianceBetween;
	                        threshold = i;
	                    }
	                }

	                else {
	                    found = true;
	                }
	            }

	            i++;
	        }



	        for(i = 0; i < data.length; i++) {
	            data[i] = data[i] <= threshold ? 0 : 1;
	            
	        }
	        
	        return data;
	    }
	 
	
	
	

}
