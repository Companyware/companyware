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
package plugins.community.mbduscustomer.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
import plugins.community.mbduscustomer.model.Repository;
import plugins.community.mbduscustomer.model.CustomerModel;
import pluginmanager.plugininterfaces.PluginManager;
import plugins.community.mbduscustomer.controller.MbdusCustomerController;
import plugins.community.plugin1.view.Plugin1View;
import plugins.core.frame.controller.FrameController;
import plugins.core.frame.view.Frame;
import plugins.core.frame.view.JOutlookBar;
import plugins.core.plugins.model.DisplayableObjectTableModel;
import plugins.core.plugins.model.ObjectTableModel;
import plugins.community.mbduscustomer.controller.ButtonTableEditor;
import plugins.core.users.controller.CheckboxCellEditor;
import plugins.community.mbduscustomer.controller.ImageButtonTableEditor;
import plugins.core.users.view.ButtonColumnRenderer;
import plugins.core.users.view.CheckboxColumnRenderer;
import plugins.core.users.view.ImageButtonColumnRenderer;
import plugins.core.users.view.IntegerColumnRenderer;
import plugins.core.users.view.ObjectColumnRenderer;

public class MbdusCustomerView implements Observer{
	private static final Log log = LogFactory.getLog(Plugin1View.class);
	
	private PluginManager pm;
	private static final Insets WEST_INSETS = new Insets(5, 0, 5, 5);
    private static final Insets EAST_INSETS = new Insets(5, 5, 5, 0);
    
    private JTextField salutationTextField;
    private JTextField firstnameTextField;
    private JTextField nameTextField;
    private JTextField emailTextField;
    private JTextField streetTextField;
    private JTextField streetnumberTextField;
    private JTextField zipcodeTextField;
    private JTextField cityTextField;
    private JTextField countryTextField;

	public MbdusCustomerView(PluginManager pm) {
		this.pm = pm;
		this.addOutlookBarEntry();
	}
	
	public void addOutlookBarEntry(){
		FrameController frameController = (FrameController)pm.getService("FrameController");
		Frame frameView = (Frame)frameController.getView();
		JOutlookBar outlookBar = frameView.getOutlookBar();
		MbdusCustomerController mbdusCustomerController = (MbdusCustomerController)pm.getService("MbdusCustomer");
		ImageIcon useradd = new ImageIcon(this.getClass().getResource("/images/user_add.png"));
		ImageIcon users = new ImageIcon(this.getClass().getResource("/images/users.gif"));
		outlookBar.addBarEntry("customer");
		outlookBar.addBarEntryIcon("customer","addcustomer", useradd, mbdusCustomerController);
		outlookBar.addBarEntryIcon("customer","customeroverview", users, mbdusCustomerController);
		outlookBar.addBar("customer", outlookBar.getPanel("customer"), 100);
		outlookBar.setPreferredSize();
	}
	
	public void addTopPanelEntry(){
		
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
	
	public void createTableOverview(){
		FrameController frameController = (FrameController)pm.getService("FrameController");
		Frame frame = frameController.getView();
		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(frame.getWidth()+frame.getConstCenterWidth()-50,frame.getHeight()+frame.getConstCenterHeight()-100));
		JLabel label = new JLabel("Kundenübersicht",SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(frame.getWidth()+frame.getConstCenterWidth()-50,50));
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, 20));
        panel.add(label,BorderLayout.NORTH);
        List<CustomerModel> customers=Repository.getCustomers();
        
        UIManager.put("Table.alternateRowColor", Color.white);
        
        ObjectTableModel<CustomerModel> tableModel = new DisplayableObjectTableModel<>(CustomerModel.class);
        tableModel.setObjectRows(customers);
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
        colModel.getColumn(0).setPreferredWidth(50);
        
	    table.setModel(tableModel);
	    
	    table.setRowHeight(35);
	    
	    panel.add(new JScrollPane(table),BorderLayout.CENTER);
	    frame.getCenterPanel().removeAll();
	    frame.getCenterPanel().add(panel,BorderLayout.CENTER);
	    frame.getCenterPanel().revalidate();
	    frame.getCenterPanel().repaint();
	    frame.setVisible(true);
	}
	
	public void addCustomerView(){
		MbdusCustomerController mbdusCustomerController = (MbdusCustomerController)pm.getService("MbdusCustomer");
		FrameController frameController = (FrameController)pm.getService("FrameController");
		Frame frame = frameController.getView();
		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(frame.getWidth()+frame.getConstCenterWidth()-250,frame.getHeight()+frame.getConstCenterHeight()-100));
		
		JPanel headerPanel = new JPanel(null);
		headerPanel.setPreferredSize(new Dimension(panel.getWidth()-50,100));
		
		JLabel label = new JLabel("Kunden hinzufügen",SwingConstants.CENTER);
		label.setBounds((frame.getWidth()+frame.getConstCenterWidth()-250)/2-125,30,200,40);
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, 20));
        headerPanel.add(label);
        panel.add(headerPanel,BorderLayout.NORTH);
        
        //Content
        JPanel buttonPanel = new JPanel(null);
	    
	    TextMessages service = ApplicationContextProvider.getContext().getBean(TextMessages.class);
        
        JButton save = new JButton(service.get("mbduscontroller.createbutton"));
        save.setActionCommand("addcustomer");
        save.addActionListener(mbdusCustomerController);
        
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
        
        JLabel salutationLabel=new JLabel(service.get("customerview.salutation"));
	    salutationLabel.setBounds(50,y,150,30);
	    this.salutationTextField=new JTextField();
	    salutationTextField.setBounds(200,y,300,30);
	    leftContainer.add(salutationLabel);
	    leftContainer.add(salutationTextField);
        
	    y += 70;
        JLabel firstnameLabel=new JLabel(service.get("customerview.firstname"));
	    firstnameLabel.setBounds(50,y,150,30);
	    this.firstnameTextField=new JTextField();
	    firstnameTextField.setBounds(200,y,300,30);
	    leftContainer.add(firstnameLabel);
	    leftContainer.add(firstnameTextField);
	    
	    y += 70;
	    JLabel nameLabel=new JLabel(service.get("customerview.name"));
	    nameLabel.setBounds(50,y,150,30);
	    this.nameTextField=new JTextField();
	    nameTextField.setBounds(200,y,300,30);
	    leftContainer.add(nameLabel);
	    leftContainer.add(nameTextField);
	    
	    y += 70;
	    JLabel emailLabel=new JLabel(service.get("customerview.email"));
	    emailLabel.setBounds(50,y,150,30);
	    this.emailTextField=new JTextField();
	    emailTextField.setBounds(200,y,300,30);
	    leftContainer.add(emailLabel);
	    leftContainer.add(emailTextField);
	    
	    y = 150;
	    JLabel streetLabel=new JLabel(service.get("customerview.street"));
	    streetLabel.setBounds(50,y,150,30);
	    this.streetTextField=new JTextField();
	    streetTextField.setBounds(200,y,300,30);
	    rightContainer.add(streetLabel);
	    rightContainer.add(streetTextField);
	    
	    y += 70;
	    JLabel streetnumberLabel=new JLabel(service.get("customerview.streetnumber"));
	    streetnumberLabel.setBounds(50,y,150,30);
	    this.streetnumberTextField=new JTextField();
	    streetnumberTextField.setBounds(200,y,300,30);
	    rightContainer.add(streetnumberLabel);
	    rightContainer.add(streetnumberTextField);
	    
	    y += 70;
	    JLabel zipcodeLabel=new JLabel(service.get("customerview.zipcode"));
	    zipcodeLabel.setBounds(50,y,150,30);
	    this.zipcodeTextField=new JTextField();
	    zipcodeTextField.setBounds(200,y,300,30);
	    rightContainer.add(zipcodeLabel);
	    rightContainer.add(zipcodeTextField);
	    
	    y += 70;
	    JLabel cityLabel=new JLabel(service.get("customerview.city"));
	    cityLabel.setBounds(50,y,150,30);
	    this.cityTextField=new JTextField();
	    cityTextField.setBounds(200,y,300,30);
	    rightContainer.add(cityLabel);
	    rightContainer.add(cityTextField);
	    
	    y += 70;
	    JLabel countryLabel=new JLabel(service.get("customerview.country"));
	    countryLabel.setBounds(50,y,150,30);
	    this.countryTextField=new JTextField();
	    countryTextField.setBounds(200,y,300,30);
	    rightContainer.add(countryLabel);
	    rightContainer.add(countryTextField);
	    
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
	
	public void editCustomerView(CustomerModel customerModel){
		MbdusCustomerController mbdusCustomerController = (MbdusCustomerController)pm.getService("MbdusCustomer");
		FrameController frameController = (FrameController)pm.getService("FrameController");
		Frame frame = frameController.getView();
		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(frame.getWidth()+frame.getConstCenterWidth()-250,frame.getHeight()+frame.getConstCenterHeight()-100));
		
		JPanel headerPanel = new JPanel(null);
		headerPanel.setPreferredSize(new Dimension(panel.getWidth()-50,100));
		
		JLabel label = new JLabel("Kunden bearbeiten",SwingConstants.CENTER);
		label.setBounds((frame.getWidth()+frame.getConstCenterWidth()-250)/2-125,30,200,40);
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, 20));
        headerPanel.add(label);
        panel.add(headerPanel,BorderLayout.NORTH);
        
        //Content
        JPanel buttonPanel = new JPanel(null);
	    
	    TextMessages service = ApplicationContextProvider.getContext().getBean(TextMessages.class);
        
        JButton save = new JButton(service.get("mbduscontroller.savebutton"));
        save.setActionCommand("savecustomer");
        save.addActionListener(mbdusCustomerController);
        
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
        
        JLabel salutationLabel=new JLabel(service.get("customerview.salutation"));
	    salutationLabel.setBounds(50,y,150,30);
	    this.salutationTextField=new JTextField();
	    salutationTextField.setBounds(200,y,300,30);
	    getSalutationTextField().setText(customerModel.getSalutation());
	    leftContainer.add(salutationLabel);
	    leftContainer.add(salutationTextField);
        
	    y += 70;
        JLabel firstnameLabel=new JLabel(service.get("customerview.firstname"));
	    firstnameLabel.setBounds(50,y,150,30);
	    this.firstnameTextField=new JTextField();
	    firstnameTextField.setBounds(200,y,300,30);
	    firstnameTextField.setText(customerModel.getFirstname());
	    leftContainer.add(firstnameLabel);
	    leftContainer.add(firstnameTextField);
	    
	    y += 70;
	    JLabel nameLabel=new JLabel(service.get("customerview.name"));
	    nameLabel.setBounds(50,y,150,30);
	    this.nameTextField=new JTextField();
	    nameTextField.setBounds(200,y,300,30);
	    nameTextField.setText(customerModel.getName());
	    leftContainer.add(nameLabel);
	    leftContainer.add(nameTextField);
	    
	    y += 70;
	    JLabel emailLabel=new JLabel(service.get("customerview.email"));
	    emailLabel.setBounds(50,y,150,30);
	    this.emailTextField=new JTextField();
	    emailTextField.setBounds(200,y,300,30);
	    emailTextField.setText(customerModel.getEmail());
	    leftContainer.add(emailLabel);
	    leftContainer.add(emailTextField);
	    
	    y = 150;
	    JLabel streetLabel=new JLabel(service.get("customerview.street"));
	    streetLabel.setBounds(50,y,150,30);
	    this.streetTextField=new JTextField();
	    streetTextField.setBounds(200,y,300,30);
	    streetTextField.setText(customerModel.getStreet());
	    rightContainer.add(streetLabel);
	    rightContainer.add(streetTextField);
	    
	    y += 70;
	    JLabel streetnumberLabel=new JLabel(service.get("customerview.streetnumber"));
	    streetnumberLabel.setBounds(50,y,150,30);
	    this.streetnumberTextField=new JTextField();
	    streetnumberTextField.setBounds(200,y,300,30);
	    streetnumberTextField.setText(customerModel.getStreetnumber());
	    rightContainer.add(streetnumberLabel);
	    rightContainer.add(streetnumberTextField);
	    
	    y += 70;
	    JLabel zipcodeLabel=new JLabel(service.get("customerview.zipcode"));
	    zipcodeLabel.setBounds(50,y,150,30);
	    this.zipcodeTextField=new JTextField();
	    zipcodeTextField.setBounds(200,y,300,30);
	    zipcodeTextField.setText(customerModel.getZipcode());
	    rightContainer.add(zipcodeLabel);
	    rightContainer.add(zipcodeTextField);
	    
	    y += 70;
	    JLabel cityLabel=new JLabel(service.get("customerview.city"));
	    cityLabel.setBounds(50,y,150,30);
	    this.cityTextField=new JTextField();
	    cityTextField.setBounds(200,y,300,30);
	    cityTextField.setText(customerModel.getCity());
	    rightContainer.add(cityLabel);
	    rightContainer.add(cityTextField);
	    
	    y += 70;
	    JLabel countryLabel=new JLabel(service.get("customerview.country"));
	    countryLabel.setBounds(50,y,150,30);
	    this.countryTextField=new JTextField();
	    countryTextField.setBounds(200,y,300,30);
	    countryTextField.setText(customerModel.getCountry());
	    rightContainer.add(countryLabel);
	    rightContainer.add(countryTextField);
	    
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
	
	public JTextField getSalutationTextField() {
		return salutationTextField;
	}

	public void setSalutationTextField(JTextField salutationTextField) {
		this.salutationTextField = salutationTextField;
	}

	public JTextField getFirstnameTextField() {
		return firstnameTextField;
	}

	public void setFirstnameTextField(JTextField firstnameTextField) {
		this.firstnameTextField = firstnameTextField;
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

	public JTextField getStreetTextField() {
		return streetTextField;
	}

	public void setStreetTextField(JTextField streetTextField) {
		this.streetTextField = streetTextField;
	}

	public JTextField getStreetnumberTextField() {
		return streetnumberTextField;
	}

	public void setStreetnumberTextField(JTextField streetnumberTextField) {
		this.streetnumberTextField = streetnumberTextField;
	}

	public JTextField getZipcodeTextField() {
		return zipcodeTextField;
	}

	public void setZipcodeTextField(JTextField zipcodeTextField) {
		this.zipcodeTextField = zipcodeTextField;
	}

	public JTextField getCityTextField() {
		return cityTextField;
	}

	public void setCityTextField(JTextField cityTextField) {
		this.cityTextField = cityTextField;
	}
	
	public JTextField getCountryTextField() {
		return countryTextField;
	}

	public void setCountryTextField(JTextField countryTextField) {
		this.countryTextField = countryTextField;
	}
}
