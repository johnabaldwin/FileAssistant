package main.Nodes;

import javafx.beans.property.*;
import javafx.collections.*;
import javafx.css.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class TopListCell extends EditableCell {
    private HBox content = new HBox();
    private Label title = new Label("Ignored Directories");
    private Button add = new Button("+");
    private Button remove = new Button("-");

    public static final int LIST_POSITION = 0;

    public BooleanProperty selected = new BooleanPropertyBase(false) {
        public void invalidated() {
            pseudoClassStateChanged(PSEUDO_CLASS_SELECTED, get());
        }

        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "TopListCell";
        }
    };

    private static final PseudoClass PSEUDO_CLASS_SELECTED = PseudoClass.getPseudoClass("selected");


    public TopListCell() {
        title.setMinSize(102, 15.33333);

        add.setOnAction(value -> {
            createNewCell();
        });
        remove.setOnAction(value -> {
            removeCell();
        });

        addEventHandler(SelectionEvent.SELECTION_EVENT, new EventHandler<CustomEvent>() {
            @Override
            public void handle(CustomEvent selectionEvent) {
                pseudoClassStateChanged(PSEUDO_CLASS_SELECTED, true);
            }
        });
        content.getChildren().addAll(title, add, remove);
        content.setSpacing(10);
        content.setAlignment(Pos.CENTER);

        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        setGraphic(content);
        setAlignment(Pos.CENTER);
    }

    private void createNewCell() {
        ListView lv = getListView();
        EditableCell cell = new EditableCell();
        ObservableList list = lv.getItems();
        list.add(cell);
        lv.setItems(list);
        System.out.println("done");
    }

    private void removeCell() {
        try {
            ListView lv = getListView();
            ListCell selected = (ListCell) lv.getSelectionModel().getSelectedItem();
            ObservableList<EditableCell> list = getListView().getItems();
            if (!(selected instanceof TopListCell)) {
                list.remove(selected);
                lv.setItems(list);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public String toString() {
        return "TopListCell{" +
                ", title=" + title +
                ", " + add.hashCode() +
                '}';
    }

    public void setSelected(boolean s) {
        selected.set(s);
    }

    public boolean isTopListCell() {
        return selected.get();
    }

    public BooleanProperty topListCellProperty() {
        return selected;
    }
}
