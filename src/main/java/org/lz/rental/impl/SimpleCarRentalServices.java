package org.lz.rental.impl;

import org.lz.rental.api.CarPicker;
import org.lz.rental.api.CarRentalServices;
import org.lz.rental.domain.Car;
import org.lz.rental.domain.Reservation;
import org.lz.rental.domain.ReservationResult;
import org.lz.rental.domain.ReservationSummary;
import org.lz.rental.exception.ConflictCarRentalException;
import org.lz.rental.exception.DuplicatedCarsException;

import java.util.*;
import java.util.stream.Collectors;

public class SimpleCarRentalServices implements CarRentalServices {

    private Map<UUID, ReservationSummary> reservationTable = Collections.synchronizedMap(new HashMap<>());
    private CarPicker carPicker = new SimpleCarPicker();

    @Override
    public void addCars(Collection<Car> cars) throws DuplicatedCarsException {
        // check the existing cars
        List<UUID> newIds = cars.stream().map(Car::getId).collect(Collectors.toList());
        List<UUID> existingIds = this.reservationTable.keySet().parallelStream()
                .filter(id -> newIds.contains(id)).collect(Collectors.toList());
        if (!existingIds.isEmpty()) {
            throw new DuplicatedCarsException(newIds);
        }
        synchronized (this.reservationTable) {
            cars.forEach(car -> this.reservationTable.put(car.getId(), new ReservationSummary(car)));
        }

    }

    @Override
    public Collection<Car> getCars() {
        return this.reservationTable.values().stream().map(summary -> summary.getCarRef()).collect(Collectors.toList());
    }

    @Override
    public ReservationResult reserveCar(Reservation reservation) throws ConflictCarRentalException {
        //get all reservation summary for given car type.
        List<ReservationSummary> summaries = reservationTable.values().stream()
                .filter(v -> v.getCarRef().getType().equals(reservation.getCarType())).collect(Collectors.toList());
        ReservationSummary selectedCarSummary = carPicker.pickup(reservation, summaries);
        return new ReservationResult(selectedCarSummary.getCarRef(), reservation);
    }
}
