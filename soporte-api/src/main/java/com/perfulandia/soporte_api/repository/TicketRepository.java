package com.perfulandia.soporte_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.perfulandia.soporte_api.Model.Ticket;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    // Este repositorio hereda de JpaRepository, lo que proporciona métodos CRUD básicos
    // Aquí puedes agregar métodos personalizados si es necesario
    // Por ejemplo, para buscar tickets por estado o fecha de creación

}
