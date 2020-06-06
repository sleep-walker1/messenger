package main.client;

import main.domain.Message;

import javax.swing.*;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.DefaultListModel;


public class Model {
    private ChatMessengerApp parent;
    private String currentUser;
    private String loggedUser;
    private String lastMessageText;
    private long lastMessageId;
    private Set<Message> messages;

   private DefaultListModel<String> listModel = new DefaultListModel();
    JList<String> UserList = new JList<String>();
    //private List<Message> users;
    private String usersStr ="";
    private String serverIPAddress = "127.0.0.1";

    public void addLoginUser() {

        if(loggedUser.length()!=0){
            if(!listModel.contains(loggedUser)){
                listModel.addElement(loggedUser);
                UserList.setModel(listModel);

            }
            /*
            if(usersStr.length()!=0){
                if (!usersStr.contains(loggedUser)){
                    usersStr += loggedUser + "<br/>";
                }
            } else usersStr += loggedUser + "<br/>";
            */

        }
    }
    public DefaultListModel getModelList() {
        return listModel;
    }

    public JList<String> getList() {
        UserList.setModel(listModel);
        return UserList;
    }
    public String getUserString() {
        return usersStr;
    }
/*
    public void setUsers(String usersStr) {
        this.usersStr = usersStr;
    }*/

    private static class ModelHolder {
        private static final Model INSTANCE = new Model();
    }

    public static Model getInstance() {
        return ModelHolder.INSTANCE;
    }

    public void init(){

    }
    public void initialize() { //проходит по всем сообщениям
        setMessages(new TreeSet<Message>(){
            @Override
            public String toString() {
                StringBuilder result = new StringBuilder("<html><body id ='body'>");
                Iterator<Message> i = iterator();
                while (i.hasNext()) {
                    Message m =i.next();
                    result.append(m.toString()).append("\n");
                    //usersStr = usersStr + m.getUsers();
                }

                return result.append("</body></html>").toString();
            }
        });
        lastMessageId = 0L;
        currentUser = "";
        loggedUser = "";
        usersStr ="";
        //listModel = new DefaultListModel();
    }

    private Model(){  }

    public String messagesToString() {
        return messages.toString();
    }

    public long getLastMessageId() {
        return lastMessageId;
    }

    public void setLastMessageId(long lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    public void addMessages(List<Message> messages) {
        for(int q=0;q<messages.size();q++){
            getModelList().addElement(messages.get(q).getUserFromMessage());
        }
        getList();
        this.getMessages().addAll(messages);
        parent.getChatPanelView(false)
                .modelChangedNotification(messages.toString(), getList().toString());
       /*
        String u = "";
        for(int q=0;q<messages.size();q++){
            u+=messages.get(q).getUsers();
        }
        usersStr+=u;
        this.getMessages().addAll(messages);
        parent.getChatPanelView(false)
            .modelChangedNotification(messages.toString(), usersStr);*/
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
    public Set<Message> getMessages() {
        return messages;
    }
    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }
    public String getServerIPAddress() { return serverIPAddress; }
    public void setServerIPAddress(String serverIPAddress) { this.serverIPAddress = serverIPAddress;}
}
