package com.uielements;

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

public class LogArea extends VBox{
	
	private ListView<String> logContent = new ListView();
	
	private ScrollPane scrollPane = new ScrollPane();
	
	
	
	public LogArea(){
			
		
		
		
		this.getChildren().addAll(logContent);

		
		
	}
	
	public void append(String newContent){
		logContent.getItems().add(newContent);
	
		
	}

	public void append(LogAreaEnums data) {
		// TODO Auto-generated method stub
		logContent.getItems().add(data.toString());
		
		
	}
			
	
	

}
