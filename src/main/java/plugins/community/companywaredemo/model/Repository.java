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
package plugins.community.companywaredemo.model;

import java.util.List;
import org.hibernate.Session;
import core.HibernateUtils;

public class Repository {
    
    private Repository() {};
    
    public static DemoModel getDemoUserById(Long id) {
        
        DemoModel demoUser;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            demoUser = session.get(DemoModel.class, id);
        }
        
        return demoUser;
    }    
    
    public static List<DemoModel> getDemoUsers() {
        
        List<DemoModel> demoUsers;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            demoUsers = session.createQuery("from DemoModel").getResultList();
        }
        return demoUsers;
    }
    
    public static void save(DemoModel demoUser) {
        
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            session.beginTransaction();
            
            session.saveOrUpdate(demoUser);
            
            session.getTransaction().commit();
        }
    }
}
