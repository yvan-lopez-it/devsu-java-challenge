package com.devsu.challenge.backend.apirest.accountapp.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.devsu.challenge.backend.apirest.accountapp.dto.ReporteMovimientoDTO;
import com.devsu.challenge.backend.apirest.accountapp.repository.MovimientoRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ReporteServiceImplTest {

    @InjectMocks
    private ReporteServiceImpl reporteService;

    @Mock
    private MovimientoRepository movimientoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetMovimientosPorFechaYCliente() {
        // Data de prueba
        LocalDateTime fechaInicio = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime fechaFin = LocalDateTime.of(2024, 12, 31, 23, 59);
        String clientId = "Cliente123";

        List<Object[]> results = Arrays.asList(
            new Object[]{"2024-05-28", "Marianela Montalvo", "225487", "CORRIENTE", 100.0, true, 600.0, 700.0},
            new Object[]{"2024-05-29", "Marianela Montalvo", "225487", "CORRIENTE", 700.0, true, -200.0, 500.0}
        );

        when(movimientoRepository.findMovimientosPorFechaYCliente(fechaInicio, fechaFin, clientId)).thenReturn(results);

        // Llamada al metodo
        List<ReporteMovimientoDTO> result = reporteService.getMovimientosPorFechaYCliente(fechaInicio, fechaFin, clientId);

        // Verifica
        assertThat(result).hasSize(2);

        // 2 Reportes de prueba
        ReporteMovimientoDTO reporte1 = result.get(0);
        assertThat(reporte1.getFecha()).isEqualTo("2024-05-28");
        assertThat(reporte1.getCliente()).isEqualTo("Marianela Montalvo");
        assertThat(reporte1.getNumeroCuenta()).isEqualTo("225487");
        assertThat(reporte1.getTipo()).isEqualTo("CORRIENTE");
        assertThat(reporte1.getSaldoInicial()).isEqualTo(100.0);
        assertThat(reporte1.getEstado()).isEqualTo(true);
        assertThat(reporte1.getMovimiento()).isEqualTo(600.0);
        assertThat(reporte1.getSaldoDisponible()).isEqualTo(700.0);

        ReporteMovimientoDTO reporte2 = result.get(1);
        assertThat(reporte2.getFecha()).isEqualTo("2024-05-29");
        assertThat(reporte2.getCliente()).isEqualTo("Marianela Montalvo");
        assertThat(reporte2.getNumeroCuenta()).isEqualTo("225487");
        assertThat(reporte2.getTipo()).isEqualTo("CORRIENTE");
        assertThat(reporte2.getSaldoInicial()).isEqualTo(700.0);
        assertThat(reporte2.getEstado()).isEqualTo(true);
        assertThat(reporte2.getMovimiento()).isEqualTo(-200.0);
        assertThat(reporte2.getSaldoDisponible()).isEqualTo(500.0);

        // Verifica
        verify(movimientoRepository, times(1))
            .findMovimientosPorFechaYCliente(fechaInicio, fechaFin, clientId);
    }

}
