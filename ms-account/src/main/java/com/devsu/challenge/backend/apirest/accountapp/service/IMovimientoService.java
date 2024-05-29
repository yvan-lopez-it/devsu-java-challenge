package com.devsu.challenge.backend.apirest.accountapp.service;

import com.devsu.challenge.backend.apirest.accountapp.dto.MovimientoRequestDTO;
import com.devsu.challenge.backend.apirest.accountapp.entity.Movimiento;
import java.util.List;
import java.util.Optional;

public interface IMovimientoService {

    List<Movimiento> getAllMovimientos();

    Optional<Movimiento> getMovimientoById(Long id);

    Movimiento createMovimiento(MovimientoRequestDTO movimiento);

    Movimiento updateMovimiento(Long id, Movimiento detalleMovimiento);

    void deleteMovimiento(Long id);
}
