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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import plugins.core.menu.view.Menu;

import pluginmanager.plugininterfaces.ActionProcessor;
import pluginmanager.plugininterfaces.Plugin;
import pluginmanager.plugininterfaces.PluginManager;
import plugins.community.companywaredemo.controller.CompanywaredemoController;

public class Companywaredemo implements Plugin {
	
	private static final Log log = LogFactory.getLog(Companywaredemo.class);

	@Override
	public void init(PluginManager pm) {
		log.info("Companywaredemo initialized in package java class!");
	
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
}