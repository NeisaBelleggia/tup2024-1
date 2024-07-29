package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteDao clienteDao;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testClienteMenor18Años() {
        Cliente clienteMenorDeEdad = new Cliente();
        clienteMenorDeEdad.setFechaNacimiento(LocalDate.of(2020, 2, 7));
        clienteMenorDeEdad.setDni(12345678);
        clienteMenorDeEdad.setNombre("Carlos");
        clienteMenorDeEdad.setApellido("Pérez");
        clienteMenorDeEdad.setTipoPersona("FISICA");
        clienteMenorDeEdad.setBanco("Banco");

        assertThrows(IllegalArgumentException.class, () -> clienteService.darDeAltaCliente(clienteMenorDeEdad));
    }

    @Test
    public void testClienteSuccess() {
    Cliente cliente = new Cliente();
    cliente.setFechaNacimiento(LocalDate.of(1978, 3, 25));
    cliente.setDni(29857643);
    cliente.setNombre("Juan");
    cliente.setApellido("Pérez");
    cliente.setTipoPersona(TipoPersona.FISICA.toString());
    cliente.setBanco("Banco");

    when(clienteDao.find(29857643, false)).thenReturn(null);

    try {
        clienteService.darDeAltaCliente(cliente);
    } catch (ClienteAlreadyExistsException e) {
        fail("No se esperaba ClienteAlreadyExistsException");
    }

    verify(clienteDao, times(1)).save(cliente);
    }   

    @Test
    public void testClienteAlreadyExistsException() throws ClienteAlreadyExistsException {
        Cliente clienteExistente = new Cliente();
        clienteExistente.setDni(26456437);
        clienteExistente.setNombre("Pepe");
        clienteExistente.setApellido("Rino");
        clienteExistente.setFechaNacimiento(LocalDate.of(1978, 3, 25));
        clienteExistente.setTipoPersona("FISICA");

        when(clienteDao.find(26456437, false)).thenReturn(clienteExistente);

        assertThrows(ClienteAlreadyExistsException.class, () -> clienteService.darDeAltaCliente(clienteExistente));
    }

    @Test
    public void testAgregarCuentaAClienteSuccess() throws TipoCuentaAlreadyExistsException, ClienteAlreadyExistsException {
        Cliente cliente = new Cliente();
        cliente.setDni(26456439);
        cliente.setNombre("Pepe");
        cliente.setApellido("Rino");
        cliente.setFechaNacimiento(LocalDate.of(1978, 3, 25));
        cliente.setTipoPersona("FISICA");

        Cuenta cuenta = new Cuenta();
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setBalance(500000);
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        when(clienteDao.find(26456439, false)).thenReturn(cliente);

        clienteService.agregarCuenta(cuenta, cliente.getDni());

        verify(clienteDao, times(1)).find(26456439, false);
        verify(clienteDao, times(1)).save(cliente);
        assertEquals(1, cliente.getCuentas().size());
        assertTrue(cliente.getCuentas().contains(cuenta));
    }

    @Test
    public void testAgregarMismaCuentaTipoMonedaFalla() throws TipoCuentaAlreadyExistsException, ClienteAlreadyExistsException {
        Cliente cliente = new Cliente();
        cliente.setDni(26456440);
        cliente.setNombre("Pepe");
        cliente.setApellido("Rino");
        cliente.setFechaNacimiento(LocalDate.of(1978, 3, 25));
        cliente.setTipoPersona("FISICA");

        Cuenta cuenta1 = new Cuenta();
        cuenta1.setMoneda(TipoMoneda.PESOS);
        cuenta1.setBalance(500000);
        cuenta1.setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        Cuenta cuenta2 = new Cuenta();
        cuenta2.setMoneda(TipoMoneda.PESOS);
        cuenta2.setBalance(200000);
        cuenta2.setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        when(clienteDao.find(26456440, false)).thenReturn(cliente);

        clienteService.agregarCuenta(cuenta1, cliente.getDni());

        assertThrows(TipoCuentaAlreadyExistsException.class, () -> clienteService.agregarCuenta(cuenta2, cliente.getDni()));
    }

    @Test
    public void testAgregarCuentasDiferentesTiposMonedas() throws TipoCuentaAlreadyExistsException, ClienteAlreadyExistsException {
        Cliente cliente = new Cliente();
        cliente.setDni(26456441);
        cliente.setNombre("Pepe");
        cliente.setApellido("Rino");
        cliente.setFechaNacimiento(LocalDate.of(1978, 3, 25));
        cliente.setTipoPersona("FISICA");

        Cuenta cuenta1 = new Cuenta();
        cuenta1.setMoneda(TipoMoneda.PESOS);
        cuenta1.setBalance(500000);
        cuenta1.setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        Cuenta cuenta2 = new Cuenta();
        cuenta2.setMoneda(TipoMoneda.DOLARES);
        cuenta2.setBalance(200000);
        cuenta2.setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        when(clienteDao.find(26456441, false)).thenReturn(cliente);

        clienteService.agregarCuenta(cuenta1, cliente.getDni());
        clienteService.agregarCuenta(cuenta2, cliente.getDni());

        verify(clienteDao, times(2)).save(cliente);
        assertEquals(2, cliente.getCuentas().size());
        assertTrue(cliente.getCuentas().contains(cuenta1));
        assertTrue(cliente.getCuentas().contains(cuenta2));
    }

    @Test
    public void testObtenerClientes() {
        Cliente cliente1 = new Cliente();
        cliente1.setDni(10000001);
        Cliente cliente2 = new Cliente();
        cliente2.setDni(10000002);

        when(clienteDao.findAll()).thenReturn(List.of(cliente1, cliente2));

        List<Cliente> clientes = clienteService.obtenerTodosClientes();

        assertEquals(2, clientes.size());
        assertTrue(clientes.contains(cliente1));
        assertTrue(clientes.contains(cliente2));
    }
}
