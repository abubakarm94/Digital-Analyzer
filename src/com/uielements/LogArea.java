package com.uielements;

/*
 * This class allows logs to be added to a listview
 */
import com.enums.LogAreaEnums;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;

public class LogArea extends VBox {

	private ListView<String> logContent = new ListView<>();

	public LogArea() {

		this.getChildren().addAll(logContent);

	}
	
	/*
	 * Append string to the list view
	 */
	public void append(Object newContent) {
		if (newContent instanceof String) {
			logContent.getItems().add((String) newContent);
		}else{
			logContent.getItems().add(((LogAreaEnums)newContent).toString());
		}

	}


}
