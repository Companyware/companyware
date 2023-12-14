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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import javax.annotation.PreDestroy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.derby.drda.NetworkServerControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import core.ApplicationContextProvider;
import core.HibernateUtils;
import core.StartProgressBar;
import pluginmanager.PluginManagerFactory;
import pluginmanager.plugininterfaces.PluginManager;
import plugins.core.frame.controller.FrameController;
import plugins.core.login.controller.LoginController;


@ComponentScan(basePackages = "/src/main")
@SpringBootApplication
public class Companyware implements CommandLineRunner{	
	
	private static final Log log = LogFactory.getLog(Companyware.class);
	
	@Autowired
	private Environment env;

	public static void main(String[] args) throws Exception{
		NetworkServerControl derbyserver =   new NetworkServerControl(InetAddress.getByName("localhost"), 1527);
        derbyserver.start(null);
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				StartProgressBar progressBar = new StartProgressBar();
				try {
					try {
						progressBar.initializeUI();
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            progressBar.setVisible(true);
			}
        });
		new SpringApplicationBuilder(Companyware.class).headless(false).run(args);
	}
	
	@Override
    public void run(String... args) throws FileNotFoundException {
		new ApplicationContextProvider();
		PluginManager pm = PluginManagerFactory.createPluginManager("target/classes/plugins");
		pm.init();
		FrameController frameController = (FrameController)pm.getService("FrameController");
		LoginController loginController = (LoginController)pm.getService("LoginController");
		
	//	core.Companyware.getContainer().executeUpdateSql();
		if(frameController!=null){
			core.Companyware.getContainer().setFirstRun();
			frameController.getView().setVisible(true);
			loginController.getView().setVisible(true);
		}
	}
	
	@PreDestroy
	public void onExit() throws Exception {
	    log.info("###STOP FROM THE LIFECYCLE###");
	    HibernateUtils.shutdown();
	}
}