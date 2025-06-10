package com.perfulandia.soporte_api.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;


@Entity // Marca esta clase como entidad JPA (mapeada a una tabla)
@Table(name = "tickets_soporte") // Nombre de la tabla en la base de datos
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String asunto;

    private String mensaje;

    private String estado;

    private Date fechaCreacion;

}