package com.devsu.challenge.backend.apirest.accountapp.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.devsu.challenge.backend.apirest.accountapp.dto.MovimientoRequestDTO;
import com.devsu.challenge.backend.apirest.accountapp.entity.Cuenta;
import com.devsu.challenge.backend.apirest.accountapp.entity.Movimiento;
import com.devsu.challenge.backend.apirest.accountapp.enums.TipoMovimiento;
import com.devsu.challenge.backend.apirest.accountapp.exceptions.CuentaNoExisteException;
import com.devsu.challenge.backend.apirest.accountapp.exceptions.SaldoNoDisponibleException;
import com.devsu.challenge.backend.apirest.accountapp.repository.CuentaRepository;
import com.devsu.challenge.backend.apirest.accountapp.repository.MovimientoRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class MovimientoServiceImplTest {

    @InjectMocks
    private MovimientoServiceImpl movimientoService;

    @Mock
    private MovimientoRepository movimientoRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllMovimientos() {
        List<Movimiento> movimientos = new ArrayList<>();
        movimientos.add(new Movimiento());
        when(movimientoRepository.findAll())
            .thenReturn(movimientos);

        List<Movimiento> allMovimientos = movimientoService.getAllMovimientos();
        assertThat(allMovimientos).isNotEmpty();
        verify(movimientoRepository, times(1)).findAll();
    }

    @Test
    void testGetMovimientoById() {
        Movimiento movimiento = new Movimiento();
        when(movimientoRepository.findById(anyLong()))
            .thenReturn(Optional.of(movimiento));

        Optional<Movimiento> result = movimientoService.getMovimientoById(1L);
        assertThat(result).isPresent();
        verify(movimientoRepository, times(1)).findById(anyLong());
    }

    @Test
    void testCreateMovimiento_CuentaExisteYActiva() {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta("113456789");
        cuenta.setEstado(true);
        cuenta.setSaldoInicial(1000.0);

        MovimientoRequestDTO movimientoRequestDTO = new MovimientoRequestDTO();
        movimientoRequestDTO.setCuentaId("113456789");
        movimientoRequestDTO.setTipoMovimiento("DEPOSITO");
        movimientoRequestDTO.setValor(500.0);

        when(cuentaRepository.findByNumeroCuenta(anyString()))
            .thenReturn(Optional.of(cuenta));
        when(movimientoRepository.save(any(Movimiento.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Movimiento resultMovimiento = movimientoService.createMovimiento(movimientoRequestDTO);

        assertThat(resultMovimiento).isNotNull();
        assertThat(resultMovimiento.getCuenta()).isEqualTo(cuenta);
        assertThat(resultMovimiento.getSaldo()).isEqualTo(1000.0);
        assertThat(cuenta.getSaldoInicial()).isEqualTo(1500.0);
        verify(cuentaRepository, times(1)).findByNumeroCuenta(anyString());
        verify(cuentaRepository, times(1)).save(any(Cuenta.class));
        verify(movimientoRepository, times(1)).save(any(Movimiento.class));
    }

    @Test
    void testCreateMovimiento_CuentaNoExiste() {
        MovimientoRequestDTO movimientoRequestDTO = new MovimientoRequestDTO();
        movimientoRequestDTO.setCuentaId("113456789");
        movimientoRequestDTO.setTipoMovimiento("RETIRO");
        movimientoRequestDTO.setValor(-500.0);

        when(cuentaRepository.findByNumeroCuenta(anyString()))
            .thenReturn(Optional.empty());

        assertThrows(CuentaNoExisteException.class, () -> movimientoService.createMovimiento(movimientoRequestDTO));

        verify(cuentaRepository, times(1)).findByNumeroCuenta(anyString());
        verify(movimientoRepository, times(0)).save(any(Movimiento.class));
    }

    @Test
    void testCreateMovimiento_SaldoNoDisponible() {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta("113456789");
        cuenta.setEstado(true);
        cuenta.setSaldoInicial(5.0);

        MovimientoRequestDTO movimientoRequestDTO = new MovimientoRequestDTO();
        movimientoRequestDTO.setCuentaId("113456789");
        movimientoRequestDTO.setTipoMovimiento("RETIRO");
        movimientoRequestDTO.setValor(-999.0);

        when(cuentaRepository.findByNumeroCuenta(anyString()))
            .thenReturn(Optional.of(cuenta));

        assertThrows(SaldoNoDisponibleException.class,
            () -> movimientoService.createMovimiento(movimientoRequestDTO));

        verify(cuentaRepository, times(1)).findByNumeroCuenta(anyString());
        verify(movimientoRepository, times(0)).save(any(Movimiento.class));
    }

    @Test
    void testUpdateMovimiento() {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta("113456789");
        cuenta.setEstado(true);
        cuenta.setSaldoInicial(1000.0);

        Movimiento movimientoExistente = new Movimiento();
        movimientoExistente.setId(1L);
        movimientoExistente.setCuenta(cuenta);
        movimientoExistente.setValor(500.0);
        movimientoExistente.setSaldo(500.0);

        Movimiento nuevoMovimiento = new Movimiento();
        nuevoMovimiento.setCuenta(cuenta);
        nuevoMovimiento.setTipoMovimiento(TipoMovimiento.RETIRO);
        nuevoMovimiento.setValor(200.0);
        nuevoMovimiento.setSaldo(500.0);

        when(cuentaRepository.findByNumeroCuenta(anyString()))
            .thenReturn(Optional.of(cuenta));
        when(movimientoRepository.findById(anyLong()))
            .thenReturn(Optional.of(movimientoExistente));
        when(movimientoRepository.save(any(Movimiento.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Movimiento resultMovimiento = movimientoService
            .updateMovimiento(1L, nuevoMovimiento);

        assertThat(resultMovimiento).isNotNull();
        assertThat(resultMovimiento.getValor()).isEqualTo(200.0);
        verify(cuentaRepository, times(1)).findByNumeroCuenta(anyString());
        verify(movimientoRepository, times(1)).findById(anyLong());
        verify(movimientoRepository, times(1)).save(any(Movimiento.class));
    }

    @Test
    void testDeleteMovimiento() {
        doNothing().when(movimientoRepository).deleteById(anyLong());
        movimientoService.deleteMovimiento(1L);
        verify(movimientoRepository, times(1)).deleteById(anyLong());
    }

}
