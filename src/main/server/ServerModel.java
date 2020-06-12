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


    private DefaultListModel<String> listModel = new DefaultListModel();
    JList<String> userList = new JList<>();

    ListSelectionModel listSelectionModel = userList.getSelectionModel();

    public JList<String> getList() {
        userList.setModel(listModel);
        return userList;
    }
    public DefaultListModel getModelList() {
        return listModel;
    }

    public void filterMessagesOfDialog() { //here todo
        int index = getList().getAnchorSelectionIndex();
        for (Message one : allMessages) {
            if(listModel.get(index).contains(one.getUserTo())  && listModel.get(index).contains(one.getUserFrom())  ) {
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
            if( !(listModel.contains(from + "<->" + to) || listModel.contains(to + "<->" + from))) {
                listModel.addElement(from + "<->" + to);

            }
        }

        getList();
        this.getAllMessages().addAll(result);

        Set set = new HashSet(result);
        parent.getServerPanel(false)
                .modelChanged(msToString(set), getList().toString());
    }
   /* public void updList(){
    for (Message one : getAllMessages()) {
        /*
        if((one.getUserTo().equals(listModel.get(index))  || (one.getUserFrom().equals(listModel.get(index)))) ){
            dialogMessages.add(one);
            System.out.println("added");
        }
        //listModel.addElement(one.getUserFrom() + ", " + one.getUserTo());
        listModel.addElement(one.getUserFromMessage());
        System.out.println("+");
    }
    System.out.println(listModel.size());
    userList.setModel(listModel);
    //System.out.println(userList);
}*/

    public String dialogMessagesToString() {
        return msToString(dialogMessages);
    }
    public Set<Message> getAllMessages() {
        return allMessages;
    }
}
