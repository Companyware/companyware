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

import java.util.Locale;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class TextMessages extends ReloadableResourceBundleMessageSource{
	
    private WildcardReloadableResourceBundleMessageSource messageSource;
    
    private Locale locale;
    
	public TextMessages(){
		if(messageSource==null){
    		messageSource = new WildcardReloadableResourceBundleMessageSource();
    		String[] baseNames = org.springframework.util.StringUtils.commaDelimitedListToStringArray("classpath*:**/messages*");
            messageSource.setBasenames(baseNames);
    	}
    	
    	String[] baseNames = org.springframework.util.StringUtils.commaDelimitedListToStringArray("classpath*:**/messages*");
    	messageSource.setBasenames(baseNames);
    	
    	if(locale == null){
    		locale = new Locale("de", "DE");
    	}
    }
    
    public WildcardReloadableResourceBundleMessageSource getMessageSource(){
        return messageSource;
    }
    
    public void setMessageSource(WildcardReloadableResourceBundleMessageSource messageSource){
    	this.messageSource = messageSource;
    }
	
	public String get(String key){
		return this.messageSource.getMessage(key, null, key, locale);
	}
	
	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}


