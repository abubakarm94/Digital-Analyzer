package com.main;



import java.io.File;

import com.uielements.UserBorderPane;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainGUI extends Application{

	private Stage primaryStage;
	private BorderPane rootLayout;
	
	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		this.primaryStage = stage;
		this.primaryStage.setTitle("Digit 2 Screen");
		
		rootLayout = new UserBorderPane();
		Scene scene = new Scene(rootLayout, 900, 600);
		

		this.primaryStage.setScene(scene);
		this.primaryStage.show();
	}
	
	public static void main(String[] args){
		launch(args);
	}

}