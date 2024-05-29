package com.devsu.challenge.backend.apirest.customerapp.service.impl;

import com.devsu.challenge.backend.apirest.customerapp.entity.Cliente;
import com.devsu.challenge.backend.apirest.customerapp.repository.ClienteRepository;
import com.devsu.challenge.backend.apirest.customerapp.service.IClienteService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteServiceImpl implements IClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> getClienteById(Long id) {
        return clienteRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> getClienteByClientId(String clientId) {
        return clienteRepository.getClienteByClientId(clientId);
    }

    @Override
    public Cliente createCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente updateCliente(Long id, Cliente detalleCliente) {
        return clienteRepository.findById(id)
            .map(cliente -> {
                cliente.setNombre(detalleCliente.getNombre());
                cliente.setGenero(detalleCliente.getGenero());
                cliente.setEdad(detalleCliente.getEdad());
                cliente.setIdentificacion(detalleCliente.getIdentificacion());
                cliente.setDireccion(detalleCliente.getDireccion());
                cliente.setTelefono(detalleCliente.getTelefono());
                cliente.setClientId(detalleCliente.getClientId());
                cliente.setContrasena(detalleCliente.getContrasena());
                cliente.setEstado(detalleCliente.isEstado());
                return clienteRepository.save(cliente);
            }).orElse(null);
    }

    @Override
    public void deleteCliente(Long id) {
        clienteRepository.deleteById(id);
    }
}
