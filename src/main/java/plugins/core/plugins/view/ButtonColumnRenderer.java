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
package plugins.core.plugins.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

public class ButtonColumnRenderer extends JComponent implements TableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final JButton button;
       
	final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    public ButtonColumnRenderer(String value){
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setBackground(Color.white);
        button = new JButton(value);
        button.setBackground(Color.white);
        add(Box.createHorizontalGlue());
        add(button);
        add(Box.createHorizontalGlue());
        button.setHorizontalAlignment(JLabel.CENTER);
    }
    
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isClicked, boolean hasFocus,
			int row, int column) {
		
		return this;
	}
}