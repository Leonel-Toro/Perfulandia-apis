package com.perfulandia.cupones_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.cupones_api.dto.CuponDTO;
import com.perfulandia.cupones_api.models.Cupon;
import com.perfulandia.cupones_api.services.CuponServices;

@RestController
@RequestMapping("/api/cupones")
public class CuponController {

    @Autowired
    private CuponServices cuponServices;

    @GetMapping
    public List<Cupon> getAll() {
        return cuponServices.getAll();
    }

    @GetMapping("/{id}")
    public Cupon getById(@PathVariable Long id) {
        return cuponServices.getById(id);
    }

    @PostMapping("/validar")
    public Cupon validarCupon(@RequestBody CuponDTO request) {
        return cuponServices.validarCupon(request);
    }

    @PostMapping
    public Cupon save(@RequestBody Cupon cupon) {
        return cuponServices.save(cupon);
    }

    @PutMapping("/{id}")
    public Cupon update(@PathVariable Long id, @RequestBody Cupon cupon) {
        return cuponServices.update(id, cupon);
    }

    @DeleteMapping("/{id}")
    public Cupon delete(@PathVariable Long id) {
        return cuponServices.delete(id);
    }
}
