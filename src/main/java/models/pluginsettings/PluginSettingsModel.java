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
package models.pluginsettings;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.swing.JCheckBox;

import models.plugin.PluginModel;
import plugins.core.plugins.model.DisplayAs;

@Entity
@Table(name = "PluginSettings")
public class PluginSettingsModel {

	public PluginSettingsModel() {
		// TODO Auto-generated constructor stub
	}
	
    private Long id;
    private Long pluginId;
    private String name;
    private String type;
    private String value;
    
    @ManyToOne
    @JoinColumn(name="pluginId", nullable=false)
    private PluginModel plugin;


	public PluginModel getPlugin() {
		return plugin;
	}

	public void setPlugin(PluginModel plugin) {
		this.plugin = plugin;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @DisplayAs(value = "Name", index = 0, editable = false)
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @DisplayAs(value = "Type", index = 1, editable = false)
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    @DisplayAs(value = "Value", index = 2, editable = false)
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    @DisplayAs(value = "PluginId", index = 4, editable = true)
    public Long getPluginId() {
 		return pluginId;
 	}

 	public void setPluginId(Long pluginId) {
 		this.pluginId = pluginId;
 	}
    
    @Override
    public String toString() {
        
        return String.format("PluginSettingsModel Id: %d Name: %s Type: %d Value: %s", 
                id, name, type, value);
    } 
}