package main.server;

import main.domain.Message;
import main.domain.xml.MessageBuilder;
import main.domain.xml.MessageParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


//@Slf4j
public class ServerThread extends  Thread{
    final static Logger log = LogManager.getLogger(ServerThread.class);
    public static final String METHOD_GET = "GET";
    public static final String METHOD_PUT = "PUT";
    public static final String END_LINE_MESSAGE = "END";
    private final Socket socket;
    private final AtomicInteger messageId;

    public AtomicInteger getMessageId() {
        return messageId;
    }

    public Map<Long, Message> getMessagesList() {
        return messagesList;
    }

    private final Map<Long, Message> messagesList;
    private final BufferedReader in;
    private final PrintWriter out;


    public ServerThread(Socket socket, AtomicInteger messageId, Map<Long, Message> messagesList) throws IOException {
        this.socket = socket;
        this.messageId = messageId;
        this.messagesList = messagesList;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        start();
    }

    @Override
    public void run() {
        try {
            log.debug("New socket thread is starting...");
            String requestLine = in.readLine();
            log.debug(requestLine);
            switch (requestLine) {
                case METHOD_GET:
                    log.debug("get");
                    Long lastId = Long.valueOf(in.readLine());
                    log.debug(String.valueOf(lastId));
                    List<Message> newMessages = messagesList.entrySet().stream()
                            .filter(message -> message.getKey().compareTo(lastId) > 0)
                            .map(Map.Entry::getValue).collect(Collectors.toList());
                    log.debug(newMessages.toString());
                    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
                    Document document = builder.newDocument();
                    java.lang.String xmlContent = MessageBuilder.buildDocument(document, newMessages);
                    out.println(xmlContent);
                    out.println(END_LINE_MESSAGE);
                    out.flush();
                    break;

                case METHOD_PUT:
                    log.debug("put");

                    requestLine = in.readLine();
                    StringBuilder mesStr = new StringBuilder();


                    while (!END_LINE_MESSAGE.equals(requestLine))
                    {
                        mesStr.append(requestLine);
                        requestLine = in.readLine();
                    }

                    log.debug(String.valueOf(mesStr));

                    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                    SAXParser parser = saxParserFactory.newSAXParser();
                    List<Message> messages = new ArrayList<>();
                    MessageParser saxp = new MessageParser(messageId, messages);

                    InputStream is = new ByteArrayInputStream(mesStr.toString().getBytes());
                    parser.parse(is,saxp);


                    for (Message message: messages)
                    {
                        messagesList.put(message.getId(), message);
                    }

                    log.trace("Echoing: " + messages);
                    out.println("OK");
                    out.flush();
                    out.close();
                    break;
                default:
                    log.info("Unknown request: " + requestLine);
                    out.println("BAD REQUEST");
                    out.flush();
                    break;
            }
        }catch (Exception e) {
            log.error(e.getMessage());
            out.println("ERROR");
            out.flush();
        } finally {
            try {
                log.debug("Socket closing...");
                log.debug("Close stream objects");
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                log.error("Socket not closed");
            }
        }
    }
}
