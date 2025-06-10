package com.perfulandia.productos_api.repository;

import com.perfulandia.productos_api.models.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario,Long> {
    Inventario findByIdProducto(Long idProducto);
}
