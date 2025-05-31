package com.clientes_api.models;

public class ApiResponse {
    //200 OK
    //400 ERROR
    //500 NO ENCONTRADO
    private int codigo;
    private String mensaje;

    public ApiResponse() {
    }

    public ApiResponse(int codigo, String mensaje) {
        this.codigo = codigo;
        this.mensaje = mensaje;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
