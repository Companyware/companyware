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
package plugins.core.menu.view;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import core.ApplicationContextProvider;
import core.TextMessages;
import pluginmanager.plugininterfaces.PluginManager;
import pluginmanager.plugininterfaces.Service;
import plugins.core.frame.controller.FrameController;
import plugins.core.menu.controller.MenuController;

public class Menu  implements Observer,Service {

	private JMenuBar menubar;
	private JMenu file;
	private JMenu settings;
	private JMenu help;
	private JMenuItem open;
	private JMenuItem finish;
	private JMenuItem faq;
	private JMenuItem about;
	private static final Log log = LogFactory.getLog(Menu.class);

	public Menu(PluginManager pm) {
		MenuController menuController = (MenuController)pm.getService("MenuController");
		FrameController frameController = (FrameController)pm.getService("FrameController");
		pm.registerService("MenuView", this);
        menubar = new JMenuBar();
        
        TextMessages service = ApplicationContextProvider.getContext().getBean(TextMessages.class);
  
        file = new JMenu(service.get("menu.file"));
        settings = new JMenu(service.get("menu.settings"));
        help = new JMenu(service.get("menu.help"));
        
        finish = new JMenuItem(service.get("menu.exit"));
        finish.setActionCommand("exit");
        finish.addActionListener(menuController);
      
        about = new JMenuItem(service.get("menu.about"));
        about.addActionListener(menuController);
        
        menubar.add(file);
        menubar.add(settings);
        menubar.add(help);
        
        file.add(finish);
        help.add(about);
        
        frameController.getView().add(menubar,BorderLayout.NORTH);
	}

	public JMenu getSettings() {
		return settings;
	}

	public void setSettings(JMenu settings) {
		this.settings = settings;
	}

	public JMenuBar getMenubar() {
		return menubar;
	}

	public void setMenubar(JMenuBar menubar) {
		this.menubar = menubar;
	}

	public JMenu getFile() {
		return file;
	}

	public void setFile(JMenu file) {
		this.file = file;
	}

	public JMenu getHelp() {
		return help;
	}

	public void setHelp(JMenu help) {
		this.help = help;
	}

	public JMenuItem getOpen() {
		return open;
	}

	public void setOpen(JMenuItem open) {
		this.open = open;
	}

	public JMenuItem getFinish() {
		return finish;
	}

	public void setFinish(JMenuItem finish) {
		this.finish = finish;
	}

	public JMenuItem getFaq() {
		return faq;
	}

	public void setFaq(JMenuItem faq) {
		this.faq = faq;
	}

	public JMenuItem getAbout() {
		return about;
	}

	public void setAbout(JMenuItem about) {
		this.about = about;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub	
	}
}
