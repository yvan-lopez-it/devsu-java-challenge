package com.devsu.challenge.backend.apirest.customerapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clientes", schema = "public")
@EqualsAndHashCode(callSuper = false)
public class Cliente extends Persona {

    @Serial
    private static final long serialVersionUID = -9015834564155336282L;

    @Column(unique = true)
    private String clientId;

    @NotBlank(message = "La contraseña no puede estar vacia")
    @Size(min = 4, message = "La contraseña debe ser tener por lo menos 4 caracteres a mas")
    @Column(nullable = false)
    private String contrasena;

    @NotBlank(message = "El estado no puede estar vacio")
    @Column(nullable = false)
    private boolean estado = true;

}
