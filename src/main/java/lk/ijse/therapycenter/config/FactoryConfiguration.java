package lk.ijse.therapycenter.config;

import lk.ijse.therapycenter.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.io.*;
import java.util.Properties;

public class FactoryConfiguration {

    private static SessionFactory sessionFactory;

    private FactoryConfiguration() {}

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration config = new Configuration();

                Properties props = new Properties();
                try (InputStream is = FactoryConfiguration.class
                        .getClassLoader().getResourceAsStream("hibernate.properties")) {
                    if (is == null)
                        throw new RuntimeException("hibernate.properties not found in classpath!");
                    props.load(is);
                }
                config.addProperties(props);

                config.addAnnotatedClass(User.class);
                config.addAnnotatedClass(Therapist.class);
                config.addAnnotatedClass(TherapyProgram.class);
                config.addAnnotatedClass(Patient.class);
                config.addAnnotatedClass(TherapySession.class);
                config.addAnnotatedClass(Payment.class);

                sessionFactory = config.buildSessionFactory();

            } catch (Exception e) {
                System.err.println("SessionFactory creation failed: " + e.getMessage());
                throw new ExceptionInInitializerError(e);
            }
        }
        return sessionFactory;
    }

    public static Session getSession() {
        return getSessionFactory().openSession();
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed())
            sessionFactory.close();
    }
}