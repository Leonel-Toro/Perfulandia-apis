package com.usuarios_api.repository;

import com.usuarios_api.models.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol,Long> {
    Optional<Rol> findByRol(String rol);

}
