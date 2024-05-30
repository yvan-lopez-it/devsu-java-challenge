package com.devsu.challenge.backend.apirest.accountapp.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.devsu.challenge.backend.apirest.accountapp.dto.ClienteResponse;
import com.devsu.challenge.backend.apirest.accountapp.entity.Cuenta;
import com.devsu.challenge.backend.apirest.accountapp.enums.TipoCuenta;
import com.devsu.challenge.backend.apirest.accountapp.exceptions.ClienteNoEncontradoException;
import com.devsu.challenge.backend.apirest.accountapp.repository.CuentaRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

class CuentaServiceImplTest {

    @InjectMocks
    private CuentaServiceImpl cuentaService;

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private RestTemplate restTemplate;

    @Value("${mscustomer.cliente.get.clientid.url}")
    private String getClientByClientIdUrl = "http://localhost:8070/api/clientes/clientid/";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCuentas() {
        List<Cuenta> cuentas = new ArrayList<>();
        cuentas.add(new Cuenta());
        when(cuentaRepository.findAll()).thenReturn(cuentas);

        List<Cuenta> allCuentas = cuentaService.getAllCuentas();

        assertThat(allCuentas).isNotEmpty();
        verify(cuentaRepository, times(1)).findAll();
    }

    @Test
    void testGetCuentaById() {
        Cuenta cuenta = new Cuenta();
        when(cuentaRepository.findById(anyLong())).thenReturn(Optional.of(cuenta));

        Optional<Cuenta> getCuenta = cuentaService.getCuentaById(1L);

        assertThat(getCuenta).isPresent();
        verify(cuentaRepository, times(1)).findById(anyLong());
    }

    @Test
    void testCrearCuenta_ClienteExisteYActivo() {
        Cuenta cuenta = new Cuenta();
        cuenta.setClientId("C1234");

        ClienteResponse clienteResponse = new ClienteResponse();
        clienteResponse.setEstado(true);

        ResponseEntity<ClienteResponse> responseEntity = new ResponseEntity<>(clienteResponse, HttpStatus.OK);
        when(restTemplate.exchange(
            anyString(),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(ClienteResponse.class)))
            .thenReturn(responseEntity);
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuenta);

        Cuenta result = cuentaService.crearCuenta(cuenta);

        assertThat(result).isNotNull();
        verify(restTemplate, times(1))
            .exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(ClienteResponse.class));
        verify(cuentaRepository, times(1)).save(any(Cuenta.class));
    }

    @Test
    void testCrearCuenta_ClienteNoEncontrado() {
        Cuenta cuenta = new Cuenta();
        cuenta.setClientId("C123");

        when(restTemplate.exchange(
            anyString(),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(ClienteResponse.class)))
            .thenThrow(new ClienteNoEncontradoException("Cliente con clientId={" + cuenta.getClientId() + "} no encontrado"));

        assertThrows(ClienteNoEncontradoException.class, () -> cuentaService.crearCuenta(cuenta));

        verify(restTemplate, times(1)).exchange(
            anyString(),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(ClienteResponse.class));
        verify(cuentaRepository, times(0)).save(any(Cuenta.class));
    }

    @Test
    void testUpdateCuenta() {
        Cuenta cuenta = new Cuenta();
        cuenta.setId(1L);
        when(cuentaRepository.findById(anyLong())).thenReturn(Optional.of(cuenta));
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuenta);

        Cuenta updatedCuenta = new Cuenta();
        updatedCuenta.setNumeroCuenta("123456780");
        updatedCuenta.setTipoCuenta(TipoCuenta.CORRIENTE);
        updatedCuenta.setSaldoInicial(5999.00);
        updatedCuenta.setEstado(true);

        Cuenta resultCuenta = cuentaService.updateCuenta(1L, updatedCuenta);

        assertThat(resultCuenta).isNotNull();
        verify(cuentaRepository, times(1)).findById(anyLong());
        verify(cuentaRepository, times(1)).save(any(Cuenta.class));
    }

    @Test
    void testDeleteCuenta() {
        doNothing().when(cuentaRepository).deleteById(anyLong());

        cuentaService.deleteCuenta(1L);

        verify(cuentaRepository, times(1)).deleteById(anyLong());
    }
}
