package com.uielements;

/*
 * This Class starts the user interaction
 */

import com.abstractClasses.AbstractViewOptionPane;
import com.enums.CanvasOrImageSelector;
import com.main.PreTrainedNeuralNetwork;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class UserBorderPane extends BorderPane implements EventHandler<KeyEvent> {

	private MenuBar menuBar = new MenuBar();

	private final Menu fileMenu = new Menu("File");

	private MenuItem fileNew = new MenuItem("New Digital Analysis (CTRL - N)");

	private final MenuItem fileExit = new MenuItem("Exit");

	private TabPane contentTab = new TabPane();


	/*
	 * Sets up the default configuration
	 */
	public UserBorderPane() {

		// sets up the appropriate functionality for the menu
		configureMenuBar();

		// When a certain key is pressed
		this.addEventHandler(KeyEvent.KEY_PRESSED, this);

		// adds the tabs to the left of the screen
		contentTab.setSide(Side.LEFT);

		// adds tab pane to the center
		this.setCenter(contentTab);


	}

	/*
	 * sets up the functionality of the menubar
	 */
	private void configureMenuBar() {

		fileMenu.getItems().addAll(fileNew, fileExit);

		menuBar.getMenus().addAll(fileMenu);

		this.setTop(menuBar);

		// sets up the user login
		fileNew.setOnAction(startNewAnalysis());

		// sets up the user exit
		fileExit.setOnAction(exitMenuClicked());

	}

	/*
	 * Start neural network
	 */
	private void trainNeuralNetwork(AbstractViewOptionPane tabInUse) {
		// TODO Auto-generated method stub

		PreTrainedNeuralNetwork network = PreTrainedNeuralNetwork.getInstance(tabInUse);

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

			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub

				showOpenAnalysisDialog();

			}
		};
	}

	/*
	 * Show the dialog to select new analysis view
	 */
	@SuppressWarnings("unchecked")
	private void showOpenAnalysisDialog() {
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

				CanvasOrImageSelector ciSelector = (CanvasOrImageSelector) optionSelector.getSelectionModel()
						.getSelectedItem();

				/*
				 * Instantiate new class
				 */
				AbstractViewOptionPane currentTab = null;

				try {
					currentTab = (AbstractViewOptionPane) ciSelector.getSelectedOption().newInstance();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (selectNetwork.isSelected()) {

					trainNeuralNetwork(currentTab);
				}

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

	/*
	 * Used in cases when keys are pressed
	 */
	@Override
	public void handle(KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.isControlDown() && event.getCode() == KeyCode.N) {
			System.out.println("dsd");
			showOpenAnalysisDialog();
		}
	}

}
