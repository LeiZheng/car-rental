package org.lz.rental.domain;

import java.util.UUID;

public  class Car {
    public CarType getType() {
        return type;
    }

    private CarType type;

    public UUID getId() {
        return id;
    }

    private UUID id;

    public Car(UUID id, CarType type) {
        this.id = id;
        this.type = type;
    }

    public static enum CarType  {
        SUV,
        TRUCK,
        SEDEN
    }
}
