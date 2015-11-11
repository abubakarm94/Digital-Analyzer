package com.uielements;

import java.awt.Image;
import java.awt.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

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

public class ImageOptionPane extends Tab {

	private ImageView imageDisplay = new ImageView();

	private Button chooseNew = new Button("Choose New Image");
	private Button analyzeImage = new Button("Analyze Image");

	private HBox buttonContent = new HBox(8,chooseNew, analyzeImage);
	
	private Label finalResultArea = new Label();

	private VBox imageContentDisplay = new VBox(8, imageDisplay, buttonContent, finalResultArea);

	private final BorderPane contentLayout = new BorderPane();

	private SplitPane splitScreen = new SplitPane();

	private LogArea logArea = LogArea.getInstance();

	private Image currentImage;
	
	private FindConnectedComponents findCC;
	
	private PreTrainedNeuralNetwork neuralNetwork;

	public ImageOptionPane() {

		setUpGraphics();

		this.setClosable(false);
		this.setContent(splitScreen);

		chooseNew.setOnAction(selectNewImage());
		analyzeImage.setOnAction(startAnalyzesOnImage());
	}

	private EventHandler<ActionEvent> startAnalyzesOnImage() {
		// TODO Auto-generated method stub
		return new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				//First we need to convert the image to back and white
				currentImage = new ImageToGrayscale((BufferedImage)currentImage).getConvertedImage();

				findCC = new FindConnectedComponents(currentImage);
				
				imageDisplay.setImage(SwingFXUtils.toFXImage(findCC.getProcessedImage(), null));
				
				
				neuralNetwork = PreTrainedNeuralNetwork.getInstance();
				ArrayList<BufferedImage> list = findCC.getConnectedComponentsAsList();
				String result="";
				for(BufferedImage bi: list){
					double[] data = neuralNetwork.convertTo2DArray(bi);
					result += neuralNetwork.processInput(data)+" ";
					
				}
		
				finalResultArea.setText("The result is: "+ result);
				
				logArea.append("The result is: "+result);
				
			}

		};
	}
	
	public void printResult(int[] data){
		System.out.println(data[0]+", "+data[1]+", "+ data[2]);
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
					currentImage = ImageIO.read(fileForAnalysis);
					javafx.scene.image.Image temp = SwingFXUtils.toFXImage(ImageIO.read(fileForAnalysis), null);
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

}
