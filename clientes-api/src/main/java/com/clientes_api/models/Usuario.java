package com.clientes_api.models;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    private String user;
    private String email;
    private String password;
    private Boolean estado;
    @OneToMany()
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;

}
