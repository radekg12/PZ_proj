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

public class TreeViewer {
    private Document doc, doc2;
    private HashMap<String, String> avatarsHashMap = new HashMap<>();


    public TreeViewer() {
        try {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setValidating(true);
            domFactory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder builder;

            builder = domFactory.newDocumentBuilder();
            builder.setErrorHandler(new ErrorHandler() {

                @Override
                public void error(SAXParseException exception) {
                    exception.printStackTrace();
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


            //Document doc = builder.parse("avatars.xml");
            String s = getClass().getResource("avatars.xml").toString();
            String ss = "D:\\ftp\\SEMESTR_5\\PZ\\moje\\PZ_proj\\src\\main\\resources\\avatars.xml";
            File file = new File(s);
            File file2 = new File(ss);
            // Document doc = builder.parse(file);
            doc2 = builder.parse(file2);

            Element root = doc2.getDocumentElement();
            Node x = root.getFirstChild();
            do {
                avatarsHashMap.put(x.getChildNodes().item(0).getTextContent(), x.getChildNodes().item(1).getTextContent());
                x = x.getNextSibling();
            } while (x!= null);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        System.out.println("zakonczoinoXML");
    }

    public HashMap<String, String> getAvatarsHashMap() {
        return avatarsHashMap;
    }
}
