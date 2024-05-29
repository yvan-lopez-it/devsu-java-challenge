package com.devsu.challenge.backend.apirest.accountapp.repository;

import com.devsu.challenge.backend.apirest.accountapp.entity.Cuenta;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    List<Cuenta> findByClientId(String clientId);

    Optional<Cuenta> findByNumeroCuenta(String cuenta);

}
