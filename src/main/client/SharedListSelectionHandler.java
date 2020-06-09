package main.client;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.TreeSet;

class SharedListSelectionHandler implements ListSelectionListener {
    private ChatMessengerApp app;
    private ChatPanelView panel;

    public void valueChanged(ListSelectionEvent e) {
        ListSelectionModel lsm = (ListSelectionModel)e.getSource();


        if (lsm.isSelectionEmpty()) {

        } else {
            System.out.println("yeah");
            app.getModel().filterMessagesOfDialog();
            panel.getMessagesTextPane().setText(app.getModel().dialogMessagesToString());
        }
        app.getModel().dialogMessages = new TreeSet<>();
    }
    public SharedListSelectionHandler(ChatMessengerApp parent, ChatPanelView view) {
        app = parent;
        panel = view;
    }
}