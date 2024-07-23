package ar.edu.utn.frbb.tup.controller;

import java.time.LocalDate;
import java.util.Collection;

public class TransaccionDTO {
    private LocalDate fecha;
    private String tipo;
    private String descripcionBreve;
    private double monto;
    private long cuentaId; // Nuevo campo para el identificador de cuenta

    // Constructor vacío
    public TransaccionDTO() {}

    // Constructor con todos los campos
    public TransaccionDTO(LocalDate fecha, String tipo, String descripcionBreve, double monto, long cuentaId) {
        this.fecha = fecha;
        this.tipo = tipo;
        this.descripcionBreve = descripcionBreve;
        this.monto = monto;
        this.cuentaId = cuentaId;
    }

    // Getters y Setters
    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcionBreve() {
        return descripcionBreve;
    }

    public void setDescripcionBreve(String descripcionBreve) {
        this.descripcionBreve = descripcionBreve;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public long getCuentaId() { // Método getter para el identificador de cuenta
        return cuentaId;
    }

    public void setCuentaId(long cuentaId) { // Método setter para el identificador de cuenta
        this.cuentaId = cuentaId;
    }
}
