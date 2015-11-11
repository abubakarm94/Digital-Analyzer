package com.enums;

import com.uielements.CanvasOptionPane;
import com.uielements.ImageOptionPane;
import com.uielements.TrainOnCanvasOptionPane;

import javafx.scene.control.Tab;

public enum CanvasOrImageSelector {
	ImageSelector("Perform Analysis on an Image", new ImageOptionPane()),
	CanvasSelector("Perform Analysis on a Canvas",new CanvasOptionPane()), 
	CanvasTrainingSelector("Train Neural Network using Canvas", new TrainOnCanvasOptionPane());

	private String title;
	private Tab selectedOption;

	CanvasOrImageSelector(String data, Tab selectedClass) {
		title = data;
		selectedOption = selectedClass;

	}

	public String getValue() {
		return title;
	}

	public Tab getSelectedOption() {
		return selectedOption;
	}

	@Override
	public String toString() {
		return this.getValue();
	}

}
