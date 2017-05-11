package treecloud;

import java.io.IOException;

/**
 * Class which links the initial frame with the "core" of the application. It gets all the parameters from the 
 * initial frame and initializes the tree.
 * @author Aleksandra Chashchina
 *
 */

public class TreeCloud {
	
	public String textfile;
	public String filetype;
	public int windowSize;
	public int stepSize;
	public int numberOfTaxa;
	public boolean removeStopwords;
	public String language;
	public String stopwordsPath;
	public String locatePattern;
	public String formula;
	
	public TreeCloud(String filepath, String type, int window, int step, int taxa, boolean stopwords, String lang, String stopwordspath, String locate, String f) {
		textfile = filepath;
		filetype = type;
		windowSize = window;
		stepSize = step;
		numberOfTaxa = taxa;
		removeStopwords = stopwords;
		language = lang;
		stopwordsPath = stopwordspath;
		locatePattern = locate;
		formula = f;

	}
	
	public void executeTreeCloud() throws IOException, InterruptedException{
			Tree t = new Tree();
			t.importedtext = textfile;
			t.importtype = filetype;
			t.setNumberOfTaxa(numberOfTaxa);
			t.winSize = windowSize;
			t.step = stepSize;
			t.removestopwords = removeStopwords;
			t.language = language;
			t.stopwordsPath = stopwordsPath;
			t.colormode = "Red & blue";
			t.locatetarget = locatePattern;
			t.formula = formula;
			t.setDistanceMatrix();
			t.performNJ();
			t.performEqualAngle();
			t.drawTree();
			TreeCloudFrame f = new TreeCloudFrame();
			f.currentTree = t;
			f.paintFrame();
	}

}
