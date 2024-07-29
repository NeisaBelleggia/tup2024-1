package ar.edu.utn.frbb.tup.controller;

public class TransferRequest {
    private long cuentaOrigen; // ID de la cuenta desde la cual se realizará la transferencia
    private long cuentaDestino; // ID de la cuenta a la cual se transferirán los fondos
    private double monto; // Monto de dinero a transferir
    private String moneda; // Tipo de moneda en la que se realiza la transferencia

    // Getter para obtener el ID de la cuenta de origen
    public long getCuentaOrigen() {
        return cuentaOrigen;
    }

    // Setter para establecer el ID de la cuenta de origen
    public void setCuentaOrigen(long cuentaOrigen) {
        this.cuentaOrigen = cuentaOrigen;
    }

    // Getter para obtener el ID de la cuenta de destino
    public long getCuentaDestino() {
        return cuentaDestino;
    }

    // Setter para establecer el ID de la cuenta de destino
    public void setCuentaDestino(long cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }

    // Getter para obtener el monto de la transferencia
    public double getMonto() {
        return monto;
    }

    // Setter para establecer el monto de la transferencia
    public void setMonto(double monto) {
        this.monto = monto;
    }

    // Getter para obtener el tipo de moneda de la transferencia
    public String getMoneda() {
        return moneda;
    }

    // Setter para establecer el tipo de moneda de la transferencia
    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
}
