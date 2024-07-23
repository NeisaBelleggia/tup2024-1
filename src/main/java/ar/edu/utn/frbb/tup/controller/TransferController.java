package ar.edu.utn.frbb.tup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ar.edu.utn.frbb.tup.service.ClienteService;
import ar.edu.utn.frbb.tup.service.CuentaService;

@RestController
@RequestMapping("/api")
public class TransferController {

    private final CuentaService cuentaService;
    private final ClienteService clienteService;

    @Autowired
    public TransferController(CuentaService cuentaService, ClienteService clienteService) {
        this.cuentaService = cuentaService;
        this.clienteService = clienteService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request) {
        try {
            String result = cuentaService.transfer(request.getCuentaOrigen(), request.getCuentaDestino(), request.getMonto(), request.getMoneda());
            return ResponseEntity.ok(new TransferResponse("EXITOSA", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new TransferResponse("FALLIDA", e.getMessage()));
        }
    }
}
