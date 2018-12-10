package org.lz.rental.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lz.rental.api.CarRentalServices;
import org.lz.rental.domain.Car;
import org.lz.rental.domain.Reservation;
import org.lz.rental.domain.ReservationResult;
import org.lz.rental.exception.ConflictCarRentalException;
import org.lz.rental.exception.DuplicatedCarsException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.Assert.*;

public class SimpleCarRentalServicesTest {

    CarRentalServices carServices = new SimpleCarRentalServices();
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void addCars() throws DuplicatedCarsException {
        carServices.addCars(Arrays.asList(new Car(UUID.randomUUID(), Car.CarType.SUV)));
        carServices.addCars(Arrays.asList(new Car(UUID.randomUUID(), Car.CarType.SEDEN)));
        carServices.addCars(Arrays.asList(new Car(UUID.randomUUID(), Car.CarType.TRUCK)));

        Assert.assertEquals(carServices.getCars().size(), 3);
    }

    @Test(expected = DuplicatedCarsException.class)
    public void addCarsFail() throws DuplicatedCarsException {
        UUID duplicated = UUID.randomUUID();
        carServices.addCars(Arrays.asList(new Car(duplicated, Car.CarType.SUV)));
        carServices.addCars(Arrays.asList(new Car(duplicated, Car.CarType.SEDEN)));
    }

    /**
     * Empty data will return ConflictCarRentalException
     * @throws ConflictCarRentalException
     */
    @Test(expected = ConflictCarRentalException.class)
    public void reserveCarFailEmptyData() throws ConflictCarRentalException {
        Reservation reservation = new Reservation(LocalDate.parse("2018-07-01"), LocalDate.parse("2018-08-01"), Car.CarType.SUV);

        ReservationResult result = carServices.reserveCar(reservation);
        Assert.assertEquals(result.getRefCar().getType(), Car.CarType.SUV);
    }

    @Test
    public void reserveCarOk() throws ConflictCarRentalException, DuplicatedCarsException {
        carServices.addCars(Arrays.asList(new Car(UUID.randomUUID(), Car.CarType.SUV)));
        Reservation reservation = new Reservation(LocalDate.parse("2018-07-01"), LocalDate.parse("2018-08-01"), Car.CarType.SUV);

        ReservationResult result = carServices.reserveCar(reservation);
        Assert.assertEquals(result.getRefCar().getType(), Car.CarType.SUV);
    }
}