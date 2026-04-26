package ru.shalaginov.appLogic;

import ru.shalaginov.enity.User;
import ru.shalaginov.enity.Ride;
import ru.shalaginov.enity.Payment;
import ru.shalaginov.repository.*;
import ru.shalaginov.util.Logs;

import java.time.LocalDateTime;
import java.util.List;

public class AppLogic {

    private final UserRepositoryInt userRepo;
    private final RideRepositoryInt rideRepo;
    private final PaymentRepositoryInt paymentRepo;

    public AppLogic() {
        this.userRepo = new UserRepository();
        this.rideRepo = new RideRepository();
        this.paymentRepo = new PaymentRepository();
    }

    // CREATE
    public boolean createUser(String name, String email, String phone) {
        User user = new User(name, email, phone);
        User createdUser = userRepo.create(user);
        if (createdUser == null) {
            Logs.error("Не удалось создать пользователя", null);
            return false;
        } else {
            Logs.info("Пользователь успешно создан с ID: " + createdUser.getId());
            return true;
        }
    }

    public boolean createRide(int userId, LocalDateTime start, LocalDateTime end, double price) {
        if (!userRepo.existsById(userId)) {
            Logs.error("Ошибка: Пользователь с ID " + userId + " не найден!", null);
            return false;
        }

        // Получаем User объект для связи (нужно для Hibernate)
        User user = userRepo.findById(userId);
        Ride ride = new Ride(user, start, end, price);
        Ride createdRide = rideRepo.create(ride);

        if (createdRide == null) {
            Logs.error("Не удалось создать поездку", null);
            return false;
        } else {
            Logs.info("Поездка создана с ID: " + createdRide.getId());
            return true;
        }
    }

    public boolean createPayment(int rideId, double amount, String status) {
        if (!rideRepo.existsById(rideId)) {
            Logs.error("Ошибка: Поездка с ID " + rideId + " не найдена!", null);
            return false;
        }

        // Получаем Ride объект для связи (нужно для Hibernate)
        Ride ride = rideRepo.findById(rideId);
        Payment payment = new Payment(ride, amount, status);
        Payment createdPayment = paymentRepo.create(payment);

        if (createdPayment == null) {
            Logs.error("Не удалось создать платёж", null);
            return false;
        } else {
            Logs.info("Платёж создан с ID: " + createdPayment.getId());
            return true;
        }
    }

    // READ
    public User getUserById(int userId) {
        if (!userRepo.existsById(userId)) {
            Logs.error("Пользователь с ID " + userId + " не найден", null);
            return null;
        }
        return userRepo.findById(userId);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public Ride getRideById(int rideId) {
        if (!rideRepo.existsById(rideId)) {
            Logs.error("Поездка с ID " + rideId + " не найдена", null);
            return null;
        }
        return rideRepo.findById(rideId);
    }

    public List<Ride> getAllRides() {
        return rideRepo.findAll();
    }

    public Payment getPaymentById(int paymentId) {
        if (!paymentRepo.existsById(paymentId)) {
            Logs.error("Платёж с ID " + paymentId + " не найден", null);
            return null;
        }
        return paymentRepo.findById(paymentId);
    }

    public List<Payment> getAllPayments() {
        return paymentRepo.findAll();
    }

    // UPDATE
    public boolean updateUser(int userId, String name, String email, String phone) {
        if (!userRepo.existsById(userId)) {
            Logs.error("Ошибка: Пользователь с ID " + userId + " не найден!", null);
            return false;
        }
        User user = new User(name, email, phone);
        user.setId(userId);
        User updatedUser = userRepo.update(user);
        if (updatedUser == null) {
            Logs.error("Не удалось обновить пользователя с ID: " + userId, null);
            return false;
        } else {
            Logs.info("Пользователь с ID " + userId + " успешно обновлён");
            return true;
        }
    }

    public boolean updateRide(int rideId, int userId, LocalDateTime start, LocalDateTime end, double price) {
        if (!rideRepo.existsById(rideId)) {
            Logs.error("Ошибка: Поездка с ID " + rideId + " не найдена!", null);
            return false;
        }
        if (!userRepo.existsById(userId)) {
            Logs.error("Ошибка: Пользователь с ID " + userId + " не найден!", null);
            return false;
        }

        User user = userRepo.findById(userId);
        Ride ride = new Ride(user, start, end, price);
        ride.setId(rideId);
        Ride updatedRide = rideRepo.update(ride);
        if (updatedRide == null) {
            Logs.error("Не удалось обновить поездку с ID: " + rideId, null);
            return false;
        } else {
            Logs.info("Поездка с ID " + rideId + " успешно обновлена");
            return true;
        }
    }

    public boolean updatePayment(int paymentId, int rideId, double amount, String status) {
        if (!paymentRepo.existsById(paymentId)) {
            Logs.error("Ошибка: Платёж с ID " + paymentId + " не найден!", null);
            return false;
        }
        if (!rideRepo.existsById(rideId)) {
            Logs.error("Ошибка: Поездка с ID " + rideId + " не найдена!", null);
            return false;
        }

        Ride ride = rideRepo.findById(rideId);
        Payment payment = new Payment(ride, amount, status);
        payment.setId(paymentId);
        Payment updatedPayment = paymentRepo.update(payment);
        if (updatedPayment == null) {
            Logs.error("Не удалось обновить платёж с ID: " + paymentId, null);
            return false;
        } else {
            Logs.info("Платёж с ID " + paymentId + " успешно обновлён");
            return true;
        }
    }

    // DELETE
    public boolean deleteUser(int userId) {
        if (!userRepo.existsById(userId)) {
            Logs.error("Ошибка: Пользователь с ID " + userId + " не найден!", null);
            return false;
        }

        boolean deleted = userRepo.delete(userId);
        if (deleted) {
            Logs.info("Пользователь с ID " + userId + " успешно удалён");
        }
        return deleted;
    }

    public boolean deleteRide(int rideId) {
        if (!rideRepo.existsById(rideId)) {
            Logs.error("Ошибка: Поездка с ID " + rideId + " не найдена!", null);
            return false;
        }

        boolean deleted = rideRepo.delete(rideId);
        if (deleted) {
            Logs.info("Поездка с ID " + rideId + " успешно удалена");
        }
        return deleted;
    }

    public boolean deletePayment(int paymentId) {
        if (!paymentRepo.existsById(paymentId)) {
            Logs.error("Ошибка: Платёж с ID " + paymentId + " не найден!", null);
            return false;
        }

        boolean deleted = paymentRepo.delete(paymentId);
        if (deleted) {
            Logs.info("Платёж с ID " + paymentId + " успешно удалён");
        }
        return deleted;
    }

    // BONUS
    public String getBonusCardStatus(int userId) {
        if (!userRepo.existsById(userId)) {
            Logs.error("Ошибка: Пользователь с ID " + userId + " не найден!", null);
            return "Пользователь не найден";
        }

        double totalPayments = paymentRepo.getTotalPaymentsByUserId(userId);
        if (totalPayments == 0) {
            return "Нет карты";
        } else if (totalPayments >= 10001) {
            return "Золотая";
        } else if (totalPayments >= 5001) {
            return "Серебряная";
        } else {
            return "Бронзовая";
        }
    }
}