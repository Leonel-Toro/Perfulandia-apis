package com.perfulandia.cupones_api.dto;

public class CuponDTO {
    private String codigo;

    public CuponDTO() {
    }

    public CuponDTO(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
