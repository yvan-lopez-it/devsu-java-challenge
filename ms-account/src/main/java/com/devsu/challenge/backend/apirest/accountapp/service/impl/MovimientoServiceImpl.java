package com.devsu.challenge.backend.apirest.accountapp.service.impl;

import com.devsu.challenge.backend.apirest.accountapp.dto.MovimientoRequestDTO;
import com.devsu.challenge.backend.apirest.accountapp.entity.Cuenta;
import com.devsu.challenge.backend.apirest.accountapp.entity.Movimiento;
import com.devsu.challenge.backend.apirest.accountapp.enums.TipoMovimiento;
import com.devsu.challenge.backend.apirest.accountapp.exceptions.CuentaNoExisteException;
import com.devsu.challenge.backend.apirest.accountapp.exceptions.SaldoNoDisponibleException;
import com.devsu.challenge.backend.apirest.accountapp.repository.CuentaRepository;
import com.devsu.challenge.backend.apirest.accountapp.repository.MovimientoRepository;
import com.devsu.challenge.backend.apirest.accountapp.service.IMovimientoService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovimientoServiceImpl implements IMovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;

    public MovimientoServiceImpl(MovimientoRepository movimientoRepository, CuentaRepository cuentaRepository) {
        this.movimientoRepository = movimientoRepository;
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Movimiento> getAllMovimientos() {
        return movimientoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Movimiento> getMovimientoById(Long id) {
        return movimientoRepository.findById(id);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Movimiento createMovimiento(MovimientoRequestDTO movimientoRequestDto) {

        // Verificar si la cuenta existe y está activa
        Optional<Cuenta> cuentaOpt = cuentaRepository.findByNumeroCuenta(movimientoRequestDto.getCuentaId());

        if (cuentaOpt.isPresent() && cuentaOpt.get().isEstado()) {
            Cuenta cuenta = cuentaOpt.get();
            double saldoIniMov = cuenta.getSaldoInicial();

            double nuevoSaldo = cuenta.getSaldoInicial() + movimientoRequestDto.getValor();

            // Si el nuevo saldo es menor a 0, lanza el aviso de saldo no disponible.
            if (nuevoSaldo < 0) {
                throw new SaldoNoDisponibleException("Saldo no disponible");
            }

            cuenta.setSaldoInicial(nuevoSaldo);
            cuentaRepository.save(cuenta);

            Movimiento movimiento = this.crearMovimientoEntity(saldoIniMov, cuenta, movimientoRequestDto);

            return movimientoRepository.save(movimiento);
        } else {
            throw new CuentaNoExisteException("La cuenta no existe o no está activa");
        }
    }

    private Movimiento crearMovimientoEntity(double saldoIniMov, Cuenta cuenta, MovimientoRequestDTO movimientoRequestDto) {
        Movimiento movimiento = new Movimiento();
        movimiento.setTipoMovimiento(TipoMovimiento.valueOf(movimientoRequestDto.getTipoMovimiento()));
        movimiento.setValor(movimientoRequestDto.getValor());
        movimiento.setSaldo(saldoIniMov);
        movimiento.setCuenta(cuenta);

        return movimiento;
    }

    @Override
    public Movimiento updateMovimiento(Long id, Movimiento detalleMovimiento) {

        // Verificar si la cuenta existe y está activa
        Optional<Cuenta> cuentaOpt = cuentaRepository.findByNumeroCuenta(detalleMovimiento.getCuenta().getNumeroCuenta());

        if (cuentaOpt.isEmpty() || !cuentaOpt.get().isEstado()) {
            throw new CuentaNoExisteException("La cuenta no existe o no está activa");
        }

        return movimientoRepository.findById(id)
            .map(movimiento -> {
                double nuevoSaldo = cuentaOpt.get().getSaldoInicial() - movimiento.getValor() + detalleMovimiento.getValor();

                if (nuevoSaldo < 0) {
                    throw new SaldoNoDisponibleException("Saldo no disponible");
                }

                movimiento.setFecha(detalleMovimiento.getFecha());
                movimiento.setTipoMovimiento(detalleMovimiento.getTipoMovimiento());
                movimiento.setValor(detalleMovimiento.getValor());
                movimiento.setSaldo(detalleMovimiento.getSaldo());

                return movimientoRepository.save(movimiento);
            }).orElse(null);
    }

    @Override
    public void deleteMovimiento(Long id) {
        movimientoRepository.deleteById(id);
    }
}
