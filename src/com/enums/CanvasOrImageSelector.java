package com.enums;

import com.abstractClasses.AbstractViewOptionPane;
import com.uielements.CanvasOptionPane;
import com.uielements.ImageOptionPane;
import com.uielements.TrainOnCanvasOptionPane;

import javafx.scene.control.Tab;

public enum CanvasOrImageSelector {
	ImageSelector("Perform Analysis on an Image",  ImageOptionPane.class),
	CanvasSelector("Perform Analysis on a Canvas", CanvasOptionPane.class); 

	private String title;
	private Class selectedOption;

	CanvasOrImageSelector(String data, Class selectedClass) {
		title = data;
		selectedOption = selectedClass;

	}

	public String getValue() {
		return title;
	}

	public Class getSelectedOption() {
		return selectedOption;
	}

	@Override
	public String toString() {
		return this.getValue();
	}

}
