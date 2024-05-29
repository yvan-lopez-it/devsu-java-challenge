package com.devsu.challenge.backend.apirest.accountapp.exceptions;

import java.io.Serial;

public class CuentaNoExisteException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1;

    public CuentaNoExisteException(String message) {
        super(message);
    }
}
