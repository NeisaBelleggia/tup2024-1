package ar.edu.utn.frbb.tup.controller;

public class TransferResponse {
    private String estado; // Estado de la transferencia (por ejemplo, "EXITOSA" o "FALLIDA")
    private String mensaje; // Mensaje adicional sobre el resultado de la transferencia

    // Constructor con parámetros para inicializar el estado y el mensaje
    public TransferResponse(String estado, String mensaje) {
        this.estado = estado;
        this.mensaje = mensaje;
    }

    // Getter para obtener el estado de la transferencia
    public String getEstado() {
        return estado;
    }

    // Setter para establecer el estado de la transferencia
    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Getter para obtener el mensaje adicional sobre la transferencia
    public String getMensaje() {
        return mensaje;
    }

    // Setter para establecer el mensaje adicional sobre la transferencia
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}


