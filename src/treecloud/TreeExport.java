package treecloud;

import org.w3c.dom.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.batik.apps.rasterizer.DestinationType;
import org.apache.batik.apps.rasterizer.SVGConverter;
import org.apache.batik.apps.rasterizer.SVGConverterException;

/**
 * Class contains methods for exporting the TreeCloud into several formats: PDF, SVG, Newick
 * @author SONY
 *
 */

public class TreeExport {
	
	
	public static void  exportAsPdf(Document svgDoc, String outputpath) throws TransformerException, IOException, SVGConverterException{

		File svgFile = File.createTempFile("treecloud-" , ".svg");
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		DOMSource source2 = new DOMSource(svgDoc);
		FileOutputStream fOut = new FileOutputStream(svgFile);
		try{ 
			transformer.transform(source2, new StreamResult(fOut)); 
			}finally{
				fOut.close(); 
				}

		File outputFile = new File(outputpath);
		SVGConverter converter = new SVGConverter();
		converter.setDestinationType(DestinationType.PDF);
		converter.setSources(new String[] { svgFile.toString() });
		converter.setDst(outputFile);
		converter.execute();
	}
	
	public static void exportAsSvg(Document svgDoc, String outputpath) throws IOException, TransformerFactoryConfigurationError, TransformerException{
		File svgFile = new File(outputpath);
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		DOMSource source2 = new DOMSource(svgDoc);
		FileOutputStream fOut = new FileOutputStream(svgFile);
		try{
			transformer.transform(source2, new StreamResult(fOut)); 
			}finally{
				fOut.close(); 
				}
	}
	
	public static void exportAsJpeg(Document svgDoc, String outputpath) throws IOException, TransformerFactoryConfigurationError, TransformerException, SVGConverterException{
		File svgFile = File.createTempFile("treecloud-" , ".svg");
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		DOMSource source2 = new DOMSource(svgDoc);
		FileOutputStream fOut = new FileOutputStream(svgFile);
		try{ 
			transformer.transform(source2, new StreamResult(fOut)); 
			}finally{
				fOut.close(); 
				}

		File outputFile = new File(outputpath);
		SVGConverter converter = new SVGConverter();
		converter.setDestinationType(DestinationType.JPEG);
		converter.setQuality(new Float(0.99));
		converter.setSources(new String[] { svgFile.toString() });
		converter.setDst(outputFile);
		converter.execute();
	}
	
	/**
	 * Save tree in Newick format
	 * @param newick String
	 * @param path Absolute path to the newick file
	 * @throws IOException
	 */
	
	public static void exportAsNewick(String newick, String path) throws IOException{
		FileWriter fw = new FileWriter(path);
		fw.write(newick);
		fw.close();
	}

}
