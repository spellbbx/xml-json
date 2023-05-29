import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        String fileName = "data.xml";
        List<Employee> list = parseXML(fileName);
        String json = listToJson(list);
        writeString(json);
    }

    public static List<Employee> parseXML(String fileName) {
        List<Employee> employees = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(fileName));

            Node root = doc.getDocumentElement();
            NodeList nodeList = root.getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (Node.ELEMENT_NODE == node.getNodeType()) {
                    Element element = (Element) node;

                    long id = Long.parseLong(Objects.requireNonNull(getElementValue(element, "id")));
                    String firstName = getElementValue(element, "firstName");
                    String lastName = getElementValue(element, "lastName");
                    String country = getElementValue(element, "country");
                    int age = Integer.parseInt(Objects.requireNonNull(getElementValue(element, "age")));

                    employees.add(new Employee(id, firstName, lastName, country, age));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return employees;
    }

    private static String getElementValue(Element element, String tagName) {
        NodeList tagNodeList = element.getElementsByTagName(tagName);
        if (tagNodeList.getLength() > 0) {
            return tagNodeList.item(0).getTextContent();
        }
        return null;
    }

    private static String listToJson(List<Employee> list) {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        return gson.toJson(list, listType);
    }

    private static void writeString(String json) {

        try (FileWriter file = new FileWriter("data.json")) {

            file.write(json);
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
