package com.devsu.challenge.backend.apirest.customerapp.service;

import com.devsu.challenge.backend.apirest.customerapp.entity.Cliente;
import java.util.List;
import java.util.Optional;

public interface IClienteService {

    List<Cliente> getAllClientes();

    Optional<Cliente> getClienteById(Long id);

    Optional<Cliente> getClienteByClientId(String clientId);

    Cliente createCliente(Cliente cliente);

    Cliente updateCliente(Long id, Cliente detalleCliente);

    void deleteCliente(Long id);

}
