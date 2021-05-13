package main.GUI;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import main.IO.Search;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import main.Language.ActionController;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
 * GUI Controller with methods outlining actions to take on various user inputs
 */
public class Controller {

    @FXML
    private MenuBar menuBar;

    @FXML
    private Menu fileMenu;

    @FXML
    private Menu settingsMenu;

    @FXML
    private MenuItem diffCheckSettings;

    @FXML
    private Menu helpMenu;

    //Text input for file name or path
    @FXML
    private TextField fileInput;

    //Toggle group for search method radio buttons
    @FXML
    private ToggleGroup fileGroup;

    //Radio button for {@code Search.findFirst} search
    @FXML
    private RadioButton findFirst;

    //Radio button for {@code Search.findFile} search
    @FXML
    private RadioButton findAll;

    //Radio button for {@code Search.findFileSet} search
    @FXML
    private RadioButton findGroup;

    //Radio button indicating {@code fileInput} is a file path
    @FXML
    private RadioButton filePath;

    //Enter button for {@code fileInput} to begin search
    @FXML
    private Button searchButton;

    //List of files matching the input to be shown in the list view
    @FXML
    private ListView<String> matchList;

    //Check box indicating to perform spell check
    @FXML
    private CheckBox spellCheck;

    //Check box indicating to perform diff check
    @FXML
    private CheckBox diffCheck;

    //Check box indicating to perform word replacement
    @FXML
    private CheckBox replaceWords;

    //Check box indicating to perform whitespace fix
    @FXML
    private CheckBox fixWhiteSpace;

    //Start button for check box actions
    @FXML
    private Button startButton;

    //Text area for replacement words for {@code replaceWords}
    @FXML
    private TextArea replacementWords;

    //Text area for new words for {@code replaceWords}
    @FXML
    private TextArea findWords;

    //In search boolean to prevent repeated search restart
    private boolean inSearch = false;

    //In actions boolean to prevent repeated action restart
    private boolean inAction = false;

    /**
     * Initializer for all key presses
     */
    @FXML
    private void initialize() {
        //Set enter key to perform search on current text
        fileInput.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                try {
                    performSearch(fileInput.getText());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //Set search button as replacement to enter key
        searchButton.setOnAction(value ->  {
            try {
                if (!inSearch) {
                    inSearch = true;
                    performSearch(fileInput.getText());
                    inSearch = false;
                    fileGroup.getSelectedToggle().setSelected(false);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //Enable selecting multiple files
        matchList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        //Only enable diff checking when 2 files are selected
        matchList.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change<? extends String> c) -> {
            if (matchList.getSelectionModel().getSelectedItems().size() == 2) {
                diffCheck.setDisable(false);
            } else {
                diffCheck.setSelected(false);
                diffCheck.setDisable(true);
            }
        });

        //Start performing diff check, spell check, white space fix, and word replacement
        startButton.setOnAction(value -> {
            try {
                if (!inAction) {
                    inAction = true;
                    startButton.setDisable(true);
                    performActions();
                    inAction = false;
                    startButton.setDisable(false);
                }
            } catch (IOException e) {
                System.err.println("IO Exception thrown: 101");
            }
        });

        //If replaceWords is selected make findWords and replacementWords visible
        replaceWords.setOnAction(value -> {
            if (replaceWords.isSelected()) {
                findWords.setDisable(false);
                replacementWords.setDisable(false);
            } else {
                findWords.setDisable(true);
                replacementWords.setDisable(true);
            }
        });

        diffCheckSettings.setOnAction(value -> {
            String name = "Settings";
            String fxmlPath = "Settings.fxml";
            try {
                newWindow(name, fxmlPath);
            } catch (IOException e) {
                System.err.println(e);
            }
        });
    }

    private double x, y;

    private void newWindow(String name, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        //loader.setLocation(getClass().getResource(fxmlPath));
        //FileInputStream fxmlStream = new FileInputStream(fxmlPath);
        AnchorPane root = loader.load();

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(name);
        stage.initStyle(StageStyle.UNDECORATED);

        stage.show();
    }

    /**
     * Takes the file manipulation selections of the user and executes them
     * @throws IOException - this method deals with many functions that are meant exclusively to
     *                          manipulate files
     */
    private void performActions() throws IOException {
        ObservableList<String> selected = matchList.getSelectionModel().getSelectedItems();
        if (spellCheck.isSelected()) {
            for (String s : selected) {
                ActionController.spellCheck(s);
            }
            spellCheck.setSelected(false);
        }
        if (replaceWords.isSelected()) {
            String[] findList = findWords.getText().split("\n");
            String[] replaceList = replacementWords.getText().split("\n");
            if (findList.length == replaceList.length) {
                HashMap<String, String> findReplace = (HashMap<String, String>) IntStream.range(0, findList.length)
                        .boxed().collect(Collectors.toMap(i -> findList[i], i -> replaceList[i]));
                for (String s : selected) {
                    ActionController.replaceWords(s, findReplace);
                }
            }
            replaceWords.setSelected(false);
        }
        if (fixWhiteSpace.isSelected()) {
            for (String s : selected) {
                ActionController.fixWhiteSpace(s);
            }
            fixWhiteSpace.setSelected(false);
        }
        if (diffCheck.isSelected()) {
            String[] paths = {selected.get(0), selected.get(1)};
            ActionController.diffCheck(paths);
            diffCheck.setSelected(false);
        }
    }

    /**
     * Takes input of a file name or path to search for in the users file system based on
     * their selected search method and adds it to the listview for users to select the
     * desired files
     * @param file - file name to perform search on
     * @throws IOException - searching a file system is likely to throw a file not found exception
     */
    private void performSearch(String file) throws IOException {
        RadioButton toggled = (RadioButton) fileGroup.getSelectedToggle();
        ObservableList<String> files;
        if (toggled != null && toggled.getId().equals(findAll.getId())) {
            Search.findSingleFile(file);
            files = FXCollections.observableList(Search.getResults());
            matchList.setItems(files);
        } else if (toggled != null && toggled.getId().equals(findFirst.getId())) {
            Search.findFirst(file);
            files = FXCollections.observableList(Search.getResults());
            matchList.setItems(files);
        } else if (toggled != null && toggled.getId().equals(findGroup.getId())) {
            ArrayList<String> temp = (ArrayList<String>)Arrays.asList(file.split(", "));
            Search.findFileSet(temp);
            files = FXCollections.observableList(Search.getResults());
            matchList.setItems(files);
        } else if (toggled != null && toggled.getId().equals(filePath.getId())) {
            ArrayList<String> temp = new ArrayList<>();
            temp.add(file);
            files = FXCollections.observableList(temp);
            File test = new File(file);
            if (test.exists())
                matchList.setItems(files);
        }

    }
}
