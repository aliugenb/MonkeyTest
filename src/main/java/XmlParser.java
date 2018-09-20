import java.io.File;
import java.util.*;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Created with IntelliJ IDEA.
 * Author: ye.liu
 * Date: 2018/9/20
 * Time: 3:56 PM
 */

public class XmlParser {
    public final static String packageName = "com.ximalaya.ting.android.live";

    public static void main(String[] args) throws Exception {
        File classpathRoot = new File(System.getProperty("user.dir"));
        File xmlDir = new File(classpathRoot, "xml");
        File xml = new File(xmlDir, "window_dump.xml");

        HashSet<String> valueSets = iterateWholeXML(xml);
        for (String str : valueSets) {
            if (str.indexOf(packageName) != -1) {
                System.out.println(str);
                System.out.println("=====");
                return;
            }
        }

    }

    public static HashSet<String> iterateWholeXML(File file) {
        HashSet<String> valueSets = new HashSet<>();
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(file);
            Element root = document.getRootElement();
            recursiveNode(root, valueSets);
            return valueSets;
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void recursiveNode(Element root, HashSet<String> valueSets) {
        for (Iterator iter = root.elementIterator(); iter.hasNext(); ) {
            Element element = (Element) iter.next();
            if (element == null)
                continue;
            for (Iterator attrs = element.attributeIterator(); attrs.hasNext(); ) {
                Attribute attr = (Attribute) attrs.next();
                if (attr == null)
                    continue;
                String attrValue = attr.getValue();
                valueSets.add(attrValue);
            }
            if (!element.isTextOnly()) {
                recursiveNode(element, valueSets);
            }
        }
    }
}
