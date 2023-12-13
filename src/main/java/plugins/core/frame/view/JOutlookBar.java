package plugins.core.frame.view;
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


/*

  @author Torridity
 */
// Import the GUI classes
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.List;

import javax.swing.*;

import core.ApplicationContextProvider;
import core.TextMessages;

// Import the Java classes

/**
 * A JOutlookBar provides a component that is similar to a JTabbedPane, but instead of maintaining
 * tabs, it uses Outlook-style bars to control the visible component
 */
public class JOutlookBar extends JPanel implements ActionListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * The top panel: contains the buttons displayed on the top of the JOutlookBar
     */
    private JPanel topPanel = new JPanel(new GridLayout(1, 1));
    /**
     * The bottom panel: contains the buttons displayed on the bottom of the JOutlookBar
     */
    private JPanel bottomPanel = new JPanel(new GridLayout(1, 1));
    /**
     * A LinkedHashMap of bars: we use a linked hash map to preserve the order of the bars
     */
    private Map bars = new LinkedHashMap();
    
    private Map barEntries = new LinkedHashMap();
    
    private Map barIconEntries = new LinkedHashMap();
    /**
     * The currently visible bar (zero-based index)
     */
    private int visibleBar = 0;
    
    /**
     * A place-holder for the currently visible component
     */
    private JComponent visibleComponent = null;

    /**
     * Creates a new JOutlookBar; after which you should make repeated calls to
     * addBar() for each bar
     */
    public JOutlookBar() {
        this.setLayout(new BorderLayout());
        this.add(topPanel, BorderLayout.NORTH);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }
    
    public void setPreferredSize(){
    	int height = this.getHeight();
    	height = this.getBarEntrySize()*120;
    	this.setPreferredSize(new Dimension(196,height));
    }
    
    public void addBarEntry(String name) {
        Map entries = new LinkedHashMap();
        Map labels = (Map) this.barEntries.get(name);
        if(labels ==null){
        	labels = new LinkedHashMap();
        }
        labels.put(name, entries);
        this.barEntries.put(name, labels);
    }

    /**
     * Adds the specified component to the JOutlookBar and sets the bar's name
     * 
     * @param  name      The name of the outlook bar
     * @param  component   The component to add to the bar
     */
    public void addBar(String name, JComponent component) {
        BarInfo barInfo = new BarInfo(name, component);
        barInfo.getButton().addActionListener(this);
        this.bars.put(name, barInfo);
        render();
    }
    
    /**
     * Adds the specified component to the JOutlookBar and sets the bar's name
     * 
     * @param  name      The name of the outlook bar
     * @param  component   The component to add to the bar
     */
    public void addBar(String name, JComponent component, int priority) {
        BarInfo barInfo = new BarInfo(name, component, priority);
        barInfo.getButton().addActionListener(this);
        this.bars.put(name, barInfo);
        render();
    }

    /**
     * Adds the specified component to the JOutlookBar and sets the bar's name
     * 
     * @param  name      The name of the outlook bar
     * @param  icon      An icon to display in the outlook bar
     * @param  component   The component to add to the bar
     */
    public void addBar(String name, Icon icon, JComponent component) {
        BarInfo barInfo = new BarInfo(name, icon, component);
        barInfo.getButton().addActionListener(this);
        this.bars.put(name, barInfo);
        render();
    }
    
    public void addBarEntryIcon(String entryName, String iconName, ImageIcon image, Object controller) {
    	if(this.barEntries == null){
    		this.barEntries = new LinkedHashMap();
    	}
    	Map labels = (Map) this.barEntries.get(entryName);
    	boolean exists = false;
    	
	    if(labels.containsKey(entryName)){
	    	this.barIconEntries = (LinkedHashMap) labels.get(entryName);
	    	if(this.barIconEntries.containsKey(iconName)){
	    		exists = true;
	    	}
	    }
    	
    	if(!exists){
    		JLabel label = new JLabel();
    		TextMessages service = ApplicationContextProvider.getContext().getBean(TextMessages.class);
    		label.setText(service.get(iconName));
    		label.setName(iconName);
    		if(image == null){
    			label.setVerticalTextPosition(JLabel.CENTER);
    		}
    		else{
    			label.setIcon(image);
    			label.setVerticalTextPosition(JLabel.BOTTOM);
    		}
    		label.setHorizontalAlignment(JLabel.CENTER);
    		label.setHorizontalTextPosition(JLabel.CENTER);
    		if(this.barIconEntries == null){
    			this.barIconEntries = new LinkedHashMap();
    		}
    		label.addMouseListener((MouseListener) controller);
    		this.barIconEntries.put(iconName, label);
    		labels.put(entryName, this.barIconEntries);
    	}
    }

    /**
     * Removes the specified bar from the JOutlookBar
     * 
     * @param  name  The name of the bar to remove
     */
    public void removeBar(String name) {
        this.bars.remove(name);
        render();
    }

    /**
     * Returns the index of the currently visible bar (zero-based)
     * 
     * @return The index of the currently visible bar
     */
    public int getVisibleBar() {
        return this.visibleBar;
    }

    /**
     * Programmatically sets the currently visible bar; the visible bar
     * index must be in the range of 0 to size() - 1
     * 
     * @param  visibleBar   The zero-based index of the component to make visible
     */
    public void setVisibleBar(int visibleBar) {
        if (visibleBar > 0
                && visibleBar < this.bars.size()) {
            this.visibleBar = visibleBar;
            render();
        }
    }
    
    public int getBarSize(){
    	return this.bars.size();
    }
    
    public int getBarEntrySize(){
    	return this.barIconEntries.size();
    }

    /**
     * Causes the outlook bar component to rebuild itself; this means that
     * it rebuilds the top and bottom panels of bars as well as making the
     * currently selected bar's panel visible
     */
    public void render() {
        // Compute how many bars we are going to have where
        int totalBars = this.bars.size();
        int topBars = this.visibleBar + 1;
        int bottomBars = totalBars - topBars;
        
        // Ordering JOutlookbar by priority
        this.sortByPriority();
        
        // Get an iterator to walk through out bars with
        Iterator itr = this.bars.keySet().iterator();

        // Render the top bars: remove all components, reset the GridLayout to
        // hold to correct number of bars, add the bars, and "validate" it to
        // cause it to re-layout its components
        this.topPanel.removeAll();
        GridLayout topLayout = (GridLayout) this.topPanel.getLayout();
        topLayout.setRows(topBars);
        BarInfo barInfo = null;
        for (int i = 0; i < topBars; i++) {
            String barName = (String) itr.next();
            barInfo = (BarInfo) this.bars.get(barName);
            this.topPanel.add(barInfo.getButton());
        }
        this.topPanel.validate();


        // Render the center component: remove the current component (if there
        // is one) and then put the visible component in the center of this panel
        if (this.visibleComponent != null) {
            this.remove(this.visibleComponent);
        }
        this.visibleComponent = barInfo.getComponent();
        this.add(visibleComponent, BorderLayout.CENTER);

        // Render the bottom bars: remove all components, reset the GridLayout to
        // hold to correct number of bars, add the bars, and "validate" it to
        // cause it to re-layout its components
        this.bottomPanel.removeAll();
        GridLayout bottomLayout = (GridLayout) this.bottomPanel.getLayout();
        bottomLayout.setRows(bottomBars);
        for (int i = 0; i < bottomBars; i++) {
            String barName = (String) itr.next();
            barInfo = (BarInfo) this.bars.get(barName);
            JButton button = barInfo.getButton();
            this.bottomPanel.add(button);
        }
        this.bottomPanel.validate();

        // Validate all of our components: cause this container to re-layout its subcomponents
        this.validate();
        if (getParent() != null) {
            try {
                //try to refit size if parent is a scrollpane's viewport (used for fast options in DSWorkbenchMainFrame)
                getParent().validate();
            } catch (Exception ignored) {
            }
        }
    }
    
    private void sortByPriority() {
        List<Map.Entry<String, BarInfo>> list = new LinkedList<Map.Entry<String, BarInfo>>(this.bars.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, BarInfo>>() {
            public int compare(Map.Entry<String, BarInfo> o1, Map.Entry<String, BarInfo> o2) {
                return (Integer.valueOf(o1.getValue().getPriority())).compareTo(Integer.valueOf(o2.getValue().getPriority()));
            }
        });

        /// Loop the sorted list and put it into a new insertion order Map
        /// LinkedHashMap
        this.bars = new LinkedHashMap<String, BarInfo>();
        for (Map.Entry<String, BarInfo> entry : list) {
            this.bars.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Invoked when one of our bars is selected
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        int currentBar = 0;
        for (Object o : this.bars.keySet()) {
            String barName = (String) o;
            BarInfo barInfo = (BarInfo) this.bars.get(barName);
            if (barInfo.getButton() == e.getSource()) {
                // Found the selected button
                this.visibleBar = currentBar;
                render();
                return;
            }
            currentBar++;
        }
    }
    
    public JPanel getPanel(String entryName){
    	Iterator<Map.Entry<String, LinkedHashMap>> labels = ((Map) this.barEntries.get(entryName)).entrySet().iterator();
    	JPanel panel = null;
    	while (labels.hasNext()) {
    	    Map.Entry<String, LinkedHashMap> entry = labels.next();
    	    String key = entry.getKey();
    	    Map value = entry.getValue();
    	   
    	    panel = new JPanel(new GridLayout(value.size(),1));
    	    Iterator<Map.Entry<String, JLabel>> entries = value.entrySet().iterator();
    	    while (entries.hasNext()) {
	      	  Map.Entry<String, JLabel> innerEntry = entries.next();
	      	  String labelKey = innerEntry.getKey();
	      	  JLabel label = innerEntry.getValue();
	      	  panel.add((JLabel) label);
	      	}
    	}
    	
    	return panel;	
    }

    /**
     * Debug, dummy method
     */
    public static JPanel getDummyPanel(String name) {
        JPanel panel = new JPanel(new GridLayout(3,1));
   //     panel.setPreferredSize(new Dimension(100,200));
        panel.setBackground(Color.green);
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        panel.add(new JLabel(name, JLabel.CENTER));
        panel.add(new JLabel(name+"2", JLabel.CENTER));
        panel.add(new JLabel(name+"3", JLabel.CENTER));
        return panel;
    }

    /**
     * Internal class that maintains information about individual Outlook bars;
     * specifically it maintains the following information:
     * 
     * name      The name of the bar
     * button     The associated JButton for the bar
     * component    The component maintained in the Outlook bar
     */
    class BarInfo{

        /**
         * The name of this bar
         */
        private String name;
        /**
         * The JButton that implements the Outlook bar itself
         */
        private JButton button;
        /**
         * The component that is the body of the Outlook bar
         */
        private JComponent component;
        
        private int priority = 1;

		/**
         * Creates a new BarInfo
         * 
         * @param  name    The name of the bar
         * @param  component  The component that is the body of the Outlook Bar
         */
        public BarInfo(String name, JComponent component) {
            this.name = name;
            this.component = component;
            
            if(component!=null){
	            Dimension componentDimension = this.component.getPreferredSize();
	            componentDimension.height = 150;
	            this.component.setPreferredSize(componentDimension);
            }
            TextMessages service = ApplicationContextProvider.getContext().getBean(TextMessages.class);
            this.button = new JButton(service.get(name));
            Dimension buttonDimension = this.button.getPreferredSize();
            buttonDimension.height = 50;
            this.button.setPreferredSize(buttonDimension);
        }

		/**
         * Creates a new BarInfo
         * 
         * @param  name    The name of the bar
         * @param  component  The component that is the body of the Outlook Bar
         */
        public BarInfo(String name, JComponent component, int priority) {
            this.name = name;
            this.component = component;
            this.priority = priority;
            
            if(component!=null){
	            Dimension componentDimension = this.component.getPreferredSize();
	            componentDimension.height = 150;
	            this.component.setPreferredSize(componentDimension);
            }
            TextMessages service = ApplicationContextProvider.getContext().getBean(TextMessages.class);
            this.button = new JButton(service.get(name));
            Dimension buttonDimension = this.button.getPreferredSize();
            buttonDimension.height = 50;
            this.button.setPreferredSize(buttonDimension);
        }

        /**
         * Creates a new BarInfo
         * 
         * @param  name    The name of the bar
         * @param  icon    JButton icon
         * @param  component  The component that is the body of the Outlook Bar
         */
        public BarInfo(String name, Icon icon, JComponent component) {
            this.name = name;
            this.component = component;
            this.button = new JButton(name, icon);
            this.button.setBackground(Color.gray);
            this.button.setForeground(Color.gray);
        }

        /**
         * Returns the name of the bar
         * 
         * @return The name of the bar
         */
        public String getName() {
            return this.name;
        }

        /**
         * Sets the name of the bar
         * 
         * @param  name The name of the bar
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Returns the outlook bar JButton implementation
         * 
         * @return   The Outlook Bar JButton implementation
         */
        public JButton getButton() {
            return this.button;
        }

        /**
         * Returns the component that implements the body of this Outlook Bar
         * 
         * @return The component that implements the body of this Outlook Bar
         */
        public JComponent getComponent() {
            return this.component;
        }
        
        public int getPriority() {
			return priority;
		}

		public void setPriority(int priority) {
			this.priority = priority;
		}
    }
}
