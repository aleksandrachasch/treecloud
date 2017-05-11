package treecloud;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.w3c.dom.Document;


/**
 * Class used to compute tree from Concordance file
 * @author Aleksandra Chashchina
 */

public class Tree {
	
	public String corpuspath;
	int numberoftaxa = 30;
	public int winSize = 10;
	public int step = 2;
	public String language;
	public ArrayList<String> words = new ArrayList<String>();
	public ArrayList<ArrayList<Double>> distancematrix = new ArrayList<ArrayList<Double>>();
	public String newick;
	public ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();
	private ArrayList<TreeNode> leaves = new ArrayList<TreeNode>();
	public String importedtext;
	public String alphabet;
	public String importtype;
	public String statsoutput;
	public String stopwordsPath;
	public boolean removestopwords;
	public String locatetarget;
	public String formula;
	int minNmbOfOccur;
	public String colormode = "Red & blue";
	String edgecolor;
	Document svgDoc; 
	
	public void setCorpusPath(String p){
		this.corpuspath = p;
	}
	
	public void setSvgDoc(Document svg){
		this.svgDoc = svg;
	}
	
	public void setAlphabetPath(String a){
		this.alphabet = a;
	}
	
	public void setLanguage(String lang){
		this.language = lang;
	}
	
	public void setMinNbOccur(int n){
		this.minNmbOfOccur = n;
	}
	
	public void setNumberOfTaxa(int n){
		this.numberoftaxa = n;
	}
	
	public void setLocateTarget(String l){
		this.locatetarget = l;
	}
	
	public void setColorMode(String color){
		this.colormode = color;
	}
	
	public ArrayList<TreeNode> getNodeList(){
		return this.nodes;
	}
	
	public String getNewickTree(){
		return this.newick;
	}
	
	public ArrayList<String> getTreeWords(){
		return this.words;
	}
	
	public int getNumberOfTaxa(){
		return this.numberoftaxa;
	}
	
	public Document getSvgDoc(){
		return this.svgDoc;
	}
	
	
	public String getStopWordsFile(String workinglanguage){
		
		if(workinglanguage.equals("English") | workinglanguage.equals("French")){
			return this.getClass().getResource("/stoplists/StoplistEnglishFrench.txt").getPath();
			
		}else if(workinglanguage.equals("German")){
			return "/stoplists/StoplistGerman.txt";
		
		}else if(workinglanguage.equals("Portuguese")){
			return "/stoplists/StoplistPortuguese.txt";
		
		}else if(workinglanguage.equals("Italian")){
			return "/stoplists/StoplistItalian.txt";
			
		}else if(workinglanguage.equals("Spanish")){
			return "/stoplists/StoplistSpanish.txt";
			
		}else if(workinglanguage.equals("Russian")){
			return this.getClass().getResource("/stoplists/StoplistRussian.txt").getPath();
		}else{
			JOptionPane.showMessageDialog(null, "Stopwords list is not available for this language. Stopwords will not be removed");
			return null;
		}
		
	}
	
	public void findFiles(String dir){   
		File f = new File(dir);
		if(f.listFiles().length !=0){
		for (File item : f.listFiles()){
			if (item.isFile()){
				if (item.getName().equals("concord.html")){
					 importedtext = item.getAbsolutePath();
					 }
			}else{
				findFiles(item.getAbsolutePath());
				}
			}
		}
		}
	

	public void setStatsOutput(String concordhtmlpath){
		
		Pattern p = Pattern.compile("(.*?)(concord\\.html)");
		Matcher m = p.matcher(concordhtmlpath);
		if(m.find()){
			String res = m.group(1) + "statistics.txt";
			statsoutput = res;
		}else{
			System.out.println("Error in concord.html filepath");
		}
	}
	

	public void setDistanceMatrix() throws IOException, InterruptedException{

		System.out.println("Computing distance matrix...");
        ConcordanceText txt = new ConcordanceText();
        txt.locatetarget = locatetarget;
        if(importtype == "text") {
        	if(removestopwords){
        		if(stopwordsPath != null){
        			leaves = txt.computeMatrixText(importedtext, stopwordsPath, numberoftaxa, colormode, winSize, step, formula);
        		}else{
        			leaves = txt.computeMatrixText(importedtext, getStopWordsFile(language), numberoftaxa, colormode, winSize, step, formula);
        		}
        	}else{
        		leaves = txt.computeMatrixText(importedtext, null, numberoftaxa, colormode, winSize, step, formula);
        	}
        }else if(importtype == "Unitex"){
        	if(removestopwords){
        		leaves = txt.computeMatrixNoStats(importedtext, getStopWordsFile(language), numberoftaxa, colormode, formula);
        	}else{
        		leaves = txt.computeMatrixNoStats(importedtext, null, numberoftaxa, colormode, formula);
        	}
        }else{
        	if(removestopwords){
        		leaves = txt.computeMatrixConcordance(importedtext, getStopWordsFile(language), numberoftaxa, colormode, formula);
        	}else{
        		leaves = txt.computeMatrixConcordance(importedtext, null, numberoftaxa, colormode, formula);
        	}
        }
		distancematrix = txt.getMatrix();
		edgecolor = txt.getEdgeColor();
		words = txt.getLabelList();
	}
	
	public void performNJ(){
		System.out.println("Performing NeighborJoining algorithm...");
		NeighborJoining nj = new NeighborJoining(leaves);
		newick = nj.computeNJTree(distancematrix, words);
		nodes = nj.allnodes;
	}
	
	public void performEqualAngle(){
		System.out.println("Performing EqualAngle algorithm...");
		EqualAngle ea = new EqualAngle();
		ea.doEqualAngle(nodes, numberoftaxa);
	}
	
	public void drawTree(){
		System.out.println("Drawing the tree...");
		setSvgDoc(TreeSVG.drawTreeCloud(nodes, edgecolor));
	}
}