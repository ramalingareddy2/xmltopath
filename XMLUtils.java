public class XMLUtils {
static public String transformToXML(Map<String, String> pathValueMap, String delimiter)
        throws ParserConfigurationException, TransformerException {

    DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
    Document document = documentBuilder.newDocument();

    Element rootElement = null;

    Iterator<Entry<String, String>> it = pathValueMap.entrySet().iterator();
    while (it.hasNext()) {
        Entry<String, String> pair = it.next();
        if (pair.getKey() != null && pair.getKey() != "" && rootElement == null) {
            String[] pathValuesplit = pair.getKey().split(delimiter);
            rootElement = document.createElement(pathValuesplit[0]);
            break;
        }
    }

    document.appendChild(rootElement);
    Element rootNode = rootElement;
    Iterator<Entry<String, String>> iterator = pathValueMap.entrySet().iterator();
    while (iterator.hasNext()) {
        Entry<String, String> pair = iterator.next();
        if (pair.getKey() != null && pair.getKey() != "" && rootElement != null) {
            String[] pathValuesplit = pair.getKey().split(delimiter);
            if (pathValuesplit[0].equals(rootElement.getNodeName())) {
                int i = pathValuesplit.length;

                Element parentNode = rootNode;
                int j = 1;

                while (j < i) {
                    Element child = null;

                    NodeList childNodes = parentNode.getChildNodes();
                    for (int k = 0; k < childNodes.getLength(); k++) {
                        if (childNodes.item(k).getNodeName().equals(pathValuesplit[j])
                                && childNodes.item(k) instanceof Element) {
                            child = (Element) childNodes.item(k);
                            break;
                        }
                    }

                    if (child == null) {
                        child = document.createElement(pathValuesplit[j]);
                        if (j == (i - 1)) {
                            child.appendChild(
                                    document.createTextNode(pair.getValue() == null ? "" : pair.getValue()));
                        }
                    }
                    parentNode.appendChild(child);
                    parentNode = child;
                    j++;
                }
            } else {
                // ignore any other root - add logger
                System.out.println("Data not processed for node: " + pair.getKey());
            }
        }
    }

    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    DOMSource domSource = new DOMSource(document);

    // to return a XMLstring in response to an API
     StringWriter writer = new StringWriter();
     StreamResult result = new StreamResult(writer);

     StreamResult resultToFile = new StreamResult(new File("C:/EclipseProgramOutputs/GeneratedXMLFromPathValue.xml"));
     transformer.transform(domSource, resultToFile);
     transformer.transform(domSource, result);

    return writer.toString();
}

public static void main(String args[])
{

    Map<String, String> pathValueMap = new HashMap<String, String>();
    String delimiter = "/";

    pathValueMap.put("create/article__1/id", "1");
    pathValueMap.put("create/article__1/description", "something");
    pathValueMap.put("create/article__1/name", "Book Name");
    pathValueMap.put("create/article__1/price/amount", "120" );
    pathValueMap.put("create/article__1/price/currency", "INR");
    pathValueMap.put("create/article__2/id", "2");
    pathValueMap.put("create/article__2/description", "something else");
    pathValueMap.put("create/article__2/name", "Book name 1");
    pathValueMap.put("create/article__2/price/amount", "2100");
    pathValueMap.put("create/article__2/price/currency", "USD");

    try {
        XMLUtils.transformToXML(pathValueMap, delimiter);
    } catch (ParserConfigurationException | TransformerException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

}}