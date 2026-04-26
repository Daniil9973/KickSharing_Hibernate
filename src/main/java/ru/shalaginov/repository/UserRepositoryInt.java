package ru.shalaginov.repository;

import ru.shalaginov.enity.User;

import java.util.List;

public interface UserRepositoryInt {

    User create(User user);
    User findById(int userId);
    List<User> findAll();
    User update(User user);
    boolean delete(int userId);
    boolean existsById(int userId);

}
