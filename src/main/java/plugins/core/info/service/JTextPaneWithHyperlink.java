package plugins.core.info.service;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
 
/**
 * A hyperlink component that is based on JLabel.
 * 
 * @author www.codejava.net
 *
 */
public class JTextPaneWithHyperlink extends JTextPane {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String url;
   
    public JTextPaneWithHyperlink(String html) {       
        this(html, null, null);
    }
     
    public JTextPaneWithHyperlink(String html, String url) {
        this(html, url, null);
    }
     
    public void setURL(String url) {
        this.url = url;
    }  
     
    public JTextPaneWithHyperlink(String html, String url, String tooltip) {
        super();
        setContentType("text/html");
        setEditable(false);
        setBackground(null);
        setText(html);
        this.url = url;
         
        setToolTipText(tooltip);       
        
        addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				// TODO Auto-generated method stub
				if(HyperlinkEvent.EventType.ENTERED.equals(e.getEventType())) {
					((HTMLEditorKit)getEditorKit()).setDefaultCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}
				if(HyperlinkEvent.EventType.EXITED.equals(e.getEventType())) {
					setCursor(Cursor.getDefaultCursor());
					((HTMLEditorKit)getEditorKit()).setDefaultCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
				
				if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        desktop.browse(e.getURL().toURI());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
			}
        }); 
    }
}