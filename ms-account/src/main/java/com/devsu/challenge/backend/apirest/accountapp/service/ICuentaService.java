package com.devsu.challenge.backend.apirest.accountapp.service;

import com.devsu.challenge.backend.apirest.accountapp.entity.Cuenta;
import java.util.List;
import java.util.Optional;

public interface ICuentaService {

    List<Cuenta> getAllCuentas();

    Optional<Cuenta> getCuentaById(Long id);

    Cuenta crearCuenta(Cuenta cuenta);

    Cuenta updateCuenta(Long id, Cuenta detalleCuenta);

    void deleteCuenta(Long id);
}
