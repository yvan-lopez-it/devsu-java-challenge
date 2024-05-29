package com.devsu.challenge.backend.apirest.accountapp.service.impl;

import com.devsu.challenge.backend.apirest.accountapp.dto.ReporteMovimientoDTO;
import com.devsu.challenge.backend.apirest.accountapp.repository.MovimientoRepository;
import com.devsu.challenge.backend.apirest.accountapp.service.IReporteService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ReporteServiceImpl implements IReporteService {

    private final MovimientoRepository movimientoRepository;

    public ReporteServiceImpl(MovimientoRepository movimientoRepository) {
        this.movimientoRepository = movimientoRepository;
    }

    @Override
    public List<ReporteMovimientoDTO> getMovimientosPorFechaYCliente(LocalDateTime fechaInicio, LocalDateTime fechaFin, String clientId) {

        List<Object[]> results = movimientoRepository.findMovimientosPorFechaYCliente(fechaInicio, fechaFin, clientId);

        return results.stream().map(result -> {
            ReporteMovimientoDTO reporteDTO = new ReporteMovimientoDTO();
            reporteDTO.setFecha(result[0].toString());
            reporteDTO.setCliente(result[1].toString());
            reporteDTO.setNumeroCuenta(result[2].toString());
            reporteDTO.setTipo(result[3].toString());
            reporteDTO.setSaldoInicial(Double.parseDouble(result[4].toString()));
            reporteDTO.setEstado(Boolean.parseBoolean(result[5].toString()));
            reporteDTO.setMovimiento(Double.parseDouble(result[6].toString()));
            reporteDTO.setSaldoDisponible(Double.parseDouble(result[7].toString()));
            return reporteDTO;
        }).collect(Collectors.toList());
    }

}
