package com.perfulandia.ventas_api.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "vendedores")
public class Vendedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idVendedor;
    @Column(name = "usuario_id")
    private Long idUsuario;
    private String sucursal;
    private BigDecimal metaMensual;
    @Transient
    @OneToMany(mappedBy = "vendedor")
    private List<Venta> ventas;

    public Long getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(Long idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public BigDecimal getMetaMensual() {
        return metaMensual;
    }

    public void setMetaMensual(BigDecimal metaMensual) {
        this.metaMensual = metaMensual;
    }

    public List<Venta> getVentas() {
        return ventas;
    }

    public void setVentas(List<Venta> ventas) {
        this.ventas = ventas;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }
}
