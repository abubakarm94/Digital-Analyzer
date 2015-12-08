package com.main;
/*
 * The class will contain a pre-trained network along with necessary configuration
 */
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import com.abstractClasses.AbstractViewOptionPane;
import com.uielements.LogArea;

import net.vivin.neural.NeuralNetwork;

public class PreTrainedNeuralNetwork {

	private static PreTrainedNeuralNetwork instance = null;

	private NeuralNetwork neuralNetwork = null;

	private final int FINAL_IMAGE_SIZE = 28;

	private static LogArea logArea = null;

	public static PreTrainedNeuralNetwork getInstance(AbstractViewOptionPane view) {

		//gets the log area
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

		logArea.append(((int) ((firstConfidence) * 100)) + "% sure found element is " + (int) first
				+ ". But it could also be " + (int) second + " at " + ((int) (secondConfidence * 100)) + "%" + " OR "
				+ (int) third + " at " + ((int) (thirdConfidence * 100)) + "%");
		return ((int) first);

	}

	/*
	 * Converts a BufferedImage to an array of 0s (whites) and 255s (blacks)
	 */
	public double[] convertTo2DArray(BufferedImage image) {
		double[][] data = new double[20][20];
		for (int x = 0; x < data.length; x++) {
			for (int y = 0; y < data.length; y++) {
				double info = image.getRGB(y, x);

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
		double[][] padded = new double[FINAL_IMAGE_SIZE][FINAL_IMAGE_SIZE];

		for (int x = 0; x < FINAL_IMAGE_SIZE; x++) {
			for (int y = 0; y < FINAL_IMAGE_SIZE; y++) {

				if ((y - 4 == 0) && (x - 4 == 0)) {
					padded[x][y] = data[x - 4][y - 4];
				} else if (((y - 4 >= 0) && (y - 4 != 0)) && ((x - 4 >= 0) && (x - 4 != 0))) {
					if (y - 4 >= 20 || x - 4 >= 20) {
						padded[x][y] = 0;
					} else {
						padded[x][y] = data[x - 4][y - 4];
					}
				} else {
					padded[x][y] = 0;
				}

			}
		}

		return padded;
	}


	/*
	 * Converts a matrix to a single array
	 */
	private double[] imageDataToSingleArray(double[][] data) {

		double[] imageData = new double[FINAL_IMAGE_SIZE * FINAL_IMAGE_SIZE];

		int count = 0;
		for (int x = 0; x < FINAL_IMAGE_SIZE; x++) {
			for (int y = 0; y < FINAL_IMAGE_SIZE; y++) {

				imageData[count] = data[x][y];

				count++;
			}

		}

		return imageData;
	}

}
