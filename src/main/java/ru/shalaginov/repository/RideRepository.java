package ru.shalaginov.repository;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.shalaginov.util.Logs;
import ru.shalaginov.enity.Ride;
import ru.shalaginov.connect.DataConnect;

import java.util.ArrayList;
import java.util.List;


public class RideRepository implements RideRepositoryInt{

    private final Session session;

    public RideRepository() {
        this.session = DataConnect.getSession();
    }

    @Override
    public Ride create(Ride ride) {
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();
            session.persist(ride);
            transaction.commit();

            Logs.info("Поездка создана id: " + ride.getId());
            return ride;
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            Logs.error("Ошибка при создании поездки", e);
            return null;
        }
    }

    @Override
    public Ride findById(int rideId) {
        try {
            return session.find(Ride.class, rideId);
        } catch (HibernateException e) {
            Logs.error("Ошибка при поиске поездки по id: " + rideId, e);
            return null;
        }
    }

    @Override
    public List<Ride> findAll() {
        try {
            return session.createQuery("FROM Ride", Ride.class).list();
        } catch (HibernateException e) {
            Logs.error("Ошибка при получении списка поездок", e);
            return new ArrayList<>();
        }
    }

    @Override
    public Ride update(Ride ride) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Ride merged = session.merge(ride);
            transaction.commit();

            Logs.info("Поездка обновлена, id: " + ride.getId());
            return merged;
        } catch (HibernateException e){
            if (transaction != null) transaction.rollback();
            Logs.error("Ошибка при обновлении поездки", e);
            return null;
        }
    }

    @Override
    public boolean delete(int rideId) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Ride ride = session.find(Ride.class, rideId);

            if (ride != null) {
                session.remove(ride);
                transaction.commit();
                Logs.info("Поездка удалена, id: " + rideId);
                return true;
            } else {
                transaction.rollback();
                Logs.info("Поездка с id " + rideId + " не найдена для удаления");
                return false;
            }
        } catch (HibernateException e) {
            if(transaction!=null) transaction.rollback();
            Logs.error("Ошибка при удалении поездки с id: " + rideId, e);
            return false;
        }
    }

    @Override
    public boolean existsById(int RideId) {
        try {
            return session.find(Ride.class, RideId) != null;
        } catch (HibernateException e) {
            Logs.error("Ошибка при проверке существования поездки с id: " + RideId, e);
            return false;
        }
    }


}