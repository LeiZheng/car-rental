package org.lz.rental.api;

import org.lz.rental.domain.Car;
import org.lz.rental.domain.Reservation;
import org.lz.rental.domain.ReservationResult;
import org.lz.rental.exception.ConflictCarRentalException;
import org.lz.rental.exception.DuplicatedCarsException;

import java.util.Collection;

public interface CarRentalServices {
    void addCars(Collection<Car> cars) throws DuplicatedCarsException;
    Collection<Car> getCars();
    ReservationResult reserveCar(Reservation range) throws ConflictCarRentalException;
}
