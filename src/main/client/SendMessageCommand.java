package main.client;
import main.server.ChatMessServer;
import main.server.ServerThread;
import main.domain.Message;
import main.domain.xml.MessageBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Slf4j
public class SendMessageCommand implements Command {
    final static Logger log = LogManager.getLogger(SendMessageCommand.class);
    private ChatMessengerApp app;
    private ChatPanelView panel;
    private InetAddress addr;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;


    public SendMessageCommand(ChatMessengerApp parent, ChatPanelView view) {
        app = parent;
        panel = view;
    }

    @Override
    public void execute() {
        try {
            addr = InetAddress.getByName(ChatMessengerApp.getModel().getServerIPAddress());
            socket = new Socket(addr, ChatMessServer.PORT);
            out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            log.error("Socket error: " + e.getMessage());
        }
        try {
            String result;
            do {
                out.println(ServerThread.METHOD_PUT);

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.newDocument();

                List<Message> message = new ArrayList<>(); //TODO
                message.add(Message.newMessage()
                        .text(panel.getTextMessageField().getText())
                        .from(app.getModel().getLoggedUser())
                        .to("") // TODO
                        .moment(Calendar.getInstance())
                        .build());

                String xmlContent = MessageBuilder.buildDocument(document, message);
                out.println(xmlContent);
                out.println(ServerThread.END_LINE_MESSAGE);
                result = in.readLine();
            } while (!"OK".equals(result));
        } catch (IOException | ParserConfigurationException e)
        {
            log.error("Send message error: " + e.getMessage());
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e)
            {
                log.error("Socket close error: " + e.getMessage());
            }
        }
        panel.getTextMessageField().setText("");
        panel.getTextMessageField().requestFocusInWindow();
    }
}
