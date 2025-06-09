package com.perfulandia.ventas_api.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.perfulandia.ventas_api.client.ClienteRestClient;
import com.perfulandia.ventas_api.client.CuponRestClient;
import com.perfulandia.ventas_api.client.EnvioClient;
import com.perfulandia.ventas_api.dto.ClienteDTO;
import com.perfulandia.ventas_api.dto.CuponRequestDTO;
import com.perfulandia.ventas_api.dto.CuponResponseDTO;
import com.perfulandia.ventas_api.dto.EnvioRequest;
import com.perfulandia.ventas_api.models.Venta;
import com.perfulandia.ventas_api.repository.VentaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ClienteRestClient clienteRest;
    private final CuponRestClient cuponRest;
    private final EnvioClient envioClient;

    public Venta procesarVenta(Venta nuevaVenta, String codigoCupon) {
        // Validación de cliente
        ClienteDTO cliente = clienteRest.findById(nuevaVenta.getIdCliente());
        if (cliente == null) {
            return null;
        }

        // Validación de cupón
        CuponRequestDTO requestDTO = new CuponRequestDTO(codigoCupon);
        CuponResponseDTO cuponResponse = cuponRest.validarCupon(requestDTO);

        if (cuponResponse == null || !cuponResponse.isValido()) {
            return null;
        }

        // Aplicación de descuento
        BigDecimal descuento = cuponResponse.getDescuento();
        if (descuento != null) {
            BigDecimal totalConDescuento = nuevaVenta.getTotal().subtract(descuento);
            nuevaVenta.setTotal(totalConDescuento.max(BigDecimal.ZERO));
        }

        // Guardar venta
        Venta ventaGuardada = ventaRepository.save(nuevaVenta);

        // Crear envío
        EnvioRequest envioRequest = EnvioRequest.builder()
            .direccion("Dirección genérica")   // Puedes adaptar esto desde la venta o frontend
            .ciudad("Ciudad genérica")
            .codigoPostal("000000")
            .estadoEnvio("PENDIENTE")
            .fechaEnvio(LocalDate.now())
            .ventaId(ventaGuardada.getIdVenta())
            .build();

        envioClient.crearEnvio(envioRequest); // Llamada al microservicio de envíos

        return ventaGuardada;
    }

    public Venta save(Venta venta, String codigoCupon) {
        return procesarVenta(venta, codigoCupon);
    }

    public List<Venta> getVentas() {
        return ventaRepository.findAll();
    }

    public Venta findById(Long id) {
        Optional<Venta> venta = ventaRepository.findById(id);
        return venta.orElse(null);
    }

    public List<Venta> getVentasByIdCliente(Long idCliente) {
        return ventaRepository.findByIdCliente(idCliente);
    }
}
