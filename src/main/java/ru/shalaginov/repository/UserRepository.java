package ru.shalaginov.repository;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import ru.shalaginov.connect.DataConnect;
import ru.shalaginov.enity.User;
import ru.shalaginov.util.Logs;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class UserRepository implements UserRepositoryInt{

    private final Session session;

    public UserRepository() {
        this.session = DataConnect.getSession();
    }

    @Override
    public User create(User user) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();

            Logs.info("Пользователь создан с ID: " + user.getId());
            return user;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            Logs.error("Ошибка при создании пользователя", e);
            return null;
        }
    }

    @Override
    public User findById(int userId) {
        try {
            return session.find(User.class, userId);
        } catch (HibernateException e) {
            Logs.error("Ошибка при поиске пользователя с id: " + userId, e);
            return null;
        }
    }

    @Override
    public List<User> findAll() {
        try {
            return session.createQuery("FROM User", User.class).list();
        } catch (HibernateException e) {
            Logs.error("Ошибка при получении списка пользователей", e);
            return new ArrayList<>();
        }
    }

    @Override
    public User update(User user) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            User merged = session.merge(user);
            transaction.commit();

            Logs.info("Пользователь обновлён, id: " + user.getId());
            return merged;
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            Logs.error("Ошибка при обновлении пользователя с id: " + user.getId(), e);
            return null;
        }
    }

    @Override
    public boolean delete(int userId) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            User user = session.find(User.class, userId);

            if (user != null) {
                session.remove(user);
                transaction.commit();
                Logs.info("Пользователь удалён, id: " + userId);
                return true;
            } else {
                transaction.rollback();
                Logs.info("Пользователь с id " + userId + " не найден для удаления");
                return false;
            }
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            Logs.error("Ошибка при удалении пользователя с id: " + userId, e);
            return false;
        }
    }

    @Override
    public boolean existsById(int userId) {
        try {
            return session.find(User.class, userId) != null;
        } catch (HibernateException e) {
            Logs.error("Ошибка при проверке существования пользователя с id: " + userId, e);
            return false;
        }
    }


}