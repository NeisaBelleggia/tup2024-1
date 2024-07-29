package ar.edu.utn.frbb.tup.controller;

import java.util.List;

public class CuentaTransaccionesDTO {
    private long numeroCuenta;
    private List<TransaccionDTO> transacciones;

    // Getters y Setters
    public long getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public List<TransaccionDTO> getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(List<TransaccionDTO> transacciones) {
        this.transacciones = transacciones;
    }
}
