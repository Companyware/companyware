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
package plugins.core.login.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import core.TextMessages;
import pluginmanager.plugininterfaces.PluginManager;
import plugins.core.login.controller.LoginController;
import plugins.core.plugins.settings.view.SettingsDialog;

public class LoginView extends JDialog implements Observer{
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
	private JPasswordField passwordField;
	private JTextField userTextField;
	private JCheckBox showPassword;

	public LoginView(PluginManager pm, JFrame frame){
		super(frame);
		this.pm = pm;
		this.initLayout();
	}
	
	public void initLayout(){
	    this.panel = new JPanel();
	    panel.setLayout(new BorderLayout());
	    
	    TextMessages service = ApplicationContextProvider.getContext().getBean(TextMessages.class);
        
	    this.setTitle(service.get("loginview.title"));
	    this.getContentPane().add(panel);
	    this.setSize(new Dimension(600, 500));
	    this.setLocationRelativeTo(this);
	    this.setModal(true);
	    
	    LoginController loginController = (LoginController)pm.getService("LoginController");
	    this.addWindowListener(loginController);
	}
	
	public void setContent(){
		LoginController loginController = (LoginController)pm.getService("LoginController");
        
        GridBagConstraints gbc;
        
        JPanel buttonPanel = new JPanel();
	    
	    buttonPanel.setLayout(new GridBagLayout());
	    
	    TextMessages service = ApplicationContextProvider.getContext().getBean(TextMessages.class);
        
        JButton save = new JButton(service.get("loginview.login"));
        save.addActionListener(loginController);
        JButton cancel = new JButton(service.get("loginview.cancel"));
        cancel.addActionListener(loginController);
   
        gbc = createGbc(0, 0, true);
        buttonPanel.add(save,gbc);
        gbc = createGbc(1, 0, true);
        buttonPanel.add(cancel,gbc);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        messagePanel = new JPanel();
        
        JPanel container = new JPanel(null);
        container.setPreferredSize(new Dimension(600,400));
       
	    JLabel userLabel=new JLabel(service.get("loginview.username"));
	    JLabel passwordLabel=new JLabel(service.get("loginview.password"));
	    this.userTextField=new JTextField();
	    
	    this.userTextField.addKeyListener(loginController);
	    
	    this.passwordField=new JPasswordField();
	    this.passwordField.addKeyListener(loginController);
	    this.showPassword=new JCheckBox(service.get("loginview.showpassword"));
	    showPassword.setActionCommand("showPassword");
	    showPassword.addActionListener(loginController);
	    
	    userLabel.setBounds(50,150,100,30);
	    passwordLabel.setBounds(50,220,100,30);
	    userTextField.setBounds(160,150,200,30);
	    passwordField.setBounds(160,220,200,30);
	    showPassword.setBounds(160,250,200,30);
		
	    container.add(userLabel);
	    container.add(passwordLabel);
	    container.add(userTextField);
	    container.add(passwordField);
	    container.add(showPassword);
	    
		this.getPanel().add(container,BorderLayout.NORTH);
		this.getPanel().add(messagePanel,BorderLayout.CENTER);
		this.getPanel().add(buttonPanel,BorderLayout.SOUTH);
		this.repaint();
		this.revalidate();
	}
	@Override
	public void update(Observable arg0, Object arg1) {
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
	
	public JPasswordField getPasswordField() {
		return passwordField;
	}

	public void setPasswordField(JPasswordField passwordField) {
		this.passwordField = passwordField;
	}

	public JTextField getUserTextField() {
		return userTextField;
	}

	public void setUserTextField(JTextField userTextField) {
		this.userTextField = userTextField;
	}

	public JCheckBox getShowPassword() {
		return showPassword;
	}

	public void setShowPassword(JCheckBox showPassword) {
		this.showPassword = showPassword;
	}
}
