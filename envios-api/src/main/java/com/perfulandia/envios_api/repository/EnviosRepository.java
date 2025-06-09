package com.perfulandia.envios_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.perfulandia.envios_api.models.Envios;

@Repository
public interface EnviosRepository extends JpaRepository<Envios, Long> {
}
