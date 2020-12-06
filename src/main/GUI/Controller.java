package main.GUI;

import javafx.collections.ListChangeListener;
import main.IO.Search;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import main.Language.ActionController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller {

    @FXML
    private SplitPane splitPane;

    @FXML
    private TextField fileInput;

    @FXML
    private RadioButton findFirst;

    @FXML
    private ToggleGroup fileGroup;

    @FXML
    private RadioButton findAll;

    @FXML
    private RadioButton findGroup;

    @FXML
    private RadioButton filePath;

    @FXML
    private Button searchButton;

    @FXML
    private ListView<String> matchList;

    @FXML
    private CheckBox spellCheck;

    @FXML
    private CheckBox diffCheck;

    @FXML
    private CheckBox replaceWords;

    @FXML
    private CheckBox fixWhiteSpace;

    @FXML
    private Button startButton;

    @FXML
    private TextArea replacementWords;

    @FXML
    private TextArea findWords;

    private boolean inSearch = false;

    private boolean inAction = false;

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

        //Set splitPane view to have immutable size
        SplitPane.Divider divider = splitPane.getDividers().get(0);
        final double splitPos = divider.getPosition();
        divider.positionProperty().addListener((observable, oldValue, newValue)
                -> divider.setPosition(splitPos));

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

        replaceWords.setOnAction(value -> {
            if (replaceWords.isSelected()) {
                findWords.setDisable(false);
                replacementWords.setDisable(false);
            } else {
                findWords.setDisable(true);
                replacementWords.setDisable(true);
            }
        });


    }

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
            ActionController.diffCheck(selected.get(0), selected.get(1));
            diffCheck.setSelected(false);
        }
    }

    @FXML
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

    public Controller() {
    }
}
