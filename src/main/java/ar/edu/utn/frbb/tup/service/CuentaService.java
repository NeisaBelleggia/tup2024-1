package ar.edu.utn.frbb.tup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.edu.utn.frbb.tup.persistence.CuentaRepository;
import ar.edu.utn.frbb.tup.persistence.TransaccionRepository;
import ar.edu.utn.frbb.tup.persistence.entity.CuentaEntity;
import ar.edu.utn.frbb.tup.persistence.entity.TransaccionEntity;
import ar.edu.utn.frbb.tup.controller.TransaccionDTO;
import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.exception.*;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Autowired
    private TransaccionService transaccionService;

    public void darDeAltaCuenta(Cuenta cuenta, long dni) throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, ClienteAlreadyExistsException {
        Optional<CuentaEntity> cuentaEntityOpt = cuentaRepository.findById(cuenta.getNumeroCuenta());
        if (cuentaEntityOpt.isPresent()) {
            throw new CuentaAlreadyExistsException("Cuenta ya existe con el número de cuenta: " + cuenta.getNumeroCuenta());
        }

        Cliente cliente = clienteService.buscarClientePorDni(dni);
        if (cliente == null) {
            throw new ClienteAlreadyExistsException("Cliente no encontrado con el DNI: " + dni);
        }

        for (Cuenta c : cliente.getCuentas()) {
            if (c.getTipoCuenta().equals(cuenta.getTipoCuenta())) {
                throw new TipoCuentaAlreadyExistsException("El cliente ya tiene una cuenta de tipo: " + cuenta.getTipoCuenta());
            }
        }

        cuenta.setTitular(cliente);
        CuentaEntity cuentaEntity = new CuentaEntity(cuenta);
        cuentaRepository.save(cuentaEntity);
        clienteService.agregarCuenta(cuenta, dni);
    }

    public boolean tipoDeCuentaSoportada(String tipoCuenta) {
        if (tipoCuenta == null) {
            return false;
        }
        switch (tipoCuenta) {
            case "CAJA_AHORRO":
            case "CUENTA_CORRIENTE":
                return true;
            default:
                return false;
        }
    }

    public void validarCuentaSoportada(TipoCuenta tipoCuenta) throws CuentaNotSupportedExcepcion {
        if (!tipoDeCuentaSoportada(tipoCuenta.toString())) {
            throw new CuentaNotSupportedExcepcion("Tipo de cuenta no soportado: " + tipoCuenta);
        }
    }

    public Optional<CuentaEntity> obtenerCuentaPorId(long cuentaId) {
        return cuentaRepository.findById(cuentaId);
    }

    public Optional<Cuenta> obtenerCuentaPorIdComoCuenta(long cuentaId) {
        Optional<CuentaEntity> cuentaEntityOpt = cuentaRepository.findById(cuentaId);
        return cuentaEntityOpt.map(ce -> ce.toCuenta(clienteService));
    }

    public String transfer(long cuentaOrigen, long cuentaDestino, double monto, String moneda) throws Exception {
        try {
            CuentaEntity origenEntity = cuentaRepository.findById(cuentaOrigen).orElse(null);
            CuentaEntity destinoEntity = cuentaRepository.findById(cuentaDestino).orElse(null);
    
            if (origenEntity == null) {
                throw new CuentaNotFoundException("La cuenta de origen no existe.");
            }
    
            if (monto <= 0) {
                throw new IllegalArgumentException("El monto debe ser mayor que cero.");
            }
    
            // Validar moneda de la transferencia
            validarMonedaCuentaOrigen(origenEntity.getMoneda(), moneda);
    
            if (destinoEntity == null) {
                // Simular transferencia externa
                boolean transferenciaExterna = simularTransferenciaExterna(cuentaDestino, monto, moneda);
                if (!transferenciaExterna) {
                    throw new Exception("La transferencia a la cuenta externa falló.");
                }
    
                // Actualizar balance de la cuenta de origen
                if (origenEntity.getBalance() < monto) {
                    throw new FondosInsuficientesException("Saldo insuficiente en la cuenta de origen.");
                }
                origenEntity.setBalance(origenEntity.getBalance() - monto);
                cuentaRepository.save(origenEntity);
    
                // Agregar la transacción para la cuenta de origen
                TransaccionDTO transaccionOrigen = new TransaccionDTO(
                    LocalDate.now(), "TRANSFERENCIA", "Transferencia a cuenta externa",
                    -monto, cuentaOrigen
                );
                transaccionService.agregarTransaccion(transaccionOrigen);
    
                return "Transferencia exitosa a cuenta externa.";
            }
    
            // Validar monedas de las cuentas
            TipoMoneda monedaOrigen = origenEntity.getMoneda();
            TipoMoneda monedaDestino = destinoEntity.getMoneda();
    
            if (!monedaOrigen.toString().equalsIgnoreCase(moneda) || !monedaDestino.toString().equalsIgnoreCase(moneda)) {
                throw new MonedaInvalidaException("Las monedas de las cuentas no coinciden.");
            }
    
            // Validar saldo suficiente
            if (origenEntity.getBalance() < monto) {
                throw new FondosInsuficientesException("Saldo insuficiente en la cuenta de origen.");
            }
    
            // Calcular comisión si aplica
            if (moneda.equalsIgnoreCase("pesos") && monto > 1000000) {
                monto += monto * 0.02;
            } else if (moneda.equalsIgnoreCase("dolares") && monto > 5000) {
                monto += monto * 0.005;
            }
    
            // Actualizar balances de ambas cuentas
            origenEntity.setBalance(origenEntity.getBalance() - monto);
            destinoEntity.setBalance(destinoEntity.getBalance() + monto);
    
            cuentaRepository.save(origenEntity);
            cuentaRepository.save(destinoEntity);
    
            // Agregar las transacciones
            TransaccionDTO transaccionOrigen = new TransaccionDTO(
                LocalDate.now(), "TRANSFERENCIA", "Transferencia a cuenta destino",
                -monto, cuentaOrigen
            );
            TransaccionDTO transaccionDestino = new TransaccionDTO(
                LocalDate.now(), "TRANSFERENCIA", "Transferencia recibida de cuenta origen",
                monto, cuentaDestino
            );
            transaccionService.agregarTransaccion(transaccionOrigen);
            transaccionService.agregarTransaccion(transaccionDestino);
    
            return "Transferencia exitosa.";
        } catch (MonedaInvalidaException e) {
            // Manejo de la excepción MonedaInvalidaException
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } catch (FondosInsuficientesException e) {
            // Manejo de la excepción FondosInsuficientesException
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            // Manejo de otras excepciones generales
            e.printStackTrace();
            throw new RuntimeException("Error en la transferencia: " + e.getMessage());
        }
    }    
    
    private void validarMoneda(TipoMoneda tipoMoneda) throws IllegalArgumentException {
        if (tipoMoneda != TipoMoneda.DOLARES && tipoMoneda != TipoMoneda.PESOS) {
            throw new IllegalArgumentException("Moneda inválida. Debe ser 'DOLARES' o 'PESOS'.");
        }
    }

    private void validarTipoCuenta(TipoCuenta tipoCuenta) throws IllegalArgumentException {
        if (tipoCuenta != TipoCuenta.CAJA_AHORRO && tipoCuenta != TipoCuenta.CUENTA_CORRIENTE) {
            throw new IllegalArgumentException("Tipo de cuenta inválido. Debe ser 'CAJA_AHORRO' o 'CUENTA_CORRIENTE'.");
        }
    }

    private void validarNumeroCuenta(long numeroCuenta) throws IllegalArgumentException {
        String numeroCuentaStr = String.valueOf(numeroCuenta);
        if (!numeroCuentaStr.matches("\\d+")) {
            throw new IllegalArgumentException("Número de cuenta inválido. Debe contener solo números.");
        }
    }

    private void validarMonedaCuentaOrigen(TipoMoneda monedaCuentaOrigen, String monedaTransferencia) throws MonedaInvalidaException {
        if (!monedaCuentaOrigen.toString().equalsIgnoreCase(monedaTransferencia)) {
            throw new MonedaInvalidaException("La moneda de la cuenta de origen no coincide con la moneda de la transferencia.");
        }
    }

    private boolean simularTransferenciaExterna(long cuentaDestino, double monto, String moneda) {
        // Aquí podrías implementar la lógica de simulación de una transferencia externa real
        return true;
    }

    public List<Cuenta> obtenerTodasLasCuentas() {
        List<CuentaEntity> cuentaEntities = cuentaRepository.findAll();
        return cuentaEntities.stream()
            .map(ce -> ce.toCuenta(clienteService))
            .collect(Collectors.toList());
    }

    public List<TransaccionDTO> obtenerTransacciones(long cuentaId) {
        try {
            Optional<CuentaEntity> cuentaEntityOpt = obtenerCuentaPorId(cuentaId);
            if (!cuentaEntityOpt.isPresent()) {
                throw new CuentaNotFoundException("La cuenta no existe.");
            }

            List<TransaccionEntity> transacciones = transaccionRepository.findByCuentaId(cuentaId);

            return transacciones.stream()
                    .map(te -> new TransaccionDTO(
                        te.getFecha().toLocalDate(), // Convertir LocalDateTime a LocalDate
                        te.getTipo(),
                        te.getDescripcionBreve(),
                        te.getMonto(),
                        te.getCuentaId()
                    ))
                    .collect(Collectors.toList());
        } catch (CuentaNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    // Método para validar el balance
    public void validarBalance(double balance) {
        BigDecimal bd = new BigDecimal(balance);
        bd = bd.setScale(1, RoundingMode.HALF_UP); // Redondea a 1 decimal

        // Verifica que el balance tenga al menos un decimal
        if (bd.scale() == 0) {
            throw new IllegalArgumentException("El balance debe tener al menos un decimal. Ejemplo válido: 100.0");
        }
    }
}
