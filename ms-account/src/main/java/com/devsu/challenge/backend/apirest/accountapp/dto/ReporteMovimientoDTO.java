package com.devsu.challenge.backend.apirest.accountapp.dto;

import lombok.Data;

@Data
public class ReporteMovimientoDTO {

    private String fecha;
    private String cliente;
    private String numeroCuenta;
    private String tipo;
    private Double saldoInicial;
    private Boolean estado;
    private Double movimiento;
    private Double saldoDisponible;
}
