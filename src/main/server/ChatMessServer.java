package main.server;

import main.client.*;
import main.domain.Message;
import main.domain.xml.MessageBuilder;
import main.domain.xml.MessageParser;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.xml.parsers.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.sun.deploy.cache.Cache.exists;

//@Slf4j

public class ChatMessServer extends JFrame {

    public ChatMessServer() {
        super();
        initialize();
    }
    private static final ServerPanelView view;
    private static final ServerModel model;
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private static final ServerController controller;
    static {
        model = ServerModel.getInstance(); //синглтон
        controller = ServerController.getInstance();

        view = ServerPanelView.getInstance();

    }

    private void initialize() {
        ServerPanelView.setParent(this);
        model.setParent(this);
        model.initialize();
       // CONTROLLER.setParent(this);

        this.setSize(WIDTH, HEIGHT);
        this.setLocationRelativeTo(null);
        this.setTitle("Server");

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(getServerPanel(true), BorderLayout.CENTER);
        this.setContentPane(contentPanel);
    }

    public ServerPanelView getServerPanel(boolean b) {
        ServerPanelView ServerPanel = ServerPanelView.getInstance();
        ServerPanel.initModel(b);
        return ServerPanel;
    }

    final static Logger log = LogManager.getLogger(ChatMessServer.class);
    public static final int PORT = 7070;
    private static final int SERVER_TIMEOUT = 500;
    private static final String XML_FILE_NAME = "messages.xml";//?
    private static String FILE_NAME= "0.xml";
   // private  String FILE_NAME ;
    private static volatile boolean stop = false;
    private static AtomicInteger id = new AtomicInteger(0);
    private static Map<Long, Message> messagesList =
            Collections.synchronizedSortedMap(new TreeMap<Long, Message>());

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        JFrame fr = new ChatMessServer();

        // Load xml files with prev messages
        loadMessageXMLFile();

        updMessages();
        
        fr.setVisible(true);
        fr.repaint();

        // Run thread with quit command handler
        quitCommandThread();

        // Create new Socket Server
        ServerSocket serverSocket = new ServerSocket(PORT);
        log.info("Server started on port: " + PORT);

        // loop of request in sockets with timeout
        while (!stop)
        {
            serverSocket.setSoTimeout(SERVER_TIMEOUT);
            Socket socket;
            try {
                socket = serverSocket.accept();
                try {
                    new ServerThread(socket, id, messagesList);
                } catch (IOException e) {
                    log.error("IO error");
                    socket.close();
                }
            } catch (SocketTimeoutException e) {
            }

        }

        // Write message into xml file
        saveMessagesXMLFile();
        log.info("Server stopped");
        serverSocket.close();
        fr.dispose();
    }

    private static void updMessages() {

        if (messagesList.size() > 0)
        {
            System.out.println("11");
            model.addMessages(messagesList);

            //model.setLastMessageId(id.longValue());
            log.trace("List of new messages: " + messagesList.toString());
        }
    }

    private static void saveMessagesXMLFile() throws ParserConfigurationException, IOException { //TODO

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        String xmlContent = MessageBuilder.buildDocument(document, messagesList.values());

        OutputStream stream  = new FileOutputStream(new File(XML_FILE_NAME));
        OutputStreamWriter out = new OutputStreamWriter(stream, StandardCharsets.UTF_8);

        out.write(xmlContent + "\n");
        out.flush();
        out.close();
    }

    private static void loadMessageXMLFile() throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory  factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        List<Message> messages = new ArrayList<>();
        MessageParser saxp = new MessageParser(id, messages);
        //InputStream is = new ByteArrayInputStream(Files.readAllBytes(Paths.get(XML_FILE_NAME)));
        //Path s= Paths.get("C://Users//Юлия//ProjectsJava//Messenger//files");

        Path str = (Paths.get(XML_FILE_NAME)).toAbsolutePath();
        InputStream is = new ByteArrayInputStream(
                Files.readAllBytes(str));

        parser.parse(is,saxp);
        for (Message message: messages)
        {
            messagesList.put(message.getId(), message); //??
        }
        id.incrementAndGet();
        is.close();

    }

    private static void quitCommandThread() {
        new Thread() {
            @Override
            public void run() {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                while(true)
                {
                    String buf;
                    try {
                        buf = br.readLine();
                        if ("quit".equals(buf)) {
                            stop = true;
                            break;
                        } else
                        {
                            log.warn("Type 'quit' for exit termination");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public static ServerModel getModel() {
        return model;
    }


    public ServerController getServerController() {
        return controller;
    }
}