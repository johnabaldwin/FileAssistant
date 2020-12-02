package main.GUI;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

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
    private ListView<?> matchList;

    @FXML
    private CheckBox spellCheck;

    @FXML
    private CheckBox diffCheck;

    @FXML
    private CheckBox replaceWords;

    @FXML
    private CheckBox fixWhiteSpace;

    @FXML
    private void initialize() {
        //Set enter key to perform search on current text
        fileInput.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                performSearch(fileInput.getText());
            }
        });

        //Set splitpane view to have immutable size
        SplitPane.Divider divider = splitPane.getDividers().get(0);
        final double splitPos = divider.getPosition();
        divider.positionProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldvalue, Number newvalue ) {
                divider.setPosition(splitPos);
            }
        });
    }

    private void performSearch(String file) {
        RadioButton toggled = (RadioButton) fileGroup.getSelectedToggle();
        String value = toggled.getId();
        if (value == findAll.getId()) {

        } else if (value == findFirst.getId()) {

        } else if (value == findGroup.getId()) {

        } else if (value == filePath.getId()) {

        }
    }

    public Controller() {

    }
}
