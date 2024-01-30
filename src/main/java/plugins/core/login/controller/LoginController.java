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
package plugins.core.login.controller;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import core.Companyware;
import models.user.UserModel;
import pluginmanager.plugininterfaces.PluginManager;
import pluginmanager.plugininterfaces.Service;
import plugins.core.frame.controller.FrameController;
import plugins.core.frame.view.Frame;
import plugins.core.login.view.LoginView;

public class LoginController implements ActionListener, Service, WindowListener, KeyListener{
	private static final Log log = LogFactory.getLog(LoginController.class);
	private LoginView view; 
	private FrameController frameController;
	private PluginManager pm;
	/**
	 * 
	 * 
	 */
	public LoginController(PluginManager pm) {
		pm.registerService("LoginController",this);
		frameController = (FrameController)pm.getService("FrameController");
		JFrame frame =  frameController.getView();
		this.view = new LoginView(pm, frame);
		this.view.setContent();
		this.pm = pm;
	}
	public LoginView getView() {
		return view;
	}
	public void setView(LoginView view) {
		this.view = view;
	}
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		Frame frame =  frameController.getView();
		frame.setVisible(false);
		frame.dispose();
		System.exit(0);
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand().toString();
		if(command.equals("Login")){
			String userName = this.view.getUserTextField().getText();
			char[] password = this.view.getPasswordField().getPassword();
			boolean checkUser = this.checkUser(userName, String.valueOf(password));
			if(checkUser){
				this.view.setVisible(false);
				this.view.dispose();
			}
			else{
				this.setFailureMessage();
			}
		}
		if(command.equals("Abbrechen")){
			Frame frame =  frameController.getView();
			frame.setVisible(false);
			frame.dispose();
			System.exit(0);
		}
		if(command.equals("showPassword")){
			if(this.view.getShowPassword().isSelected()){
				this.view.getPasswordField().setEchoChar((char) 0);
			}
			else{
				this.view.getPasswordField().setEchoChar('*');
			}
		}
	}

	public boolean checkUser(String userName, String password){
		try {
			UserModel user = models.user.Repository.getActiveUserByUsername(userName);
			if(user == null){
				return false;
			}
			if(Companyware.getContainer().encoder().matches(password, user.getPassword())){
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			String userName = this.view.getUserTextField().getText();
			char[] password = this.view.getPasswordField().getPassword();
			boolean checkUser = this.checkUser(userName, String.valueOf(password));
			if(checkUser){
				this.view.setVisible(false);
				this.view.dispose();
			}
			else{
				this.setFailureMessage();
			}
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	
	public void setFailureMessage(){
		FrameController frameController = (FrameController)pm.getService("FrameController");
		JPanel bottom = frameController.getView().getBottomPanel();
		String message = "Benutzer und/oder Passwort falsch!";
	    JLabel labelBottom = new JLabel(message);
	    JOptionPane.showMessageDialog(frameController.getView(), message, "Info", JOptionPane.INFORMATION_MESSAGE);
		bottom.setLayout(new FlowLayout(FlowLayout.LEFT));
		bottom.removeAll();
		bottom.add(labelBottom);
		bottom.revalidate();
		bottom.repaint();
	}
}
