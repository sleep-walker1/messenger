package main;

import main.domain.Message;

import javax.swing.*;
import java.util.Set;

public abstract class AbstractModel {
    public String msToString( Set<Message> s){
        StringBuilder result = new StringBuilder("<html><body id ='body'>");
        for (Message one : s) {
            result.append(one.toString()).append("\n");
        }
        return result.append("</body></html>").toString();
    }
}
