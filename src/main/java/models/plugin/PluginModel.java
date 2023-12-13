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
package models.plugin;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import plugins.core.plugins.model.DisplayAs;
import models.pluginsettings.PluginSettingsModel;

@Entity
@Table(name = "Plugins")
public class PluginModel {

	public PluginModel() {
		// TODO Auto-generated constructor stub
	}
	
    private Long id;
    private String name;
    private String author;
    private String version;
    private Boolean active;
    private String namespace;
    private JCheckBox action;
    private Integer prio;
    private String path;
    private JButton button;
    
    private Set<PluginSettingsModel> pluginsettings;
	
    
	public Set<PluginSettingsModel> getPluginsettings() {
		return pluginsettings;
	}

	public void setPluginsettings(Set<PluginSettingsModel> pluginsettings) {
		this.pluginsettings = pluginsettings;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @DisplayAs(value = "plugin.name", index = 0, editable = false)
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @DisplayAs(value = "plugin.author", index = 1, editable = false)
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    @DisplayAs(value = "plugin.version", index = 2, editable = false)
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
    
    @DisplayAs(value = "plugin.active", index = 3, editable = true)
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
    
    @DisplayAs(value = "plugin.prio", index = 4, editable = true)
    public Integer getPrio() {
 		return prio;
 	}

 	public void setPrio(Integer prio) {
 		this.prio = prio;
 	}
    
    @DisplayAs(value = "plugin.action", index = 5, editable = true)
    public JCheckBox getAction() {
        return action;
    }

    public void setAction(JCheckBox action) {
        this.action = action;
    }
    
    @DisplayAs(value = "", index = 6, editable = true)
    public JButton getButton() {
		return button;
	}

	public void setButton(JButton button) {
		this.button = button;
	}
 	
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    @Override
    public String toString() {
        return String.format("PluginModel Id: %d Name: %s Namespace: %d Active: %s", 
                id, name, namespace, active);
    } 
}