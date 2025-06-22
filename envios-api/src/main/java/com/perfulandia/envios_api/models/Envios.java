package com.perfulandia.envios_api.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "envios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Envios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String direccion;

    private String ciudad;

    private String estado;

    @Column(name = "codigo_postal")
    private String codigoPostal;

    @Column(name = "estado_envio")
    private String estadoEnvio; // pendiente, enviado, entregado, etc.

    @Column(name = "fecha_envio")
    private LocalDate fechaEnvio;

    @Column(name = "venta_id")
    private Long ventaId;
}
