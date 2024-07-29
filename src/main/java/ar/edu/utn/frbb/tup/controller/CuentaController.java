package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.service.ClienteService;
import ar.edu.utn.frbb.tup.service.CuentaService;
import ar.edu.utn.frbb.tup.service.TransaccionService; 
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuenta")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService; // Servicio para gestionar las operaciones relacionadas con las cuentas

    @Autowired
    private ClienteService clienteService; // Servicio para gestionar las operaciones relacionadas con los clientes

    @Autowired
    private TransaccionService transaccionService; // Servicio para gestionar las transacciones (actualmente no se usa en este controlador)

    @PostMapping("/crearCuenta")
    public ResponseEntity<String> crearCuenta(@RequestBody CuentaDto cuentaDto) {
        try {
            // Busca el cliente usando el DNI proporcionado en el DTO
            Cliente cliente = clienteService.buscarClientePorDni(cuentaDto.getTitularDni());
            if (cliente == null) {
                // Si no se encuentra el cliente, retorna un error con código 400 (Bad Request)
                return ResponseEntity.badRequest().body("Cliente no encontrado");
            }

            // Crea una nueva instancia de Cuenta y establece sus atributos
            Cuenta cuenta = new Cuenta();
            cuenta.setNumeroCuenta(cuentaDto.getNumeroCuenta()); // Establece el número de cuenta
            cuenta.setFechaCreacion(cuentaDto.getFechaCreacion().atStartOfDay()); // Establece la fecha de creación (con tiempo a medianoche)
            cuenta.setBalance(cuentaDto.getBalance()); // Establece el saldo de la cuenta
            cuenta.setTipoCuenta(TipoCuenta.valueOf(cuentaDto.getTipoCuenta())); // Establece el tipo de cuenta (se convierte de String a TipoCuenta)
            cuenta.setMoneda(TipoMoneda.valueOf(cuentaDto.getMoneda().toUpperCase())); // Establece el tipo de moneda (se convierte de String a TipoMoneda)
            cuenta.setTitular(cliente); // Establece el titular de la cuenta como el cliente encontrado

            // Llama al servicio para dar de alta la cuenta y retorna una respuesta exitosa
            cuentaService.darDeAltaCuenta(cuenta, cliente.getDni());
            return ResponseEntity.ok("Cuenta creada exitosamente.");
        } catch (CuentaAlreadyExistsException | TipoCuentaAlreadyExistsException | ClienteAlreadyExistsException e) {
            // Maneja excepciones específicas y retorna un error con el mensaje de la excepción
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalArgumentException e) {
            // Maneja errores en la conversión de datos (por ejemplo, valores inválidos para TipoCuenta o TipoMoneda)
            return ResponseEntity.badRequest().body("Error en los datos proporcionados: " + e.getMessage());
        }
    }

    @GetMapping("/cuentas")
    public ResponseEntity<List<Cuenta>> obtenerTodasLasCuentas() {
        try {
            // Obtiene la lista de todas las cuentas del servicio
            List<Cuenta> cuentas = cuentaService.obtenerTodasLasCuentas();
            if (cuentas.isEmpty()) {
                // Si no hay cuentas, retorna una respuesta sin contenido con código 204 (No Content)
                return ResponseEntity.noContent().build();
            }
            // Retorna la lista de cuentas con una respuesta exitosa con código 200 (OK)
            return ResponseEntity.ok(cuentas);
        } catch (Exception e) {
            // Maneja cualquier otra excepción no específica y retorna un error interno del servidor con código 500 (Internal Server Error)
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
