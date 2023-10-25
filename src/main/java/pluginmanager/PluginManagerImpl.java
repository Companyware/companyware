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
package pluginmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.yaml.snakeyaml.Yaml;

import core.HibernateUtils;
import models.plugin.PluginModel;
import models.plugin.Repository;
import models.pluginsettings.PluginSettingsModel;
import pluginmanager.plugininterfaces.ActionProcessor;
import pluginmanager.plugininterfaces.Plugin;
import pluginmanager.plugininterfaces.PluginManager;
import pluginmanager.plugininterfaces.Service;

public class PluginManagerImpl implements PluginManager {

	private String pluginsDir;
	private PluginClassLoader classLoader;
	private List<String> loadedPlugins = new ArrayList<>();
	private Map<String, Service> services = new HashMap<>();
	private Boolean isJar = false;
	private static final Log log = LogFactory.getLog(PluginManagerImpl.class);

	public PluginManagerImpl(String pluginsDir) {
		this.pluginsDir = pluginsDir;
		this.classLoader = new PluginClassLoader();
	}

	@Override
	public void init() {
	//	URL url = PluginManagerImpl.class.getResource("/resources");
		URL url = getClass().getResource("/");

        URI uri = null;
		
		
		try {
			if(url!=null){
				uri = url.toURI();
			}
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Path path;
		if (uri != null && uri.getScheme().equals("jar")) {
			this.isJar = true;
        	this.pluginsDir = "classes/plugins";
        	FileSystem fileSystem = null;
			try {
				fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            path = fileSystem.getPath("/BOOT-INF/classes/plugins/");
            Stream<Path> walk = null;
			try {
				walk = Files.walk(path,1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            for (Iterator<Path> it = walk.iterator(); it.hasNext();){
            	String filename = it.next().toString();
                if(filename.contains("/core/") || filename.contains("community") ){
                	boolean core = filename.contains("/core/")?true:false;
                	
                	Path pathPlugins = fileSystem.getPath(filename);
                	try {
						Files.walk(pathPlugins,1).forEach(pathP -> {
							 File file = new File(pathP.toString());
						
						     if(!file.getName().startsWith(".") && !file.getName().endsWith("core") && !file.getName().endsWith("community")){
						    	 initPlugin(file, core);
						     }
						});
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            }
        }
        else{
        	File dh = new File(this.pluginsDir);
        
            if(dh != null){ 
    			List<String> listFiles = Arrays.asList(dh.list());
    		
    			Collections.sort(listFiles,Collections.reverseOrder());
    			for (String pluginDir : listFiles){
    				if(pluginDir.equals("community") || pluginDir.equals("core")){
    					boolean core = pluginDir.equals("core")?true:false;
    					File pluginDirectory = new File(this.pluginsDir + System.getProperty("file.separator") + pluginDir); 
    					for (File plugin : pluginDirectory.listFiles()){
    						if(!plugin.getName().startsWith(".")){
    							initPlugin(plugin, core);
    						}
    					}
    				}
    			}
            }
        }
		loadPlugins();
	}
	
	@SuppressWarnings("unchecked")
	private void initPlugin(File pluginDir, boolean core) {
		try {
			String subdirectory = core?"core":"community";
			String pattern = Pattern.quote(System.getProperty("file.separator"));
			String[] pathParts = pluginDir.getName().split(pattern);
			String pluginpath = "plugins."+subdirectory+"."+pathParts[0]+"."+pathParts[0].substring(0, 1).toUpperCase() + pathParts[0].substring(1);
 	
 			
			String pluginNamespace = pathParts[0].substring(0, 1).toUpperCase() + pathParts[0].substring(1);
			Yaml yaml = new Yaml();
			
 			File file = new File(pluginDir, pluginNamespace+".yaml");
 			Map<String, Object> pluginConf = null; 
 			if(file.exists()){
				pluginConf = (Map<String, Object>) yaml
						.load(new FileInputStream(
								new File(pluginDir, pluginNamespace+".yaml")));
 			}
 			String name = "";
			String version = "";
			String author = "";
			Integer prio = null;
			name = pluginNamespace;
			if(!name.equals("Community") && !name.equals("Core")){
				if(pluginConf != null){
					version = pluginConf.get("version").toString();
					author = pluginConf.get("author").toString();
				
					if(pluginConf.get("prio") != null){
						prio = (Integer)pluginConf.get("prio");
					}
				}
			
				Query query= HibernateUtils.getSessionFactory().openSession().
				        createQuery("from PluginModel where name=:name");
				query.setParameter("name", name);
				PluginModel pluginModel = (PluginModel) query.uniqueResult();
				Long pluginId;
			    if(pluginModel == null){
			       	PluginModel newPlugin = new PluginModel();
			       	newPlugin.setName(name);
			       	newPlugin.setActive(core);
			       	newPlugin.setNamespace(subdirectory);
			       	newPlugin.setPath(pluginpath);
			       	if(prio != null){
			       		newPlugin.setPrio(prio);
			       	}
			       	Repository.save(newPlugin);
			       	pluginId = newPlugin.getId();
			    }
			    else{
			    	pluginId = pluginModel.getId();
			    }
			    this.readAndSaveXmlSettings(name, pluginId);
			}
		} catch (Exception ex) {
			log.error("Exception in PluginManagerImp loadPlugins",ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void loadPlugin(File pluginDir, boolean core) {
		try {
			String subdirectory = core?"core":"community";
			String pattern = Pattern.quote(System.getProperty("file.separator"));
			String[] pathParts = pluginDir.getName().split(pattern);
			String pluginpath = "plugins."+subdirectory+"."+pathParts[0]+"."+pathParts[0].substring(0, 1).toUpperCase() + pathParts[0].substring(1);
 		
			String pluginNamespace = pathParts[0].substring(0, 1).toUpperCase() + pathParts[0].substring(1);
			Yaml yaml = new Yaml();
			
 			File file = new File(pluginDir, pluginNamespace+".yaml");
 			Map<String, String> pluginConf = null; 
 			if(file.exists()){
				pluginConf = (Map<String, String>) yaml
						.load(new FileInputStream(
								new File(pluginDir, pluginNamespace+".yaml")));
 			}
 			String name = "";
			String version = "";
			String author = "";
			name = pluginNamespace;
			if(pluginConf != null){
				version = pluginConf.get("version");
				author = pluginConf.get("author");
			}
			
			Query query= HibernateUtils.getSessionFactory().openSession().
			        createQuery("from PluginModel where name=:name");
			query.setParameter("name", name);
			PluginModel pluginModel = (PluginModel) query.uniqueResult();
		    Long pluginId;
			if(pluginModel == null){
		       	PluginModel newPlugin = new PluginModel();
		       	newPlugin.setName(name);
		       	newPlugin.setActive(core);
		       	newPlugin.setNamespace(subdirectory);
		       	newPlugin.setPath(pluginpath);
		       	Repository.save(newPlugin);
		       	pluginId = newPlugin.getId();
		    }
			else{
				pluginId = pluginModel.getId();
			}
		    this.readAndSaveXmlSettings(name, pluginId);
		   
			File jarFile = new File(pluginDir+".jar");
			Class<?> pluginClass = null;
			if(jarFile.exists()){
				ClassLoader classLoader = URLClassLoader.newInstance(
		            new URL[] { jarFile.toURI().toURL() },
		            getClass().getClassLoader()
		        );
				pluginClass = classLoader.loadClass(pluginpath);
			}
			else{
				pluginClass = this.classLoader.loadClass(pluginpath);
			}
			Plugin pluginInstance = (Plugin) pluginClass.newInstance();
			pluginInstance.init(this);
			loadedPlugins.add(pluginDir.getAbsolutePath());
		} catch (Exception ex) {
			log.error("Exception in PluginManagerImp loadPlugin",ex);
		}
	}

	@SuppressWarnings("unchecked")
	private void loadPlugins() {
		String pluginPath = "";
		String pluginpath = "";
		try {
			List<PluginModel> plugins;
			Session session = HibernateUtils.getSessionFactory().openSession();
			Query query = session.createQuery("from PluginModel where active =:active order by namespace desc, prio asc");
			query.setParameter("active", true);
			plugins = query.list();
			for (PluginModel plugin: plugins){
				pluginPath = plugin.getPath();
				pluginPath = pluginPath.replace(".", File.separator);
				String subdirectory="";
				if(pluginPath.contains("core")){
					subdirectory="core";
				}
				else{
					subdirectory = "community";
				}
				File file = new File("classes"+File.separator+pluginPath+".class");
				String pattern = Pattern.quote(System.getProperty("file.separator"));
				String[] pathParts = file.getPath().split(pattern);
				if(pathParts.length == 1){
					pattern = Pattern.quote(".");
					pathParts = file.getPath().split(pattern);
				}
			
				pluginpath = "plugins."+subdirectory+"."+pathParts[3]+"."+pathParts[3].substring(0, 1).toUpperCase() + pathParts[3].substring(1);
				String pluginNamespace = pathParts[2].substring(0, 1).toUpperCase() + pathParts[2].substring(1);
				
				if (loadedPlugins.contains(file.getAbsolutePath()))
					continue;
				if(!file.getAbsolutePath().endsWith("community") && !file.getAbsolutePath().endsWith("core")){
					classLoader.loadPlugin(file);
				}
				Yaml yaml = new Yaml();
	 			File yamlFile = new File(pluginPath+".yaml");
	 			Map<String, String> pluginConf = null; 
	 			if(yamlFile.exists()){
					pluginConf = (Map<String, String>) yaml
							.load(new FileInputStream(
									new File(pluginPath+".yaml")));
	 			}
	 			String name = "";
				String version = "";
				String author = "";
				name = pluginNamespace;
				if(pluginConf != null){
					version = pluginConf.get("version");
					author = pluginConf.get("author");
					String deps = pluginConf.get("dependencies");
					if (deps != null){
						loadPluginDependencies(deps);
					}
				}
				
				File jarFile = null;
				if(this.isJar){
					String classpathEntry = System.getProperty("java.class.path");
					
				    if (classpathEntry.endsWith(".jar") || classpathEntry.endsWith(".exe") || classpathEntry.endsWith(".tgz")) {
				    	pluginPath = pluginPath.replace("\\", "/");
				    	jarFile = new File(classpathEntry);
				    }
				}
				else{
					jarFile = new File(pluginPath+".jar");
				}
				Class<?> pluginClass = null;
				if(jarFile.exists() || this.isJar){
					ClassLoader classLoader = URLClassLoader.newInstance(
			            new URL[] { jarFile.toURI().toURL() },
			            getClass().getClassLoader()
			        );
					pluginClass = classLoader.loadClass(pluginpath);
				}
				else{
					pluginClass = this.classLoader.loadClass(pluginpath);
				}
				Plugin pluginInstance = (Plugin) pluginClass.newInstance();
				pluginInstance.init(this);
				loadedPlugins.add(file.getAbsolutePath());
			}
		} catch (Exception ex) {
			log.error("Exception in PluginManagerImp loadPlugins",ex);
			log.error(pluginPath);
			log.error(pluginpath);
		}
	}

	private void loadPluginDependencies(String dependencies) {
		String[] deps = dependencies.split(",");
		for (String dep : deps){
			loadPlugin(new File(pluginsDir, dep), false);
		}
	}
	
	public void readAndSaveXmlSettings(String pluginName, Long pluginId) throws ParserConfigurationException, SAXException, IOException{
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

	    	//Here comes the root node
	    	Element root = document.getDocumentElement();

	    	//Get all settings
	    	NodeList nList = document.getElementsByTagName("Setting");
	  
	    	for (int temp = 0; temp < nList.getLength(); temp++)
	    	{
	    		Node node = nList.item(temp);
	    		if (node.getNodeType() == Node.ELEMENT_NODE)
	    		{
	    			Element eElement = (Element) node;
	    			String settingType = eElement.getElementsByTagName("type").item(0).getTextContent();
	    			String settingName = eElement.getElementsByTagName("name").item(0).getTextContent();
	    			String settingValue = eElement.getElementsByTagName("value").item(0).getTextContent();
	    		
	    			Query query= HibernateUtils.getSessionFactory().openSession().
	    			        createQuery(""
	    			        		+ "from PluginModel pm "
	    			        		+ "join PluginSettingsModel sm "
	    			        		+ "on pm.id = sm.pluginId "
	    			        		+ "where pm.name=:name "
	    			        		+ "and sm.name=:settingName "
	    			        		+ "");
	    	    	List<Object[]> settings;
	    			query.setParameter("name", pluginName);
	    			query.setParameter("settingName", settingName);
	    			settings = query.list();
	    		
	    		    if(settings.isEmpty()){   
	    		       PluginSettingsModel newSetting = new PluginSettingsModel();
	   		       	   newSetting.setPluginId(pluginId);
	   		       	   newSetting.setName(settingName);
	   		       	   newSetting.setType(settingType);
	   		       	   newSetting.setValue(settingValue);
	   		       	   models.pluginsettings.Repository.save(newSetting);
	    		    }
	    		
	    		}
	    	}
	    }
	}

	@Override
	public void registerService(String name, Service service) {
		services.put(name, service);
	}

	@Override
	public Service getService(String name) {
		return services.get(name);
	}

	private class ActionProcessorElement implements Comparable<ActionProcessorElement> {

		private ActionProcessor processor;
		private int priority;
				
		public ActionProcessorElement(ActionProcessor processor, int priority) {
			this.processor = processor;
			this.priority = priority;
		}

		@Override
		public int compareTo(ActionProcessorElement o) {
			return o.priority - this.priority;
		}

		public ActionProcessor getProcessor() {
			return processor;
		}
	}

	private class ActionProcessorList {
		
		private List<ActionProcessorElement> actionProcessorElements = new ArrayList<ActionProcessorElement>();

		public void addActionProcessor(ActionProcessor processor, int priority) {
			actionProcessorElements.add(new ActionProcessorElement(processor, priority));
			Collections.sort(actionProcessorElements);
		}

		public void removeActionProcessor(ActionProcessor processor) {
			ActionProcessorElement element = null;
			for(ActionProcessorElement ape : actionProcessorElements) {
				if(ape.getProcessor() == processor)
					element = ape;
			}
			if(element != null)
				actionProcessorElements.remove(element);
		}

		public void call(Map<String, Object> context) {
			for(ActionProcessorElement ape : actionProcessorElements) {
				ape.getProcessor().call(context);
			}
		}	
	}
	
	private Map<String, ActionProcessorList> actionProcessors = new HashMap<String, ActionProcessorList>(); 
		
	@Override
	public void addActionProcessor(String actionName, ActionProcessor processor) {
		addActionProcessor(actionName, processor, 0);
	}

	@Override
	public void addActionProcessor(String actionName,
			ActionProcessor processor, int priority) {
		ActionProcessorList apl = actionProcessors.get(actionName);
		if(apl == null)
			actionProcessors.put(actionName, apl = new ActionProcessorList());
		apl.addActionProcessor(processor, priority);
	}

	@Override
	public void removeActionProcessor(String actionName,
			ActionProcessor processor) {
		ActionProcessorList apl = actionProcessors.get(actionName);
		if(apl == null)
			return;
		apl.removeActionProcessor(processor);
	}

	@Override
	public void callAction(String actionName, Map<String, Object> context) {
		ActionProcessorList apl = actionProcessors.get(actionName);
		if(apl == null)
			return;
		apl.call(context);
	}
}