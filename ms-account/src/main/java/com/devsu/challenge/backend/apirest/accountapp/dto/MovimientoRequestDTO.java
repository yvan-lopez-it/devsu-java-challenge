package com.devsu.challenge.backend.apirest.accountapp.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class MovimientoRequestDTO implements Serializable {

    private String tipoMovimiento;
    private Double valor;
    private String cuentaId;
}
