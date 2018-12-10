package org.lz.rental.domain;

import java.time.LocalDate;
import java.util.UUID;

public class ReservationResult {
    private UUID id;
    private Reservation reservationRef;
    private Car refCar;

    public ReservationResult(Car car, Reservation reservation) {
        this.id = UUID.randomUUID();
        this.refCar = car;
        this.reservationRef = reservation;
    }

    public UUID getId() {
        return id;
    }

    public Reservation getReservationRef() {
        return reservationRef;
    }

    public Car getRefCar() {
        return refCar;
    }
}
