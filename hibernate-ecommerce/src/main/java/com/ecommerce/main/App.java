package com.ecommerce.main;

import java.time.LocalDate;
import java.util.List;

import com.ecommerce.dao.CategoryDAO;
import com.ecommerce.dao.OrdersDAO;
import com.ecommerce.dao.ProductDAO;
import com.ecommerce.dao.UsersDAO;
import com.ecommerce.entity.Category;
import com.ecommerce.entity.OrderDetails;
import com.ecommerce.entity.Orders;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.Role;
import com.ecommerce.entity.Users;
import com.ecommerce.util.HibernateUtil;

public class App {

    public static void main(String[] args) {

        CategoryDAO categoryDAO = new CategoryDAO();
        ProductDAO productDAO = new ProductDAO();
        UsersDAO usersDAO = new UsersDAO();
        OrdersDAO ordersDAO = new OrdersDAO();

        System.out.println("==================================================");
        System.out.println("1. CREATING CATEGORY & PRODUCTS");
        System.out.println("==================================================");

        Category electronics = new Category("Electronics", "Gadgets, laptops and devices");
        categoryDAO.saveCategory(electronics);
        System.out.println("Saved Category: " + electronics.getName() + " (ID: " + electronics.getId() + ")");

        Product laptop = new Product("Gaming Laptop", 75000.0, 10, electronics);
        Product mouse = new Product("Wireless Mouse", 1200.0, 50, electronics);
        productDAO.saveProduct(laptop);
        productDAO.saveProduct(mouse);
        System.out.println("Saved Products: " + laptop.getName() + ", " + mouse.getName());

        System.out.println("\n==================================================");
        System.out.println("2. CREATING USER");
        System.out.println("==================================================");

        Users customer = new Users("khushbu_k", "pass1234", "khushbu@example.com", Role.CUSTOMER);
        usersDAO.saveUser(customer);
        System.out.println("Saved User: " + customer.getUsername() + " with role: " + customer.getRole());

        System.out.println("\n==================================================");
        System.out.println("3. PLACING AN ORDER (CASCADING TO ORDER_DETAILS)");
        System.out.println("==================================================");

        Orders order = new Orders(LocalDate.now(), 76200.0, customer);

        OrderDetails item1 = new OrderDetails(1, 75000.0, order, laptop);
        OrderDetails item2 = new OrderDetails(1, 1200.0, order, mouse);

        order.getOrderDetails().add(item1);
        order.getOrderDetails().add(item2);

        // Saving order automatically cascades and persists item1 and item2 into order_details table
        ordersDAO.saveOrder(order);
        System.out.println("Saved Order with ID: " + order.getId() + ", Total Amount: " + order.getTotalAmount());

        System.out.println("\n==================================================");
        System.out.println("4. FETCHING ORDER WITH LINE ITEMS");
        System.out.println("==================================================");

        Orders fetchedOrder = ordersDAO.getOrderWithDetails(order.getId());
        if (fetchedOrder != null) {
            System.out.println("Order ID: " + fetchedOrder.getId() + " placed by: " + fetchedOrder.getUser().getUsername());
            for (OrderDetails detail : fetchedOrder.getOrderDetails()) {
                System.out.println(" -> Item: " + detail.getProduct().getName() 
                                   + " | Qty: " + detail.getQuantity() 
                                   + " | Price: Rs." + detail.getUnitPrice());
            }
        }

        System.out.println("\n==================================================");
        System.out.println("5. UPDATING A PRODUCT");
        System.out.println("==================================================");

        laptop.setPrice(72999.0);
        laptop.setStockQuantity(9);
        productDAO.updateProduct(laptop);
        System.out.println("Updated " + laptop.getName() + " new price: " + laptop.getPrice());

        System.out.println("\n==================================================");
        System.out.println("6. QUERYING PRODUCTS BY CATEGORY");
        System.out.println("==================================================");

        List<Product> products = productDAO.getProductsByCategoryId(electronics.getId());
        for (Product p : products) {
            System.out.println("Category: " + p.getCategory().getName() + " -> Product: " + p.getName() + " (Rs." + p.getPrice() + ")");
        }

        System.out.println("\n==================================================");
        System.out.println("DEMO COMPLETED SUCCESSFULLY");
        System.out.println("==================================================");

        HibernateUtil.getSessionFactory().close();
    }
}
