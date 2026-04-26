package ru.shalaginov.appLogic;

import ru.shalaginov.connect.DataConnect;
import ru.shalaginov.enity.User;
import ru.shalaginov.enity.Ride;
import ru.shalaginov.enity.Payment;
import ru.shalaginov.util.Logs;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Main {

    public static void main(String[] args) {
        java.util.logging.LogManager.getLogManager().reset();
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Logs.error("Ошибка кодировки", e);
        }

        AppLogic logic = new AppLogic();
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        boolean running = true;

        while (running) {
            System.out.println("\n========== МЕНЮ ==========");
            System.out.println("=== CREATE ===");
            System.out.println("1  - Создать пользователя");
            System.out.println("2  - Создать поездку");
            System.out.println("3  - Создать платеж");
            System.out.println("\n=== READ ===");
            System.out.println("4  - Список всех пользователей");
            System.out.println("5  - Найти пользователя по ID");
            System.out.println("6  - Список всех поездок");
            System.out.println("7  - Найти поездку по ID");
            System.out.println("8  - Список всех платежей");
            System.out.println("9  - Найти платёж по ID");
            System.out.println("\n=== UPDATE ===");
            System.out.println("10 - Обновить пользователя");
            System.out.println("11 - Обновить поездку");
            System.out.println("12 - Обновить платёж");
            System.out.println("\n=== DELETE ===");
            System.out.println("13 - Удалить пользователя");
            System.out.println("14 - Удалить поездку");
            System.out.println("15 - Удалить платёж");
            System.out.println("\n=== BONUS ===");
            System.out.println("16 - Статус бонусной карты");
            System.out.println("\n0  - Выход");
            System.out.println("==========================");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Имя: ");
                    String name = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Телефон: ");
                    String phone = scanner.nextLine();
                    if (logic.createUser(name, email, phone)) {
                        System.out.println("Пользователь создан");
                    } else {
                        System.out.println("Не удалось создать пользователя");
                    }
                    break;

                case "2":
                    try {
                        System.out.print("User ID: ");
                        int userId = Integer.parseInt(scanner.nextLine());

                        System.out.print("Start (yyyy-MM-dd HH:mm): ");
                        String startStr = scanner.nextLine();
                        LocalDateTime start = LocalDateTime.parse(startStr, formatter);

                        System.out.print("End (yyyy-MM-dd HH:mm): ");
                        String endStr = scanner.nextLine();
                        LocalDateTime end = LocalDateTime.parse(endStr, formatter);

                        System.out.print("Price: ");
                        double price = Double.parseDouble(scanner.nextLine());

                        if (logic.createRide(userId, start, end, price)) {
                            System.out.println("Поездка создана");
                        } else {
                            System.out.println("Не удалось создать поездку");
                        }
                    } catch (DateTimeParseException e) {
                        System.out.println("Ошибка: Неправильный формат даты! Используйте yyyy-MM-dd HH:mm");
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: ID и Price должны быть числами!");
                    }
                    break;

                case "3":
                    try {
                        System.out.print("Ride ID: ");
                        int rideId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Amount: ");
                        double amount = Double.parseDouble(scanner.nextLine());
                        System.out.print("Status: ");
                        String status = scanner.nextLine();
                        if (logic.createPayment(rideId, amount, status)) {
                            System.out.println("Платёж создан");
                        } else {
                            System.out.println("Не удалось создать платёж");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: ID и Amount должны быть числами!");
                    }
                    break;

                case "4":
                    List<User> users = logic.getAllUsers();
                    if (users.isEmpty()) {
                        System.out.println("Список пользователей пуст");
                    } else {
                        System.out.println("\n=== СПИСОК ПОЛЬЗОВАТЕЛЕЙ ===");
                        for (User u : users) {
                            System.out.println(u.getId() + " | " + u.getName() + " | " + u.getEmail() + " | " + u.getPhone());
                        }
                    }
                    break;

                case "5":
                    try {
                        System.out.print("ID пользователя: ");
                        int userId = Integer.parseInt(scanner.nextLine());
                        User user = logic.getUserById(userId);
                        if (user != null) {
                            System.out.println("Найден: " + user.getId() + " | " + user.getName() + " | " + user.getEmail() + " | " + user.getPhone());
                        } else {
                            System.out.println("Пользователь не найден");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: ID должен быть числом!");
                    }
                    break;

                case "6":
                    List<Ride> rides = logic.getAllRides();
                    if (rides.isEmpty()) {
                        System.out.println("Список поездок пуст");
                    } else {
                        System.out.println("\n=== СПИСОК ВСЕХ ПОЕЗДОК ===");
                        for (Ride r : rides) {
                            System.out.println(r.getId() + " | user_id: " + r.getUser().getId() + " | " + r.getStartTime() + " - " + r.getEndTime() + " | " + r.getPrice());
                        }
                    }
                    break;

                case "7":
                    try {
                        System.out.print("ID поездки: ");
                        int rideId = Integer.parseInt(scanner.nextLine());
                        Ride ride = logic.getRideById(rideId);
                        if (ride != null) {
                            System.out.println("Найдена: ID=" + ride.getId() + " | user_id=" + ride.getUser().getId() + " | " + ride.getStartTime() + " - " + ride.getEndTime() + " | " + ride.getPrice());
                        } else {
                            System.out.println("Поездка не найдена");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: ID должен быть числом!");
                    }
                    break;

                case "8":
                    List<Payment> payments = logic.getAllPayments();
                    if (payments.isEmpty()) {
                        System.out.println("Список платежей пуст");
                    } else {
                        System.out.println("\n=== СПИСОК ВСЕХ ПЛАТЕЖЕЙ ===");
                        for (Payment p : payments) {
                            System.out.println(p.getId() + " | ride_id: " + p.getRide().getId() + " | сумма: " + p.getAmount() + " | статус: " + p.getStatus());
                        }
                    }
                    break;

                case "9":
                    try {
                        System.out.print("ID платежа: ");
                        int paymentId = Integer.parseInt(scanner.nextLine());
                        Payment payment = logic.getPaymentById(paymentId);
                        if (payment != null) {
                            System.out.println("Найден: ID=" + payment.getId() + " | ride_id=" + payment.getRide().getId() + " | сумма=" + payment.getAmount() + " | статус=" + payment.getStatus());
                        } else {
                            System.out.println("Платёж не найден");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: ID должен быть числом!");
                    }
                    break;

                case "10":
                    try {
                        System.out.print("ID пользователя для обновления: ");
                        int updUserId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Новое имя: ");
                        String newName = scanner.nextLine();
                        System.out.print("Новый email: ");
                        String newEmail = scanner.nextLine();
                        System.out.print("Новый телефон: ");
                        String newPhone = scanner.nextLine();
                        if (logic.updateUser(updUserId, newName, newEmail, newPhone)) {
                            System.out.println("Пользователь обновлён");
                        } else {
                            System.out.println("Ошибка обновления");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: ID должен быть числом!");
                    }
                    break;

                case "11":
                    try {
                        System.out.print("ID поездки для обновления: ");
                        int updRideId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Новый User ID: ");
                        int newUserId = Integer.parseInt(scanner.nextLine());
                        System.out.print("New Start (yyyy-MM-dd HH:mm): ");
                        String newStartStr = scanner.nextLine();
                        LocalDateTime newStart = LocalDateTime.parse(newStartStr, formatter);
                        System.out.print("New End (yyyy-MM-dd HH:mm): ");
                        String newEndStr = scanner.nextLine();
                        LocalDateTime newEnd = LocalDateTime.parse(newEndStr, formatter);
                        System.out.print("New Price: ");
                        double newPrice = Double.parseDouble(scanner.nextLine());
                        if (logic.updateRide(updRideId, newUserId, newStart, newEnd, newPrice)) {
                            System.out.println("Поездка обновлена");
                        } else {
                            System.out.println("Ошибка обновления");
                        }
                    } catch (DateTimeParseException e) {
                        System.out.println("Ошибка: Неправильный формат даты! Используйте yyyy-MM-dd HH:mm");
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: ID и Price должны быть числами!");
                    }
                    break;

                case "12":
                    try {
                        System.out.print("ID платежа для обновления: ");
                        int updPaymentId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Новый Ride ID: ");
                        int newRideId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Новая сумма: ");
                        double newAmount = Double.parseDouble(scanner.nextLine());
                        System.out.print("Новый статус: ");
                        String newStatus = scanner.nextLine();
                        if (logic.updatePayment(updPaymentId, newRideId, newAmount, newStatus)) {
                            System.out.println("Платёж обновлён");
                        } else {
                            System.out.println("Ошибка обновления");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: ID и сумма должны быть числами!");
                    }
                    break;

                case "13":
                    try {
                        System.out.print("ID пользователя для удаления: ");
                        int delUserId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Вы уверены? (y/n): ");
                        String confirm = scanner.nextLine();
                        if (confirm.equalsIgnoreCase("y")) {
                            if (logic.deleteUser(delUserId)) {
                                System.out.println("Пользователь удалён (и все его поездки с платежами)");
                            } else {
                                System.out.println("Ошибка удаления");
                            }
                        } else {
                            System.out.println("Удаление отменено");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: ID должен быть числом!");
                    }
                    break;

                case "14":
                    try {
                        System.out.print("ID поездки для удаления: ");
                        int delRideId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Вы уверены? (y/n): ");
                        String confirm = scanner.nextLine();
                        if (confirm.equalsIgnoreCase("y")) {
                            if (logic.deleteRide(delRideId)) {
                                System.out.println("Поездка удалена (и связанные платежи)");
                            } else {
                                System.out.println("Ошибка удаления");
                            }
                        } else {
                            System.out.println("Удаление отменено");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: ID должен быть числом!");
                    }
                    break;

                case "15":
                    try {
                        System.out.print("ID платежа для удаления: ");
                        int delPaymentId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Вы уверены? (y/n): ");
                        String confirm = scanner.nextLine();
                        if (confirm.equalsIgnoreCase("y")) {
                            if (logic.deletePayment(delPaymentId)) {
                                System.out.println("Платёж удалён");
                            } else {
                                System.out.println("Ошибка удаления");
                            }
                        } else {
                            System.out.println("Удаление отменено");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: ID должен быть числом!");
                    }
                    break;

                case "16":
                    try {
                        System.out.print("User ID для просмотра бонусной карты: ");
                        int bonusUserId = Integer.parseInt(scanner.nextLine());
                        String cardStatus = logic.getBonusCardStatus(bonusUserId);
                        System.out.println("Статус бонусной карты: " + cardStatus);
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: ID должен быть числом!");
                    }
                    break;

                case "0":
                    running = false;
                    System.out.println("До свидания!");
                    break;

                default:
                    System.out.println("Неверный выбор, попробуйте снова.");
            }
        }

        DataConnect.closeConnection();
        scanner.close();
    }
}