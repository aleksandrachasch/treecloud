package treecloud;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.transform.TransformerException;

import org.apache.batik.apps.rasterizer.SVGConverterException;
import org.apache.batik.transcoder.TranscoderException;

/**
 * Main class of the TreeCloud
 * @author Aleksandra Chashchina
 *
 */

public class Main{
	
	public static void main(String [] args) throws IOException, InterruptedException, TransformerException, SVGConverterException, TranscoderException, InvocationTargetException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException{
        
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		Runnable main = new Runnable() {
			public void run() {
				
				InitialFrame initialFrame = new InitialFrame();
				initialFrame.showFrame();
				}
			};
			
		SwingUtilities.invokeAndWait(main);
		
		}
		
	}


