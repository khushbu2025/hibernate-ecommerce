package com.ecommerce.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

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

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EcommerceDAOTest {

    private static CategoryDAO categoryDAO;
    private static ProductDAO productDAO;
    private static UsersDAO usersDAO;
    private static OrdersDAO ordersDAO;

    @BeforeAll
    public static void setup() {
        categoryDAO = new CategoryDAO();
        productDAO = new ProductDAO();
        usersDAO = new UsersDAO();
        ordersDAO = new OrdersDAO();
    }

    @Test
    @Order(1)
    public void testCreateAndFetchCategoryWithProducts() {
        Category cat = new Category("Books", "Printed and digital books");
        categoryDAO.saveCategory(cat);
        if (cat.getId() == null) {
            throw new AssertionError("Category ID should not be null");
        }

        Product book = new Product("Clean Code", 450.0, 25, cat);
        productDAO.saveProduct(book);
        if (book.getId() == null) {
            throw new AssertionError("Product ID should not be null");
        }

        List<Product> products = productDAO.getProductsByCategoryId(cat.getId());
        if (products.isEmpty() || !products.get(0).getName().equals("Clean Code")) {
            throw new AssertionError("Product not found by Category ID");
        }
    }

    @Test
    @Order(2)
    public void testUserCreationAndLookup() {
        String testUser = "test_user_" + System.currentTimeMillis();
        Users user = new Users(testUser, "securePass", testUser + "@example.com", Role.CUSTOMER);
        usersDAO.saveUser(user);
        if (user.getId() == null) {
            throw new AssertionError("User ID should not be null");
        }

        Users fetched = usersDAO.getUserByUsername(testUser);
        if (fetched == null || fetched.getRole() != Role.CUSTOMER) {
            throw new AssertionError("User not fetched correctly or role mismatch");
        }
    }

    @Test
    @Order(3)
    public void testOrderCascadeAndItemDetails() {
        Category cat = new Category("Stationery", "Office and school supplies");
        categoryDAO.saveCategory(cat);

        Product notebook = new Product("Notebook", 80.0, 100, cat);
        productDAO.saveProduct(notebook);

        String orderUser = "buyer_" + System.currentTimeMillis();
        Users user = new Users(orderUser, "pass123", orderUser + "@example.com", Role.CUSTOMER);
        usersDAO.saveUser(user);

        Orders order = new Orders(LocalDate.now(), 160.0, user);
        OrderDetails detail = new OrderDetails(2, 80.0, order, notebook);
        order.getOrderDetails().add(detail);

        ordersDAO.saveOrder(order);
        if (order.getId() == null) {
            throw new AssertionError("Order ID should not be null");
        }

        Orders fetchedOrder = ordersDAO.getOrderWithDetails(order.getId());
        if (fetchedOrder == null || fetchedOrder.getOrderDetails().size() != 1) {
            throw new AssertionError("Order cascade details mismatch");
        }
    }
}