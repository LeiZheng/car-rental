package org.lz.rental.domain;
import org.lz.rental.exception.ConflictCarRentalException;
import java.util.ArrayList;
import java.util.Collection;

public class ReservationSummary {

    /**
     * keep the reference of car instance to check car detail information.
     */
    private Car carRef;

    /**
     * store the reservations of ref car. when adding new reservation, we need check to make sure, there is no
     * no conflict.
     */
    private Collection<Reservation> reservations;


    public ReservationSummary(Car carRef) {
        this.carRef = carRef;
        this.reservations = new ArrayList<>();
    }

    public Collection<Reservation> getReservations() {
        return reservations;
    }

    public Car getCarRef() {
        return carRef;
    }

    public boolean checkAvailable(Reservation req) {
        return !this.reservations.stream().parallel().anyMatch(x -> !x.getEndDate().isBefore(req.getStartDate())
                && !x.getStartDate().isAfter(req.getEndDate()));
   }

    /**
     * syned coe to make sure there is onl one iem to reserations with overlayed time slot.
     * @param req
     * @return
     * @throws ConflictCarRentalException
     */
    public boolean commitReservation(Reservation req) throws ConflictCarRentalException {
        synchronized(this.reservations) {
            if(checkAvailable(req)) {
                this.reservations.add(req);
                return true;
            }
            else {
                throw new ConflictCarRentalException(req);
            }
        }
    }
}
