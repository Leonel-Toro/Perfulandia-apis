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


    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    @GetMapping("/hateoas/")
    public ResponseEntity<CollectionModel<EntityModel<Venta>>> getVentasHateoas() {
        List<EntityModel<Venta>> ventasHateoas = ventaService.getVentas().stream()
            .map(venta -> EntityModel.of(
                venta,
                linkTo(methodOn(VentaController.class).getVentaHateoas(venta.getIdVenta())).withRel("venta-por-id")
            ))
            .collect(Collectors.toList());

        return ResponseEntity.ok(
            CollectionModel.of(
                ventasHateoas,
                linkTo(methodOn(VentaController.class).getVentasHateoas()).withRel("lista-ventas")
            )
        );
    }


    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    @GetMapping("/hateoas/{id}")
    public ResponseEntity<?> getVentaHateoas(@PathVariable Long id) {
        Venta venta = ventaService.findById(id);
        if (venta == null) {
            return ResponseEntity.notFound().build();
        }

        EntityModel<Venta> ventaModel = EntityModel.of(
            venta,
            linkTo(methodOn(VentaController.class).getVentaHateoas(id)).withRel("venta-por-id"),
            linkTo(methodOn(VentaController.class).getVentasHateoas()).withRel("lista-ventas"),
            linkTo(methodOn(VentaController.class).nuevaVenta(null)).withRel("crear-venta")
        );

        return ResponseEntity.ok(ventaModel);
    }


    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    @PostMapping("/hateoas")
    public ResponseEntity<?> nuevaVenta(@RequestBody NuevaVentaDTO nuevaVentaDTO) {
        try {
            Venta ventaCreada = ventaService.procesarVenta(nuevaVentaDTO).getVenta();

            EntityModel<Venta> ventaModel = EntityModel.of(
                ventaCreada,
                linkTo(methodOn(VentaController.class).getVentaHateoas(ventaCreada.getIdVenta())).withRel("venta-por-id"),
                linkTo(methodOn(VentaController.class).getVentasHateoas()).withRel("lista-ventas")
            );

            return ResponseEntity
                .created(linkTo(methodOn(VentaController.class).getVentaHateoas(ventaCreada.getIdVenta())).toUri())
                .body(ventaModel);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(500, "Error interno: " + e.getMessage()));
        }
    }


    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    @GetMapping("/hateoas/cliente/{idCliente}")
    public ResponseEntity<CollectionModel<EntityModel<Venta>>> listaVentasByIdCliente(@PathVariable Long idCliente) {
        List<EntityModel<Venta>> ventasHateoas = ventaService.getVentasByIdCliente(idCliente).stream()
            .map(venta -> EntityModel.of(
                venta,
                linkTo(methodOn(VentaController.class).getVentaHateoas(venta.getIdVenta())).withRel("venta-por-id")
            ))
            .collect(Collectors.toList());

        return ResponseEntity.ok(
            CollectionModel.of(
                ventasHateoas,
                linkTo(methodOn(VentaController.class).listaVentasByIdCliente(idCliente)).withRel("ventas-por-cliente")
            )
        );
    }

    // ✅ HATEOAS: Filtrar por Vendedor
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    @GetMapping("/hateoas/vendedor/{idVendedor}")
    public ResponseEntity<?> listarVentasByVendedor(@PathVariable Long idVendedor) {
        Vendedor vendedor = vendedorService.getById(idVendedor);
        if (vendedor == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, "Vendedor no encontrado."));
        }

        List<EntityModel<Venta>> ventasHateoas = ventaService.getVentasByIdVendedor(vendedor).stream()
            .map(venta -> EntityModel.of(
                venta,
                linkTo(methodOn(VentaController.class).getVentaHateoas(venta.getIdVenta())).withRel("venta-por-id")
            ))
            .collect(Collectors.toList());

        return ResponseEntity.ok(
            CollectionModel.of(
                ventasHateoas,
                linkTo(methodOn(VentaController.class).listarVentasByVendedor(idVendedor)).withRel("ventas-por-vendedor")
            )
        );
    }
}
