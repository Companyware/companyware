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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.*;
import java.net.URL;

import pluginmanager.plugininterfaces.PluginManager;
import plugins.core.users.view.WhiteButton;

public class ButtonTableEditor extends DefaultCellEditor{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(ButtonTableEditor.class);
	
	private JButton button;
	private String label;
	private boolean clicked;
	private int row, col;
	private ImageIcon icon;

	public ButtonTableEditor(JCheckBox checkBox, PluginManager pm)
	{
		super(checkBox);
		button = new WhiteButton();
		URL url = getClass().getResource("/small_edit.png");
    	BufferedImage img = null;
		try {
			img = ImageIO.read(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	this.icon = new ImageIcon(img);
    	button.setIcon(this.icon);
		button.setBackground(Color.white);
		button.setBorderPainted(false);
		button.setActionCommand("button renderer");
		button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				button.setForeground(Color.black);
				button.setBackground(Color.white);
				log.info("button action");
				log.info("ACTION: " + e.getActionCommand().toString());
				fireEditingStopped();
			}
		});
	}
	public JButton getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		this.row = row;
		this.col = column;
		
		label = (value == null) ? "" : value.toString();
		button.setIcon(this.icon);
		clicked = true;
		return button;
	}
	public Object getCellEditorValue()
	{
		if (clicked)
		{
			log.info(row);
			log.info(col);
			
			log.info("user edit");
	//		JOptionPane.showInputDialog(button, "Column with Value: "+table.getValueAt(row, 0) + " -  Clicked!");

		}
		clicked = false;
		log.info(label);
		return new String(label);
	}

	public boolean stopCellEditing()
	{
		clicked = false;
		return super.stopCellEditing();
	}

	protected void fireEditingStopped()
	{
		super.fireEditingStopped();
	}
}
