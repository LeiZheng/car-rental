package org.lz.rental.exception;

import org.lz.rental.domain.Car;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

public class DuplicatedCarsException extends Exception {
    private final Collection<UUID> carIds;

    public DuplicatedCarsException(Collection<UUID> carIds) {
        super("Duplicated cars {" + carIds.stream().map(car -> car.toString()).collect(Collectors.joining(",")) + "}");
        this.carIds = carIds;
    }
}
