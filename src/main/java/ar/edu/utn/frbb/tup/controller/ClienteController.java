package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    @Lazy
    private ClienteService clienteService; // Servicio inyectado para manejar operaciones relacionadas con Cliente

    @PostMapping
    public Cliente crearCliente(@RequestBody ClienteDto clienteDto) throws ClienteAlreadyExistsException {
        // Convertir ClienteDto a Cliente
        Cliente cliente = new Cliente();
        // Establece los atributos del cliente
        cliente.setDni(clienteDto.getDni()); 
        cliente.setNombre(clienteDto.getNombre()); 
        cliente.setApellido(clienteDto.getApellido());
        cliente.setFechaNacimiento(clienteDto.getFechaNacimiento()); 
        cliente.setTipoPersona(clienteDto.getTipoPersona()); 
        cliente.setBanco(clienteDto.getBanco()); 
        cliente.setFechaAlta(java.time.LocalDate.now()); 

        // Llama al servicio para dar de alta al cliente y retorna el cliente creado
        return clienteService.darDeAltaCliente(cliente);
    }

    @GetMapping("/{dni}")
    public ResponseEntity<Cliente> obtenerClientePorDni(@PathVariable long dni) {
        // Busca el cliente por DNI
        Cliente cliente = clienteService.buscarClientePorDni(dni);
        if (cliente == null) {
            // Si no se encuentra el cliente, retorna una respuesta con código 404 (No Encontrado)
            return ResponseEntity.notFound().build();
        }
        // Si el cliente se encuentra, retorna una respuesta con código 200 (OK) y el cliente
        return ResponseEntity.ok(cliente);
    }

    @GetMapping
    public List<Cliente> obtenerTodosClientes() {
        // Llama al servicio para obtener todos los clientes y retorna la lista de clientes
        return clienteService.obtenerTodosClientes();
    }
}





