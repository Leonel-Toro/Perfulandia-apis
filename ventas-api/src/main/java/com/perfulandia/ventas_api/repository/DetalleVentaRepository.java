package com.perfulandia.ventas_api.repository;

import com.perfulandia.ventas_api.models.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta,Long> {

}
