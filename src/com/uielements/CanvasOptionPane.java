package com.uielements;
/*
 * This class allows you to draw digits on screen that will be used for analysis
 */
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.abstractClasses.AbstractViewOptionPane;
import com.connectedcomponents.FindConnectedComponents;
import com.imagethreshold.ImageToGrayscale;
import com.main.PreTrainedNeuralNetwork;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class CanvasOptionPane extends AbstractViewOptionPane {

	private Canvas imageDisplay = new Canvas(381, 100);

	private final GraphicsContext graphicsContext = imageDisplay.getGraphicsContext2D();

	private Button recongizeWriting = new Button("Recongize Writing");
	private Button clearWriting = new Button("Clear");

	private HBox buttonContent = new HBox(8, recongizeWriting, clearWriting);

	private VBox imageContentDisplay = new VBox(8, imageDisplay, buttonContent, finalResultArea);

	private WritableImage currentImage = null;

	public CanvasOptionPane() {

		setUpGraphics();

		recongizeWriting.setOnAction(performAnalysisOnCanvas());
		clearWriting.setOnAction(clearCanvas());
		
	}

	/*
	 * Clears the drawing canvas
	 */
	private EventHandler<ActionEvent> clearCanvas() {
		// TODO Auto-generated method stub
		return new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				resetDrawingCanvas();
				logArea.append("Screen cleared");

			}
		};
	}

	/*
	 * Perform Digital analysis on canvas
	 */
	private EventHandler<ActionEvent> performAnalysisOnCanvas() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		return new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				currentImage = imageDisplay.snapshot(null, currentImage);
				

				java.awt.Image tempImage = new ImageToGrayscale(CanvasOptionPane.this,SwingFXUtils.fromFXImage(currentImage, null)).getConvertedImage();

				findCC = new FindConnectedComponents(CanvasOptionPane.this, tempImage);

				resetDrawingCanvas();
				
				graphicsContext.drawImage(SwingFXUtils.toFXImage(findCC.getProcessedImage(), null), 0, 0);

				neuralNetwork = PreTrainedNeuralNetwork.getInstance(CanvasOptionPane.this);
				ArrayList<BufferedImage> list = findCC.getConnectedComponentsAsList();
				String result = "";
				for (BufferedImage bi : list) {
					double[] data = neuralNetwork.convertTo2DArray(bi);
					result += neuralNetwork.processInput(data) + " ";

				}

				finalResultArea.setText("The result is: " + result);

				logArea.append("The result is: " + result);

			}
		};
	}

	/*
	 * Sets up the look of the view
	 */
	private void setUpGraphics() {
		// TODO Auto-generated method stub
		imageContentDisplay.setPadding(new Insets(10, 10, 10, 10));
		imageContentDisplay.setAlignment(Pos.CENTER);

		buttonContent.setAlignment(Pos.CENTER);

		contentLayout.setCenter(imageContentDisplay);

		splitScreen.getItems().addAll(contentLayout, logArea);
		splitScreen.setOrientation(Orientation.VERTICAL);

		finalResultArea.setFont(new Font(20));

		setUpCanvas();

	}
	
	/*
	 * Reset the drawing canvas
	 */
	private void resetDrawingCanvas(){
		graphicsContext.clearRect(0, 0, imageDisplay.getWidth(),
				imageDisplay.getHeight() );
		graphicsContext.setFill(Color.WHITE);
		graphicsContext.fillRect(0, 0, imageDisplay.getWidth(), imageDisplay.getHeight());
	}

	/*
	 *Set up canvas 
	 */
	private void setUpCanvas() {
		// TODO Auto-generated method stub
		double canvasWidth = graphicsContext.getCanvas().getWidth();
		double canvasHeight = graphicsContext.getCanvas().getHeight();

		graphicsContext.setFill(Color.WHITE);
		graphicsContext.fillRect(0, 0, imageDisplay.getWidth(), imageDisplay.getHeight());
		graphicsContext.setStroke(Color.BLACK);

		graphicsContext.setLineWidth(5);

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
