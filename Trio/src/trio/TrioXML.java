package trio;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class TrioXML {

	public static void main(String args[]) {
		Document doc = null;
		Boolean res = false;
		
		System.out.println("Convertion starting...");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		TrioConvert converter = new TrioConvert();
		factory.setNamespaceAware(true);

		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		try {
			doc = builder.parse("test.xml");
		} catch (SAXException e) {
			System.err.println("SAXException Occured");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IOException Occured");
			e.printStackTrace();
		}

		res = converter.convertDocument(doc);

		if (res) {
			System.out.println("Code generating finished.");

		} else {
			System.err.println("Unexpected error during code generating.");
		}

	}

}
