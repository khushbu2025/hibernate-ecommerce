package com.ecommerce.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.ecommerce.entity.Orders;
import com.ecommerce.util.HibernateUtil;

public class OrdersDAO {

    // 1. Create / Save an Order (Cascades to OrderDetails automatically)
    public void saveOrder(Orders order) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(order);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    // 2. Read / Find Order by ID
    public Orders getOrderById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Orders.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 3. Read / Get Order with its OrderDetails (Eager fetch via JOIN FETCH to avoid LazyInitializationException)
    public Orders getOrderWithDetails(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT o FROM Orders o LEFT JOIN FETCH o.orderDetails WHERE o.id = :orderId";
            Query<Orders> query = session.createQuery(hql, Orders.class);
            query.setParameter("orderId", id);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 4. Read / Get all Orders by User ID
    public List<Orders> getOrdersByUserId(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Orders o WHERE o.user.id = :uid";
            Query<Orders> query = session.createQuery(hql, Orders.class);
            query.setParameter("uid", userId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 5. Delete an Order by ID
    public void deleteOrder(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Orders order = session.get(Orders.class, id);
            if (order != null) {
                session.remove(order);
                System.out.println("Order deleted with ID: " + id);
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
