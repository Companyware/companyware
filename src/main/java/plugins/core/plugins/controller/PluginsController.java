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
package plugins.core.plugins.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import models.plugin.PluginModel;
import models.plugin.Repository;
import plugins.core.plugins.model.*;
import pluginmanager.plugininterfaces.PluginManager;
import pluginmanager.plugininterfaces.Service;
import plugins.core.plugins.view.Plugins;

public class PluginsController implements ActionListener, Service{

	private static final Log log = LogFactory.getLog(PluginsController.class);
	private PluginsModel model;
	private Plugins view;
	private String name;

	public PluginsController(PluginManager pm) {
		this.model = new PluginsModel();
		pm.registerService("PluginsController",this);
		view = new Plugins(pm);
		model.addObserver(view);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		log.info("test action pluginscontroller");
		this.model.printOut("ACTION pluginscontroller: " + e.getActionCommand().toString());
		switch(e.getActionCommand().toString()) {
		case "Pluginmanager":
		{
			this.createPluginManagerOverview();
			break;
		}
		case "installPlugin":
		{
			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter zipFilter = new FileNameExtensionFilter("Zipped Files (*.zip)", "zip");
			fileChooser.setFileFilter(zipFilter);
			int returnValue = fileChooser.showOpenDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				File dir;
				File newFile;
				if(this.isJarFile()){
					dir = new File("classes/plugins/community",FilenameUtils.removeExtension(selectedFile.getName().toLowerCase()));
					newFile = new File("classes/plugins/community",selectedFile.getName().toLowerCase());
				}
				else{
					dir = new File("target/classes/plugins/community",FilenameUtils.removeExtension(selectedFile.getName().toLowerCase()));
					newFile = new File("target/classes/plugins/community",selectedFile.getName().toLowerCase());
				}
				if (!dir.exists()){
				    dir.mkdirs();
				}
				try {
					Files.copy(selectedFile.toPath(), Paths.get(newFile.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
					this.unzip(newFile.getAbsolutePath(), dir.getAbsolutePath(), FilenameUtils.removeExtension(selectedFile.getName().toLowerCase()));
					newFile.delete();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			break;
		}
		}
	}

	public boolean isJarFile(){
		URI uri = null;
		URL url = getClass().getResource("/");
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
			return true;
		}
		return false;
	}
	
	private static void unzip(String zipFilePath, String destDir, String zipDirectory) {
		File dir = new File(destDir);
		// create output directory if it doesn't exist
		if(!dir.exists()) dir.mkdirs();
		FileInputStream fis;
		//buffer for read and write data to file
		byte[] buffer = new byte[1024];
		try {
			fis = new FileInputStream(zipFilePath);
			ZipInputStream zis = new ZipInputStream(fis);
			ZipEntry ze = zis.getNextEntry();
			while(ze != null){
				String fileName = ze.getName();
				File newFile = new File(destDir);
			
				File parent = newFile.getParentFile();
		        if (!parent.isDirectory() && !parent.mkdirs()) {
		            throw new IOException("Failed to create directory " + parent);
		        }
				
				if (ze.isDirectory()) {
					newFile = new File(destDir.replace(zipDirectory, "") + File.separator + fileName);
			        newFile.mkdirs();
			    } else {
			    	newFile = new File(destDir.replace(zipDirectory, "") + File.separator + fileName);
			    	//create directories for sub directories in zip
			    	FileOutputStream fos = new FileOutputStream(newFile);
					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}
					fos.close();
					//close this ZipEntry
					zis.closeEntry();
			    }
				ze = zis.getNextEntry();
			}
			//close last ZipEntry
			zis.closeEntry();
			zis.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createPluginManagerOverview(){
		view.createTableOverview();
	}
	
	public void setPlugin(String name){
		this.name = name;
	}
	
	public PluginModel getPluginByName(){
		PluginModel pluginModel = Repository.getPluginByName(this.name);
		return pluginModel;
	}
	
	public void deletePlugin(){
		Repository.deleteByName(this.name);

		view.createTableOverview();
	}
}
