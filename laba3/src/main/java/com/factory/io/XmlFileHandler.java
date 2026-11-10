package com.factory.io;

import com.factory.model.Truck;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class XmlFileHandler {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yyyy");

    public static void writeToXml(String fileName, List<Truck> trucks) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();

        Element root = doc.createElement("trucks");
        doc.appendChild(root);

        for (Truck t : trucks) {
            Element el = doc.createElement("truck");
            el.setAttribute("id", String.valueOf(t.getId()));
            append(doc, el, "type", t.getType());
            append(doc, el, "model", t.getModel());
            append(doc, el, "enginePower", String.valueOf(t.getEnginePower()));
            append(doc, el, "maxSpeed", String.valueOf(t.getMaxSpeed()));
            append(doc, el, "productionDate",
                    t.getProductionDate() != null ? SDF.format(t.getProductionDate()) : "");
            append(doc, el, "price", String.valueOf(t.getPrice()));
            root.appendChild(el);
        }

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer tr = tf.newTransformer();
        tr.setOutputProperty(OutputKeys.INDENT, "yes");
        tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        tr.transform(new DOMSource(doc), new StreamResult(new File(fileName)));
    }

    private static void append(Document doc, Element parent, String name, String val) {
        Element e = doc.createElement(name);
        e.setTextContent(val == null ? "" : val);
        parent.appendChild(e);
    }

    public static List<Truck> readFromXml(String fileName) throws Exception {
        List<Truck> trucks = new ArrayList<>();
        File file = new File(fileName);
        if (!file.exists()) return trucks;

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);
        doc.getDocumentElement().normalize();

        NodeList list = doc.getElementsByTagName("truck");
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) continue;
            Element el = (Element) node;
            try {
                Truck t = new Truck();
                t.setId(Integer.parseInt(el.getAttribute("id")));
                t.setType(getText(el, "type"));
                t.setModel(getText(el, "model"));
                t.setEnginePower(Double.parseDouble(getText(el, "enginePower")));
                t.setMaxSpeed(Integer.parseInt(getText(el, "maxSpeed")));
                String dateStr = getText(el, "productionDate");
                if (!dateStr.isEmpty()) t.setProductionDate(SDF.parse(dateStr));
                t.setPrice(Double.parseDouble(getText(el, "price")));
                trucks.add(t);
            } catch (Exception e) {
                System.err.println("Ошибка XML-элемента #" + i + ": " + e.getMessage());
            }
        }
        return trucks;
    }

    private static String getText(Element el, String tag) {
        NodeList nl = el.getElementsByTagName(tag);
        return nl.getLength() == 0 ? "" : nl.item(0).getTextContent();
    }
}