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

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;
import core.HibernateUtils;

public class Repository {
    
    private Repository() {};
    
    public static PluginModel getPluginById(Long id) {
        PluginModel plugin;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            plugin = session.get(PluginModel.class, id);
        }
        return plugin;
    }  
    
    public static PluginModel getPluginByName(String name) {
    	PluginModel plugin;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
        	// Create CriteriaBuilder
        	CriteriaBuilder builder = session.getCriteriaBuilder();
        	// Create CriteriaQuery
        	CriteriaQuery criteriaQuery = builder.createQuery(PluginModel.class);
        	
        	Root<PluginModel> root = criteriaQuery.from(PluginModel.class);
        	ParameterExpression<String> nameParam = builder.parameter(String.class);
        	
        	criteriaQuery.select(root).where(builder.like(root.get("name"), nameParam));
        	
        	TypedQuery<PluginModel> query = session.createQuery(criteriaQuery);
        	query.setParameter(nameParam, name);
        	plugin = query.getSingleResult();
        }
         
        return plugin;
    }    
    
    public static List<PluginModel> getPlugins() {
        List<PluginModel> plugins;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            plugins = session.createQuery("from PluginModel").list();
        }
        return plugins;
    }
    
    @SuppressWarnings("deprecation")
	public static List<PluginModel> getCommunityPlugins() {
        List<PluginModel> plugins;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Query query = session.createQuery("from PluginModel where namespace =:namespace");
            query.setParameter("namespace", "community");
            plugins = query.list();
        }
        return plugins;
    }
    
    public static void save(PluginModel plugin) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(plugin);
            session.getTransaction().commit();
        }
    }
    
  public static void deleteByName(String name) {
    	
    	PluginModel plugin = getPluginByName(name);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            session.beginTransaction();
   
            session.remove(plugin);
            
            session.getTransaction().commit();
        }
    }
}
