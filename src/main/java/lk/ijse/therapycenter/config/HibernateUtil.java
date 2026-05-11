package lk.ijse.therapycenter.config;

import lk.ijse.therapycenter.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class HibernateUtil {

    private static SessionFactory sessionFactory;

    private HibernateUtil() {}

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration config = new Configuration();

                config.configure();

                config.addAnnotatedClass(User.class);
                config.addAnnotatedClass(Therapist.class);
                config.addAnnotatedClass(TherapyProgram.class);
                config.addAnnotatedClass(Patient.class);
                config.addAnnotatedClass(TherapySession.class);
                config.addAnnotatedClass(Payment.class);

                sessionFactory = config.buildSessionFactory();

            } catch (Exception e) {
                System.err.println("Failed to build SessionFactory: " + e.getMessage());
                throw new ExceptionInInitializerError(e);
            }
        }
        return sessionFactory;
    }

    public static Session getSession() {
        return getSessionFactory().openSession();
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}