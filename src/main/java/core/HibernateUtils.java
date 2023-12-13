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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import models.plugin.PluginModel;
import models.pluginsettings.PluginSettingsModel;
import models.user.UserModel;

public class HibernateUtils {

    private HibernateUtils() {}

    @Autowired
    private static final SessionFactory sessionFactory;

    static {
        try {
        	Configuration config = new Configuration().configure("hibernate.cfg.xml");
 	        
 	        URL url = HibernateUtils.class.getResource("/");
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
 				installFile = new File("classes"+fileSeparator+"install.lck");
 			} 
 			else{
 				file = new File("src/main/java");
 				installFile = new File("src"+fileSeparator+"main"+fileSeparator+"java"+fileSeparator+"install.lck");
 			}
 			config.addDirectory(file);

            if(installFile.exists()){
	        	config.setProperty("hibernate.hbm2ddl.auto", "none");
	        	config.setProperty("hbm2ddl.auto", "none");	
            }
            else{
            	FileOutputStream s = new FileOutputStream(installFile,false);
            	config.setProperty("hibernate.hbm2ddl.auto", "update");
	        	config.setProperty("hbm2ddl.auto", "update");	
            }
       // 	config.setProperty("hbm2ddl.import_files", "tables.sql");
            sessionFactory = config.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            System.out.println(ex.getMessage());
            System.out.println(ex.getStackTrace().toString());
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public static void shutdown() throws Exception {
    	getSessionFactory().close();
    }    
}
