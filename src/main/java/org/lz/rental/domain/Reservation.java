package org.lz.rental.domain;

import java.time.LocalDate;
import java.util.UUID;

/**
 * The class represents the car rental request form user.
 */
public class Reservation {
    private UUID id;
    private Car.CarType carType;

    private LocalDate startDate;
    private LocalDate endDate;

    public Reservation(LocalDate startDate, LocalDate endDate, Car.CarType carType) {
        this.id = UUID.randomUUID();
        this.startDate = startDate;
        this.endDate = endDate;
        this.carType = carType;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Car.CarType getCarType() {
        return carType;
    }

    public void setCarType(Car.CarType carType) {
        this.carType = carType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


}
