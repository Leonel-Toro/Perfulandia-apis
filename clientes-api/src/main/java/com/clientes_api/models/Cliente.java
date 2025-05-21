package com.clientes_api.models;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

@Entity
@Table(name="clientes")
public class Cliente {
    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental (MySQL)
    private Integer id;

    @Column(nullable = false, length = 45)
    private String nombre;

    @Column(nullable = false, length = 45)
    private String apellido;

    private String rut;

    private String telefono;

    private String direccion;

    @OneToMany(mappedBy = "cliente")
    private TicketSoporte ticketSoporte;

    public Cliente() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
