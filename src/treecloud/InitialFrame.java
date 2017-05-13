package treecloud;


import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

/**
 * Initial frame of the application where user specifies all the parameters
 * @author Aleksandra Chashchina
 *
 */
public class InitialFrame extends JFrame{
	
	public String filepath;
	public String filetype;
	public String stopwordspath;
	public String language;
	public String target;
	public String formula;
	public int winSize;
	public int stepSize;
	public int numberOfTaxa;
	public boolean stopwords;
	private JButton importTextButton = new JButton("Import text");
	private JButton importStopwordsButton = new JButton("Import stopword list");
	private JButton drawButton = new JButton("Draw TreeCloud!");
	private JTextArea importedTextpath = new JTextArea(2,50);
	private JTextArea importedStopwords = new JTextArea(1,45);
	private JTextField windowSize = new JTextField(5);
	private JTextField step = new JTextField(5);
	private JTextField taxa = new JTextField(5);
	private JTextField locateTarget = new JTextField(20);
	private final String [] langs = {"", "English", "French", "German", "Italian", "Portuguese", "Russian","Spanish"};
	private JComboBox languageBox = new JComboBox(langs);
	private JRadioButton plainText = new JRadioButton("Plain text", true);
	private JRadioButton unitexConcordance = new JRadioButton("Unitex HTML concordance");
	private JRadioButton concordance = new JRadioButton("Concordance");
	private JCheckBox removestopwords = new JCheckBox("Remove stopwords");
	private ButtonGroup formulas = new ButtonGroup();
	private JProgressBar progress = new JProgressBar();
	
	public void showFrame() {
		
		this.constructFrame();
		this.setVisible(true);
		
	}
	
	private void constructFrame() {
		
		this.setTitle("TreeCloud");
		this.setSize(850, 450);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLayout(new GridLayout(7,1));
		
		JPanel importTextPanel = new JPanel();
		importedTextpath.setEditable(false);
//		importedTextpath.setFont(new Font("Times New Roman", Font.PLAIN, 10));
		importTextPanel.add(importedTextpath);
		importTextPanel.add(importTextButton);

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(drawButton);
		drawButton.setEnabled(false);
	
		importTextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showImportDialog();
			}
			
		});
		
		drawButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					drawTreeCloud();
				} catch (IOException | InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		importStopwordsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				importStopwords();
			}
		});
		
		languageBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JComboBox cb = (JComboBox)e.getSource();
					String res = (String)cb.getSelectedItem();
					language = res;
					/*TODO:
					 * check if all the parameters update properly if clicking on the button several times
					 *
					 */
					if (!res.equals("")) {
						importedStopwords.setText("");
						stopwordspath = null;
					}
					
				}
			
		});
		
		removestopwords.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(removestopwords.isSelected()) {
						importStopwordsButton.setEnabled(true);
						languageBox.setEnabled(true);
						stopwords = true;
				}else{
						importedStopwords.setText("");
						importStopwordsButton.setEnabled(false);
						languageBox.setSelectedIndex(0);
						languageBox.setEnabled(false);
						stopwords = false;	
				}
				
				
			}
		});
		
		JPanel importTypePanel = new JPanel();
//		importTypePanel.setLayout(new GridLayout(1,5));
		JLabel importTypeLabel = new JLabel();
		importTypeLabel.setText("Type of the imported file: ");
		ButtonGroup bg = new ButtonGroup();
		bg.add(plainText);
		bg.add(unitexConcordance);
		bg.add(concordance);
		importTypePanel.add(importTypeLabel);
		importTypePanel.add(plainText);
		importTypePanel.add(concordance);
		importTypePanel.add(unitexConcordance);
		
		JLabel targetLabel = new JLabel();
		targetLabel.setText("Target word: ");
		JPanel locatePanel = new JPanel();
		locatePanel.add(targetLabel);
		locatePanel.add(locateTarget);
		
		JPanel paramPanel = new JPanel();
		JLabel winLabel = new JLabel();
		winLabel.setText("Window size: ");
		windowSize.setDocument(getIntegerFilter());
		windowSize.setText("10");
		paramPanel.add(winLabel);
		paramPanel.add(windowSize);
		JLabel stepLabel = new JLabel();
		stepLabel.setText("Step size: ");
		step.setDocument(getIntegerFilter());
		step.setText("2");
		paramPanel.add(stepLabel);
		paramPanel.add(step);
		JLabel taxaLabel = new JLabel();
		taxaLabel.setText("Number of words to be presented on the tree: ");
		taxa.setDocument(getIntegerFilter());
		taxa.setText("30");
		paramPanel.add(taxaLabel);
		paramPanel.add(taxa);
		
		JPanel formulaPanel = new JPanel();
//		formulaPanel.setLayout(new GridLayout(1,6));
		JRadioButton chisquared = new JRadioButton("Chi-squared");
		JRadioButton pmi = new JRadioButton("PMI");
		JRadioButton oddsratio = new JRadioButton("Odds ratio");
		JRadioButton zscore = new JRadioButton("Z-score");
		JRadioButton loglikelihood = new JRadioButton("Log-likelihood");
		JRadioButton jaccard = new JRadioButton("Jaccard");
		formulas.add(chisquared);
		formulas.add(pmi);
		formulas.add(oddsratio);
		formulas.add(zscore);
		formulas.add(loglikelihood);
		formulas.add(jaccard);
		chisquared.setSelected(true);
		JLabel formulaLabel = new JLabel();
		formulaLabel.setText("Select formula: ");
		formulaPanel.add(formulaLabel);
		formulaPanel.add(chisquared);
		formulaPanel.add(pmi);
		formulaPanel.add(oddsratio);
		formulaPanel.add(zscore);
		formulaPanel.add(loglikelihood);
		formulaPanel.add(jaccard);
			
		JPanel importStopwordsPanel = new JPanel();
		importStopwordsPanel.add(removestopwords);
		importedStopwords.setEditable(false);
		importStopwordsPanel.add(importedStopwords);
		importStopwordsButton.setEnabled(false);
		importStopwordsPanel.add(importStopwordsButton);
		JLabel langLabel = new JLabel();
		langLabel.setText("Or choose the stopword list: ");
		importStopwordsPanel.add(langLabel);
		languageBox.setEnabled(false);
		importStopwordsPanel.add(languageBox);
		
		this.getContentPane().add(importTextPanel);
		this.getContentPane().add(importTypePanel);
		this.getContentPane().add(locatePanel);
		this.getContentPane().add(formulaPanel);
		this.getContentPane().add(importStopwordsPanel);
		this.getContentPane().add(paramPanel);
		this.getContentPane().add(buttonPanel);
		
	}
	
	private void showImportDialog() {
		
		JFileChooser importchooser = new JFileChooser();
		importchooser.setMultiSelectionEnabled(false);
		importchooser.setDialogType(JFileChooser.OPEN_DIALOG);
		
		int returnVal = importchooser.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			String res = importchooser.getSelectedFile().getAbsolutePath();
			System.out.println(res);
			filepath = res;
			importedTextpath.setText(res);
			drawButton.setEnabled(true);
			}
	}
	
	private void importStopwords() {
		
		JFileChooser importchooser = new JFileChooser();
		importchooser.setMultiSelectionEnabled(false);
		importchooser.setDialogType(JFileChooser.OPEN_DIALOG);
		
		int returnVal = importchooser.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			String res = importchooser.getSelectedFile().getAbsolutePath();
			importedStopwords.setText(res);
			stopwordspath = res;
			System.out.println(res);
			
			languageBox.setSelectedIndex(0);
			language = null;
		}
			
	}
	
	private PlainDocument getIntegerFilter() {
	
		PlainDocument doc = new PlainDocument();
		doc.setDocumentFilter(new DocumentFilter() {
			@Override
			public void insertString(FilterBypass fb, int off, String str, AttributeSet attr) 
					throws BadLocationException 
			{
				fb.insertString(off, str.replaceAll("\\D++", ""), attr);  // remove non-digits
			} 
			@Override
			public void replace(FilterBypass fb, int off, int len, String str, AttributeSet attr) 
					throws BadLocationException 
			{
				fb.replace(off, len, str.replaceAll("\\D++", ""), attr);  // remove non-digits
			}
		});
		return doc;
	}
	
	private String getSelectedFormula() {  
	    for (Enumeration<AbstractButton> buttons = formulas.getElements(); buttons.hasMoreElements();) {
	            AbstractButton button = buttons.nextElement();
	            if (button.isSelected()) {
	                return button.getText();
	            }
	        }
	    return null;
		}
	
	private void drawTreeCloud() throws IOException, InterruptedException {
		
		if (!filepath.equals("")) {
			
			if (plainText.isSelected()) {
				filetype = "text";
			}else if (concordance.isSelected()) {
				filetype = "concordance";
			}else{
				filetype = "Unitex";
			}
			
			target = locateTarget.getText();
			winSize = Integer.parseInt(windowSize.getText());
			stepSize = Integer.parseInt(step.getText());
			numberOfTaxa = Integer.parseInt(taxa.getText());
			formula = getSelectedFormula();
			System.out.println("winsize: " + winSize);
			System.out.println("stepSize: " + stepSize);
			System.out.println("numberofTaxa: " + numberOfTaxa);
			System.out.println("stopwords: " + stopwords);
			System.out.println("language: " + language);
			System.out.println("stopwordspath: " + stopwordspath);
			System.out.println("formula: " + formula);
			TreeCloud currentTree = new TreeCloud(filepath, filetype, winSize, stepSize, numberOfTaxa, stopwords, language, stopwordspath, target, formula);
			currentTree.executeTreeCloud();
		}
	}

}
