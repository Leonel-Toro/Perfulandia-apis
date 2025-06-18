package com.perfulandia.ventas_api.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.perfulandia.ventas_api.dto.ClienteDTO;
import com.perfulandia.ventas_api.dto.EnvioRequest;
import com.perfulandia.ventas_api.models.Vendedor;
import com.perfulandia.ventas_api.models.Venta;
import com.perfulandia.ventas_api.repository.VentaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ClienteDTOService clienteDTOService;

    @Autowired
    private RestTemplate restTemplate;

    public Venta procesarVenta(Venta nuevaVenta){
        try {
            ClienteDTO cliente = clienteDTOService.findById(nuevaVenta.getIdCliente());
            if (cliente == null) {
                throw new IllegalArgumentException("Cliente no asignado.");
            }

            if (nuevaVenta.getVendedor() == null) {
                throw new IllegalArgumentException("Vendedor no asignado.");
            }

            if (nuevaVenta.getFecha() == null) {
                throw new IllegalArgumentException("La fecha no puede estar vacía.");
            }

            if (nuevaVenta.getTotal() == null || nuevaVenta.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("El total no puede ser nulo, negativo o cero.");
            }

            // Guardar la venta
            Venta ventaGuardada = ventaRepository.save(nuevaVenta);

            // Crear y enviar el envío al microservicio de envíos
            EnvioRequest envio = new EnvioRequest();
            envio.setEstado("Pendiente");
            envio.setFecha(ventaGuardada.getFecha());
            envio.setTransportista("Por asignar");
            envio.setTracking("En proceso");
            envio.setDireccion(cliente.getDireccion());

            try {
                restTemplate.postForEntity("http://localhost:8083/envios", envio, Void.class);
            } catch (Exception e) {
                System.err.println("Error al registrar el envío: " + e.getMessage());
                // Dependiendo del proyecto, puedes hacer rollback o dejar la venta creada igual
            }

            return ventaGuardada;

        } catch (Exception e) {
            throw new RuntimeException("Error al procesar la venta: " + e.getMessage(), e);
        }
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

    public List<Venta> getVentasByIdVendedor(Vendedor vendedor){
        return ventaRepository.findByVendedor(vendedor);
    }
}
