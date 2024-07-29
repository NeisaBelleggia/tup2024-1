package ar.edu.utn.frbb.tup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ar.edu.utn.frbb.tup.persistence.TransaccionRepository;
import ar.edu.utn.frbb.tup.persistence.entity.TransaccionEntity;
import java.time.LocalDateTime;
import java.util.List;

// Clase de prueba para el repositorio de transacciones
public class TransaccionRepositoryTest {
    
    // Instancia del repositorio de transacciones que se va a probar
    private TransaccionRepository transaccionRepository;

    // Configuración inicial antes de cada prueba
    @BeforeEach
    public void setUp() {
        // Crear una nueva instancia del repositorio de transacciones
        transaccionRepository = new TransaccionRepository();
    }

    // Prueba del método addTransaccion
    @Test
    public void testAddTransaccion() {
        // Crear una entidad de transacción para la prueba
        TransaccionEntity transaccion = new TransaccionEntity(
            LocalDateTime.now(), "CREDITO", "Test", 100.0, 123456
        );
        
        // Añadir la transacción al repositorio
        transaccionRepository.addTransaccion(transaccion);

        // Buscar transacciones por ID de cuenta para verificar que la transacción ha sido añadida
        List<TransaccionEntity> transacciones = transaccionRepository.findByCuentaId(123456);

        // Verificar que se ha añadido una transacción
        assertEquals(1, transacciones.size()); // Verificar que el tamaño de la lista es 1
        assertEquals("Test", transacciones.get(0).getDescripcionBreve()); // Verificar la descripción breve de la transacción añadida
    }
}
