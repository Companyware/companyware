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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javassist.expr.Instanceof;

public class WildcardReloadableResourceBundleMessageSource extends ReloadableResourceBundleMessageSource {

	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	private static final Log log = LogFactory.getLog(WildcardReloadableResourceBundleMessageSource.class);
	
	@Override
	public void setBasenames(String... basenames) {
		if (basenames != null) {
			List<String> baseNames = new ArrayList<String>();
			for (int i = 0; i < basenames.length; i++) {
				String basename = StringUtils.trimToEmpty(basenames[i]);
				if (StringUtils.isNotBlank(basename)) {
					try {
						Resource[] resources = resourcePatternResolver.getResources(basename);
						for (int j = 0; j < resources.length; j++) {
							Resource resource = resources[j];
							String uri = resource.getURI().toString();
							String baseName = null;
	
							if (resource instanceof FileSystemResource) {
								baseName = "classpath:" + StringUtils.substringBetween(uri, "/classes/", ".properties");
							} else if (resource instanceof ClassPathResource) {
								baseName = StringUtils.substringBefore(uri, ".properties");
							} else if (resource instanceof UrlResource) {
								if(uri.contains(".jar!")){
									baseName = "classpath:" + StringUtils.substringBetween(uri, ".jar!/", ".properties");
								}
								if(uri.contains("classes!")){
									baseName = "classpath:" + StringUtils.substringBetween(uri, "classes!/", ".properties");
								}
							}
						
							if (baseName != null) {
								String fullName = processBasename(baseName);
								baseNames.add(fullName);
							}
						}
					} catch (IOException e) {
						log.debug("No message source files found for basename " + basename + ".");
					}
				}
				String[] resourceBasenames = baseNames.toArray(new String[baseNames.size()]);
				super.setBasenames(resourceBasenames);
			}
		}
	}

	String processBasename(String baseName) {
		String prefix = StringUtils.substringBeforeLast(baseName, "/");
		String name = StringUtils.substringAfterLast(baseName, "/");
		do {
			name = StringUtils.substringBeforeLast(name, "_");
		} while (name.contains("_"));
		return prefix + "/" + name;
	}
}