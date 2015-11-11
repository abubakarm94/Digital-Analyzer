package net.vivin;

import net.vivin.digit.DigitImage;
import net.vivin.digit.generator.DigitTrainingDataGenerator;
import net.vivin.neural.Backpropagator;
import net.vivin.neural.Layer;
import net.vivin.neural.NeuralNetwork;
import net.vivin.neural.Neuron;
import net.vivin.neural.activators.LinearActivationStrategy;
import net.vivin.neural.activators.SigmoidActivationStrategy;
import net.vivin.neural.generator.TrainingData;
import net.vivin.service.DigitImageLoadingService;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

/**
 * Created by IntelliJ IDEA.
 * User: vivin
 * Date: 11/11/11
 * Time: 10:02 AM
 */
public class DigitRecognizingNeuralNetwork {

    public static void main(String[] args) throws IOException {


    	ObjectInputStream in = new ObjectInputStream(new FileInputStream("DigitRecognizingNeuralNetwork.net"));
    	NeuralNetwork neuralNetwork=null;
        try {
			neuralNetwork = (NeuralNetwork) in.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        BufferedImage image=null;
        
       image= ImageIO.read(new File("images/connectedComponentsImages/usenum2.png"));
       int count=0;
       
       double[] imageData = new double[28*28];

       
       for(int y=0; y< 28; y++){
    	   for(int x=0; x < 28; x++){
    		   double data = image.getRGB(x, y);
    		   
    		   if(data != 0){
    			   data = 1;
    		   }
    		   imageData[count]= data;
    		   count++;
    	   }
       }
       
       neuralNetwork.setInputs(imageData);
       double[] output = neuralNetwork.getOutput();

       double first = 0;
       double second = 0;
       double third = 0;

       double firstConfidence = output[0];
       double secondConfidence = output[0];
       double thirdConfidence = output[0];

       for(int j = 0; j < output.length; j++) {
           if(output[j] > firstConfidence) {
               thirdConfidence = secondConfidence;
               secondConfidence = firstConfidence;
               firstConfidence = output[j];

               third = second;
               second = first;
               first = j;
           }

           else if(output[j] > secondConfidence) {
               thirdConfidence = secondConfidence;
               secondConfidence = output[j];

               third = second;
               second = j;
           }

           else if(output[j] > thirdConfidence) {
               thirdConfidence = output[j];
               third = j;
           }
       }
       
       System.out.println(""+first+", "+second+", "+third);
        
    }
}
