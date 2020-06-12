package main.server;

import main.AbstractModel;
import main.domain.Message;

import javax.swing.*;
import java.util.*;
import javax.swing.DefaultListModel;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ServerModel extends AbstractModel {
    private ChatMessServer parent;
    private Set<Message> allMessages= new TreeSet<>();
    public Set<Message> dialogMessages = new TreeSet<>();


    private DefaultListModel<String> listMod = new DefaultListModel();
    JList<String> userList = new JList<>();

    //ListSelectionModel listSelectionModel = userList.getSelectionModel();

    public JList<String> getList() {
        userList.setModel(listMod);
        return userList;
    }
    public DefaultListModel getModelList() {
        return listMod;
    }

    public void filterMessagesOfDialog() { //here
        int index = getList().getAnchorSelectionIndex();
        for (Message one : allMessages) {
            if((one.getUserTo().equals(listMod.get(index))  || (one.getUserFrom().equals(listMod.get(index)))) ){
                dialogMessages.add(one);
                System.out.println("added");
            }
        }
    }

    public void setParent(ChatMessServer chatMessServer) {
        parent = chatMessServer;
    }

    private static class ServerModelHolder {
        private static final ServerModel INSTANCE = new ServerModel();
    }

    public static ServerModel getInstance() {
        return ServerModel.ServerModelHolder.INSTANCE;
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

        // UserList.addListSelectionListener(lis );

        //lastMessageId = 0L;
        //currentUser = "";
        //loggedUser = "";
        //usersStr ="";
        dialogMessages = new TreeSet<Message>();
        userList.setVisible(true);

    }

    private void setAllMessages(TreeSet<Message> messages) {
        allMessages = messages;
    }

    private ServerModel(){  }

    public void addMessages(Map<Long, Message> messages) {
        List<Message> result = new ArrayList(messages.values());

        for (int q = 0; q < result.size(); q++) {
            String from =result.get(q).getUserFrom();
            String to = result.get(q).getUserTo();
            //System.out.println(from + ", " + to);
            if( !(listMod.contains(from + ", " + to) || listMod.contains(to + ", " + from))) {
                //listModel.addElement(from + ", " + to);
                listMod.addElement(from );
            }

        }

        getList();
        this.getAllMessages().addAll(result);
        //System.out.println(getAllMessages());
        //updList();
        Set set = new HashSet(result);
        parent.getServerPanel(false)
                .modelChanged(msToString(set), getList().toString());
    }
    public void updList(){
    for (Message one : getAllMessages()) {
        /*
        if((one.getUserTo().equals(listModel.get(index))  || (one.getUserFrom().equals(listModel.get(index)))) ){
            dialogMessages.add(one);
            System.out.println("added");
        }*/
        //listModel.addElement(one.getUserFrom() + ", " + one.getUserTo());
        listMod.addElement(one.getUserFromMessage());
        System.out.println("+");
    }
    System.out.println(listMod.size());
    userList.setModel(listMod);
    //System.out.println(userList);
}

    public Set<Message> getAllMessages() {
        return allMessages;
    }
}
