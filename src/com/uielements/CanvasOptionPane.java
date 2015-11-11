package com.uielements;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

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
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class CanvasOptionPane extends Tab {

	// how far the drawing is recognized.
	private final int DRAWABLE_EXTENT = 10;

	private Canvas imageDisplay = new Canvas(381, 81);

	private final GraphicsContext graphicsContext = imageDisplay.getGraphicsContext2D();

	private Button recongizeWriting = new Button("Recongize Writing");
	private Button clearWriting = new Button("Clear");

	private HBox buttonContent = new HBox(8, recongizeWriting, clearWriting);

	private Label finalResultArea = new Label();

	private VBox imageContentDisplay = new VBox(8, imageDisplay, buttonContent, finalResultArea);

	private final BorderPane contentLayout = new BorderPane();

	private FindConnectedComponents findCC;

	private PreTrainedNeuralNetwork neuralNetwork;

	private SplitPane splitScreen = new SplitPane();

	private LogArea logArea = LogArea.getInstance();

	private WritableImage currentImage = null;

	public CanvasOptionPane() {

		setUpGraphics();

		this.setClosable(false);
		this.setContent(splitScreen);

		recongizeWriting.setOnAction(performAnalysisOnCanvas());
		clearWriting.setOnAction(clearCanvas());
	}

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

	private EventHandler<ActionEvent> performAnalysisOnCanvas() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		return new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				currentImage = imageDisplay.snapshot(null, currentImage);

				java.awt.Image tempImage = new ImageToGrayscale(SwingFXUtils.fromFXImage(currentImage, null)).getConvertedImage();

				findCC = new FindConnectedComponents(tempImage);

				resetDrawingCanvas();
				graphicsContext.drawImage(SwingFXUtils.toFXImage(findCC.getProcessedImage(), null), 0, 0);

				neuralNetwork = PreTrainedNeuralNetwork.getInstance();
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

	private void setUpGraphics() {
		// TODO Auto-generated method stub
		imageContentDisplay.setPadding(new Insets(10, 10, 10, 10));
		imageContentDisplay.setAlignment(Pos.CENTER);

		buttonContent.setAlignment(Pos.CENTER);

		contentLayout.setCenter(imageContentDisplay);

		splitScreen.getItems().addAll(contentLayout, logArea);
		splitScreen.setOrientation(Orientation.VERTICAL);

		finalResultArea.setFont(new Font(20));

		setUpCanvas(graphicsContext);

	}
	
	private void resetDrawingCanvas(){
		graphicsContext.clearRect(0, 0, imageDisplay.getWidth(),
				imageDisplay.getHeight() );
		graphicsContext.setFill(Color.WHITE);
		graphicsContext.fillRect(0, 0, imageDisplay.getWidth(), imageDisplay.getHeight());
	}

	private void setUpCanvas(GraphicsContext gc) {
		// TODO Auto-generated method stub
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
