package com.devsu.challenge.backend.apirest.accountapp.controller;

import com.devsu.challenge.backend.apirest.accountapp.dto.ReporteMovimientoDTO;
import com.devsu.challenge.backend.apirest.accountapp.service.IReporteService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    private final IReporteService reporteService;

    public ReporteController(IReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping()
    public ResponseEntity<List<ReporteMovimientoDTO>> getMovimientosPorFechaYCliente(
        @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
        @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
        @RequestParam("cliente") String cliente) {

        LocalDateTime ldtFechaInicio = fechaInicio.atStartOfDay();
        LocalDateTime ldtFechaFin = fechaFin.atTime(LocalTime.MAX);

        List<ReporteMovimientoDTO> reporte = reporteService.getMovimientosPorFechaYCliente(ldtFechaInicio, ldtFechaFin, cliente);

        if (reporte.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reporte, HttpStatus.OK);
    }

}
