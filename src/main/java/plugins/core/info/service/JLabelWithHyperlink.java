package plugins.core.info.service;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
 
import javax.swing.JLabel;
import javax.swing.JOptionPane;
 
/**
 * A hyperlink component that is based on JLabel.
 * 
 * @author www.codejava.net
 *
 */
public class JLabelWithHyperlink extends JLabel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String url;
   
    public JLabelWithHyperlink(String html) {       
        this(html, null, null);
    }
     
    public JLabelWithHyperlink(String html, String url) {
        this(html, url, null);
    }
     
    public void setURL(String url) {
        this.url = url;
    }  
     
    public JLabelWithHyperlink(String html, String url, String tooltip) {
        super(html);
        this.url = url;
         
        setToolTipText(tooltip);       
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
         
        addMouseListener(new MouseAdapter() {
             
            @Override
            public void mouseEntered(MouseEvent e) {
            }
             
            @Override
            public void mouseExited(MouseEvent e) {
            }
             
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                     
                    Desktop.getDesktop().browse(new URI(JLabelWithHyperlink.this.url));
                     
                } catch (IOException | URISyntaxException e1) {
                    JOptionPane.showMessageDialog(JLabelWithHyperlink.this,
                            "Could not open the hyperlink. Error: " + e1.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }              
            }
             
        });  
    }
}