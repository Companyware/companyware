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
package plugins.community.plugin1.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import models.plugin.PluginModel;
import models.plugin.Repository;
import pluginmanager.plugininterfaces.PluginManager;
import pluginmanager.plugininterfaces.Service;
import plugins.core.frame.controller.FrameController;
import plugins.core.frame.view.Frame;
import plugins.core.menu.view.Menu;
import plugins.core.plugins.controller.ButtonTableEditor;
import plugins.core.plugins.controller.PluginsController;
import plugins.core.plugins.model.DisplayableObjectTableModel;
import plugins.core.plugins.model.ObjectTableModel;

public class Plugin1View  implements Observer {
	private static final Log log = LogFactory.getLog(Plugin1View.class);
	
	private PluginManager pm;
	
	public Plugin1View(PluginManager pm) {
		this.pm = pm;
		this.createMenuEntry();
	}
	
	/**
	 * create menu entry "Plugin1"
	 */
	public void createMenuEntry(){
		PluginsController pluginsController = (PluginsController)pm.getService("PluginsController");
		FrameController frameController = (FrameController)pm.getService("FrameController");
		Menu menuView = (Menu)pm.getService("MenuView");
		JMenu settings = menuView.getSettings();
		JMenuItem plugin1 = new JMenuItem("Plugin1");
        plugin1.addActionListener(pluginsController);
        settings.add(plugin1);
	}
	

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
	}
}
