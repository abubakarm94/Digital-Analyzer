package net.vivin;

import java.io.IOException;

import net.vivin.digit.generator.DigitTrainingDataGenerator;
import net.vivin.neural.Backpropagator;
import net.vivin.neural.Layer;
import net.vivin.neural.NeuralNetwork;
import net.vivin.neural.Neuron;
import net.vivin.neural.activators.LinearActivationStrategy;
import net.vivin.neural.activators.SigmoidActivationStrategy;
import net.vivin.neural.generator.TrainingData;
import net.vivin.service.DigitImageLoadingService;

public class TrainNewNeuralNetwork {

	  public static void main(String[] args) throws IOException {

	        DigitImageLoadingService trainingService = new DigitImageLoadingService("training/train/trainLabels", "training/train/trainImages");
	        DigitImageLoadingService testService = new DigitImageLoadingService("training/test/testLabels", "training/test/testImages");

	        NeuralNetwork neuralNetwork = new NeuralNetwork("Digit Recognizing Neural Network");

	        Neuron inputBias = new Neuron(new LinearActivationStrategy());
	        inputBias.setOutput(1);

	        Layer inputLayer = new Layer(null, inputBias);

	        for(int i = 0; i < DigitImageLoadingService.ROWS * DigitImageLoadingService.COLUMNS; i++) {
	            Neuron neuron = new Neuron(new SigmoidActivationStrategy());
	            neuron.setOutput(0);
	            inputLayer.addNeuron(neuron);
	        }

	        Neuron hiddenBias = new Neuron(new LinearActivationStrategy());
	        hiddenBias.setOutput(1);

	        Layer hiddenLayer = new Layer(inputLayer, hiddenBias);

	        long numberOfHiddenNeurons = Math.round((2.0 / 3.0) * (DigitImageLoadingService.ROWS * DigitImageLoadingService.COLUMNS) + 10);

	        for(int i = 0; i < numberOfHiddenNeurons; i++) {
	            Neuron neuron = new Neuron(new SigmoidActivationStrategy());
	            neuron.setOutput(0);
	            hiddenLayer.addNeuron(neuron);
	        }

	        Layer outputLayer = new Layer(hiddenLayer);

	        //10 output neurons - 1 for each digit
	        for(int i = 0; i < 10; i++) {
	            Neuron neuron = new Neuron(new SigmoidActivationStrategy());
	            neuron.setOutput(0);
	            outputLayer.addNeuron(neuron);
	        }

	        neuralNetwork.addLayer(inputLayer);
	        neuralNetwork.addLayer(hiddenLayer);
	        neuralNetwork.addLayer(outputLayer);

	        DigitTrainingDataGenerator trainingDataGenerator = new DigitTrainingDataGenerator(trainingService.loadDigitImages());
	        Backpropagator backpropagator = new Backpropagator(neuralNetwork, 0.1, 0.9, 0);
	        backpropagator.train(trainingDataGenerator, 0.005);
	        neuralNetwork.persist();

	        DigitTrainingDataGenerator testDataGenerator = new DigitTrainingDataGenerator(testService.loadDigitImages());
	        TrainingData testData = testDataGenerator.getTrainingData();

	        for(int i = 0; i < testData.getInputs().length; i++) {
	            double[] input = testData.getInputs()[i];
	            double[] output = testData.getOutputs()[i];

	            int digit = 0;
	            boolean found = false;
	            while(digit < 10 && !found) {
	                found = (output[digit] == 1);
	                digit++;
	            }

	            neuralNetwork.setInputs(input);
	            double[] receivedOutput = neuralNetwork.getOutput();

	            double max = receivedOutput[0];
	            double recognizedDigit = 0;
	            for(int j = 0; j < receivedOutput.length; j++) {
	                if(receivedOutput[j] > max) {
	                    max = receivedOutput[j];
	                    recognizedDigit = j;
	                }
	            }

	            System.out.println("Recognized " + (digit - 1) + " as " + recognizedDigit + ". Corresponding output value was " + max);
	        }
	    }
	
}
