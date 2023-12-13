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
package plugins.community.mbduscustomer.model;

import java.util.List;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import core.HibernateUtils;

public class Repository {
    
    private Repository() {};
    
    public static CustomerModel getCustomerById(Long id) {
        
        CustomerModel customer;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            customer = session.get(CustomerModel.class, id);
        }
        
        return customer;
    }    
    
    public static CustomerModel getCustomerByEmail(String email) {
    	CustomerModel customer;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
        	// Create CriteriaBuilder
        	CriteriaBuilder builder = session.getCriteriaBuilder();
        	// Create CriteriaQuery
        	CriteriaQuery criteriaQuery = builder.createQuery(CustomerModel.class);
        	
        	Root<CustomerModel> root = criteriaQuery.from(CustomerModel.class);
        	ParameterExpression<String> emailParam = builder.parameter(String.class);
        	
        	criteriaQuery.select(root).where(builder.like(root.get("email"), emailParam));
        	
        	TypedQuery<CustomerModel> query = session.createQuery(criteriaQuery);
        	query.setParameter(emailParam, email);
        	customer = query.getSingleResult();
        }
         
        return customer;
    }    
    
    public static List<CustomerModel> getCustomers() {
        
        List<CustomerModel> customers;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            customers = session.createQuery("from CustomerModel").getResultList();
        }
        return customers;
    }
    
    public static void save(CustomerModel customer) {
        
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            session.beginTransaction();
            
            session.saveOrUpdate(customer);
            
            session.getTransaction().commit();
        }
    }
    
    public static void deleteByEmail(String email) {
    	
    	CustomerModel customer = getCustomerByEmail(email);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            session.beginTransaction();
   
            session.remove(customer);
            
            session.getTransaction().commit();
        }
    }
}
