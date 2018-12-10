package org.lz.rental.api;

import org.lz.rental.domain.Reservation;
import org.lz.rental.domain.ReservationSummary;
import org.lz.rental.exception.ConflictCarRentalException;

import java.util.Collection;

public interface CarPicker {
    ReservationSummary pickup(Reservation res, Collection<ReservationSummary> summaries) throws ConflictCarRentalException;
}
