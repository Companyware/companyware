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
package plugins.core.users.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.TableColumnModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import core.ApplicationContextProvider;
import core.TextMessages;
import models.user.UserModel;
import models.user.Repository;
import pluginmanager.plugininterfaces.PluginManager;
import plugins.core.frame.controller.FrameController;
import plugins.core.frame.view.Frame;
import plugins.core.frame.view.JOutlookBar;
import plugins.core.menu.view.Menu;
import plugins.core.users.controller.ButtonTableEditor;
import plugins.core.users.controller.CheckboxCellEditor;
import plugins.core.users.controller.ImageButtonTableEditor;
import plugins.core.users.controller.UsersController;
import plugins.core.plugins.model.DisplayableObjectTableModel;
import plugins.core.plugins.model.ObjectTableModel;

public class Users  implements Observer {
	private static final Log log = LogFactory.getLog(Users.class);
	
	private PluginManager pm;

	private JTextField nameTextField;
	private JTextField emailTextField;
	private JCheckBox activeCheckbox;
	private JTextField usernameTextField;
	private JPasswordField passwordField;

	
	public Users(PluginManager pm) {
		this.pm = pm;
		this.createMenuEntry();
		this.addOutlookBarEntry();
	}
	
	/**
	 * create menu entry "Pluginmanager"
	 */
	public void createMenuEntry(){
		UsersController usersController = (UsersController)pm.getService("UsersController");
		Menu menuView = (Menu)pm.getService("MenuView");
		if(menuView != null){
			JMenu settings = menuView.getSettings();
			TextMessages service = ApplicationContextProvider.getContext().getBean(TextMessages.class);
			JMenuItem pluginManager = new JMenuItem(service.get("usermanager.menu"));
	        pluginManager.addActionListener(usersController);
	        settings.add(pluginManager);
		}
	}
	
	public void addOutlookBarEntry(){
		FrameController frameController = (FrameController)pm.getService("FrameController");
		Frame frameView = (Frame)frameController.getView();
		JOutlookBar outlookBar = frameView.getOutlookBar();
		UsersController usersController = (UsersController)pm.getService("UsersController");
		ImageIcon useradd = new ImageIcon(this.getClass().getResource("/images/user_add.png"));
		ImageIcon users = new ImageIcon(this.getClass().getResource("/images/users.gif"));
		outlookBar.addBarEntry("settings");
		outlookBar.addBarEntryIcon("settings","adduser", useradd, usersController);
		outlookBar.addBarEntryIcon("settings","useroverview", users, usersController);
		outlookBar.addBar("settings", outlookBar.getPanel("settings"),200);
		outlookBar.setPreferredSize();
	}
	
	public void createTableOverview(){
		FrameController frameController = (FrameController)pm.getService("FrameController");
		Frame frame = frameController.getView();
		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(frame.getWidth()+frame.getConstCenterWidth()-50,frame.getHeight()+frame.getConstCenterHeight()-100));
		JLabel label = new JLabel("Benutzerverwaltung",SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(frame.getWidth()+frame.getConstCenterWidth()-50,50));
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, 20));
        panel.add(label,BorderLayout.NORTH);
        List<UserModel> users=Repository.getUsers();
        
        UIManager.put("Table.alternateRowColor", Color.white);
        
        ObjectTableModel<UserModel> tableModel = new DisplayableObjectTableModel<>(UserModel.class);
        tableModel.setObjectRows(users);
        JTable table = new JTable(tableModel);
	    table.setDefaultRenderer(Object.class, new ObjectColumnRenderer());
	    table.setDefaultRenderer(Integer.class, new IntegerColumnRenderer());
        table.setDefaultRenderer(Boolean.class, new CheckboxColumnRenderer());
        table.setDefaultRenderer(JCheckBox.class, new ButtonColumnRenderer("Edit"));
        table.setDefaultRenderer(JButton.class, new ImageButtonColumnRenderer("Delete"));
        
        tableModel.addTableModelListener((e)->{table.repaint();});
	    
	    JCheckBox checkBox = new JCheckBox();
	    checkBox.setActionCommand("active");
        checkBox.setHorizontalAlignment(JCheckBox.CENTER);
   //     table.setDefaultEditor(Boolean.class, new DefaultCellEditor(checkBox));
        table.setDefaultEditor(Boolean.class,new CheckboxCellEditor(checkBox,pm));
        
        JCheckBox button = new JCheckBox();
        button.setActionCommand("settings button");
        button.setHorizontalAlignment(JCheckBox.CENTER);
        table.setDefaultEditor(JCheckBox.class, new ButtonTableEditor(button,pm));
        
        JCheckBox imageButton = new JCheckBox();
        imageButton.setActionCommand("delete");
        imageButton.setHorizontalAlignment(JCheckBox.CENTER);
        table.setDefaultEditor(JButton.class, new ImageButtonTableEditor(imageButton,pm));
	    
        table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        TableColumnModel colModel=table.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(400);
        colModel.getColumn(1).setPreferredWidth(400);    
        colModel.getColumn(2).setPreferredWidth(25);
        colModel.getColumn(3).setPreferredWidth(5);    
        colModel.getColumn(4).setPreferredWidth(5);    
        
	    table.setModel(tableModel);
	    
	    table.setRowHeight(35);
	    
	    panel.add(new JScrollPane(table),BorderLayout.CENTER);
	    frame.getCenterPanel().removeAll();
	    frame.getCenterPanel().add(panel,BorderLayout.CENTER);
	    frame.getCenterPanel().revalidate();
	    frame.getCenterPanel().repaint();
	    frame.setVisible(true);
	}
	
	public void addUserView(){
		UsersController usersController = (UsersController)pm.getService("UsersController");
		FrameController frameController = (FrameController)pm.getService("FrameController");
		Frame frame = frameController.getView();
		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(frame.getWidth()+frame.getConstCenterWidth()-250,frame.getHeight()+frame.getConstCenterHeight()-100));
		
		JPanel headerPanel = new JPanel(null);
		headerPanel.setPreferredSize(new Dimension(panel.getWidth()-50,100));
		
		TextMessages service = ApplicationContextProvider.getContext().getBean(TextMessages.class);
		
		JLabel label = new JLabel(service.get("userview.adduser"),SwingConstants.CENTER);
		label.setBounds((frame.getWidth()+frame.getConstCenterWidth()-250)/2-125,30,200,40);
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, 20));
        headerPanel.add(label);
        panel.add(headerPanel,BorderLayout.NORTH);
        
        //Content
        JPanel buttonPanel = new JPanel(null);
        
        JButton save = new JButton(service.get("userview.saveuser"));
        save.setActionCommand("adduser");
        save.addActionListener(usersController);
        
        save.setBounds((frame.getWidth()+frame.getConstCenterWidth()-250)/2-125,30,200,40);
   
        buttonPanel.add(save);
        buttonPanel.setPreferredSize(new Dimension(panel.getWidth()-50,100));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JPanel container = new JPanel(new BorderLayout());
        container.setPreferredSize(new Dimension((frame.getWidth()+frame.getConstCenterWidth()-250),frame.getHeight()+frame.getConstCenterHeight()-100));
        JPanel leftContainer = new JPanel(null);
        leftContainer.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JPanel rightContainer = new JPanel(null);
        rightContainer.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        leftContainer.setPreferredSize(new Dimension((frame.getWidth()/2)+frame.getConstCenterWidth()-100,frame.getHeight()+frame.getConstCenterHeight()-100));
        rightContainer.setPreferredSize(new Dimension((frame.getWidth()/2)+frame.getConstCenterWidth()-50,frame.getHeight()+frame.getConstCenterHeight()-100));
		int y = 150;
	    
	    JLabel nameLabel=new JLabel(service.get("userview.name"));
	    nameLabel.setBounds(50,y,150,30);
	    this.nameTextField=new JTextField();
	    nameTextField.setBounds(200,y,300,30);
	    leftContainer.add(nameLabel);
	    leftContainer.add(nameTextField);
	    
	    y += 70;
	    JLabel emailLabel=new JLabel(service.get("userview.email"));
	    emailLabel.setBounds(50,y,150,30);
	    this.emailTextField=new JTextField();
	    emailTextField.setBounds(200,y,300,30);
	    leftContainer.add(emailLabel);
	    leftContainer.add(emailTextField);
	    
	    y += 70;
	    JLabel activeLabel=new JLabel(service.get("userview.active"));
	    activeLabel.setBounds(50,y,150,30);
	    this.activeCheckbox=new JCheckBox();
	    activeCheckbox.setBounds(195,y,200,30);
	    leftContainer.add(activeLabel);
	    leftContainer.add(activeCheckbox);
	    
	    y = 150;
	    JLabel usernameLabel=new JLabel(service.get("userview.username"));
	    usernameLabel.setBounds(50,y,150,30);
	    this.usernameTextField=new JTextField();
	    usernameTextField.setBounds(200,y,300,30);
	    rightContainer.add(usernameLabel);
	    rightContainer.add(usernameTextField);
	    
	    y += 70;
	    JLabel passwordLabel=new JLabel(service.get("userview.password"));
	    passwordLabel.setBounds(50,y,150,30);
	    this.passwordField=new JPasswordField();
	    passwordField.setBounds(200,y,300,30);
	    rightContainer.add(passwordLabel);
	    rightContainer.add(passwordField);
	    
	    
	    container.add(leftContainer,BorderLayout.WEST);
	    container.add(rightContainer,BorderLayout.EAST);
	    
		container.add(buttonPanel,BorderLayout.SOUTH);
	
		panel.add(container, BorderLayout.CENTER);
        
        frame.getCenterPanel().removeAll();
	    frame.getCenterPanel().add(panel,BorderLayout.CENTER);
	    frame.getCenterPanel().revalidate();
	    frame.getCenterPanel().repaint();
	    frame.setVisible(true);
	}
	
	public JTextField getNameTextField() {
		return nameTextField;
	}

	public void setNameTextField(JTextField nameTextField) {
		this.nameTextField = nameTextField;
	}

	public JTextField getEmailTextField() {
		return emailTextField;
	}

	public void setEmailTextField(JTextField emailTextField) {
		this.emailTextField = emailTextField;
	}

	public JTextField getUsernameTextField() {
		return usernameTextField;
	}

	public void setUsernameTextField(JTextField usernameTextField) {
		this.usernameTextField = usernameTextField;
	}

	public JPasswordField getPasswordField() {
		return passwordField;
	}

	public void setPasswordField(JPasswordField passwordField) {
		this.passwordField = passwordField;
	}
	
	public JCheckBox getActiveCheckbox() {
		return activeCheckbox;
	}

	public void setActiveCheckbox(JCheckBox activeCheckbox) {
		this.activeCheckbox = activeCheckbox;
	}

	public void editUserView(UserModel userModel){
		UsersController usersController = (UsersController)pm.getService("UsersController");
		FrameController frameController = (FrameController)pm.getService("FrameController");
		Frame frame = frameController.getView();
		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(frame.getWidth()+frame.getConstCenterWidth()-250,frame.getHeight()+frame.getConstCenterHeight()-100));
		
		JPanel headerPanel = new JPanel(null);
		headerPanel.setPreferredSize(new Dimension(panel.getWidth()-50,100));
		
		TextMessages service = ApplicationContextProvider.getContext().getBean(TextMessages.class);
		
		JLabel label = new JLabel(service.get("userview.edituser"),SwingConstants.CENTER);
		label.setBounds((frame.getWidth()+frame.getConstCenterWidth()-250)/2-125,30,200,40);
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, 20));
        headerPanel.add(label);
        panel.add(headerPanel,BorderLayout.NORTH);
        
        //Content
        JPanel buttonPanel = new JPanel(null);
        
        JButton save = new JButton(service.get("userview.saveuser"));
        save.setActionCommand("saveuser");
        save.addActionListener(usersController);
        
        save.setBounds((frame.getWidth()+frame.getConstCenterWidth()-250)/2-125,30,200,40);
   
        buttonPanel.add(save);
        buttonPanel.setPreferredSize(new Dimension(panel.getWidth()-50,100));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JPanel container = new JPanel(new BorderLayout());
        container.setPreferredSize(new Dimension((frame.getWidth()+frame.getConstCenterWidth()-250),frame.getHeight()+frame.getConstCenterHeight()-100));
        JPanel leftContainer = new JPanel(null);
        leftContainer.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JPanel rightContainer = new JPanel(null);
        rightContainer.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        leftContainer.setPreferredSize(new Dimension((frame.getWidth()/2)+frame.getConstCenterWidth()-100,frame.getHeight()+frame.getConstCenterHeight()-100));
        rightContainer.setPreferredSize(new Dimension((frame.getWidth()/2)+frame.getConstCenterWidth()-50,frame.getHeight()+frame.getConstCenterHeight()-100));
		int y = 150;
		
		JLabel nameLabel=new JLabel(service.get("userview.name"));
		nameLabel.setBounds(50,y,150,30);
		this.nameTextField=new JTextField();
		nameTextField.setBounds(200,y,300,30);
		nameTextField.setText(userModel.getName());
		leftContainer.add(nameLabel);
		leftContainer.add(nameTextField);
	    
	    y += 70;
	    JLabel emailLabel=new JLabel(service.get("userview.email"));
	    emailLabel.setBounds(50,y,150,30);
	    this.emailTextField=new JTextField();
	    emailTextField.setBounds(200,y,300,30);
	    emailTextField.setText(userModel.getEmail());
	    leftContainer.add(emailLabel);
	    leftContainer.add(emailTextField);
	    
	    y += 70;
	    JLabel activeLabel=new JLabel(service.get("userview.active"));
	    activeLabel.setBounds(50,y,150,30);
	    this.activeCheckbox=new JCheckBox();
	    activeCheckbox.setBounds(195,y,200,30);
	    activeCheckbox.setSelected(userModel.getActive());
	    leftContainer.add(activeLabel);
	    leftContainer.add(activeCheckbox);
	    
	    y = 150;
	    JLabel usernameLabel=new JLabel(service.get("userview.username"));
	    usernameLabel.setBounds(50,y,150,30);
	    this.usernameTextField=new JTextField();
	    usernameTextField.setBounds(200,y,300,30);
	    usernameTextField.setText(userModel.getUsername());
	    rightContainer.add(usernameLabel);
	    rightContainer.add(usernameTextField);
	    
	    y += 70;
	    JLabel passwordLabel=new JLabel(service.get("userview.password"));
	    passwordLabel.setBounds(50,y,150,30);
	    this.passwordField=new JPasswordField();
	    passwordField.setBounds(200,y,300,30);
//	    passwordField.setText(userModel.getPassword());
	    rightContainer.add(passwordLabel);
	    rightContainer.add(passwordField);
	    
	    container.add(leftContainer,BorderLayout.WEST);
	    container.add(rightContainer,BorderLayout.EAST);
	    
		container.add(buttonPanel,BorderLayout.SOUTH);
	
		panel.add(container, BorderLayout.CENTER);
        
        frame.getCenterPanel().removeAll();
	    frame.getCenterPanel().add(panel,BorderLayout.CENTER);
	    frame.getCenterPanel().revalidate();
	    frame.getCenterPanel().repaint();
	    frame.setVisible(true);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
	}
}
