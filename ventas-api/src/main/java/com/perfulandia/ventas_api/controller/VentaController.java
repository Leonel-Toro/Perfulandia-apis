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
import com.perfulandia.ventas_api.dto.NuevaVentaDTO;
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
    @GetMapping("/{id}")
    public ResponseEntity<Venta> getVentaById(@PathVariable Long id) {
        Venta venta = ventaService.findById(id);
        return venta != null ? ResponseEntity.ok(venta) : ResponseEntity.notFound().build();
    }

    // ✅ ✅ ✅ VERSIÓN HATEOAS SEPARADA
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    @GetMapping("/hateoas/")
    public ResponseEntity<CollectionModel<EntityModel<Venta>>> getVentasHateoas() {
        List<Venta> ventas = ventaService.getVentas();
        List<EntityModel<Venta>> ventasHateoas = ventas.stream()
            .map(v -> EntityModel.of(
                v,
                linkTo(methodOn(VentaController.class).getVentaHateoas(v.getIdVenta())).withSelfRel()
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


    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    @PostMapping("/nueva")
    public ResponseEntity<?> nuevaVenta(@RequestBody NuevaVentaDTO nuevaVentaDTO) {
        try {
            NuevaVentaDTO nuevaVenta = ventaService.procesarVenta(nuevaVentaDTO);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(200, "Se ha realizado la venta con exito."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(500, "Error interno: " + e.getMessage()));
        }
    }


    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<CollectionModel<EntityModel<Venta>>> listaVentasByIdCliente(@PathVariable Long idCliente) {
        List<Venta> ventas = ventaService.getVentasByIdCliente(idCliente);
        List<EntityModel<Venta>> ventasHateoas = ventas.stream()
            .map(v -> EntityModel.of(
                v,
                linkTo(methodOn(VentaController.class).getVentaHateoas(v.getIdVenta())).withSelfRel()
            ))
            .collect(Collectors.toList());

        return ResponseEntity.ok(
            CollectionModel.of(
                ventasHateoas,
                linkTo(methodOn(VentaController.class).listaVentasByIdCliente(idCliente)).withSelfRel()
            )
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    @GetMapping("/vendedor/{idVendedor}")
    public ResponseEntity<?> listarVentasByVendedor(@PathVariable Long idVendedor) {
        Vendedor vendedor = vendedorService.getById(idVendedor);
        if (vendedor == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, "Vendedor no encontrado."));
        }

        List<Venta> ventas = ventaService.getVentasByIdVendedor(vendedor);
        List<EntityModel<Venta>> ventasHateoas = ventas.stream()
            .map(v -> EntityModel.of(
                v,
                linkTo(methodOn(VentaController.class).getVentaHateoas(v.getIdVenta())).withSelfRel()
            ))
            .collect(Collectors.toList());

        return ResponseEntity.ok(
            CollectionModel.of(
                ventasHateoas,
                linkTo(methodOn(VentaController.class).listarVentasByVendedor(idVendedor)).withSelfRel()
            )
        );
    }
}
