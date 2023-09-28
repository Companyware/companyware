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
package plugins.core.plugins.settings.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TextField;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import core.ApplicationContextProvider;
import core.Companyware;
import core.TextMessages;
import models.pluginsettings.PluginSettingsModel;
import pluginmanager.plugininterfaces.PluginManager;
import plugins.core.frame.controller.FrameController;
import plugins.core.plugins.settings.controller.SettingsController;

public class SettingsDialog extends JDialog implements Observer{ 	
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

	public SettingsDialog(PluginManager pm, JFrame frame){
		super(frame);
		TextMessages service = ApplicationContextProvider.getContext().getBean(TextMessages.class);
	//	this.setTitle(service.get("program.name"));
		this.pm = pm;
		this.initLayout();
	}
	
	public void initLayout(){
	    this.panel = new JPanel();
	    panel.setLayout(new BorderLayout());
	    this.setTitle("Plugineinstellungen");
	    this.getContentPane().add(panel);
	    this.setSize(new Dimension(600, 600));
	    this.setLocationRelativeTo(this);
	    this.setModal(true);
	    
	    SettingsController settingsController = (SettingsController)pm.getService("SettingsController");
	    this.addWindowListener(settingsController);
	}
	
	public void setContent(List<Object[]> settings){
		SettingsController settingsController = (SettingsController)pm.getService("SettingsController");
	    
	    JPanel panel = new JPanel();
	    
	    panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(settingsController.getPluginName()),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        GridBagConstraints gbc;
	    
        Integer counter = 0;
        for (Object[] object : settings) {
        	PluginSettingsModel sm = (PluginSettingsModel) object[1];
        	JLabel label = new JLabel(sm.getName() + ":", JLabel.LEFT);
        	JTextField textField = new JTextField(10);
        	textField.setText(sm.getValue());
        	textField.setName(sm.getId().toString());
        	gbc = createGbc(0, counter, false);
        	panel.add(label, gbc);
        	gbc = createGbc(1, counter, false);
        	panel.add(textField, gbc);
        	counter++;
        }
        
        JPanel buttonPanel = new JPanel();
	    
	    buttonPanel.setLayout(new GridBagLayout());
	    
	    TextMessages service = ApplicationContextProvider.getContext().getBean(TextMessages.class);
        
        JButton save = new JButton(service.get("settingsdialog.save"));
        save.setActionCommand("save");
        save.addActionListener(settingsController);
        JButton cancel = new JButton(service.get("settingsdialog.cancel"));
        cancel.setActionCommand("cancel");
        cancel.addActionListener(settingsController);
   
        gbc = createGbc(0, 0, true);
        buttonPanel.add(save,gbc);
        gbc = createGbc(1, 0, true);
        buttonPanel.add(cancel,gbc);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        messagePanel = new JPanel();
	    
		this.getPanel().add(panel,BorderLayout.NORTH);
		this.getPanel().add(messagePanel,BorderLayout.CENTER);
		this.getPanel().add(buttonPanel,BorderLayout.SOUTH);
		this.repaint();
		this.revalidate();
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
	
	private GridBagConstraints createGbc(int x, int y, boolean isButton) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = (x == 0) ? GridBagConstraints.WEST : GridBagConstraints.EAST;
		gbc.fill = (x == 0) ? GridBagConstraints.BOTH : GridBagConstraints.HORIZONTAL;
		gbc.insets = (x == 0) ? WEST_INSETS : EAST_INSETS;
		
		if(!isButton){
			gbc.weightx = (x == 0) ? 0.1 : 1.0;
		}
		else{
			gbc.weightx = 1.0;
		}
		
		gbc.weighty = 1.0;
		return gbc;
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