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
package plugins.core.plugins.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import core.ApplicationContextProvider;
import core.TextMessages;
import models.plugin.PluginModel;
import models.plugin.Repository;
import pluginmanager.plugininterfaces.PluginManager;
import plugins.core.frame.controller.FrameController;
import plugins.core.frame.view.Frame;
import plugins.core.menu.view.Menu;
import plugins.core.plugins.controller.ButtonTableEditor;
import plugins.core.plugins.controller.CheckboxCellEditor;
import plugins.core.plugins.controller.PluginsController;
import plugins.core.plugins.model.DisplayableObjectTableModel;
import plugins.core.plugins.model.ObjectTableModel;
import plugins.core.users.view.ButtonColumnRenderer;
import plugins.core.users.view.ImageButtonColumnRenderer;
import plugins.core.plugins.controller.ImageButtonTableEditor;

public class Plugins  implements Observer {
	private static final Log log = LogFactory.getLog(Plugins.class);
	
	private PluginManager pm;
	
	public Plugins(PluginManager pm) {
		this.pm = pm;
		this.createMenuEntry();
	}
	
	/**
	 * create menu entry "Pluginmanager"
	 */
	public void createMenuEntry(){
		PluginsController pluginsController = (PluginsController)pm.getService("PluginsController");
		FrameController frameController = (FrameController)pm.getService("FrameController");
		Menu menuView = (Menu)pm.getService("MenuView");
		JMenu settings = menuView.getSettings();
		TextMessages service = ApplicationContextProvider.getContext().getBean(TextMessages.class);
	        
		JMenuItem pluginManager = new JMenuItem(service.get("pluginmanager.menu"));
        pluginManager.addActionListener(pluginsController);
        settings.add(pluginManager);
	}
	
	public void createTableOverview(){
		PluginsController pluginsController = (PluginsController)pm.getService("PluginsController");
		FrameController frameController = (FrameController)pm.getService("FrameController");
		Frame frame = frameController.getView();
		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(frame.getWidth()+frame.getConstCenterWidth()-50,frame.getHeight()+frame.getConstCenterHeight()-100));
		JButton fileButton = new JButton("Plugin installieren");
		URL url = getClass().getResource("/upload3.png");
    	BufferedImage img = null;
		try {
			img = ImageIO.read(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	ImageIcon icon = new ImageIcon(img);
		fileButton.setIcon(icon);
		fileButton.setPreferredSize(new Dimension(200,50));
		fileButton.setActionCommand("installPlugin");
	    fileButton.addActionListener(pluginsController);
		JLabel label = new JLabel("Pluginmanager",SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(frame.getWidth()+frame.getConstCenterWidth()-50,50));
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, 20));
        JPanel filePanel = new JPanel(new BorderLayout());
        JPanel layoutPanel = new JPanel(new BorderLayout());
        filePanel.add(fileButton, BorderLayout.EAST);
        layoutPanel.add(filePanel, BorderLayout.NORTH);
        layoutPanel.add(label,BorderLayout.SOUTH);
        panel.add(layoutPanel,BorderLayout.NORTH);
        List<PluginModel> plugins=Repository.getCommunityPlugins();
        
        ObjectTableModel<PluginModel> tableModel = new DisplayableObjectTableModel<>(PluginModel.class);
        tableModel.setObjectRows(plugins);
        JTable table = new JTable(tableModel){
			private static final long serialVersionUID = 1L;

			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                 Component comp = super.prepareRenderer(renderer, row, column);
                 Color alternateColor = new Color(200, 201, 210);
                 Color whiteColor = Color.WHITE;
                 if(!comp.getBackground().equals(getSelectionBackground())) {
                    Color c = (row % 2 != 0 ? alternateColor : whiteColor);
                    comp.setBackground(c);
                    c = null;
                 }
                 return comp;
              }
        };
	    table.setDefaultRenderer(Object.class, new ObjectColumnRenderer());
	    table.setDefaultRenderer(Integer.class, new IntegerColumnRenderer());
        table.setDefaultRenderer(Boolean.class, new CheckboxColumnRenderer());
        table.setDefaultRenderer(JCheckBox.class, new ButtonColumnRenderer("Edit"));
        table.setDefaultRenderer(JButton.class, new ImageButtonColumnRenderer("Delete"));

        JCheckBox imageButton = new JCheckBox();
        imageButton.setActionCommand("edit");
        imageButton.setHorizontalAlignment(JCheckBox.CENTER);
        table.setDefaultEditor(JButton.class, new ImageButtonTableEditor(imageButton,pm));
        
        tableModel.addTableModelListener((e)->{table.repaint();});
	    
	    JCheckBox checkBox = new JCheckBox();
	    checkBox.setActionCommand("active");
        checkBox.setHorizontalAlignment(JCheckBox.CENTER);
        table.setDefaultEditor(Boolean.class,new CheckboxCellEditor(checkBox,pm));
        
        JCheckBox button = new JCheckBox();
        button.setActionCommand("settings button");
        button.setHorizontalAlignment(JCheckBox.CENTER);
        table.setDefaultEditor(JCheckBox.class, new ButtonTableEditor(button,pm));
	    
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
