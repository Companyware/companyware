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
package plugins.core.users.view;

import java.awt.Color;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

public class CheckboxColumnRenderer extends JComponent implements TableCellRenderer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6320822810696989145L;

	private JCheckBox box;
       
	final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    public CheckboxColumnRenderer(){
    	box = new JCheckBox();
    	box.setHorizontalAlignment(JLabel.CENTER);
    	box.setBackground(Color.white);
    }
    
    public JCheckBox getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus,
			int row, int column) {
	
    	box.setBackground(Color.white);
		box.setSelected((value != null && ((Boolean) value).booleanValue()));

		return box;
	}
	
}
