package org.lz.rental.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.lz.rental.api.CarPicker;
import org.lz.rental.domain.Car;
import org.lz.rental.domain.Reservation;
import org.lz.rental.domain.ReservationSummary;
import org.lz.rental.exception.ConflictCarRentalException;

import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.*;

/**
 * the test case we need go:
 * 1. one thread:
 * 1.1 no time conflict when for one reservation.
 * 1.2 throw Conflict exception when hit the data conflict.
 * 2. multiple threads:
 * 2.1 time conflict for multiple threads
 */
public class SimpleCarPickerTest {

    CarPicker carPicker = new SimpleCarPicker();
    Collection<ReservationSummary> summaries = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        System.out.println("setup");
        // the picker class will only look for the same type of CarType.

    }

    @Test
    public void pickupOkOnethread() throws ConflictCarRentalException {
        // construct the time line: 2018-08-01 -> 2018-08-05, 2018-09-01 -> 2018-10-01
        ReservationSummary summary1 = new ReservationSummary(new Car(UUID.randomUUID(), Car.CarType.SUV));
        Reservation reservation1 = new Reservation(LocalDate.parse("2018-08-01"), LocalDate.parse("2018-08-05"), Car.CarType.SUV);
        Reservation reservation2 = new Reservation(LocalDate.parse("2018-09-01"), LocalDate.parse("2018-10-01"), Car.CarType.SUV);
        summary1.commitReservation(reservation1);
        summary1.commitReservation(reservation2);
        summaries.add(summary1);

        // reservation: 2018-05-01 -> 2018-07-01, early than any reervation.
        Reservation reservation3 = new Reservation(LocalDate.parse("2018-05-01"), LocalDate.parse("2018-07-01"), Car.CarType.SUV);

        ReservationSummary reservationSummary = carPicker.pickup(reservation3, this.summaries);
        Assert.assertSame(3, reservationSummary.getReservations().size());

        // reservation: 2017-08-08 -> 2017-08-20, between 2 reservations.
        // construct the time line: 2017-08-01 -> 2017-08-05, 2017-09-01 -> 2017-10-01
        this.summaries.clear();
        ReservationSummary summary2 = new ReservationSummary(new Car(UUID.randomUUID(), Car.CarType.SUV));
        summary2.commitReservation(new Reservation(LocalDate.parse("2017-08-01"), LocalDate.parse("2017-08-05"), Car.CarType.SUV));
        summary2.commitReservation(new Reservation(LocalDate.parse("2017-09-01"), LocalDate.parse("2017-10-01"), Car.CarType.SUV));
        summaries.add(summary2);
        reservationSummary = carPicker.pickup(new Reservation(LocalDate.parse("2017-08-08"), LocalDate.parse("2017-08-20"), Car.CarType.SUV), this.summaries);
        Assert.assertSame(3, reservationSummary.getReservations().size());

        // reservation: 2016-10-08 -> 2016-10-20, between 2 reservations.
        // construct the time line: 2016-08-01 -> 2016-08-05, 2016-09-01 -> 2016-10-01
        summaries.clear();
        ReservationSummary summary3 = new ReservationSummary(new Car(UUID.randomUUID(), Car.CarType.SUV));
        summary3.commitReservation(new Reservation(LocalDate.parse("2016-08-01"), LocalDate.parse("2016-08-05"), Car.CarType.SUV));
        summary3.commitReservation(new Reservation(LocalDate.parse("2016-09-01"), LocalDate.parse("2016-10-01"), Car.CarType.SUV));
        summaries.add(summary3);
        reservationSummary = carPicker.pickup(new Reservation(LocalDate.parse("2016-10-08"), LocalDate.parse("2016-10-20"), Car.CarType.SUV), this.summaries);
        Assert.assertSame(3, reservationSummary.getReservations().size());

    }

    @Test(expected = ConflictCarRentalException.class)
    public void pickupFailConflictOnethread() throws Exception {
        // construct the time line: 2018-08-01 -> 2018-08-05, 2018-09-01 -> 2018-10-01
        ReservationSummary summary1 = new ReservationSummary(new Car(UUID.randomUUID(), Car.CarType.SUV));
        Reservation reservation1 = new Reservation(LocalDate.parse("2018-08-01"), LocalDate.parse("2018-08-05"), Car.CarType.SUV);
        Reservation reservation2 = new Reservation(LocalDate.parse("2018-09-01"), LocalDate.parse("2018-10-01"), Car.CarType.SUV);
        summary1.commitReservation(reservation1);
        summary1.commitReservation(reservation2);
        summaries.add(summary1);

        // reservation: 2018-05-01 -> 2018-07-01, early than any reervation.
        Reservation reservation3 = new Reservation(LocalDate.parse("2018-07-01"), LocalDate.parse("2018-08-01"), Car.CarType.SUV);
        ReservationSummary reservationSummary = carPicker.pickup(reservation3, this.summaries);

    }


    @Test
    public void pickupFailMultipleThread() throws InterruptedException {
        final ReservationSummary summary1 = new ReservationSummary(new Car(UUID.randomUUID(), Car.CarType.SUV));

        Thread thread1 = new Thread() {
            public void run() {
                Reservation reservation3 = new Reservation(LocalDate.parse("2018-07-01"), LocalDate.parse("2018-08-01"), Car.CarType.SUV);
                try {
                    summary1.commitReservation(reservation3);
                } catch (ConflictCarRentalException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread thread2 = new Thread() {
            public void run() {
                Reservation reservation3 = new Reservation(LocalDate.parse("2018-07-01"), LocalDate.parse("2018-08-01"), Car.CarType.SUV);
                try {
                    summary1.commitReservation(reservation3);
                } catch (ConflictCarRentalException e) {
                    e.printStackTrace();
                }
            }
        };
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        assertEquals(summary1.getReservations().size(), 1);
    }
}