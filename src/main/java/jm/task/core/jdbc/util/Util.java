package jm.task.core.jdbc.util;

import org.hibernate.SessionFactory;
import jm.task.core.jdbc.model.User;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {

    private static final String URL = "jdbc:mysql://localhost:3306/mydbtest?useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DIALECT = "org.hibernate.dialect.MySQL5Dialect";

    private static Connection connection;
    private static SessionFactory sessionFactory;

    private Util() {

    }

    public static Connection getConnection() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static SessionFactory getSessionFactory() {

        if (sessionFactory == null) {

            try {
                Configuration configuration = new Configuration();
                Properties properties = new Properties();

                properties.put(Environment.DRIVER, DRIVER);
                properties.put(Environment.URL, URL);
                properties.put(Environment.USER, USERNAME);
                properties.put(Environment.PASS, PASSWORD);
                properties.put(Environment.DIALECT, DIALECT);
                properties.put(Environment.SHOW_SQL, "true");
                properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                properties.put(Environment.HBM2DDL_AUTO, "");

                configuration.setProperties(properties);
                configuration.addAnnotatedClass(User.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return sessionFactory;
    }

    public static void closeSessionFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

}
