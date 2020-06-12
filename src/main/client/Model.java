package main.client;

import main.AbstractModel;
import main.domain.Message;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class Model extends AbstractModel {
    private ChatMessengerApp parent;
    private String currentUser;
    private String loggedUser;
    private String lastMessageText;
    private long lastMessageId;
    private Set<Message> allMessages;
    public Set<Message> messagesOfLoggedUser = new TreeSet<>();
    public Set<Message> dialogMessages = new TreeSet<>();

   private DefaultListModel<String> listModel = new DefaultListModel();
    JList<String> UserList = new JList<String>();

    ListSelectionModel listSelectionModel = UserList.getSelectionModel();

    private String serverIPAddress = "127.0.0.1";

    public void addLoginUser() {

        if(loggedUser.length()!=0){
            if(!listModel.contains(loggedUser)){
                listModel.addElement(loggedUser);
                UserList.setModel(listModel);

            }
        }
    }
    public DefaultListModel getModelList() {
        return listModel;
    }

    public JList<String> getList( ) {
        UserList.setModel(listModel);
        return UserList;
    }

    public void filterMessagesOfCurrentUser() {
        for (Message one : allMessages) {
            if((one.getUserTo().equals(loggedUser)  || (one.getUserFrom().equals(loggedUser))) ){
                messagesOfLoggedUser.add(one);
                System.out.println("added");
            }
        }
    }
    public void filterMessagesOfDialog() {
        int index = getList().getAnchorSelectionIndex();
        for (Message one : messagesOfLoggedUser) {
            if((one.getUserTo().equals(listModel.get(index))  || (one.getUserFrom().equals(listModel.get(index)))) ){
                dialogMessages.add(one);
                System.out.println("added");
            }
        }
    }

    private static class ModelHolder {
        private static final Model INSTANCE = new Model();
    }

    public static Model getInstance() {
        return ModelHolder.INSTANCE;
    }


    public void initialize() { //проходит по всем сообщениям  TODO

       setAllMessages(new TreeSet<Message>(){
            @Override
            public String toString() {
                StringBuilder result = new StringBuilder("<html><body id ='body'>");
                Iterator<Message> i = iterator();
                while (i.hasNext()) {
                    Message m =i.next();
                    result.append(m.toString()).append("\n");
                }

                return result.append("</body></html>").toString();
            }
        });

        lastMessageId = 0L;
        currentUser = "";
        loggedUser = "";
       messagesOfLoggedUser = new TreeSet<Message>();
    }

    private Model(){  }

    public String messagesToString() {
        return msToString(allMessages);
    }
    public String filteredMessagesToString() {
       return msToString(messagesOfLoggedUser);
    }

    public String dialogMessagesToString() {
        return msToString(dialogMessages);
    }

    public long getLastMessageId() {
        return lastMessageId;
    }

    public void setLastMessageId(long lastMessageId) {
        this.lastMessageId = lastMessageId;
    }


    public void addMessages(List<Message> messages) {
        for(int q=0;q<messages.size();q++){
            String mes =messages.get(q).getUserFromMessage();
            if(!listModel.contains(mes)){
                listModel.addElement(mes);
            }
        }
        getList();
        this.getAllMessages().addAll(messages);

        parent.getChatPanelView(false)
                .modelChangedNotification(messages.toString(), getList().toString());

    }
    public void setFilteredMessages(Set<Message> Messages) {
        this.messagesOfLoggedUser = Messages;
    }

    public ChatMessengerApp getParent() {
        return parent;
    }
    public void setParent(ChatMessengerApp parent) {
        this.parent = parent;
    }
    public String getCurrentUser() {
        return currentUser;
    }
    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }
    public String getLoggedUser() {
        return loggedUser;
    }
    public void setLoggedUser(String loggedUser) {
        this.loggedUser = loggedUser;
    }
    public String getLastMessageText() {
        return lastMessageText;
    }
    public void setLastMessageText(String lastMessageText) {
        this.lastMessageText = lastMessageText;
    }
    public Set<Message> getAllMessages() {
        return allMessages;
    }
    public void setAllMessages(Set<Message> allMessages) {
        this.allMessages = allMessages;
    }
    public String getServerIPAddress() { return serverIPAddress; }
    public void setServerIPAddress(String serverIPAddress) { this.serverIPAddress = serverIPAddress;}
}
