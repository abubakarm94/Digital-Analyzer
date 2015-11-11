package com.main;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;

import com.uielements.LogArea;

import net.vivin.neural.NeuralNetwork;

public class PreTrainedNeuralNetwork {

	static PreTrainedNeuralNetwork instance = null;

	private NeuralNetwork neuralNetwork = null;

	private final int imageSize = 28;

	private LogArea logArea = LogArea.getInstance();

	public static PreTrainedNeuralNetwork getInstance() {

		if (instance == null) {
			try {
				instance = new PreTrainedNeuralNetwork();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return instance;

	}

	public PreTrainedNeuralNetwork() throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream("DigitRecognizingNeuralNetwork.net"));

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

		logArea.append(((firstConfidence) * 100) + "% sure found element is " + (int) first + ". But it could also be "
				+ (int) second);
		return ((int) first);

	}

	public double[] convertTo2DArray(BufferedImage image) {
		double[][] data = new double[20][20];
		for (int y = 0; y < 20; y++) {
			for (int x = 0; x < 20; x++) {
				double info = image.getRGB(x, y);

				if (info != 0.0) {
					info = 255;
				}
				data[y][x] = info;
			}
		}
		return imageDataToSingleArray(convertToNeededSize(data));

	}

	/*
	 * Pads the 20 * 20 array to 28 * 28
	 */
	private double[][] convertToNeededSize(double[][] data) {
		double[][] padded = new double[imageSize][imageSize];

		for (int y = 0; y < imageSize; y++) {
			for (int x = 0; x < imageSize; x++) {

				if (y - 4 < 0) {
					padded[y][x] = 0;

				} else if ((y - 4 >= 0) && (x - 4 < 0)) {
					padded[y][x] = 0;

				} else {
					if ((!(y - 4 >= 20)) && (!(x - 4 >= 20))) {
						padded[y][x] = data[y - 4][x - 4];
					}else{
						padded[y][x] =0;
					}
				}

			}
		}

		return padded;
	}

	/*
	 * Converts a matrix to a single array
	 */
	private double[] imageDataToSingleArray(double[][] data) {
		double[] imageData = new double[imageSize * imageSize];

		int count = 0;
		for (int y = 0; y < imageSize; y++) {
			for (int x = 0; x < imageSize; x++) {
				imageData[count] = data[y][x];
				count++;
			}
		}
		return imageData;
	}

	public double[] convertImageToArray(BufferedImage image) {
		double[] imageData = new double[imageSize * imageSize];
		int count = 0;
		String hello = "";
		for (int y = 0; y < imageSize; y++) {
			for (int x = 0; x < imageSize; x++) {
				double data = image.getRGB(x, y);

				if (data != 0.0) {
					data = 255;
				}

				hello += data + ", ";
				imageData[count] = data;
				count++;
			}
		}
		// System.out.println(hello);
		return imageData;

	}

}
