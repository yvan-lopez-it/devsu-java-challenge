package com.devsu.challenge.backend.apirest.accountapp.exceptions;

import java.io.Serial;


public class ClienteNoEncontradoException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1;

    public ClienteNoEncontradoException(String message) {
        super(message);
    }

}
