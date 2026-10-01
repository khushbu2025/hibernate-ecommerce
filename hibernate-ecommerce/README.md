# E-Commerce Application (Hibernate ORM & JPA)

A complete Object-Relational Mapping (ORM) based E-commerce system built with Java 17, Hibernate 6, Jakarta Persistence API (JPA), and MySQL using Maven.

---

## Project Structure

The project follows a standard Maven directory layout:

```text
HibernateEcommerce
│
├── src/main/java
│   └── com/ecommerce
│       ├── dao
│       │   ├── CategoryDAO.java             # CRUD logic for categories
│       │   ├── OrdersDAO.java               # Order placement & cascading
│       │   ├── ProductDAO.java              # Catalog & HQL query logic
│       │   └── UsersDAO.java                # User registration & lookup
│       │
│       ├── entity
│       │   ├── Category.java                # Category entity mapping
│       │   ├── OrderDetails.java            # Order line item mapping
│       │   ├── Orders.java                  # Orders entity mapping
│       │   ├── Product.java                 # Product entity mapping
│       │   ├── Role.java                    # Role enum (ADMIN, CUSTOMER)
│       │   └── Users.java                   # User account entity mapping
│       │
│       ├── main
│       │   ├── App.java                     # Main workflow runner
│       │   └── TestConnection.java          # DB connection & SessionFactory test
│       │
│       └── util
│           └── HibernateUtil.java           # Singleton SessionFactory setup
│
├── src/main/resources
│   ├── hibernate.cfg.xml                    # Database connection & entity mapping config
│   └── schema.sql                           # Standalone SQL schema & sample data
│
├── src/test/java
│   └── com/ecommerce/test
│       └── EcommerceDAOTest.java            # Automated CRUD & relationship unit tests
│
├── pom.xml                                  # Maven dependencies & plugins
├── .gitignore                               # Git ignore rules for Maven & IDE files
└── README.md                                # Project documentation
```

---

## Technologies Used

* Java 17 (OpenJDK)
* Hibernate ORM 6.6.1.Final
* Jakarta Persistence API 3.1.0
* MySQL Server 8.0
* MySQL Connector/J 9.0.0
* Apache Maven
* JUnit 5 (Jupiter)
* Eclipse IDE

---

## Database Configuration

* **Database Engine:** MySQL
* **Database Name:** ecommerce_db
* **Default Port:** 3306
* **Hibernate Dialect:** org.hibernate.dialect.MySQLDialect
* **Schema Generation Mode:** `update` (automatically creates and syncs tables, constraints, and foreign keys)

---

## Entity Relationships & JPA Annotations

| Entity | Target Entity | Relationship | Key Annotations Used |
| :--- | :--- | :--- | :--- |
| Category | Product | One-to-Many | `@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)` |
| Product | Category | Many-to-One | `@ManyToOne`, `@JoinColumn(name = "category_id", nullable = false)` |
| Users | Orders | One-to-Many | `@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)` |
| Orders | Users | Many-to-One | `@ManyToOne`, `@JoinColumn(name = "user_id", nullable = false)` |
| Orders | OrderDetails | One-to-Many | `@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)` |
| OrderDetails | Orders | Many-to-One | `@ManyToOne`, `@JoinColumn(name = "order_id", nullable = false)` |
| OrderDetails | Product | Many-to-One | `@ManyToOne`, `@JoinColumn(name = "product_id", nullable = false)` |

### Key Annotations Used
* `@Entity` and `@Table`: Binds Java model classes directly to MySQL tables.
* `@Id` and `@GeneratedValue(strategy = GenerationType.IDENTITY)`: Manages auto-increment primary keys natively in MySQL.
* `@Enumerated(EnumType.STRING)`: Saves user role constants (ADMIN, CUSTOMER) as clean strings.
* `@Column(nullable = false, unique = true)`: Enforces column-level data constraints directly through Java entities.

---

## How to Run & Verify

1. **Verify Database Connection**
   * Open `src/main/java/com/ecommerce/main/TestConnection.java`
   * Right-click and choose **Run As -> Java Application**
   * Output verifies that the database connects and Hibernate SessionFactory is initialized.

2. **Run Main Demonstration**
   * Open `src/main/java/com/ecommerce/main/App.java`
   * Right-click and choose **Run As -> Java Application**
   * Performs complete end-to-end execution: inserts categories and products, registers users, places orders with cascade line items, and runs HQL queries.

3. **Run Automated Unit Tests**
   * Open `src/test/java/com/ecommerce/test/EcommerceDAOTest.java`
   * Right-click and choose **Run As -> JUnit Test**
   * All CRUD and relationship tests execute in order and pass with 0 failures and 0 errors.
   
