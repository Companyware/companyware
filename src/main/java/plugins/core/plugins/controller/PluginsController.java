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
package plugins.core.plugins.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import plugins.core.plugins.model.*;
import pluginmanager.PluginManagerImpl;
import pluginmanager.plugininterfaces.PluginManager;
import pluginmanager.plugininterfaces.Service;
import plugins.core.plugins.view.Plugins;

public class PluginsController implements ActionListener, Service{

	private static final Log log = LogFactory.getLog(PluginsController.class);
	private PluginsModel model;
	private Plugins view;
	
	public PluginsController(PluginManager pm) {
		this.model = new PluginsModel();
		pm.registerService("PluginsController",this);
		view = new Plugins(pm);
		model.addObserver(view);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		log.info("test action pluginscontroller");
		this.model.printOut("ACTION pluginscontroller: " + e.getActionCommand().toString());
		switch(e.getActionCommand().toString()) {
	    case "Pluginmanager":
	    	{
	    		this.createPluginManagerOverview();
	    	}
		}
	}
	
	public void createPluginManagerOverview(){
		view.createTableOverview();
	}
}
