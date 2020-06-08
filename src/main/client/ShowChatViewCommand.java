package main.client;

import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
//@Slf4j
public class ShowChatViewCommand implements Command {
    private ChatMessengerApp app;
    private LoginPanelView view;
    public ShowChatViewCommand(ChatMessengerApp parent, LoginPanelView view) {
        app = parent;
        this.view = view;
    }

    @Override
    public void execute() {
        Utility.messagesUpdate(app);
        //app.getModel().setLoggedUser(view.getUserNameField().getText());
        view.clearFiels();
        view.setVisible(false);
        app.setTimer(new Timer());
        app.getTimer().scheduleAtFixedRate(new UpdateMessageTask(app),
                ChatMessengerApp.DELAY, ChatMessengerApp.PERIOD);
        app.showChatPanelView();


    }
}
