package main.Nodes;

import javafx.event.*;

public class SelectionEvent extends CustomEvent {

    public static final EventType<CustomEvent> SELECTION_EVENT = new EventType(CUSTOM_EVENT_TYPE, "Selection");

    public SelectionEvent() {
        super(SELECTION_EVENT);
    }

    @Override
    public void invokeHandler(EventHandler handler) {
    }
}
