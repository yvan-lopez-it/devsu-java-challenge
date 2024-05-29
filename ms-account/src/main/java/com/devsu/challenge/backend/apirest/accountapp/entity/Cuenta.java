package com.devsu.challenge.backend.apirest.accountapp.entity;

import com.devsu.challenge.backend.apirest.accountapp.enums.TipoCuenta;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cuentas", schema = "public")
public class Cuenta implements Serializable {

    @Serial
    private static final long serialVersionUID = -1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El clientId no puede estar vacio")
    @Column(nullable = false)
    private String clientId;

    @NotBlank(message = "El numero de cuenta no puede estar vacio")
    @Column(nullable = false, unique = true)
    private String numeroCuenta;

    @NotNull(message = "El tipo de cuenta no puede ser nulo")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoCuenta tipoCuenta;

    @NotNull(message = "El saldo inicial no puede estar vacio")
//    @DecimalMin(value = "0.00", inclusive = true, message = "El monto mínimo debe ser 0.00")
    @Column(nullable = false)
    private Double saldoInicial;

    @Column(nullable = false)
    private boolean estado = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}
