package com.devsu.challenge.backend.apirest.customerapp.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.devsu.challenge.backend.apirest.customerapp.entity.Cliente;
import com.devsu.challenge.backend.apirest.customerapp.repository.ClienteRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Perez");
        cliente.setGenero("Masculino");
        cliente.setEdad(30L);
        cliente.setIdentificacion("1235468990");
        cliente.setDireccion("Calle La Calle 123");
        cliente.setTelefono("990000000");
        cliente.setClientId("fakeClientId987");
        cliente.setContrasena("password");
        cliente.setEstado(true);
    }

    @Test
    void testGetAllClientes() {
        List<Cliente> clientes = Collections.singletonList(cliente);
        when(clienteRepository.findAll()).thenReturn(clientes);

        List<Cliente> result = clienteService.getAllClientes();
        assertEquals(1, result.size());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void testGetClienteById() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Optional<Cliente> result = clienteService.getClienteById(1L);
        assertTrue(result.isPresent());
        assertEquals("Juan Perez", result.get().getNombre());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateCliente() {
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente result = clienteService.createCliente(cliente);
        assertEquals("Juan Perez", result.getNombre());
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    void testUpdateCliente() {
        Cliente updatedCliente = new Cliente();
        updatedCliente.setNombre("Gustavo Cerati");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente result = clienteService.updateCliente(1L, updatedCliente);
        assertEquals("Gustavo Cerati", result.getNombre());
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    void testUpdateClienteNotFound() {
        Cliente updatedCliente = new Cliente();
        updatedCliente.setNombre("Gustavo Cerati");

        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        Cliente result = clienteService.updateCliente(1L, updatedCliente);
        assertNull(result);
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(0)).save(any(Cliente.class));
    }

    @Test
    void testDeleteCliente() {
        doNothing().when(clienteRepository).deleteById(1L);

        clienteService.deleteCliente(1L);
        verify(clienteRepository, times(1)).deleteById(1L);
    }

}
