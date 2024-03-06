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
package models.user;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import models.user.UserModel;

import core.HibernateUtils;

public class Repository {
    
    private Repository() {};
    
    public static UserModel getUserById(Long id) {
        UserModel user;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            user = session.get(UserModel.class, id);
        }
        return user;
    }  
    
    public static UserModel getUserByUsername(String username) {
        UserModel user;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
        	Criteria criteria = session.createCriteria(UserModel.class);
        	user = (UserModel) criteria.add(Restrictions.eq("username", username))
        	                             .uniqueResult();
        }
        return user;
    }  
    
    public static UserModel getActiveUserByUsername(String username) {
        UserModel user;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
        	Criteria criteria = session.createCriteria(UserModel.class);
        	Criterion restUsername = Restrictions.eq("username", username);
        	Criterion restActive = Restrictions.eq("active", true); 
        	user = (UserModel) criteria.add(Restrictions.and(restUsername, restActive))
        	                             .uniqueResult();
        }
        return user;
    }  
    
    public static List<UserModel> getUsers() {
        List<UserModel> users;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            users = session.createQuery("from UserModel").list();
        }
        return users;
    }
    
    public static void save(UserModel user) {
        
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            session.beginTransaction();
            
            session.saveOrUpdate(user);
            
            session.getTransaction().commit();
        }
    }
}
