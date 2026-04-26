package ru.shalaginov.connect;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DataConnect {
    private static final SessionFactory sessionFactory = buildSessionFactory();
    private static Session currentSession;

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration()
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() {
        if (currentSession == null || !currentSession.isOpen()) {
            currentSession = sessionFactory.openSession();
        }
        return currentSession;
    }

    public static void closeConnection() {
        if (currentSession != null && currentSession.isOpen()) {
            currentSession.close();
        }
    }
}