package com.main;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import net.vivin.neural.NeuralNetwork;

public class LaunchPreTrainedNeuralNetwork {
	
	private final int imageSize = 28;
	
	private NeuralNetwork neuralNetwork;
	
	public LaunchPreTrainedNeuralNetwork() throws  IOException, ClassNotFoundException{
		
	  	ObjectInputStream in = new ObjectInputStream(new FileInputStream("network2.net"));
	  	
    	neuralNetwork =neuralNetwork = (NeuralNetwork) in.readObject();
    
			
		
	}
	
	public double[] convertImageToArray(BufferedImage image){
		double[] imageData= new double[imageSize*imageSize];
		int count =0;
		String hello = "";
	    for(int y=0; y< imageSize; y++){
	    	   for(int x=0; x < imageSize; x++){
	    		   double data = image.getRGB(x, y);
	    		   
	    		   if(data != 0.0){
	    			   data = 255;
	    		   }
	    		   hello +=data+", ";
	    		   imageData[count]= data;
	    		   count++;
	    	   }
	       }
	    System.out.println(hello);
	    return imageData;
		
	}
	
	public void setInputs(double[] data){
		neuralNetwork.setInputs(data);
	}
	
	public int[] getResults(){
		int[] finalResult = new int[3];
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
	       
	       finalResult[0]= (int)first;
	       finalResult[1]= (int)second;
	       finalResult[2] = (int)third;
	       return finalResult;
	}
	

}
