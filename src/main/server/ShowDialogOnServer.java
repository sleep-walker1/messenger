package main.server;

import main.client.ChatMessengerApp;
import main.client.ChatPanelView;
import main.client.Command;

import java.util.TreeSet;

public class ShowDialogOnServer implements Command {
    private ChatMessServer app;
    private ServerPanelView panel;
    public ShowDialogOnServer(ChatMessServer parent, ServerPanelView view) {
        app = parent;
        panel = view;
    }

    @Override
    public void execute() {
        app.getModel().filterMessagesOfDialog();
        panel.getMessagesTextPane().setText(app.getModel().dialogMessagesToString());
        app.getModel().dialogMessages = new TreeSet<>();
    }
}
