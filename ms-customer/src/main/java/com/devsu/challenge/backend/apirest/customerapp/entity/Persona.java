package com.devsu.challenge.backend.apirest.customerapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "personas", schema = "public")
public class Persona implements Serializable {

    @Serial
    private static final long serialVersionUID = -1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "Debe especificar el genero de la persona")
    @Column(nullable = false)
    private String genero;

    @NotBlank(message = "Debe especificar la edad")
    @Min(value = 18L, message = "Debe ser mayor a 18 años")
    @Column(nullable = false)
    private Long edad;

    @NotBlank(message = "Debe especificar la edad")
    @Digits(integer = 8, fraction = 0, message = "DNI debe tener 8 digitos")
    @Column(nullable = false)
    private String identificacion;

    @NotBlank(message = "Debe especificar la dirección de la persona")
    @Column(nullable = false)
    private String direccion;

    @NotBlank(message = "Debe especificar la dirección de la persona")
    @Size(min = 9, max = 9)
    @Column(nullable = false)
    private String telefono;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}
