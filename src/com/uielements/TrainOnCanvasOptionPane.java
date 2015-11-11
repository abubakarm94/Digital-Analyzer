package com.uielements;

import com.abstractClasses.AbstractViewOptionPane;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class TrainOnCanvasOptionPane extends AbstractViewOptionPane{
	
	private final int CONTENT_SIZE = 80;
	private final int FONT_SIZE = 20;

	
	private Canvas imageDisplay = new Canvas(CONTENT_SIZE,CONTENT_SIZE);
	
	private Label equalitySign = new Label("=");
	
	private TextArea equalityTextArea = new TextArea();
	
	private HBox trainingViewContent = new HBox(8,imageDisplay, equalitySign,equalityTextArea);

	private final GraphicsContext graphicsContext = imageDisplay.getGraphicsContext2D();

	private Button trainWriting = new Button("Recongize Writing");
	private Button clearWriting = new Button("Clear");

	private HBox buttonContent = new HBox(8, trainWriting, clearWriting);

	private VBox imageContentDisplay = new VBox(8, trainingViewContent, buttonContent, finalResultArea);

	private WritableImage currentImage = null;
	
	public TrainOnCanvasOptionPane(){
		
		setUpGraphics();
		
	}
	
	public void setUpGraphics(){
		imageContentDisplay.setPadding(new Insets(10, 10, 10, 10));
		imageContentDisplay.setAlignment(Pos.CENTER);

		trainingViewContent.setAlignment(Pos.CENTER);

		buttonContent.setAlignment(Pos.CENTER);

		contentLayout.setCenter(imageContentDisplay);

		splitScreen.getItems().addAll(contentLayout, logArea);
		splitScreen.setOrientation(Orientation.VERTICAL);

		finalResultArea.setFont(new Font(FONT_SIZE));
		equalitySign.setFont(new Font(FONT_SIZE));
		
		equalityTextArea.setFont(new Font(FONT_SIZE));
		equalityTextArea.setPrefSize(CONTENT_SIZE, CONTENT_SIZE);

		setUpCanvas(graphicsContext);
	}
	
	public void setUpCanvas(GraphicsContext gc){
		double canvasWidth = gc.getCanvas().getWidth();
		double canvasHeight = gc.getCanvas().getHeight();

		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, imageDisplay.getWidth(), imageDisplay.getHeight());
		gc.setStroke(Color.BLACK);

		gc.setLineWidth(4);

		imageDisplay.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				graphicsContext.beginPath();
				graphicsContext.moveTo(event.getX(), event.getY());
				graphicsContext.stroke();
			}
		});

		imageDisplay.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				graphicsContext.lineTo(event.getX(), event.getY());
				graphicsContext.stroke();
			}
		});

		imageDisplay.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

			}
		});

	}

}
