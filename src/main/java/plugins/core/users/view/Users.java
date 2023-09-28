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

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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
	
	public Users(PluginManager pm) {
		this.pm = pm;
		this.createMenuEntry();
	}
	
	/**
	 * create menu entry "Pluginmanager"
	 */
	public void createMenuEntry(){
		UsersController usersController = (UsersController)pm.getService("UsersController");
		FrameController frameController = (FrameController)pm.getService("FrameController");
		Menu menuView = (Menu)pm.getService("MenuView");
		JMenu settings = menuView.getSettings();
		TextMessages service = ApplicationContextProvider.getContext().getBean(TextMessages.class);
		JMenuItem pluginManager = new JMenuItem(service.get("usermanager.menu"));
        pluginManager.addActionListener(usersController);
        settings.add(pluginManager);
	}
	
	public void createTableOverview(){
		UsersController pluginsController = (UsersController)pm.getService("UsersController");
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

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
	}
}
