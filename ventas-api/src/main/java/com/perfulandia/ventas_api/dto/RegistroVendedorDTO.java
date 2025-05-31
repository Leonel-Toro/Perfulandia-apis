package com.perfulandia.ventas_api.dto;

import java.math.BigDecimal;

public class RegistroVendedorDTO {
    private String sucursal;
    private BigDecimal metaMensual;
    private String correo;
    private String password;

    public RegistroVendedorDTO() {
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
