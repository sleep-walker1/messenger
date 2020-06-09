package main.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import java.awt.*;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Array;

//@Slf4j //элементы панели
public class ChatPanelView extends AbstractView {
    final static Logger log = LogManager.getLogger(ChatPanelView.class);
    public static final String SEND_ACTION_COMMAND = "send";
    public static final String LOGOUT_ACTION_COMMAND = "logout";

    private JLabel promptLabel;
    private JScrollPane messagesListPanel;
    private JScrollPane usersListPanel; //
    private JTextPane messagesTextPane;
    private JTextPane usersPane; //
    private JPanel textMessagePanel;
    private JButton sendMessageButton;
    private JTextField textMessageField;
    private JButton logoutButton;


    private ChatPanelView() {
        super();
        initialize();
    }

    public static ChatPanelView getInstance() {
        return ChatPanelViewHolder.INSTANCE;
    }


    public void modelChangedNotification(String newMessages, String users) {//TODO
        System.out.println("12");
        if (newMessages.length() != 0) {
            System.out.println("22");
            log.trace("New messages arrived: " + newMessages);
            HTMLDocument document = (HTMLDocument) getMessagesTextPane().getStyledDocument();
                        Element element = document.getElement(document.getRootElements()[0],
                    HTML.Attribute.ID, "body");
            try {
                document.insertBeforeEnd(element, newMessages);

            } catch (BadLocationException | IOException e) {
                log.error("Bad location error: " + e.getMessage());
                System.out.println("333");
            }
            getMessagesTextPane().setCaretPosition(document.getLength());
            parent.getModel().getList();
            //parent.getModel().filterMessagesOfCurrentUser();
/*
            Reader stringReader = new StringReader(users);
            HTMLEditorKit htmlKit = new HTMLEditorKit();
            HTMLDocument doc2 = (HTMLDocument) htmlKit.createDefaultDocument();
            HTMLEditorKit.Parser parser = new ParserDelegator();
            try {
                parser.parse(stringReader, doc2.getReader(0), true);
            } catch (IOException e) {
                e.printStackTrace();
            }

*/
            //getUsersPane().setCaretPosition(doc2.getLength()); /////////TODO
            log.trace("Messages text update");
        }
    }

    private static class ChatPanelViewHolder {
        private static final ChatPanelView INSTANCE = new ChatPanelView();
    }

    public JLabel getPromptLabel() {
        if (promptLabel == null)
            promptLabel = new JLabel("Hello, " + parent.getModel().getLoggedUser() + "!");
        return promptLabel;
    }


    @Override
    public void initialize() {
        this.setName("chatPanelView");
        this.setLayout(new BorderLayout());
        JPanel header = new JPanel(new BorderLayout());

        header.add(getPromptLabel(), BorderLayout.WEST); //слева
        header.add(getLogoutButton(), BorderLayout.EAST); //справа
        this.add(header, BorderLayout.NORTH);
        this.add(getMessagesListPanel(), BorderLayout.CENTER);

        this.add(getUsersListPanel(), BorderLayout.WEST);

        this.add(getTextMessagePanel(), BorderLayout.SOUTH);
        InputMap im = getSendMessageButton().getInputMap();
        im.put(KeyStroke.getKeyStroke("ENTER"), "pressed");
        im.put(KeyStroke.getKeyStroke("released ENTER"), "released");


    }

    @Override
    public void clearFiels() {
        getMessagesTextPane().setText("");
        getTextMessageField().setText("");
    }

    public void initModel(boolean getMessages) {
        parent.getModel().setLastMessageText("");
        if (getMessages) {
            parent.getModel().filterMessagesOfCurrentUser();
            //System.out.println(parent.getModel().messagesOfLoggedUser);
            //getMessagesTextPane().setText(parent.getModel().messagesToString()); //filteredMessagesToString
            getMessagesTextPane().setText(parent.getModel().filteredMessagesToString());

            //getUsersPane().setText(parent.getModel().getUserString());
           // setUsersList(parent.getModel().getList());
            getUsersListPanel();
        }
        getPromptLabel().setText("Hello, " + parent.getModel().getLoggedUser() + "!");
        getTextMessageField().requestFocusInWindow();
        getSendMessageButton().setBackground(Color.ORANGE );
        parent.getRootPane().setDefaultButton(getSendMessageButton());

        //TODO
        parent.getModel().listSelectionModel.addListSelectionListener(
                new SharedListSelectionHandler(parent, this));
    }

   // private void setUsersList(JList<String> userList) {
    //    this.userList = userList;
   // }

   /* private JList<String> getUsersList() { //TODO

        if (listModel == null) {
            listModel = new DefaultListModel();
            userList = new JList(); //TODO
            listModel.addElement("hii");
            userList.setModel(listModel);
            userList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
            userList.setVisibleRowCount(0);
            //listModel = new DefaultListModel();

            //usersListPanel.setSize(getWidth()/2, getHeight()); //!
            //usersListPanel                    .setVerticalScrollBarPolicy(
                            //ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        }
        //userList = new JList(listModel);

        //char[] dat = parent.getModel().getUserString().toCharArray();
        //String[] data = new String[]{"jhjh"}; //TODO

        return userList;
    }*/

    private JScrollPane getUsersListPanel() { //TODO
        if (usersListPanel == null) {
            usersListPanel = new JScrollPane(parent.getModel().getList());
            //usersListPanel.setPreferredSize(new Dimension(100, 100));
            parent.getModel().getList().setSelectionMode(1);
            usersListPanel.setSize(getWidth()/2, getHeight()); //!
            Color bl = new Color(130,193, 237);
            parent.getModel().getList().setBackground(bl );
            usersListPanel
                    .setVerticalScrollBarPolicy(
                            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        }
        return usersListPanel;
    }
    public JTextPane getUsersPane() {
        if (usersPane == null) {
            usersPane = new JTextPane();
            usersPane.setContentType("text/html");
            usersPane.setEditable(false);
            usersPane.setName("usersArea");
            ((DefaultCaret) usersPane.getCaret())
                    .setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        }
        return usersPane;

    }
    public JScrollPane getMessagesListPanel() {
        if (messagesListPanel == null) {
            messagesListPanel = new JScrollPane(getMessagesTextPane());
            messagesListPanel.setSize(getMaximumSize());

            messagesListPanel
                    .setVerticalScrollBarPolicy(
                            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        }
        return messagesListPanel;
    }

    public JTextPane getMessagesTextPane() {
        if (messagesTextPane == null) {
            messagesTextPane = new JTextPane();
            messagesTextPane.setContentType("text/html");
            messagesTextPane.setEditable(false);
            messagesTextPane.setName("messagesTextArea");
            Color lightblue = new Color(150,228, 204);
            messagesTextPane.setBackground(lightblue);
            ((DefaultCaret) messagesTextPane.getCaret())
                    .setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        }
        return messagesTextPane;
    }

    public JPanel getTextMessagePanel() {
        if (textMessagePanel == null) {
            textMessagePanel = new JPanel();
            textMessagePanel.setLayout(new BoxLayout(textMessagePanel, BoxLayout.X_AXIS));
            addLabeledFiled(textMessagePanel, "Enter message: ", getTextMessageField());
            textMessagePanel.add(getSendMessageButton());
        }
        return textMessagePanel;
    }

    public JButton getSendMessageButton() {
        if (sendMessageButton == null) {
            sendMessageButton = new JButton();
            sendMessageButton.setText("Send");
            sendMessageButton.setName("sendMessageButton");
            sendMessageButton.setActionCommand(SEND_ACTION_COMMAND);
            sendMessageButton.addActionListener(parent.getController());
        }
        return sendMessageButton;
    }

    public JTextField getTextMessageField() {
        if (textMessageField == null) {
            textMessageField = new JTextField(12);
            textMessageField.setName("textMessageField");

        }
        return textMessageField;
    }

    public JButton getLogoutButton() {
        if (logoutButton == null) {
            logoutButton = new JButton();
            logoutButton.setText("Logout");
            logoutButton.setName("logoutButton");
            logoutButton.setActionCommand(LOGOUT_ACTION_COMMAND);
            logoutButton.addActionListener(parent.getController());
        }
        return logoutButton;
    }
}
