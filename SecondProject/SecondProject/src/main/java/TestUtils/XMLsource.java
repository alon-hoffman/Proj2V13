package TestUtils;

import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Objects;

public class XMLsource {

    public String getDriverType(){
       return getData("browserType");
    }
    public String getTestURL(){
        return getData("TestURL");
    }

    private String getData (String keyName) {
        String xmlFilePath = "C:\\Users\\MD\\Desktop\\Alon Java Projs\\SecondProject\\SecondProject\\src\\main\\java\\";
        String xmlFileName = "TestParams.xml";
        int itemIndex = 0;
        try {
            return Objects.requireNonNull(getDoc(xmlFilePath, xmlFileName)).getElementsByTagName(keyName).item(itemIndex).getTextContent();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Document getDoc (String xmlFilePath,String xmlFileName)  {
        try {
            File fXmlFile = new File(xmlFilePath + xmlFileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            return doc;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}
