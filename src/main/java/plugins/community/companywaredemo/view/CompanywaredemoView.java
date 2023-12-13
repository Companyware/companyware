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
package plugins.community.companywaredemo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
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
import pluginmanager.plugininterfaces.PluginManager;
import plugins.community.companywaredemo.controller.CompanywaredemoController;
import plugins.community.companywaredemo.controller.ButtonTableEditor;
import plugins.community.companywaredemo.controller.ImageButtonTableEditor;
import plugins.community.companywaredemo.model.DemoModel;
import plugins.community.companywaredemo.model.Repository;
import plugins.core.frame.controller.FrameController;
import plugins.core.frame.view.Frame;
import plugins.core.menu.view.Menu;
import plugins.core.plugins.model.DisplayableObjectTableModel;
import plugins.core.plugins.model.ObjectTableModel;
import plugins.core.users.controller.CheckboxCellEditor;
import plugins.core.users.view.ButtonColumnRenderer;
import plugins.core.users.view.CheckboxColumnRenderer;
import plugins.core.users.view.ImageButtonColumnRenderer;
import plugins.core.users.view.IntegerColumnRenderer;
import plugins.core.users.view.ObjectColumnRenderer;

public class CompanywaredemoView  implements Observer {
	private static final Log log = LogFactory.getLog(CompanywaredemoView.class);
	
	private PluginManager pm;
	
	public CompanywaredemoView(PluginManager pm) {
		this.pm = pm;
		this.createMenuEntry();
	}
	
	/**
	 * create menu entry "Companywaredemo"
	 */
	public void createMenuEntry(){
		CompanywaredemoController companywaredemoController = (CompanywaredemoController)pm.getService("CompanywaredemoController");
		Menu menuView = (Menu)pm.getService("MenuView");
		JMenu settings = menuView.getSettings();
		JMenuItem companywaredemo = new JMenuItem("Companywaredemo");
        companywaredemo.addActionListener(companywaredemoController);
        settings.add(companywaredemo);
	}
	
	public void createTableOverview(){
		FrameController frameController = (FrameController)pm.getService("FrameController");
		Frame frame = frameController.getView();
		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(frame.getWidth()+frame.getConstCenterWidth()-50,frame.getHeight()+frame.getConstCenterHeight()-100));
		JLabel label = new JLabel("Demoübersicht",SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(frame.getWidth()+frame.getConstCenterWidth()-50,50));
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, 20));
        panel.add(label,BorderLayout.NORTH);
        List<DemoModel> demoUsers=Repository.getDemoUsers();
        
        UIManager.put("Table.alternateRowColor", Color.white);
        
        ObjectTableModel<DemoModel> tableModel = new DisplayableObjectTableModel<>(DemoModel.class);
        tableModel.setObjectRows(demoUsers);
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
	
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
	}
}
