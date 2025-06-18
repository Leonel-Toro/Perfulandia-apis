package com.perfulandia.envios_api.model;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity // Marca la clase como una entidad JPA 
@Table(name = "envios") // Especifica el nombre de la tabla en la base de datos
@Data // Genera automáticamente los métodos getter y setter
@AllArgsConstructor // Genera un constructor con todos los atributos
@NoArgsConstructor // Genera un constructor sin parámetros
public class Envios {

    @Id // Marca el campo como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Genera el valor automáticamente
    private int id;

    // Atributo estado que representa el estado del envío
    private String estado;
    // Atributo fecha que representa la fecha del envío
    private Date fecha;
    // Atributo transportista que representa el transportista del envío
    private String transportista;
    // Atributo tracking que representa el número de seguimiento del envío
    private String tracking;

    private String direccion;
}
