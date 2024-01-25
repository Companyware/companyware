/*******************************************************************************
 * MIT License
 *
 * Companyware - a java desktop framework for plugins
 *
 * Copyright (c) 2023 mbdus-Softwareentwicklung - Mathias Bauer
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package plugins.core.info.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import core.ApplicationContextProvider;
import core.Companyware;
import core.TextMessages;
import models.plugin.PluginModel;
import pluginmanager.plugininterfaces.PluginManager;
import plugins.core.info.controller.InfoController;
import plugins.core.info.service.JTextPaneWithHyperlink;
import plugins.core.plugins.settings.view.SettingsDialog;

public class InfoView extends JDialog implements Observer{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(SettingsDialog.class);
	private static final Insets WEST_INSETS = new Insets(5, 0, 5, 5);
    private static final Insets EAST_INSETS = new Insets(5, 5, 5, 0);
	private PluginManager pm;
	private JPanel panel;
	private JPanel messagePanel;

	public InfoView(PluginManager pm, JFrame frame){
		super(frame);
		this.pm = pm;
		this.initLayout();
	}
	
	public void initLayout(){
	    this.panel = new JPanel();
	    panel.setLayout(new BorderLayout());
	    
	    TextMessages service = ApplicationContextProvider.getContext().getBean(TextMessages.class);
        
	    this.setTitle(service.get("infoview.title"));
	    this.getContentPane().add(panel);
	    this.setSize(new Dimension(400, 400));
	    this.setLocationRelativeTo(this);
	    this.setModal(true);
	    
	    InfoController infoController = (InfoController)pm.getService("InfoController");
	    this.addWindowListener(infoController);
	}
	
	public void setContent(){
		InfoController infoController = (InfoController)pm.getService("InfoController");
        
        JPanel buttonPanel = new JPanel();
	    
	    buttonPanel.setLayout(new GridBagLayout());
	    
  TextMessages service = ApplicationContextProvider.getContext().getBean(TextMessages.class);
        messagePanel = new JPanel();
        
        String version = Companyware.getContainer().getVersionNumber();
     
        String text = service.get("infoview.text");
        text = text.replace("%version%", version);
        
        JTextPaneWithHyperlink info = new JTextPaneWithHyperlink(text);
       
        messagePanel.add(info,BorderLayout.CENTER);
        messagePanel.setPreferredSize(new Dimension(200,300));
        
        JPanel container = new JPanel(null);
        container.setPreferredSize(new Dimension(200,50));
	    
		this.getPanel().add(container,BorderLayout.NORTH);
		this.getPanel().add(messagePanel,BorderLayout.CENTER);
		this.repaint();
		this.revalidate();
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}
	
	public JPanel getMessagePanel() {
		return messagePanel;
	}

	public void setMessagePanel(JPanel messagePanel) {
		this.messagePanel = messagePanel;
	}
}
