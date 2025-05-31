package com.perfulandia.ventas_api.repository;

import com.perfulandia.ventas_api.models.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta,Long> {
    List<Venta> findByIdCliente(Long idCliente);
}
