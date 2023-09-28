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

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.hibernate.query.Query;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import models.pluginsettings.PluginSettingsModel;

public class CompanywareHelper {
	
	public void clearXmlSettings(String pluginName) throws ParserConfigurationException, SAXException, IOException{
	    //Get Document Builder
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    
	    String fileSeparator = FileSystems.getDefault().getSeparator();
	    
	    File file = new File("src"+fileSeparator+"main"+fileSeparator+"java"+fileSeparator+"plugins"+fileSeparator+"community/"+pluginName.substring(0, 1).toLowerCase()+pluginName.substring(1)+fileSeparator+pluginName+".xml");
	    
	    if(file.exists()){
	    	//Build Document
	    	Document document = builder.parse(file);

	    	//Normalize the XML Structure; It's just too important !!
	    	document.getDocumentElement().normalize();

	    	//Get all settings
	    	NodeList nList = document.getElementsByTagName("Setting");
	    	
	    	List<Object[]> settings = this.getSettings(pluginName);
		
			ArrayList<String> settingNames = new ArrayList<>();
			HashSet<String> hashSet = new HashSet<String>();
			for (Object[] object : settings) {
				PluginSettingsModel sm = (PluginSettingsModel) object[1];
		        settingNames.add(sm.getName());
		        //delete duplicates
		        if(!hashSet.add(sm.getName())){
		        	models.pluginsettings.Repository.deleteById(sm.getId());
		        }
			}
			
	    	for (int temp = 0; temp < nList.getLength(); temp++){
	    		Node node = nList.item(temp);
	    		if (node.getNodeType() == Node.ELEMENT_NODE){
	    			Element eElement = (Element) node;
	    			String settingName = eElement.getElementsByTagName("name").item(0).getTextContent();
	    	
	    			settings = this.getSettings(pluginName);
	    		  
	    		    if(!settingNames.contains(settingName)){
	    		    	for (Object[] object : settings) {
	    		        	PluginSettingsModel sm = (PluginSettingsModel) object[1];
	    		        	models.pluginsettings.Repository.deleteById(sm.getId());
	    		    	}
	    		    }
	    		}
	    	}
	    }
	}
	
	public List<Object[]> getSettings(String pluginName){
		Query query= HibernateUtils.getSessionFactory().openSession().
		        createQuery(""
		        		+ "from PluginModel pm "
		        		+ "join PluginSettingsModel sm "
		        		+ "on pm.id = sm.pluginId "
		        		+ "where pm.name=:name "
		        		+ "");
    	List<Object[]> settings;
		query.setParameter("name", pluginName);
		settings = query.getResultList();
		return settings;
	}
}
