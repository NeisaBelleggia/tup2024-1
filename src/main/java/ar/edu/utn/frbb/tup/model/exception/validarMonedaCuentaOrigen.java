package ar.edu.utn.frbb.tup.model.exception;

import ar.edu.utn.frbb.tup.model.TipoMoneda;

public class validarMonedaCuentaOrigen {
    private void validarMonedaCuentaOrigen(TipoMoneda monedaCuentaOrigen, String monedaTransferencia) throws MonedaInvalidaException {
    if (!monedaCuentaOrigen.toString().equalsIgnoreCase(monedaTransferencia)) {
        throw new MonedaInvalidaException("La moneda de la cuenta de origen no coincide con la moneda de la transferencia.");
    }
}

}
