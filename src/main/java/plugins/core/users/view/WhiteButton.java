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

import javax.swing.*;
import java.awt.*;

public class WhiteButton extends JButton {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton button = new JButton();
    private Color bg;

    public WhiteButton() {
        super();
        setBorderPainted(false);
    }

    @Override
    public void setBackground(Color bg) {
        this.bg = bg;
        if(button != null) {
    //        button.setBackground(bg);
        }
    }

    @Override
    public void setForeground(Color fg) {
        super.setForeground(fg);
        if(button != null){
            button.setForeground(fg);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Rectangle rectangle = getBounds();
        rectangle.x = 0;
        rectangle.y = 0;
        rectangle.width -= rectangle.x * 2;
        rectangle.height -= rectangle.y * 2;

        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(bg);
      
        g2d.fill(rectangle);
    }
}