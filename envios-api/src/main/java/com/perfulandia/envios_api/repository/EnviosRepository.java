package com.perfulandia.envios_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.perfulandia.envios_api.model.Envios;

@Repository
public interface EnviosRepository extends JpaRepository<Envios, Integer> {
    // Este repositorio hereda de JpaRepository, lo que proporciona métodos CRUD básicos
    // Aquí puedes agregar métodos personalizados si es necesario
    // Por ejemplo, para buscar tickets por estado o fecha de creación

}
