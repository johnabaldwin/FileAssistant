package main.Nodes;

import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.input.*;

public class EditableCell extends ListCell {

    private TextField filePath = new TextField("test");
    private Label pathLabel = new Label();

    public EditableCell() {
        filePath.setMinSize(150, 23.333333);
        pathLabel.setMinSize(150, 23.33333);
        filePath.setOnKeyReleased(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                onEnter();
            }
        });
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        setGraphic(filePath);
    }

    private void onEnter() {
        pathLabel.setText(filePath.getText());
        setGraphic(pathLabel);
    }

    @Override
    public String toString() {
        return "EditableCell{" +
                "filePath=" + filePath.getText() +
                ", " + filePath.hashCode() +
                ", " + pathLabel.isVisible() +
                '}';
    }
}
