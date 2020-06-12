package main.client;

import java.util.TreeSet;

public class ShowDialogCommand implements Command {
    private ChatMessengerApp app;
    private ChatPanelView panel;
    public ShowDialogCommand(ChatMessengerApp parent, ChatPanelView view) {
        app = parent;
        panel = view;
    }

    @Override
    public void execute() {
        System.out.println("yeah");
        app.getModel().filterMessagesOfDialog();
        panel.getMessagesTextPane().setText(app.getModel().dialogMessagesToString());
        app.getModel().dialogMessages = new TreeSet<>();
    }
}
