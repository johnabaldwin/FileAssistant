package main.GUI;

import javafx.application.*;
import javafx.beans.Observable;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import main.IO.*;
import main.Language.*;
import main.Nodes.*;

import java.util.*;

public class Settings {

    @FXML
    private AnchorPane root;

    @FXML
    private SplitPane splitPane;

    @FXML
    private ToolBar toolBar;

    @FXML
    private Button closeWindow;

    @FXML
    private Button diffCheckSettings;

    @FXML
    private Button searchSettings;

    @FXML
    private Button spellcheckSettings;

    @FXML
    private GridPane diffCheckPane;

    @FXML
    private Spinner shinglesSpinner;

    private SpinnerValueFactory shinglesValueFactory;
    private int MIN_SHINGLE = 2;
    private int MAX_SHINGLE = 15;
    private int DEFAULT_SHINGLE = 2;

    @FXML
    private Spinner falsePositiveSpinner;

    private SpinnerValueFactory fpValueFactory;
    private double MIN_TOLERANCE = 0.1;
    private double MAX_TOLERANCE = 0.4;
    private double DEFAULT_TOLERANCE = 0.2;

    @FXML
    private Spinner scaleSpinner;

    private SpinnerValueFactory scaleValueFactory;
    private double MIN_SCALE = 0.05;
    private double MAX_SCALE = 0.4;
    private double DEFAULT_SCALE = 0.1;


    @FXML
    private VBox searchPane;

    @FXML
    private TextField searchHome;

    @FXML
    private ListView ignoreDirectories;

    private ArrayList<ListCell> cells;

//    @FXML
//    private GridPane spellcheckPane;

    private double x = 0.0, y = 0.0;

    @FXML
    public void initialize() {
        //Set splitPane view to have immutable size
        SplitPane.Divider divider = splitPane.getDividers().get(0);
        final double splitPos = divider.getPosition();
        divider.positionProperty().addListener((observable, oldValue, newValue)
                -> divider.setPosition(splitPos));

        diffCheckPane.setVisible(true);
        searchPane.setVisible(false);
        //spellcheckPane.setVisible(false);

        shinglesValueFactory = new SpinnerValueFactory
                .IntegerSpinnerValueFactory(MIN_SHINGLE, MAX_SHINGLE, DEFAULT_SHINGLE);
        shinglesSpinner.setValueFactory(shinglesValueFactory);
        shinglesSpinner.valueProperty().addListener(listener -> {
            setShinglesValue();
        });
        shinglesSpinner.getEditor().setAlignment(Pos.CENTER);

        fpValueFactory = new SpinnerValueFactory
                .DoubleSpinnerValueFactory(MIN_TOLERANCE, MAX_TOLERANCE, DEFAULT_TOLERANCE, 0.01);
        falsePositiveSpinner.setValueFactory(fpValueFactory);
        falsePositiveSpinner.valueProperty().addListener(listener -> {
            setFalsePositiveValue();
        });

        scaleValueFactory = new SpinnerValueFactory
                .DoubleSpinnerValueFactory(MIN_SCALE, MAX_SCALE, DEFAULT_SCALE, 0.01);
        scaleSpinner.setValueFactory(scaleValueFactory);
        scaleSpinner.valueProperty().addListener(listener -> {
            setScaleValue();
        });


        searchHome.getStyleClass().add("custom");
        searchHome.setText(Search.getHome());

        createListView();
    }

    private void createListView() {
        ObservableList<EditableCell> cells = FXCollections.observableArrayList();
        TopListCell top = new TopListCell();
        top.updateListView(ignoreDirectories);

        cells.add(top);
        ignoreDirectories.setItems(cells);
        ignoreDirectories.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        Runnable run = () -> {
            MultipleSelectionModel selectionModel = ignoreDirectories.getSelectionModel();
            if (selectionModel.getSelectedIndex() == 0)
                selectionModel.clearSelection(TopListCell.LIST_POSITION);
        };
//        ignoreDirectories.getSelectionModel().getSelectedItems().addListener((ListChangeListener) change -> {
//            Platform.runLater(run);
//        });

    }

    private void setScaleValue() {
        double value = (double) scaleSpinner.getValue();
        if (Double.compare(value, MIN_SCALE) > -1 && Double.compare(value, MAX_SCALE) < 1) {
            DiffCheck.setScalingFactor(value);
        }
    }

    private void setFalsePositiveValue() {
        double value = (double) falsePositiveSpinner.getValue();
        if (Double.compare(value, MIN_TOLERANCE) > -1 && Double.compare(value, MAX_TOLERANCE) < 1) {
            DiffCheck.setFalsePositiveTolerance(value);
        }
    }

    @FXML
    void diffCheckPaneSwitch(ActionEvent event) {
        searchPane.setVisible(false);
        //spellcheckPane.setVisible(false);
        diffCheckPane.setVisible(true);
    }

    @FXML
    void searchPaneSwitch(ActionEvent event) {
        searchPane.setVisible(true);
        //spellcheckPane.setVisible(false);
        diffCheckPane.setVisible(false);
    }

    @FXML
    void spellCheckPaneSwitch(ActionEvent event) {
        searchPane.setVisible(false);
        //spellcheckPane.setVisible(true);
        diffCheckPane.setVisible(false);
    }

    @FXML
    void closeWindow(ActionEvent event) {
        Stage window = (Stage) closeWindow.getScene().getWindow();
        window.close();
    }

    @FXML
    void getScenePosition(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    @FXML
    void setScenePosition(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

    void setShinglesValue() {
        int value = (int) shinglesSpinner.getValue();
        if (value >= 2 && value <= 15)
            DiffCheck.setSHINGLES(value);
    }
}
