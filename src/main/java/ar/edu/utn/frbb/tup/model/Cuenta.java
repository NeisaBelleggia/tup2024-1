package ar.edu.utn.frbb.tup.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {
    private long numeroCuenta;
    private LocalDateTime fechaCreacion;
    private double balance;
    private TipoCuenta tipoCuenta;
    private Cliente titular;
    private TipoMoneda moneda;
    private List<Transaccion> transacciones;

    public Cuenta() {
        this.numeroCuenta = -1; // Número de cuenta no definido
        this.balance = 0;
        this.fechaCreacion = LocalDateTime.now();
        this.transacciones = new ArrayList<>();
    }

    public Cuenta(long numeroCuenta, double balance, TipoMoneda moneda, TipoCuenta tipoCuenta) {
        this.numeroCuenta = numeroCuenta;
        this.balance = balance;
        this.moneda = moneda;
        this.tipoCuenta = tipoCuenta;
        this.fechaCreacion = LocalDateTime.now();
        this.transacciones = new ArrayList<>();
    }

    public long getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
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

    public List<Transaccion> getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(List<Transaccion> transacciones) {
        this.transacciones = transacciones;
    }

    public void debitarDeCuenta(double cantidadADebitar) throws NoAlcanzaException, CantidadNegativaException {
        if (cantidadADebitar < 0) {
            throw new CantidadNegativaException();
        }
        if (balance < cantidadADebitar) {
            throw new NoAlcanzaException();
        }
        this.balance -= cantidadADebitar;
    }

    public void acreditarEnCuenta(double cantidadAcreditar) throws CantidadNegativaException {
        if (cantidadAcreditar < 0) {
            throw new CantidadNegativaException();
        }
        this.balance += cantidadAcreditar;
    }

    public void agregarTransaccion(Transaccion transaccion) {
        this.transacciones.add(transaccion);
    }
}
