package main.server;

import main.client.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerController implements ActionListener, ListSelectionListener {
    final static Logger log = LogManager.getLogger(Controller.class);
    private ChatMessServer parent;
    private Command command;


    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        ListSelectionModel lsm = (ListSelectionModel)e.getSource();
        ServerPanelView view = ServerPanelView.getInstance();
        if (!lsm.isSelectionEmpty()) {
            command = new ShowDialogOnServer(parent, view);
            command.execute();
        }
    }

    private ServerController() {
    }

    public static ServerController getInstance() {
        return ServerController.ServerControllerHolder.INSTANCE;
    }
    private static class ServerControllerHolder {
            private static final ServerController INSTANCE = new ServerController();

    }
}