package org.lz.rental.exception;

import org.lz.rental.domain.Reservation;

public class ConflictCarRentalException extends Exception {

    private final Reservation reservation;

    public ConflictCarRentalException(Reservation req) {
        super("conflict reservation");
        this.reservation = req;
    }
}
