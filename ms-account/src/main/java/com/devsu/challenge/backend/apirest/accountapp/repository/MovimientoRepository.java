package com.devsu.challenge.backend.apirest.accountapp.repository;

import com.devsu.challenge.backend.apirest.accountapp.entity.Movimiento;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    @Query(
        nativeQuery = true,
        value =
            "SELECT to_char(m.fecha, 'DD/MM/YYYY') AS fecha, "
                + "       p.nombre          AS cliente, "
                + "       c.numero_cuenta   AS numeroCuenta, "
                + "       c.tipo_cuenta     AS tipo, "
                + "       m.saldo           AS saldoInicial, "
                + "       c.estado          AS estado, "
                + "       m.valor           AS movimiento, "
                + "       c.saldo_inicial   AS saldoDisponible "
                + "FROM movimientos m JOIN cuentas c "
                + "    ON m.cuenta_id = c.id "
                + "JOIN clientes cli "
                + "    ON c.client_id = cli.client_id "
                + "JOIN personas p "
                + "    ON cli.id = p.id "
                + "WHERE m.fecha BETWEEN :fechaInicio and :fechaFin "
                + "  AND c.client_id = :clientId")
    List<Object[]> findMovimientosPorFechaYCliente(
        @Param("fechaInicio") LocalDateTime fechaInicio,
        @Param("fechaFin") LocalDateTime fechaFin,
        @Param("clientId") String clientId);

}
