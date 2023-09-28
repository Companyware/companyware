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
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Autowired;

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
        	
        	File file = new File("src/main/java");
        	Files.walk(file.toPath())
            .filter(path -> !Files.isDirectory(path))
            .forEach(path -> {
            	 if(path.toString().endsWith("hbm.xml")) {
            		 String resourcePath = path.toString();
            		 resourcePath = resourcePath.replace("src\\main\\java\\", "");
            		 resourcePath = resourcePath.replace("src/main/java/", "");
            		 config.addResource(resourcePath);
            	 }
            });
        	String fileSeparator = FileSystems.getDefault().getSeparator();
        	File installFile = new File("src"+fileSeparator+"main"+fileSeparator+"java"+fileSeparator+"install.lck");
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
