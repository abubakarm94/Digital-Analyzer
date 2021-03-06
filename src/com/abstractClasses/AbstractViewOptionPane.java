/*
 * This abstract acts the super class for training arenas
 * SplitView that contain the training area and a log area (where important info are displayed)
 */

package com.abstractClasses;

import com.connectedcomponents.FindConnectedComponents;
import com.main.PreTrainedNeuralNetwork;
import com.uielements.LogArea;

import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;



public abstract class AbstractViewOptionPane extends Tab{
	
	//set up the required variables
	protected SplitPane splitScreen = new SplitPane();
	protected LogArea logArea = new LogArea();
	protected final BorderPane contentLayout = new BorderPane();
	protected FindConnectedComponents findCC;
	protected PreTrainedNeuralNetwork neuralNetwork;
	protected Label finalResultArea = new Label();

	
	public AbstractViewOptionPane(){
		
		this.setContent(splitScreen);
	}
	
	//returns the Log Area 
	public LogArea getLogArea(){
		return logArea;
	}
	


}
