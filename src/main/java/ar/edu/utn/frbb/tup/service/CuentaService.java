package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNotFoundException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNotSupportedExcepcion;
import ar.edu.utn.frbb.tup.model.exception.FondosInsuficientesException;
import ar.edu.utn.frbb.tup.model.exception.MonedaInvalidaException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.entity.CuentaEntity;
import org.springframework.beans.factory.annotation.Autowired; // Línea agregada
import org.springframework.stereotype.Service; // Línea agregada

@Service // Línea agregada
public class CuentaService {

    @Autowired // Línea agregada
    private CuentaDao cuentaDao;

    @Autowired // Línea agregada
    private ClienteService clienteService;

    public CuentaService(CuentaDao cuentaDao, ClienteService clienteService) {
        this.cuentaDao = cuentaDao;
        this.clienteService = clienteService;
    }

    public CuentaService() {
    }

    public void darDeAltaCuenta(Cuenta cuenta, long dni) throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, ClienteAlreadyExistsException {
        Cuenta cuentaExistente = cuentaDao.find(cuenta.getNumeroCuenta());
        if (cuentaExistente != null) {
            throw new CuentaAlreadyExistsException("Cuenta ya existe con el número de cuenta: " + cuenta.getNumeroCuenta());
        }

        Cliente cliente = clienteService.buscarClientePorDni(dni);
        for (Cuenta c : cliente.getCuentas()) {
            if (c.getTipoCuenta() == cuenta.getTipoCuenta()) {
                throw new TipoCuentaAlreadyExistsException("El cliente ya tiene una cuenta de tipo: " + cuenta.getTipoCuenta());
            }
        }

        clienteService.agregarCuenta(cuenta, dni);
        cuentaDao.save(cuenta);
    }   

    public void validarCuentaSoportada(TipoCuenta tipoCuenta) throws CuentaNotSupportedExcepcion {
        if (!tipoDeCuentaSoportada(tipoCuenta.toString())) {
            throw new CuentaNotSupportedExcepcion("Tipo de cuenta no soportado: " + tipoCuenta);
        }
    }

    public Cuenta find(long id) {
        throw new UnsupportedOperationException("Unimplemented method 'find'");
    }

    public boolean tipoDeCuentaSoportada(String tipoCuenta) {
        if (tipoCuenta == null) {
            return false; 
        }
        switch (tipoCuenta) {
            case "CA$":
            case "CC$":
            case "CAU$S":
                return true;
            default:
                return false;
        }
    }   

    public String transfer(long cuentaOrigen, long cuentaDestino, double monto, String moneda) throws Exception {
        CuentaEntity origen = cuentaDao.findById(cuentaOrigen);
        CuentaEntity destino = cuentaDao.findById(cuentaDestino);

        if (origen == null) {
            throw new CuentaNotFoundException("La cuenta de origen no existe.");
        }

        if (destino == null) {
            // Simular la invocación a un servicio externo (Banelco)
            boolean transferenciaExterna = simularTransferenciaExterna(cuentaDestino, monto, moneda);
            if (!transferenciaExterna) {
                throw new Exception("La transferencia a la cuenta externa falló.");
            }
            origen.setBalance(origen.getBalance() - monto);
            cuentaDao.save(origen);
            return "Transferencia exitosa a cuenta externa.";
        }

        if (!origen.getMoneda().equals(moneda) || !destino.getMoneda().equals(moneda)) {
            throw new MonedaInvalidaException("Las monedas de las cuentas no coinciden.");
        }

        if (origen.getBalance() < monto) {
            throw new FondosInsuficientesException("Saldo insuficiente en la cuenta de origen.");
        }

        if (moneda.equals("pesos") && monto > 1000000) {
            monto += monto * 0.02;
        } else if (moneda.equals("dolares") && monto > 5000) {
            monto += monto * 0.005;
        }

        origen.setBalance(origen.getBalance() - monto);
        destino.setBalance(destino.getBalance() + monto);

        cuentaDao.save(origen);
        cuentaDao.save(destino);

        return "Transferencia exitosa.";
    }

    private boolean simularTransferenciaExterna(long cuentaDestino, double monto, String moneda) {
        // Simulación de una llamada a un servicio externo
        return true; // Asumimos que siempre es exitosa
    }
}
