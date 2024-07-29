package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNotSupportedExcepcion;
import ar.edu.utn.frbb.tup.persistence.CuentaRepository;
import ar.edu.utn.frbb.tup.persistence.TransaccionRepository;
import ar.edu.utn.frbb.tup.persistence.entity.CuentaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CuentaServiceTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private ClienteService clienteService;

    @Mock
    private TransaccionRepository transaccionRepository;

    @InjectMocks
    private CuentaService cuentaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCuentaExistente() throws CuentaAlreadyExistsException {
        Cuenta cuentaExistente = new Cuenta();
        cuentaExistente.setNumeroCuenta(12345);

        when(cuentaRepository.findById(12345L)).thenReturn(Optional.of(new CuentaEntity()));

        assertThrows(CuentaAlreadyExistsException.class, () -> cuentaService.darDeAltaCuenta(cuentaExistente, 12345678));
    }

    @Test
    public void testTipoDeCuentaSoportada() {
        assertTrue(cuentaService.tipoDeCuentaSoportada(TipoCuenta.CAJA_AHORRO.toString()));
        assertTrue(cuentaService.tipoDeCuentaSoportada(TipoCuenta.CUENTA_CORRIENTE.toString()));
        assertFalse(cuentaService.tipoDeCuentaSoportada(null));
        assertFalse(cuentaService.tipoDeCuentaSoportada("CUENTA_INVALIDA"));
    }

    @Test
    public void testCuentaNoSoportada() {
        CuentaService cuentaService = new CuentaService() {
            @Override
            public boolean tipoDeCuentaSoportada(String tipoCuenta) {
                // Simula que el tipo de cuenta no está soportado
                return false;
            }
        };

        TipoCuenta tipoValido = TipoCuenta.CAJA_AHORRO;
        TipoCuenta tipoValido2 = TipoCuenta.CUENTA_CORRIENTE;
        assertThrows(CuentaNotSupportedExcepcion.class, () -> {
            cuentaService.validarCuentaSoportada(tipoValido);
        });
    }


    @Test
    public void testClienteYaTieneCuentaDeEseTipo() throws TipoCuentaAlreadyExistsException, ClienteAlreadyExistsException, CuentaAlreadyExistsException {
        Cliente cliente = new Cliente();
        cliente.setDni(12345678);
        Cuenta cuentaExistente = new Cuenta();
        cuentaExistente.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cliente.addCuenta(cuentaExistente);

        Cuenta cuentaNueva = new Cuenta();
        cuentaNueva.setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        when(clienteService.buscarClientePorDni(12345678)).thenReturn(cliente);

        assertThrows(TipoCuentaAlreadyExistsException.class, () -> cuentaService.darDeAltaCuenta(cuentaNueva, 12345678));

        verify(clienteService, times(1)).buscarClientePorDni(12345678);
    }

    @Test
    public void testCuentaCreadaExitosamente() throws Exception, ClienteAlreadyExistsException, TipoCuentaAlreadyExistsException, CuentaAlreadyExistsException {
        Cuenta cuentaNueva = new Cuenta();
        cuentaNueva.setNumeroCuenta(12345);

        when(cuentaRepository.findById(12345L)).thenReturn(Optional.empty());

        Cliente cliente = new Cliente();
        cliente.setDni(12345678);
        when(clienteService.buscarClientePorDni(12345678)).thenReturn(cliente);

        doNothing().when(clienteService).agregarCuenta(any(Cuenta.class), anyLong());

        assertDoesNotThrow(() -> cuentaService.darDeAltaCuenta(cuentaNueva, 12345678));

        verify(cuentaRepository, times(1)).save(any(CuentaEntity.class));
        verify(clienteService, times(1)).agregarCuenta(cuentaNueva, 12345678);
    }

    @Test
    public void testCuentaCreadaExitosamenteConCuentaCorriente()
            throws Exception, ClienteAlreadyExistsException, TipoCuentaAlreadyExistsException, CuentaAlreadyExistsException {
        Cuenta cuentaNueva = new Cuenta();
        cuentaNueva.setNumeroCuenta(67890);
        cuentaNueva.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);

        when(cuentaRepository.findById(67890L)).thenReturn(Optional.empty());

        Cliente cliente = new Cliente();
        cliente.setDni(87654321);
        when(clienteService.buscarClientePorDni(87654321)).thenReturn(cliente);

        doNothing().when(clienteService).agregarCuenta(any(Cuenta.class), anyLong());

        assertDoesNotThrow(() -> cuentaService.darDeAltaCuenta(cuentaNueva, 87654321));

        verify(cuentaRepository, times(1)).save(any(CuentaEntity.class));
        verify(clienteService, times(1)).agregarCuenta(cuentaNueva, 87654321);
    }
}
