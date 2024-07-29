package ar.edu.utn.frbb.tup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ar.edu.utn.frbb.tup.service.CuentaService;
import ar.edu.utn.frbb.tup.service.TransaccionService;
import ar.edu.utn.frbb.tup.persistence.entity.CuentaEntity;
import ar.edu.utn.frbb.tup.controller.TransaccionDTO;
import ar.edu.utn.frbb.tup.controller.CuentaTransaccionesDTO;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TransaccionController {

    @Autowired
    private CuentaService cuentaService; // Servicio para gestionar las operaciones relacionadas con las cuentas

    @Autowired
    private TransaccionService transaccionService; // Servicio para gestionar las transacciones

    @GetMapping("/transacciones")
    public List<TransaccionDTO> obtenerTodasLasTransacciones() {
        // Obtiene la lista de todas las transacciones a través del servicio y la retorna
        return transaccionService.obtenerTodasTransacciones();
    }

    @GetMapping("/cuenta/{cuentaId}/transacciones")
    public ResponseEntity<CuentaTransaccionesDTO> obtenerTransacciones(@PathVariable long cuentaId) {
        // Busca la cuenta usando el ID proporcionado
        Optional<CuentaEntity> cuentaOpt = cuentaService.obtenerCuentaPorId(cuentaId);
        if (!cuentaOpt.isPresent()) {
            // Si la cuenta no existe, retorna una respuesta con código 404 (No Encontrado)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        try {
            // Obtiene las transacciones asociadas al ID de cuenta
            List<TransaccionDTO> transaccionesDTO = transaccionService.obtenerTransacciones(cuentaId);

            // Crea un DTO que contiene el número de cuenta y las transacciones asociadas
            CuentaTransaccionesDTO cuentaTransaccionesDTO = new CuentaTransaccionesDTO();
            cuentaTransaccionesDTO.setNumeroCuenta(cuentaId);
            cuentaTransaccionesDTO.setTransacciones(transaccionesDTO);

            // Retorna la información de la cuenta con las transacciones asociadas con código 200 (OK)
            return ResponseEntity.ok(cuentaTransaccionesDTO);
        } catch (Exception e) {
            // Maneja cualquier excepción y retorna una respuesta con código 500 (Error Interno del Servidor)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/transacciones")
    public ResponseEntity<String> agregarTransaccion(@RequestBody TransaccionDTO transaccionDTO) {
        try {
            // Llama al servicio para agregar la transacción usando el DTO recibido
            transaccionService.agregarTransaccion(transaccionDTO);
            // Retorna una respuesta con código 201 (Creado) si la transacción se agrega exitosamente
            return ResponseEntity.status(HttpStatus.CREATED).body("Transacción agregada exitosamente.");
        } catch (Exception e) {
            // Maneja cualquier excepción y retorna una respuesta con código 500 (Error Interno del Servidor)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al agregar la transacción.");
        }
    }
}
