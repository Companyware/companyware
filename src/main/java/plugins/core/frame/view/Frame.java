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
package plugins.core.frame.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.formdev.flatlaf.FlatLightLaf;

import core.ApplicationContextProvider;
import core.TextMessages;
import plugins.core.frame.controller.FrameController;
import plugins.core.menu.Menu;

public class Frame extends JFrame{ 	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(Frame.class);
	private JPanel topPanel;
	private JPanel centerPanel;
	private JScrollPane leftScrollPane;
	private JPanel leftPanel;
	private JOutlookBar outlookBar;
	private JPanel bottomPanel;
	private int constTopHeight;
	private int constCenterWidth;
	private int constCenterHeight;
	private int constLeftWidth;
	private int constBottomHeight;
	
	public Frame(){
		TextMessages service = ApplicationContextProvider.getContext().getBean(TextMessages.class);
		this.setTitle(service.get("program.name"));
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/company-ico.png")));
		this.initLayout();
	}
	
	public void initLayout(){
		FlatLightLaf.install();
		this.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH); 
		try {
		//	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			UIManager.setLookAndFeel(new FlatLightLaf());
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		this.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH); 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(400, 1000));
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
        int x = (int) rect.getMaxX() - this.getWidth();
        int y = 0;
        this.setLocation(x, y);
		
		this.setUIFont(new FontUIResource(new Font("Arial", 0, 16)));
		
		this.addWindowStateListener(new WindowStateListener() {
			@Override
			public void windowStateChanged(WindowEvent arg) {
				// TODO Auto-generated method stub
				 if (arg.getNewState() == java.awt.Frame.NORMAL){
				      Frame.this.setAlwaysOnTop(true);
				 }
				 else if (arg.getNewState() == java.awt.Frame.MAXIMIZED_BOTH){
				      Frame.this.setAlwaysOnTop(false);
				 }
			}
		});
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		this.initTopArea();
		mainPanel.add(this.topPanel,BorderLayout.NORTH);
		this.initLeftArea();
		mainPanel.add(this.leftScrollPane,BorderLayout.WEST);
		this.initCenterArea();
		mainPanel.add(this.centerPanel,BorderLayout.CENTER);
		this.initBottomArea();
		mainPanel.add(this.bottomPanel,BorderLayout.SOUTH);
		this.getContentPane().add(mainPanel,BorderLayout.CENTER);
		this.setVisible(true);
	}
	
	public void setUIFont(FontUIResource f) {
        Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                FontUIResource orig = (FontUIResource) value;
                Font font = new Font(f.getFontName(), orig.getStyle(), f.getSize());
                UIManager.put(key, new FontUIResource(font));
            }
        }
    }
	
	public void initTopArea(){
		this.topPanel = new JPanel();
		this.constTopHeight = 150;
		this.topPanel.setPreferredSize(new Dimension(this.getWidth(),this.constTopHeight));
		this.topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
	}
	
	public void initLeftArea(){
		this.leftPanel = new JPanel();
		this.constLeftWidth = 200;
		this.leftPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
		
		this.outlookBar = new JOutlookBar();
        this.outlookBar.setVisibleBar(1);
    //    this.outlookBar.setPreferredSize(this.getHeight());
        this.leftPanel.add(outlookBar);
       	
        this.leftScrollPane = new JScrollPane();
    	this.leftScrollPane.setBackground(Color.cyan);
    	this.leftScrollPane.setPreferredSize(new Dimension(this.constLeftWidth+9,this.getHeight()));
        this.leftScrollPane.setViewportView(this.leftPanel);
        this.leftScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 1));
	}
	
	public void initCenterArea(){
		this.centerPanel = new JPanel();
		this.constCenterHeight = -180;
		this.constCenterWidth = -200;
		this.centerPanel.setPreferredSize(new Dimension(this.getWidth()-this.constCenterWidth,this.getHeight()-this.constCenterHeight));
	}
	
	public void initBottomArea(){
		this.bottomPanel = new JPanel();
		this.constBottomHeight = 30;
		this.bottomPanel.setPreferredSize(new Dimension(this.getWidth(),this.constBottomHeight));
		this.bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
	}
	
	public JPanel getTopPanel() {
		return topPanel;
	}

	public void setTopPanel(JPanel topPanel) {
		this.topPanel = topPanel;
	}

	public JPanel getCenterPanel() {
		return centerPanel;
	}

	public void setCenterPanel(JPanel centerPanel) {
		this.centerPanel = centerPanel;
	}

	public JPanel getLeftPanel() {
		return leftPanel;
	}

	public void setLeftPanel(JPanel leftPanel) {
		this.leftPanel = leftPanel;
	}
	
	public JScrollPane getLeftScrollPane() {
		return leftScrollPane;
	}

	public void setLeftScrollPane(JScrollPane leftScrollPane) {
		this.leftScrollPane = leftScrollPane;
	}

	public JPanel getBottomPanel() {
		return bottomPanel;
	}

	public void setBottomPanel(JPanel bottomPanel) {
		this.bottomPanel = bottomPanel;
	}
	
	public int getConstTopHeight() {
		return constTopHeight;
	}

	public void setConstTopHeight(int constTopHeight) {
		this.constTopHeight = constTopHeight;
	}

	public int getConstCenterWidth() {
		return constCenterWidth;
	}

	public void setConstCenterWidth(int constCenterWidth) {
		this.constCenterWidth = constCenterWidth;
	}

	public int getConstCenterHeight() {
		return constCenterHeight;
	}

	public void setConstCenterHeight(int constCenterHeight) {
		this.constCenterHeight = constCenterHeight;
	}

	public int getConstLeftWidth() {
		return constLeftWidth;
	}

	public void constSetLeftWidth(int constLeftWidth) {
		this.constLeftWidth = constLeftWidth;
	}

	public int getConstBottomHeight() {
		return constBottomHeight;
	}

	public void setConstBottomHeight(int constBottomHeight) {
		this.constBottomHeight = constBottomHeight;
	}
	
	public JOutlookBar getOutlookBar() {
		return outlookBar;
	}

	public void setOutlookBar(JOutlookBar outlookBar) {
		this.outlookBar = outlookBar;
	}
}