package com.perfulandia.ventas_api.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.perfulandia.ventas_api.dto.*;
import com.perfulandia.ventas_api.models.DetalleVenta;
import com.perfulandia.ventas_api.repository.DetalleVentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.perfulandia.ventas_api.models.Vendedor;
import com.perfulandia.ventas_api.models.Venta;
import com.perfulandia.ventas_api.repository.VentaRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ClienteDTOService clienteDTOService;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private RestTemplate restTemplate;

    public NuevaVentaDTO procesarVenta(NuevaVentaDTO nuevaVentaDTO){
        try {
            ClienteDTO cliente = clienteDTOService.findById(nuevaVentaDTO.getVenta().getIdCliente());
            if (cliente == null) {
                throw new IllegalArgumentException("Cliente no asignado.");
            }

            if (nuevaVentaDTO.getVenta().getVendedor() == null) {
                throw new IllegalArgumentException("Vendedor no asignado.");
            }

            if (nuevaVentaDTO.getVenta().getFecha() == null) {
                throw new IllegalArgumentException("La fecha no puede estar vacía.");
            }

            if(nuevaVentaDTO.getDetalleVenta().getIdProducto() == null || nuevaVentaDTO.getDetalleVenta().getIdProducto().equals("")){
                throw new IllegalArgumentException("El producto no puede ser vacio.");
            }

            if(nuevaVentaDTO.getDetalleVenta().getCantidad() <= 0){
                throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
            }

            ProductoDTO producto = new ProductoDTO();
            try {
                producto = restTemplate.getForObject("http://localhost:8080/api/productos/"+nuevaVentaDTO.getDetalleVenta().getIdProducto(),ProductoDTO.class);
            }catch (HttpStatusCodeException e) {
                System.err.println("Error HTTP al ajustar inventario: " + e.getStatusCode());
                System.err.println("Respuesta del servidor: " + e.getResponseBodyAsString());
                throw new RuntimeException("No se pudo ajustar el inventario: " + e.getMessage(), e);
            }

            BigDecimal total = producto.getPrecio().multiply(BigDecimal.valueOf(nuevaVentaDTO.getDetalleVenta().getCantidad()));
            nuevaVentaDTO.getVenta().setTotal(total);
            nuevaVentaDTO.getDetalleVenta().setPrecioUnitario(producto.getPrecio());
            // Crear y enviar el envío al microservicio de envíos
            EnvioRequest envio = new EnvioRequest();
            envio.setEstado("Pendiente");
            envio.setFecha(nuevaVentaDTO.getVenta().getFecha());
            envio.setTransportista("Por asignar");
            envio.setTracking("En proceso");
            envio.setDireccion(cliente.getDireccion());

            try {
                restTemplate.put(
                        "http://localhost:8080/api/inventario/ajustar",
                        new InventarioDTO(nuevaVentaDTO.getDetalleVenta().getIdProducto(), nuevaVentaDTO.getDetalleVenta().getCantidad())
                );
            }catch (HttpStatusCodeException e) {
                System.err.println("Error HTTP al ajustar inventario: " + e.getStatusCode());
                System.err.println("Respuesta del servidor: " + e.getResponseBodyAsString());
                throw new RuntimeException("No se pudo ajustar el inventario: " + e.getMessage(), e);
            }

            try {
                restTemplate.postForEntity("http://localhost:8080/envios/", envio, Void.class);
            } catch (Exception e) {
                System.err.println("Error al registrar el envío: " + e.getMessage());
            }

            ventaRepository.save(nuevaVentaDTO.getVenta());
            nuevaVentaDTO.getDetalleVenta().setVenta(nuevaVentaDTO.getVenta());
            detalleVentaRepository.save(nuevaVentaDTO.getDetalleVenta());
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar la venta: " + e.getMessage(), e);
        }
        return nuevaVentaDTO;
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
