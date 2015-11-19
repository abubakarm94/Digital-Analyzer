package com.uielements;

import java.io.File;

import javax.imageio.ImageWriter;

import com.enums.CanvasOrImageSelector;
import com.main.PreTrainedNeuralNetwork;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class UserBorderPane extends BorderPane {

	private MenuBar menuBar = new MenuBar();

	private final Menu fileMenu = new Menu("File");
	private final Menu featuresMenu = new Menu("Features");

	private MenuItem fileNew = new MenuItem("New Digital Analysis");
	private final MenuItem fileExit = new MenuItem("Exit");

	private TabPane contentTab = new TabPane();

	/*
	 * Sets up the default configuration
	 */
	public UserBorderPane() {

		// sets up the appropriate functionality for the menu
		configureMenuBar();

		contentTab.setSide(Side.LEFT);
		


		this.setCenter(contentTab);

	}

	/*
	 * sets up the functionality of the menubar
	 */
	private void configureMenuBar() {

		fileMenu.getItems().addAll(fileNew, fileExit);

		menuBar.getMenus().addAll(fileMenu, featuresMenu);

		this.setTop(menuBar);

		// sets up the user login
		fileNew.setOnAction(startNewAnalysis());

		// sets up the user exit
		fileExit.setOnAction(exitMenuClicked());



	}

	private void trainNeuralNetwork() {
		// TODO Auto-generated method stub

		PreTrainedNeuralNetwork network = PreTrainedNeuralNetwork.getInstance();

	}

	/*
	 * Exit the application
	 */
	private EventHandler<ActionEvent> exitMenuClicked() {
		// TODO Auto-generated method stub
		return new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				System.exit(0);
			}

		};
	}

	/*
	 * Sets up the functionality for selecting new image to analyze
	 */
	private EventHandler<ActionEvent> startNewAnalysis() {
		// TODO Auto-generated method stub
		return new EventHandler<ActionEvent>() {

			@SuppressWarnings("unchecked")
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub

				Stage parentStage = new Stage();

				ObservableList<CanvasOrImageSelector> options = FXCollections
						.observableArrayList(CanvasOrImageSelector.values());

				final ComboBox optionSelector = new ComboBox(options);
				optionSelector.setValue(options.get(0));

				final Button submitButton = new Button("Continue");

				final CheckBox selectNetwork = new CheckBox("Use Trained Neural Network");
				selectNetwork.setSelected(true);

				submitButton.setOnAction(new EventHandler() {

					@Override
					public void handle(Event arg0) {
						// TODO Auto-generated method stub
						if (selectNetwork.isSelected()) {

							trainNeuralNetwork();
						}

						CanvasOrImageSelector ciSelector = (CanvasOrImageSelector) optionSelector.getSelectionModel()
								.getSelectedItem();
						Tab currentTab = ciSelector.getSelectedOption();
						
						currentTab.setText(ciSelector.getValue());

						contentTab.getTabs().addAll(currentTab);
						contentTab.getSelectionModel().select(currentTab);
						parentStage.close();

					}

				});

				VBox contentLayout = new VBox(8);
				contentLayout.setAlignment(Pos.CENTER);
				contentLayout.setPadding(new Insets(20, 20, 20, 20));
				contentLayout.getChildren().addAll(optionSelector, selectNetwork, submitButton);

				Scene secondScene = new Scene(contentLayout);

				parentStage.setTitle("Select Analysis Mode");
				parentStage.setScene(secondScene);

				parentStage.show();

			}
		};
	}

}
