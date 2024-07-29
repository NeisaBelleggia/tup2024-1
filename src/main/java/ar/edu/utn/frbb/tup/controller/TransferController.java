package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.service.CuentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TransferController {

    private final CuentaService cuentaService; // Servicio para gestionar operaciones relacionadas con cuentas

     // Constructor para inyectar el servicio de cuenta
    public TransferController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    // Método para manejar solicitudes de transferencia de fondos entre cuentas
    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(@RequestBody TransferRequest request) {
        try {
            String result = cuentaService.transfer(request.getCuentaOrigen(), request.getCuentaDestino(), request.getMonto(), request.getMoneda());
            return ResponseEntity.ok(new TransferResponse("EXITOSA", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new TransferResponse("FALLIDA", e.getMessage()));
        }
    }
}