package treecloud;



import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.batik.anim.dom.SVGDOMImplementation;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Class used for generating and rendering SVG image of a tree
 * @author Aleksandra Chashchina
 *
 */

public class TreeSVG {
	
     final static String scr = "<![CDATA[var selectedElement = 0; var currentX = 0; var currentY = 0; var currentMatrix = 0; function selectElement(evt) { selectedElement = evt.target; currentX = evt.clientX; currentY = evt.clientY; currentMatrix = selectedElement.getAttributeNS(null, \"transform\").slice(7,-1).split(' '); for(var i=0; i<currentMatrix.length; i++) { currentMatrix[i] = parseFloat(currentMatrix[i]); } selectedElement.setAttributeNS(null, \"onmousemove\", \"moveElement(evt)\"); selectedElement.setAttributeNS(null, \"onmouseout\", \"deselectElement(evt)\"); selectedElement.setAttributeNS(null, \"onmouseup\", \"deselectElement(evt)\"); } function moveElement(evt) { var dx = evt.clientX - currentX; var dy = evt.clientY - currentY; currentMatrix[4] += dx; currentMatrix[5] += dy; selectedElement.setAttributeNS(null, \"transform\", \"matrix(\" + currentMatrix.join(' ') + \")\"); currentX = evt.clientX; currentY = evt.clientY; } function deselectElement(evt) { if(selectedElement != 0){ selectedElement.removeAttributeNS(null, \"onmousemove\"); selectedElement.removeAttributeNS(null, \"onmouseout\"); selectedElement.removeAttributeNS(null, \"onmouseup\"); selectedElement = 0; } } ]]>";
	
	
	/**
	 * Generate SVG file from tree data structure
	 * @param nodes ArrayList of nodes of a tree
	 * @throws IOException 
	 * 
	 */
	
	public static String loadScript() throws IOException {
		String res = "";
		
		FileInputStream stream = new FileInputStream("C:/Users/SONY/Documents/drag_script.txt");
    	BufferedReader bufferedr = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
		while (true){
			String line = bufferedr.readLine();
			if (line == null) {
				break;
			}else{
			    res += line;}
			}
		bufferedr.close();
		
		return res;
	}
	
	public static Document drawTreeCloud(ArrayList<TreeNode> nodes, String edgecolor) throws IOException{
		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		Document doc = impl.createDocument(svgNS, "svg", null);
	
		Element svgRoot = doc.getDocumentElement();
		svgRoot.setAttributeNS(null, "width", "500");
		svgRoot.setAttributeNS(null, "height", "500");
		svgRoot.setAttributeNS(null, "viewBox", getViewBox(nodes));
		
		Element scr = doc.createElementNS(svgNS, "script");
		scr.setAttributeNS(null, "type", "text/ecmascript");
		scr.setTextContent(DragScript.getScript().getTextContent());
		svgRoot.appendChild(scr);
			
		for(int i=0; i<nodes.size(); i++){
	
				Element path = doc.createElementNS(svgNS, "path");
				path.setAttributeNS(null, "stroke-width", "0.9");
				path.setAttributeNS(null, "stroke", edgecolor.toLowerCase());
				path.setAttributeNS(null, "stroke-linecap","round");
				path.setAttributeNS(null, "stroke-linejoin", "round");
				path.setAttributeNS(null, "d", nodes.get(i).pathattr);
				svgRoot.appendChild(path);
				
				if(nodes.get(i).isLeaf){
					
					Element text = doc.createElementNS(svgNS, "text");
				    text.setAttributeNS(null,"style" , "fill:" + nodes.get(i).fontcolor);
					text.setAttributeNS(null,"font-size", nodes.get(i).fontsize);
					text.setAttributeNS(null,"font-family", "Arial");
					text.setTextContent(nodes.get(i).name);
					text.setAttributeNS(null,"class", "draggable");
					text.setAttributeNS(null,"ID", "1");
					text.setAttributeNS(null,"x", Double.toString(nodes.get(i).endX));
					text.setAttributeNS(null,"y", Double.toString(nodes.get(i).endY));
				    text.setAttributeNS(null,"transform", "matrix(1 0 0 1 0 0)");
				    text.setAttributeNS(null,"onmousedown", "selectElement(evt)");
				    svgRoot.appendChild(text);
					
					if(nodes.get(i).angle >= Math.PI/4 & nodes.get(i).angle <= Math.PI*3/4){
					
						text.setAttributeNS(null,"y" , Double.toString(Double.parseDouble(text.getAttribute("y"))+5));
						text.setTextContent(nodes.get(i).name);
						
						if(nodes.get(i).hasSisterLeaf){
							text.setAttributeNS(null,"text-anchor" , "start");
							text.setAttributeNS(null,"y",  Double.toString(nodes.get(i).endY+5));
						}else{
							text.setAttributeNS(null,"text-anchor" , "middle");
						}
						}
					
					else if(nodes.get(i).angle >= Math.PI*3/4 & nodes.get(i).angle <= Math.PI*5/4){
						text.setTextContent(nodes.get(i).name);
						text.setAttributeNS(null,"text-anchor" , "end");
						text.setAttributeNS(null,"y", Double.toString(nodes.get(i).endY));
					}
					
					else if(nodes.get(i).angle >= Math.PI*5/4 & nodes.get(i).angle <= Math.PI*7/4){
						text.setAttributeNS(null,"text-anchor" , "middle");
						text.setAttributeNS(null,"y", Double.toString(Double.parseDouble(text.getAttribute("y"))-5));
						text.setTextContent(nodes.get(i).name);
						
						if(nodes.get(i).hasSisterLeaf){
							text.setAttributeNS(null,"text-anchor" , "end");
							text.setAttributeNS(null,"y",  Double.toString(nodes.get(i).endY-6));
						}else{
							text.setAttributeNS(null,"text-anchor" , "middle");
						}
					}
					else{
						text.setTextContent(nodes.get(i).name);
						text.setAttributeNS(null,"text-anchor", "start");
						
					}
					
				
			}
				
	}
		
	NodeList tmpp = doc.getElementsByTagName("script");
	System.out.println("length of the imported: " + tmpp.getLength());
	System.out.println("text of the importd: " + tmpp.item(0).getTextContent());
	return doc;
			}
	
	private static String getViewBox(ArrayList<TreeNode> nodes){
		ArrayList<Double> allX = new ArrayList<Double>();
		ArrayList<Double> allY = new ArrayList<Double>();
		for(TreeNode node : nodes){
			allX.add(node.startX);
			allX.add(node.endX);
			allY.add(node.startY);
			allY.add(node.endY);
		}
		double c = Collections.min(allY)-50.0;
		double m = Collections.max(allY)+ 50.0;
		double mx = Collections.min(allX) - 50.0;
		double maxx = Collections.max(allX) + 50.0;
		String result = mx + " " 
				+ c + " "
				+ maxx + " "
				+ m;
		return result;
	}
	
}