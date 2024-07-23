package ar.edu.utn.frbb.tup.controller;

import java.util.List;

public class CuentaTransaccionesDTO {
    private String numeroCuenta;
    private List<TransaccionDTO> transacciones;

    // Getters y setters
    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }
    public List<TransaccionDTO> getTransacciones() { return transacciones; }
    public void setTransacciones(List<TransaccionDTO> transacciones) { this.transacciones = transacciones; }
}
