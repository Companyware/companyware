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
package plugins.community.companywaredemo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

import core.HibernateUtils;
import pluginmanager.plugininterfaces.ActionProcessor;
import pluginmanager.plugininterfaces.Plugin;
import pluginmanager.plugininterfaces.PluginManager;
import plugins.community.companywaredemo.controller.CompanywaredemoController;

public class Companywaredemo implements Plugin {
	
	private static final Log log = LogFactory.getLog(Companywaredemo.class);

	@Override
	public void init(PluginManager pm) {
		log.info("Companywaredemo initialized in package java class!");
		Boolean firstRun = this.firstRun();
		
		if(firstRun){
			try {
				this.installSql();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		pm.registerService("CompanywaredemoController", new CompanywaredemoController(pm));
		// service test
		pm.registerService("testService", new TestService());
		TestService testService = (TestService) pm.getService("testService");
		testService.testFunction();
		
		// action test
		pm.addActionProcessor("test_hook_companywaredemo", new ActionProcessor() {
			
			@Override
			public void call(Map<String, Object> context) {
				log.info("Test hook in Companywaredemo called!");
			}
		});
		pm.callAction("test_hook_companywaredemo", new HashMap<String, Object>());
	}
	
	public Boolean firstRun(){
			URL url = Companywaredemo.class.getResource("/");
	        URI uri = null;
			try {
				if(url!=null){
					uri = url.toURI();
				}
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	     
			String fileSeparator = FileSystems.getDefault().getSeparator();
			File installFile = new File(uri.getPath()+"plugins"+fileSeparator+"community"+fileSeparator+"companywaredemo"+fileSeparator+"install.lck");
			
			if(!installFile.exists()){
				try {
					installFile.createNewFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} // if file already exists will do nothing 
				try {
					FileOutputStream oFile = new FileOutputStream(installFile, false);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				return true;
			}
			return false;
	}
	
	public void installSql() throws SQLException{
		URL url = Companywaredemo.class.getResource("/");
        URI uri = null;
		try {
			if(url!=null){
				uri = url.toURI();
			}
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (uri != null){
			String fileSeparator = FileSystems.getDefault().getSeparator();
			InputStream is = getClass().getClassLoader()
					.getResourceAsStream("plugins"+fileSeparator+"community"+fileSeparator+"companywaredemo"+fileSeparator+"setup"+fileSeparator+"firstRun.sql");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
	
			String line = null;
			StringBuilder sb = new StringBuilder();
	
			SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
			
			Connection conn;
			conn = sessionFactory.getSessionFactoryOptions().getServiceRegistry().getService(ConnectionProvider.class).getConnection();
			
			DatabaseMetaData dmd = conn.getMetaData();
			ResultSet rs = dmd.getTables(null,"APP", "DEMO",null);
			if (!rs.next()) {
				try {
					Session session = HibernateUtils.getSessionFactory().openSession();
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
	}
}