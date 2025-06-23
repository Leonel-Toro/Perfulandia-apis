package com.perfulandia.ventas_api.models;

import java.math.BigDecimal;
import java.sql.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "ventas")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idVenta;
    private Date fecha;
    @Transient
    private BigDecimal total;
    @Column(name = "cliente_id")
    private Integer idCliente;
    @ManyToOne()
    @JoinColumn(name = "vendedor_id")
    private Vendedor vendedor;

    public Venta() {
    }

    public Long getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Long idVenta) {
        this.idVenta = idVenta;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }
}