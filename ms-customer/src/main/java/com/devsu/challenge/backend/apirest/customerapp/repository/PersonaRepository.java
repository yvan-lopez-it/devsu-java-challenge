package com.devsu.challenge.backend.apirest.customerapp.repository;

import com.devsu.challenge.backend.apirest.customerapp.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<Persona, Long> {

}
