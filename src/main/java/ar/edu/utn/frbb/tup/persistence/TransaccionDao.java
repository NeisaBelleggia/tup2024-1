package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.controller.TransaccionDTO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TransaccionDao {

    private final List<TransaccionDTO> transacciones = new ArrayList<>();

    public List<TransaccionDTO> findByCuentaId(long cuentaId) {
        List<TransaccionDTO> result = new ArrayList<>();
        for (TransaccionDTO transaccion : transacciones) {
            if (transaccion.getCuentaId() == cuentaId) {
                result.add(transaccion);
            }
        }
        return result;
    }

    public void save(TransaccionDTO transaccion) {
        transacciones.add(transaccion);
    }
}
