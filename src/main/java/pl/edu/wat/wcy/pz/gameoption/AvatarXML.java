package pl.edu.wat.wcy.pz.gameoption;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AvatarXML {
    private static final Logger LOGGER = Logger.getLogger(AvatarXML.class.getSimpleName(), "LogsMessages");
    private static AvatarXML ourInstance = new AvatarXML();
    private HashMap<String, String> avatarsHashMap = new HashMap<>();


    private AvatarXML() {
        try {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setValidating(true);
            domFactory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder builder;

            builder = domFactory.newDocumentBuilder();
            builder.setErrorHandler(new ErrorHandler() {

                @Override
                public void error(SAXParseException exception) {
                    exception.
                            printStackTrace();
                }

                @Override
                public void fatalError(SAXParseException exception) {
                    exception.printStackTrace();
                }

                @Override
                public void warning(SAXParseException exception) {
                    exception.printStackTrace();
                }
            });

            String s = getClass().getClassLoader().getResource("avatars.xml").getPath();
            File file = new File(s);
            Document doc = builder.parse(file);

            Element root = doc.getDocumentElement();
            Node x = root.getFirstChild();
            do {
                avatarsHashMap.put(x.getChildNodes().item(0).getTextContent(), x.getChildNodes().item(1).getTextContent());
                x = x.getNextSibling();
            } while (x != null);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            LOGGER.log(Level.WARNING, "xml.open", e);
        }
        LOGGER.info("xml.openInfo");
    }

    public static AvatarXML getInstance() {
        return ourInstance;
    }

    public HashMap<String, String> getAvatarsHashMap() {
        return avatarsHashMap;
    }
}
