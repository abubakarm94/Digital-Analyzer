package com.uielements;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.abstractClasses.AbstractViewOptionPane;
import com.connectedcomponents.FindConnectedComponents;
import com.enums.LogAreaEnums;
import com.imagethreshold.ImageToGrayscale;
import com.main.PreTrainedNeuralNetwork;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class ImageOptionPane extends AbstractViewOptionPane {

	private ImageView imageDisplay = new ImageView();

	private Button chooseNew = new Button("Choose New Image");
	private Button analyzeImage = new Button("Analyze Image");

	private HBox buttonContent = new HBox(8, chooseNew, analyzeImage);

	private VBox imageContentDisplay = new VBox(8, imageDisplay, buttonContent, finalResultArea);

	private Image currentImage;
	
	private final int finalWidth = 381;
	private final int finalHeight = 81;

	public ImageOptionPane() {

		setUpGraphics();

		chooseNew.setOnAction(selectNewImage());
		analyzeImage.setOnAction(startAnalyzesOnImage());
	}

	private EventHandler<ActionEvent> startAnalyzesOnImage() {
		// TODO Auto-generated method stub
		return new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				// First we need to convert the image to back and white
				currentImage = new ImageToGrayscale(ImageOptionPane.this,(BufferedImage) currentImage).getConvertedImage();

				findCC = new FindConnectedComponents(ImageOptionPane.this, currentImage);

				imageDisplay.setImage(SwingFXUtils.toFXImage(findCC.getProcessedImage(), null));

				neuralNetwork = PreTrainedNeuralNetwork.getInstance(ImageOptionPane.this);
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

	public void printResult(int[] data) {
		System.out.println(data[0] + ", " + data[1] + ", " + data[2]);
	}

	public void setUpGraphics() {
		imageContentDisplay.setPadding(new Insets(10, 10, 10, 10));
		imageContentDisplay.setAlignment(Pos.CENTER);

		buttonContent.setAlignment(Pos.CENTER);

		contentLayout.setCenter(imageContentDisplay);

		splitScreen.getItems().addAll(contentLayout, logArea);
		splitScreen.setOrientation(Orientation.VERTICAL);

		analyzeImage.setDisable(true);

		finalResultArea.setFont(new Font(20));
	}

	private EventHandler<ActionEvent> selectNewImage() {
		// TODO Auto-generated method stub
		return new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				ArrayList<String> imageFilters = new ArrayList<String>();
				imageFilters.add("*.png");
				imageFilters.add("*.jpg");

				ExtensionFilter filter = new ExtensionFilter("Image Files", imageFilters);

				FileChooser chooseFile = new FileChooser();
				chooseFile.getExtensionFilters().add(filter);

				chooseFile.setTitle("Select File For Analysis");
				File fileForAnalysis = chooseFile.showOpenDialog(getTabPane().getScene().getWindow());

				try {
					currentImage = resizeImage(ImageIO.read(fileForAnalysis));
					javafx.scene.image.Image temp = SwingFXUtils.toFXImage(resizeImage(ImageIO.read(fileForAnalysis)), null);
					imageDisplay.setImage(temp);
					analyzeImage.setDisable(false);
					logArea.append(LogAreaEnums.ImageReadyForAnalysis);
				} catch (IOException e) {
					// TODO Auto-generated catch block

					logArea.append(LogAreaEnums.ImageNotReadyForAnalysis);
				}

			}

		};
	}

	/*
	 * Resize the image if either the height or the width is greater than the preferred size
	 */
	public BufferedImage resizeImage(BufferedImage image) {
		
		BufferedImage resizedImage  = null;
		if (image.getWidth() > finalWidth || image.getHeight() > finalHeight) {

			resizedImage = new BufferedImage(finalWidth, finalHeight, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(image, 0, 0, finalWidth, finalHeight, null);
			g.dispose();

		}else{
			return image;
		}

		return resizedImage;

	}

}
