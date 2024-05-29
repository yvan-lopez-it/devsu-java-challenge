package com.devsu.challenge.backend.apirest.accountapp.service;

import com.devsu.challenge.backend.apirest.accountapp.dto.ReporteMovimientoDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface IReporteService {

    List<ReporteMovimientoDTO> getMovimientosPorFechaYCliente(LocalDateTime fechaInicioLDT, LocalDateTime fechaFinLDT, String clientId);
}
