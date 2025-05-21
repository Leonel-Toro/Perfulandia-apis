package com.perfulandia.soporte_api.services;

import com.perfulandia.soporte_api.Model.Ticket;
import com.perfulandia.soporte_api.repository.TicketRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;
    // Método para obtener todos los tickets
    public List<Ticket> getAll() {
        return ticketRepository.findAll();
    }

    // Método para obtener un ticket por su ID
    public Ticket getById(Integer id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        return ticket.orElse(null); // Devuelve null si no se encuentra el ticket
    }

    // Método para crear un nuevo ticket
    public Ticket add(Ticket ticket) {
        return ticketRepository.save(ticket);
    }
    // Método para actualizar un ticket existente
    public Ticket update(Integer id, Ticket ticket) {
        if (ticketRepository.existsById(id)) {
            ticket.setId(id); // Asegúrate de que el ID del ticket a actualizar sea correcto
            return ticketRepository.save(ticket);
        }
        return null; // Devuelve null si no se encuentra el ticket
    }

    // Método para eliminar un ticket por su ID
    public Ticket delete(Integer id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if (ticket.isPresent()) {
            ticketRepository.deleteById(id); // Elimina el ticket de la base de datos
            return ticket.get(); // Devuelve el ticket eliminado
        }
        return null; // Devuelve null si no se encuentra el ticket
    }



}
