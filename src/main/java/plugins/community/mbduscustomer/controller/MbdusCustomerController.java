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
package plugins.community.mbduscustomer.controller;

import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import core.Companyware;
import models.plugin.PluginModel;
import pluginmanager.plugininterfaces.PluginManager;
import pluginmanager.plugininterfaces.Service;
import plugins.community.mbduscustomer.model.CustomerModel;
import plugins.community.mbduscustomer.model.Repository;
import plugins.community.mbduscustomer.view.MbdusCustomerView;
import plugins.core.frame.controller.FrameController;
import plugins.core.frame.view.Frame;
import plugins.core.plugins.model.PluginsModel;

public class MbdusCustomerController extends Observable implements Service, MouseListener, ActionListener{
	private static final Log log = LogFactory.getLog(MbdusCustomerController.class);
	private PluginManager pm;
	private CustomerModel model;
	private MbdusCustomerView view;
	private String email;
	
	public MbdusCustomerController(PluginManager pm) {
		this.pm = pm;
		this.model = new CustomerModel();
		this.pm.registerService("MbdusCustomer",this);
		this.view = new MbdusCustomerView(pm);
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		JLabel label = (JLabel) event.getSource();
		
		switch (label.getName()) {
			case "customeroverview":{
				this.createCustomersOverview();
				break;
			}
			case "addcustomer":{
				this.createAddCustomerview();
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
	
	public void createCustomersOverview(){
		view.createTableOverview();
	}
	
	public void createAddCustomerview(){
		view.addCustomerView();
	}
	
	public void editCustomerView(){
		view.editCustomerView(model);
	}
	
	public void deleteCustomer(){
		Repository.deleteByEmail(this.email);

		view.createTableOverview();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand().toString();
		CustomerModel customer = null;
		if(command.equals("addcustomer")){
			customer = new CustomerModel();
		}
		if(command.equals("savecustomer")){
			customer = Repository.getCustomerByEmail(this.email);
		}
		if(command.equals("addcustomer")||command.equals("savecustomer")){
			if(customer != null){
				customer.setSalutation(view.getSalutationTextField().getText());
				customer.setFirstname(view.getFirstnameTextField().getText());
				customer.setName(view.getNameTextField().getText());
				customer.setEmail(view.getEmailTextField().getText());
				customer.setStreet(view.getStreetTextField().getText());
				customer.setStreetnumber(view.getStreetnumberTextField().getText());
				customer.setZipcode(view.getZipcodeTextField().getText());
				customer.setCity(view.getCityTextField().getText());
				customer.setCountry(view.getCountryTextField().getText());
				Repository.save(customer);
				Companyware.getContainer().setSuccessMessage("Kunde gespeichert!", pm);
			}
		}
	}
	
	public void setCustomer(String email){
		this.email = email;
		model = Repository.getCustomerByEmail(email);
	}

	public CustomerModel getModel() {
		return model;
	}

	public void setModel(CustomerModel model) {
		this.model = model;
	}

	public MbdusCustomerView getView() {
		return view;
	}

	public void setView(MbdusCustomerView view) {
		this.view = view;
	}
}
