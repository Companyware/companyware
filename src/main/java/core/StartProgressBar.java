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
package core;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ResourceBundle;

public class StartProgressBar extends JWindow{

    private static final long serialVersionUID = 1L;
    private Timer timer;
    private JProgressBar progressBar;
    private final int maxval = 100;


    public void initializeUI() {
    	try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	
        progressBar = new JProgressBar();

        timer = new Timer(50, new UpdateBarListener());
        timer.start();
        
        ResourceBundle bundle = ResourceBundle.getBundle("messages");
        JLabel label = new JLabel();
        label.setFont(new Font("Verdana", Font.BOLD, 14));
        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
        label.setBorder(border);
        label.setPreferredSize(new Dimension(150, 131));
        label.setText(bundle.getString("program.name") + " - " + bundle.getString("program.start"));

        createLayout(label, progressBar);
        setSize(350, 150);
        setLocationRelativeTo(null);
    }

    private void createLayout(JComponent...arg) {
    	Container container = getContentPane();
        
        container.add(arg[0],BorderLayout.NORTH);
        container.add(arg[1],BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    private class UpdateBarListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            int val = progressBar.getValue();

            if (val >= maxval) {
                timer.stop();
                dispose();
                return;
            }

            progressBar.setValue(++val);
        }
    }
}