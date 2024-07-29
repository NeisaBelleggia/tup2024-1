package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.controller.TransaccionDTO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

// Anotación que indica que esta clase es un repositorio para la persistencia de datos
@Repository
public class TransaccionDao {

    // Lista para almacenar transacciones en memoria
    private final List<TransaccionDTO> transacciones = new ArrayList<>();

    // Método para buscar transacciones por ID de cuenta
    public List<TransaccionDTO> findByCuentaId(long cuentaId) {
        // Crear una lista para almacenar las transacciones que coincidan con el ID de cuenta
        List<TransaccionDTO> result = new ArrayList<>();
        
        // Recorrer todas las transacciones almacenadas
        for (TransaccionDTO transaccion : transacciones) {
            // Verificar si el ID de la cuenta en la transacción coincide con el ID de cuenta proporcionado
            if (transaccion.getCuentaId() == cuentaId) {
                // Si coinciden, añadir la transacción a la lista de resultados
                result.add(transaccion);
            }
        }
        // Devolver la lista de transacciones que coinciden con el ID de cuenta
        return result;
    }

    // Método para guardar una nueva transacción
    public void save(TransaccionDTO transaccion) {
        // Añadir la transacción a la lista de transacciones
        transacciones.add(transaccion);
    }

    // Método para obtener todas las transacciones almacenadas
    public List<TransaccionDTO> getAllTransacciones() {
        // Devolver una nueva lista que contiene todas las transacciones actuales
        return new ArrayList<>(transacciones);
    }
}

