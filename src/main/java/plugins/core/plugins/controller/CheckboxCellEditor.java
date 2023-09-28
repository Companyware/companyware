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

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import core.Companyware;
import models.plugin.PluginModel;
import models.plugin.Repository;

import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import pluginmanager.plugininterfaces.PluginManager;
import plugins.core.frame.controller.FrameController;


public class CheckboxCellEditor extends DefaultCellEditor{
	
	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(CheckboxCellEditor.class);
	private PluginManager pm;
	private JTable table;
	private int row;
	private boolean isSelected;
	
	public CheckboxCellEditor(JCheckBox checkBox, PluginManager pm)
	{
		super(checkBox);
		this.pm = pm;
		checkBox.addItemListener(new ItemListener() {

			@Override
		    public void itemStateChanged(ItemEvent e) {
		        if(e.getStateChange() == ItemEvent.SELECTED) {
		        	CheckboxCellEditor.this.isSelected = true;
		           
		        } else {
		        	CheckboxCellEditor.this.isSelected = false;
		        }
		    }
		});
	}
	
	public JCheckBox getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		this.table = table;
		this.row = row;
		this.isSelected = isSelected;
		JCheckBox checkBox = (JCheckBox)super.getTableCellEditorComponent(table, value, isSelected, row, column);
		return checkBox;
	}
	
	public Object getCellEditorValue()
	{
		String pluginName = (String)table.getValueAt(row, 0);
		PluginModel plugin = Companyware.getContainer().getPluginByName(pluginName);
		plugin.setActive(this.isSelected);
		Repository.save(plugin);
		Object object = super.getCellEditorValue();
		this.setSuccessMessage(this.isSelected);
		return object;
	}
	
	public void setSuccessMessage(boolean isSelected){
		FrameController frameController = (FrameController)pm.getService("FrameController");
		JPanel bottom = frameController.getView().getBottomPanel();
		String message = isSelected?"Erfolgreich aktiviert!":"Erfolgreich deaktiviert!";
	    JLabel labelBottom = new JLabel(message);
	    JOptionPane.showMessageDialog(frameController.getView(), message, "Info", JOptionPane.INFORMATION_MESSAGE);
		bottom.setLayout(new FlowLayout(FlowLayout.LEFT));
		bottom.removeAll();
		bottom.add(labelBottom);
		bottom.revalidate();
		bottom.repaint();
	}
}
