package com.devsu.challenge.backend.apirest.accountapp.exceptions;

import java.io.Serial;

public class SaldoNoDisponibleException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1;

    public SaldoNoDisponibleException(String message) {
        super(message);
    }
}
