package main.server;


import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.io.IOException;

public class ServerPanelView extends JPanel {
    private static ChatMessServer parent;

    public static void setParent(ChatMessServer parent) {
        ServerPanelView.parent = parent;
    }

    //private JLabel promptLabel;
    private JScrollPane messagesListPanel;
    private JScrollPane usersListPanel; //
    private JTextPane messagesTextPane;


    private ServerPanelView() {
        super();
        initialize();
    }

    public void initialize() {
        this.setName("serverPanelView");
        this.setLayout(new BorderLayout());

        this.add(getMessagesListPanel(), BorderLayout.CENTER);

        this.add(getUsersListPanel(), BorderLayout.WEST);

    }
    public void initModel(boolean getMessages) {

        if (getMessages) {
            getMessagesTextPane().setText(parent.getModel().msToString(parent.getModel().getAllMessages()));

            //parent.getModel().updList();
            parent.getModel().getList();
            getUsersListPanel();
            //System.out.println(parent.getModel().getList());
        }

        //TODO
        parent.getModel().listSelectionModel.addListSelectionListener(
                parent.getServerController());
    }

    public void modelChanged(String newMessages, String users) {//TODO
        System.out.println("12");
        if (newMessages.length() != 0) {
            System.out.println("22");
           // log.trace("New messages arrived: " + newMessages);
            HTMLDocument document2 = (HTMLDocument) getMessagesTextPane().getStyledDocument();
            Element element = document2.getElement(document2.getRootElements()[0],
                    HTML.Attribute.ID, "body");
            try {
                document2.insertBeforeEnd(element, newMessages);

            } catch (BadLocationException | IOException e) {
                //log.error("Bad location error: " + e.getMessage());
                System.out.println("333");
            }
            getMessagesTextPane().setCaretPosition(document2.getLength());
            //parent.getModel().getList();
           // parent.getModel().filterMessagesOfDialog();


            //log.trace("Messages text update");
        }
    }


    private JScrollPane getUsersListPanel() { //TODO
        if (usersListPanel == null) {
            usersListPanel = new JScrollPane(parent.getModel().getList());
            //usersListPanel.setPreferredSize(new Dimension(230, getHeight()));

            parent.getModel().getList().setSelectionMode(1);
            usersListPanel.setSize(getWidth()/3, getHeight());
            Color bl = new Color(190,193, 237);
            parent.getModel().getList().setBackground(bl );
            usersListPanel
                    .setVerticalScrollBarPolicy(
                            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        }
        return usersListPanel;
    }

    public JScrollPane getMessagesListPanel() {
        if (messagesListPanel == null) {
            messagesListPanel = new JScrollPane(getMessagesTextPane());
            messagesListPanel.setSize(getWidth()*2/3, getHeight());
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
            Color lightblue = new Color(150,120, 199);
            messagesTextPane.setBackground(lightblue);
            ((DefaultCaret) messagesTextPane.getCaret())
                    .setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        }
        return messagesTextPane;
    }

    public static ServerPanelView getInstance() {
        return ServerPanelView.ServerPanelViewHolder.INSTANCE;
    }
    private static class ServerPanelViewHolder {
        private static final ServerPanelView INSTANCE = new ServerPanelView();
    }
}
