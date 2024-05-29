package com.devsu.challenge.backend.apirest.customerapp;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.devsu.challenge.backend.apirest.customerapp.entity.Cliente;
import com.devsu.challenge.backend.apirest.customerapp.repository.ClienteRepository;
import java.util.Optional;
import org.assertj.core.api.OptionalAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MsCustomerApplicationTests {

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void testCreateAndFindCliente() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Juan Perez");
        cliente.setGenero("Masculino");
        cliente.setEdad(30L);
        cliente.setIdentificacion("1234509876");
        cliente.setDireccion("Calle La Calle 123");
        cliente.setTelefono("555-555-555");
        cliente.setClientId("fakeClientId123");
        cliente.setContrasena("password");
        cliente.setEstado(true);

        Cliente savedCliente = clienteRepository.save(cliente);
        Optional<Cliente> foundCliente = clienteRepository.findById(savedCliente.getId());

        assertThat(foundCliente).isPresent();
        assertThat(foundCliente.get().getNombre()).isEqualTo("Juan Perez");
    }

    @AfterEach
    void cleanUp() {
        clienteRepository.deleteAll();
    }

}
