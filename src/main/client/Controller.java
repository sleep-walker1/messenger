package main.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import static main.client.ChatPanelView.LOGOUT_ACTION_COMMAND;
import static main.client.LoginPanelView.ACTION_COMMAND_LOGIN;
import static main.client.ChatPanelView.SEND_ACTION_COMMAND;

@Slf4j //тута команды
public class Controller implements ActionListener {
    final static Logger log = LogManager.getLogger(Controller.class);
    private ChatMessengerApp parent;
    private Command command;

    private Controller() {
    }

    public static Controller getInstance() {
        return ControllerHolder.INSTANCE;
    }

    private static class ControllerHolder {
        private static final Controller INSTANCE = new Controller();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            action(e);
        } catch (ParseException parseEx) {
            log.error(parseEx.getMessage());
        }
        command.execute();
    }

    private void action(ActionEvent e) throws ParseException {
        String commandName = e.getActionCommand();
        switch (commandName) {
            case ACTION_COMMAND_LOGIN: {
                LoginPanelView view = Utility.findParent(
                        (Component) e.getSource(), LoginPanelView.class);
                if (! EmailValidator.getInstance().isValid(view.getUserNameField().getText()) ||
                        !InetAddressValidator.getInstance().isValid(view.getServerIpAddressField().getText())) {
                    command = new LoginErrorCommand(view);
                } else {
                    parent.getModel().setCurrentUser(view.getUserNameField().getText());
                    parent.getModel().setServerIPAddress(view.getServerIpAddressField().getText());
                    parent.getModel().setLoggedUser(view.getUserNameField().getText());
                    parent.getModel().addLoginUser();
                    command = new ShowChatViewCommand(parent, view);
                }
            }
            break;
            case SEND_ACTION_COMMAND: {
                ChatPanelView view = Utility.findParent(
                        (Component) e.getSource(), ChatPanelView.class);
                parent.getModel().setLastMessageText(view.getTextMessageField().getText());
                command = new SendMessageCommand(parent, view);
            }
            break;
            case LOGOUT_ACTION_COMMAND: {
                ChatPanelView view = Utility.findParent(
                        (Component) e.getSource(), ChatPanelView.class);
                parent.getModel().initialize();
                command = new ShowLoginVewCommand(parent, view);
            }
            break;
            default:
                throw new ParseException("Unknown command: " + commandName, 0);

        }
    }


    public void setParent(ChatMessengerApp parent) {
        this.parent = parent;
    }
}
