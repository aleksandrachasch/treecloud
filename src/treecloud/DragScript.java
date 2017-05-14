package treecloud;


import java.io.IOException;
import java.io.InputStream;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGDocument;

public class DragScript {
	
	public static Node getScript() throws IOException{
	
		String parser = XMLResourceDescriptor.getXMLParserClassName();
		SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
		//InputStream stream = new FileInputStream("C:/Users/SONY/Documents/diploma/treecloud-ruscorpora/resources/stoplists/script.svg");
		InputStream stream = DragScript.class.getResourceAsStream("/script.svg");
		Document doc = f.createDocument("http://www.w3.org/2000/svg", "svg" ,"http://www.w3.org/2000/svg", stream);
		System.out.println(doc.getDocumentElement().getTagName());
		SVGDocument svgDoc = (SVGDocument) doc;
		System.out.println("length: " + svgDoc.getElementsByTagName("script").getLength());
		return svgDoc.getElementsByTagName("script").item(0);
	
	}
	
}
