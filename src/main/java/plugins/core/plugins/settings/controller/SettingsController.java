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
package plugins.core.plugins.settings.controller;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;
import core.Companyware;
import models.pluginsettings.Repository;
import models.pluginsettings.PluginSettingsModel;
import plugins.core.frame.controller.FrameController;
import plugins.core.plugins.settings.model.SettingsModel;
import plugins.core.plugins.settings.view.SettingsDialog;
import pluginmanager.plugininterfaces.PluginManager;
import pluginmanager.plugininterfaces.Service;

public class SettingsController implements ActionListener, Service, WindowListener{

	private static final Log log = LogFactory.getLog(SettingsController.class);
	private SettingsModel model;

	private SettingsDialog view;
	private String pluginName;
	private FrameController frameController;

	public SettingsController(PluginManager pm) {
		this.model = new SettingsModel();
		pm.registerService("SettingsController",this);
		frameController = (FrameController)pm.getService("FrameController");
		JFrame frame =  frameController.getView();
		view = new SettingsDialog(pm, frame);
		model.addObserver(view);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		log.info("test action settingscontroller");
		this.model.printOut("S ACTION: " + pluginName + e.getActionCommand().toString());
		String command = e.getActionCommand().toString();
		if(command.equals("save")){
			for (Component component : view.getPanel().getComponents()) {
				if (component instanceof JPanel) {
					for (Component insideComponent : ((JPanel)component).getComponents()) {
			            if (insideComponent instanceof JTextField) {
			            	PluginSettingsModel pluginSettingsModel = Repository.getSettingById(Long.parseLong(((JTextField) insideComponent).getName()));
			            	pluginSettingsModel.setValue(((JTextField) insideComponent).getText());
			                Repository.save(pluginSettingsModel);
			            }
					}
				}
	        }
			JPanel panel = this.view.getMessagePanel();
			panel.setLayout(new FlowLayout(FlowLayout.LEFT));
			JLabel label = new JLabel("Erfolgreich gespeichert!");
			panel.removeAll();
			panel.add(label);
			panel.revalidate();
			
			JPanel bottom = frameController.getView().getBottomPanel();
		    JLabel labelBottom = new JLabel("Erfolgreich gespeichert!");
			bottom.setLayout(new FlowLayout(FlowLayout.LEFT));
			bottom.removeAll();
			bottom.add(labelBottom);
			bottom.revalidate();
		}
		if(command.equals("cancel")){
			this.view.dispose();
			JPanel bottom = frameController.getView().getBottomPanel();
			bottom.removeAll();
			bottom.revalidate();
			bottom.repaint();
		}
	}

	public SettingsDialog getView() {
		return view;
	}

	public void setView(SettingsDialog view) {
		this.view = view;
	}
	
	public void readXmlSettings() throws ParserConfigurationException, SAXException, IOException{
		List<Object[]> settings = Companyware.getContainer().getPluginSettings(pluginName);
		this.view.setContent(settings);
	}
	
	
	public String getPluginName() {
		return pluginName;
	}

	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}
	
	public SettingsModel getModel() {
		return model;
	}

	public void setModel(SettingsModel model) {
		this.model = model;
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		JPanel bottom = frameController.getView().getBottomPanel();
		bottom.removeAll();
		bottom.revalidate();
		bottom.repaint();
	}

	@Override
	public void windowClosing(WindowEvent e) {
		JPanel bottom = frameController.getView().getBottomPanel();
		bottom.removeAll();
		bottom.revalidate();
		bottom.repaint();	
	}
}
