package com.perfulandia.cupones_api.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.cupones_api.dto.CuponDTO;
import com.perfulandia.cupones_api.models.Cupon;
import com.perfulandia.cupones_api.repository.CuponRepository;

@Service
public class CuponServices {

    @Autowired
    private CuponRepository cuponRepository;

    public List<Cupon> getAll() {
        return cuponRepository.findAll();
    }

    public Cupon getById(Long id) {
        Optional<Cupon> cupon = cuponRepository.findById(id);
        return cupon.orElse(null);
    }

    // Validar cupón según código y fecha de vencimiento
    public Cupon validarCupon(CuponDTO request) {
        Cupon cupon = cuponRepository.findByCodigo(request.getCodigo());
        if (cupon != null && cupon.getValidoHasta().isAfter(LocalDate.now())) {
            return cupon;
        } else {
            throw new RuntimeException("Cupón no válido o vencido");
        }
    }

    public Cupon save(Cupon cupon) {
        return cuponRepository.save(cupon);
    }

    public Cupon update(Long id, Cupon cupon) {
        if (cuponRepository.existsById(id)) {
            cupon.setId(id);
            return cuponRepository.save(cupon);
        }
        return null;
    }

    public Cupon delete(Long id) {
        Optional<Cupon> cupon = cuponRepository.findById(id);
        if (cupon.isPresent()) {
            cuponRepository.deleteById(id);
            return cupon.get();
        }
        return null;
    }
}
