package ru.shalaginov.repository;

import ru.shalaginov.enity.Ride;

import java.util.List;

public interface RideRepositoryInt {
    Ride create(Ride ride);
    Ride findById(int RideId);
    List<Ride> findAll();
    Ride update(Ride ride);
    boolean delete(int RideId);
    boolean existsById(int RideId);
}
