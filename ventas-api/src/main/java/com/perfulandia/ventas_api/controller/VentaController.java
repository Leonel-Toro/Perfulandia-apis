package com.perfulandia.ventas_api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clientes_api.models.ApiResponse;
import com.perfulandia.ventas_api.dto.VentaRequestDTO;
import com.perfulandia.ventas_api.models.Vendedor;
import com.perfulandia.ventas_api.models.Venta;
import com.perfulandia.ventas_api.service.VendedorService;
import com.perfulandia.ventas_api.service.VentaService;

@RestController
@RequestMapping("/api/venta")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private VendedorService vendedorService;

    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    @GetMapping("/")
    public ResponseEntity<List<Venta>> listaVentas() {
        List<Venta> ventas = ventaService.getVentas();
        return ventas.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(ventas);
    }

    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    @PostMapping("/nueva")
    public ResponseEntity<?> nuevaVenta(@RequestBody VentaRequestDTO request) {
        try {
            Venta nueva = ventaService.procesarVenta(request.getVenta(), request.getDetalleVenta());
            return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(500, "Error interno: " + e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<?> listaVentasByIdCliente(@PathVariable Long idCliente) {
        List<Venta> ventas = ventaService.getVentasByIdCliente(idCliente);
        return ResponseEntity.ok(ventas);
    }

    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    @GetMapping("/vendedor/{idVendedor}")
    public ResponseEntity<?> listarVentasByVendedor(@PathVariable Long idVendedor) {
        Vendedor vendedor = vendedorService.getById(idVendedor);
        if (vendedor == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, "Vendedor no encontrado."));
        }

        List<Venta> ventas = ventaService.getVentasByIdVendedor(vendedor);
        return ResponseEntity.ok(ventas);
    }

    // ✅ NUEVO: Listar ventas con HATEOAS
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    @GetMapping("/hateoas")
    public ResponseEntity<CollectionModel<EntityModel<Venta>>> getVentasHateoas() {
        List<EntityModel<Venta>> ventasHateoas = ventaService.getVentas().stream()
                .map(venta -> EntityModel.of(
                        venta,
                        linkTo(methodOn(VentaController.class).getVentaHateoas(venta.getIdVenta())).withSelfRel()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(
                        ventasHateoas,
                        linkTo(methodOn(VentaController.class).getVentasHateoas()).withSelfRel()
                )
        );
    }


    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    @GetMapping("/hateoas/{id}")
    public ResponseEntity<EntityModel<Venta>> getVentaHateoas(@PathVariable Long id) {
        Venta venta = ventaService.findById(id);
        if (venta == null) {
            return ResponseEntity.notFound().build();
        }

        EntityModel<Venta> resource = EntityModel.of(
                venta,
                linkTo(methodOn(VentaController.class).getVentaHateoas(id)).withSelfRel(),
                linkTo(methodOn(VentaController.class).getVentasHateoas()).withRel("all-ventas")
        );

        return ResponseEntity.ok(resource);
    }
}
