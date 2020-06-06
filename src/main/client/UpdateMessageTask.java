package main.client;

import java.util.TimerTask;

public class UpdateMessageTask extends TimerTask {
    ChatMessengerApp app;
    public UpdateMessageTask(ChatMessengerApp app) {
        this.app = app;
    }

    @Override
    public void run() {
        Utility.messagesUpdate(app);
    }
}
