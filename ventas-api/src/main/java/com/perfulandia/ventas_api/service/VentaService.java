package com.perfulandia.ventas_api.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.perfulandia.ventas_api.dto.ClienteDTO;
import com.perfulandia.ventas_api.dto.CuponRequestDTO;
import com.perfulandia.ventas_api.dto.CuponResponseDTO;
import com.perfulandia.ventas_api.dto.EnvioRequest;
import com.perfulandia.ventas_api.dto.InventarioDTO;
import com.perfulandia.ventas_api.models.DetalleVenta;
import com.perfulandia.ventas_api.models.Vendedor;
import com.perfulandia.ventas_api.models.Venta;
import com.perfulandia.ventas_api.repository.DetalleVentaRepository;
import com.perfulandia.ventas_api.repository.VentaRepository;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ClienteDTOService clienteDTOService;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Venta procesarVenta(Venta nuevaVenta, DetalleVenta detalleVenta) {

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

        if (detalleVenta.getIdProducto() == null) {
            throw new IllegalArgumentException("El producto no puede ser vacío.");
        }

        if (detalleVenta.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
        }

        if (detalleVenta.getPrecioUnitario() == null || detalleVenta.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor a 0.");
        }

        BigDecimal total = detalleVenta.getPrecioUnitario().multiply(BigDecimal.valueOf(detalleVenta.getCantidad()));

        if (nuevaVenta.getCodigoCupon() != null && !nuevaVenta.getCodigoCupon().isEmpty()) {
            CuponRequestDTO requestDTO = new CuponRequestDTO(nuevaVenta.getCodigoCupon());

            CuponResponseDTO responseDTO = restTemplate.postForObject(
                "http://localhost:8082/api/cupones/validar",
                requestDTO,
                CuponResponseDTO.class
            );

            if (responseDTO == null || !responseDTO.isValido()) {
                throw new IllegalArgumentException("Cupón inválido o expirado.");
            }

            BigDecimal descuento = responseDTO.getDescuento();
            total = total.subtract(total.multiply(descuento));

            if (total.compareTo(BigDecimal.ZERO) < 0) {
                total = BigDecimal.ZERO;
            }
        }

        nuevaVenta.setTotal(total);

        restTemplate.put(
            "http://localhost:8084/api/inventario/ajustar",
            new InventarioDTO(detalleVenta.getIdProducto(), detalleVenta.getCantidad())
        );

        EnvioRequest envio = new EnvioRequest();
        envio.setEstado("Pendiente");
        envio.setFecha(nuevaVenta.getFecha());
        envio.setTransportista("Por asignar");
        envio.setTracking("En proceso");
        envio.setDireccion(cliente.getDireccion());

        restTemplate.postForEntity("http://localhost:8080/envios/", envio, Void.class);

        ventaRepository.save(nuevaVenta);
        detalleVenta.setVenta(nuevaVenta);
        detalleVentaRepository.save(detalleVenta);

        return nuevaVenta;
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

    public List<Venta> getVentasByIdVendedor(Vendedor vendedor) {
        return ventaRepository.findByVendedor(vendedor);
    }
}
