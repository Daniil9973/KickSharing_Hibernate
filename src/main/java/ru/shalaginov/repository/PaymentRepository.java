package ru.shalaginov.repository;

import org.hibernate.Session;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import ru.shalaginov.enity.Payment;
import ru.shalaginov.util.Logs;
import ru.shalaginov.connect.DataConnect;


import java.util.ArrayList;
import java.util.List;

public class PaymentRepository implements PaymentRepositoryInt {


    private final Session session;

    public PaymentRepository() {
        this.session = DataConnect.getSession();
    }

    @Override
    public Payment create(Payment payment) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(payment);
            transaction.commit();

            Logs.info("Платёж создана с ID: " + payment.getId());
            return payment;
        } catch (HibernateException e) {
            if(transaction!=null) transaction.rollback();
            Logs.error("Ошибка при создании платежа", e);
            return null;
        }
    }

    @Override
    public Payment findById(int paymentId) {
        try {
            return session.find(Payment.class, paymentId);
        } catch (HibernateException e) {
            Logs.error("Ошибка при поиске платежа с id: " + paymentId, e);
            return null;
        }
    }

    @Override
    public List<Payment> findAll() {
        try {
            return session.createQuery("FROM Payment", Payment.class).list();
        } catch (HibernateException e) {
            Logs.error("Ошибка при получении списка платежей", e);
            return new ArrayList<>();
        }
    }

    @Override
    public Payment update(Payment payment) {
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();
            Payment merged = session.merge(payment);
            transaction.commit();

            Logs.info("Платеж обновлен, id: " + payment.getId());
            return merged;
        } catch (HibernateException e){
            if(transaction!=null) transaction.rollback();
            Logs.error("Ошибка при обновлении платежа с id: " + payment.getId(), e);
            return null;
        }
    }

    @Override
    public boolean delete(int paymentId) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Payment payment = session.find(Payment.class,paymentId);

            if (payment != null) {
                session.remove(payment);
                transaction.commit();
                Logs.info("Платеж удален, id: " + paymentId);
                return true;
            } else {
                transaction.rollback();
                Logs.info("Платеж с id " + paymentId + " не найдена для удаления");
                return false;
            }

        } catch (HibernateException e){
            if (transaction!=null)transaction.rollback();
            Logs.error("Ошибка при удалении платежа с id:" + paymentId, e);
            return false;

        }
    }

    @Override
    public double getTotalPaymentsByUserId(int userId) {
        try {
            Double total = session.createQuery(
                    "SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.ride.user.id = :userId",
                                    Double.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
            return total;
        } catch (HibernateException e) {
            Logs.error("Ошибка при подсчёте суммы платежей", e);
            return 0.0;  // вместо throw
        }
    }

    @Override
    public boolean existsById(int paymentId){
        try {
            return session.find(Payment.class, paymentId) != null;
        } catch (HibernateException e) {
            Logs.error("Ошибка при проверке существования поездки с id: " + paymentId, e);
            return false;
        }
    }


}
