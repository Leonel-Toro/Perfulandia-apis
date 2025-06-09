package com.perfulandia.ventas_api.repository;

import com.perfulandia.ventas_api.models.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendedorRepository extends JpaRepository<Vendedor,Long> {

}
