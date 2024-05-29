package com.devsu.challenge.backend.apirest.accountapp.service.impl;

import com.devsu.challenge.backend.apirest.accountapp.dto.ClienteResponse;
import com.devsu.challenge.backend.apirest.accountapp.entity.Cuenta;
import com.devsu.challenge.backend.apirest.accountapp.exceptions.ClienteNoEncontradoException;
import com.devsu.challenge.backend.apirest.accountapp.repository.CuentaRepository;
import com.devsu.challenge.backend.apirest.accountapp.service.ICuentaService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class CuentaServiceImpl implements ICuentaService {

    @Value("${mscustomer.cliente.get.clientid.url}")
    private String getClientByClientIdUrl;

    private final CuentaRepository cuentaRepository;

    private final RestTemplate restTemplate;

    public CuentaServiceImpl(CuentaRepository cuentaRepository, RestTemplate restTemplate) {
        this.cuentaRepository = cuentaRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cuenta> getAllCuentas() {
        return cuentaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cuenta> getCuentaById(Long id) {
        return cuentaRepository.findById(id);
    }

    @Override
    public Cuenta crearCuenta(Cuenta cuenta) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = getClientByClientIdUrl + cuenta.getClientId();

        ResponseEntity<ClienteResponse> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            ClienteResponse.class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            // El cliente existe. Verificar que el cliente está activo.
            if (Boolean.TRUE.equals(response.getBody().getEstado())) {
                return cuentaRepository.save(cuenta);
            } else {
                throw new ClienteNoEncontradoException("Cliente no esta activo");
            }
        } else {
            // El cliente no existe, lanzar una excepción o manejar el caso de error según corresponda
            throw new ClienteNoEncontradoException("Cliente con clientId={" + cuenta.getClientId() + "} no encontrado");
        }
    }

    @Override
    public Cuenta updateCuenta(Long id, Cuenta detalleCuenta) {
        return cuentaRepository.findById(id)
            .map(cuenta -> {
                cuenta.setNumeroCuenta(detalleCuenta.getNumeroCuenta());
                cuenta.setTipoCuenta(detalleCuenta.getTipoCuenta());
                cuenta.setSaldoInicial(detalleCuenta.getSaldoInicial());
                cuenta.setEstado(detalleCuenta.isEstado());
                return cuentaRepository.save(cuenta);
            }).orElse(null);
    }

    @Override
    public void deleteCuenta(Long id) {
        cuentaRepository.deleteById(id);
    }
}
