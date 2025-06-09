package com.perfulandia.productos_api.repository;

import com.perfulandia.productos_api.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto,Long> {
    List<Producto> findByCategoriaNombre(String categoria);
}
