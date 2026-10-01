package com.ecommerce.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.ecommerce.entity.Users;
import com.ecommerce.util.HibernateUtil;

public class UsersDAO {

    // 1. Create / Save a new User
    public void saveUser(Users user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    // 2. Read / Find User by ID
    public Users getUserById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Users.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 3. Read / Find User by Username (HQL)
    public Users getUserByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Users u WHERE u.username = :uname";
            Query<Users> query = session.createQuery(hql, Users.class);
            query.setParameter("uname", username);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 4. Read / Get all Users
    public List<Users> getAllUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Users", Users.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 5. Update an existing User
    public void updateUser(Users user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    // 6. Delete a User by ID
    public void deleteUser(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Users user = session.get(Users.class, id);
            if (user != null) {
                session.remove(user);
                System.out.println("User deleted with ID: " + id);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
