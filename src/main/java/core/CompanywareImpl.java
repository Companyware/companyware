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

import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.xml.parsers.ParserConfigurationException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.query.Query;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.xml.sax.SAXException;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

import models.plugin.PluginModel;
import models.settings.SettingsModel;
import models.user.UserModel;
import pluginmanager.plugininterfaces.PluginManager;
import plugins.core.frame.controller.FrameController;
import models.settings.Repository;

public class CompanywareImpl {
	
	public List<Object[]> getPluginSettings(String pluginName) throws ParserConfigurationException, SAXException, IOException{
		CompanywareHelper ch = new CompanywareHelper(); 
		ch.clearXmlSettings(pluginName);
		
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
	
	public PluginModel getPluginByName(String pluginName){
		Query query= HibernateUtils.getSessionFactory().openSession().
		        createQuery(""
		        		+ "from PluginModel pm "
		        		+ "where pm.name=:name "
		        		+ "");
		PluginModel plugin;
		query.setParameter("name", pluginName);
		plugin = (PluginModel) query.getSingleResult();
		return plugin;
	}
	
	public String getVersionNumber(){
		String version = "";
		Query query= HibernateUtils.getSessionFactory().openSession().
		        createQuery(""
		        		+ "from SettingsModel s "
		        		+ "where s.name=:name "
		        		+ "");
		SettingsModel settings = null;
		query.setParameter("name", "version");
		try {
			settings = (SettingsModel) query.getSingleResult();
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(settings != null){
			version = settings.getValue();
		}
		return version;
	}
	
	public void checkUpdate(){
		Query query= HibernateUtils.getSessionFactory().openSession().
		        createQuery(""
		        		+ "from SettingsModel s "
		        		+ "where s.name=:name "
		        		+ "");
		SettingsModel settings = null;
		query.setParameter("name", "version");
		try {
			settings = (SettingsModel) query.getSingleResult();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		DefaultArtifactVersion artifactDbVersion = null;
		String dbVersion = "";
		if(settings != null){
			dbVersion = settings.getValue();
			artifactDbVersion = new DefaultArtifactVersion(dbVersion);
		}
		
		Path temp = null;
		try {
			temp = Files.createTempFile("application-", ".properties");
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			Files.copy(getClass().getClassLoader().getResourceAsStream("application.properties"), temp, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		FileInputStream input = null;
		try {
			input = new FileInputStream(temp.toFile());
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Properties appProps = new Properties();
		try {
			appProps.load(input);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String fileVersion = appProps.getProperty("companyware.version");
		DefaultArtifactVersion artifactFileVersion = new DefaultArtifactVersion(fileVersion);
		
		if(dbVersion == "" && fileVersion != null){
			settings = new SettingsModel();
			settings.setName("version");
			settings.setValue(fileVersion);
			Repository.save(settings);
			artifactDbVersion = new DefaultArtifactVersion(fileVersion);
		}
		
		if (artifactDbVersion.compareTo(artifactFileVersion)<0){
			settings.setName("version");
			settings.setValue(fileVersion);
			Repository.save(settings);
			//update
			this.executeUpdateSql(fileVersion);
		}
	}
	
	public void checkFirstRun(){
		URL url = CompanywareImpl.class.getResource("/");
		URI uri = null;
		try {
			if(url!=null){
				uri = url.toURI();
			}
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		File file;
		File installFile;
		String fileSeparator = FileSystems.getDefault().getSeparator();
		if (uri != null && uri.getScheme().equals("jar")) {
			file = new File("classes");
			installFile = new File("classes"+fileSeparator+"installsql.lck");
		} 
		else{
			file = new File("src/main/java");
			installFile = new File("src"+fileSeparator+"main"+fileSeparator+"java"+fileSeparator+"installsql.lck");
		}
		
		if(!installFile.exists()){
			this.executeSetupSql();
			this.setFirstRun();
			try {
				FileOutputStream s = new FileOutputStream(installFile,false);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setFirstRun(){
		Query query= HibernateUtils.getSessionFactory().openSession().
		        createQuery(""
		        		+ "from SettingsModel s "
		        		+ "where s.name=:name "
		        		+ "");
		SettingsModel settings = null;
		query.setParameter("name", "firstRun");
		try {
			settings = (SettingsModel) query.getSingleResult();
		} catch (Exception e) {
			// TODO: handle exception
		}
		boolean insertData = false;
		if(settings == null){
			settings = new SettingsModel();
			settings.setName("firstRun");
			settings.setValue("1");
			insertData = true;
		}
		else{
			settings.setValue("0");
		}
		Repository.save(settings);
		if(insertData){
			this.executeSetupSql();
			this.insertData();
		}
	}
	
	public void insertData(){
		UserModel user = new UserModel();
		user.setName("companyware");
		user.setUsername("companyware");
		user.setEmail("info@companyware.de");
		user.setActive(true);
		String encryptedPassword = this.encoder().encode("companyware");
		user.setPassword(encryptedPassword);
		models.user.Repository.save(user);
	}
	
	public void executeSetupSql(){
		InputStream is = getClass().getClassLoader()
                .getResourceAsStream("setup/install.sql");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
        String line = null;
        StringBuilder sb = new StringBuilder();
       
        Session session = HibernateUtils.getSessionFactory().openSession();
        try {
			while((line = br.readLine()) != null) {
				if (line.trim().endsWith(";")){
					session.beginTransaction();
					sb.append(line.replace(";", ""));
					session.createNativeQuery(sb.toString()).executeUpdate();
			        session.getTransaction().commit();
					sb = new StringBuilder();
				}
				else{
					sb.append(line);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void executeUpdateSql(String fileVersion){
		InputStream is = getClass().getClassLoader()
                .getResourceAsStream("setup/update"+fileVersion+".sql");
		if(is != null){
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
	        String line = null;
	        StringBuilder sb = new StringBuilder();
	       
	        Session session = HibernateUtils.getSessionFactory().openSession();
	        try {
	        	while((line = br.readLine()) != null) {
	        		if (line.trim().endsWith(";")){
	        			session.beginTransaction();
	        			sb.append(line.replace(";", ""));
	        			session.createNativeQuery(sb.toString()).executeUpdate();
	        			session.getTransaction().commit();
	        			sb = new StringBuilder();
	        		}
	        		else{
	        			sb.append(line);
	        		}
	        	}
	        } catch (IOException e) {
	        	// TODO Auto-generated catch block
	        	e.printStackTrace();
	        }
		}
	}
	
	public BCryptPasswordEncoder encoder() {
	    return new BCryptPasswordEncoder();
	}
	
	public void setSuccessMessage(String message, PluginManager pm){
		FrameController frameController = (FrameController)pm.getService("FrameController");
		JPanel bottom = frameController.getView().getBottomPanel();
	    JLabel labelBottom = new JLabel(message);
	    JOptionPane.showMessageDialog(frameController.getView(), message, "Info", JOptionPane.INFORMATION_MESSAGE);
		bottom.setLayout(new FlowLayout(FlowLayout.LEFT));
		bottom.removeAll();
		bottom.add(labelBottom);
		bottom.revalidate();
		bottom.repaint();
	}
}
