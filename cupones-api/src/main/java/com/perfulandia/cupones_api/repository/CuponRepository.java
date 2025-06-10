package com.perfulandia.cupones_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.perfulandia.cupones_api.models.Cupon;

@Repository
public interface CuponRepository extends JpaRepository<Cupon, Long> {
    Cupon findByCodigo(String codigo);
}
