package com.ecommerce.main;

import org.hibernate.SessionFactory;
import com.ecommerce.util.HibernateUtil;

public class TestConnection {

    public static void main(String[] args) {

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        if (sessionFactory != null) {
            System.out.println("Hibernate SessionFactory created successfully!");
        }

        sessionFactory.close();
    }
}

