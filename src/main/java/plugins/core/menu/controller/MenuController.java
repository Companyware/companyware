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
package plugins.core.menu.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import core.ApplicationContextProvider;
import core.TextMessages;
import plugins.core.frame.controller.FrameController;
import plugins.core.info.controller.InfoController;
import plugins.core.login.controller.LoginController;
import plugins.core.menu.model.*;
import pluginmanager.plugininterfaces.PluginManager;
import pluginmanager.plugininterfaces.Service;
import plugins.core.menu.view.Menu;

public class MenuController implements ActionListener, Service{

	private MenuModel model;
	private PluginManager pm;
	
	private static final Log log = LogFactory.getLog(MenuController.class);

	public MenuController(PluginManager pm) {
		this.model = new MenuModel();
		this.pm = pm;
		pm.registerService("MenuController",this);
		Menu view = new Menu(pm);
		model.addObserver(view);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.model.printOut("menu ACTION: " + e.getActionCommand().toString());
		String command = e.getActionCommand().toString();
		if(command.equals("exit")){
			FrameController frameController = (FrameController)pm.getService("FrameController");
			if(frameController!=null){
				frameController.getView().setVisible(false);
				System.exit(0);
			}
		}
		
		TextMessages service = ApplicationContextProvider.getContext().getBean(TextMessages.class);
		
		if(command.equals(service.get("menu.about"))){
			InfoController infoController = (InfoController)pm.getService("InfoController");
			infoController.getView().setVisible(true);
		}
	}
}