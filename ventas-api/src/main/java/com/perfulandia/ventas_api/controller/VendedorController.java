package com.perfulandia.ventas_api.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.clientes_api.models.ApiResponse;
import com.perfulandia.ventas_api.dto.RegistroVendedorDTO;
import com.perfulandia.ventas_api.models.Vendedor;
import com.perfulandia.ventas_api.service.VendedorService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/vendedor")
public class VendedorController {

    @Autowired
    private VendedorService vendedorService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public ResponseEntity<List<Vendedor>> getAll() {
        return ResponseEntity.ok(vendedorService.getAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{idVendedor}")
    public ResponseEntity<Vendedor> getVendedorById(@PathVariable Long idVendedor) {
        Vendedor vendedor = vendedorService.getById(idVendedor);
        return vendedor != null ? ResponseEntity.ok(vendedor) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/hateoas/")
    public ResponseEntity<CollectionModel<EntityModel<Vendedor>>> getAllHateoas() {
        List<EntityModel<Vendedor>> vendedoresHateoas = vendedorService.getAll().stream()
            .map(v -> EntityModel.of(
                v,
                linkTo(methodOn(VendedorController.class).getVendedorByIdHateoas(v.getIdVendedor())).withRel("vendedor-por-id")
            ))
            .collect(Collectors.toList());

        return ResponseEntity.ok(
            CollectionModel.of(
                vendedoresHateoas,
                linkTo(methodOn(VendedorController.class).getAllHateoas()).withRel("lista-vendedores")
            )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/hateoas/{idVendedor}")
    public ResponseEntity<?> getVendedorByIdHateoas(@PathVariable Long idVendedor) {
        Vendedor vendedor = vendedorService.getById(idVendedor);
        if (vendedor == null) {
            return ResponseEntity.notFound().build();
        }

        EntityModel<Vendedor> resource = EntityModel.of(
            vendedor,
            linkTo(methodOn(VendedorController.class).getVendedorByIdHateoas(idVendedor)).withRel("vendedor-por-id"),
            linkTo(methodOn(VendedorController.class).getAllHateoas()).withRel("lista-vendedores"),
            linkTo(methodOn(VendedorController.class).actualizarVendedorHateoas(idVendedor, null)).withRel("actualizar-vendedor")
        );

        return ResponseEntity.ok(resource);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/hateoas/nuevo")
    public ResponseEntity<?> nuevoVendedorHateoas(@RequestBody RegistroVendedorDTO request, HttpServletRequest httpRequest) {
        List<String> errores = new ArrayList<>();
        if (request.getSucursal() == null || request.getSucursal().equals("")) {
            errores.add("Sucursal: La sucursal no debe estar vacía.");
        }
        if (request.getMetaMensual() == null || request.getMetaMensual().compareTo(BigDecimal.ZERO) <= 0) {
            errores.add("Meta Mensual: La meta mensual no debe estar vacía o debe ser mayor a 0");
        }

        if (!errores.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, String.join("; ", errores)));
        }

        try {
            vendedorService.registrarVendedor(request);
            List<Vendedor> lista = vendedorService.getAll();
            Vendedor nuevoVendedor = lista.get(lista.size() - 1);

            EntityModel<Vendedor> vendedorModel = EntityModel.of(
                nuevoVendedor,
                linkTo(methodOn(VendedorController.class).getVendedorByIdHateoas(nuevoVendedor.getIdVendedor())).withRel("vendedor-por-id"),
                linkTo(methodOn(VendedorController.class).getAllHateoas()).withRel("lista-vendedores")
            );

            return ResponseEntity
                .created(linkTo(methodOn(VendedorController.class).getVendedorByIdHateoas(nuevoVendedor.getIdVendedor())).toUri())
                .body(vendedorModel);

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, ex.getMessage()));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/hateoas/{idVendedor}")
    public ResponseEntity<?> actualizarVendedorHateoas(@PathVariable Long idVendedor, @RequestBody Vendedor vendedor) {
        try {
            Vendedor vendedorActualizado = vendedorService.actualizarVendedor(idVendedor, vendedor);
            if (vendedorActualizado == null) {
                return ResponseEntity.notFound().build();
            }

            EntityModel<Vendedor> vendedorModel = EntityModel.of(
                vendedorActualizado,
                linkTo(methodOn(VendedorController.class).getVendedorByIdHateoas(idVendedor)).withRel("vendedor-por-id"),
                linkTo(methodOn(VendedorController.class).getAllHateoas()).withRel("lista-vendedores")
            );

            return ResponseEntity.ok(vendedorModel);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, ex.getMessage()));
        }
    }
}
