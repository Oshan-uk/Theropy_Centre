package lk.ijse.therapycenter.config;

import lk.ijse.therapycenter.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class FactoryConfiguration {

    private static FactoryConfiguration instance;

    private final SessionFactory sessionFactory;

    private FactoryConfiguration() {

        Configuration configuration = new Configuration();

        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Patient.class);
        configuration.addAnnotatedClass(Therapist.class);
        configuration.addAnnotatedClass(TherapyProgram.class);
        configuration.addAnnotatedClass(TherapySession.class);
        configuration.addAnnotatedClass(Payment.class);

        sessionFactory = configuration.buildSessionFactory(new org.hibernate.boot.registry.StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build());
    }

    public static FactoryConfiguration getInstance(){

        return instance == null ? instance = new FactoryConfiguration() : instance;
    }

    public Session getSession(){

        return sessionFactory.openSession();
    }
}