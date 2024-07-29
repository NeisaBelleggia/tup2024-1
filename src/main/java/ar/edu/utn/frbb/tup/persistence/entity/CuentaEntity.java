package ar.edu.utn.frbb.tup.persistence.entity;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.Transaccion;
import ar.edu.utn.frbb.tup.service.ClienteService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CuentaEntity {
    private long numeroCuenta;
    private LocalDate fechaCreacion; // Cambiado a LocalDate
    private double balance;
    private TipoCuenta tipoCuenta;
    private TipoMoneda moneda;
    private Cliente titular;
    private List<TransaccionEntity> transacciones = new ArrayList<>();

    // Constructor vacío
    public CuentaEntity() {
    }

    // Constructor para convertir Cuenta a CuentaEntity
    public CuentaEntity(Cuenta cuenta) {
        this.numeroCuenta = cuenta.getNumeroCuenta();
        this.fechaCreacion = cuenta.getFechaCreacion().toLocalDate(); // Convertir LocalDateTime a LocalDate
        this.balance = cuenta.getBalance();
        this.tipoCuenta = cuenta.getTipoCuenta();
        this.moneda = cuenta.getMoneda();
        this.titular = cuenta.getTitular();
        this.transacciones = cuenta.getTransacciones().stream()
                .map(t -> new TransaccionEntity(
                    t.getFecha().atStartOfDay(),  // Convertir LocalDate a LocalDateTime
                    t.getTipo(),
                    t.getDescripcionBreve(),
                    t.getMonto(),
                    t.getCuentaId()
                ))
                .collect(Collectors.toList());
    }

    // Getters y Setters
    public long getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public TipoMoneda getMoneda() {
        return moneda;
    }

    public void setMoneda(TipoMoneda moneda) {
        this.moneda = moneda;
    }

    public Cliente getTitular() {
        return titular;
    }

    public void setTitular(Cliente titular) {
        this.titular = titular;
    }

    public List<TransaccionEntity> getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(List<TransaccionEntity> transacciones) {
        this.transacciones = transacciones;
    }

    public void addTransaccion(TransaccionEntity transaccion) {
        this.transacciones.add(transaccion);
    }

    public void removeTransaccion(TransaccionEntity transaccion) {
        this.transacciones.remove(transaccion);
    }

    public Cuenta toCuenta(ClienteService clienteService) {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(this.numeroCuenta);
        cuenta.setFechaCreacion(this.fechaCreacion.atStartOfDay()); // Convertir LocalDate a LocalDateTime
        cuenta.setBalance(this.balance);
        cuenta.setTipoCuenta(this.tipoCuenta);
        cuenta.setMoneda(this.moneda);
        cuenta.setTitular(this.titular);

        List<Transaccion> transacciones = this.transacciones.stream()
                .map(te -> new Transaccion(te.getFecha().toLocalDate(), te.getTipo(), te.getDescripcionBreve(), te.getMonto(), te.getCuentaId()))
                .collect(Collectors.toList());
        cuenta.setTransacciones(transacciones);

        return cuenta;
    }
}




