package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Transaccion;
import ar.edu.utn.frbb.tup.persistence.TransaccionRepository;
import ar.edu.utn.frbb.tup.persistence.entity.TransaccionEntity;
import ar.edu.utn.frbb.tup.controller.TransaccionDTO;
import ar.edu.utn.frbb.tup.persistence.TransaccionDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

// Clase de prueba para el servicio de transacciones
@SpringBootTest
public class TransaccionServiceTest {

    // Inyección de la instancia del servicio que se va a probar
    @InjectMocks
    private TransaccionService transaccionService;

    // Inyección de un mock del repositorio de transacciones
    @Mock
    private TransaccionDao transaccionDao;

    // Configuración inicial antes de cada prueba
    @BeforeEach
    public void setUp() {
        // Inicializa los mocks anotados con @Mock
        MockitoAnnotations.openMocks(this);
    }

    // Prueba del método obtenerTransacciones
    @Test
    public void testObtenerTransacciones() {
        // Configurar datos de prueba
        TransaccionDTO transaccion1 = new TransaccionDTO(
            LocalDate.of(2024, 7, 1), "CREDITO", "Transferencia entrante", 1500.00, 12345678
        );
        TransaccionDTO transaccion2 = new TransaccionDTO(
            LocalDate.of(2024, 7, 3), "DEBITO", "Compra Cooperativa Obrera", 500.00, 12345678
        );

        // Configurar el comportamiento del mock para devolver las transacciones de prueba
        when(transaccionDao.findByCuentaId(12345678)).thenReturn(List.of(transaccion1, transaccion2));

        // Llamar al método que se va a probar
        List<TransaccionDTO> transaccionesDTO = transaccionService.obtenerTransacciones(12345678);

        // Verificar los resultados
        assertEquals(2, transaccionesDTO.size()); // Verificar que el tamaño de la lista es 2
        assertEquals(LocalDate.of(2024, 7, 1), transaccionesDTO.get(0).getFecha()); // Verificar la fecha de la primera transacción
        assertEquals("CREDITO", transaccionesDTO.get(0).getTipo()); // Verificar el tipo de la primera transacción
        assertEquals("Transferencia entrante", transaccionesDTO.get(0).getDescripcionBreve()); // Verificar la descripción breve de la primera transacción
        assertEquals(1500.00, transaccionesDTO.get(0).getMonto()); // Verificar el monto de la primera transacción
    }

    // Prueba del método obtenerTodasTransacciones
    @Test
    public void testObtenerTodasTransacciones() {
        // Configurar datos de prueba
        TransaccionDTO transaccion1 = new TransaccionDTO(
            LocalDate.of(2024, 7, 1), "TRANSFERENCIA_ENTRANTE", "Transferencia entrante", 1500.00, 12345678
        );
        TransaccionDTO transaccion2 = new TransaccionDTO(
            LocalDate.of(2024, 7, 3), "TRANSFERENCIA_SALIENTE", "Transferencia saliente", 500.00, 12345678
        );

        // Configurar el comportamiento del mock para devolver todas las transacciones de prueba
        when(transaccionDao.getAllTransacciones()).thenReturn(List.of(transaccion1, transaccion2));

        // Llamar al método que se va a probar
        List<TransaccionDTO> transaccionesDTO = transaccionService.obtenerTodasTransacciones();

        // Verificar los resultados
        assertEquals(2, transaccionesDTO.size()); // Verificar que el tamaño de la lista es 2
        assertEquals(LocalDate.of(2024, 7, 1), transaccionesDTO.get(0).getFecha()); // Verificar la fecha de la primera transacción
        assertEquals("TRANSFERENCIA_ENTRANTE", transaccionesDTO.get(0).getTipo()); // Verificar el tipo de la primera transacción
        assertEquals("Transferencia entrante", transaccionesDTO.get(0).getDescripcionBreve()); // Verificar la descripción breve de la primera transacción
        assertEquals(1500.00, transaccionesDTO.get(0).getMonto()); // Verificar el monto de la primera transacción
    }
}
