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
	
	protected SplitPane splitScreen = new SplitPane();
	
	protected LogArea logArea = LogArea.getInstance();
	
	protected final BorderPane contentLayout = new BorderPane();

	protected FindConnectedComponents findCC;

	protected PreTrainedNeuralNetwork neuralNetwork;
	
	protected Label finalResultArea = new Label();

	
	public AbstractViewOptionPane(){

		this.setClosable(false);
		this.setContent(splitScreen);
	}
	


}
