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
package plugins.core.users.controller;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import core.Companyware;
import models.user.UserModel;
import pluginmanager.plugininterfaces.PluginManager;
import pluginmanager.plugininterfaces.Service;
import models.user.Repository;
import plugins.core.frame.controller.FrameController;
import plugins.core.frame.view.Frame;
import plugins.core.frame.view.JOutlookBar;
import plugins.core.users.view.Users;

public class UsersController extends Observable implements Service, MouseListener, ActionListener{

	private static final Log log = LogFactory.getLog(UsersController.class);
	private UserModel model;
	private Users view;
	private PluginManager pm;
	private String username;
	
	public UsersController(PluginManager pm) {
		this.pm = pm;
		this.model = new UserModel();
		this.pm.registerService("UsersController",this);
		view = new Users(pm);
		model.addObserver(view);
	}
	
	@Override
	public void mouseClicked(MouseEvent event) {
		JLabel label = (JLabel) event.getSource();
		
		switch (label.getName()) {
			case "useroverview":{
				this.createUsersOverview();
				break;
			}
			case "adduser":{
				this.createAddUserview();
				break;
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent event) {
		JLabel label = (JLabel) event.getSource();
		label.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	@Override
	public void mouseExited(MouseEvent event) {
		JLabel label = (JLabel) event.getSource();
		label.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		label.setBorder(BorderFactory.createEmptyBorder());
	}

	@Override
	public void mousePressed(MouseEvent event) {
		// TODO Auto-generated method stub
		JLabel label = (JLabel) event.getSource();
		label.setBorder(BorderFactory.createLoweredBevelBorder());
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		// TODO Auto-generated method stub
		JLabel label = (JLabel) event.getSource();
		label.setBorder(BorderFactory.createEmptyBorder());
	}

	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e) {
		log.info("test action userscontroller");
		String command = e.getActionCommand().toString();
		UserModel user = null;
		this.model.printOut("ACTION: " + e.getActionCommand().toString());
		
		if(command.equals("adduser")){
			user = new UserModel();
		}
		if(command.equals("saveuser")){
			user = Repository.getUserByUsername(this.username);
		}
		if(command.equals("adduser")||command.equals("saveuser")){
			if(user != null){
				user.setUsername(view.getUsernameTextField().getText());
				user.setName(view.getNameTextField().getText());
				user.setEmail(view.getEmailTextField().getText());
				user.setActive(view.getActiveCheckbox().isSelected());
				char[] pw = view.getPasswordField().getPassword();
				String password = new String(pw);
				String encryptedPassword = this.encoder().encode(password);
				if(!password.isEmpty() && password != null){
					user.setPassword(encryptedPassword);
				}
				Repository.save(user);
				Companyware.getContainer().setSuccessMessage("Benutzer gespeichert!", pm);
			}
		}
		if(command.equals("Benutzerverwaltung")){
	    		this.createUsersOverview();
	    		FrameController frameController = (FrameController)pm.getService("FrameController");
	    		Frame frameView = (Frame)frameController.getView();
	    		JOutlookBar outlookBar = frameView.getOutlookBar();
	    		outlookBar.setVisibleBar(1);
		}
	}
	
	public void setUser(String username){
		this.username = username;
		model = Repository.getUserByUsername(username);
	}
	
	public void createUsersOverview(){
		view.createTableOverview();
	}
	
	public void createAddUserview(){
		view.addUserView();
	}
	
	public void editCustomerView(){
		view.editUserView(model);
	}
	
	public BCryptPasswordEncoder encoder() {
	    return new BCryptPasswordEncoder();
	}
}
