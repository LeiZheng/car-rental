package org.lz.rental.impl;

import org.lz.rental.api.CarPicker;
import org.lz.rental.domain.Reservation;
import org.lz.rental.domain.ReservationSummary;
import org.lz.rental.exception.ConflictCarRentalException;

import java.util.Collection;

/**
 * search the first available car to pickup.
 * in future, we could use the smarter way to use car more efficient.
 */

public class SimpleCarPicker implements CarPicker {

    @Override
    public ReservationSummary pickup(Reservation reservation, Collection<ReservationSummary> summaries) throws ConflictCarRentalException {
        // firstly, do the quick check without lock to make sure there is still available resource to rent.
        ReservationSummary workingCarSummary = summaries.stream()
                .filter(summary -> summary.checkAvailable(reservation))
                .findFirst().orElse(null);
        if (workingCarSummary == null) {
            throw new ConflictCarRentalException(reservation);
        }
        workingCarSummary.commitReservation(reservation);

        return workingCarSummary;
    }
}
