package com.perfulandia.productos_api.repository;

import com.perfulandia.productos_api.models.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario,Long> {
    Optional<Inventario> findByProducto_Id(Long productoId);
}
