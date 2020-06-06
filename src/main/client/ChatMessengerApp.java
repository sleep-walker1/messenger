package main.client;


import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

//@Slf4j
public class ChatMessengerApp extends JFrame {
    final static Logger log = LogManager.getLogger(ChatMessengerApp.class);
    public static final long DELAY = 100;
    public static final long PERIOD = 1000;

    private static final Model MODEl;
    private static final Controller CONTROLLER;
    private static final ViewFactory VIEWS;

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private static Timer timer;


    /*
    private List<String> users ;
    public void userToList(){
        if(!users.contains(getModel().getLoggedUser())){ //синглтон
            users.add(getModel().getLoggedUser());
            getModel().getUsers() +=  getModel().getLoggedUser();
        }
    }*/

    public static void main(String[] args) {
        JFrame frame = new ChatMessengerApp();
        frame.setVisible(true);
        frame.repaint();
    }


    static {
        MODEl = Model.getInstance(); //синглтон
        CONTROLLER = Controller.getInstance();
        VIEWS = ViewFactory.getInstance();

        log.trace("MVC instatiated" + MODEl + ";" + CONTROLLER + ";" + VIEWS);
    }

    public ChatMessengerApp() {
        super();
        initialize();
    }

    private void initialize() {
        AbstractView.setParent(this);
        MODEl.setParent(this);
        MODEl.initialize();
        CONTROLLER.setParent(this);
        VIEWS.viewRegister("login", LoginPanelView.getInstance());
        VIEWS.viewRegister("chat", ChatPanelView.getInstance());
        timer = new Timer("Server request for update messages");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH, HEIGHT);
        this.setLocationRelativeTo(null);
        this.setTitle("Messenger");

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(getLoginPanel(), BorderLayout.CENTER);
        this.setContentPane(contentPanel);
        //users = new ArrayList<>();
    }

    private JPanel getLoginPanel() {
        LoginPanelView loginPanelView = VIEWS.getview("login"); //внизу полоска
        loginPanelView.initModel();
        return loginPanelView;
    }

    ChatPanelView getChatPanelView(boolean doGetMessages) {
        ChatPanelView chatPanelView = VIEWS.getview("chat");
        chatPanelView.initModel(doGetMessages);
        return chatPanelView;
    }

    public static Model getModel() {
        return MODEl;
    }

    public static Controller getController() {
        return CONTROLLER;
    }

    public static ViewFactory getViews() {
        return VIEWS;
    }

    public static Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public void showChatPanelView() {
        showPanel(getChatPanelView(true));
    }

    private void showPanel(JPanel panel) {
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setVisible(true);
    }

    public void showLoginPanelView() {
        showPanel(getLoginPanel());
        LoginPanelView.getInstance().getUserNameField().requestFocusInWindow();
    }
}
